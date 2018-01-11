package com.bossien.train.domain.eum;

/**
 * Created by A on 2017/7/27.
 * 项目类型枚举
 */
public enum ProjectStatusEnum {
    ProjectStatus_1("1", "未发布"),
    ProjectStatus_2("2", "未开始"),
    ProjectStatus_3("3", "进行中"),
    ProjectStatus_4("4", "已结束");


    private String value;
    private String name;

    ProjectStatusEnum(String value, String name) {
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

    public static ProjectStatusEnum get(String value) {
        ProjectStatusEnum[] values = ProjectStatusEnum.values();
        for (ProjectStatusEnum object : values) {
            if (object.value.equals(value)) {
                return object;
            }
        }
        return null;
    }

}