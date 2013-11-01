package com.hyrt.cei.ui.phonestudy;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.PhoneStudyRecordAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class PlayRecordCourseActivity extends Activity implements
		OnClickListener {

	private ListView lv;
	// 标签集合的父级元素
	private LinearLayout labelParent;
	// 标签菜单图片集合
	private int[][] drawables = new int[5][2];
	// 当前显示课程列表集合
	private List<Courseware> coursewares = new ArrayList<Courseware>();
	// 所有课程列表集合
	public List<Courseware> courses = new ArrayList<Courseware>();
	// 当前需要看的操作集合
	public List<Courseware> currentCoursewares = new ArrayList<Courseware>();
	// 当前页码
	private int index = 0;
	// 数据列表适配器
	private PhoneStudyRecordAdapter phoneStudyRecordAdapter;
	// 底部更多按钮
	private LinearLayout footer;
	// 4个操作标志位
	private final static int ALL_INDEX = 0;
	private final static int NOSTUDY_INDEX = 1;
	private final static int NOUPLOAD_INDEX = 2;
	private final static int UPLOAD_INDEX = 3;
	private final static int STUDY_INDEX = 4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_playrecord);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		initBottom();
		drawables[0][1] = R.drawable.phone_study_playrecord_m1;
		drawables[0][0] = R.drawable.phone_study_playrecord_m1_select;
		drawables[1][1] = R.drawable.phone_study_playrecord_m3;
		drawables[1][0] = R.drawable.phone_study_playrecord_m3_select;
		drawables[2][1] = R.drawable.phone_study_playrecord_m2;
		drawables[2][0] = R.drawable.phone_study_playrecord_m2_select;
		drawables[3][1] = R.drawable.phone_study_playrecord_m4;
		drawables[3][0] = R.drawable.phone_study_playrecord_m4_select;
		drawables[4][1] = R.drawable.phone_study_playrecord_m5;
		drawables[4][0] = R.drawable.phone_study_playrecord_m5_select;
		for (int i = 0; i < HomePageActivity.phoneStudyContainer.size(); i++) {
			try {
				PlayRecordCourseActivity isPlayRecordCourseActivity = (PlayRecordCourseActivity) (HomePageActivity.phoneStudyContainer
						.get(i));
				isPlayRecordCourseActivity.finish();
			} catch (Exception e) {
			}
		}
		HomePageActivity.phoneStudyContainer.add(this);
		findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PlayRecordCourseActivity.this.finish();
			}
		});
		findViewById(R.id.phone_study_search_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								PlayRecordCourseActivity.this,
								SearchCourseActivity.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.phone_study_downmanager).setOnClickListener(this);
		findViewById(R.id.phone_study_refresh).setOnClickListener(this);
	}
	
	protected void onResume() {
		courses.clear();
		coursewares.clear();
		currentCoursewares.clear();
		initLvData();
		super.onResume();
	};

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			coursewares.clear();
			for (int i = index * 888; i < (index + 1) * 888
					&& i < currentCoursewares.size(); i++) {
				if (i == currentCoursewares.size() - 1) {
					footer.setVisibility(View.GONE);
				} else {
					if (footer.getVisibility() == View.GONE)
						footer.setVisibility(View.VISIBLE);
				}
				coursewares.add(currentCoursewares.get(i));
			}
			findViewById(R.id.phone_study_progressLl).setVisibility(View.GONE);
			phoneStudyRecordAdapter = new PhoneStudyRecordAdapter(
					PlayRecordCourseActivity.this,
					R.layout.phone_study_playrecord_listview_item, coursewares,
					lv);
			lv.setAdapter(phoneStudyRecordAdapter);
		}
	};

	private void initLvData() {
		labelParent = (LinearLayout) findViewById(R.id.phone_study_labelParent);
		for (int i = 0; i < labelParent.getChildCount(); i++) {
			final int x = i;
			ImageView iv = (ImageView) labelParent.getChildAt(i);
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						switch (x) {
						case ALL_INDEX:
							currentCoursewares.clear();
							currentCoursewares.addAll(courses);
							handler.sendMessage(handler.obtainMessage());
							break;
						case NOUPLOAD_INDEX:
							currentCoursewares.clear();
							for (int j = 0; j < courses.size(); j++) {
								if (!"1".equals(courses.get(j).getIscompleted()) && courses.get(j).getUploadTime() != 0) {
									currentCoursewares.add(courses.get(j));
								}
							}
							handler.sendMessage(handler.obtainMessage());
							break;
						case NOSTUDY_INDEX:
							currentCoursewares.clear();
							for (int j = 0; j < courses.size(); j++) {
								if (!"1".equals(courses.get(j).getIscompleted())) {
									currentCoursewares.add(courses.get(j));
								}
							}
							handler.sendMessage(handler.obtainMessage());
							break;
						case UPLOAD_INDEX:
							currentCoursewares.clear();
							currentCoursewares.clear();
							for (int j = 0; j < courses.size(); j++) {
								if ("1".equals(courses.get(j).getIscompleted()) || courses.get(j).getUploadTime() == 0) {
									currentCoursewares.add(courses.get(j));
								}
							}
							handler.sendMessage(handler.obtainMessage());
							break;
						case STUDY_INDEX:
							currentCoursewares.clear();
							for (int j = 0; j < courses.size(); j++) {
								if ("1".equals(courses.get(j).getIscompleted())) {
									currentCoursewares.add(courses.get(j));
								}
							}
							handler.sendMessage(handler.obtainMessage());
							break;
						}
						for (int i = 0; i < labelParent.getChildCount(); i++) {
							ImageView iv = (ImageView) labelParent
									.getChildAt(i);
							if (i == x) {
								iv.setImageResource(drawables[i][1]);
							} else {
								iv.setImageResource(drawables[i][0]);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});
		}
		lv = (ListView) findViewById(R.id.phone_study_playrecord_listview);
		footer = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.phone_study_listview_bottom, null);
		footer.findViewById(R.id.phone_study_morebtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 翻页增加数据
						index++;
						for (int i = index * 888; i < (index + 1) * 888
								&& i < currentCoursewares.size(); i++) {
							if (i == currentCoursewares.size() - 1)
								footer.setVisibility(View.GONE);
							coursewares.add(currentCoursewares.get(i));
						}
						phoneStudyRecordAdapter.notifyDataSetChanged();
					}
				});
		lv.addFooterView(footer);
		findViewById(R.id.phone_study_progressLl).setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			public void run() {
				CeiApplication ceiApplication = (CeiApplication) (PlayRecordCourseActivity.this
						.getApplication());
				String userId = ((CeiApplication) (PlayRecordCourseActivity.this
						.getApplication())).columnEntry.getUserId();
				if (userId == null || userId.equals(""))
					return;
				if (((CeiApplication) getApplication()).isNet()) {
					String result = Service.queryUserClassTime(userId);
					XmlUtil.parseCoursewareTimes(result, courses);
					ceiApplication.dataHelper.savePlayRecords(courses);
					List<Courseware> beforeCoursewares = new ArrayList<Courseware>();
					beforeCoursewares.addAll(courses);
					courses.clear();
					List<Courseware> coursewares = ceiApplication.dataHelper
							.getStudyRecord();
					courses.addAll(coursewares);
					for (int i = 0; i < courses.size(); i++) {
						for (int j = 0; j < beforeCoursewares.size(); j++) {
							if (courses
									.get(i)
									.getClassId()
									.equals(beforeCoursewares.get(j)
											.getClassId())) {
								courses.get(i).setDownPath(
										beforeCoursewares.get(j).getDownPath());
								courses.get(i).setLookPath(
										beforeCoursewares.get(j).getLookPath());
								courses.get(i).setPath(
										beforeCoursewares.get(j).getPath());
								courses.get(i).setKey(beforeCoursewares.get(j)
											.getKey());
							}
						}
						courses.get(i).setFree(true);
					}
				} else {
					List<Courseware> cws = ceiApplication.dataHelper
							.getStudyRecord();
					if (cws != null)
						courses = cws;
				}
				currentCoursewares.addAll(courses);
				handler.sendMessage(handler.obtainMessage());
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		HomePageActivity.phoneStudyContainer.remove(this);
		super.onDestroy();
	}

	private void initBottom() {
		ImageView headIv = (ImageView) findViewById(R.id.phone_study);
		ImageView newIv = (ImageView) findViewById(R.id.phone_study_new);
		ImageView nominateIv = (ImageView) findViewById(R.id.phone_study_nominate);
		ImageView freeIv = (ImageView) findViewById(R.id.phone_study_free);
		ImageView kindIv = (ImageView) findViewById(R.id.phone_study_kind);
		ImageView selfIv = (ImageView) findViewById(R.id.phone_study_self);
		ImageView studyIv = (ImageView) findViewById(R.id.phone_study_study);
		ImageView sayIv = (ImageView) findViewById(R.id.phone_study_say);
		headIv.setOnClickListener(this);
		newIv.setOnClickListener(this);
		nominateIv.setOnClickListener(this);
		freeIv.setOnClickListener(this);
		kindIv.setOnClickListener(this);
		selfIv.setOnClickListener(this);
		studyIv.setOnClickListener(this);
		sayIv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.phone_study:
			intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
			break;
		case R.id.phone_study_downmanager:
			intent = new Intent(this, PreloadActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_new:
			intent = new Intent(this, HomePageActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_nominate:
			intent = new Intent(this, NominateActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_free:
			intent = new Intent(this, FreeActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_kind:
			intent = new Intent(this, KindsActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_self:
			intent = new Intent(this, SelfSelectCourseActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_refresh:
			intent = new Intent(this, PlayRecordCourseActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_say:
			intent = new Intent(this, SayGroupListActivity.class);
			startActivity(intent);
			break;
		}
	}
}