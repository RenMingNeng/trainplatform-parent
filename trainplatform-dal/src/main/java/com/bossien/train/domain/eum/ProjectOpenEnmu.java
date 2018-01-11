package com.bossien.train.domain.eum;

/**
 * Created by Administrator on 2017/8/1.
 */
public enum ProjectOpenEnmu {
    PROJECT_OPEN("1","开启"),PROJECT_CLOSE("2","未开启");
    private String value;
    private String name;

    ProjectOpenEnmu(String value, String name) {
        this.value = value;
        this.name = name;
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
}
