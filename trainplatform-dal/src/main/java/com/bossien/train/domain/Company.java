package com.bossien.train.domain;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-07-31.
 */
public class Company implements Serializable {

    private static final long serialVersionUID = -5860409387069566404L;
    private Integer intId;          //主键
    private String varCode;         //单位编号
    private String varName;         //单位名称
    private Integer intTypeId;      //单位分类主键
    private String varBusinessId;  //行业表主键
    private Integer intCategoryId;  //单位类型主键
    private Integer intRegionId;    //行政区域主键
    private String varSupporter;    //培训载体(1：平台 2：APP 3：工具箱 4：班组终端)
    private String varLegalPerson;  //法定代表人（部门负责人）
    private String varContacter;    //联系人
    private String varContactInfo;  //联系方式
    private String varPostCode;     //邮政编码
    private String varAddress;      //通讯地址
    private String varBankAccount;  //银行账号
    private String chrIsRegulator;  //是否监管单位 0: 否 1: 是
    private String chrIsValid;      //有效标志(1：有效 2：无效)
    private String chrIsTermination;//是否停业
    private Integer intPid;         //上级单位序号
    private Integer intOrder;       //顺序号
    private String datCreateDate;   //创建日期
    private String varCreateUser;   //创建用户
    private String varOperUser;     //操作用户
    private String datOperDate;     //操作日期
    private String varRemark;       //备注

    public Company() {
    }

    public Company(Integer intId, String varCode, String varName, Integer intTypeId, String chrIsValid, Integer intPid) {
        this.intId = intId;
        this.varCode = varCode;
        this.varName = varName;
        this.intTypeId = intTypeId;
        this.chrIsValid = chrIsValid;
        this.intPid = intPid;
    }

    public Company(Integer intId, String varCode, String varName, Integer intTypeId, String varBusinessId, Integer intCategoryId, Integer intRegionId, String varSupporter, String varLegalPerson, String varContacter, String varContactInfo, String varPostCode, String varAddress, String varBankAccount, String chrIsRegulator, String chrIsValid, String chrIsTermination, Integer intPid, Integer intOrder, String datCreateDate, String varCreateUser, String varOperUser, String datOperDate, String varRemark) {
        this.intId = intId;
        this.varCode = varCode;
        this.varName = varName;
        this.intTypeId = intTypeId;
        this.varBusinessId = varBusinessId;
        this.intCategoryId = intCategoryId;
        this.intRegionId = intRegionId;
        this.varSupporter = varSupporter;
        this.varLegalPerson = varLegalPerson;
        this.varContacter = varContacter;
        this.varContactInfo = varContactInfo;
        this.varPostCode = varPostCode;
        this.varAddress = varAddress;
        this.varBankAccount = varBankAccount;
        this.chrIsRegulator = chrIsRegulator;
        this.chrIsValid = chrIsValid;
        this.chrIsTermination = chrIsTermination;
        this.intPid = intPid;
        this.intOrder = intOrder;
        this.datCreateDate = datCreateDate;
        this.varCreateUser = varCreateUser;
        this.varOperUser = varOperUser;
        this.datOperDate = datOperDate;
        this.varRemark = varRemark;
    }

    public Integer getIntId() {
        return intId;
    }

    public void setIntId(Integer intId) {
        this.intId = intId;
    }

    public String getVarCode() {
        return varCode;
    }

    public void setVarCode(String varCode) {
        this.varCode = varCode;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public Integer getIntTypeId() {
        return intTypeId;
    }

    public void setIntTypeId(Integer intTypeId) {
        this.intTypeId = intTypeId;
    }


    public String getVarBusinessId() {
        return varBusinessId;
    }

    public void setVarBusinessId(String varBusinessId) {
        this.varBusinessId = varBusinessId;
    }

    public Integer getIntCategoryId() {
        return intCategoryId;
    }

    public void setIntCategoryId(Integer intCategoryId) {
        this.intCategoryId = intCategoryId;
    }

    public Integer getIntRegionId() {
        return intRegionId;
    }

    public void setIntRegionId(Integer intRegionId) {
        this.intRegionId = intRegionId;
    }

    public String getVarSupporter() {
        return varSupporter;
    }

    public void setVarSupporter(String varSupporter) {
        this.varSupporter = varSupporter;
    }

    public String getVarLegalPerson() {
        return varLegalPerson;
    }

    public void setVarLegalPerson(String varLegalPerson) {
        this.varLegalPerson = varLegalPerson;
    }

    public String getVarContacter() {
        return varContacter;
    }

    public void setVarContacter(String varContacter) {
        this.varContacter = varContacter;
    }

    public String getVarContactInfo() {
        return varContactInfo;
    }

    public void setVarContactInfo(String varContactInfo) {
        this.varContactInfo = varContactInfo;
    }

    public String getVarPostCode() {
        return varPostCode;
    }

    public void setVarPostCode(String varPostCode) {
        this.varPostCode = varPostCode;
    }

    public String getVarAddress() {
        return varAddress;
    }

    public void setVarAddress(String varAddress) {
        this.varAddress = varAddress;
    }

    public String getVarBankAccount() {
        return varBankAccount;
    }

    public void setVarBankAccount(String varBankAccount) {
        this.varBankAccount = varBankAccount;
    }

    public String getChrIsRegulator() {
        return chrIsRegulator;
    }

    public void setChrIsRegulator(String chrIsRegulator) {
        this.chrIsRegulator = chrIsRegulator;
    }

    public String getChrIsValid() {
        return chrIsValid;
    }

    public void setChrIsValid(String chrIsValid) {
        this.chrIsValid = chrIsValid;
    }

    public String getChrIsTermination() {
        return chrIsTermination;
    }

    public void setChrIsTermination(String chrIsTermination) {
        this.chrIsTermination = chrIsTermination;
    }

    public Integer getIntPid() {
        return intPid;
    }

    public void setIntPid(Integer intPid) {
        this.intPid = intPid;
    }

    public Integer getIntOrder() {
        return intOrder;
    }

    public void setIntOrder(Integer intOrder) {
        this.intOrder = intOrder;
    }

    public String getDatCreateDate() {
        return datCreateDate;
    }

    public void setDatCreateDate(String datCreateDate) {
        this.datCreateDate = datCreateDate;
    }

    public String getVarCreateUser() {
        return varCreateUser;
    }

    public void setVarCreateUser(String varCreateUser) {
        this.varCreateUser = varCreateUser;
    }

    public String getVarOperUser() {
        return varOperUser;
    }

    public void setVarOperUser(String varOperUser) {
        this.varOperUser = varOperUser;
    }

    public String getDatOperDate() {
        return datOperDate;
    }

    public void setDatOperDate(String datOperDate) {
        this.datOperDate = datOperDate;
    }

    public String getVarRemark() {
        return varRemark;
    }

    public void setVarRemark(String varRemark) {
        this.varRemark = varRemark;
    }

    // 单位是否有效 1：有效、2：无效
    public enum ChrIsValid {
        TYPE_1("有效", "1"), TYPE_2("无效", "2");
        // 枚举中文
        private String name;
        // 枚举值
        private String value;

        // 枚举翻译
        public static Company.ChrIsValid getEnum(String value) {
            Company.ChrIsValid[] is = Company.ChrIsValid.values();
            for (Company.ChrIsValid i : is) {
                if (!value.equals(i.getValue())) {
                    continue;
                }
                return i;
            }
            return null;
        }

        // 构造
        ChrIsValid(String name, String value) {
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
