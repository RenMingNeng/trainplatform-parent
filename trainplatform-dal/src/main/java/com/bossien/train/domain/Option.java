package com.bossien.train.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("option")
public class Option {

	@XStreamAlias("item")
    private String item;
	@XStreamAlias("itemDesc")
    private String itemDesc;
	@XStreamAlias("fileInfo")
    private FileInfo fileInfo;
    
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public FileInfo getFileInfo() {
		return fileInfo;
	}
	public void setFileInfo(FileInfo fileInfo) {
		this.fileInfo = fileInfo;
	}
	
}
