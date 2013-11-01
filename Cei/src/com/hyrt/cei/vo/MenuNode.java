package com.hyrt.cei.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单元素的节点
 *
 */
public class MenuNode implements java.io.Serializable{

	private static final long serialVersionUID = 2552654659301840163L;
	
	//获取内容的id
	private String id;
	//菜单栏内容
	private String content;
	//该节点的子节点
	private List<MenuNode> menuNodeChilds = new ArrayList<MenuNode>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<MenuNode> getMenuNodeChilds() {
		return menuNodeChilds;
	}
	public void setMenuNodeChilds(List<MenuNode> menuNodeChilds) {
		this.menuNodeChilds = menuNodeChilds;
	}
	
	
}
