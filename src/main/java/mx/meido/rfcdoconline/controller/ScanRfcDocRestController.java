package mx.meido.rfcdoconline.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mx.meido.rfcdoconline.entity.RfcDocEntity;
import mx.meido.rfcdoconline.entity.RfcDocRepository;
import mx.meido.rfcdoconline.service.ScanNewRfcService;
import mx.meido.rfcdoconline.service.ScanRfcResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/scan")
@Slf4j
public class ScanRfcDocRestController {

    @Autowired
    private ScanNewRfcService scanNewRfcService;

    @GetMapping("/full_init_database")
    public Map<String, String> fullInitDatabase() {

        for (int i = 1; i < Integer.MAX_VALUE; i++) {

            ScanRfcResponse checkResponse = scanNewRfcService.checkLocalRfcByPage(i);
            if (checkResponse.getIdList() != null && !checkResponse.getIdList().isEmpty()) {
                LOGGER.info("check page [{}]. already scanned.", i);
                continue;
            }

            ScanRfcResponse scanRfcResponse = scanNewRfcService.scanRfcByPage(i);

            if (scanRfcResponse.getIdList() == null || scanRfcResponse.getIdList().isEmpty()) {
                LOGGER.info("query finished for page: " + i);
                break;
            }

            String str = null;
            try {
                str = new ObjectMapper().writeValueAsString(scanRfcResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            System.out.println(str);
        }


        return new HashMap<String, String>() {{
            put("name", "done");
        }};
    }
}
