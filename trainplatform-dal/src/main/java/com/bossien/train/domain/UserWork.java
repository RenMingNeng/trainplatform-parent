package com.bossien.train.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/31.
 */
public class UserWork implements Serializable {
    private String id;
    private String userId;
    private String companyId;
    private String deptId;
    private String deptName;
    private String entryTime;
    private String quitTime;
    private String operater;
    private String createUser;
    private String createTime;
    private String operUser;
    private String operTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getQuitTime() {
        return quitTime;
    }

    public void setQuitTime(String quitTime) {
        this.quitTime = quitTime;
    }

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
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

    // 操作记录(1.人员登记/2：人员转移/3：人员退厂)
    public enum Operater {
        TYPE_1("人员登记", "1"), TYPE_2("人员转移", "2"), TYPE_3("人员退厂", "3"), TYPE_4("人员修改", "4");
        // 枚举中文
        private String name;
        // 枚举值
        private String value;
        // 枚举翻译
        public static Operater getEnum(String value) {
            Operater[] is = Operater.values();
            for (Operater i : is) {
                if (!value.equals(i.getValue())) {
                    continue;
                }
                return i;
            }
            return null;
        }
        // 构造
        Operater(String name, String value) {
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
