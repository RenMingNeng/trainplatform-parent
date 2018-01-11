package com.bossien.train.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("content")
public class Content implements Serializable {

    private static final long serialVersionUID = -5141886693065536093L;
    @XStreamAlias("title")
    private String title;
    @XStreamAlias("titleImgs")
    private ArrayList<FileInfo> titleImgs;
    @XStreamAlias("options")
    private ArrayList<Option> options;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<FileInfo> getTitleImgs() {
        return titleImgs;
    }

    public void setTitleImgs(ArrayList<FileInfo> titleImgs) {
        this.titleImgs = titleImgs;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }

}
