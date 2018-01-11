package com.bossien.train.domain;

import com.google.gson.Gson;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Feedback implements Serializable {
    private Gson gson = new Gson();

    private String id;
    private String userId;          // user_id
    private String userName;        // user_name
    private String problemType;     // 问题类型
    private String content;         // 反馈内容
    private String contactWay;      // 联系方式
    private String attchments;      // 上传附件
    private String problemStatus;     // 问题类型
    private String createUser;      // 创建人
    private String createTime;      // 创建时间

    private List<Map<String, Object>> attachmentLMap = new ArrayList<Map<String, Object>>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblemStatus() {
        return problemStatus;
    }

    public void setProblemStatus(String problemStatus) {
        this.problemStatus = problemStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public String getAttchments() {
        return attchments;
    }

    public void setAttchments(String attchments) {

        this.attchments = attchments;
        this.convertAttachmentsJson2LMap(attchments);

    }

    private void convertAttachmentsJson2LMap(String attchments) {
        if(StringUtils.isEmpty(attchments)) {
            return;
        }
        try {
            attachmentLMap = gson.fromJson(attchments, List.class);
        } catch(Exception ex) {

        }
        setAttachmentLMap(attachmentLMap);

    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Feedback() {
        super();
    }

    public List<Map<String, Object>> getAttachmentLMap() {
        return attachmentLMap;
    }

    public void setAttachmentLMap(List<Map<String, Object>> attachmentLMap) {
        this.attachmentLMap = attachmentLMap;
    }

    public Feedback(String id, String userId, String userName, String problemType, String content, String contactWay, String attchments, String createUser, String createTime) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.problemType = problemType;
        this.content = content;
        this.contactWay = contactWay;
        this.attchments = attchments;
        this.createUser = createUser;
        this.createTime = createTime;
    }
}
