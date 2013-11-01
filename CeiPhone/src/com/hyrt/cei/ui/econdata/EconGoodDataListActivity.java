package com.hyrt.cei.ui.econdata;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.adapter.EconAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.econdata.adapter.GridViewAdapter;
import com.hyrt.cei.ui.information.view.GGridView;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.New;
import com.hyrt.cei.vo.funId;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class EconGoodDataListActivity extends Activity implements
		OnClickListener, OnItemClickListener {
	private ImageView img_econ_main, img_szks, img_zjzs, img_fxyc, img_zbsc,
			img_data_search;
	Context context = EconGoodDataListActivity.this;
	private Application application;
	private ColumnEntry columnEntry;
	private CeiApplication mapplication;
	private List<New> DataList = new ArrayList<New>();;
	private List<ColumnEntry> columnEntries;
	private ListView lv;
	private String nowID;
	private EconAdapter adapter;
	//public static final String MODEL_NAME = "经济数据";
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 绑定适配器
			if (DataList == null) {
				return;
			}
			// 绑定数据，所有的数据都要放在newsList中，每刷新一次，就要先清空以前的数据
			adapter = new EconAdapter(context, DataList);
			lv.setAdapter(adapter);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.gooddata);
		super.onCreate(savedInstanceState);
		initView();
		application = (CeiApplication) getApplication();
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		nowID = getIntent().getStringExtra("id");
		refreshList();
	}

	private void initView() {
		lv = (ListView) findViewById(R.id.econ_good_list_content);
		img_econ_main = (ImageView) findViewById(R.id.econ_good_tops_text1);
		img_szks = (ImageView) findViewById(R.id.econ_good_bottons_1);
		img_zjzs = (ImageView) findViewById(R.id.econ_good_bottons_2);
		img_fxyc = (ImageView) findViewById(R.id.econ_good_bottons_3);
		img_zbsc = (ImageView) findViewById(R.id.econ_good_bottons_4);
		img_econ_main.setOnClickListener(this);
		// 对自定义GridView进行初始化
		lv.setOnItemClickListener(this);
		img_szks.setOnClickListener(this);
		img_zjzs.setOnClickListener(this);
		img_fxyc.setOnClickListener(this);
		img_zbsc.setOnClickListener(this);

	}

	private void refreshList() {
		DataList = new ArrayList<New>();
		new Thread() {

			@Override
			public void run() {
				ColumnEntry col=columnEntry.getColByName(EconDataMain.MODEL_NAME);
				if(columnEntry.getColByName("精彩数据",col.getId())==null) return;
				ColumnEntry colBg = columnEntry.getColByName("精彩数据",col.getId());

				if (colBg != null) {
					String numberId = colBg.getId();
					if (numberId != null && !numberId.equals("")) {
						try {
							List<New> dataList = null;
							if (((CeiApplication) getApplication()).isNet()) {
								String zhib = Service.querydbsByFunctionId(
										numberId, "40");
								dataList = XmlUtil.getNews(zhib);
								WriteOrRead.write(zhib, MyTools.nativeData,
										"goodecon.xml");
								String buyEcon = Service
										.queryBuyDbNews(columnEntry.getUserId());
								WriteOrRead.write(buyEcon, MyTools.nativeData,
										"buyEcon.xml");
								// 清空集合中的数据
								mapplication.buyEconData.clear();
								// 把购买的数据加入集合中
								mapplication.buyEconData.addAll(XmlUtil
										.queryBuyNews(buyEcon));
							} else {
								String zhib = WriteOrRead.read(
										MyTools.nativeData, "goodecon.xml");
								dataList = XmlUtil.getNews(zhib);
								// 购买的新闻
								String buyEcon = Service
										.queryBuyDbNews(columnEntry.getUserId());
								WriteOrRead.write(buyEcon, MyTools.nativeData,
										"buyEcon.xml");
								// 清空集合中的数据
								mapplication.buyEconData.clear();
								// 把购买的数据加入集合中
								mapplication.buyEconData.addAll(XmlUtil
										.queryBuyNews(buyEcon));
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

	private boolean isHasfunID(String funid) {
		CeiApplication application = (CeiApplication) getApplication();
		for (funId id : application.buyEconData) {
			if (funid.equals(id.getFunid())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.econ_good_list_content:
			// 传递id
			String funid = columnEntry.getColByName(EconDataMain.MODEL_NAME).getId();
			New oneNew = DataList.get(position);
			System.out.println("title" + oneNew.getTitle());
			if (isHasfunID(funid) || oneNew.getIsfree().equals("1")) {
				Intent intent = new Intent(EconGoodDataListActivity.this,
						EconDataContent.class);
				intent.putExtra("id", oneNew.getId());
				startActivity(intent);
			} else {
				// 栏目没有购买
				MyTools.exitShow(EconGoodDataListActivity.this, getWindow()
						.getDecorView(), "未购买该栏目！");
			}
			break;

		}
	}

	@Override
	public void onClick(View v) {
		if (v == img_econ_main) {
			Intent intent = new Intent(context, EconDataMain.class);
			startActivity(intent);

		} else if (v == img_szks) {
			Intent intent = new Intent(context, EconDateNumberActivity.class);
			startActivity(intent);

		} else if (v == img_zjzs) {
			Intent intent = new Intent(context, EconZZDataActivity.class);
			startActivity(intent);

		} else if (v == img_fxyc) {
			Intent intent = new Intent(context, EconFXDataActivity.class);
			startActivity(intent);

		} else if (v == img_zbsc) {
			Intent intent = new Intent(context, EconZBQueryActivity.class);
			startActivity(intent);

		}

	}

}
