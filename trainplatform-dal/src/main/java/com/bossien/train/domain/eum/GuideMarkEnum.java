package com.bossien.train.domain.eum;

/**
 * Created by A on 2017/7/27.
 * 项目类型枚举
 */
public enum GuideMarkEnum {
    GuideMark_1("0", "显示"),
    GuideMark_2("1", "隐藏");

    private String value;
    private String name;

    GuideMarkEnum(String value, String name) {
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

    public static GuideMarkEnum get(String value) {
        GuideMarkEnum[] values = GuideMarkEnum.values();
        for (GuideMarkEnum object : values) {
            if (object.value.equals(value)) {
                return object;
            }
        }
        return null;
    }

}