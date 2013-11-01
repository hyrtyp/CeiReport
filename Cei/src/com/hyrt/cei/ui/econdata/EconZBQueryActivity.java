package com.hyrt.cei.ui.econdata;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.EconDataZBAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.ui.information.funId;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.New;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class EconZBQueryActivity extends Activity implements OnClickListener {
	public List<View> groupViews = new ArrayList<View>();
	private ColumnEntry columnEntry;
	private List<New> DataList;
	private ImageView zxImg, szImg, zjImg, fxImg, zbImg, sjImg, homeImg,
			backImg, shuax;
	private ListView expList;
	private WebView webView;
	CeiApplication application;
	private LinearLayout prolayout;
	boolean flage;
	private String nowID, numberId;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==400){
				webView.loadDataWithBaseURL("about:blank",
						"没有数据！", "text/html", "utf-8", null);
				prolayout.setVisibility(View.GONE);
				return;
			}
			if (nowID != null && !nowID.equals("")) {
				/*
				 * String
				 * htmlUrl=MyTools.newsHtml+getIntent().getStringExtra("id");
				 * 
				 * webView.loadUrl(htmlUrl);
				 */
				if (((CeiApplication) getApplication()).isNet()) {
					String htmlUrl = MyTools.newsHtml
							+ getIntent().getStringExtra("id");
					webView.loadUrl(htmlUrl);
					// 条目变色
				} else {
					webView.loadDataWithBaseURL("about:blank",
							"现在是离线状态，请链接网络后获取数据！", "text/html", "utf-8", null);
				}
			} else if (DataList.size() > 0) {
				/*
				 * String htmlUrl = MyTools.newsHtml + DataList.get(0).getId();
				 * webView.loadUrl(htmlUrl);
				 */
				if (((CeiApplication) getApplication()).isNet()) {
					String htmlUrl = MyTools.newsHtml + DataList.get(0).getId();
					webView.loadUrl(htmlUrl);
				} else {
					webView.loadDataWithBaseURL("about:blank",
							"现在是离线状态，请链接网络后获取数据！", "text/html", "utf-8", null);
				}
			} else {
				MyTools.exitShow(EconZBQueryActivity.this,
						findViewById(R.id.econ_data_zb_all), "没有数据！");
			}
			expList.setAdapter(new EconDataZBAdapter(EconZBQueryActivity.this,
					DataList, nowID));
			expList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String funid = columnEntry.getColByName("指标查询",columnEntry.getColByName(EconDataMain.MODEL_NAME).getId()).getId();
					if (isHasfunID(funid)
							|| DataList.get(arg2).getIsfree().equals("1")) {
						String htmlUrl = MyTools.newsHtml
								+ DataList.get(arg2).getId();
						if (((CeiApplication) getApplication()).isNet()) {
							webView.loadUrl(htmlUrl);
						} else {
							webView.loadDataWithBaseURL("about:blank",
									"现在是离线状态，请链接网络后获取数据！", "text/html",
									"utf-8", null);
						}

						for (View view : groupViews) {
							view.findViewById(R.id.econ_data_ea_item_group_tv)
									.setBackgroundResource(
											R.drawable.econ_data_ea_item_group);
						}
						arg1.findViewById(R.id.econ_data_ea_item_group_tv)
								.setBackgroundResource(
										R.drawable.econ_data_ea_item_group1);
						groupViews.add(arg1);
					} else {
						MyTools.exitShow(EconZBQueryActivity.this,
								findViewById(R.id.econ_data_zb_all), "没有数据！");
					}
				}
			});
			flage = true;
			prolayout.setVisibility(View.GONE);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_data_zhib);
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		application = (CeiApplication) getApplication();
		nowID = getIntent().getStringExtra("id");
		initView();
		prolayout.setVisibility(View.VISIBLE);
		initData();
	}

	private void initData() {
		if (EconDataMain.allColBg != null
				&& EconDataMain.allColBg.getId() != null
				&& !EconDataMain.allColBg.getId().equals("")) {
			String allBgId = EconDataMain.allColBg.getId();
			List<ColumnEntry> allCol = columnEntry
					.getEntryChildsForParent(allBgId);
			for (ColumnEntry columnEntry : allCol) {
				if (columnEntry.getName().equals("指标查询")) {
					numberId = columnEntry.getId();
				}
			}
		}
		// 获取数据
		DataList = new ArrayList<New>();
		new Thread() {

			@Override
			public void run() {
				if (numberId != null && !numberId.equals("")) {
					try {
						List<New> dataList = null;
						if (((CeiApplication) getApplication()).isNet()) {
							String zhib = Service.querydbsByFunctionId(
									numberId, "40");
							dataList = XmlUtil.getNews(zhib);
							WriteOrRead.write(zhib, MyTools.nativeData,
									"zhibiao.xml");

						} else {
							String zhib = WriteOrRead.read(MyTools.nativeData,
									"goodecon.xml");
							dataList = XmlUtil.getNews(zhib);
						}
						DataList.addAll(dataList);
						if (handler != null) {
							handler.sendEmptyMessage(1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					if (handler != null) {
						handler.sendEmptyMessage(400);
					}
				}

			}

		}.start();
	}

	private void initView() {
		prolayout = (LinearLayout) findViewById(R.id.econ_data_zb_pro);
		expList = (ListView) findViewById(R.id.econ_data_number_list);
		webView = (WebView) findViewById(R.id.econ_data_number_web);
		// 低栏图标
		shuax = (ImageView) findViewById(R.id.econ_data_zb_shuax);
		shuax.setOnClickListener(this);
		zxImg = (ImageView) findViewById(R.id.econ_data_zb_zx);
		zxImg.setOnClickListener(this);
		szImg = (ImageView) findViewById(R.id.econ_data_zb_sz);
		szImg.setOnClickListener(this);
		zjImg = (ImageView) findViewById(R.id.econ_data_zb_zz);
		zjImg.setOnClickListener(this);
		fxImg = (ImageView) findViewById(R.id.econ_data_zb_fx);
		fxImg.setOnClickListener(this);
		zbImg = (ImageView) findViewById(R.id.econ_data_zb_zb);
		zbImg.setOnClickListener(this);
		sjImg = (ImageView) findViewById(R.id.econ_data_zb_sj);
		sjImg.setOnClickListener(this);
		homeImg = (ImageView) findViewById(R.id.econ_data_zb_home);
		homeImg.setOnClickListener(this);
		backImg = (ImageView) findViewById(R.id.econ_data_zb_back);
		backImg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == zxImg) {
			// 最新页面
			Intent intent = new Intent(this, EconDataMain.class);
			startActivity(intent);
		} else if (v == szImg) {
			Intent intent = new Intent(this, EconDateNumberActivity.class);
			startActivity(intent);
			// 数字快讯
		} else if (v == zjImg) {
			// 中经指数
			Intent intent = new Intent(this, EconZZDataActivity.class);
			startActivity(intent);
		} else if (v == fxImg) {
			// 分析预测
			Intent intent = new Intent(this, EconFXDataActivity.class);
			startActivity(intent);
		} else if (v == zbImg) {
			// 指标查询
			return;
		} else if (v == sjImg) {
			// 数据查询
			Intent intent = new Intent(this, EconDataQueryActivity.class);
			startActivity(intent);
		} else if (v == homeImg) {
			// 首页
			Intent intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
		} else if (v == backImg) {
			// 返回键
			this.finish();
		} else if (v == shuax) {
			if (flage) {
				flage=false;
				Intent intent = new Intent(this, EconZBQueryActivity.class);
				startActivity(intent);
				this.finish();
			} else {
				return;
			}
		}

	}

	private boolean isHasfunID(String funid) {
		CeiApplication application = (CeiApplication) getApplication();
		for (funId id : application.buyEconData) {
			if (funid.equals(id.getFunid())) {
				return true;
			}
		}
		return false;
	}

	public void saveActivity(Class<?> clas) {
		if (application.activitys.contains(clas)) {
			application.activitys.remove(clas);
			application.activitys.add(clas);
		} else {
			application.activitys.add(clas);
		}
	}
}
