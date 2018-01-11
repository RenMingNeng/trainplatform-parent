package com.bossien.train.domain.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/16.
 */
public class QuestionMessage implements Serializable {

    private static final long serialVersionUID = 5742989368656155099L;
    private String companyCode;//公司编码
    private String companyId;//公司编码
    private String questionId;//题库ID
    private String questionNo;//题库编号 唯一
    private String questionTitle;//题目简称
    private String questionContent;//题目内容，格式
    private String questionCategory;//试题类型: （1.文字题、2.多媒体题、3.图片题）
    private String questionType;//题目类型：（01.单选题 02.多选题 03.判断题04.填空题 05.简答题 06.论述题07.分析题）",
    private String questionDifficult;//试题难度系数：（1到10）
    private String questionImportant;//重要度: （1:低 2:中 3:高）
    private String questionIndustry;//"适用行业"
    private String questionValid;//"是否有效：（1-有效 2-无效）"
    private String questionAnswer;//"用于记录单选、多选题、判断题选择型答案"
    private String questionAnswerDesc;//"用于记录填空、简答、分析文字型答案"
    private String questionSource;//"试题来源"
    private String questionAnalysis;//"试题解析,格式"
    private String questionExamPoint;//"试题考点"
    private String createUser;//创建人
    private String datCreateDate;//创建时间
    private String operUser;//修改人
    private String datOperDate;//修改时间
    private String courseId;


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

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

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(String questionCategory) {
        this.questionCategory = questionCategory;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionDifficult() {
        return questionDifficult;
    }

    public void setQuestionDifficult(String questionDifficult) {
        this.questionDifficult = questionDifficult;
    }

    public String getQuestionImportant() {
        return questionImportant;
    }

    public void setQuestionImportant(String questionImportant) {
        this.questionImportant = questionImportant;
    }

    public String getQuestionIndustry() {
        return questionIndustry;
    }

    public void setQuestionIndustry(String questionIndustry) {
        this.questionIndustry = questionIndustry;
    }

    public String getQuestionValid() {
        return questionValid;
    }

    public void setQuestionValid(String questionValid) {
        this.questionValid = questionValid;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public String getQuestionAnswerDesc() {
        return questionAnswerDesc;
    }

    public void setQuestionAnswerDesc(String questionAnswerDesc) {
        this.questionAnswerDesc = questionAnswerDesc;
    }

    public String getQuestionSource() {
        return questionSource;
    }

    public void setQuestionSource(String questionSource) {
        this.questionSource = questionSource;
    }

    public String getQuestionAnalysis() {
        return questionAnalysis;
    }

    public void setQuestionAnalysis(String questionAnalysis) {
        this.questionAnalysis = questionAnalysis;
    }

    public String getQuestionExamPoint() {
        return questionExamPoint;
    }

    public void setQuestionExamPoint(String questionExamPoint) {
        this.questionExamPoint = questionExamPoint;
    }
}
