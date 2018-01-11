package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by A on 2017/7/31.
 */
public class Question implements Serializable {
    private static final long serialVersionUID = 6227595535045711013L;
    private String intId;
    private String varNo; //题号：需要提供编号规则
    private String varTitle;  //题目简称chrCategory
    private String varContent;  //题目内容，格式：{"title":"","titleImgs":[{"fileId":"","fileName":"","filePath":""}],"options":
    // [{"item":"","itemDesc":"","fileInfo":{"fileId":"","fileName":"","filePath":""}}]}
    private Content content;
    private String chrCategory;  //试题类型:1.文字题、2.多媒体题、3.图片题
    private String chrType;     //题目类型：01.单选题 02.多选题 03.判断题 04.填空题 05.简答题 06.论述题 07.分析题
    private String intDifficult;    //试题难度系数：1到10
    private String chrValid;        //是否有效：1-有效 2-无效
    private String varAnswer;       //用于记录单选、多选题、判断题选择型答案
    private String varAnswerDesc;      //用于记录填空、简答、分析文字型答案
    private String varSource;       //试题来源
    private String varAnalysis;     //试题解析,格式：{"content":"","images":{}}
    private String varExamPoint;       //试题考点
    private String varCreateUser;
    private String datCreateDate;
    private String varOperUser;
    private String datOperDate;
    private String intImportant;
    private String varIndustry;


    public String getIntImportant() {
        return intImportant;
    }

    public void setIntImportant(String intImportant) {
        this.intImportant = intImportant;
    }

    public String getVarIndustry() {
        return varIndustry;
    }

    public void setVarIndustry(String varIndustry) {
        this.varIndustry = varIndustry;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getIntId() {
        return intId;
    }

    public void setIntId(String intId) {
        this.intId = intId;
    }

    public String getVarNo() {
        return varNo;
    }

    public void setVarNo(String varNo) {
        this.varNo = varNo;
    }

    public String getVarTitle() {
        return varTitle;
    }

    public void setVarTitle(String varTitle) {
        this.varTitle = varTitle;
    }

    public String getVarContent() {
        return varContent;
    }

    public void setVarContent(String varContent) {
        this.varContent = varContent;
    }

    public String getChrCategory() {
        return chrCategory;
    }

    public void setChrCategory(String chrCategory) {
        this.chrCategory = chrCategory;
    }

    public String getChrType() {
        return chrType;
    }

    public void setChrType(String chrType) {
        this.chrType = chrType;
    }

    public String getIntDifficult() {
        return intDifficult;
    }

    public void setIntDifficult(String intDifficult) {
        this.intDifficult = intDifficult;
    }

    public String getChrValid() {
        return chrValid;
    }

    public void setChrValid(String chrValid) {
        this.chrValid = chrValid;
    }

    public String getVarAnswer() {
        return varAnswer;
    }

    public void setVarAnswer(String varAnswer) {
        this.varAnswer = varAnswer;
    }

    public String getVarAnswerDesc() {
        return varAnswerDesc;
    }

    public void setVarAnswerDesc(String varAnswerDesc) {
        this.varAnswerDesc = varAnswerDesc;
    }

    public String getVarSource() {
        return varSource;
    }

    public void setVarSource(String varSource) {
        this.varSource = varSource;
    }

    public String getVarAnalysis() {
        return varAnalysis;
    }

    public void setVarAnalysis(String varAnalysis) {
        this.varAnalysis = varAnalysis;
    }

    public String getVarExamPoint() {
        return varExamPoint;
    }

    public void setVarExamPoint(String varExamPoint) {
        this.varExamPoint = varExamPoint;
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
