package com.hyrt.cei.vo;

import java.io.Serializable;

public class Report  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SD_PATH="/mnt/sdcard/cei/data/bg/";
    public static final String ID="_id";
    public static final String REPORT_ID="report_id";
    public static final String REPORT_NAME="report_name";
    public static final String REPORT_AUTHOR="report_author";
    public static final String REPORT_TIME="report_time";
    public static final String REPORT_READ_TIME="report_read_time";
    public static final String REPORTPATH="report_path";
    public static final String REPORT_IMGPATH="report_imgpath";
    public static final String REPORTPRICE="report_price";
    public static final String REPORT_MULU="report_mulu";
    public static final String REPORT_KEY="report_key";//报告简码的key
    public static final String REPORT_DOWNLOAD="report_download";//报告的下载路径
    public static final String REPORT_SIZE="report_size"; //记录此报告的大小
    public static final String REPORT_ISLOAD="report_isload";//表明此报告是否下载的状态，yes已下载完，start开始下载，err下载错误
    public static final String REPORT_PARTITION_ID="report_partition_id";//报告分类id
    public static final String REPORT_INTRO="report_intro";//报告分类id
    public static final String REPORT_FILENAME="report_filename";//报告分类id
    private String id;
	private String isCheck;
	private String name;
	private String intro;
	private String author;
	private String ppath;
	private String price;
	private String protime;//发布时间
	private String readtime;//最后阅读时间
	private String datapath;//数据库文件路径
	private String downpath;
	private String mulu;
	private String key;
	private String isFree;
	private String reportSize;
	private String isLoad;
	private String partitiontID;
	private String fileName;
	private String isAsk;

	public String getIsAsk() {
		return isAsk;
	}

	public void setIsAsk(String isAsk) {
		this.isAsk = isAsk;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPartitiontID() {
		return partitiontID;
	}

	public void setPartitiontID(String partitiontID) {
		this.partitiontID = partitiontID;
	}

	public String getReportSize() {
		return reportSize;
	}

	public void setReportSize(String reportSize) {
		this.reportSize = reportSize;
	}

	public String getIsLoad() {
		return isLoad;
	}

	public void setIsLoad(String isLoad) {
		this.isLoad = isLoad;
	}

	public String getIsFree() {
		return isFree;
	}

	public void setIsFree(String isFree) {
		this.isFree = isFree;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMulu() {
		return mulu;
	}

	public void setMulu(String mulu) {
		this.mulu = mulu;
	}

	public String getReadtime() {
		return readtime;
	}

	public void setReadtime(String readtime) {
		this.readtime = readtime;
	}

	public String getDatapath() {
		return datapath;
	}

	public void setDatapath(String datapath) {
		this.datapath = datapath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPpath() {
		return ppath+"/big.png";
	}
	public String getSmallPpath() {
		return ppath+"/small.png";
	}

	public void setPpath(String ppath) {
		this.ppath = ppath;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProtime() {
		return protime;
	}

	public void setProtime(String protime) {
		this.protime = protime;
	}

	public String getDownpath() {
		return downpath;
	}

	public void setDownpath(String downpath) {
		this.downpath = downpath;
	}

	

}
