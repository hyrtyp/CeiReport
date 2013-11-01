package com.hyrt.ceiphone.phonestudy;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.adapter.PhoneStudyAdapter;
import com.hyrt.ceiphone.phonestudy.data.DataHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

public class FoundationActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	// 维护activity集合
	public static final List<Activity> activitys = new ArrayList<Activity>();
	// 获取免费课件的数据标志
	public static final int FREE_DATA_KEY = 1;
	// 获取详细课件的数据标志
	public static final int DETAIL_DATA_KEY = 2;
	// 获取业务课件的数据标志
	public static final int SERVICE_DATA_KEY = 3;
	// 获取种类课件的数据标志
	public static final int KIND_DATA_KEY = 4;
	// 获取课件搜索的数据标志
	public static final int SEARCH_DATA_KEY = 5;
	// 获取最新课件的数据标志
	public static final int NEW_DATA_KEY = 6;
	// 获取自选课件的数据标志
	public static final int SELF_DATA_KEY = 7;
	// 获取讨论组课件的数据标志
	public static final int SAY_DATA_KEY = 8;
	// 获取学习记录课件的数据标志
	public static final int RECORD_DATA_KEY = 9;
	// 课件下载的标志
	public static final int PRELOAD_DATA_KEY = 10;
	// 讨论详细的标志
	public static final int SAYGROUP_DATA_KEY = 11;
	// 当前页面
	protected int CURRENT_KEY;
	// 数据列表视图控件
	public ListView phoneStudyListView;
	// 当前页码
	public int index = 0;
	// 更多数据按钮
	//private LinearLayout footer;
	private View footer;
	// 原始全部的数据列表
	public List<Courseware> allCoursewares = new ArrayList<Courseware>();
	// 分页列表数据
	public List<Courseware> coursewares = new ArrayList<Courseware>();
	// 适配器
	public BaseAdapter phoneStudyAdapter;
	// 所有的列表数据
	public List<Courseware> courses = new ArrayList<Courseware>();
	// 所有的列表数据
	// 获取数据的工具类并通知handler
	private DataHelper dataHelper;
	// 当前课件列表所对应的课件种类id
	public String currentFunctionId;

	public static final String WELLCLASS_NAME = "置顶课件";
	public static String MODEL_NAME = "移动学习";
	public static final String PHONE_NAME = "移动课堂";
	public static final String FREE_NAME = "免费课件";
	public static final int LVDATA_KEY = 1;
	public static final int NO_NET = 2;
	public static final String FLASH_GATE = "/apad.html";
	public static final String FLASH_POSTFIX = ".zip";
	public static final String FLASH_UNPOSTFIX = ".yepeng";
	// 用户名
	private String loginName;

	// 数据视图附加handler
	public Handler dataHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.arg1) {
			case LVDATA_KEY:
				coursewares.clear();
				if(courses.size() == 0){
					footer.setVisibility(View.GONE);
					MyTools.exitShow(FoundationActivity.this, FoundationActivity.this.getWindow().getDecorView(), "没有查到您需要的信息!");
				}
				if(CURRENT_KEY == SELF_DATA_KEY || CURRENT_KEY == RECORD_DATA_KEY){
					for (int i = index * 888; i < (index + 1) * 888
							&& i < courses.size(); i++) {
						if (i == courses.size() - 1) {
							footer.setVisibility(View.GONE);
						} else {
							if (footer.getVisibility() == View.GONE)
								footer.setVisibility(View.VISIBLE);
						}
						coursewares.add(courses.get(i));
					}
				}else{
					for (int i = index * 20; i < (index + 1) * 20
							&& i < courses.size(); i++) {
						if (i == courses.size() - 1) {
							footer.setVisibility(View.GONE);
						} else {
							if (footer.getVisibility() == View.GONE)
								footer.setVisibility(View.VISIBLE);
						}
						coursewares.add(courses.get(i));
					}
				}
				if(phoneStudyAdapter == null){
				switch (CURRENT_KEY) {
				case FREE_DATA_KEY:
					phoneStudyAdapter = new PhoneStudyAdapter(
							FoundationActivity.this,
							R.layout.phone_study_listivewitem_one, coursewares,
							phoneStudyListView,false);
					break;
				case SELF_DATA_KEY:
					phoneStudyAdapter = new PhoneStudyAdapter(
							FoundationActivity.this,
							R.layout.phone_study_listivewitem_two, coursewares,
							phoneStudyListView,allCoursewares,true);
					break;
				case NEW_DATA_KEY:
					phoneStudyAdapter = new PhoneStudyAdapter(
							FoundationActivity.this,
							R.layout.phone_study_listivewitem_one, coursewares,
							phoneStudyListView,false);
					break;
				case SERVICE_DATA_KEY:
					phoneStudyAdapter = new PhoneStudyAdapter(
							FoundationActivity.this,
							R.layout.phone_study_listivewitem_two, coursewares,
							phoneStudyListView,true);
					break;
				case KIND_DATA_KEY:
					phoneStudyAdapter = new PhoneStudyAdapter(
							FoundationActivity.this,
							R.layout.phone_study_listivewitem_two, coursewares,
							phoneStudyListView,false);
					break;
				case SEARCH_DATA_KEY:
					phoneStudyAdapter = new PhoneStudyAdapter(
							FoundationActivity.this,
							R.layout.phone_study_listivewitem_three,
							coursewares, phoneStudyListView,false);
					break;
				case SAY_DATA_KEY:
					phoneStudyAdapter = new PhoneStudyAdapter(
							FoundationActivity.this,
							R.layout.phone_study_listivewitem_four,
							coursewares, phoneStudyListView,false);
					break;
				case RECORD_DATA_KEY:
					phoneStudyAdapter = new PhoneStudyAdapter(
							FoundationActivity.this,
							R.layout.phone_study_listivewitem_five,
							coursewares, phoneStudyListView,true);
					break;
				}
				phoneStudyListView.setAdapter(phoneStudyAdapter);
				}else{
					phoneStudyAdapter.notifyDataSetChanged();
				}
				break;
			case NO_NET:
				MyTools.exitShow(FoundationActivity.this, ((Activity)FoundationActivity.this).getWindow().getDecorView(),  "网络有问题!");
				break;
			}
		}
	};

	/**
	 * 做初始化工作
	 */
	@Override
	protected void onResume() {
		SharedPreferences settings = getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		activitys.add(this);
		/*if (activitys.size() > 10) {
			activitys.get(0).finish();
			activitys.remove(0);
		}*/
		if (dataHelper == null)
			dataHelper = new DataHelper(this);
		if (coursewares.size() == 0 || CURRENT_KEY == RECORD_DATA_KEY) {
			initChangedElements();
			registCommonEvent();
			getDataForListView();
			/*
			 * new Handler().postDelayed(new Runnable() {
			 * 
			 * @Override public void run() { new Progresser().alertProgress(); }
			 * }, 200);
			 */

		}
		super.onResume();
	}

	/**
	 * 初始化变动的页面元素
	 */
	private void initChangedElements() {
		switch (CURRENT_KEY) {
		case NEW_DATA_KEY:
			((TextView) findViewById(R.id.phone_study_title))
					.setText("移动学习");
			break;
		case FREE_DATA_KEY:
			((TextView) findViewById(R.id.phone_study_title))
					.setText("免费学习");
			break;
		case PRELOAD_DATA_KEY:
			((TextView) findViewById(R.id.phone_study_title))
					.setText("下载管理");
			break;
		case DETAIL_DATA_KEY:
			((TextView) findViewById(R.id.phone_study_title))
					.setText("课件详细");
			break;
		case SERVICE_DATA_KEY:
			((TextView) findViewById(R.id.phone_study_title))
					.setText("推荐课程");
			break;
		case KIND_DATA_KEY:
			((TextView) findViewById(R.id.phone_study_title))
					.setText("课程分类");
			break;
		case SEARCH_DATA_KEY:
			((TextView) findViewById(R.id.phone_study_title))
					.setText("课件搜索");
			break;
		case SELF_DATA_KEY:
			((TextView) findViewById(R.id.phone_study_title))
					.setText("自选课程");
			break;
		case SAY_DATA_KEY:
			((TextView) findViewById(R.id.phone_study_title))
					.setText("课程讨论");
			break;
		case SAYGROUP_DATA_KEY:
			((TextView) findViewById(R.id.phone_study_title))
					.setText("课程讨论");
			break;
		case RECORD_DATA_KEY:
			((TextView) findViewById(R.id.phone_study_title))
					.setText("课程学习");
			break;
		}
		registEventForTopFirst(CURRENT_KEY);
	}

	// 为头部第一个按钮注册事件
	private void registEventForTopFirst(int currentPage) {
		if (currentPage == NEW_DATA_KEY) {
			((TextView) findViewById(R.id.phone_study_basetitle))
					.setText("中经智库");
			((TextView) findViewById(R.id.phone_study_basetitle))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							for(int i=0;i<activitys.size();i++){
								activitys.get(i).finish();
							}
						}
					});
		} else {
			((TextView) findViewById(R.id.phone_study_basetitle)).setText("移动学习");
			((TextView) findViewById(R.id.phone_study_basetitle))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(FoundationActivity.this,PhoneStudyActivity.class);
							startActivity(intent);
						}
					});
		}
	}

	/**
	 * 为公共控件注册事件
	 */
	private void registCommonEvent() {
		LinearLayout bottomParent = (LinearLayout) findViewById(R.id.bottoms_parent);
		for (int i = 0; bottomParent != null
				&& i < bottomParent.getChildCount(); i++) {
			bottomParent.getChildAt(i).setOnClickListener(this);
		}
		findViewById(R.id.phone_study_search).setOnClickListener(this);
	}

	/**
	 * 获取listview列表数据
	 */
	private void getDataForListView() {
		if (phoneStudyListView == null) {
			phoneStudyListView = (ListView) findViewById(R.id.phone_study_listview);
			if (phoneStudyListView == null)
				return;
			//footer = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.phone_study_listview_bottom, null);
			footer = findViewById(R.id.phone_study_morebtn);
			footer.setOnClickListener(this);
			//footer.findViewById(R.id.phone_study_morebtn).setOnClickListener(this);
			/*try {
				phoneStudyListView.removeFooterView(footer);
			} catch (Exception e) {
				e.printStackTrace();
			}
			phoneStudyListView.addFooterView(footer);*/
		}
		if (phoneStudyListView != null) {
			switch (CURRENT_KEY) {
			case FREE_DATA_KEY:
				dataHelper.loadFreeData();
				break;
			case NEW_DATA_KEY:
				dataHelper.getNewData();
				break;
			case SELF_DATA_KEY:
				footer.setVisibility(View.GONE);
				dataHelper.getSelCourseData();
				break;
			case SAY_DATA_KEY:
				dataHelper.getSayListData();
				break;
			case RECORD_DATA_KEY:
				dataHelper.getStudyRecords();
			case KIND_DATA_KEY:
				footer.setVisibility(View.GONE);
				break;
			case SEARCH_DATA_KEY: 
				footer.setVisibility(View.GONE);
				break;
			}
		}
	}

	public void getServiceDataByServiceId(String serviceId) {
		index = 0;
		if (dataHelper == null)
			dataHelper = new DataHelper(this);
		dataHelper.loadDataByServiceId(serviceId);
	}

	public void getServiceDataByKindId(String kindId) {
		index = 0;
		if (dataHelper == null)
			dataHelper = new DataHelper(this);
		dataHelper.loadClassesByKind(kindId);
	}

	public void getServiceDataByClassName(String className) {
		index = 0;
		if (dataHelper == null)
			dataHelper = new DataHelper(this);
		dataHelper.loadClassesBySearch(className);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.phone_study_self:
			if (CURRENT_KEY == SELF_DATA_KEY)
				return;
			Intent intent11 = new Intent(this, SelfActivity.class);
			if (!loginName.equals(""))
				startActivity(intent11);
			else
				MyTools.exitShow(FoundationActivity.this, ((Activity)FoundationActivity.this).getWindow().getDecorView(),  "请登陆后查看！");
			break;
		case R.id.phone_study_say:
			if (CURRENT_KEY == SAY_DATA_KEY)
				return;
			Intent intent12 = new Intent(this, SayActivity.class);
			if (!loginName.equals(""))
				startActivity(intent12);
			else
				MyTools.exitShow(FoundationActivity.this, ((Activity)FoundationActivity.this).getWindow().getDecorView(),  "请登陆后查看！");
			break;
		case R.id.phone_study_down:
			if (CURRENT_KEY == PRELOAD_DATA_KEY)
				return;
			Intent intent22 = new Intent(this, PreloadActivity.class);
			startActivity(intent22);
			break;
		case R.id.phone_study_kind:
			if (CURRENT_KEY == KIND_DATA_KEY)
				return;
			Intent intent1 = new Intent(this, KindsActivity.class);
			startActivity(intent1);
			break;
		case R.id.phone_study_free:
			if (CURRENT_KEY == FREE_DATA_KEY)
				return;
			Intent intent3 = new Intent(this, FreeActivity.class);
			startActivity(intent3);
			break;
		case R.id.phone_study_nominate:
			if (CURRENT_KEY == SERVICE_DATA_KEY)
				return;
			Intent intent = new Intent(this, NominateActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_mymore:
			if (CURRENT_KEY == RECORD_DATA_KEY)
				return;
			Intent intent4 = new Intent(this, StudyRecordActivity.class);
			if (!loginName.equals(""))
				startActivity(intent4);
			else
				MyTools.exitShow(FoundationActivity.this, ((Activity)FoundationActivity.this).getWindow().getDecorView(),  "请登陆后查看！");
			break;
		case R.id.phone_study_search:
			if (CURRENT_KEY == SEARCH_DATA_KEY)
				return;
			Intent intent2 = new Intent(this, SearchActivity.class);
			startActivity(intent2);
			break;
		case R.id.phone_study_morebtn:
			if(phoneStudyAdapter == null)
				return;
			index++;
			switch (CURRENT_KEY) {
			case KIND_DATA_KEY:
				new Thread(new Runnable() {

					@Override
					public void run() {
						List<Courseware> selfselCourseware = new ArrayList<Courseware>();
						List<Courseware> moreCoursewares = new ArrayList<Courseware>();
						String result = Service.queryClassTypeByClass(
								currentFunctionId, index + 1);
						if (XmlUtil.parseReturnCode(result).equals("")) {
							XmlUtil.parseCoursewares(result, moreCoursewares);
							result = Service
									.queryCourse(((CeiApplication) (getApplication())).columnEntry
											.getUserId());
							XmlUtil.parseCoursewares(result, selfselCourseware);
							for (int i = 0; i < moreCoursewares.size(); i++) {
								for (int j = 0; j < selfselCourseware.size(); j++) {
									if (moreCoursewares
											.get(i)
											.getClassId()
											.equals(selfselCourseware.get(j)
													.getClassId())) {
										moreCoursewares.get(i).setSelfCourse(
												true);
									}
								}
								moreCoursewares.get(i).setParentId(
										currentFunctionId);
								((CeiApplication) getApplication()).dataHelper
										.saveCourseware(courses.get(i));
							}
							courses.addAll(moreCoursewares);
							Message msg = dataHandler.obtainMessage();
							msg.arg1 = LVDATA_KEY;
							dataHandler.sendMessage(msg);
						} else {
							Message msg = dataHandler.obtainMessage();
							msg.arg1 = NO_NET;
							dataHandler.sendMessage(msg);
						}
					}
				}).start();
				break;
			default:
				if(CURRENT_KEY == SELF_DATA_KEY || CURRENT_KEY == RECORD_DATA_KEY){
					for (int i = index * 888; i < (index + 1) * 888
							&& i < courses.size(); i++) {
						if (i == courses.size() - 1)
							footer.setVisibility(View.GONE);
						coursewares.add(courses.get(i));
					}
				}else{
					for (int i = index * 20; i < (index + 1) * 20
							&& i < courses.size(); i++) {
						if (i == courses.size() - 1)
							footer.setVisibility(View.GONE);
						coursewares.add(courses.get(i));
					}
				}
				
				phoneStudyAdapter.notifyDataSetChanged();
				break;
			}
		}
	}

	/**
	 * 做销毁工作
	 */
	@Override
	protected void onDestroy() {
		activitys.remove(this);
		super.onDestroy();
	}

	/**
	 * 
	 * 转圈
	 */
	class Progresser {
		private PopupWindow popWin;

		private void alertProgress() {
			View popView = FoundationActivity.this.getLayoutInflater().inflate(
					R.layout.phone_study_progress, null);
			popWin = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			popWin.setFocusable(true);
			popWin.showAtLocation(findViewById(R.id.full_view), Gravity.CENTER,
					0, 0);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					popWin.dismiss();
				}
			}, 2000);
		}

	}

}
