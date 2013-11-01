package com.hyrt.cei.ui.information;

import java.io.Serializable;


public class funId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String funid;
	
	public funId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public funId(String funid) {
		super();
		this.funid = funid;
	}

	public String getFunid() {
		return funid;
	}

	public void setFunid(String funid) {
		this.funid = funid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "funId [funid=" + funid + "]";
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return this.funid.equals(((funId)o).getFunid());
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return funid.hashCode();
	}
}
