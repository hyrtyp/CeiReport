package com.hyrt.cei.vo;

import android.graphics.drawable.Drawable;

/**
 *图片全局资源实体 
 *
 */
public class ImageResourse{
	
	public static final String ID = "_id";
	public static final String ICONID = "icon_id";
	public static final String ICON = "icon";
	public static final String ICONTIME = "icon_time";
	public static final String ICONTYPE = "icon_type";
	
	//数据库id
	private int id;
	//图片唯一Id
	private String iconId;
	//图片
	private Drawable icon;
	private String iconTime;
	private String iconUrl;
	//图片类型
	private String type;
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIconId() {
		return iconId;
	}
	public void setIconId(String iconId) {
		this.iconId = iconId;
	}
	
	public String getIconTime() {
		return iconTime;
	}
	public void setIconTime(String iconTime) {
		this.iconTime = iconTime;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
	
}
