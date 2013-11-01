package com.hyrt.cei.vo;

import java.io.Serializable;

/**
 * 
 * 视频课件实体
 *
 */
public class Courseware implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static final String ID = "_id";
	public static final String CLASSID = "class_id";
	public static final String NAME = "name";
	public static final String INTRO = "intro";
	public static final String TEACHERNAME = "teacher_name";
	public static final String CLASSLENGTH = "class_length";
	public static final String PROTIME = "product_time";
	public static final String CLASSLEVEL = "class_level";
	//0为非自选课 1为自选课
	public static final String ISSELFCOURSE = "isself_course";
	public static final String ISSAYCOURSE = "issay_course";
	public static final String PARENTID = "parent_id";
	public static final String STUTIME = "study_time";
	public static final String TIMEPOINT = "time_point";
	public static final String UPLOADTIME = "upload_time";
	public static final String FULLNAME = "full_name";
	//0为未完成 1为完成
	public static final String ISCOMPLETED = "is_completed";
	public static final String ORDERTIME = "order_time";
	
	private int id;
	private String classId;
	private boolean ischeck;
	private String name;
	private String intro;
	private String teacherName;
	private String path;
    private String classLength;
    private String downPath;
    private String lookPath;
    private String proTime;
    private String classLevel;
    private boolean isSelfCourse;
    private boolean isSay;
    private String parentId;
    private boolean isFree;
    private String key;
    //学习时间
    private String studyTime;
    //学习时间点
    private String timePoint;
    //需要上传的学习记录
    private int uploadTime;
    //是否观看结束
    private String iscompleted;
    //完整名称
    private String fullName;
    //排序时间
    private long orderTime;
    
	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}
   
	public String getIscompleted() {
		return iscompleted;
	}

	public void setIscompleted(String iscompleted) {
		this.iscompleted = iscompleted;
	}

	public int getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(int uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getTimePoint() {
		return timePoint;
	}

	public void setTimePoint(String timePoint) {
		this.timePoint = timePoint;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public boolean isSay() {
		return isSay;
	}

	public void setSay(boolean isSay) {
		this.isSay = isSay;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isSelfCourse() {
		return isSelfCourse;
	}

	public void setSelfCourse(boolean isSelfCourse) {
		this.isSelfCourse = isSelfCourse;
	}

	public String getBigPath() {
		return path + "/big.png";
	}

	public String getSmallPath() {
		return path + "/small.png";
	}

	public String getClassLevel() {
		return classLevel;
	}

	public void setClassLevel(String classLevel) {
		this.classLevel = classLevel;
	}

	public String getProTime() {
		return proTime;
	}

	public void setProTime(String proTime) {
		this.proTime = proTime;
	}

	public String getLookPath() {
		return lookPath;
	}

	public void setLookPath(String lookPath) {
		this.lookPath = lookPath;
	}

	public String getDownPath() {
		return downPath;
	}

	public void setDownPath(String downPath) {
		this.downPath = downPath;
	}

	public String getClassLength() {
		return classLength;
	}

	public void setClassLength(String classLength) {
		this.classLength = classLength;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getStudyTime() {
		return studyTime;
	}

	public void setStudyTime(String studyTime) {
		this.studyTime = studyTime;
	}

	public Courseware() {
		super();
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
}
