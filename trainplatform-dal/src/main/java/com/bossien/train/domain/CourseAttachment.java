package com.bossien.train.domain;

import java.io.Serializable;

public class CourseAttachment implements Serializable {

    private static final long serialVersionUID = 3935937545945647048L;
    private String intId;           // 主键
    private String intCourseId;     // 课程id
    private String intAttachmentId; // 附件id

    public String getIntId() {
        return intId;
    }

    public void setIntId(String intId) {
        this.intId = intId;
    }

    public String getIntCourseId() {
        return intCourseId;
    }

    public void setIntCourseId(String intCourseId) {
        this.intCourseId = intCourseId;
    }

    public String getIntAttachmentId() {
        return intAttachmentId;
    }

    public void setIntAttachmentId(String intAttachmentId) {
        this.intAttachmentId = intAttachmentId;
    }
}
