package mx.meido.rfcdoconline.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mx.meido.rfcdoconline.entity.RfcDocEntity;
import mx.meido.rfcdoconline.entity.RfcDocRepository;
import mx.meido.rfcdoconline.service.DocObtainService;
import mx.meido.rfcdoconline.service.RfcDoc;
import mx.meido.rfcdoconline.service.ScanNewRfcService;
import mx.meido.rfcdoconline.service.ScanRfcResponse;
import mx.meido.rfcdoconline.util.RfcIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ScanNewRfcServiceImpl implements ScanNewRfcService {

    @Autowired
    private RfcDocRepository rfcDocRepository;

    @Autowired
    private DocObtainService docObtainService;

    @Override
    @Transactional
    public ScanRfcResponse scanRfcByPage(int page) {
        int start = (page - 1) * 100 + 1;
        int end = start + 99;

        List<String> idList = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            idList.add(RfcIdGenerator.rfcId(i));
        }

        Map<String, RfcDoc> rfcDocMap = docObtainService.batchObtainDocs(idList);
        for (RfcDoc rfcDoc : rfcDocMap.values()) {
            rfcDocRepository.save(rfcDoc.toEntity());
        }

        return new ScanRfcResponse().setIdList(rfcDocMap.keySet().stream().sorted().collect(Collectors.toList()));
    }

    @Override
    public ScanRfcResponse checkLocalRfcByPage(int page) {
        int start = (page - 1) * 100 + 1;
        int end = start + 99;
        String endId = RfcIdGenerator.rfcId(end);

        List<RfcDocEntity> rfcDocEntityList = rfcDocRepository.queryRfcPage(RfcIdGenerator.rfcId(start));
        if (rfcDocEntityList == null) {
            rfcDocEntityList = Collections.emptyList();
        }

    List<String> idList = new ArrayList<>();
        for (RfcDocEntity rfcDocEntity : rfcDocEntityList) {
        if (rfcDocEntity.getRfcId().compareTo(endId) > 0) {
            continue;
        }
        idList.add(rfcDocEntity.getRfcId());
    }

        return new ScanRfcResponse().setIdList(idList);
}

    @Override
    public ScanRfcResponse findAllMissingItems() {
        //TODO
        return null;
    }
}
