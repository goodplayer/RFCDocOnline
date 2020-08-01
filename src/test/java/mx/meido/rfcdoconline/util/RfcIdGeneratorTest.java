package mx.meido.rfcdoconline.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RfcIdGeneratorTest {
    @Test
    public void testRfcId() {
        assertEquals("rfc0001", RfcIdGenerator.rfcId(1));
        assertEquals("rfc0999", RfcIdGenerator.rfcId(999));
        assertEquals("rfc1999", RfcIdGenerator.rfcId(1999));
    }

    @Test
    public void testAlterRfcId() {
        List<String> idlist = RfcIdGenerator.alterIdList("rfc0001");
        assertFalse(idlist.isEmpty());
        idlist = RfcIdGenerator.alterIdList("rfc1001");
        assertTrue(idlist.isEmpty());
    }
}
