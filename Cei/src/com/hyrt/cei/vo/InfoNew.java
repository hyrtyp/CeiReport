package com.hyrt.cei.vo;

import java.io.Serializable;

public class InfoNew implements Serializable{

	/**
	 * 资讯
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String subhead;
	private String time;
	private String imagepath;
	private String isfree;
	private String functionId;
	//是否显示收藏操作
	private String isCollect;
	
	public InfoNew() {
		super();
		// TODO Auto-generated constructor stub
	}
	public InfoNew(String id, String title, String subhead, String time,
			String imagepath,String isfree,String isCollect,String functionId) {
		super();
		this.id = id;
		this.title = title;
		this.subhead = subhead;
		this.time = time;
		this.imagepath = imagepath;
		this.isfree = isfree;
		this.isCollect = isCollect;
		this.functionId = functionId;
	}
	
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public String getIsfree() {
		return isfree;
	}
	public void setIsfree(String isfree) {
		this.isfree = isfree;
	}
	public void setCollect(String isCollect) {
		this.isCollect = isCollect;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubhead() {
		return subhead;
	}
	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getImagepath() {
		return imagepath;
	}
	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}
	@Override
	public String toString() {
		return "InfoNew [id=" + id + ", title=" + title + ", subhead="
				+ subhead + ", time=" + time + ", imagepath=" + imagepath
				+ ", isfree=" + isfree + ", functionId=" + functionId
				+ ", isCollect=" + isCollect + "]";
	}
	
}
