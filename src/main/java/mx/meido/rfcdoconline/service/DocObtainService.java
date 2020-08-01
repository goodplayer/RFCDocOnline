package mx.meido.rfcdoconline.service;

import java.util.List;
import java.util.Map;

public interface DocObtainService {
    Map<String, RfcDoc> batchObtainDocs(List<String> rfcIds);

    RfcDoc queryDoc(String rfcId);
}
