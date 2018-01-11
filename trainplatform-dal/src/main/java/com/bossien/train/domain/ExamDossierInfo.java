package com.bossien.train.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by A on 2017/7/25.
 */
public class ExamDossierInfo implements Serializable{
    private String projectId;
    private Integer yetExamCount;    //已考人数
    private Integer notExamCount;    //未考人数
    private Integer qualifiedCount;  //合格人数
    private Integer unqualifiedCount; //不合格人数
    private double examPassRate;       //考试通过率

    public ExamDossierInfo(){}

    /**
     * 初始化构造函数
     * @param projectId
     * @param notExamCount 未考人数 （默认总人数)
     */
    public ExamDossierInfo(String projectId, Integer notExamCount){
        this.projectId = projectId;
        this.notExamCount = notExamCount;
        this.yetExamCount = 0;
        this.qualifiedCount = 0;
        this.unqualifiedCount = 0;
        this.examPassRate = 0.0;
    }

    /**
     * 修改构造方法
     * @param projectId
     * @param yetExamCount 已考人数
     * @param notExamCount 未考人数
     * @param qualifiedCount 合格人数
     * @param unqualifiedCount 不合格人数
     */
    public ExamDossierInfo(String projectId, Integer yetExamCount, Integer notExamCount,
                           Integer qualifiedCount, Integer unqualifiedCount){
        this.projectId = projectId;
        this.yetExamCount = yetExamCount;
        this.notExamCount = notExamCount;
        this.qualifiedCount = qualifiedCount;
        this.unqualifiedCount = unqualifiedCount;

        this.examPassRate = Double.parseDouble(new DecimalFormat("0.0").
                format(qualifiedCount * 100 / yetExamCount));
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getYetExamCount() {
        return yetExamCount;
    }

    public void setYetExamCount(Integer yetExamCount) {
        this.yetExamCount = yetExamCount;
    }

    public Integer getNotExamCount() {
        return notExamCount;
    }

    public void setNotExamCount(Integer notExamCount) {
        this.notExamCount = notExamCount;
    }

    public Integer getQualifiedCount() {
        return qualifiedCount;
    }

    public void setQualifiedCount(Integer qualifiedCount) {
        this.qualifiedCount = qualifiedCount;
    }

    public Integer getUnqualifiedCount() {
        return unqualifiedCount;
    }

    public void setUnqualifiedCount(Integer unqualifiedCount) {
        this.unqualifiedCount = unqualifiedCount;
    }

    public double getExamPassRate() {
        return examPassRate;
    }

    public void setExamPassRate(double examPassRate) {
        this.examPassRate = examPassRate;
    }
}
