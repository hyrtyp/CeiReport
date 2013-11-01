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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 数字快讯
 * 
 * @author tmy
 * 
 */
public class EconDateNumberActivity extends ContainerActivity implements
		OnClickListener, OnItemClickListener {
	private ImageView img_econ_main, img_szks, img_zjzs, img_fxyc, img_zbsc,
			img_data_search;
	public static String MODEL_NAME;
	private TextView tv_left, tv_center;
	Context context = EconDateNumberActivity.this;
	private Application application;
	private String firstfunid;
	private String currentFunId;
	private String nowId;
	private ColumnEntry columnEntry;
	private List<New> groupNewsList = new ArrayList<New>();
	private CeiApplication mapplication;
	private List<New> newsList = new ArrayList<New>();;
	List<ColumnEntry> groupDataList = new ArrayList<ColumnEntry>();
	private GGridView gGridView;
	private GridViewAdapter gAdapter;
	private List<ColumnEntry> columnEntries;
	private ListView lv;
	private EconAdapter adapter;
	private RelativeLayout rl, r2;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (newsList == null) {
				return;
			}
			// 绑定数据，所有的数据都要放在newsList中，每刷新一次，就要先清空以前的数据
			adapter = new EconAdapter(context, newsList);
			lv.setAdapter(adapter);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_data_szkx);
		MODEL_NAME = ((CeiApplication) getApplication()).nowStart;// 获取当前业务名称。
		application = (CeiApplication) getApplication();
		mapplication = (CeiApplication) getApplication();
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		// String model = "数字快讯";
		initView();
		// 通过传递父ID,得到子栏目
		ColumnEntry col = columnEntry.getColByName(EconDataMain.MODEL_NAME);
		if (columnEntry.getColByName("数字快讯", col.getId()) == null)
			return;
		columnEntries = (List<ColumnEntry>) columnEntry
				.getEntryChildsForParent(columnEntry.getColByName("数字快讯",
						col.getId()).getId());
		gAdapter = new GridViewAdapter(context, columnEntries);
		gGridView.setAdapter(gAdapter);
		currentFunId = columnEntries.get(0).getId();
		refreshListData(currentFunId);

	}

	private void initView() {
		lv = (ListView) findViewById(R.id.econ_szkxs_list_content);
		tv_left = (TextView) findViewById(R.id.econ_szkx_tops_text1);
		tv_center = (TextView) findViewById(R.id.econ_szkx_tops_img);
		tv_left.setText(MODEL_NAME);
		tv_center.setText("数字快讯");
		img_szks = (ImageView) findViewById(R.id.econ_szkx_bottons_1);
		img_zjzs = (ImageView) findViewById(R.id.econ_szkx_bottons_2);
		img_fxyc = (ImageView) findViewById(R.id.econ_szkx_bottons_3);
		img_zbsc = (ImageView) findViewById(R.id.econ_szkx_bottons_4);
		// img_data_search = (ImageView) findViewById(R.id.econ_szkx_tops_tv2);
		tv_left.setOnClickListener(this);
		// 对自定义GridView进行初始化
		gGridView = (GGridView) findViewById(R.id.econ_data_info_gridview);
		gGridView.setOnItemClickListener(this);
		lv.setOnItemClickListener(this);
		img_szks.setOnClickListener(this);
		img_zjzs.setOnClickListener(this);
		img_fxyc.setOnClickListener(this);
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
			return;

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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.econ_data_info_gridview:
			currentFunId = columnEntries.get(position).getId();
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

		case R.id.econ_szkxs_list_content:

			ColumnEntry col = columnEntry.getColByName(EconDataMain.MODEL_NAME);
			if (columnEntry.getColByName("数字快讯", col.getId()) == null)
				return;
			firstfunid = columnEntry.getColByName("数字快讯", col.getId()).getId();
			New oneNew = newsList.get(position);
			//nowId = columnEntry.getColByName(oneNew.getFunname()).getId();
			if (isHasfunID(firstfunid) ||(isHasfunID(currentFunId))||  oneNew.getIsfree().equals("1")) {
				Intent intent = new Intent(EconDateNumberActivity.this,
						EconDataContent.class);
				intent.putExtra("id", oneNew.getId());
				startActivity(intent);
			} else {
				// 栏目没有购买
				MyTools.exitShow(EconDateNumberActivity.this, getWindow()
						.getDecorView(), "未购买该栏目！");
			}

			break;
		}

	}

}
