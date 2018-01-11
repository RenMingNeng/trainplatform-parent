package com.bossien.train.domain;

import java.io.Serializable;

public class Attachment implements Serializable {

    private String intId;           // 主键
    private String varFid;          // 文件id
    private String varLocalName;    // 附件名称
    private String varRemotePath;   // 文件远程访问路径
    private String varExt;          // 文件后缀
    private String varType;         // 附件类型（01：图片，02：文档，03：视频，04：其它）
    private String intBusinessId;  // 行业类型
    private String varMd5;          // MD5值
    private String chrComplete;     // 上传完成状态：01：初始化，02：上传完成，03：上传失败
    private String varRemark;       // 备注
    private String varCreateUser;   // 创建人
    private String datCreateDate;   // 创建时间
    private String varOperUser;     // 最后操作人
    private String datOperDate;     // 最后操作时间


    public String getIntId() {
        return intId;
    }

    public void setIntId(String intId) {
        this.intId = intId;
    }

    public String getVarFid() {
        return varFid;
    }

    public void setVarFid(String varFid) {
        this.varFid = varFid;
    }

    public String getVarLocalName() {
        return varLocalName;
    }

    public void setVarLocalName(String varLocalName) {
        this.varLocalName = varLocalName;
    }

    public String getVarRemotePath() {
        return varRemotePath;
    }

    public void setVarRemotePath(String varRemotePath) {
        this.varRemotePath = varRemotePath;
    }

    public String getVarExt() {
        return varExt;
    }

    public void setVarExt(String varExt) {
        this.varExt = varExt;
    }

    public String getVarType() {
        return varType;
    }

    public void setVarType(String varType) {
        this.varType = varType;
    }

    public String getIntBusinessId() {
        return intBusinessId;
    }

    public void setIntBusinessId(String intBusinessId) {
        this.intBusinessId = intBusinessId;
    }

    public String getVarMd5() {
        return varMd5;
    }

    public void setVarMd5(String varMd5) {
        this.varMd5 = varMd5;
    }

    public String getChrComplete() {
        return chrComplete;
    }

    public void setChrComplete(String chrComplete) {
        this.chrComplete = chrComplete;
    }

    public String getVarRemark() {
        return varRemark;
    }

    public void setVarRemark(String varRemark) {
        this.varRemark = varRemark;
    }

    public String getVarCreateUser() {
        return varCreateUser;
    }

    public void setVarCreateUser(String varCreateUser) {
        this.varCreateUser = varCreateUser;
    }

    public String getDatCreateDate() {
        return datCreateDate;
    }

    public void setDatCreateDate(String datCreateDate) {
        this.datCreateDate = datCreateDate;
    }

    public String getVarOperUser() {
        return varOperUser;
    }

    public void setVarOperUser(String varOperUser) {
        this.varOperUser = varOperUser;
    }

    public String getDatOperDate() {
        return datOperDate;
    }

    public void setDatOperDate(String datOperDate) {
        this.datOperDate = datOperDate;
    }
}
