package mx.meido.rfcdoconline.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import mx.meido.rfcdoconline.model.RfcPage;

import org.junit.Assert;
import org.junit.Test;

public class RfcTxtParserTest {
	@Test
	public void testParseRfcBytes(){
		File f1 = new File("R:/rfc5661.txt");
		List<RfcPage> l = null;
		try {
			FileInputStream fis = new FileInputStream(f1);
			byte[] buf = new byte[fis.available()];
			fis.read(buf);
			RfcTxtParser parser = new RfcTxtParser();
			l = parser.parseRfcBytes(buf, 5661);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(617, l.size());
		for(int i = 0; i<l.size(); i++){
			Assert.assertEquals(i+1, l.get(i).getPageNum());
			Assert.assertEquals(5661, l.get(i).getId());
		}
	}
}
