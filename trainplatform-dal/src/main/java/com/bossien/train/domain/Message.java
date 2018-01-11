package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by A on 2017/7/25.
 */
public class Message implements Serializable{
    public static final String MSG_TPL_TITLE_0001 = "监管申请";
    public static final String MSG_TPL_CONTENT_0001 = "{0}申请监管您的单位<div><a href='javascript:void(0)' {1} class=\"a agree_yes\">同意</a>&nbsp;<a href='javascript:void(0)' {2} class=\"a agree_no\">拒绝</a></div>";
    public static final String MSG_TPL_TITLE_0002 = "单位变更申请";
    public static final String MSG_TPL_CONTENT_0002 = "{0}用户[{1}]申请加入本单位<div><a href='javascript:void(0)' {2} class=\"a move_yes\">同意</a>&nbsp;<a href='javascript:void(0)' {2} class=\"a move_no\">拒绝</a></div>";
    public static final String MSG_TPL_TITLE_0003 = "单位变更通知";
    public static final String MSG_TPL_CONTENT_0003 = "您的变更申请已被{0}";
    public static final String MSG_TPL_TITLE_0004 = "单位注册通知";
    public static final String MSG_TPL_CONTENT_0004_success = "您好！您使用博晟一号通账号注册申请的\"{0}\"已经{1}，单位编号为\"{2}\"，单位编号是贵单位在本系统中的唯一标识，请您牢记。您已经成为\"{3}\"的管理员，可使用管理员身份，进行相关操作，祝您使用愉快。";
    public static final String MSG_TPL_CONTENT_0004_fail = "您好！您使用博晟一号通账号注册申请的\"{0}\"{1}，原因如下：{2}";

    private String id;
    private String userId;              //用户
    private String companyId;           //企业
    private String messageTitle;        //标题
    private String messageContent;     //内容
    private String messageStatus;       //查看状态：1未查看2已查看
    private String sendId;              //发送人
    private String sendName;           //发送人姓名
    private String createTime;          //创建时间
    private String createUser;          //创建用户

    public Message(){}

    /**
     * 修改
     * @param userId
     * @param companyId—
     * @param messageStatus
     */
    public Message(String userId, String companyId, String messageStatus, String sendId) {
        this.userId = userId;
        this.companyId = companyId;
        this.messageStatus = messageStatus;
        this.sendId = sendId;
    }

    /**
     * 初始化
     * @param userId
     * @param companyId
     * @param messageTitle
     * @param messageContent
     * @param sendId
     * @param sendName
     */
    public Message(String userId, String companyId, String messageTitle,
                   String messageContent, String sendId, String sendName) {
        this.userId = userId;
        this.companyId = companyId;
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
        this.sendId = sendId;
        this.sendName = sendName;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
