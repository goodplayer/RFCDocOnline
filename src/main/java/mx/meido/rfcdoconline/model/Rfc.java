package mx.meido.rfcdoconline.model;

public class Rfc {
	private int id;
	private String title;
	private String author;
	private String origInfo;
	
	public int getId() {
		return id;
	}
	public String getAuthor() {
		return author;
	}
	public String getOrigInfo() {
		return origInfo;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setOrigInfo(String origInfo) {
		this.origInfo = origInfo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
