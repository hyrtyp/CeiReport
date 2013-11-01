package com.hyrt.cei.vo;

import java.io.Serializable;

public class PersonCenterInf implements Serializable {

	/**
	 * 个人中心数据
	 */
	private static final long serialVersionUID = 1L;
	private String lgoinname;// 登陆名
	private String name;// 用户名
	private String sex;// 性别
	private String certype;// 证件类型
	private String cardno;// 证件号码
	private String phone;// 电话
	private String email;// 电子邮件
	private String unitname;// 单位名称
	private String integral;// 积分

	public PersonCenterInf() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PersonCenterInf(String lgoinname, String name, String sex,
			String certype, String cardno, String phone, String email,
			String unitname, String integral) {
		super();
		this.lgoinname = lgoinname;
		this.name = name;
		this.sex = sex;
		this.certype = certype;
		this.cardno = cardno;
		this.phone = phone;
		this.email = email;
		this.unitname = unitname;
		this.integral = integral;
	}

	public String getLgoinname() {
		return lgoinname;
	}

	public void setLgoinname(String lgoinname) {
		this.lgoinname = lgoinname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCertype() {
		return certype;
	}

	public void setCertype(String certype) {
		this.certype = certype;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "PersonCenterInf [lgoinname=" + lgoinname + ", name=" + name
				+ ", sex=" + sex + ", certype=" + certype + ", cardno="
				+ cardno + ", phone=" + phone + ", email=" + email
				+ ", unitname=" + unitname + ", integral=" + integral + "]";
	}

}
