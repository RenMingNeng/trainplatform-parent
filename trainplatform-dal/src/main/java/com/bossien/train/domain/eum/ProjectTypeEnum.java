package com.bossien.train.domain.eum;

import sun.applet.Main;

/**
 * Created by A on 2017/7/27.
 * 项目类型枚举
 */
public enum ProjectTypeEnum {
    QuestionType_1("1", "培训"),
    QuestionType_2("2", "练习"),
    QuestionType_3("3", "考试"),
    QuestionType_4("4", "培训-练习"),
    QuestionType_5("5", "培训-考试 "),
    QuestionType_6("6", "练习-考试"),
    QuestionType_7("7", "培训-练习-考试 ");

    private String value;
    private String name;

    ProjectTypeEnum(String value, String name) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ProjectTypeEnum get(String value) {
        ProjectTypeEnum[] values = ProjectTypeEnum.values();
        for (ProjectTypeEnum object : values) {
            if (object.value.equals(value)) {
                return object;
            }
        }
        return null;
    }

}