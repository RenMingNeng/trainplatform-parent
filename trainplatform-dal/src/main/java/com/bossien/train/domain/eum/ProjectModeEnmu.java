package com.bossien.train.domain.eum;

/**
 * 项目模式枚举类
 * Created by Administrator on 2017/8/1.
 */
public enum ProjectModeEnmu {
    PUBLIC_MODE("1","私有项目"),PRIVATE_MODE("0","公开项目");
    private String value;
    private String name;

    public String getValue() {
        return value;
    }

    ProjectModeEnmu(String value, String name) {
        this.value = value;
        this.name = name;
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

}
