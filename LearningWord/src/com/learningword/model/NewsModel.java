package com.learningword.model;

import java.io.Serializable;

public class NewsModel implements Serializable {
	private String id;
	public boolean isclicked= false;
	@Override
	public String toString() {
		return "NewsModel [id=" + id + ", imagerurl=" + imagerurl + ", titel=" + titel + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImagerurl() {
		return imagerurl;
	}
	public void setImagerurl(String imagerurl) {
		this.imagerurl = imagerurl;
	}
	public String getTitel() {
		return titel;
	}
	public void setTitel(String titel) {
		this.titel = titel;
	}
	private String imagerurl;
	private String titel;
}
