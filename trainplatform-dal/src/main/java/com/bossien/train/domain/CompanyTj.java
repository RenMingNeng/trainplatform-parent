package com.bossien.train.domain;

import com.bossien.train.util.DateUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 公司统计表
 * Created by Administrator on 2017/8/16.
 */
public class CompanyTj implements Serializable {
    private String  companyId;               //公司id
    private String  companyName;            //公司名称
    private String  countProject;            //项目数量
    private String  countUser;               //学员人数
    private String  countTrain;              //参与培训人次
    private String  countTrainCompleteYes;   //完成培训人次
    private String  countExam;               //参与考试人次
    private String  countExamPassYes;        //考试合格人次
    private String  percentTrainComplete;    //培训率
    private String  countTrainUser;          //培训人数
    private String  countCourse;              //课程数量
    private String  countQuestion;          //题库数量
    private String  totalClassHour;          //累计学时
    private String  averagePersonClassHour;   //人均学时
    private String  totalYearClassHour;       //年度累计学时
    private String  averageYearClassHour;    //年度平均学时
    private String  createTime;             //创建时间
    private String  createUser;             //操作用户
    private String  operTime;               //操作时间
    private String  operUser;               //操作用户

    private String  percentTrain;    //培训率
    private String  averageTrainCount;   //人均培训次数

    public CompanyTj() {
    }

    public CompanyTj(String companyId, String companyName, int countUser, int countTrain, int countTrainUser, Long totalClassHour) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.countUser = countUser + "";
        this.countTrain = countTrain + "";
        this.countTrainUser = countTrainUser + "";
        this.totalClassHour = totalClassHour + "";

        if(countUser != 0){
            this.percentTrain = Double.parseDouble(new DecimalFormat("0.0").format(countTrainUser * 100.0 / countUser)) + "";
            this.averagePersonClassHour = Double.parseDouble(new DecimalFormat("0.00").format(totalClassHour * 1.00  / countUser)) + "";
            this.averageTrainCount = Double.parseDouble(new DecimalFormat("0.00").format(countTrain * 1.00  / countUser)) + "";
        }
    }

    public CompanyTj(String companyId, String companyName, String userName) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.createTime = DateUtils.formatDateTime(new Date());
        this.createUser = userName;
        this.operTime = DateUtils.formatDateTime(new Date());
        this.operUser = userName;

        /*初始值*/
        this.countProject = "0";
        this.countUser = "0";
        this.countTrain = "0";
        this.countTrainCompleteYes = "0";
        this.countExam = "0";
        this.countExamPassYes = "0";
        this.percentTrainComplete = "0.00";
        this.countTrainUser = "0";
        this.countCourse = "0";
        this.countQuestion = "0";
        this.totalClassHour = "0";
        this.averagePersonClassHour = "0.00";
        this.totalYearClassHour = "0";
        this.averageYearClassHour = "0.00";
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

    public String getCountExamPassYes() {
        return countExamPassYes;
    }

    public void setCountExamPassYes(String countExamPassYes) {
        this.countExamPassYes = countExamPassYes;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCountProject() {
        return countProject;
    }

    public void setCountProject(String countProject) {
        this.countProject = countProject;
    }

    public String getCountTrainCompleteYes() {
        return countTrainCompleteYes;
    }

    public void setCountTrainCompleteYes(String countTrainCompleteYes) {
        this.countTrainCompleteYes = countTrainCompleteYes;
    }

    public String getCountExam() {
        return countExam;
    }

    public void setCountExam(String countExam) {
        this.countExam = countExam;
    }

    public String getPercentTrainComplete() {
        return percentTrainComplete;
    }

    public void setPercentTrainComplete(String percentTrainComplete) {
        this.percentTrainComplete = percentTrainComplete;
    }

    public String getCountCourse() {
        return countCourse;
    }

    public void setCountCourse(String countCourse) {
        this.countCourse = countCourse;
    }

    public String getCountQuestion() {
        return countQuestion;
    }

    public void setCountQuestion(String countQuestion) {
        this.countQuestion = countQuestion;
    }

    public String getTotalYearClassHour() {
        return totalYearClassHour;
    }

    public void setTotalYearClassHour(String totalYearClassHour) {
        this.totalYearClassHour = totalYearClassHour;
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

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getOperUser() {
        return operUser;
    }

    public String getPercentTrain() {
        return percentTrain;
    }

    public void setPercentTrain(String percentTrain) {
        this.percentTrain = percentTrain;
    }

    public String getAverageTrainCount() {
        return averageTrainCount;
    }

    public void setAverageTrainCount(String averageTrainCount) {
        this.averageTrainCount = averageTrainCount;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getCountUser() {
        return countUser;
    }

    public void setCountUser(String countUser) {
        this.countUser = countUser;
    }

    public String getCountTrain() {
        return countTrain;
    }

    public void setCountTrain(String countTrain) {
        this.countTrain = countTrain;
    }

    public String getCountTrainUser() {
        return countTrainUser;
    }

    public void setCountTrainUser(String countTrainUser) {
        this.countTrainUser = countTrainUser;
    }

    public String getTotalClassHour() {
        return totalClassHour;
    }

    public void setTotalClassHour(String totalClassHour) {
        this.totalClassHour = totalClassHour;
    }

    public String getAveragePersonClassHour() {
        return averagePersonClassHour;
    }

    public void setAveragePersonClassHour(String averagePersonClassHour) {
        this.averagePersonClassHour = averagePersonClassHour;
    }
}
