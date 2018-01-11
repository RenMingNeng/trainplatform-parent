package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by A on 2017/7/25.
 */
public class Article implements Serializable{
    private String id;
    private String title;    //标题
    private String content;    //内容
    private String personType;  //阅读人群：1不限2学员3管理员
    private Integer orderNub; //排序号
    private String isTop;       //是否置顶
    private String author;    //作者
    private String status;    //是否有效：1是2否
    private String createTime;  //创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getOrderNub() {
        return orderNub;
    }

    public void setOrderNub(Integer orderNub) {
        this.orderNub = orderNub;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }
}
