package mx.meido.rfcdoconline.util;

import java.util.Collections;
import java.util.List;

public class RfcIdGenerator {
    public static String rfcId(int id) {
        if (id < 1000) {
            return "rfc" + String.format("%04d", id);
        } else {
            return "rfc" + id;
        }
    }

    public static List<String> alterIdList(String id) {
        String n = id.substring(3);
        int num = Integer.parseInt(n);
        if (num < 1000) {
            return Collections.singletonList("rfc" + num);
        } else {
            return Collections.emptyList();
        }
    }
}
