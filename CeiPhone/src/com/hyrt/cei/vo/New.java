package com.hyrt.cei.vo;

import java.io.Serializable;

/**
 * 政经资讯内的新闻
 * @author Administrator
 *
 */
public class New implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String time;
	private String title;
	private String subhead;
	private String author;
	private String ppath;
	private String funname;
	private String isfree;
	public New() {
		super();
		// TODO Auto-generated constructor stub
	}
	public New(String id, String classid, String ischeck, String title,
			String subhead, String author, String ppath) {
		super();
		this.id = id;
		this.title = title;
		this.subhead = subhead;
		this.author = author;
		this.ppath = ppath;
	}
	
	public String getIsfree() {
		return isfree;
	}
	public void setIsfree(String isfree) {
		this.isfree = isfree;
	}
	public String getFunname() {
		return funname;
	}
	public void setFunname(String funname) {
		this.funname = funname;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPpath() {
		return ppath;
	}
	public void setPpath(String ppath) {
		this.ppath = ppath;
	}
	
}
