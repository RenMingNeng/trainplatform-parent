package com.bossien.train.domain.eum;

/**
 * Created by A on 2017/7/27.
 * 项目类型枚举
 */
public enum ExamTypeEnum {
    ExamType_1("1", "模拟考试"),
    ExamType_2("2", "正式考试");

    private String value;
    private String name;

    ExamTypeEnum(String value, String name) {
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

    public static ExamTypeEnum get(String value) {
        ExamTypeEnum[] values = ExamTypeEnum.values();
        for (ExamTypeEnum object : values) {
            if (object.value.equals(value)) {
                return object;
            }
        }
        return null;
    }

}