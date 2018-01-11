package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/25.
 */
public class User implements Serializable {

    private String id;
    private String userAccount;
    private String userPasswd;
    private String userName;
    private String sex;
    private String idType;
    private String idNumber;
    private String supporter;
    private String userType;
    private String mobileNo;
    private String companyId;
    private String departmentId;
    private String departmentName;
    private String registType;
    private String registDate;
    private String lastLoginTime;
    private String isValid;
    private String openId;
    private String createUser;
    private String createDate;
    private String operUser;
    private String operDate;

    private String troleName;//受训角色



    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPasswd() {
        return userPasswd;
    }

    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getSupporter() {
        return supporter;
    }

    public void setSupporter(String supporter) {
        this.supporter = supporter;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getRegistType() {
        return registType;
    }

    public void setRegistType(String registType) {
        this.registType = registType;
    }

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }

    public String getTroleName() {
        return troleName;
    }

    public void setTroleName(String troleName) {
        this.troleName = troleName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public User() {}
    public User(String id,String lastLoginTime) {
        this.id = id;
        this.lastLoginTime = lastLoginTime;
    }
    public User(String id, String userAccount, String userPasswd, String userName) {
        this.id = id;
        this.userAccount = userAccount;
        this.userPasswd = userPasswd;
        this.userName = userName;
    }

    public User(String id, String userAccount, String userPasswd, String userName, String sex, String idType, String idNumber, String supporter, String userType, String mobileNo, String companyId, String departmentId, String departmentName, String registType, String registDate, String lastLoginTime, String isValid, String openId, String createUser, String createDate, String operUser, String operDate) {
        this.id = id;
        this.userAccount = userAccount;
        this.userPasswd = userPasswd;
        this.userName = userName;
        this.sex = sex;
        this.idType = idType;
        this.idNumber = idNumber;
        this.supporter = supporter;
        this.userType = userType;
        this.mobileNo = mobileNo;
        this.companyId = companyId;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.registType = registType;
        this.registDate = registDate;
        this.lastLoginTime = lastLoginTime;
        this.isValid = isValid;
        this.openId = openId;
        this.createUser = createUser;
        this.createDate = createDate;
        this.operUser = operUser;
        this.operDate = operDate;
    }

    public User(String id, String userAccount, String userPasswd, String userName, String openId) {
        this.id = id;
        this.userAccount = userAccount;
        this.userPasswd = userPasswd;
        this.userName = userName;
        this.openId = openId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userAccount='" + userAccount + '\'' +
                ", userPasswd='" + userPasswd + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    // 性别：1：女 2：男
    public enum Sex {
        TYPE_1("女", "1"), TYPE_2("男", "2");
        // 枚举中文
        private String name;
        // 枚举值
        private String value;

        // 枚举翻译
        public static Sex getEnum(String value) {
            Sex[] is = Sex.values();
            for (Sex i : is) {
                if (!value.equals(i.getValue())) {
                    continue;
                }
                return i;
            }
            return null;
        }

        // 构造
        Sex(String name, String value) {
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

    // 用户是否有效 1：有效、2：无效
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

    // 证件类型:1: 身份证 2: 居住证 3: 签证 4: 护照5: 军人证6: 港澳通行证7: 台胞证
    public enum IdType {
        TYPE_1("身份证", "1"), TYPE_2("居住证", "2"), TYPE_3("签证", "3"), TYPE_4("护照",
                "4"), TYPE_5("军人证", "5"), TYPE_6("港澳通行证", "6"), TYPE_7("台胞证",
                "7");
        // 枚举中文
        private String name;
        // 枚举值
        private String value;

        // 枚举翻译
        public static IdType getEnum(String value) {
            IdType[] is = IdType.values();
            for (IdType i : is) {
                if (!value.equals(i.getValue())) {
                    continue;
                }
                return i;
            }
            return null;
        }

        // 构造
        IdType(String name, String value) {
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

    // 注册类型
    public enum RegistType {
        TYPE_1("个人自行注册", "1"), TYPE_2("企业批量注册", "2");
        // 枚举中文
        private String name;
        // 枚举值
        private String value;

        // 枚举翻译
        public static RegistType getEnum(String value) {
            RegistType[] is = RegistType.values();
            for (RegistType i : is) {
                if (!value.equals(i.getValue())) {
                    continue;
                }
                return i;
            }
            return null;
        }

        // 构造
        RegistType(String name, String value) {
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

    // 培训载体 1：平台； 2：工具箱
    public enum Supporter {
        TYPE_1("平台", "1"), TYPE_2("工具箱", "2");
        // 枚举中文
        private String name;
        // 枚举值
        private String value;

        // 枚举翻译
        public static Supporter getEnum(String value) {
            Supporter[] is = Supporter.values();
            for (Supporter i : is) {
                if (!value.equals(i.getValue())) {
                    continue;
                }
                return i;
            }
            return null;
        }

        // 构造
        Supporter(String name, String value) {
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

    // 人员类型 1：培训组织人员(单位管理员)； 2：培训监督人员 3学员
    public enum UserType {
        TYPE_1("培训组织人员", "1"), TYPE_2("培训监督人员", "2"), TYPE_3("学员", "3");
        // 枚举中文
        private String name;
        // 枚举值
        private String value;

        // 枚举翻译
        public static UserType getEnum(String value) {
            UserType[] is = UserType.values();
            for (UserType i : is) {
                if (!value.equals(i.getValue())) {
                    continue;
                }
                return i;
            }
            return null;
        }

        // 构造
        UserType(String name, String value) {
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
