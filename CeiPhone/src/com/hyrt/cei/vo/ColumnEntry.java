package com.hyrt.cei.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.ceiphone.WelcomeActivity;

public class ColumnEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String parentId;
	private String name;
	private String operationImage;
	private String background;
	private String logo;
	private String loginid;
	private String userId;
	private String loginName;
	private String wColor;
	private String issueTime;
	private String password;
	private String type;
	private String path;
	//非必须业务的定制与否
	private boolean isSelected;
	//描述内容
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	//版本默认的业务集合
	private List<ColumnEntry> columnEntryChilds = 
		new ArrayList<ColumnEntry>();
	//用户定义下载的应用
	private List<ColumnEntry> selectColumnEntryChilds = 
		new ArrayList<ColumnEntry>();
	//智慧海内的应用
	private List<ColumnEntry> witSeaColumns = new ArrayList<ColumnEntry>();
	
	/**
	 * 根据父级Id获取子元素,传入空值获取一级
	 * @param parentId
	 * @return
	 */
	public List<ColumnEntry> getEntryChildsForParent(String parentId){
		if(parentId==null&&columnEntryChilds.size()>0){
			parentId = columnEntryChilds.get(0).getId();
		}
		List<ColumnEntry> columnEntrys = new ArrayList<ColumnEntry>();
		for(int i=1;i<columnEntryChilds.size();i++){
			ColumnEntry columnEntry = columnEntryChilds.get(i);
			if(columnEntry.getParentId() != null && columnEntry.getParentId().equals(parentId)){
				columnEntrys.add(columnEntry);
			}
		}
		return columnEntrys;
	}
	/**
	 * 根据Id获取父级名字
	 * @return
	 */
	public String getFunNameByID(String Id){
		for(int i=1;i<columnEntryChilds.size();i++){
			ColumnEntry columnEntry = columnEntryChilds.get(i);
			if(columnEntry.getId() != null && columnEntry.getId().equals(Id)){
				return columnEntry.getName();
			}
		}
		return null;
	}
	/**
	 * 根据模块名去找模块实体
	 * @return
	 */
	public ColumnEntry getColByName(String name){
		for(int i=1;i<columnEntryChilds.size();i++){
			ColumnEntry columnEntry = columnEntryChilds.get(i);
			if(columnEntry.getName().equals(name)){
				return columnEntry;
			}
		}
		columnEntryChilds.clear();
		selectColumnEntryChilds.clear();
		String result = WriteOrRead.read(MyTools.nativeData,
				WelcomeActivity.INITRESOURCES_FILENAME);
		XmlUtil.parseInitResources(result, this);
		// 请求个人资源100%
		result = WriteOrRead.read(MyTools.nativeData,
				WelcomeActivity.INITSELFRESOURCES_FILENAME);
		for(int i=1;i<columnEntryChilds.size();i++){
			ColumnEntry columnEntry = columnEntryChilds.get(i);
			if(columnEntry.getName().equals(name)){
				return columnEntry;
			}
		}
		return null;
	}
	
	/**
	 *  
	 * @param name 模块名
	 * @param parentId 父级id
	 * @return
	 */
	public ColumnEntry getColByName(String name,String parentId){
		for(int i=1;i<columnEntryChilds.size();i++){
			ColumnEntry columnEntry = columnEntryChilds.get(i);
			if(columnEntry.getName().equals(name) && columnEntry.getPath().contains(parentId)){
				return columnEntry;
			}
		}
		return null;
	}
	
	
	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取用户定制的业务
	 * @return
	 */
	public List<ColumnEntry> getSelectColumnEntryChilds() {
		return selectColumnEntryChilds;
	}
    
	public void setSelectColumnEntryChilds(List<ColumnEntry> selectColumnEntryChilds) {
		this.selectColumnEntryChilds = selectColumnEntryChilds;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperationImage() {
		return operationImage;
	}

	public void setOperationImage(String operationImage) {
		this.operationImage = operationImage;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getwColor() {
		return wColor;
	}

	public void setwColor(String wColor) {
		this.wColor = wColor;
	}

	public String getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(String issueTime) {
		this.issueTime = issueTime;
	}

	public List<ColumnEntry> getColumnEntryChilds() {
		return columnEntryChilds;
	}

	public void setColumnEntryChilds(List<ColumnEntry> columnEntryChilds) {
		this.columnEntryChilds = columnEntryChilds;
	}

	public List<ColumnEntry> getWitSeaColumns() {
		return witSeaColumns;
	}

	public void setWitSeaColumns(List<ColumnEntry> witSeaColumns) {
		this.witSeaColumns = witSeaColumns;
	}
	
}
