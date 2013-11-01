package com.hyrt.cei.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementNews implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private String subhead;
	private String time;
	private String id;
	public AnnouncementNews() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnnouncementNews(String title, String subhead, String time, String id) {
		super();
		this.title = title;
		this.subhead = subhead;
		this.time = time;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "AnnouncementNews [title=" + title + ", subhead=" + subhead
				+ ", time=" + time + ", id=" + id + "]";
	}

	
}
