package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/15.
 */
public class UserTrainRole implements Serializable {

    private static final long serialVersionUID = 4982656077737763948L;
    private String userId;
    private String trainRoleId;
    private String roleName;
    private String isValid;
    private String createUser;
    private String createTime;
    private String operUser;
    private String operTime;

    public UserTrainRole(){}
    public UserTrainRole(String userId){
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrainRoleId() {
        return trainRoleId;
    }

    public void setTrainRoleId(String trainRoleId) {
        this.trainRoleId = trainRoleId;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public enum IsValid {
        TYPE_1("有效", "1"), TYPE_2("无效", "2");
        // 枚举中文
        private String name;
        // 枚举值
        private String value;

        // 枚举翻译
        public static User.IsValid getEnum(String value) {
            User.IsValid[] is = User.IsValid.values();
            for (User.IsValid i : is) {
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
