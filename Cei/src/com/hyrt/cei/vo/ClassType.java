package com.hyrt.cei.vo;

import java.io.Serializable;

public class ClassType implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final String ID = "_id";
	public static final String CLASSID = "class_id";
	public static final String PARENTID = "parent_id";
	public static final String CONTENT = "content";
	
	private String classId;
	private String parentId;
	private String content;
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
