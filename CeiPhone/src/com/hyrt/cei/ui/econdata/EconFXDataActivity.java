package com.hyrt.cei.ui.econdata;

import java.util.ArrayList;
import java.util.List;

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

/**
 * 分析预测
 * 
 * @author tmy
 * 
 */

public class EconFXDataActivity extends ContainerActivity implements
		OnClickListener, OnItemClickListener {
	private ListView lv;
	private ImageView img_econ_main, img_szks, img_zjzs, img_fxyc, img_zbsc,
			img_data_search;
	private TextView tv_left, tv_center;
	public static String MODEL_NAMES;
	Context context = EconFXDataActivity.this;
	private Application application;
	private String currentFunId;
	private String firstfunid;
	private ColumnEntry columnEntry;
	private List<New> groupNewsList = new ArrayList<New>();
	private List<New> newsList = new ArrayList<New>();;
	List<ColumnEntry> groupDataList = new ArrayList<ColumnEntry>();
	private List<ColumnEntry> columnEntriesfxd;
	private GridViewAdapter gAdapter;
	private CeiApplication mapplication;
	private GGridView gGridView;
	private EconAdapter adapter;
	public static final String MODEL_NAME = "分析预测";
	private RelativeLayout rl, r2;
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
		setContentView(R.layout.econ_data_fxyc);
		MODEL_NAMES = ((CeiApplication) getApplication()).nowStart;// 获取当前业务名称。
		application = (CeiApplication) getApplication();
		mapplication = (CeiApplication) getApplication();
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		String model = "分析预测";
		initView();
		ColumnEntry col = columnEntry.getColByName(EconDataMain.MODEL_NAME);
		if (columnEntry
				.getColByName(EconFXDataActivity.MODEL_NAME, col.getId()) == null)
			return;
		//根据父类ID获取子类实体集合
		columnEntriesfxd = (List<ColumnEntry>) columnEntry
				.getEntryChildsForParent(columnEntry.getColByName(
						EconFXDataActivity.MODEL_NAME, col.getId()).getId());
		gAdapter = new GridViewAdapter(context, columnEntriesfxd);
		gGridView.setAdapter(gAdapter);
		currentFunId = columnEntriesfxd.get(0).getId();
		refreshListData(currentFunId);

	}

	private void initView() {
		lv = (ListView) findViewById(R.id.econ_fxyc_list_content);
		lv.setOnItemClickListener(this);
		tv_left = (TextView) findViewById(R.id.econ_fxyc_top_text1);
		tv_left.setText(MODEL_NAMES);
		tv_center = (TextView) findViewById(R.id.econ_main_top_img);
		tv_center.setText("分析预测");
		img_szks = (ImageView) findViewById(R.id.econ_fxyc_botton_1);
		img_zjzs = (ImageView) findViewById(R.id.econ_fxyc_botton_2);
		img_fxyc = (ImageView) findViewById(R.id.econ_fxyc_botton_3);
		img_zbsc = (ImageView) findViewById(R.id.econ_fxyc_botton_4);
		// img_data_search = (ImageView) findViewById(R.id.econ_main_top_tv2);
		//
		gGridView = (GGridView) findViewById(R.id.zjzx_info_gridview);
		gGridView.setOnItemClickListener(this);
		tv_left.setOnClickListener(this);
		img_fxyc.setOnClickListener(this);
		img_szks.setOnClickListener(this);
		img_zjzs.setOnClickListener(this);
		img_zbsc.setOnClickListener(this);
		// img_data_search.setOnClickListener(this);

	}

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
						// 购买的新闻
						String buyEcon = Service.queryBuyDbNews(columnEntry
								.getUserId());
						WriteOrRead.write(buyEcon, MyTools.nativeData,
								"buyEcon.xml");
						// 清空集合中的数据
						mapplication.buyEconData.clear();
						// 把购买的数据加入集合中
						mapplication.buyEconData.addAll(XmlUtil
								.queryBuyNews(buyEcon));
					} else {
						newsList.clear();
						String zhib = WriteOrRead.read(MyTools.nativeData,
								funtionId);
						newsList = XmlUtil.getNews(zhib);
						String buyEcon = WriteOrRead.read(MyTools.nativeData,
								"buyEcon.xml");
						if (buyEcon != null && !buyEcon.equals("")) {
							mapplication.buyEconData.clear();
							mapplication.buyEconData.addAll(XmlUtil
									.queryBuyNews(buyEcon));
						}
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
		mapplication = (CeiApplication) getApplication();
		for (funId id : mapplication.buyEconData) {
			if (funid.equals(id.getFunid())) {
				return true;
			}
		}

		return false;
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
			Intent intent = new Intent(context, EconZZDataActivity.class);
			startActivity(intent);

		} else if (v == img_fxyc) {
			return;

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
			currentFunId = columnEntriesfxd.get(position).getId();
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

		case R.id.econ_fxyc_list_content:
			firstfunid = columnEntry.getColByName("分析预测").getId();
			New oneNew = newsList.get(position);
			String nowID="";
			//nowID=columnEntry.getColByName(
					//oneNew.getFunname()).getId();
			// 1免费0付费
			System.out.println("title" + oneNew.getTitle());
			if (isHasfunID(firstfunid)||isHasfunID(currentFunId)
					|| oneNew.getIsfree().equals("1")) {
				Intent intent = new Intent(EconFXDataActivity.this,
						EconDataContent.class);
				intent.putExtra("id", oneNew.getId());
				startActivity(intent);
			} else {
				// 栏目没有购买
				MyTools.exitShow(EconFXDataActivity.this, getWindow()
						.getDecorView(), "未购买该栏目！");

			}

			break;
		}

	}

}
