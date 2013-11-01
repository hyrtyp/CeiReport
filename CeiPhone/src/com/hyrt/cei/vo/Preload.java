package com.hyrt.cei.vo;

import java.io.Serializable;

/**
 *下载课件的实体 
 *
 */
public class Preload implements Serializable {
	
	private static final long serialVersionUID = 0xe66297979cf3983eL;
	public static final String ID = "_id";
	public static final String LOAD_PLAYID = "loadPlayId";
	public static final String LOAD_CURRENTBYTE = "loadCurrentByte";
	public static final String LOAD_PLAYTITLE = "loadPlayTitle";
	public static final String LOAD_SUMBYTE = "loadSumByte";
	public static final String LOAD_LOCALPATH = "loadLocalPath";
	public static final String LOAD_URL = "loadUrl";
	public static final String LOAD_FINISH = "loadFinish";
	public static final String LOADING = "loading";
	public static final String NEW_POSTION = "newPostion";
	public static final String LOAD_PLAYTITLE_BELOW = "loadPlayTitleBelow";
	public static final String LOAD_PARENTID = "loadParentId";
	public static final String PASS_KEY = "passKey";
	public static final String CLASS_LENGTH = "classLength";
	public static final String CLASS_LEVEL = "classLevel";

	private Long _id;
	private int loadCurrentByte = -1;
	private int loadFinish = -1;
	private String loadLocalPath;
	private String loadPlayId;
	private String loadPlayTitle;
	private int loadSumByte = -1;
	private String loadUrl;
	private int loading = -1;
	private int newPostion = -1;
	private String loadPlayTitleBelow;
	private String loadParentId;
	private String passKey;
	private String classLength;
	private String classLevel;
	
	public String getClassLevel() {
		return classLevel;
	}
	public void setClassLevel(String classLevel) {
		this.classLevel = classLevel;
	}
	public String getPassKey() {
		return passKey;
	}
	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}
	public Long get_id() {
		return _id;
	}
	public void set_id(Long _id) {
		this._id = _id;
	}
	public int getLoadCurrentByte() {
		return loadCurrentByte;
	}
	public void setLoadCurrentByte(int loadCurrentByte) {
		this.loadCurrentByte = loadCurrentByte;
	}
	public int getLoadFinish() {
		return loadFinish;
	}
	public void setLoadFinish(int loadFinish) {
		this.loadFinish = loadFinish;
	}
	public String getLoadLocalPath() {
		return loadLocalPath;
	}
	public void setLoadLocalPath(String loadLocalPath) {
		this.loadLocalPath = loadLocalPath;
	}
	public String getLoadPlayId() {
		return loadPlayId;
	}
	public void setLoadPlayId(String loadPlayId) {
		this.loadPlayId = loadPlayId;
	}
	public String getLoadPlayTitle() {
		return loadPlayTitle;
	}
	public void setLoadPlayTitle(String loadPlayTitle) {
		this.loadPlayTitle = loadPlayTitle;
	}
	public String getLoadPlayTitleBelow() {
		return loadPlayTitleBelow;
	}
	public void setLoadPlayTitleBelow(String loadPlayTitleBelow) {
		this.loadPlayTitleBelow = loadPlayTitleBelow;
	}
	public int getLoadSumByte() {
		return loadSumByte;
	}
	public void setLoadSumByte(int loadSumByte) {
		this.loadSumByte = loadSumByte;
	}
	public String getLoadUrl() {
		return loadUrl;
	}
	public void setLoadUrl(String loadUrl) {
		this.loadUrl = loadUrl;
	}
	public int getLoading() {
		return loading;
	}
	public void setLoading(int loading) {
		this.loading = loading;
	}
	public int getNewPostion() {
		return newPostion;
	}
	public void setNewPostion(int newPostion) {
		this.newPostion = newPostion;
	}
	public String getLoadParentId() {
		return loadParentId;
	}
	public void setLoadParentId(String loadParentId) {
		this.loadParentId = loadParentId;
	}
	public String getClassLength() {
		return classLength;
	}
	public void setClassLength(String classLength) {
		this.classLength = classLength;
	}
}
