package com.hyrt.cei.ui.econdata;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.adapter.EconAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.econdata.adapter.EconDataZBAdapter;
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
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 指标速查
 * 
 * @author tmy
 * 
 */
public class EconZBQueryActivity extends ContainerActivity implements OnClickListener {
	private List<View> groupViews = new ArrayList<View>();
	private ColumnEntry columnEntry;
	private List<New> DataList;
	private ListView expList;
	CeiApplication application;
	public static String MODEL_NAME;
	private CeiApplication mapplication;
	boolean flage;
	private ImageView img_econ_main, img_szks, img_zjzs, img_fxyc, img_zbsc,
			img_data_search;
	private TextView tv_left,tv_center;
	Context context = EconZBQueryActivity.this;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			expList.setAdapter(new EconDataZBAdapter(EconZBQueryActivity.this,
					DataList));
			expList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ColumnEntry col=columnEntry.getColByName(EconDataMain.MODEL_NAME);
					if(columnEntry.getColByName("指标查询",col.getId())==null) return;
					String funid = columnEntry.getColByName("指标查询",col.getId()).getId();
					New oneNew = DataList.get(arg2);
					if (isHasfunID(funid)
							|| DataList.get(arg2).getIsfree().equals("1")) {
						String htmlUrl = MyTools.newsHtml
								+ DataList.get(arg2).getId();
					/*	for (View view : groupViews) {
							view.findViewById(R.id.econ_data_ea_item_group_tv)
									.setBackgroundResource(
											R.drawable.econ_data_ea_item_group);
						}
						arg1.findViewById(R.id.econ_data_ea_item_group_tv)
								.setBackgroundResource(
										R.drawable.econ_data_ea_item_group1);
						groupViews.add(arg1);*/
						Intent intent = new Intent(EconZBQueryActivity.this,
								EconDataContent.class);
						intent.putExtra("id", oneNew.getId());
						startActivity(intent);
					} else {
						// show location view指的是父容器，popwindow显示在父容器的什么位置
						MyTools.exitShow(EconZBQueryActivity.this,
								findViewById(R.id.econ_data_zb_all), "没有数据！");
					}
				}
			});
			flage = true;
			// prolayout.setVisibility(View.GONE);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_data_index_search);
		MODEL_NAME = ((CeiApplication) getApplication()).nowStart;// 获取当前业务名称。
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		application = (CeiApplication) getApplication();
		mapplication = (CeiApplication) getApplication();
		initView();
		initData();

	}

	private void initData() {
		DataList = new ArrayList<New>();
		new Thread() {

			@Override
			public void run() {
				ColumnEntry col=columnEntry.getColByName(EconDataMain.MODEL_NAME);
				if(columnEntry.getColByName("指标查询",col.getId())==null) return;
				ColumnEntry colBg = columnEntry.getColByName("指标查询",col.getId());
				if (colBg != null) {
					String numberId = colBg.getId();
					if (numberId != null && !numberId.equals("")) {
						if (((CeiApplication) getApplication()).isNet()) {
							String zhib = Service.querydbsByFunctionId(
									numberId, "40");
							try {
								List<New> dataList = XmlUtil.getNews(zhib);
								WriteOrRead.write(zhib, MyTools.nativeData,
										"zhibsc.xml");
								DataList.addAll(dataList);
								System.out.println(DataList.size());
								//购买的新闻
								String buyEcon = Service.queryBuyDbNews(columnEntry
										.getUserId());
								WriteOrRead.write(buyEcon, MyTools.nativeData,
										"buyEcon.xml");										
								// 清空集合中的数据
								mapplication.buyEconData.clear();
								// 把购买的数据加入集合中
								mapplication.buyEconData.addAll(XmlUtil
										.queryBuyNews(buyEcon));
								if (handler != null) {
									handler.sendEmptyMessage(1);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							String zhib = WriteOrRead.read(MyTools.nativeData,
									"zhibsc.xml");
							try {
								List<New> dataList = XmlUtil.getNews(zhib);
								DataList.addAll(dataList);
								String buyEcon = WriteOrRead.read(MyTools.nativeData,
										"buyEcon.xml");
								if (buyEcon != null && !buyEcon.equals("")) {
									mapplication.buyEconData.clear();
									mapplication.buyEconData.addAll(XmlUtil
											.queryBuyNews(buyEcon));
								}
								if (handler != null) {
									handler.sendEmptyMessage(2);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

			}

		}.start();

	}

	private void initView() {

		expList = (ListView) findViewById(R.id.econ_index_search_list_content);
		tv_left = (TextView) findViewById(R.id.econ_zj_num_top_text1);
		tv_left.setText(MODEL_NAME);
		tv_center=(TextView)findViewById(R.id.econ_main_top_img);
		tv_center.setText("指标速查");
		img_szks = (ImageView) findViewById(R.id.econ_index_search_botton_1);
		img_zjzs = (ImageView) findViewById(R.id.econ_index_search_botton_2);
		img_fxyc = (ImageView) findViewById(R.id.econ_index_search_botton_3);
		img_zbsc = (ImageView) findViewById(R.id.econ_index_search_botton_4);
		//img_data_search = (ImageView) findViewById(R.id.econ_main_top_tv2);
		tv_left.setOnClickListener(this);
		img_szks.setOnClickListener(this);
		img_fxyc.setOnClickListener(this);
		img_zjzs.setOnClickListener(this);
		img_zbsc.setOnClickListener(this);
		//img_data_search.setOnClickListener(this);

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
			Intent intent = new Intent(context, EconFXDataActivity.class);
			startActivity(intent);

		} else if (v == img_zbsc) {
			return;

		} 

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

}
