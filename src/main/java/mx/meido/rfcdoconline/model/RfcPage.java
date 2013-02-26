package mx.meido.rfcdoconline.model;

public class RfcPage {
	private int id;
	private int pageNum;
	private String content;
	
	public int getId() {
		return id;
	}
	public int getPageNum() {
		return pageNum;
	}
	public String getContent() {
		return content;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
