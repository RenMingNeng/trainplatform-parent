package com.bossien.train.domain.eum;

/**
 * excel导入模板枚举
 */
public enum ExcelImportEnum {

    USER_TEMP("用户导入模板","1","AP_User.xlsx"),
    ROLE_TEMP("角色导入模板","2", "TP_Role.xlsx"),
    COMPANY_TEMP("其他模板", "3","");


    private String name;
    private String value;
    private String tempName;
    // 构造
    ExcelImportEnum(String name,String value,String tempName) {
        this.name = name;
        this.value = value;
        this.tempName=tempName;
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

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }
}
