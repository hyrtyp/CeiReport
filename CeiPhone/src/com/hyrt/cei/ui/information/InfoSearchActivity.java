package com.hyrt.cei.ui.information;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrt.cei.adapter.InfAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.vo.funId;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;

public class InfoSearchActivity extends ContainerActivity implements
		OnClickListener {
	private ListView list;
	private List<InfoNew> infoNews = new ArrayList<InfoNew>();
	private EditText searchEdittext;
	//public static final String MODEL_NAME = "政经资讯";
	private String strId;
	private Intent intent;
	public static  String MODEL_NAME;
	private Boolean canRead;
	private List<funId> funIds;
	private String loginName;

	// 当前页索引
	private int index = 0;
	// 该搜索下所有的课件列表
	private List<InfoNew> allInfoNews = new ArrayList<InfoNew>();
	// 上一次的查询内容
	private String oldSearchText = "";
	// 此次的查询内容
	private String currentSearchText;
	// 更多按钮
	private LinearLayout footer;
	// 适配器
	private InfAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_search);
		MODEL_NAME=((CeiApplication)getApplication()).nowStart;//获取当前业务名称。
		((TextView)findViewById(R.id.back)).setText(MODEL_NAME);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		init();
	}

	public void init() {
		canRead = false;
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.info_search_button).setOnClickListener(this);
		searchEdittext = (EditText) findViewById(R.id.info_search_edittext);
		SharedPreferences settings = getSharedPreferences("search_result",
				Activity.MODE_PRIVATE);
		String historyStr = settings.getString("history_information", "");
		loginName = settings.getString("LOGINNAME", "");
		searchEdittext.setText(historyStr);
		list = (ListView) findViewById(R.id.info_search_list);
		findViewById(R.id.zjzx_botton_1).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_2).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_3).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_4).setOnClickListener(this);
		footer = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.phone_study_listview_bottom, null);
		footer.findViewById(R.id.phone_study_morebtn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						index++;
						for (int i = index * 20; i < (index + 1) * 20
								&& i < allInfoNews.size(); i++) {
							if (i == allInfoNews.size() - 1) {
								footer.setVisibility(View.GONE);
							} else {
								if (footer.getVisibility() == View.GONE)
									footer.setVisibility(View.VISIBLE);
							}
							infoNews.add(allInfoNews.get(i));
						}
						adapter.notifyDataSetChanged();
					}
				});
		list.addFooterView(footer);
	}

	public void refreshListData() {
		final ColumnEntry columnEntry = ((CeiApplication) getApplication()).columnEntry;
		List<ColumnEntry> list = columnEntry
				.getEntryChildsForParent(columnEntry.getColByName(InformationActivity.MODEL_NAME)
						.getId());
		strId = "";
		for (int i = 0; i < list.size(); i++) {
			if (i + 1 == list.size()) {
				strId += list.get(i).getId();
			} else {
				strId += list.get(i).getId() + ",";
			}
		}
		final String srarchStr = searchEdittext.getText().toString();
		currentSearchText = srarchStr;
		if (currentSearchText.equals(oldSearchText))
			return;
		oldSearchText = currentSearchText;
		this.list.setVisibility(View.GONE);
		index = 0;
		new Thread((new Runnable() {
			@Override
			public void run() {
				SharedPreferences settings = getSharedPreferences(
						"search_result", Activity.MODE_PRIVATE);

				Editor editor = settings.edit();
				editor.putString("history_information", srarchStr);
				editor.commit();
				String result = Service.queryBuyNews(columnEntry.getUserId());
				String rs = Service.queryNewsByName(strId, srarchStr);
				try {
					infoNews.clear();
					allInfoNews.clear();
					XmlUtil.getNewsList(rs, allInfoNews);
					funIds = XmlUtil.queryBuyNews(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = newsHandler.obtainMessage();
				newsHandler.sendMessage(msg);
			}
		})).start();
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			list.setVisibility(View.VISIBLE);
			if (allInfoNews.size() == 0) {
				MyTools.exitShow(InfoSearchActivity.this,
						InfoSearchActivity.this.getWindow().getDecorView(),
						"没有查到您需要的信息!");
				footer.setVisibility(View.GONE);
				if (adapter != null)
					adapter.notifyDataSetChanged();
			} else {
				if (adapter == null) {
					for (int i = index * 20; i < (index + 1) * 20
							&& i < allInfoNews.size(); i++) {
						if (i == allInfoNews.size() - 1) {
							footer.setVisibility(View.GONE);
						} else {
							if (footer.getVisibility() == View.GONE)
								footer.setVisibility(View.VISIBLE);
						}
						infoNews.add(allInfoNews.get(i));
					}
				 adapter = new InfAdapter(InfoSearchActivity.this,
						R.layout.inf_list_item, infoNews);
				list.setAdapter(adapter);
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						InfoNew new2 = infoNews.get(arg2);
						String intentId = new2.getId();
						intent = new Intent();
						intent.putExtra("extra", intentId);
						intent.putExtra("functionId", new2.getFunctionId());
						intent.setClass(InfoSearchActivity.this,
								InformationReadActivity.class);
						if (new2.getIsfree().endsWith("1")) {
							InfoSearchActivity.this.startActivity(intent);
						} else {
							for (int i = 0; i < funIds.size(); i++) {
								if (funIds.get(i).getFunid()
										.endsWith(new2.getFunctionId())) {
									canRead = true;
									break;
								} else {
									canRead = false;
								}
							}
							if (canRead) {
								InfoSearchActivity.this.startActivity(intent);
							} else {
								MyTools.exitShow(InfoSearchActivity.this,
										getWindow().getDecorView(), "未购买该栏目！");
							}
						}
					}
				});
				}else{
					for (int i = index * 20; i < (index + 1) * 20
							&& i < allInfoNews.size(); i++) {
						if (i == allInfoNews.size() - 1) {
							footer.setVisibility(View.GONE);
						} else {
							if (footer.getVisibility() == View.GONE)
								footer.setVisibility(View.VISIBLE);
						}
						infoNews.add(allInfoNews.get(i));
					}
					adapter.notifyDataSetChanged();
				}
			}

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zjzx_botton_1:
			intent = new Intent(InfoSearchActivity.this, InformationOne.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_2:
			intent = new Intent(InfoSearchActivity.this, InformationTwo.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_3:
			intent = new Intent(InfoSearchActivity.this, InformationThree.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_4:
			if (!loginName.equals("")) {
				intent = new Intent(InfoSearchActivity.this,
						InformationCollect.class);
				startActivity(intent);
			} else {
				Toast.makeText(this, "请登录后查看！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.back:
			InfoSearchActivity.this.finish();
			intent = new Intent(InfoSearchActivity.this,
					InformationActivity.class);
			startActivity(intent);
			break;
		case R.id.info_search_button:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			// 得到InputMethodManager的实例
			if (imm.isActive()) {
				// 如果开启
				imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
			if (searchEdittext.getText().toString().trim().equals("")) {
				Toast.makeText(InfoSearchActivity.this, "搜索内容不能为空",
						Toast.LENGTH_LONG).show();
			} else {
				refreshListData();
			}
			break;
		}
	}
}
