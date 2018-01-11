package com.bossien.train.domain.eum;

/**
 * @author zhanghaitao
 * @date 2018/1/4 0004
 */
public enum CompanyRegisterStatusEnum {
    AUDITING("0", "待审核", ""),
    AUDIT_PASS("1", "审核通过", "pass"),
    AUDIT_REJECT("2", "审核未通过", "reject");

    private String code;
    private String desc;
    private String cmd;

    CompanyRegisterStatusEnum(String code, String desc, String cmd) {
        this.code = code;
        this.desc = desc;
        this.cmd = cmd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
