package com.bossien.train.domain;

import com.bossien.train.util.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 公司统计表
 * Created by Administrator on 2017/8/16.
 */
public class CompanyTjLog implements Serializable {
    private String  companyId;               //公司id
    private String  companyName;            //公司名称
    private Integer  countProject;            //项目数量
    private Integer  countUser;               //学员人数
    private Integer  countTrain;              //参与培训人次
    private Integer  countTrainCompleteYes;   //完成培训人次
    private Integer  countExam;               //参与考试人次
    private Integer  countExamPassYes;        //考试合格人次
    private String  percentTrainComplete;    //培训完成率
    private Integer  countTrainUser;          //培训人数
    private Integer  countCourse;              //课程数量
    private Integer  countQuestion;          //题库数量
    private Long  totalClassHour;          //累计学时
    private String  averagePersonClassHour;   //人均学时
    private Long  totalYearClassHour;       //年度累计学时
    private String  averageYearClassHour;    //年度平均学时
    private String  createTime;             //创建时间
    private String  createUser;             //操作用户

    public CompanyTjLog(){}

    public CompanyTjLog(String companyId, String  companyName){
        this.companyId = companyId;
        this.companyName = companyName;
        this.createTime = DateUtils.convertDate2String(new Date());
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCountExamPassYes() {
        return countExamPassYes;
    }

    public CompanyTjLog setCountExamPassYes(Integer countExamPassYes) {
        this.countExamPassYes = countExamPassYes;
        return this;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getCountProject() {
        return countProject;
    }

    public CompanyTjLog setCountProject(Integer countProject) {
        this.countProject = countProject;
        return this;
    }

    public Integer getCountUser() {
        return countUser;
    }

    public CompanyTjLog setCountUser(Integer countUser) {
        this.countUser = countUser;
        return this;
    }

    public Integer getCountTrain() {
        return countTrain;
    }

    public CompanyTjLog setCountTrain(Integer countTrain) {
        this.countTrain = countTrain;
        return this;
    }

    public Integer getCountTrainCompleteYes() {
        return countTrainCompleteYes;
    }

    public CompanyTjLog setCountTrainCompleteYes(Integer countTrainCompleteYes) {
        this.countTrainCompleteYes = countTrainCompleteYes;
        return this;
    }

    public Integer getCountExam() {
        return countExam;
    }

    public CompanyTjLog setCountExam(Integer countExam) {
        this.countExam = countExam;
        return this;
    }

    public String getPercentTrainComplete() {
        return percentTrainComplete;
    }

    public void setPercentTrainComplete(String percentTrainComplete) {
        this.percentTrainComplete = percentTrainComplete;
    }

    public Integer getCountTrainUser() {
        return countTrainUser;
    }

    public CompanyTjLog setCountTrainUser(Integer countTrainUser) {
        this.countTrainUser = countTrainUser;
        return this;
    }

    public Integer getCountCourse() {
        return countCourse;
    }

    public CompanyTjLog setCountCourse(Integer countCourse) {
        this.countCourse = countCourse;
        return this;
    }

    public Integer getCountQuestion() {
        return countQuestion;
    }

    public CompanyTjLog setCountQuestion(Integer countQuestion) {
        this.countQuestion = countQuestion;
        return this;
    }

    public Long getTotalClassHour() {
        return totalClassHour;
    }

    public CompanyTjLog setTotalClassHour(Long totalClassHour) {
        this.totalClassHour = totalClassHour;
        return this;
    }

    public String getAveragePersonClassHour() {
        return averagePersonClassHour;
    }

    public void setAveragePersonClassHour(String averagePersonClassHour) {
        this.averagePersonClassHour = averagePersonClassHour;
    }

    public Long getTotalYearClassHour() {
        return totalYearClassHour;
    }

    public CompanyTjLog setTotalYearClassHour(Long totalYearClassHour) {
        this.totalYearClassHour = totalYearClassHour;
        return this;
    }

    public String getAverageYearClassHour() {
        return averageYearClassHour;
    }

    public void setAverageYearClassHour(String averageYearClassHour) {
        this.averageYearClassHour = averageYearClassHour;
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
