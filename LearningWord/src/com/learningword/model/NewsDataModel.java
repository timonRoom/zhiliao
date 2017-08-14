package com.learningword.model;

public class NewsDataModel {
	private String title;
	private String url;
	public  boolean isclicked = false;
	private String author_name;
	private String data;
	private String imageurl;
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuthor_name() {
		return author_name;
	}
	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "NewsDataModel [title=" + title + ", url=" + url + ", author_name=" + author_name + ", data=" + data
				+ "]";
	}
	
}
