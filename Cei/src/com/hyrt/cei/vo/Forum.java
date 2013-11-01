package com.hyrt.cei.vo;

import java.util.ArrayList;
import java.util.List;

public class Forum {
	
	/*<bbsmotif>
    <id>40288a15369f6dbb01369f6e2a3b0001</id>
    <title>论坛测试</title>
    <time>2012-04-11 11:24:18</time>
    <userid>000001</userid>
    <num>0</num>
    <follownum>0</follownum>
    <name>中经网测试人员</name>
  </bbsmotif>*/
	private String id;
	private String title;
	private String time;
	private String strTime;
	private String userid;
	private String num;
	private String followNum;
	private String name;
	private String classId;
	private String length;
	private String path;
	private String content;
	private String functionid;
	private String serial;
	private List<Forum> belowForums = new ArrayList<Forum>();
	
	public List<Forum> getBelowForums() {
		return belowForums;
	}
	public void setBelowForums(List<Forum> belowForums) {
		this.belowForums = belowForums;
	}
	
	public String getStrTime() {
		return strTime;
	}
	public void setStrTime(String strTime) {
		this.strTime = strTime;
	}
	public String getFunctionid() {
		return functionid;
	}
	public void setFunctionid(String functionid) {
		this.functionid = functionid;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getFollowNum() {
		return followNum;
	}
	public void setFollowNum(String followNum) {
		this.followNum = followNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
