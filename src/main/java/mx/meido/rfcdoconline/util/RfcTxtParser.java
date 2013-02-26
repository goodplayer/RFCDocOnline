package mx.meido.rfcdoconline.util;

import java.util.ArrayList;
import java.util.List;

import mx.meido.rfcdoconline.model.RfcPage;

/**
 * Txt版Rfc文档特点: <br>
 * 每页55行(当把每页最后的0x0A算为下一页时为56行),每行71个字符+1个换行符('\n')<br>
 * 每页结束后有: 0x0C 0x0A<br>
 * 文档首页有58行,即开头多出5个0x0A<br>
 * <br>
 * 分页特殊方法: 每页最后都有页码, 通过']'+'\n'+0x0C+'\n'来确定一页<br>
 * 即确定0x0C+'\n'
 * 
 * @author sun hao
 * 
 */
public class RfcTxtParser {
	public List<RfcPage> parseRfcBytes(byte[] data, int curRfcId) {
		try {
			List<RfcPage> list = new ArrayList<RfcPage>();

			int pageNo = 1;

			int curpos = 0;
			int previousStart = 0;
			while (curpos < (data.length)) {
				if ( (curpos < (data.length - 1))
						&& (data[curpos] == 0x0c && data[curpos + 1] == '\n') ) {
					RfcPage rp = new RfcPage();
					rp.setId(curRfcId);
					rp.setPageNum(pageNo);
					rp.setContent(new String(data, previousStart, curpos - previousStart));
					list.add(rp);

					curpos += 2;
					previousStart = curpos;
					pageNo++;
				} else {
					curpos++;
				}
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
