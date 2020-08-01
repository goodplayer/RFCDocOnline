package mx.meido.rfcdoconline.service;

public interface ScanNewRfcService {
    /**
     * query rfc doc by page number. 100 docs/page
     */
    ScanRfcResponse scanRfcByPage(int page);

    ScanRfcResponse checkLocalRfcByPage(int page);

    ScanRfcResponse findAllMissingItems();
}
