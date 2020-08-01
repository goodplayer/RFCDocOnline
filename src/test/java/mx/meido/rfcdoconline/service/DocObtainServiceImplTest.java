package mx.meido.rfcdoconline.service;

import mx.meido.rfcdoconline.service.impl.DocObtainServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

public class DocObtainServiceImplTest {
    @Test
    @Disabled
    public void testQueryDoc() {
        RfcDoc rfcDoc = new DocObtainServiceImpl()
                .queryDoc("rfc8314");
        System.out.println(rfcDoc);
        RfcDoc rfcDoc2 = new DocObtainServiceImpl()
                .queryDoc("rfc4409");
        System.out.println(rfcDoc2);
    }

    @Test
    @Disabled
    public void testBatchQueryDoc() {
        Map<String, RfcDoc> docMap = new DocObtainServiceImpl()
                .batchObtainDocs(Arrays.asList("rfc4409", "rfc8314"));
        System.out.println(docMap);
    }
}
