package com.bossien.train.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * 公司统计表
 * Created by Administrator on 2017/8/16.
 */
public class VideoPosition implements Serializable {
    private String id;
    private String userId;      //用户id
    private String courseId;    //课程id
    private String videoId;     //附件id
    private Integer lastPosition;//附件id
    private String createTime;
    private String operTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Integer getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Integer lastPosition) {
        this.lastPosition = lastPosition;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }
}
