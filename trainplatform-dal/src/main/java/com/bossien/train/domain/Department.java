package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/1.
 */
public class Department implements Serializable {
    private String id;
    private String companyId;
    private String deptName;
    private String director; //部门负责人
    private String parentId; //上级单位序号
    private String isValid; //是否有效1: 是 2: 否
    private Integer orderNum; //排序号
    private String createUser;
    private String createTime;
    private String operUser;
    private String operTime;

    public Department(){}

    public Department(String id, String deptName, String companyId){
        this.id = id;
        this.deptName = deptName;
        this.companyId = companyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    // 是否有效 1：有效、2：无效
    public enum IsValid {
        TYPE_1("有效", "1"), TYPE_2("无效", "2");
        // 枚举中文
        private String name;
        // 枚举值
        private String value;
        // 枚举翻译
        public static IsValid getEnum(String value) {
            IsValid[] is = IsValid.values();
            for (IsValid i : is) {
                if (!value.equals(i.getValue())) {
                    continue;
                }
                return i;
            }
            return null;
        }
        // 构造
        IsValid(String name, String value) {
            this.name = name;
            this.value = value;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }
}
