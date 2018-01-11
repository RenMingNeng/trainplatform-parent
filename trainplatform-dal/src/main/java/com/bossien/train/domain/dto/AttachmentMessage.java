package com.bossien.train.domain.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/16.
 */
public class AttachmentMessage implements Serializable {
    private static final long serialVersionUID = -4305206526787864772L;
    private String companyCode;//公司编码
    private String attachmentId;//附件ID
    private String attachmentFid;//文件ID
    private String attachmentLocalName;//附件名称
    private String attachmentRemotePath;//文件远程访问路径
    private String attachmentExt;//文件后缀
    private String attachmentType;//附件类型（01：图片，02：文档，03：视频，04：其它）
    private String attachmentBusinessId;//行业类型
    private String attachmentMd5;//MD5值
    private String attachmentStatus;//"上传完成状态：（01：初始化，02：上传完成，03：上传失败）"
    private String attachmentRemark;//"备注"
    private String companyId;//公司id
    private String createUser;//创建人
    private String datCreateDate;//创建时间
    private String operUser;//修改人
    private String datOperDate;//修改时间

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getDatCreateDate() {
        return datCreateDate;
    }

    public void setDatCreateDate(String datCreateDate) {
        this.datCreateDate = datCreateDate;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getDatOperDate() {
        return datOperDate;
    }

    public void setDatOperDate(String datOperDate) {
        this.datOperDate = datOperDate;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentFid() {
        return attachmentFid;
    }

    public void setAttachmentFid(String attachmentFid) {
        this.attachmentFid = attachmentFid;
    }

    public String getAttachmentLocalName() {
        return attachmentLocalName;
    }

    public void setAttachmentLocalName(String attachmentLocalName) {
        this.attachmentLocalName = attachmentLocalName;
    }

    public String getAttachmentRemotePath() {
        return attachmentRemotePath;
    }

    public void setAttachmentRemotePath(String attachmentRemotePath) {
        this.attachmentRemotePath = attachmentRemotePath;
    }

    public String getAttachmentExt() {
        return attachmentExt;
    }

    public void setAttachmentExt(String attachmentExt) {
        this.attachmentExt = attachmentExt;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentBusinessId() {
        return attachmentBusinessId;
    }

    public void setAttachmentBusinessId(String attachmentBusinessId) {
        this.attachmentBusinessId = attachmentBusinessId;
    }

    public String getAttachmentMd5() {
        return attachmentMd5;
    }

    public void setAttachmentMd5(String attachmentMd5) {
        this.attachmentMd5 = attachmentMd5;
    }

    public String getAttachmentStatus() {
        return attachmentStatus;
    }

    public void setAttachmentStatus(String attachmentStatus) {
        this.attachmentStatus = attachmentStatus;
    }

    public String getAttachmentRemark() {
        return attachmentRemark;
    }

    public void setAttachmentRemark(String attachmentRemark) {
        this.attachmentRemark = attachmentRemark;
    }
}
