package com.hyrt.cei.ui.information;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.CommonPopAdapter;
import com.hyrt.cei.adapter.InformationAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.information.adapter.GridViewAdapter;
import com.hyrt.cei.ui.information.view.GGridView;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.webservice.service.Service;

public class Information extends Activity implements OnClickListener,
		OnItemClickListener {
	private String loginName;
	public static List<View> groupViews = new ArrayList<View>();
	private String htmlHade = MyTools.newsHtml;
	private ColumnEntry columnEntry;
	private List<ColumnEntry> columnEntries = new ArrayList<ColumnEntry>();
	private String adress;
	private Intent intent;
	private Boolean alreadBuy;
	private Boolean canRead;
	private List<funId> funIds;
	private RelativeLayout rl;
	private LinearLayout prolayout;
	private static final int LEFT_LV = 1;
	private static final int ALREADY_BUY = 2;
	private String shuaxinId, shuaxinFunctionId, shuaxinNum;
	// 刚进入页面默认的顶部菜单显示
	private String loadCurrentParentId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		prolayout = (LinearLayout) findViewById(R.id.econ_data_pro);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		alreadBuy = false;
		canRead = false;
		loadCurrentParentId = "";
		// 为控件增加事件
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		intent = getIntent();
		loadCurrentParentId = intent.getStringExtra("functionId");
		shuaxinFunctionId = intent.getStringExtra("functionId");
		shuaxinId = intent.getStringExtra("extra");
		shuaxinNum = intent.getStringExtra("topNum");
		refreshListData(columnEntry.getUserId(), ALREADY_BUY);
		columnEntries.addAll(columnEntry.getEntryChildsForParent(columnEntry
				.getColByName(InformationMainActivity.MODEL_NAME).getId()));

		registEvent();
	}

	private void registEvent() {
		findViewById(R.id.home).setOnClickListener(this);
		findViewById(R.id.zjzx_zixun).setOnClickListener(this);
		gGridView = (GGridView) findViewById(R.id.zjzx_info_gridview);
		leftLv = (ListView) findViewById(R.id.list);
		int num = Integer.parseInt(((Activity) this).getIntent()
				.getStringExtra("topNum"));
		if (num != 0) {
			ColumnEntry columnEntry = columnEntries.remove(num);
			columnEntries.add(0, columnEntry);
		}
		GridViewAdapter gridViewAdapter = new GridViewAdapter(this,
				columnEntries);
		gGridView.setAdapter(gridViewAdapter);
		if (loadCurrentParentId != "")
			for (int i = 0; i < columnEntries.size(); i++) {
				if (columnEntries.get(i).getId().equals(loadCurrentParentId)) {
					refreshListData(columnEntries.get(i).getId(), LEFT_LV);
				}
			}
		gGridView.setOnItemClickListener(this);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				TranslateAnimation ta = new TranslateAnimation(0, -1000, 0, 0);
				ta.setDuration(2000);
				ta.setFillAfter(true);
				ta.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						TranslateAnimation ta = new TranslateAnimation(-1000,
								0, 0, 0);
						ta.setDuration(2000);
						ta.setFillAfter(true);
						gGridView.setAnimation(ta);
						ta.start();
					}
				});
				gGridView.setAnimation(ta);
				ta.start();
			}
		}, 500);
		findViewById(R.id.zjzx_shoucang).setOnClickListener(this);
		findViewById(R.id.zjzx_sousuo).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zjzx_shoucang:
			if (!loginName.equals("")) {
				alertCommonListPop();
			} else {
				Toast.makeText(Information.this, "请登陆后查看！", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.home:
			Information.this.finish();
			break;
		case R.id.zjzx_sousuo:
			intent = new Intent(this, InfoSearchActivity.class);
			startActivity(intent);
			break;
		case R.id.zjzx_zixun:
			intent = new Intent(this, InformationMainActivity.class);
			startActivity(intent);
			break;
		case R.id.common_listpop_edit:
			prolayout.setVisibility(View.VISIBLE);
			for (int i = 0; i < infoNews.size(); i++) {
				infoNews.get(i).setIsCollect("0");
			}
			adapter.notifyDataSetChanged();
			prolayout.setVisibility(View.GONE);
			break;
		case R.id.common_listpop_clear:
			prolayout.setVisibility(View.VISIBLE);
			Information.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Service.clearCollect(((CeiApplication) Information.this
							.getApplication()).columnEntry.getUserId());
				}
			});
			infoNews.clear();
			adapter.notifyDataSetChanged();
			prolayout.setVisibility(View.GONE);
			break;
		}
	}

	// 横向的菜单
	private GGridView gGridView;
	// 当前查询的业务资讯id
	private String oldFunctionId = "";
	// 当前查询的业务资讯id
	private String currentFunctionId;

	// 收藏列表弹出框
	private PopupWindow popWin;
	// 收藏列表listview
	private ListView listView;
	// 弹出框信息列表
	private List<InfoNew> infoNews = new ArrayList<InfoNew>();
	// 收藏列表适配器
	private CommonPopAdapter adapter;

	private void alertCommonListPop() {
		prolayout.setVisibility(View.VISIBLE);
		View popView = this.getLayoutInflater().inflate(
				R.layout.common_list_pop, null);
		listView = (ListView) popView
				.findViewById(R.id.common_listpop_listview);
		listView.setOnItemClickListener(this);
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				Information.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						String result = Service
								.queryCollect(((CeiApplication) Information.this
										.getApplication()).columnEntry
										.getUserId());
						infoNews.clear();
						XmlUtil.getNewsList(result, infoNews);
						for (int i = 0; i < infoNews.size(); i++) {
							infoNews.get(i).setIsCollect("1");
						}
						dataHandler.sendMessage(dataHandler.obtainMessage());
					}

				});
			}
		});
		popView.findViewById(R.id.common_listpop_edit).setOnClickListener(this);
		popView.findViewById(R.id.common_listpop_clear)
				.setOnClickListener(this);
		popView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popWin.dismiss();
				return false;
			}
		});
		popWin = new PopupWindow(popView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(findViewById(R.id.full_view), Gravity.CENTER, 0,
				0);
		prolayout.setVisibility(View.GONE);
	}

	@Override
	public void onItemClick(AdapterView<?> groupView, View arg1, int position,
			long arg3) {
		switch (groupView.getId()) {
		case R.id.common_listpop_listview:
			InfoNew new2 = infoNews.get(position);
			String intentId = new2.getId();
			// 跳转到详细页
			intent = new Intent();
			intent.putExtra("extra", intentId);
			for (int i = 0; i < columnEntries.size(); i++) {
				if (columnEntries.get(i).getId().equals(new2.getFunctionId())) {
					intent.putExtra("topNum", i + "");
				}
			}
			intent.putExtra("functionId", new2.getFunctionId());
			intent.setClass(Information.this, Information.class);
			if (infoNews.get(position).getIsfree().endsWith("1")) {
				Information.this.finish();
				Information.this.startActivity(intent);
			} else {
				for (int i = 0; i < funIds.size(); i++) {
					if (funIds.get(i).getFunid()
							.endsWith(infoNews.get(position).getFunctionId())) {
						canRead = true;
						break;
					} else {
						canRead = false;
					}
				}
				if (canRead) {
					Information.this.finish();
					Information.this.startActivity(intent);
				} else {
					MyTools.exitShow(Information.this, getWindow()
							.getDecorView(), "未购买该栏目！");
					prolayout.setVisibility(View.GONE);
				}
			}
			break;
		case R.id.zjzx_info_gridview:
			shuaxinNum = position + "";
			shuaxinId = "";
			String functionId = columnEntries.get(position).getId();
			shuaxinFunctionId = columnEntries.get(position).getId();
			currentFunctionId = functionId;
			if (oldFunctionId.equals(currentFunctionId))
				return;
			prolayout.setVisibility(View.VISIBLE);
			oldFunctionId = currentFunctionId;
			if (funIds.size() != 0) {
				for (int i = 0; i < funIds.size(); i++) {
					if (funIds.get(i).getFunid().endsWith(oldFunctionId)) {
						alreadBuy = true;
						break;
					} else {
						alreadBuy = false;
					}
				}
			}
			for (int i = 0; i < groupView.getChildCount(); i++) {
				rl = (RelativeLayout) groupView.getChildAt(i);
				if (i == position)
					((ImageView) rl.getChildAt(0))
							.setImageResource(R.drawable.common_menubg_select);
				else
					((ImageView) rl.getChildAt(0)).setImageDrawable(null);
			}
			refreshListData(currentFunctionId, LEFT_LV);
			break;
		case R.id.list:
			prolayout.setVisibility(View.VISIBLE);
			for (View view : groupViews) {
				view.setBackgroundDrawable(null);
			}
			arg1.setBackgroundColor(Color.WHITE);
			groupViews.add(arg1);
			// if (alreadBuy) {
			if (((CeiApplication) getApplication()).isNet()) {
				InfoNew newnow = news.get(position);
				if (newnow.getIsfree().endsWith("1")) {
					((WebView) findViewById(R.id.inf_web)).loadUrl(htmlHade
							+ news.get(position).getId());
				} else {
					for (int i = 0; i < funIds.size(); i++) {
						if (funIds.get(i).getFunid()
								.endsWith(newnow.getFunctionId())) {
							canRead = true;
							break;
						} else {
							canRead = false;
						}
					}
					if (canRead) {
						((WebView) findViewById(R.id.inf_web)).loadUrl(htmlHade
								+ news.get(position).getId());
					} else {
						MyTools.exitShow(Information.this, getWindow()
								.getDecorView(), "未购买该栏目！");
					}
				}
			} else {
				((WebView) findViewById(R.id.inf_web)).loadDataWithBaseURL(
						"about:blank", "现在是离线状态，请链接网络后获取数据！", "text/html",
						"utf-8", null);
			}
			prolayout.setVisibility(View.GONE);
			break;
		}
	}

	// 资讯列表数据集合
	private List<InfoNew> news = new ArrayList<InfoNew>();
	// 视图通知器
	private Handler dataHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.arg1) {
			case LEFT_LV:
				if (!((CeiApplication) getApplication()).isNet()) {
					((WebView) findViewById(R.id.inf_web)).loadDataWithBaseURL(
							"about:blank", "现在是离线状态，请链接网络后获取数据！", "text/html",
							"utf-8", null);
					prolayout.setVisibility(View.GONE);
				} else {
					if (news.isEmpty() || news.size() == 0) {
						((WebView) findViewById(R.id.inf_web))
								.loadDataWithBaseURL("about:blank",
										"该栏目下没有资讯！", "text/html", "utf-8", null);
						prolayout.setVisibility(View.GONE);
					} else {
						InformationAdapter adapter = new InformationAdapter(
								Information.this, R.layout.info_left_list_item,
								news, currentFunctionId, shuaxinId == null
										|| shuaxinId.equals("")
										|| shuaxinId.equals("more") ? news.get(
										0).getId() : shuaxinId);
						leftLv.setAdapter(adapter);
						leftLv.setOnItemClickListener(Information.this);
						prolayout.setVisibility(View.GONE);
						if (intent.getStringExtra("extra").equals("more")) {
							adress = htmlHade + news.get(0).getId();
							shuaxinId = intent.getStringExtra("extra");
							((WebView) findViewById(R.id.inf_web))
									.loadUrl(adress);
						} else if (intent.getStringExtra("extra").equals("")) {
							adress = htmlHade + news.get(0).getId();
							shuaxinId = intent.getStringExtra("extra");
							((WebView) findViewById(R.id.inf_web))
									.loadUrl(adress);
						} else if (intent.getStringExtra("extra") == "") {
							adress = htmlHade + news.get(0).getId();
							shuaxinId = intent.getStringExtra("extra");
							((WebView) findViewById(R.id.inf_web))
									.loadUrl(adress);
						} else if (shuaxinId == "") {
							adress = htmlHade + news.get(0).getId();
							shuaxinId = intent.getStringExtra("extra");
							((WebView) findViewById(R.id.inf_web))
									.loadUrl(adress);
						} else {
							adress = htmlHade + intent.getStringExtra("extra");
							shuaxinId = intent.getStringExtra("extra");
							((WebView) findViewById(R.id.inf_web))
									.loadUrl(adress);
						}
					}
				}
				break;
			default:
				Information.this.adapter = new CommonPopAdapter(
						Information.this,
						R.layout.common_listpop_listview_item, infoNews);
				listView.setAdapter(Information.this.adapter);
				prolayout.setVisibility(View.GONE);
				break;
			}
		}
	};

	// 左面的列表
	private ListView leftLv;

	private void refreshListData(final String functionId, final int operationId) {
		news.clear();
		prolayout.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = "";
				switch (operationId) {
				case LEFT_LV:
					result = Service
							.queryNewsByFunctionId(functionId, "40",
									((CeiApplication) Information.this
											.getApplication()).columnEntry
											.getUserId());
					XmlUtil.getNewsList(result, news);
					Message message = dataHandler.obtainMessage();
					message.arg1 = LEFT_LV;
					dataHandler.sendMessage(message);
					break;
				case ALREADY_BUY:
					result = Service.queryBuyNews(functionId);
					try {
						funIds = XmlUtil.queryBuyNews(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}

			}
		}).start();
	}
}
