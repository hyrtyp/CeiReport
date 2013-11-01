package com.hyrt.cei.vo;

import java.io.Serializable;

public class Updata implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int aphoneversion;
	private String aphonename;
	private String aphoneurl;
	
	private int apadversion;
	private String apadname;
	private String apadurl;
	public Updata() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Updata(int aphoneversion, String aphonename, String aphoneurl,
			int apadversion, String apadname, String apadurl) {
		super();
		this.aphoneversion = aphoneversion;
		this.aphonename = aphonename;
		this.aphoneurl = aphoneurl;
		this.apadversion = apadversion;
		this.apadname = apadname;
		this.apadurl = apadurl;
	}
	public int getAphoneversion() {
		return aphoneversion;
	}
	public void setAphoneversion(int aphoneversion) {
		this.aphoneversion = aphoneversion;
	}
	public String getAphonename() {
		return aphonename;
	}
	public void setAphonename(String aphonename) {
		this.aphonename = aphonename;
	}
	public String getAphoneurl() {
		return aphoneurl;
	}
	public void setAphoneurl(String aphoneurl) {
		this.aphoneurl = aphoneurl;
	}
	public int getApadversion() {
		return apadversion;
	}
	public void setApadversion(int apadversion) {
		this.apadversion = apadversion;
	}
	public String getApadname() {
		return apadname;
	}
	public void setApadname(String apadname) {
		this.apadname = apadname;
	}
	public String getApadurl() {
		return apadurl;
	}
	public void setApadurl(String apadurl) {
		this.apadurl = apadurl;
	}
	@Override
	public String toString() {
		return "Updata [aphoneversion=" + aphoneversion + ", aphonename="
				+ aphonename + ", aphoneurl=" + aphoneurl + ", apadversion="
				+ apadversion + ", apadname=" + apadname + ", apadurl="
				+ apadurl + "]";
	}
	
	
}
