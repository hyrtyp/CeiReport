package com.hyrt.cei.dzb.ui.vo;

/**
 *首页加载元素的图片实体 
 */
public class LRBitmap {
	
	//是否为左面的图片
	private boolean isLeft;
	//图片的名字
	private String name;
	//图片上下的优先级
	private int level;
	
	public boolean isLeft() {
		return isLeft;
	}
	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
