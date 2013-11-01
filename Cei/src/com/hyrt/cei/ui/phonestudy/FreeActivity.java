package com.hyrt.cei.ui.phonestudy;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.ui.phonestudy.adapter.PhoneStudyAdapter;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 免费课件
 * 
 */
public class FreeActivity extends Activity implements OnClickListener {

	// 课件集合
	private List<Courseware> coursewares = new ArrayList<Courseware>();
	// 业务集合
	private static final int LVDATA_KEY = 1;
	private static final int NO_NET = 2;
	private ListView lv;
	private PhoneStudyAdapter phoneStudyAdapter;
	// 当前页码
	private int index = 0;
	// 更多数据按钮
	private LinearLayout footer;
	// 用户名
	private String loginName;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_free);
		SharedPreferences settings = getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		initBottom();
		for (int i = 0; i < HomePageActivity.phoneStudyContainer.size(); i++) {
			try {
				FreeActivity isFreeActivity = (FreeActivity) (HomePageActivity.phoneStudyContainer
						.get(i));
				isFreeActivity.finish();
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

	

	private void loadData() {
		ColumnEntry columnEntry = ((CeiApplication) (getApplication())).columnEntry;
		if(columnEntry.getColByName(HomePageActivity.FREE_NAME,columnEntry.getColByName(HomePageActivity.MODEL_NAME).getId())
				== null)
			return;
		loadDataForView(columnEntry.getColByName(HomePageActivity.FREE_NAME,columnEntry.getColByName(HomePageActivity.MODEL_NAME).getId())
				.getId());
		lv = (ListView) findViewById(R.id.phone_study_listview);
		footer = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.phone_study_listview_bottom, null);
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
		findViewById(R.id.phone_study_progressLl).setVisibility(View.VISIBLE);
	}

	private List<Courseware> courses = new ArrayList<Courseware>();
	Handler dataHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			findViewById(R.id.phone_study_progressLl).setVisibility(View.GONE);
			switch (msg.arg1) {
			case LVDATA_KEY:
				if(courses.size() == 0)
					footer.setVisibility(View.GONE);
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
				phoneStudyAdapter = new PhoneStudyAdapter(FreeActivity.this,
						R.layout.phone_study_listview_nocontroitem,
						coursewares, lv,false);
				lv.setAdapter(phoneStudyAdapter);
				break;
			case NO_NET:
				MyTools.exitShow(FreeActivity.this, ((Activity)FreeActivity.this).getWindow().getDecorView(), "网络有问题！");
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
			MyTools.exitShow(FreeActivity.this, ((Activity)FreeActivity.this).getWindow().getDecorView(), "sd卡不存在！");
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 获取数据
				if (((CeiApplication) getApplication()).isNet()) {
					String result = Service.queryPhoneFunctionTree(functionId,"kj", "androidpad");
					if (XmlUtil.parseReturnCode(result).equals("")) {
						XmlUtil.parseCoursewares(result, courses);
						Message message = dataHandler.obtainMessage();
						for (int i = 0; i < courses.size(); i++) {
							Courseware courseware = courses.get(i);
							courseware.setParentId(functionId);
							courses.get(i).setFree(true);
							((CeiApplication) (FreeActivity.this
									.getApplication())).dataHelper
									.saveCourseware(courseware);
						}
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
					courses = ((CeiApplication) (FreeActivity.this
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
		findViewById(R.id.phone_study_search_btn).setOnClickListener(this);
		findViewById(R.id.phone_study_refresh).setOnClickListener(this);
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
		case R.id.phone_study_nominate:
			intent = new Intent(this, NominateActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_free:
			/*
			 * intent = new Intent(this,FreeActivity.class);
			 * startActivity(intent);
			 */
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
				MyTools.exitShow(FreeActivity.this, ((Activity)FreeActivity.this).getWindow().getDecorView(), "请登陆后查看！");
			break;
		case R.id.phone_study_study:
			intent = new Intent(this, PlayRecordCourseActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				MyTools.exitShow(FreeActivity.this, ((Activity)FreeActivity.this).getWindow().getDecorView(), "请登陆后查看！");
			break;
		case R.id.phone_study_say:
			intent = new Intent(this, SayGroupListActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				MyTools.exitShow(FreeActivity.this, ((Activity)FreeActivity.this).getWindow().getDecorView(), "请登陆后查看！");
			break;
		case R.id.phone_study_refresh:
			intent = new Intent(this, FreeActivity.class);
			startActivity(intent);
			break;
		}
	}
}
