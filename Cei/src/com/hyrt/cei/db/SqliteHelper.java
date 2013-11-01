package com.hyrt.cei.db;

import com.hyrt.cei.vo.ClassType;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Preload;
import com.hyrt.cei.vo.Report;
import com.hyrt.cei.vo.WitSea;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper {

	public static final String TB_WITSEA_NAME = "witsea";
	public static final String TB_PRELOAD_NAME = "preload";
	public static final String TB_IMAGERESOURCE_NAME = "image_resource";
	public static final String TB_REPORT_NAME="report_self";
	public static final String TB_COURSEWARE_NAME = "courseware";
	public static final String TB_CLASSTYPE_NAME = "class_type";
	public static final String TB_BUYRESOURCE_NAME = "buy_resource";
	public static final String TB_STUDYRECORD_NAME = "study_record";
	
	public static final String TB_ECONDATA_NAME = "econ_data";
	public static final String TB_ALLREPORT_NAME = "report";//缓存报告表

	public SqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	// 创建表
	@Override
	public void onCreate(SQLiteDatabase db) {
		//智慧海业务表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_WITSEA_NAME + "("
				+ WitSea.ID + " TEXT PRIMARY KEY," + WitSea.NAME + " TEXT,"
				+ WitSea.IMAGE + " TEXT," + WitSea.ISORDER + " TEXT" + ")");
		//阅读报告表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_REPORT_NAME + "("
				+ Report.ID + " INTEGER PRIMARY KEY," + Report.REPORT_ID + " VERCHAR,"
				+ Report.REPORT_AUTHOR + " VERCHAR,"+ Report.REPORT_NAME + " VERCHAR,"
				+ Report.REPORT_TIME + " VERCHAR," + Report.REPORT_READ_TIME + " VERCHAR," 
				+ Report.REPORTPRICE + " VERCHAR," +Report.REPORTPATH + " VERCHAR," 
				+ Report.REPORT_IMGPATH + " VERCHAR,"+ Report.REPORT_MULU + " VERCHAR,"
				+ Report.REPORT_KEY+ " VERCHAR,"
				+ Report.REPORT_DOWNLOAD+ " VERCHAR,"
				+ Report.REPORT_SIZE+ " VERCHAR,"
				+ Report.REPORT_ISLOAD+ " VERCHAR,"
				+ Report.REPORT_PARTITION_ID+ " VERCHAR,"
				+ Report.REPORT_INTRO+ " VERCHAR,"
				+ Report.REPORT_FILENAME+ " VERCHAR"
				+")");
		//所有报告表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_ALLREPORT_NAME + "("
				+ Report.ID + " INTEGER PRIMARY KEY," + Report.REPORT_ID + " VERCHAR,"
				+ Report.REPORT_AUTHOR + " VERCHAR,"+ Report.REPORT_NAME + " VERCHAR,"
				+ Report.REPORT_TIME + " VERCHAR," + Report.REPORT_READ_TIME + " VERCHAR," 
				+ Report.REPORTPRICE + " VERCHAR," +Report.REPORTPATH + " VERCHAR," 
				+ Report.REPORT_IMGPATH + " VERCHAR,"+ Report.REPORT_MULU + " VERCHAR,"
				+ Report.REPORT_KEY+ " VERCHAR,"
				+ Report.REPORT_DOWNLOAD+ " VERCHAR,"
				+ Report.REPORT_SIZE+ " VERCHAR,"
				+ Report.REPORT_ISLOAD+ " VERCHAR,"
				+ Report.REPORT_PARTITION_ID+ " VERCHAR,"
				+ Report.REPORT_INTRO+ " VERCHAR"
				+")");
		//图片缓存表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_IMAGERESOURCE_NAME + "("
				+ ImageResourse.ID + " INTEGER  PRIMARY KEY," + ImageResourse.ICON + " BLOB,"
				+ ImageResourse.ICONID + " VERCHAR,"
				+ ImageResourse.ICONTIME + " VERCHAR,"
				+ ImageResourse.ICONTYPE + " VERCHAR" + ")");
		//课件表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_COURSEWARE_NAME + "("
				+ Courseware.ID + " INTEGER  PRIMARY KEY," 
				+ Courseware.CLASSID + " VERCHAR,"
				+ Courseware.NAME + " VERCHAR,"
				+ Courseware.INTRO + " VERCHAR,"
				+ Courseware.TEACHERNAME + " VERCHAR,"
				+ Courseware.CLASSLENGTH + " VERCHAR,"
				+ Courseware.PROTIME + " VERCHAR,"
				+ Courseware.CLASSLEVEL + " VERCHAR,"
				+ Courseware.ISSELFCOURSE + " VERCHAR,"
				+ Courseware.ISSAYCOURSE + " VERCHAR,"
				+ Courseware.PARENTID + " VERCHAR"+ ")");
		//课件类型表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_CLASSTYPE_NAME + "("
				+ ClassType.ID + " INTEGER  PRIMARY KEY," 
				+ ClassType.CLASSID + " VERCHAR,"
				+ ClassType.PARENTID + " VERCHAR,"
				+ ClassType.CONTENT + " VERCHAR"+ ")");
		//课件缓存表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_PRELOAD_NAME + "("
				+ Preload.ID + " INTEGER PRIMARY KEY,"
				+ Preload.LOAD_PLAYID + " VERCHAR," 
				+ Preload.LOAD_PLAYTITLE + " VERCHAR,"
				+ Preload.LOAD_CURRENTBYTE + " INTEGER," + Preload.LOAD_SUMBYTE
				+ " INTEGER," + Preload.LOAD_URL + " VERCHAR,"
				+ Preload.LOAD_LOCALPATH + " VERCHAR,"
				+ Preload.LOAD_FINISH + " INTEGER," 
				+ Preload.LOADING + " INTEGER,"
				+ Preload.NEW_POSTION + " INTEGER," 
				+ Preload.LOAD_PLAYTITLE_BELOW + " VERCHAR,"
				+ Preload.LOAD_PARENTID + " VERCHAR," 
				+ Preload.PASS_KEY + " TEXT," 
				+ Preload.CLASS_LENGTH + " VERCHAR"+")");
		//课件学习记录
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_STUDYRECORD_NAME + "("
				+ Courseware.ID + " INTEGER  PRIMARY KEY," 
				+ Courseware.CLASSID + " VERCHAR,"
				+ Courseware.NAME + " VERCHAR,"
				+ Courseware.INTRO + " VERCHAR,"
				+ Courseware.TEACHERNAME + " VERCHAR,"
				+ Courseware.CLASSLENGTH + " VERCHAR,"
				+ Courseware.PROTIME + " VERCHAR,"
				+ Courseware.STUTIME + " VERCHAR," 
				+ Courseware.TIMEPOINT + " VERCHAR,"
				+ Courseware.UPLOADTIME + " VERCHAR,"
				+ Courseware.ISCOMPLETED + " VERCHAR,"
				+ Courseware.FULLNAME + " VERCHAR,"
				+ Courseware.ORDERTIME + " VERCHAR"
				+ ")");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_PRELOAD_NAME + "("
				+ Report.ID + " INTEGER PRIMARY KEY," + Report.REPORT_ID
				+ " VERCHAR," + Report.REPORT_NAME + " VERCHAR,"
				+ Report.REPORT_AUTHOR + " INTEGER," + Report.REPORT_TIME
				+ " INTEGER," + Report.REPORTPRICE + " INTEGER"+ ")");
		//报告下载日志
		 db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog " +
		 		"(id integer primary key autoincrement, downpath varchar(100), " +
		 		"threadid INTEGER, downlength INTEGER)");

		Log.e("Database", "onCreate");
	}

	// 更新表
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_STUDYRECORD_NAME);
		db.execSQL("DROP TABLE IF EXISTS filedownlog");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_STUDYRECORD_NAME + "("
				+ Courseware.ID + " INTEGER  PRIMARY KEY," 
				+ Courseware.CLASSID + " VERCHAR,"
				+ Courseware.NAME + " VERCHAR,"
				+ Courseware.INTRO + " VERCHAR,"
				+ Courseware.TEACHERNAME + " VERCHAR,"
				+ Courseware.CLASSLENGTH + " VERCHAR,"
				+ Courseware.PROTIME + " VERCHAR,"
				+ Courseware.STUTIME + " VERCHAR," 
				+ Courseware.TIMEPOINT + " VERCHAR,"
				+ Courseware.UPLOADTIME + " VERCHAR,"
				+ Courseware.ISCOMPLETED + " VERCHAR,"
				+ Courseware.FULLNAME + " VERCHAR," 
				+ Courseware.ORDERTIME + " VERCHAR"
				+ ")");
		onCreate(db);
		Log.e("Database", "onUpgrade");
	}

	// 更新列
	public void updateColumn(SQLiteDatabase db, String oldColumn,
			String newColumn, String typeColumn) {
		try {
			db.execSQL("ALTER TABLE " + TB_WITSEA_NAME + " CHANGE " + oldColumn
					+ " " + newColumn + " " + typeColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}