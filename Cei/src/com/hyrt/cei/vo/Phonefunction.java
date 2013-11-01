package com.hyrt.cei.vo;

import java.io.Serializable;

public class Phonefunction implements Serializable{

	/**
	 * 登录信息
	 */
	private static final long serialVersionUID = 1L;
	private String loginname;
	private String password;
	private String unitid;
	public Phonefunction(String loginname, String password, String unitid) {
		super();
		this.loginname = loginname;
		this.password = password;
		this.unitid = unitid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUnitid() {
		return unitid;
	}
	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}
	
}
