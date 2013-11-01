package com.hyrt.cei.ui.phonestudy;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.ui.phonestudy.adapter.GridViewAdapter;
import com.hyrt.cei.ui.phonestudy.adapter.PhoneStudyAdapter;
import com.hyrt.cei.ui.phonestudy.view.MenuGridView;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 推荐课件
 * 
 */
public class NominateActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	// 课件集合
	private List<Courseware> coursewares = new ArrayList<Courseware>();
	private List<Courseware> courses = new ArrayList<Courseware>();
	// 业务集合
	private List<ColumnEntry> columnEntries;
	private static final int LVDATA_KEY = 1;
	private static final int NO_NET = 2;
	private ListView lv;
	private PhoneStudyAdapter phoneStudyAdapter;
	private MenuGridView gridView;
	// 当前查询的业务课件id
	private String oldFunctionId = "";
	// 当前查询的业务课件id
	private String currentFunctionId;
	// 当前页码
	private int index = 0;
	// 用户名
	private String loginName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_nominate);
		SharedPreferences settings = getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		initBottom();
		for (int i = 0; i < HomePageActivity.phoneStudyContainer.size(); i++) {
			try {
				NominateActivity isNominateActivity = (NominateActivity) (HomePageActivity.phoneStudyContainer
						.get(i));
				isNominateActivity.finish();
			} catch (Exception e) {
			}
		}
		HomePageActivity.phoneStudyContainer.add(this);
		registEvent();
		loadData();
	}

	@Override
	protected void onDestroy() {
		HomePageActivity.phoneStudyContainer.remove(this);
		super.onDestroy();
	}

	// 更多按钮
	private LinearLayout footer;

	private void loadData() {
		ColumnEntry columnEntry = ((CeiApplication) (getApplication())).columnEntry;
		if(columnEntry
				.getColByName(HomePageActivity.PHONE_NAME,columnEntry.getColByName(HomePageActivity.MODEL_NAME).getId())
						== null)
						return;
		columnEntries = columnEntry.getEntryChildsForParent(columnEntry
				.getColByName(HomePageActivity.PHONE_NAME,columnEntry.getColByName(HomePageActivity.MODEL_NAME).getId()).getId());
		lv = (ListView) findViewById(R.id.phone_study_listview);
		footer = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.phone_study_listview_bottom, null);
		footer.findViewById(R.id.phone_study_morebtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						index++;
						for (int i = index * 20; i < (index + 1) * 20
								&& i < courses.size(); i++) {
							if (i == courses.size() - 1)
								footer.setVisibility(View.GONE);
							coursewares.add(courses.get(i));
						}
						phoneStudyAdapter.notifyDataSetChanged();
					}
				});
		lv.addFooterView(footer);
		GridViewAdapter gridViewAdapter = new GridViewAdapter(this,
				columnEntries);
		gridView = (MenuGridView) findViewById(R.id.phone_study_gridview);
		gridView.setAdapter(gridViewAdapter, 7);
		gridView.setOnItemClickListener(this);
		findViewById(R.id.phone_study_progressLl).setVisibility(View.VISIBLE);
		loadDataForView(columnEntries.get(0).getId());
	}

	Handler dataHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			findViewById(R.id.phone_study_progressLl).setVisibility(View.GONE);
			switch (msg.arg1) {
			case LVDATA_KEY:
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
				phoneStudyAdapter = new PhoneStudyAdapter(
						NominateActivity.this,
						R.layout.phone_study_listview_item, coursewares, lv,true);
				lv.setAdapter(phoneStudyAdapter);
				lv.setSelection(0);
				break;
			case NO_NET:
				MyTools.exitShow(NominateActivity.this, ((Activity)NominateActivity.this).getWindow().getDecorView(), "网络有问题！");
				break;
			}
		}
	};

	/**
	 * 获取某一业务下的课件列表
	 * 
	 * @param functionId
	 */
	private void loadDataForView(final String functionId) {
		// 检查sd卡是否存在不存在的话，则退出
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			MyTools.exitShow(this, ((Activity)this).getWindow().getDecorView(),  "sd卡不存在!");
			this.finish();
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 获取数据
				if (((CeiApplication) getApplication()).isNet()) {
					String result = Service.queryPhoneFunctionTree(functionId,"kj");
					if (XmlUtil.parseReturnCode(result).equals("")) {
						XmlUtil.parseCoursewares(result, courses);
						result = Service
								.queryCourse(((CeiApplication) (getApplication())).columnEntry
										.getUserId());
						List<Courseware> selfselCoursewares = new ArrayList<Courseware>();
						XmlUtil.parseCoursewares(result, selfselCoursewares);
						for (int i = 0; i < courses.size(); i++) {
							for (int j = 0; j < selfselCoursewares.size(); j++) {
								if (courses
										.get(i)
										.getClassId()
										.equals(selfselCoursewares.get(j)
												.getClassId())) {
									courses.get(i).setSelfCourse(true);
								}
							}
							courses.get(i).setParentId(functionId);
							((CeiApplication) getApplication()).dataHelper
									.saveCourseware(courses.get(i));
						}
						Message message = dataHandler.obtainMessage();
						message.arg1 = LVDATA_KEY;
						dataHandler.sendMessage(message);
					} else {
						Message message = dataHandler.obtainMessage();
						message.arg1 = NO_NET;
						dataHandler.sendMessage(message);
					}
				} else {
					Courseware courseware = new Courseware();
					courseware.setParentId(functionId);
					courses = ((CeiApplication) (NominateActivity.this
							.getApplication())).dataHelper
							.getCoursewares(courseware);
					Message message = dataHandler.obtainMessage();
					message.arg1 = LVDATA_KEY;
					dataHandler.sendMessage(message);
				}
			}
		}).start();
	}

	private void registEvent() {
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.phone_study_refresh).setOnClickListener(this);
		findViewById(R.id.phone_study_search_btn).setOnClickListener(this);
		findViewById(R.id.phone_study_downmanager).setOnClickListener(this);
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
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.phone_study_search_btn:
			intent = new Intent(this, SearchCourseActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_downmanager:
			intent = new Intent(this, PreloadActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study:
			intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
			break;
		case R.id.phone_study_new:
			intent = new Intent(this, HomePageActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_refresh:
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
			if (!loginName.equals(""))
				startActivity(intent);
			else
				MyTools.exitShow(NominateActivity.this, ((Activity)NominateActivity.this).getWindow().getDecorView(), "请登陆后查看！");
			break;
		case R.id.phone_study_study:
			intent = new Intent(this, PlayRecordCourseActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				MyTools.exitShow(NominateActivity.this, ((Activity)NominateActivity.this).getWindow().getDecorView(), "请登陆后查看！");
			break;
		case R.id.phone_study_say:
			intent = new Intent(this, SayGroupListActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				MyTools.exitShow(NominateActivity.this, ((Activity)NominateActivity.this).getWindow().getDecorView(), "请登陆后查看！");
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String functionId = columnEntries.get(position).getId();
		currentFunctionId = functionId;
		if (oldFunctionId.equals(currentFunctionId))
			return;
		oldFunctionId = currentFunctionId;
		for (int i = 0; i < parent.getChildCount(); i++) {
			RelativeLayout rl = (RelativeLayout) parent.getChildAt(i);
			if (i == position) {
				((ImageView) rl.getChildAt(0))
						.setImageResource(R.drawable.phone_study_menu_select);
				((TextView) rl.getChildAt(1)).setTextColor(Color.WHITE);
			} else {
				((ImageView) rl.getChildAt(0)).setImageDrawable(null);
				((TextView) rl.getChildAt(1)).setTextColor(Color.BLUE);
			}
		}
		index = 0;
		courses.clear();
		coursewares.clear();
		loadDataForView(functionId);
	}

}
