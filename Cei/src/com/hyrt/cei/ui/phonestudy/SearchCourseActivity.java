package com.hyrt.cei.ui.phonestudy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.PhoneStudySearchAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.information.InfoSearchActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SearchCourseActivity extends Activity {

	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private ListView lv;
	private List<Courseware> coursewares = new ArrayList<Courseware>();
	private EditText searchContent;
	// 当前页索引
	private int index = 0;
	// 该搜索下所有的课件列表
	private List<Courseware> courses = new ArrayList<Courseware>();
	// 上一次的查询内容
	private String oldSearchText = "";
	// 此次的查询内容
	private String currentSearchText;
	// 课件列表适配器
	private PhoneStudySearchAdapter phoneStudySearchAdapter;
	//更多按钮
	private LinearLayout footer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study_searchcourse);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		lv = (ListView) findViewById(R.id.phone_study_searchcourse_listview);
		footer = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.phone_study_listview_bottom, null);
		footer.findViewById(R.id.phone_study_morebtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						index++;
						for (int i = index * 20; i < (index + 1) * 20
								&& i < courses.size(); i++) {
							if(i == courses.size()-1)
								footer.setVisibility(View.GONE);
							coursewares.add(courses.get(i));
						}
						phoneStudySearchAdapter.notifyDataSetChanged();
					}
				});
		lv.addFooterView(footer);
		for (int i = 0; i < HomePageActivity.phoneStudyContainer.size(); i++) {
			try {
				SearchCourseActivity isSearchCourseActivity = (SearchCourseActivity) (HomePageActivity.phoneStudyContainer
						.get(i));
				isSearchCourseActivity.finish();
			} catch (Exception e) {
			}
		}
		HomePageActivity.phoneStudyContainer.add(this);
		findViewById(R.id.back_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchCourseActivity.this.finish();
			}
		});
		searchContent = (EditText) findViewById(R.id.phone_study_search_edit);
		final SharedPreferences settings = getSharedPreferences("search_result",Activity.MODE_PRIVATE);
		String historyStr = settings.getString("history", "");
		searchContent.setText(historyStr);
		findViewById(R.id.phone_study_search_ss).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!(searchContent.getText() != null && searchContent
								.getText().toString().trim().equals(""))) {
							InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
							 //得到InputMethodManager的实例 
							if (imm.isActive()) { 
							//如果开启 
							}
							imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
							currentSearchText = searchContent.getText().toString().trim();
							if (oldSearchText.equals(currentSearchText))
								return;
							oldSearchText = currentSearchText;
							Editor editor = settings.edit();
							editor.putString("history", currentSearchText);
							editor.commit();
							initLvData();
						}else{
							MyTools.exitShow(SearchCourseActivity.this, ((Activity)SearchCourseActivity.this).getWindow().getDecorView(), "搜索内容不能为空");
						}

					}
				});
		findViewById(R.id.phone_study_downmanager).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(SearchCourseActivity.this,
								PreloadActivity.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.phone_study_refresh).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						SearchCourseActivity.this.finish();
						Intent intent = new Intent(SearchCourseActivity.this,
								SearchCourseActivity.class);
						startActivity(intent);
					}
				});
		searchContent.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					if (!(searchContent.getText() != null && searchContent
							.getText().toString().trim().equals("")))
						initLvData();
					return true;
				}
				return false;

			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			((ProgressBar) findViewById(R.id.phone_study_searchcourse_progress)).setVisibility(View.GONE);
			if(courses.size() == 0){
				MyTools.exitShow(SearchCourseActivity.this, SearchCourseActivity.this.getWindow().getDecorView(), "没有查到您需要的信息!");
				footer.setVisibility(View.GONE);
			}
			for (int i = index * 20; i < (index + 1) * 20
					&& i < courses.size(); i++) {
				if(i == courses.size()-1){
					footer.setVisibility(View.GONE);
				}else{
					if(footer.getVisibility() == View.GONE)
						footer.setVisibility(View.VISIBLE);
				}
				coursewares.add(courses.get(i));
			}
			phoneStudySearchAdapter = new PhoneStudySearchAdapter(
					SearchCourseActivity.this,
					R.layout.phone_study_searchcourse_listview_item,
					coursewares, lv);
			lv.setAdapter(phoneStudySearchAdapter);
		}
	};

	private void initLvData() {
		((ProgressBar) findViewById(R.id.phone_study_searchcourse_progress)).setVisibility(View.VISIBLE);
		executorService.submit(new Runnable() {

			private StringBuilder functionIds;
			List<Courseware> selfselCoursewares;

			// 初始化请求数据
			private void initSendData() {
				ColumnEntry columnEntry = ((CeiApplication) (SearchCourseActivity.this
						.getApplication())).columnEntry;
				ColumnEntry phoneStudyCol = columnEntry
						.getColByName(HomePageActivity.MODEL_NAME);
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
				coursewares.clear();
				courses.clear();
			}

			public void run() {
				initSendData();
				if (((CeiApplication) getApplication()).isNet()) {
					String result = Service.queryClassName(searchContent.getText().toString().trim(),functionIds.toString());
					XmlUtil.parseCoursewares(result, courses);
					result = Service.queryCourse(((CeiApplication) (getApplication())).columnEntry.getUserId());
					XmlUtil.parseCoursewares(result, selfselCoursewares);
					for (int i = 0; i < courses.size(); i++) {
						for (int j = 0; j < selfselCoursewares.size(); j++) {
							if (courses.get(i).getClassId().equals(selfselCoursewares.get(j).getClassId())) {
								courses.get(i).setSelfCourse(true);
							}
						}
						((CeiApplication) getApplication()).dataHelper.saveCourseware(courses.get(i));
					}
				} else {
					Courseware courseware = new Courseware();
					courseware.setName(searchContent.getText().toString()
							.trim());
					courses = ((CeiApplication) getApplication()).dataHelper
							.getCoursewares(courseware);
				}
				handler.sendMessage(handler.obtainMessage());
			}
		});
	}

	@Override
	protected void onDestroy() {
		HomePageActivity.phoneStudyContainer.remove(this);
		super.onDestroy();
	}
}