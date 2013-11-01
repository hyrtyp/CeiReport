package com.hyrt.cei.vo;

public class ReportpaitElement {
	public static String ID="_id";
	public static String REPORTPORTID="report_port_id";
	public static String TITLE="outlineTitle";
	public static String HASPARENT="mhasParent";
	public static String HASCHILD="mhasChild";
	public static String PARENTID="parent";
	public static String LEVEL="level";
	public static String EXPANDED="expanded";
	private String id;
	private String outlineTitle ;
	private boolean mhasParent; 
	private boolean mhasChild ;
	private String parent;
	private int level;
	private boolean expanded; 
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOutlineTitle() {
		return outlineTitle;
	}

	public void setOutlineTitle(String outlineTitle) {
		this.outlineTitle = outlineTitle;
	}

	public boolean isMhasParent() {
		return mhasParent;
	}

	public void setMhasParent(boolean mhasParent) {
		this.mhasParent = mhasParent;
	}

	public boolean isMhasChild() {
		return mhasChild;
	}

	public void setMhasChild(boolean mhasChild) {
		this.mhasChild = mhasChild;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}


	public ReportpaitElement(){}
	
}
