package com.bossien.train.domain.eum;

/**
 * Created by A on 2017/9/20.
 */
public enum ProblemTypeEnum {
    ProblemType_1("1", "视频播放问题"),
    ProblemType_2("2", "题目问题"),
    ProblemType_3("3", "系统错误"),
    ProblemType_4("4", "界面问题"),
    ProblemType_5("5", "功能要求"),
    ProblemType_6("6", "其他建议");


    private String value;
    private String name;

    ProblemTypeEnum(String value, String name) {
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

    public static ProblemTypeEnum get(String value) {
        ProblemTypeEnum[] values = ProblemTypeEnum.values();
        for (ProblemTypeEnum object : values) {
            if (object.value.equals(value)) {
                return object;
            }
        }
        return null;
    }
}
