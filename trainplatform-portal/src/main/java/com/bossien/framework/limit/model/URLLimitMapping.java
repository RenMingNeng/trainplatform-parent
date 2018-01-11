package com.bossien.framework.limit.model;

/**
 * Created by Administrator on 2017/8/18.
 */
public class URLLimitMapping {

    private String[] urls;
    private int rate=0;

    public URLLimitMapping() {
    }

    public URLLimitMapping(String[] urls, int rate) {
        this.urls = urls;
        this.rate = rate;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
