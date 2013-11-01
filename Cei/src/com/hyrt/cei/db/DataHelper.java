package com.hyrt.cei.db;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.hyrt.cei.vo.ClassType;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Preload;
import com.hyrt.cei.vo.Report;
import com.hyrt.cei.vo.WitSea;
import com.hyrt.cei.webservice.service.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DataHelper {
	// 数据库名称
	private static String DB_NAME = "cei.db";
	// 数据库版本
	private static int DB_VERSION = 2;
	private SQLiteDatabase db;
	public SqliteHelper dbHelper;

	public DataHelper(Context context) {
		dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}

	public void Close() {
		db.close();
		dbHelper.close();
	}

	/**
	 * 保存下载列表
	 */
	public void savePreload(final Preload preload) {
		new Thread(new Runnable(){

			@Override
			public void run() {
				Service.updatedownsum(preload.getLoadPlayId(),"kj");
			}}).start();
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(Preload.LOAD_PLAYID, preload.getLoadPlayId());
		contentvalues.put(Preload.LOAD_PLAYTITLE, preload.getLoadPlayTitle());
		contentvalues.put(Preload.LOAD_PLAYTITLE_BELOW,preload.getLoadPlayTitleBelow());
		contentvalues.put(Preload.LOAD_CURRENTBYTE,preload.getLoadCurrentByte());
		contentvalues.put(Preload.LOAD_SUMBYTE, preload.getLoadSumByte());
		contentvalues.put(Preload.LOAD_URL, preload.getLoadUrl());
		contentvalues.put(Preload.LOAD_LOCALPATH, preload.getLoadLocalPath());
		contentvalues.put(Preload.LOAD_FINISH, preload.getLoadFinish());
		contentvalues.put(Preload.LOADING, preload.getLoading());
		contentvalues.put(Preload.LOAD_PARENTID,preload.getLoadParentId() == null ? "" : preload.getLoadParentId());
		contentvalues.put(Preload.PASS_KEY, preload.getPassKey() == null ? "": preload.getPassKey());
		contentvalues.put(Preload.CLASS_LENGTH,preload.getClassLength() == null ? "0": preload.getClassLength());
		db.insert(SqliteHelper.TB_PRELOAD_NAME, Preload.ID, contentvalues);
		contentvalues.clear();
	}

	/**
	 * 保存图片离线资源
	 */
	public boolean saveImageResource(ImageResourse imageResource) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(ImageResourse.ICONID, imageResource.getIconId());
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		((BitmapDrawable) imageResource.getIcon()).getBitmap().compress(
				Bitmap.CompressFormat.PNG, 100, os);
		contentvalues.put(ImageResourse.ICON, os.toByteArray());
		contentvalues.put(
				ImageResourse.ICONTIME,
				imageResource.getIconTime() == null ? "" : imageResource
						.getIconTime());
		contentvalues.put(ImageResourse.ICONTYPE,
				imageResource.getType() == null ? "" : imageResource.getType());
		if (os.toByteArray().length > 100) {
			db.insert(SqliteHelper.TB_IMAGERESOURCE_NAME, "", contentvalues);// ImageResourse.ID
			return true;
		}
		contentvalues.clear();
		return false;
	}

	/**
	 * 保存课件类别
	 */
	public void saveClassType(List<ClassType> classTypes) {
		if (classTypes.size() != 0)
			db.delete(SqliteHelper.TB_CLASSTYPE_NAME, null, null);
		ContentValues values = new ContentValues();
		for (int i = 0; i < classTypes.size(); i++) {
			ClassType classType = classTypes.get(i);
			values.put(ClassType.CLASSID, classType.getClassId());
			values.put(ClassType.PARENTID, classType.getParentId() == null ? ""
					: classType.getParentId());
			values.put(ClassType.CONTENT, classType.getContent());
			db.insert(SqliteHelper.TB_CLASSTYPE_NAME, ClassType.ID, values);
			values.clear();
		}
	}

	/**
	 * 获取课件类别列表
	 */
	public List<ClassType> getClassTypes() {
		List<ClassType> classTypes = new ArrayList<ClassType>();
		Cursor cursor = db.query(SqliteHelper.TB_CLASSTYPE_NAME, null, null,
				null, null, null, ClassType.ID + " ASC");
		cursor.moveToFirst();
		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			ClassType classType = new ClassType();
			classType.setClassId(cursor.getString(1));
			classType.setParentId(cursor.getString(2));
			classType.setContent(cursor.getString(3));
			classTypes.add(classType);
			cursor.moveToNext();
		}
		cursor.close();
		return classTypes;
	}

	/**
	 * 缓存课件
	 * 
	 * @param courseware
	 */
	public void saveCourseware(Courseware courseware) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(Courseware.CLASSID,
				courseware.getClassId() == null ? "" : courseware.getClassId());
		contentvalues.put(Courseware.NAME, courseware.getName() == null ? ""
				: courseware.getName());
		contentvalues.put(Courseware.INTRO, courseware.getIntro() == null ? ""
				: courseware.getIntro());
		contentvalues.put(
				Courseware.TEACHERNAME,
				courseware.getTeacherName() == null ? "" : courseware
						.getTeacherName());
		contentvalues.put(
				Courseware.CLASSLENGTH,
				courseware.getClassLength() == null ? "" : courseware
						.getClassLength());
		contentvalues.put(Courseware.PROTIME,
				courseware.getProTime() == null ? "" : courseware.getProTime());
		contentvalues.put(
				Courseware.CLASSLEVEL,
				courseware.getClassLevel() == null ? "" : courseware
						.getClassLevel());
		contentvalues.put(Courseware.ISSELFCOURSE,
				courseware.isSelfCourse() == true ? "1" : "0");
		contentvalues.put(Courseware.ISSAYCOURSE,
				courseware.isSay() == true ? "1" : "0");
		contentvalues.put(
				Courseware.PARENTID,
				courseware.getParentId() == null ? "" : courseware
						.getParentId());
		List<Courseware> coursewares = getCoursewares(courseware);
		if (coursewares.size() == 0)
			db.insert(SqliteHelper.TB_COURSEWARE_NAME, Courseware.ID,
					contentvalues);
		else {
			courseware
					.setParentId(coursewares.get(0).getParentId() == null ? ""
							: coursewares.get(0).getParentId()
									+ courseware.getParentId() == null ? ""
									: courseware.getParentId());
			courseware.setId(coursewares.get(0).getId());
			updateCourseware(courseware);
		}
		contentvalues.clear();
	}

	/**
	 * 根据参数的值，获取指定id、类别、谈论组、自选课下的课件列表。
	 * 
	 * @return
	 */
	public List<Courseware> getCoursewares(Courseware cw) {
		Cursor cursor = null;
		if (cw.getClassId() != null && !cw.getClassId().equals("")) {
			cursor = db.query(SqliteHelper.TB_COURSEWARE_NAME, null,
					Courseware.CLASSID + "=?",
					new String[] { cw.getClassId() }, null, null, null);
		} else if (cw.getParentId() != null && !cw.getParentId().equals("")) {
			String parentId = "%%%%" + cw.getParentId() + "%%%%";
			cursor = db.query(SqliteHelper.TB_COURSEWARE_NAME, null,
					Courseware.PARENTID + " like ? ",
					new String[] { parentId }, null, null, null);
		} else if (cw.getName() != null) {
			String name = "%%%%" + cw.getName() + "%%%%";
			cursor = db.query(SqliteHelper.TB_COURSEWARE_NAME, null,
					Courseware.NAME + " like ?", new String[] { name }, null,
					null, null);
		} else if (cw.isSelfCourse()) {
			cursor = db.query(SqliteHelper.TB_COURSEWARE_NAME, null,
					Courseware.ISSELFCOURSE + "=?", new String[] { "1" }, null,
					null, null);
		} else if (cw.isSay()) {
			cursor = db.query(SqliteHelper.TB_COURSEWARE_NAME, null,
					Courseware.ISSAYCOURSE + "=?", new String[] { "1" }, null,
					null, null);
		}
		if (cursor == null)
			return new ArrayList<Courseware>();
		cursor.moveToFirst();
		List<Courseware> coursewareList = new ArrayList<Courseware>();
		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			Courseware courseware = new Courseware();
			courseware.setId(cursor.getInt(0));
			courseware.setClassId(cursor.getString(1));
			courseware.setName(cursor.getString(2));
			courseware.setIntro(cursor.getString(3));
			courseware.setTeacherName(cursor.getString(4));
			courseware.setClassLength(cursor.getString(5));
			courseware.setProTime(cursor.getString(6));
			courseware.setClassLevel(cursor.getString(7));
			courseware.setSelfCourse(cursor.getInt(8) == 1 ? true : false);
			courseware.setParentId(cursor.getString(10));
			coursewareList.add(courseware);
			cursor.moveToNext();
		}
		cursor.close();
		return coursewareList;
	}

	/**
	 * 更新指定课件的值
	 * 
	 * @param courseware
	 */
	public void updateCourseware(Courseware courseware) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(Courseware.ISSELFCOURSE,
				courseware.isSelfCourse() == true ? "1" : "0");
		contentvalues.put(Courseware.PARENTID, courseware.getParentId());
		contentvalues.put(Courseware.ISSAYCOURSE,
				courseware.isSay() == true ? "1" : "0");
		db.update(SqliteHelper.TB_COURSEWARE_NAME, contentvalues, Courseware.ID
				+ "=?", new String[] { courseware.getId() + "" });
	}

	/**
	 * 获取指定的图片资源
	 * 
	 * @param iconId
	 *            图片资源id
	 * @param type
	 *            1为大图，其他为小图。
	 * @return
	 */
	public ImageResourse getImageResource(String iconId, String type) {
		Cursor cursor = db.query(SqliteHelper.TB_IMAGERESOURCE_NAME, null,
				ImageResourse.ICONID + "=? and " + ImageResourse.ICONTYPE
						+ "=?", new String[] { iconId == null ? "" : iconId,
						type == null ? "" : type }, null, null,
				ImageResourse.ID + " DESC");
		cursor.moveToFirst();
		ImageResourse imageResource = null;
		while (!cursor.isAfterLast() && (cursor.getString(2) != null)) {
			byte[] bytes = cursor.getBlob(1);
			imageResource = new ImageResourse();
			imageResource.setId(cursor.getInt(0));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
			options.inJustDecodeBounds = false;
			int be = (int) (options.outHeight / (float) 200);
			if (be <= 0)
				be = 1;
			options.inSampleSize = be;
			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
					bytes.length, options);
			Drawable icon = new BitmapDrawable(bitmap);
			imageResource.setIcon(icon);
			imageResource.setIconId(cursor.getString(2));
			imageResource.setIconTime(cursor.getString(3));
			cursor.moveToNext();
		}
		cursor.close();
		return imageResource;
	}

	/**
	 * 更新过期图片
	 * 
	 * @param imageResource
	 */
	public void updateImageResource(ImageResourse imageResource) {
		ContentValues contentvalues = new ContentValues();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		((BitmapDrawable) imageResource.getIcon()).getBitmap().compress(
				Bitmap.CompressFormat.PNG, 100, os);
		contentvalues.put(ImageResourse.ICON, os.toByteArray());
		contentvalues.put(ImageResourse.ICONTIME,
				imageResource.getIconTime() == null ? "" : imageResource
						.getIconTime());
		if (os.toByteArray().length > 100) {
			db.update(SqliteHelper.TB_IMAGERESOURCE_NAME, contentvalues,
					ImageResourse.ID + "=?",
					new String[] { imageResource.getId() + "" });
		}
	}

	/**
	 * 获取课件视频缓存列表
	 * 
	 * @return
	 */
	public List<Preload> getPreloadList() {
		List<Preload> preloadList = new ArrayList<Preload>();
		Cursor cursor = db.query(SqliteHelper.TB_PRELOAD_NAME, null, null,
				null, null, null, Preload.ID + " DESC");
		cursor.moveToFirst();
		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			Preload preload = new Preload();
			preload.set_id(Long.valueOf(cursor.getLong(0)));
			preload.setLoadPlayId(cursor.getString(1));
			preload.setLoadPlayTitle(cursor.getString(2));
			preload.setLoadCurrentByte(Integer.valueOf(cursor.getInt(3)));
			preload.setLoadSumByte(Integer.valueOf(cursor.getInt(4)));
			preload.setLoadUrl(cursor.getString(5));
			preload.setLoadLocalPath(cursor.getString(6));
			preload.setLoadFinish(Integer.valueOf(cursor.getInt(7)));
			preload.setLoading(Integer.valueOf(cursor.getInt(8)));
			preload.setNewPostion(Integer.valueOf(cursor.getInt(9)));
			preload.setLoadPlayTitleBelow(cursor.getString(10));
			preload.setLoadParentId(cursor.getString(11));
			preload.setPassKey(cursor.getString(12));
			preload.setClassLength(cursor.getString(13));
			preloadList.add(preload);
			cursor.moveToNext();
		}
		cursor.close();
		return preloadList;
	}

	/**
	 * 更新课件视频缓存条目
	 * 
	 * @param preload
	 * @return
	 */
	public int updatePreload(Preload preload) {
		ContentValues values = new ContentValues();
		if (preload.getLoadCurrentByte() != -1)
			values.put(Preload.LOAD_CURRENTBYTE, preload.getLoadCurrentByte());
		if (preload.getLoadSumByte() != -1)
			values.put(Preload.LOAD_SUMBYTE, preload.getLoadSumByte());
		if (preload.getLoadFinish() != -1)
			values.put(Preload.LOAD_FINISH, preload.getLoadFinish());
		if (preload.getLoading() != -1)
			values.put(Preload.LOADING, preload.getLoading());
		if (preload.getNewPostion() != -1)
			values.put(Preload.NEW_POSTION, preload.getNewPostion());
		int id = db.update(SqliteHelper.TB_PRELOAD_NAME, values,Preload.LOAD_PLAYID + "=?",new String[] { preload.getLoadPlayId() });
		return id;
	}

	/**
	 * 判断该课件是否已经缓存
	 * 
	 * @param playId
	 * @return
	 */
	public Boolean hasPreload(String playId) {
		Boolean b = false;
		Cursor cursor = db.query(SqliteHelper.TB_PRELOAD_NAME, null,
				Preload.LOAD_PLAYID + "=?", new String[] { playId }, null,
				null, null);
		b = cursor.moveToFirst();
		cursor.close();
		return b;
	}
	
	/**
	 * 根据课件id获取课件信息
	 * 
	 * @param playId
	 * @return
	 */
	public Preload getPreload(String playId) {
		Preload preload = null;
		Cursor cursor = db.query(SqliteHelper.TB_PRELOAD_NAME, null,
				Preload.LOAD_PLAYID + "=?", new String[] { playId }, null,
				null, null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			preload = new Preload();
			preload.setLoadLocalPath(cursor.getString(6));
			preload.setLoadFinish(Integer.valueOf(cursor.getInt(7)));
			preload.setPassKey(cursor.getString(12));
			cursor.close();
		}
		return preload;
	}

	/**
	 * 删除指定缓存列表
	 * 
	 * @param playId
	 * @return
	 */
	public int deletePreload(String playId) {
		int id = -1;
		id = db.delete(SqliteHelper.TB_PRELOAD_NAME, Preload.LOAD_PLAYID
				+ "=?", new String[] { playId });
		return id;
	}

	public Long saveWitSeaInfo(WitSea witSea) {
		ContentValues values = new ContentValues();
		values.put(WitSea.ID, witSea.getFunid());
		values.put(WitSea.NAME, witSea.getName());
		values.put(WitSea.IMAGE, witSea.getOperationimage());
		values.put(WitSea.ISORDER, witSea.getIsCustom());
		Long uid = db.insert(SqliteHelper.TB_WITSEA_NAME, "", values);
		return uid;
	}

	/**
	 * 保存已下载报告
	 * 
	 * @param Report
	 * @return
	 */
	// nullpointE
	public Long saveReport(Report report) {
		ContentValues values = new ContentValues();
		// values.put(Report.ID, report.getId());
		values.put(Report.REPORT_ID, report.getId());
		values.put(Report.REPORT_AUTHOR, report.getAuthor());
		values.put(Report.REPORT_NAME, report.getName());
		values.put(Report.REPORT_TIME, report.getProtime());
		values.put(Report.REPORT_READ_TIME, report.getReadtime());
		values.put(Report.REPORTPRICE, report.getPrice());
		values.put(Report.REPORTPATH,report.getDatapath());
		values.put(Report.REPORT_IMGPATH, report.getPpath());
		values.put(Report.REPORT_MULU, report.getMulu());
		values.put(Report.REPORT_KEY, report.getKey());
		values.put(Report.REPORT_DOWNLOAD, report.getDownpath());
		values.put(Report.REPORT_PARTITION_ID, report.getPartitiontID());
		values.put(Report.REPORT_INTRO, report.getIntro());
		values.put(Report.REPORT_FILENAME, report.getFileName());
		Long uid = db.insert(SqliteHelper.TB_REPORT_NAME, "", values);
		return uid;
	}
	/**
	 * 保存分类下所有报告
	 * 
	 * @param report
	 * @return
	 */
	public Long saveAllReport(Report report) {
		ContentValues values = new ContentValues();
		// values.put(Report.ID, report.getId());
		values.put(Report.REPORT_ID, report.getId());
		values.put(Report.REPORT_AUTHOR, report.getAuthor());
		values.put(Report.REPORT_NAME, report.getName());
		values.put(Report.REPORT_TIME, report.getProtime());
		values.put(Report.REPORT_READ_TIME, report.getReadtime());
		values.put(Report.REPORTPRICE, report.getPrice());
		values.put(Report.REPORTPATH,report.getDatapath());
		values.put(Report.REPORT_IMGPATH, report.getPpath());
		values.put(Report.REPORT_MULU, report.getMulu());
		values.put(Report.REPORT_KEY, report.getKey());
		values.put(Report.REPORT_DOWNLOAD, report.getDownpath());
		values.put(Report.REPORT_PARTITION_ID, report.getPartitiontID());
		values.put(Report.REPORT_INTRO, report.getIntro());
		Long uid = db.insert(SqliteHelper.TB_ALLREPORT_NAME, "", values);
		return uid;
	}

	/**
	 * 已下载报告类表
	 * 
	 * @return
	 */
	public List<Report> getReportList() {
		List<Report> reportList = new ArrayList<Report>();
		Cursor cursor = db.query(SqliteHelper.TB_REPORT_NAME, null, null, null,
				null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			Report report = new Report();
			report.setId(cursor.getString(1));
			report.setAuthor(cursor.getString(2));
			report.setName(cursor.getString(3));
			report.setProtime(cursor.getString(4));
			report.setReadtime(cursor.getString(5));
			report.setPrice(cursor.getString(6));
			report.setDatapath(cursor.getString(7));
			report.setPpath(cursor.getString(8));
			report.setMulu(cursor.getString(9));
			report.setKey(cursor.getString(10));
			report.setDownpath(cursor.getString(11));
			report.setReportSize(cursor.getString(12));
			report.setIsLoad(cursor.getString(13));
			report.setPartitiontID(cursor.getString(14));
			report.setIntro(cursor.getString(15));
			report.setFileName(cursor.getString(16));
			reportList.add(report);
			cursor.moveToNext();
		}
		cursor.close();
		return reportList;
	}

	public List<Report> getAllReportListByID(String id) {
		List<Report> reportList = new ArrayList<Report>();
		Cursor cursor = db.query(SqliteHelper.TB_ALLREPORT_NAME, null, null,
				null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			if (cursor.getString(14).equals(id)) {
				Report report = new Report();
				report.setId(cursor.getString(1));
				report.setAuthor(cursor.getString(2));
				report.setName(cursor.getString(3));
				report.setProtime(cursor.getString(4));
				report.setReadtime(cursor.getString(5));
				report.setPrice(cursor.getString(6));
				report.setDatapath(cursor.getString(7));
				report.setPpath(cursor.getString(8));
				report.setMulu(cursor.getString(9));
				report.setKey(cursor.getString(10));
				report.setDownpath(cursor.getString(11));
				report.setReportSize(cursor.getString(12));
				report.setIsLoad(cursor.getString(13));
				report.setPartitiontID(cursor.getString(14));
				report.setIntro(cursor.getString(15));
				reportList.add(report);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return reportList;
	}

	/**
	 * 根据名称查报告
	 * 
	 * @return
	 */

	public List<Report> getReportListById(String name) {
		List<Report> reportList = new ArrayList<Report>();
		Cursor cursor = db.query(SqliteHelper.TB_REPORT_NAME, null, null, null,
				null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			if (cursor.getString(3).contains(name)) {
				Report report = new Report();
				report.setId(cursor.getString(1));
				report.setAuthor(cursor.getString(2));
				report.setName(cursor.getString(3));
				report.setProtime(cursor.getString(4));
				report.setReadtime(cursor.getString(5));
				report.setPrice(cursor.getString(6));
				report.setDatapath(cursor.getString(7));
				report.setPpath(cursor.getString(8));
				report.setMulu(cursor.getString(9));
				report.setKey(cursor.getString(10));
				report.setDownpath(cursor.getString(11));
				report.setReportSize(cursor.getString(12));
				report.setIsLoad(cursor.getString(13));
				report.setPartitiontID(cursor.getString(14));
				report.setIntro(cursor.getString(15));
				report.setFileName(cursor.getString(16));
				reportList.add(report);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return reportList;
	}
	/**
	 * 增加报告状态
	 * 
	 * @param report
	 * @return
	 */
	public int UpdateReportZT(Report report) {
		ContentValues values = new ContentValues();
		values.put(Report.REPORT_ISLOAD, report.getIsLoad());
		int id = db.update(SqliteHelper.TB_REPORT_NAME, values,
				Report.REPORT_ID + "=?", new String[] { report.getId() });
		return id;
	}

	public int delWitSeaInfo(String witSeaId) {
		int id = db.delete(SqliteHelper.TB_WITSEA_NAME, WitSea.ID + "=?",
				new String[] { witSeaId });
		return id;
	}

	public int UpdateWitSeaInfo(WitSea witSea) {
		ContentValues values = new ContentValues();
		values.put(WitSea.ISORDER, witSea.getIsCustom());
		int id = db.update(SqliteHelper.TB_WITSEA_NAME, values, WitSea.ID
				+ "=?", new String[] { witSea.getFunid() });
		return id;
	}

	public List<WitSea> getWitSeaList() {
		List<WitSea> witSeaList = new ArrayList<WitSea>();
		Cursor cursor = db.query(SqliteHelper.TB_WITSEA_NAME, null, null, null,
				null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			WitSea witSea = new WitSea();
			witSea.setFunid(cursor.getString(0));
			witSea.setName(cursor.getString(1));
			witSea.setOperationimage(cursor.getString(2));
			witSea.setIsCustom(cursor.getString(3));
			witSeaList.add(witSea);
			cursor.moveToNext();
		}
		cursor.close();
		return witSeaList;
	}

	public Boolean haveWitSeaInfo(String id) {
		Boolean b = false;
		Cursor cursor = db.query(SqliteHelper.TB_WITSEA_NAME, null, WitSea.ID
				+ "=?", new String[] { id }, null, null, null);
		b = cursor.moveToFirst();
		cursor.close();
		return b;
	}

	/**
	 * 删除书架中的记录
	 */
	public int delReport(String reportid) {
		int id = db.delete(SqliteHelper.TB_REPORT_NAME,
				Report.REPORT_ID + "=?", new String[] { reportid });
		return id;
	}
	
	/**
	 * 插入学习记录数据列表
	 */
	public void savePlayRecords(List<Courseware> coursewares){
		for(int i=0;i<coursewares.size();i++){
			Courseware courseware = coursewares.get(i);
			Courseware newCourseware = new Courseware();
			newCourseware.setClassId(courseware.getClassId());
			//根据课件id，查出学习时间点和需要上传的学习时长
			if(getStudyRecord(newCourseware)){
				courseware.setTimePoint(newCourseware.getTimePoint());
				//假如服务端的学习状态是未完成的话，则以本地的为主，否则则相反
				if(!"1".equals(courseware.getIscompleted()))
					courseware.setIscompleted(newCourseware.getIscompleted());
				courseware.setUploadTime(newCourseware.getUploadTime());
			}
			courseware.setOrderTime(i+1);
			insertOrUpdateStudyRecord(courseware);
		}
	}
	
	/**
	 * 插入学习记录
	 */
	private void savePlayRecord(Courseware courseware){
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(Courseware.CLASSID,courseware.getClassId() == null ? "" : courseware.getClassId());
		contentvalues.put(Courseware.NAME, courseware.getName() == null ? "" : courseware.getName());
		contentvalues.put(Courseware.INTRO, courseware.getIntro() == null ? "" : courseware.getIntro());
		contentvalues.put(Courseware.TEACHERNAME,courseware.getTeacherName() == null ? "" : courseware.getTeacherName());
		contentvalues.put(Courseware.CLASSLENGTH,courseware.getClassLength() == null ? "0" : courseware.getClassLength());
		contentvalues.put(Courseware.PROTIME,courseware.getProTime() == null ? "" : courseware.getProTime());
		contentvalues.put(Courseware.STUTIME,courseware.getStudyTime() == null ? "0" : courseware.getStudyTime());
		contentvalues.put(Courseware.TIMEPOINT,courseware.getTimePoint() == null ? "0" : courseware.getTimePoint());
		contentvalues.put(Courseware.ISCOMPLETED,courseware.getIscompleted() == null ? "0" : courseware.getIscompleted());
		contentvalues.put(Courseware.UPLOADTIME,courseware.getUploadTime());
		contentvalues.put(Courseware.FULLNAME,courseware.getFullName() == null ? "" : courseware.getFullName());
		contentvalues.put(Courseware.ORDERTIME,courseware.getOrderTime());
		db.insert(SqliteHelper.TB_STUDYRECORD_NAME, Courseware.ID, contentvalues);
		contentvalues.clear();
	} 
	
	/**
	 * 获取学习记录的列表
	 * 
	 * @return
	 */
	public List<Courseware> getStudyRecord() {
		List<Courseware> studyRecords = new ArrayList<Courseware>();
		Cursor cursor = db.query(SqliteHelper.TB_STUDYRECORD_NAME, null, null,
				null, null, null, Courseware.ORDERTIME + " ASC");
		cursor.moveToFirst();
		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			Courseware studyRecord = new Courseware();
			studyRecord.setClassId(cursor.getString(1));
			studyRecord.setName(cursor.getString(2));
			studyRecord.setIntro(cursor.getString(3));
			studyRecord.setTeacherName(cursor.getString(4));
			studyRecord.setClassLength(cursor.getString(5));
			studyRecord.setProTime(cursor.getString(6));
			studyRecord.setStudyTime(cursor.getString(7));
			studyRecord.setTimePoint(cursor.getString(8));
			studyRecord.setUploadTime(cursor.getInt(9));
			studyRecord.setIscompleted(cursor.getString(10));
			studyRecord.setFullName(cursor.getString(11));
			studyRecords.add(studyRecord);
			cursor.moveToNext();
		}
		cursor.close();
		return studyRecords;
	}
	
	/**
	 * 更新指定课件的值
	 * 
	 * @param courseware
	 */
	public void updatePlayRecord(Courseware courseware) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put(Courseware.STUTIME, courseware.getStudyTime()== null ? "0" : courseware.getStudyTime());
		contentvalues.put(Courseware.TIMEPOINT, courseware.getTimePoint()== null ? "0" : courseware.getTimePoint());
		contentvalues.put(Courseware.UPLOADTIME, courseware.getUploadTime());
		if(courseware.getOrderTime() != 500)
			contentvalues.put(Courseware.ORDERTIME,courseware.getOrderTime());
		if("1".equals(courseware.getIscompleted()))
			contentvalues.put(Courseware.ISCOMPLETED, "1");
		db.update(SqliteHelper.TB_STUDYRECORD_NAME, contentvalues, Courseware.CLASSID
				+ "=?", new String[] { courseware.getClassId() + "" });
	}
	
	/**
	 * 判断是否存在该条记录
	 * @param classId
	 * @return
	 */
	public Boolean haveStudyRecord(String classId) {
		Boolean b = false;
		Cursor cursor = db.query(SqliteHelper.TB_STUDYRECORD_NAME, null, Courseware.CLASSID+ "=?", new String[] { classId }, null, null, null);
		b = cursor.moveToFirst();
		cursor.close();
		return b;
	}
	
	/**
	 * 根据课件id该条记录
	 * @param classId
	 * @return
	 */
	public Boolean getStudyRecord(Courseware courseware) {
		Boolean b = false;
		Cursor cursor = db.query(SqliteHelper.TB_STUDYRECORD_NAME, null, Courseware.CLASSID+ "=?", 
				new String[] { courseware.getClassId() }, null, null, null);
		b = cursor.moveToFirst();
		while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
			courseware.setClassId(cursor.getString(1));
			courseware.setName(cursor.getString(2));
			courseware.setIntro(cursor.getString(3));
			courseware.setTeacherName(cursor.getString(4));
			courseware.setClassLength(cursor.getString(5));
			courseware.setProTime(cursor.getString(6));
			courseware.setStudyTime(cursor.getString(7));
			courseware.setTimePoint(cursor.getString(8));
			courseware.setUploadTime(cursor.getInt(9));
			cursor.moveToNext();
		}
		cursor.close();
		return b;
	}
	
	/**
	 * 向数据库中插入或更新学习记录
	 * @param playRecord
	 */
	public void insertOrUpdateStudyRecord(Courseware playRecord){
		if(haveStudyRecord(playRecord.getClassId())){
			updatePlayRecord(playRecord);
		}else{
			savePlayRecord(playRecord);
		}
	}

}