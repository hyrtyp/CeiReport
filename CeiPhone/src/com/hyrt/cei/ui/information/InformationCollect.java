package com.hyrt.cei.ui.information;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hyrt.cei.adapter.CommonPopAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.vo.funId;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;

public class InformationCollect extends ContainerActivity implements
		OnClickListener, OnItemClickListener {
	// 资讯列表数据集合
	private List<InfoNew> news = new ArrayList<InfoNew>();
	private Intent intent;
	private Boolean canRead;
	private List<funId> funIds;
	private ColumnEntry columnEntry;
	private List<ColumnEntry> columnEntries;
	private static final int ALREADY_BUY = 3;
	private Boolean BJ;
	// 收藏列表listview
	private ListView listView;
	// 弹出框信息列表
	private List<InfoNew> infoNews = new ArrayList<InfoNew>();
	// 收藏列表适配器
	private CommonPopAdapter adapter;
	// 视图通知器
	private Handler dataHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			InformationCollect.this.adapter = new CommonPopAdapter(
					InformationCollect.this,
					R.layout.common_listpop_listview_item, infoNews);
			listView.setAdapter(InformationCollect.this.adapter);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BJ = true;
		setContentView(R.layout.infomation_collect);
		listView = (ListView) findViewById(R.id.read_report_main_lv);
		listView.setOnItemClickListener(this);
		findViewById(R.id.zjzx_bianji).setOnClickListener(this);
		findViewById(R.id.zjzx_qingkong).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_1).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_2).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_3).setOnClickListener(this);
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		columnEntries = columnEntry.getEntryChildsForParent(columnEntry
				.getColByName(InformationActivity.MODEL_NAME).getId());
		refreshListData(columnEntry.getUserId(), ALREADY_BUY);
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				InformationCollect.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						String result = Service
								.queryCollect(((CeiApplication) InformationCollect.this
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
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.read_report_main_lv:
			InfoNew new2 = infoNews.get(arg2);
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
			intent.putExtra("collect", new2.getIsCollect());
			intent.setClass(InformationCollect.this,
					InformationReadActivity.class);
			if (infoNews.get(arg2).getIsfree().endsWith("1")) {
				InformationCollect.this.startActivity(intent);
			} else {
				for (int i = 0; i < funIds.size(); i++) {
					if (funIds.get(i).getFunid()
							.endsWith(infoNews.get(arg2).getFunctionId())) {
						canRead = true;
						break;
					} else {
						canRead = false;
					}
				}
				if (canRead) {
					InformationCollect.this.startActivity(intent);
				} else {
					MyTools.exitShow(InformationCollect.this, getWindow()
							.getDecorView(), "未购买该栏目！");
				}
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zjzx_botton_1:
			intent = new Intent(InformationCollect.this, InformationOne.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_2:
			intent = new Intent(InformationCollect.this, InformationTwo.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_3:
			intent = new Intent(InformationCollect.this, InformationThree.class);
			startActivity(intent);
			break;
		case R.id.zjzx_bianji:
			if (infoNews.isEmpty()) {
				Toast.makeText(InformationCollect.this, "收藏列表为空！",
						Toast.LENGTH_LONG).show();
			} else {
				if (BJ == true) {
					for (int i = 0; i < infoNews.size(); i++) {
						infoNews.get(i).setIsCollect("0");
					}
					BJ = false;
					adapter.notifyDataSetChanged();
				} else {
					for (int i = 0; i < infoNews.size(); i++) {
						infoNews.get(i).setIsCollect("1");
					}
					BJ = true;
					adapter.notifyDataSetChanged();
				}
			}
			break;
		case R.id.zjzx_qingkong:
			if (infoNews.isEmpty()) {
				Toast.makeText(InformationCollect.this, "收藏列表为空！",
						Toast.LENGTH_LONG).show();
			} else {
				AlertDialog alertDialog = new AlertDialog.Builder(this)
						.setTitle("清空收藏")
						.setMessage("您确定清空所有资讯吗？")
						.setIcon(R.drawable.icon)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										InformationCollect.this
												.runOnUiThread(new Runnable() {
													@Override
													public void run() {
														Service.clearCollect(((CeiApplication) InformationCollect.this
																.getApplication()).columnEntry
																.getUserId());
													}
												});
										infoNews.clear();
										adapter.notifyDataSetChanged();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).create();
				alertDialog.show();
			}
			break;
		}
	}

	private void refreshListData(final String functionId, final int operationId) {
		news.clear();
		InformationCollect.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String result = "";
				result = Service.queryBuyNews(functionId);
				try {
					funIds = XmlUtil.queryBuyNews(result);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
}
