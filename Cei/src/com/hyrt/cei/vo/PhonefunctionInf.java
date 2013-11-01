package com.hyrt.cei.vo;

import java.io.Serializable;

public class PhonefunctionInf implements Serializable {

	/**
	 * 首页业务内容
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String operationimage;

	public PhonefunctionInf(String id, String name, String operationimage) {
		super();
		this.id = id;
		this.name = name;
		this.operationimage = operationimage;
	}

	public PhonefunctionInf() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "PhonefunctionInf [id=" + id + ", name=" + name
				+ ", operationimage=" + operationimage + "]";
	}

}
