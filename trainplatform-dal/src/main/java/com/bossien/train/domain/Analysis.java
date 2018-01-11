package com.bossien.train.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;

@XStreamAlias("analysis")
public class Analysis {

    @XStreamAlias("content")
    private String content;
    @XStreamAlias("images")
    private ArrayList<FileInfo> images;
    
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ArrayList<FileInfo> getImages() {
		return images;
	}
	public void setImages(ArrayList<FileInfo> images) {
		this.images = images;
	}
	
}
