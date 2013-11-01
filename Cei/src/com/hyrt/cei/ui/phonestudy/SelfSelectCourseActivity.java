package com.hyrt.cei.ui.phonestudy;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.PhoneStudySelcoAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.Preload;
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

public class SelfSelectCourseActivity extends Activity implements
		OnClickListener {

	// 自选课列表控件
	private ListView lv;
	// 自选课列表
	private List<Courseware> selfselCourseware = new ArrayList<Courseware>();
	// 当前页索引
	private int index = 0;
	// 该搜索下所有的课件列表
	private List<Courseware> courses = new ArrayList<Courseware>();
	// 自选课适配器
	private PhoneStudySelcoAdapter phoneStudyKindsAdapter;
	// 数据库查询类
	private DataHelper dataHelper;
	// 当先显示的列表种类0代表全部，1代表下载的
	private int currentLookKind = 0;
	// 当前显示全部的课件列表数据
	private List<Courseware> currentTotalCourseware = new ArrayList<Courseware>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_selfcourse);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		initBottom();
		initLvData();
		for (int i = 0; i < HomePageActivity.phoneStudyContainer.size(); i++) {
			try {
				SelfSelectCourseActivity isSelfSelectCourseActivity = (SelfSelectCourseActivity) (HomePageActivity.phoneStudyContainer
						.get(i));
				isSelfSelectCourseActivity.finish();
			} catch (Exception e) {
			}
		}
		HomePageActivity.phoneStudyContainer.add(this);
		findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SelfSelectCourseActivity.this.finish();
			}
		});
		findViewById(R.id.phone_study_search_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								SelfSelectCourseActivity.this,
								SearchCourseActivity.class);
						startActivity(intent);

					}
				});
		findViewById(R.id.phone_study_refresh).setOnClickListener(this);
		findViewById(R.id.phone_study_downmanager).setOnClickListener(this);
	}

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			findViewById(R.id.phone_study_progressLl).setVisibility(View.GONE);
			for (int i = index * 888; i < (index + 1) * 888 && i < courses.size(); i++) {
				if (i == courses.size() - 1) {
					footer.setVisibility(View.GONE);
				} else {
					if (footer.getVisibility() == View.GONE)
						footer.setVisibility(View.VISIBLE);
				}
				selfselCourseware.add(courses.get(i));
			}
			if(selfselCourseware.size() == 0)
				footer.setVisibility(View.GONE);
			phoneStudyKindsAdapter = new PhoneStudySelcoAdapter(
					SelfSelectCourseActivity.this,
					R.layout.phone_study_selfcourse_listview_item,
					selfselCourseware, lv,currentTotalCourseware);
			lv.setAdapter(phoneStudyKindsAdapter);
			super.dispatchMessage(msg);
		}
	};

	private int[][] drawables = new int[2][2];
	private LinearLayout footer;

	private void initLvData() {
		dataHelper = ((CeiApplication) getApplication()).dataHelper;
		final LinearLayout labelParent = (LinearLayout) findViewById(R.id.phone_study_labelParent);
		drawables[0][1] = R.drawable.phone_study_selfallmenu;
		drawables[0][0] = R.drawable.phone_study_selfallmenu_select;
		drawables[1][1] = R.drawable.phone_study_selfdownmenu;
		drawables[1][0] = R.drawable.phone_study_selfdownmenu_select;
		for (int i = 0; i < labelParent.getChildCount(); i++) {
			final int j = i;
			labelParent.getChildAt(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (j) {
					case 0:
						if (currentLookKind == 0)
							return;
						currentLookKind = 0;
						selfselCourseware.clear();
						selfselCourseware.addAll(currentTotalCourseware);
						phoneStudyKindsAdapter.notifyDataSetChanged();
						break;
					case 1:
						if (currentLookKind == 1)
							return;
						currentLookKind = 1;
						currentTotalCourseware.clear();
						currentTotalCourseware.addAll(selfselCourseware);
						List<Preload> preloadCourseware = dataHelper.getPreloadList();
						//已下载自选课集合
						List<Courseware> alDownCourseware = new ArrayList<Courseware>();
						for(int x=0;x<preloadCourseware.size();x++){
							if(preloadCourseware.get(x).getLoadFinish() == 1){
								for(int y=0;y<selfselCourseware.size();y++){
									if(selfselCourseware.get(y).getClassId().equals(preloadCourseware.get(x).getLoadPlayId())){
										alDownCourseware.add(selfselCourseware.get(y));
									}
								}
							}
						}
						selfselCourseware.clear();
						selfselCourseware.addAll(alDownCourseware);
						phoneStudyKindsAdapter.notifyDataSetChanged();
						break;
					}
					for (int i = 0; i < labelParent.getChildCount(); i++) {
						ImageView labelIv = (ImageView) labelParent
								.getChildAt(i);
						if (i == j) {
							labelIv.setImageResource(drawables[i][1]);
						} else {
							labelIv.setImageResource(drawables[i][0]);
						}
					}
				}
			});
		}
		lv = (ListView) findViewById(R.id.phone_study_selfcourse_listview);
		footer = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.phone_study_listview_bottom, null);
		footer.findViewById(R.id.phone_study_morebtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						index++;
						for (int i = index * 888; i < (index + 1) * 888
								&& i < courses.size(); i++) {
							if (i == courses.size() - 1)
								footer.setVisibility(View.GONE);
							selfselCourseware.add(courses.get(i));
						}
						phoneStudyKindsAdapter.notifyDataSetChanged();
					}
				});
		lv.addFooterView(footer);
		findViewById(R.id.phone_study_progressLl).setVisibility(View.VISIBLE);
		new Thread(new Runnable() {

			@Override
			public void run() {

				if (((CeiApplication) getApplication()).isNet()) {
					String result = Service
							.queryCourse(((CeiApplication) (getApplication())).columnEntry
									.getUserId());
					XmlUtil.parseErrorCoursewares(result, courses);
					for (int i = 0; i < courses.size(); i++) {
						courses.get(i).setSelfCourse(true);
						((CeiApplication) (getApplication())).dataHelper
								.saveCourseware(courses.get(i));
					}
				} else {
					Courseware couseware = new Courseware();
					couseware.setSelfCourse(true);
					courses = ((CeiApplication) (getApplication())).dataHelper
							.getCoursewares(couseware);
				}
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
		case R.id.phone_study_refresh:
			intent = new Intent(this, SelfSelectCourseActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_study:
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