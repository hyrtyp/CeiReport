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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

public class EconGoodDataActivity extends Activity implements OnClickListener {
	public List<View> groupViews = new ArrayList<View>();
	private ColumnEntry columnEntry;
	private List<New> DataList;
	private ImageView backImg, shuax;
	private ListView expList;
	private WebView webView;
	CeiApplication application;
	boolean flage;
	private String nowID,numberId;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (nowID != null && !nowID.equals("")) {
				String htmlUrl = MyTools.newsHtml
						+ getIntent().getStringExtra("id");
				if (((CeiApplication) getApplication()).isNet()) {
					webView.loadUrl(htmlUrl);
				} else {
					webView.loadDataWithBaseURL("about:blank",
							"现在是离线状态，请链接网络后获取数据！", "text/html", "utf-8", null);
				}
			} else if (DataList.size() > 0) {
				if (((CeiApplication) getApplication()).isNet()) {
					String htmlUrl = MyTools.newsHtml + DataList.get(0).getId();
					webView.loadUrl(htmlUrl);
				} else {
					webView.loadDataWithBaseURL("about:blank",
							"现在是离线状态，请链接网络后获取数据！", "text/html", "utf-8", null);
				}
			} else {
				MyTools.exitShow(EconGoodDataActivity.this,
						findViewById(R.id.econ_data_good_all), "没有数据！");
			}
			expList.setAdapter(new EconDataZBAdapter(EconGoodDataActivity.this,
					DataList, nowID));
			expList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String funid = columnEntry.getColByName("精彩数据",columnEntry.getColByName(EconDataMain.MODEL_NAME).getId()).getId();
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
						MyTools.exitShow(EconGoodDataActivity.this,
								findViewById(R.id.econ_data_good_all),
								"未购买该栏目！");
					}

				}
			});
			flage = true;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_data_gooddata);
		application = (CeiApplication) getApplication();
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		nowID = getIntent().getStringExtra("id");
		initView();
		initData();
		/*
		 * if (getIntent().getStringExtra("id") != null &&
		 * !getIntent().getStringExtra("id").equals("")) { String htmlUrl =
		 * MyTools.newsHtml + getIntent().getStringExtra("id");
		 * webView.loadUrl(htmlUrl); }
		 */
	}

	private void initData() {
		if (EconDataMain.allColBg != null && EconDataMain.allColBg.getId() != null
				&& !EconDataMain.allColBg.getId().equals("")) {
			String allBgId = EconDataMain.allColBg.getId();
			List<ColumnEntry> allCol = columnEntry
					.getEntryChildsForParent(allBgId);
			for (ColumnEntry columnEntry : allCol) {
				if (columnEntry.getName().equals("精彩数据")) {
					numberId = columnEntry.getId();
				}
			}
		}
		// 获取数据
		DataList = new ArrayList<New>();
		new Thread() {
			@Override
			public void run() {
				ColumnEntry colBg = columnEntry.getColByName("精彩数据");
				if (colBg != null) {
					if (numberId != null && !numberId.equals("")) {
						try {
							List<New> dataList = null;
							if (((CeiApplication) getApplication()).isNet()) {
								String zhib = Service.querydbsByFunctionId(
										numberId, "");
								dataList = XmlUtil.getNews(zhib);
								WriteOrRead.write(zhib, MyTools.nativeData,
										"goodecon.xml");
							} else {
								String zhib = WriteOrRead.read(
										MyTools.nativeData, "goodecon.xml");
								dataList = XmlUtil.getNews(zhib);
							}
							DataList.addAll(dataList);
							if (handler != null) {
								handler.sendEmptyMessage(1);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

		}.start();
	}

	private void initView() {
		expList = (ListView) findViewById(R.id.econ_data_number_list);
		webView = (WebView) findViewById(R.id.econ_data_number_web);
		// 低栏图标
		shuax = (ImageView) findViewById(R.id.econ_data_gooddata_shuax);
		shuax.setOnClickListener(this);
		backImg = (ImageView) findViewById(R.id.econ_data_gooddata_back);
		backImg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v == backImg) {
			this.finish();
			return;
		} else {
			if (flage) {
				flage=false;
				Intent intent = new Intent(this, EconGoodDataActivity.class);
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
}
