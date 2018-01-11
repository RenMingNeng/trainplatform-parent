package com.bossien.train.sso;

import java.io.Serializable;

public class SsoUser implements Serializable {

	private static final long serialVersionUID = 1L;

	// 账号
	private String account;
	// 密码
	private String password;
	// 用户姓名
	private String name;
	// 手机号
	private String mobileNo;
	// 用户性别： 0：女 1：男
	private String sex;
	// 证件类型: 0: 身份证 1: 居住证 2: 签证 3: 护照 4: 军人证 5: 港澳通行证 6: 台胞证
	private String idType;
	// 证件号
	private String idNumber;
	// 邮箱
	private String email;
	// 外部系统唯一授权码（UUID生成，唯一）
	private String openId;

	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
}
