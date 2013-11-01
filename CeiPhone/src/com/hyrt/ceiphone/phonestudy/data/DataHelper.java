package com.hyrt.ceiphone.phonestudy.data;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Environment;
import android.os.Message;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.phonestudy.FoundationActivity;
import com.hyrt.ceiphone.phonestudy.KindsActivity;
import com.hyrt.ceiphone.phonestudy.NominateActivity;
import com.hyrt.ceiphone.phonestudy.PhoneStudyActivity;

public class DataHelper {

	private FoundationActivity activity;
	private String currentFunctionId;

	public DataHelper(FoundationActivity activity) {
		this.activity = activity;
	}

	/**
	 * 加载免费课件的数据
	 */
	public void loadFreeData() {
		// 检查sd卡是否存在不存在的话，则退出
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			MyTools.exitShow(activity, ((Activity) activity).getWindow()
					.getDecorView(), "sd卡不存在！");
			activity.finish();
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				ColumnEntry columnEntry = ((CeiApplication) (activity
						.getApplication())).columnEntry;
				if(columnEntry.getColByName(FoundationActivity.FREE_NAME,columnEntry.getColByName(FoundationActivity.MODEL_NAME).getId())==null)
						return;
				// 获取数据
				if (((CeiApplication) activity.getApplication()).isNet()) {
					String result = Service.queryPhoneFunctionTree(
							columnEntry.getColByName(
									FoundationActivity.FREE_NAME,columnEntry.getColByName(FoundationActivity.MODEL_NAME).getId()).getId(),
							"kj", "androidpad");
					if (XmlUtil.parseReturnCode(result).equals("")) {
						activity.courses.clear();
						activity.coursewares.clear();
						XmlUtil.parseCoursewares(result, activity.courses);
						Message message = activity.dataHandler.obtainMessage();
						for (int i = 0; i < activity.courses.size(); i++) {
							Courseware courseware = activity.courses.get(i);
							courseware.setParentId(columnEntry.getColByName(
									FoundationActivity.FREE_NAME).getId());
							courseware.setFree(true);
							((CeiApplication) (activity.getApplication())).dataHelper
									.saveCourseware(courseware);
						}
						message.arg1 = FoundationActivity.LVDATA_KEY;
						activity.dataHandler.sendMessage(message);
					} else {
						Message message = activity.dataHandler.obtainMessage();
						message.arg1 = FoundationActivity.NO_NET;
						activity.dataHandler.sendMessage(message);
					}
				} else {
					try {
						Courseware courseware = new Courseware();
						courseware.setParentId(columnEntry.getColByName(
								FoundationActivity.FREE_NAME,columnEntry.getColByName(FoundationActivity.MODEL_NAME).getId()).getId());
						activity.courses = ((CeiApplication) (activity
								.getApplication())).dataHelper
								.getCoursewares(courseware);
						Message message = activity.dataHandler.obtainMessage();
						message.arg1 = FoundationActivity.LVDATA_KEY;
						activity.dataHandler.sendMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 获取某一业务下的课件列表
	 * 
	 * @param functionId
	 */
	public void loadDataByServiceId(final String functionId) {
		if (functionId.equals(currentFunctionId))
			return;
		currentFunctionId = functionId;
		// 检查sd卡是否存在不存在的话，则退出
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			MyTools.exitShow(activity, ((Activity) activity).getWindow()
					.getDecorView(), "sd卡不存在！");
			activity.finish();
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 获取数据
					if (((CeiApplication) activity.getApplication()).isNet()) {
						String result = Service.queryPhoneFunctionTree(
								functionId, "kj");
						if (XmlUtil.parseReturnCode(result).equals("")) {
							List<Courseware> interimCoursewares = new ArrayList<Courseware>();
							XmlUtil.parseCoursewares(result, interimCoursewares);
							result = Service
									.queryCourse(((CeiApplication) (activity
											.getApplication())).columnEntry
											.getUserId());
							List<Courseware> selfselCoursewares = new ArrayList<Courseware>();
							XmlUtil.parseCoursewares(result, selfselCoursewares);
							for (int i = 0; i < interimCoursewares.size(); i++) {
								for (int j = 0; j < selfselCoursewares.size(); j++) {
									if (interimCoursewares
											.get(i)
											.getClassId()
											.equals(selfselCoursewares.get(j)
													.getClassId())) {
										interimCoursewares.get(i)
												.setSelfCourse(true);
									}
								}
							}
							Message message = activity.dataHandler
									.obtainMessage();
							WriteOrRead.write(result, MyTools.nativeData,
									NominateActivity.SERVICE_DATA + functionId);
							message.arg1 = FoundationActivity.LVDATA_KEY;
							if (!currentFunctionId.equals(functionId))
								return;
							activity.courses.clear();
							activity.courses.addAll(interimCoursewares);
							activity.dataHandler.sendMessage(message);
						} else {
							Message message = activity.dataHandler
									.obtainMessage();
							message.arg1 = FoundationActivity.NO_NET;
							activity.dataHandler.sendMessage(message);
						}
					} else {
						String result = WriteOrRead.read(MyTools.nativeData,
								NominateActivity.SERVICE_DATA + functionId);
						if (XmlUtil.parseReturnCode(result).equals("")) {
							XmlUtil.parseCoursewares(result, activity.courses);
							Message msg = activity.dataHandler.obtainMessage();
							msg.arg1 = FoundationActivity.LVDATA_KEY;
							activity.dataHandler.sendMessage(msg);
						} else {
							Message msg = activity.dataHandler.obtainMessage();
							msg.arg1 = FoundationActivity.NO_NET;
							activity.dataHandler.sendMessage(msg);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 根据课件种类获取课件列表
	public void loadClassesByKind(final String functionId) {
		if (functionId.equals(currentFunctionId))
			return;
		currentFunctionId = functionId;
		new Thread(new Runnable() {
			public void run() {
				List<Courseware> selfselCourseware = new ArrayList<Courseware>();
				List<Courseware> interimCoursewares = new ArrayList<Courseware>();
				if (((CeiApplication) activity.getApplication()).isNet()) {
					String result = Service.queryClassTypeByClass(functionId, 1);
					if (XmlUtil.parseReturnCode(result).equals("")) {
						XmlUtil.parseCoursewares(result, interimCoursewares);
						result = Service
								.queryCourse(((CeiApplication) (activity
										.getApplication())).columnEntry
										.getUserId());
						XmlUtil.parseCoursewares(result, selfselCourseware);
						for (int i = 0; i < interimCoursewares.size(); i++) {
							for (int j = 0; j < selfselCourseware.size(); j++) {
								if (interimCoursewares
										.get(i)
										.getClassId()
										.equals(selfselCourseware.get(j)
												.getClassId())) {
									interimCoursewares.get(i).setSelfCourse(true);
								}
							}
						}
						Message msg = activity.dataHandler.obtainMessage();
						WriteOrRead.write(result, MyTools.nativeData,
								KindsActivity.KIND_DATA + functionId);
						msg.arg1 = FoundationActivity.LVDATA_KEY;
						if (!currentFunctionId.equals(functionId))
							return;
						activity.courses.clear();
						activity.courses.addAll(interimCoursewares);
						activity.dataHandler.sendMessage(msg);
					} else {
						Message msg = activity.dataHandler.obtainMessage();
						msg.arg1 = FoundationActivity.NO_NET;
						activity.dataHandler.sendMessage(msg);
					}
				} else {
					String result = WriteOrRead.read(MyTools.nativeData,
							KindsActivity.KIND_DATA + functionId);
					if (XmlUtil.parseReturnCode(result).equals("")) {
						XmlUtil.parseCoursewares(result, activity.courses);
						Message msg = activity.dataHandler.obtainMessage();
						msg.arg1 = FoundationActivity.LVDATA_KEY;
						activity.dataHandler.sendMessage(msg);
					} else {
						Message msg = activity.dataHandler.obtainMessage();
						msg.arg1 = FoundationActivity.NO_NET;
						activity.dataHandler.sendMessage(msg);
					}
				}
			}
		}).start();
	}

	// 搜索课件列表
	public void loadClassesBySearch(final String className) {
		new Thread(new Runnable() {

			private StringBuilder functionIds;
			List<Courseware> selfselCoursewares;

			// 初始化请求数据
			private void initSendData() {
				ColumnEntry columnEntry = ((CeiApplication) (activity
						.getApplication())).columnEntry;
				ColumnEntry phoneStudyCol = columnEntry
						.getColByName(FoundationActivity.MODEL_NAME);
				functionIds = new StringBuilder(phoneStudyCol.getId());
				selfselCoursewares = new ArrayList<Courseware>();
				for (int i = 0; i < columnEntry.getColumnEntryChilds().size(); i++) {
					ColumnEntry entryChild = columnEntry.getColumnEntryChilds()
							.get(i);
					if (entryChild.getPath() != null
							&& entryChild.getPath().contains(
									phoneStudyCol.getId())) {
						functionIds.append("," + entryChild.getId());
					}
				}
				activity.coursewares.clear();
			}

			public void run() {
				initSendData();
				if (((CeiApplication) activity.getApplication()).isNet()) {
					String result = Service.queryClassName(className,
							functionIds.toString());
					XmlUtil.parseCoursewares(result, activity.courses);
					result = Service.queryCourse(((CeiApplication) (activity
							.getApplication())).columnEntry.getUserId());
					XmlUtil.parseCoursewares(result, selfselCoursewares);
					for (int i = 0; i < activity.courses.size(); i++) {
						for (int j = 0; j < selfselCoursewares.size(); j++) {
							if (activity.courses
									.get(i)
									.getClassId()
									.equals(selfselCoursewares.get(j)
											.getClassId())) {
								activity.courses.get(i).setSelfCourse(true);
							}
						}
						((CeiApplication) activity.getApplication()).dataHelper
								.saveCourseware(activity.courses.get(i));
					}
				} else {
					Courseware courseware = new Courseware();
					courseware.setName(className);
					activity.courses = ((CeiApplication) activity
							.getApplication()).dataHelper
							.getCoursewares(courseware);
				}
				Message msg = activity.dataHandler.obtainMessage();
				msg.arg1 = FoundationActivity.LVDATA_KEY;
				activity.dataHandler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * 获取最新课件排序的列表
	 */
	public void getNewData() {

		new Thread(new Runnable() {

			private StringBuilder functionIds;
			private ColumnEntry columnEntry;

			// 初始化请求数据
			private void initSendData() {
				columnEntry = ((CeiApplication) (activity.getApplication())).columnEntry;
				ColumnEntry phoneStudyCol = columnEntry
						.getColByName(FoundationActivity.MODEL_NAME);
				functionIds = new StringBuilder(phoneStudyCol.getId());
				for (int i = 0; i < columnEntry.getColumnEntryChilds().size(); i++) {
					ColumnEntry entryChild = columnEntry.getColumnEntryChilds()
							.get(i);
					if (entryChild.getPath() != null
							&& entryChild.getPath().contains(
									phoneStudyCol.getId())) {
						functionIds.append("," + entryChild.getId());
					}
				}
			}

			public void run() {
				activity.courses.clear();
				activity.coursewares.clear();
				if (((CeiApplication) activity.getApplication()).isNet()) {
					initSendData();
					String result = "";
					if (columnEntry.getColumnEntryChilds().size() > 0) {
						result = Service.queryClassByTime(columnEntry
								.getColumnEntryChilds().get(0).getId(),
								functionIds.toString());
						WriteOrRead.write(result, MyTools.nativeData,
								PhoneStudyActivity.NEWCLASS_FILENAME);
					}
					if (XmlUtil.parseReturnCode(result).equals("")) {
						XmlUtil.parseCoursewares(result, activity.courses);
						result = Service
								.queryCourse(((CeiApplication) (activity
										.getApplication())).columnEntry
										.getUserId());
						List<Courseware> selfselCoursewares = new ArrayList<Courseware>();
						XmlUtil.parseCoursewares(result, selfselCoursewares);
						for (int i = 0; i < activity.courses.size(); i++) {
							for (int j = 0; j < selfselCoursewares.size(); j++) {
								if (activity.courses
										.get(i)
										.getClassId()
										.equals(selfselCoursewares.get(j)
												.getClassId())) {
									activity.courses.get(i).setSelfCourse(true);
								}
							}
						}
						Message msg = activity.dataHandler.obtainMessage();
						msg.arg1 = FoundationActivity.LVDATA_KEY;
						activity.dataHandler.sendMessage(msg);
					} else {
						Message msg = activity.dataHandler.obtainMessage();
						msg.arg1 = FoundationActivity.NO_NET;
						activity.dataHandler.sendMessage(msg);
					}
				} else {
					String result = WriteOrRead.read(MyTools.nativeData,
							PhoneStudyActivity.NEWCLASS_FILENAME);
					if (XmlUtil.parseReturnCode(result).equals("")) {
						XmlUtil.parseCoursewares(result, activity.courses);
						Message msg = activity.dataHandler.obtainMessage();
						msg.arg1 = FoundationActivity.LVDATA_KEY;
						;
						activity.dataHandler.sendMessage(msg);
					} else {
						Message msg = activity.dataHandler.obtainMessage();
						msg.arg1 = FoundationActivity.NO_NET;
						activity.dataHandler.sendMessage(msg);
					}
				}
			}
		}).start();
	}

	// 获取自选课课程列表
	public void getSelCourseData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				activity.courses.clear();
				activity.coursewares.clear();
				if (((CeiApplication) activity.getApplication()).isNet()) {
					String result = Service.queryCourse(((CeiApplication) (activity
							.getApplication())).columnEntry.getUserId());
					XmlUtil.parseErrorCoursewares(result, activity.courses);
					for (int i = 0; i < activity.courses.size(); i++) {
						activity.courses.get(i).setSelfCourse(true);
						activity.courses.get(i).setSelfPage(true);
						((CeiApplication) (activity.getApplication())).dataHelper
								.saveCourseware(activity.courses.get(i));
					}
				} else {
					Courseware couseware = new Courseware();
					couseware.setSelfCourse(true);
					activity.courses = ((CeiApplication) (activity
							.getApplication())).dataHelper
							.getCoursewares(couseware);
				}
				activity.allCoursewares.addAll(activity.courses);
				Message msg = activity.dataHandler.obtainMessage();
				msg.arg1 = FoundationActivity.LVDATA_KEY;
				activity.dataHandler.sendMessage(msg);
			}
		}).start();
	}

	// 获取讨论组列表
	public void getSayListData() {
		new Thread(new Runnable() {
			public void run() {
				String result = Service
						.querySchoolForumInfo(((CeiApplication) activity
								.getApplication()).columnEntry.getUserId());
				Message message = activity.dataHandler.obtainMessage();
				activity.courses.clear();
				activity.coursewares.clear();
				if (((CeiApplication) activity.getApplication()).isNet()) {
					if (XmlUtil.parseReturnCode(result).equals("-1")) {
						message.arg1 = FoundationActivity.NO_NET;
					} else {
						message.arg1 = FoundationActivity.LVDATA_KEY;
						XmlUtil.parseErrorCoursewares(result, activity.courses);
						for (int i = 0; i < activity.courses.size(); i++) {
							Courseware courseware = activity.courses.get(i);
							courseware.setSay(true);
							((CeiApplication) (activity.getApplication())).dataHelper
									.saveCourseware(courseware);
						}
					}
				} else {
					Courseware courseware = new Courseware();
					courseware.setSay(true);
					activity.courses = ((CeiApplication) (activity
							.getApplication())).dataHelper
							.getCoursewares(courseware);
				}
				activity.dataHandler.sendMessage(message);
			}
		}).start();
	}

	// 获取个人学习记录
	public void getStudyRecords() {
		new Thread(new Runnable() {
			public void run() {
				activity.courses.clear();
				activity.coursewares.clear();
				activity.allCoursewares.clear();
				CeiApplication ceiApplication = (CeiApplication) (activity
						.getApplication());
				String userId = ((CeiApplication) (activity.getApplication())).columnEntry
						.getUserId();
				if (userId == null || userId.equals(""))
					return;
				if (((CeiApplication) activity.getApplication()).isNet()) {
					String result = Service.queryUserClassTime(userId);
					XmlUtil.parseCoursewareTimes(result, activity.courses);
					ceiApplication.dataHelper.savePlayRecords(activity.courses);
					List<Courseware> beforeCoursewares = new ArrayList<Courseware>();
					beforeCoursewares.addAll(activity.courses);
					activity.courses.clear();
					List<Courseware> coursewares = ceiApplication.dataHelper
							.getStudyRecord();
					activity.courses.addAll(coursewares);
					for (int i = 0; i < activity.courses.size(); i++) {
						for (int j = 0; j < beforeCoursewares.size(); j++) {
							if (activity.courses
									.get(i)
									.getClassId()
									.equals(beforeCoursewares.get(j)
											.getClassId())) {
								activity.courses.get(i).setDownPath(
										beforeCoursewares.get(j).getDownPath());
								activity.courses.get(i).setLookPath(
										beforeCoursewares.get(j).getLookPath());
								activity.courses.get(i).setFullName(
										beforeCoursewares.get(j).getFullName());
								activity.courses.get(i).setPath(
										beforeCoursewares.get(j).getPath());
								activity.courses.get(i).setClassLevel(
										beforeCoursewares.get(j).getClassLevel());
								activity.courses.get(i).setKey(
										beforeCoursewares.get(j).getKey());
								activity.courses.get(i).setIscompleted(
										beforeCoursewares.get(j).getIscompleted() == null?"0":beforeCoursewares.get(j).getIscompleted());
							}
						}
						activity.courses.get(i).setFree(true);
					}
				} else {
					activity.courses = ceiApplication.dataHelper
							.getStudyRecord();
				}
				activity.allCoursewares.addAll(activity.courses);
				Message msg = activity.dataHandler.obtainMessage();
				msg.arg1 = FoundationActivity.LVDATA_KEY;
				activity.dataHandler.sendMessage(msg);
			}
		}).start();
	}

}
