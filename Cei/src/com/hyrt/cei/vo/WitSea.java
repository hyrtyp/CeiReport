package com.hyrt.cei.vo;

import java.io.Serializable;

public class WitSea implements Serializable{
	/**
	 * 
	 */
	public static String ID="_id";
	public static String NAME="_name";
	public static String IMAGE="_image";
	public static String ISORDER="_isorder";
	
	
	private static final long serialVersionUID = 1L;
	private String funid;
	private String name;
	private String operationimage;
	private String isCustom;
	private String issuetime;
	//该业务价格
	private String price;
	//该业务的介绍
	private String intro;
	
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getIssuetime() {
		return issuetime;
	}

	public void setIssuetime(String issuetime) {
		this.issuetime = issuetime;
	}

	public WitSea() {

	}

	public WitSea(String funid, String name, String operationimage,String isCustom,String issuetime) {
         this.funid=funid;
         this.name=name;
         this.operationimage=operationimage;
	}

	public String getFunid() {
		return funid;
	}

	public void setFunid(String funid) {
		this.funid = funid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperationimage() {
		return operationimage;
	}

	public void setOperationimage(String operationimage) {
		this.operationimage = operationimage;
	}
	public String getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(String isCustom) {
		this.isCustom = isCustom;
	}

}
