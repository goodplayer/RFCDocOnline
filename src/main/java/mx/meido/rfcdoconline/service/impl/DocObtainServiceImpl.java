package mx.meido.rfcdoconline.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mx.meido.rfcdoconline.service.DocObtainRunningContext;
import mx.meido.rfcdoconline.service.DocObtainService;
import mx.meido.rfcdoconline.service.RfcDoc;
import mx.meido.rfcdoconline.service.RfcMetadata;
import mx.meido.rfcdoconline.util.RfcIdGenerator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DocObtainServiceImpl implements DocObtainService {
    private static final String RFC_HTTP_PREFIX = "https://www.rfc-editor.org/rfc/";
//    private static final String RFC_HTTP_PREFIX = "https://www.ietf.org/rfc/";

    private static final String RFC_CONTENT_SUFFIX = ".txt";
    private static final String RFC_METADATA_SUFFIX = ".json";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Proxy proxy;

    static {
        proxy = Proxy.NO_PROXY;
//        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1081));

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static final AtomicReference<DocObtainRunningContext> context = new AtomicReference<>(null);
    private static final ReentrantLock lock = new ReentrantLock();

    @Override
    public Map<String, RfcDoc> batchObtainDocs(List<String> rfcIds) {
        boolean locked = false;
        try {
            DocObtainRunningContext ctx = new DocObtainRunningContext();
            ctx.setCurrentTask(rfcIds);

            locked = lock.tryLock(10, TimeUnit.SECONDS);
            context.set(ctx);

            return rfcIds.stream().map(p -> {
                LOGGER.info("querying doc: " + p);
                RfcDoc r = queryDoc(p);
                LOGGER.info("queried doc: " + r);

                // deal with rfc1 from rfc0001
                List<String> alterIds = RfcIdGenerator.alterIdList(p);
                if (!alterIds.isEmpty()) {
                    for (String id : alterIds) {
                        LOGGER.info("querying doc using alter id: " + p);
                        r = queryDoc(id);
                        LOGGER.info("queried doc using alter id: " + r);
                        if (!r.isNotFound()) {
                            break;
                        }
                    }
                }

                // fill origin rfc id
                r.setRfcId(p);

                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    boolean omit = Thread.interrupted();
                    LOGGER.error("query sleep interrupted", e);
                }

                return r;
            }).filter(p -> !p.isNotFound()).collect(Collectors.toMap(
                    RfcDoc::getRfcId, p -> p
            ));
        } catch (InterruptedException e) {
            boolean omit = Thread.interrupted();
            LOGGER.error("try lock failed", e);
            return Collections.emptyMap();
        } finally {
            if (locked) {
                context.set(null);
                lock.unlock();
            }
        }
    }

    @Override
    public RfcDoc queryDoc(String rfcId) {
        Connection.Response contentResponse = null;
        Connection.Response metadataResponse = null;
        try {
            contentResponse = Jsoup.connect(concat(RFC_HTTP_PREFIX, rfcId, RFC_CONTENT_SUFFIX))
                    .proxy(proxy)
                    .method(Connection.Method.GET)
                    .ignoreHttpErrors(true)
                    .maxBodySize(10*1024*1024)
                    .execute();
            byte[] content = contentResponse.bodyAsBytes();
            if (contentResponse.statusCode() != 200) {
                LOGGER.warn("retrieve document error: " + rfcId);
                return new RfcDoc(true);
            }

            metadataResponse = Jsoup.connect(concat(RFC_HTTP_PREFIX, rfcId, RFC_METADATA_SUFFIX))
                    .proxy(proxy)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .ignoreHttpErrors(true)
                    .maxBodySize(10*1024*1024)
                    .execute();
            byte[] metadata = metadataResponse.bodyAsBytes();
            if (metadataResponse.statusCode() != 200) {
                LOGGER.warn("retrieve document metadata error: " + rfcId);
                return new RfcDoc(true);
            }
            RfcMetadata rfcMetadata = objectMapper.readValue(metadata, RfcMetadata.class);
            rfcMetadata.standardize();

            RfcDoc rfcDoc = new RfcDoc();
            rfcDoc.setRfcId(rfcId);
            rfcDoc.setRfcContent(content);
            rfcDoc.getObsoletes().addAll(rfcMetadata.getObsoletes());
            rfcDoc.getUpdates().addAll(rfcMetadata.getUpdates());
            return rfcDoc;
        } catch (IOException e) {
            LOGGER.error("queyr doc error for id:" + rfcId, e);
            throw new RuntimeException("cannot query document for id:" + rfcId, e);
        } finally {
            if (contentResponse != null) {
                contentResponse.bodyAsBytes();
            }
            if (metadataResponse != null) {
                metadataResponse.bodyAsBytes();
            }
        }
    }

    private static String concat(String url, String id, String suffix) {
        String u = url + id + suffix;
        LOGGER.info("concat url: " + u);
        return u;
    }
}
