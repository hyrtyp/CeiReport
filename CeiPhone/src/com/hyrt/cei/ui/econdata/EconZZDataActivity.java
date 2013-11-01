package com.hyrt.cei.ui.econdata;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.adapter.EconAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.information.adapter.GridViewAdapter;
import com.hyrt.cei.ui.information.view.GGridView;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.New;
import com.hyrt.cei.vo.funId;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 中经指数
 * 
 * @author tmy
 * 
 */

public class EconZZDataActivity extends ContainerActivity implements
		OnClickListener, OnItemClickListener {
	private ImageView img_econ_main, img_szks, img_zjzs, img_fxyc, img_zbsc,
			img_data_search;
	private TextView tv_left, tv_center;
	Context context = EconZZDataActivity.this;
	private Application application;
	private ColumnEntry columnEntry;
	private String currentFunId;
	private String firstfunid;
	public static String MODEL_NAMES;
	private List<New> groupNewsList = new ArrayList<New>();
	private List<New> newsList = new ArrayList<New>();
	List<ColumnEntry> groupDataList = new ArrayList<ColumnEntry>();
	private ListView lv;
	private GGridView gGridView;
	private GridViewAdapter gAdapter;
	private RelativeLayout rl, r2;
	private List<ColumnEntry> columnEntrieszjzs;
	public static final String MODEL_NAME = "中经指数";
	private EconAdapter adapter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (newsList == null) {
				return;
			}
			adapter = new EconAdapter(context, newsList);
			lv.setAdapter(adapter);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_data_zj_number);
		MODEL_NAMES = ((CeiApplication) getApplication()).nowStart;// 获取当前业务名称。
		application = (CeiApplication) getApplication();
		// ColumnEntry columnEntry = new ColumnEntry();
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		initView();
		String model = "中经指数";
		ColumnEntry col = columnEntry.getColByName(EconDataMain.MODEL_NAME);
		if (columnEntry.getColByName("中经指数", col.getId()) == null)
			return;
		columnEntrieszjzs = (List<ColumnEntry>) columnEntry
				.getEntryChildsForParent(columnEntry.getColByName(
						EconZZDataActivity.MODEL_NAME, col.getId()).getId());
		gAdapter = new GridViewAdapter(context, columnEntrieszjzs);
		gGridView.setAdapter(gAdapter);
		currentFunId = columnEntrieszjzs.get(0).getId();
		refreshListData(currentFunId);

	}

	/*
	 * private void initData(final String model, final String newId) { new
	 * Thread() { public void run() { if ("中经指数".equals(model)) { ColumnEntry
	 * colBg = columnEntry.getColByName("中经指数"); if (colBg != null) { String
	 * numberId = colBg.getId(); if (numberId != null && !numberId.equals("")) {
	 * groupDataList = columnEntry .getEntryChildsForParent(numberId); for
	 * (ColumnEntry columnEntry : groupDataList) { try { if (((CeiApplication)
	 * getApplication()) .isNet()) { String zhib = Service
	 * .querydbsByFunctionId( columnEntry.getId(), "40"); groupNewsList =
	 * XmlUtil.getNews(zhib); for (New news : groupNewsList) {
	 * newsList.add(news); } WriteOrRead.write(zhib, MyTools.nativeData,
	 * columnEntry.getId()); } else { String zhib = WriteOrRead.read(
	 * MyTools.nativeData, columnEntry.getId()); groupNewsList =
	 * XmlUtil.getNews(zhib); for (New news : groupNewsList) {
	 * newsList.add(news); }
	 * 
	 * } System.out.println("中经指数news" + newsList.size()); } catch (Exception e)
	 * { e.printStackTrace(); } } if (handler != null) {
	 * handler.sendEmptyMessage(1); } } } } else { System.out.println("子栏目");
	 * try { if (((CeiApplication) getApplication()).isNet()) { String zhib =
	 * Service.querydbsByFunctionId(newId, "40"); newsList =
	 * XmlUtil.getNews(zhib); WriteOrRead.write(zhib, MyTools.nativeData,
	 * newId); } else { String zhib = WriteOrRead.read(MyTools.nativeData,
	 * newId); newsList = XmlUtil.getNews(zhib); } System.out.println("子栏目news"
	 * + newsList.size()); } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * } if (handler != null) { handler.sendEmptyMessage(2); }
	 * 
	 * }; }.start();
	 * 
	 * }
	 */

	private void refreshListData(final String funtionId) {
		new Thread() {

			@Override
			public void run() {

				try {
					System.out.println("funcId" + funtionId);
					if (((CeiApplication) getApplication()).isNet()) {

						String zhib = Service.querydbsByFunctionId(funtionId,
								"40");
						newsList = XmlUtil.getNews(zhib);
						System.out.println("保险业" + newsList.size());
						WriteOrRead.write(zhib, MyTools.nativeData, funtionId);
						/*
						 * // 购买的新闻 String buyEcon = Service
						 * .queryBuyDbNews(columnEntry .getUserId());
						 * WriteOrRead.write(buyEcon, MyTools.nativeData,
						 * "buyEcon.xml"); // 清空集合中的数据
						 * application.buyEconData.clear(); // 把购买的数据加入集合中
						 * mapplication.buyEconData.addAll(XmlUtil
						 * .queryBuyNews(buyEcon));
						 */
					} else {
						newsList.clear();
						String zhib = WriteOrRead.read(MyTools.nativeData,
								funtionId);
						newsList = XmlUtil.getNews(zhib);
						/*
						 * // 购买的新闻 String buyEcon = Service
						 * .queryBuyDbNews(columnEntry .getUserId());
						 * WriteOrRead.write(buyEcon, MyTools.nativeData,
						 * "buyEcon.xml"); // 清空集合中的数据
						 * mapplication.buyEconData.clear(); // 把购买的数据加入集合中
						 * mapplication.buyEconData.addAll(XmlUtil
						 * .queryBuyNews(buyEcon));
						 */
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (handler != null) {
					handler.sendEmptyMessage(3);
				}
			}
		}.start();

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

	private void initView() {
		lv = (ListView) findViewById(R.id.econ_zjzs_list_content);
		lv.setOnItemClickListener(this);
		tv_left = (TextView) findViewById(R.id.econ_zj_nums_top_text1);
		tv_left.setText(MODEL_NAMES);
		tv_center = (TextView) findViewById(R.id.econ_main_top_img);
		tv_center.setText("中经指数");
		img_szks = (ImageView) findViewById(R.id.econ_zjzs_botton_2);
		img_zjzs = (ImageView) findViewById(R.id.econ_zjzs_botton_3);
		img_fxyc = (ImageView) findViewById(R.id.econ_zjzs_botton_4);
		img_zbsc = (ImageView) findViewById(R.id.econ_zjzs_botton_5);
		// img_data_search = (ImageView) findViewById(R.id.econ_main_top_tv2);
		tv_left.setOnClickListener(this);
		img_szks.setOnClickListener(this);
		gGridView = (GGridView) findViewById(R.id.zjzx_info_gridview);
		gGridView.setOnItemClickListener(this);
		img_zjzs.setOnClickListener(this);
		img_fxyc.setOnClickListener(this);
		img_zbsc.setOnClickListener(this);
		// img_data_search.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == tv_left) {
			Intent intent = new Intent(context, EconDataMain.class);
			startActivity(intent);

		} else if (v == img_szks) {
			Intent intent = new Intent(context, EconDateNumberActivity.class);
			startActivity(intent);

		} else if (v == img_zjzs) {
			return;
		} else if (v == img_fxyc) {
			Intent intent = new Intent(context, EconFXDataActivity.class);
			startActivity(intent);

		} else if (v == img_zbsc) {
			Intent intent = new Intent(context, EconZBQueryActivity.class);
			startActivity(intent);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.zjzx_info_gridview:
			currentFunId = columnEntrieszjzs.get(position).getId();
			refreshListData(currentFunId);
			for (int i = 0; i < parent.getChildCount(); i++) {
				r2 = (RelativeLayout) parent.getChildAt(i);
				if (i == position) {
					((ImageView) r2.getChildAt(0))
							.setImageResource(R.drawable.phone_study_menu_select);
					((TextView) r2.getChildAt(1)).setTextColor(Color.WHITE);
				} else {
					((ImageView) r2.getChildAt(0)).setImageDrawable(null);
					((TextView) r2.getChildAt(1)).setTextColor(Color.BLUE);
				}

			}

			break;

		case R.id.econ_zjzs_list_content:
			ColumnEntry col = columnEntry.getColByName(EconDataMain.MODEL_NAME);
			if (columnEntry.getColByName("中经指数", col.getId()) == null)
				return;
			firstfunid = columnEntry.getColByName("中经指数", col.getId()).getId();
			New oneNew = newsList.get(position);
			System.out.println("title" + oneNew.getTitle());
			//判断父类栏目是否购买，当前栏目是否购买，
			if (isHasfunID(firstfunid)||isHasfunID(currentFunId)
					|| oneNew.getIsfree().equals("1")) {
				Intent intent = new Intent(EconZZDataActivity.this,
						EconDataContent.class);
				intent.putExtra("id", oneNew.getId());
				startActivity(intent);
			} else {
				// 栏目没有购买
				MyTools.exitShow(EconZZDataActivity.this, getWindow()
						.getDecorView(), "未购买该栏目！");

			}

			break;
		}

	}

}
