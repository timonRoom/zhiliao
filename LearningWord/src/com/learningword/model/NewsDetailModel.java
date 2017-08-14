package com.learningword.model;

public class NewsDetailModel {
	private String body;
	private String imageuri;
	private String title;
	@Override
	public String toString() {
		return "NewsDetailModel [body=" + body + ", imageuri=" + imageuri + ", title=" + title + "]";
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getImageuri() {
		return imageuri;
	}
	public void setImageuri(String imageuri) {
		this.imageuri = imageuri;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
