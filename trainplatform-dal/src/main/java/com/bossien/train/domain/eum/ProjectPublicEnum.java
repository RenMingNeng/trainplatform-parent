package com.bossien.train.domain.eum;

/**
 * Created by A on 2017/7/27.
 * 项目发布状态枚举
 */
public enum ProjectPublicEnum {
    Public_1("1", "未发布"),
    Public_2("2", "已发布");


    private String value;
    private String name;

    ProjectPublicEnum(String value, String name) {
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

    public static ProjectPublicEnum get(String value) {
        ProjectPublicEnum[] values = ProjectPublicEnum.values();
        for (ProjectPublicEnum object : values) {
            if (object.value.equals(value)) {
                return object;
            }
        }
        return null;
    }

}