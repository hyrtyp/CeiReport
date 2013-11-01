package com.hyrt.cei.adapter;

import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.ui.common.WebViewUtil;
import com.hyrt.cei.ui.phonestudy.CourseDetailActivity;
import com.hyrt.cei.ui.phonestudy.PlayRecordCourseActivity;
import com.hyrt.cei.ui.phonestudy.PreloadActivity;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.BitmapManager;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Preload;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PhoneStudyRecordAdapter extends BaseAdapter {

	private int itemLayout;
	private LayoutInflater inflater;
	private List<Courseware> coursewares;
	private DataHelper dataHelper;
	private ListView lv;
	private HashMap<String, Drawable> drawables = new HashMap<String, Drawable>();
	private Activity activity;
	private BitmapManager bmpManager;
	public static final String FLASH_GATE = "/apad.html";
	public static final String FLASH_POSTFIX = ".zip";

	public PhoneStudyRecordAdapter(Activity activity, int itemLayout,
			List<Courseware> coursewares, ListView lv) {
		this.activity = activity;
		this.itemLayout = itemLayout;
		this.coursewares = coursewares;
		this.lv = lv;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dataHelper = ((CeiApplication) (activity.getApplication())).dataHelper;
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(activity.getResources(), R.drawable.courseware_default_icon));
	}

	public int getCount() {
		return coursewares.size();
	}

	public Object getItem(int position) {
		return Integer.valueOf(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// if (convertView == null) {
		holder = new ViewHolder();
		convertView = inflater.inflate(itemLayout, null);
		holder.courseIcon = (ImageView) convertView
				.findViewById(R.id.phone_study_playrecord_course_icon);
		holder.coursePlayBtn = (ImageView) convertView
				.findViewById(R.id.phone_study_playrecord_play_btn);
		holder.downloadBtn = (ImageView) convertView
				.findViewById(R.id.phone_study_playrecord_download_btn);
		holder.addCourse = (ImageView) convertView
				.findViewById(R.id.phone_study_playrecord_addcourse);
		holder.alStudyTime = (TextView) convertView
				.findViewById(R.id.phone_study_playrecord_alstudytime);
		holder.studyStatus = (TextView) convertView
				.findViewById(R.id.phone_study_playrecord_studystatus);
		holder.totalTime = (TextView) convertView
				.findViewById(R.id.phone_study_playrecord_totaltime);
		holder.tv1 = (TextView) convertView
				.findViewById(R.id.phone_study_gird_item_tv1);
		holder.tv2 = (TextView) convertView
				.findViewById(R.id.phone_study_gird_item_tv2);
		holder.tv3 = (TextView) convertView
				.findViewById(R.id.phone_study_gird_item_tv3);
		// convertView.setTag(holder);
		// } else {
		// holder = (ViewHolder) convertView.getTag();
		// }
		if (coursewares.get(position).getStudyTime().equals(""))
			coursewares.get(position).setStudyTime("0");
		if (coursewares.get(position).getClassLength().equals(""))
			coursewares.get(position).setClassLength("0");
		try {
				holder.alStudyTime.setText("已学习时间 ："
						+ coursewares.get(position).getStudyTime());
		} catch (Exception e) {

		}
		try {
			holder.studyStatus.setText(("1".equals(coursewares.get(position).getIscompleted())) ? "学习状态 ：已学完"
					: "学习状态 ：未学完");
		} catch (Exception e) {

		}
		holder.totalTime.setText("总时长 : "
				+ coursewares.get(position).getClassLength());
		holder.coursePlayBtn
				.setImageResource(R.drawable.phone_study_playrecord_study_btn);
		try {
			if (coursewares.get(position).getUploadTime() != 0
					&& !"1".equals(coursewares.get(position).getIscompleted()))
				holder.addCourse
						.setImageResource(R.drawable.phone_study_uploadcourse_btn);
			else
				holder.addCourse
						.setImageResource(R.drawable.phone_study_uploadcourse_btn_hover);
		} catch (Exception e) {
			holder.addCourse
					.setImageResource(R.drawable.phone_study_uploadcourse_btn);
			e.printStackTrace();
		}
		holder.tv1.setText(coursewares.get(position).getName());
		holder.tv2
				.setText("讲师姓名：" + coursewares.get(position).getTeacherName());
		holder.tv3.setText("发布时间：" + coursewares.get(position).getProTime());
		holder.coursePlayBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, WebViewUtil.class);
				if (coursewares.get(position).getLookPath() == null)
					coursewares.get(position).setLookPath("file:///" + 
				dataHelper.getPreload(coursewares.get(position).getClassId()).getLoadLocalPath()
							.replace(
									FLASH_POSTFIX,
									FLASH_GATE));
				intent.putExtra("path",
						coursewares.get(position).getLookPath() == null ? ""
								: coursewares.get(position).getLookPath());
				intent.putExtra("class", coursewares.get(position));
				intent.putExtra("isRecord",true);
				activity.startActivity(intent);
			}
		});
		holder.addCourse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 上传学习记录，并同步数据库的信息，更新列表
				final Handler handler = new Handler();
				try {
					if (coursewares.get(position).getUploadTime() != 0
							 && !"1".equals(coursewares.get(position).getIscompleted())) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								if (!XmlUtil
										.parseReturnCode(
												Service.saveUserClassTime(
														((CeiApplication) (activity
																.getApplication())).columnEntry
																.getUserId(),
														coursewares.get(
																position)
																.getClassId(),
														coursewares
																.get(position)
																.getUploadTime()
																+ "")).equals(
												"-1")) {
									handler.post(new Runnable() {

										@Override
										public void run() {
											try{
											coursewares.get(position).setStudyTime((Integer.parseInt(coursewares.get(position)
													.getStudyTime()) + coursewares.get(position).getUploadTime())+"");
											}catch(Exception e){
												coursewares.get(position).setStudyTime(coursewares.get(position).getUploadTime()+"");
												e.printStackTrace();
											}
											coursewares.get(position)
													.setUploadTime(0);
											((CeiApplication) (activity
													.getApplication())).dataHelper
													.updatePlayRecord(coursewares
															.get(position));
											try{
												coursewares.add(0, coursewares.remove(position));
												((PlayRecordCourseActivity)activity).courses.add(0,((PlayRecordCourseActivity)activity).courses.remove(position));
												((PlayRecordCourseActivity)activity).currentCoursewares.add(0,((PlayRecordCourseActivity)activity).currentCoursewares.remove(position));
											}catch(Exception e){
												
											}
											PhoneStudyRecordAdapter.this
													.notifyDataSetChanged();
											AlertDialog.Builder builder = new Builder(
													activity);
											builder.setMessage("上传学习记录成功 ！");
											builder.setPositiveButton(
													"确认",
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															dialog.dismiss();
														}
													});
											builder.create().show();
										}
									});
								}
							}
						}).start();
					}
				} catch (Exception e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							if (!XmlUtil
									.parseReturnCode(
											Service.saveUserClassTime(
													((CeiApplication) (activity
															.getApplication())).columnEntry
															.getUserId(),
													coursewares.get(
															position)
															.getClassId(),
													coursewares
															.get(position)
															.getUploadTime()
															+ "")).equals(
											"-1")) {
								handler.post(new Runnable() {

									@Override
									public void run() {
										coursewares.get(position)
												.setUploadTime(0);
										((CeiApplication) (activity
												.getApplication())).dataHelper
												.updatePlayRecord(coursewares
														.get(position));
										PhoneStudyRecordAdapter.this
												.notifyDataSetChanged();
										AlertDialog.Builder builder = new Builder(
												activity);
										builder.setMessage("上传学习记录成功 ！");
										builder.setPositiveButton(
												"确认",
												new DialogInterface.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														dialog.dismiss();
													}
												});
										builder.create().show();
									}
								});
							}
						}
					}).start();
					e.printStackTrace();
				}
			}
		});
		holder.courseIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, CourseDetailActivity.class);
				coursewares.get(position).setFree(true);
				intent.putExtra("coursewareInfo", coursewares.get(position));
				intent.putExtra("isRecord",true);
				activity.startActivity(intent);
			}
		});

		holder.downloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadThisCourse(coursewares.get(position));
			}
		});
		changeDownBtn(holder.downloadBtn, coursewares.get(position)
				.getClassId());

		if (coursewares.size() != 0) {
			String imageUrl = coursewares.get(position).getSmallPath();
			holder.courseIcon.setTag(imageUrl);
			if (drawables.containsKey(coursewares.get(position).getClassId())
					&& drawables.get(coursewares.get(position).getClassId()) != null) {
				holder.courseIcon.setImageDrawable(drawables.get(coursewares
						.get(position).getClassId()));
			} else {
				bmpManager.loadBitmap(imageUrl,holder.courseIcon,coursewares
						.get(position).getClassId());
			}
		}
		return convertView;
	}

	class ViewHolder {
		ImageView courseIcon;
		ImageView downloadBtn;
		ImageView coursePlayBtn;
		ImageView addCourse;
		TextView alStudyTime;
		TextView studyStatus;
		TextView totalTime;
		TextView tv1;
		TextView tv2;
		TextView tv3;
	}

	private void downloadThisCourse(final Courseware courseware) {
		alertIsSurePop(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popWin.dismiss();
				DataHelper dataHelper = ((CeiApplication) (activity
						.getApplication())).dataHelper;
				Preload preload = new Preload();
				preload.setLoadPlayId(courseware.getClassId());
				preload.setLoadCurrentByte(0);
				preload.setLoading(1);
				preload.setLoadFinish(0);
				preload.setLoadUrl(courseware.getDownPath());
				if (courseware.getDownPath() != null)
					preload.setLoadLocalPath(MyTools.RESOURCE_PATH
							+ MyTools.KJ_PARTPATH
							+ courseware
									.getDownPath()
									.substring(
											courseware
													.getDownPath()
													.lastIndexOf(
															"/",
															(courseware
																	.getDownPath()
																	.length() - 10)) + 1,
											courseware.getDownPath()
													.lastIndexOf("/")) + ".zip");
				preload.setLoadPlayTitle(courseware.getFullName());
				preload.setLoadPlayTitleBelow("讲师姓名 ： "
						+ courseware.getTeacherName() + "    发布时间 ： "
						+ courseware.getProTime());
				preload.setPassKey(courseware.getKey());
				preload.setClassLength(courseware.getClassLength());
				if (!dataHelper.hasPreload(preload.getLoadPlayId())) {
					dataHelper.savePreload(preload);
					AlertDialog.Builder builder = new Builder(activity);
					builder.setMessage("成功加入下载队列 ！");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(activity,
											PreloadActivity.class);
									activity.startActivity(intent);
								}
							});
					builder.create().show();
				} else {
					AlertDialog.Builder builder = new Builder(activity);
					builder.setMessage("下载队列已存在该剧集 ！");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(activity,
											PreloadActivity.class);
									activity.startActivity(intent);
								}
							});
					builder.create().show();
				}
			}
		});

	}

	private PopupWindow popWin;

	private void alertIsSurePop(OnClickListener clickListener) {
		View popView = activity.getLayoutInflater().inflate(
				R.layout.phone_study_issure, null);
		popView.findViewById(R.id.phone_study_issure_sure_btn)
				.setOnClickListener(clickListener);
		popView.findViewById(R.id.phone_study_issure_cancel_btn)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						popWin.dismiss();
					}
				});
		popWin = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(activity.findViewById(R.id.full_view),
				Gravity.CENTER, 0, 0);
	}

	private void changeDownBtn(View view, String classId) {
		DataHelper dataHelper = ((CeiApplication) (activity.getApplication())).dataHelper;
		ImageView downBtn = (ImageView) view;
		Preload preload = dataHelper.getPreload(classId);
		if (preload != null && preload.getLoadFinish() == 1) {
			downBtn.setOnClickListener(null);
			downBtn.setImageResource(R.drawable.phone_study_nodown_btn);
		}
	}
}
