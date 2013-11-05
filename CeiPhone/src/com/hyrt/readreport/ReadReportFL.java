package com.hyrt.readreport;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.adapter.FlTableAdapter;
import com.hyrt.cei.adapter.ReadReportAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.ui.common.LoginActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.ReportpaitUtil;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Report;
import com.hyrt.cei.vo.ReportpaitElement;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.common.HomePageDZB;
import com.hyrt.readreport.view.GGridView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReadReportFL extends ContainerActivity implements OnClickListener {
	private ColumnEntry columnEntry;
	private ImageView goodImg, paihangImg, fenleiImg, mianfeiImg, homeImg,
			findImg, bookself;
	private ListView flList;
	private GGridView flGridView1, flGridView2;
	private HorizontalScrollView flTable;
	private List<Report> findData;
	private StringBuilder colIDs = null;
	private List<ReportpaitElement> firstData, secondData, allData;
	private ReportpaitElement rootElement;
	private DataHelper dataHelper;
	private ReadReportAdapter adapter;
	private int pageindex = 1;
	private String nowId;
	private TextView moreText, iconImg, backImg;
	private Handler findHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 2) {
				// 已达最低层
				adapter = new ReadReportAdapter(ReadReportFL.this, findData,
						flList);
				flList.setAdapter(adapter);
				if(findData.size()<20)
					moreText.setVisibility(View.GONE);
			} else if (msg.what == 404) {
				//Toast.makeText(ReadReportFL.this, "分类下没有数据!", 2).show();
				MyTools.exitShow(ReadReportFL.this,ReadReportFL.this.
						getWindow().getDecorView(),  "分类下没有数据！");
			} else if (msg.what == 3) {
				if (msg.arg1 < 20) {
					moreText.setVisibility(View.GONE);
				}
				if (adapter != null)
					adapter.notifyDataSetChanged();
			} else {
				click(rootElement, 1);
				if (firstData.size() > 0) {
					click(firstData.get(0), 2);
					if(secondData.size()>0){
						// 加载默认数据
						nowId = secondData.get(0).getId();
						if (((CeiApplication) getApplication()).isNet()) {
							String reportData = Service.queryAllClassTypeReport(nowId,
									pageindex + "");
							try {
								findData = XmlUtil.parseReport(reportData);
								// 保存数据库
								for (Report report : findData) {
									report.setPartitiontID(nowId);
									dataHelper.saveAllReport(report);
								}
								findHandler.sendEmptyMessage(2);
							} catch (Exception e) {
								MyTools.showPushXml(getApplicationContext());
								e.printStackTrace();
							}
						}
					}else if(!firstData.get(0).isMhasChild()){
						nowId = firstData.get(0).getId();
						if (((CeiApplication) getApplication()).isNet()) {
							String reportData = Service.queryAllClassTypeReport(nowId,
									pageindex + "");
							try {
								findData = XmlUtil.parseReport(reportData);
								// 保存数据库
								for (Report report : findData) {
									report.setPartitiontID(nowId);
									dataHelper.saveAllReport(report);
								}
								findHandler.sendEmptyMessage(2);
							} catch (Exception e) {
								MyTools.showPushXml(getApplicationContext());
								e.printStackTrace();
							}
						}
					}
				}
				FlTableAdapter adapter = new FlTableAdapter(ReadReportFL.this,
						firstData, 0);
				FlTableAdapter adapter1 = new FlTableAdapter(ReadReportFL.this,
						secondData, 0);
				flGridView1.setAdapter(adapter);
				flGridView2.setAdapter(adapter1);
				flGridView1.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// findData.clear();
						// 换点击item背景
						for (int i = 0; i < arg0.getChildCount(); i++) {
							RelativeLayout rl = (RelativeLayout) arg0
									.getChildAt(i);
							if (i == arg2) {
								((ImageView) (rl.getChildAt(0)))
										.setImageResource(R.drawable.phone_study_menu_select);
								((TextView) rl.getChildAt(1))
										.setTextColor(Color.WHITE);
							} else {
								((ImageView) (rl.getChildAt(0)))
										.setImageResource(R.drawable.menu_transbg);
								((TextView) rl.getChildAt(1))
										.setTextColor(Color.BLUE);
							}
						}
						// 第二层的数据集合
						ReportpaitElement element = (ReportpaitElement) arg0
								.getAdapter().getItem(arg2);
						click(element, 2);

						if (secondData != null) {//
							// 加载下一层数据
							/*
							 * findViewById(R.id.read_report_fltable1).setVisibility
							 * (View.VISIBLE);
							 * flGridView2.setVisibility(View.VISIBLE);
							 */

							FlTableAdapter adapter = new FlTableAdapter(
									ReadReportFL.this, secondData, -1);
							flGridView2.setAdapter(adapter);
							
						}
					}
				});
				flGridView2
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(
							AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						findData.clear();
						// 换点击item背景
						for (int i = 0; i < arg0
								.getChildCount(); i++) {
							RelativeLayout rl = (RelativeLayout) arg0
									.getChildAt(i);
							if (i == arg2) {
								((ImageView) (rl
										.getChildAt(0)))
										.setImageResource(R.drawable.phone_study_menu_select);
								((TextView) rl
										.getChildAt(1))
										.setTextColor(Color.WHITE);
							} else {
								((ImageView) (rl
										.getChildAt(0)))
										.setImageResource(R.drawable.menu_transbg);
								((TextView) rl
										.getChildAt(1))
										.setTextColor(Color.BLUE);
							}
						}
						ReportpaitElement element = (ReportpaitElement) arg0
								.getAdapter().getItem(arg2);
						click(element, 3);
					}
				});
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_report_find);
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		dataHelper = ((CeiApplication) getApplication()).dataHelper;
		initView();
//		imgLight();
		initData();
	}

	private void initView() {
		flList = (ListView) findViewById(R.id.read_report_data_lv);
		flTable = (HorizontalScrollView) findViewById(R.id.read_report_fltable);
		flTable.setVisibility(View.VISIBLE);
		flGridView1 = (GGridView) findViewById(R.id.read_report_fl_gv);
		flGridView2 = (GGridView) findViewById(R.id.read_report_fl_gv2);
//		goodImg = (ImageView) findViewById(R.id.read_report_jp);
//		goodImg.setOnClickListener(this);
//		paihangImg = (ImageView) findViewById(R.id.read_report_ph);
//		paihangImg.setOnClickListener(this);
//		fenleiImg = (ImageView) findViewById(R.id.read_report_fl);
//		fenleiImg.setOnClickListener(this);
//		mianfeiImg = (ImageView) findViewById(R.id.read_report_mf);
//		mianfeiImg.setOnClickListener(this);
		/*
		 * homeImg = (ImageView) findViewById(R.id.read_report_home);
		 * homeImg.setOnClickListener(this);
		 */
		iconImg = (TextView) findViewById(R.id.read_report_topicon);
		iconImg.setText("报告分类");
		bookself = (ImageView) findViewById(R.id.ib_findbg_bookshelf);
		bookself.setOnClickListener(this);
//		findImg = (ImageView) findViewById(R.id.read_report_find);
//		findImg.setOnClickListener(this);
		moreText = (TextView) findViewById(R.id.read_report_more);
		moreText.setOnClickListener(this);
		backImg = (TextView) findViewById(R.id.ib_findbg_back);
		backImg.setOnClickListener(this);
	}

	private void initData() {
		firstData = new ArrayList<ReportpaitElement>();
		secondData = new ArrayList<ReportpaitElement>();
		findData = new ArrayList<Report>();
		colIDs = new StringBuilder();
		ColumnEntry allColBg = columnEntry.getColByName(ReadReportMainActivity.MODEL_NAME);
		if (allColBg != null) {
			String allBgId = allColBg.getId();
			if (allBgId != null) {
				List<ColumnEntry> allCol = columnEntry
						.getEntryChildsForParent(allBgId);
				for (ColumnEntry columnEntry : allCol) {
					String forId = columnEntry.getId();
					if (this.columnEntry.getEntryChildsForParent(forId).size() != 0
							&& this.columnEntry.getEntryChildsForParent(forId) != null) {
						List<ColumnEntry> childCols = this.columnEntry
								.getEntryChildsForParent(forId);
						for (ColumnEntry columnEntry2 : childCols) {
							colIDs.append(columnEntry2.getId() + ",");
						}
					} else {
						colIDs.append(forId + ",");
					}
				}
			}
		}

		new Thread() {

			@Override
			public void run() {
				String retCord = "";
				if (((CeiApplication) getApplication()).isNet()) {
					// 获取报告分类类表
					retCord = Service.queryReportPartition(colIDs.toString());
					WriteOrRead.write(retCord, MyTools.nativeData, "portAll");
				} else {
					retCord = WriteOrRead.read(MyTools.nativeData, "portAll");
				}

				// 解析
				try {
					allData = XmlUtil.parseReportPart(retCord);
					if (allData == null || allData.size() == 0) {
						findHandler.sendEmptyMessage(404);
						return;
					}
					for (ReportpaitElement reportpaitElement : allData) {
						if (reportpaitElement.getParent() == null
								|| reportpaitElement.getParent().equals("")) {
							reportpaitElement.setLevel(0);
							reportpaitElement.setExpanded(false);
							reportpaitElement.setMhasParent(false);
							reportpaitElement.setMhasChild(true);
							rootElement = reportpaitElement;
						} else if (!reportpaitElement.getParent().equals("")
								&& ReportpaitUtil.getChild(allData,
										reportpaitElement.getId())) {
							reportpaitElement.setLevel(1);
							reportpaitElement.setExpanded(false);
							reportpaitElement.setMhasParent(true);
							reportpaitElement.setMhasChild(true);
						} else if (!reportpaitElement.getParent().equals("")
								&& !ReportpaitUtil.getChild(allData,
										reportpaitElement.getId())) {
							reportpaitElement.setLevel(2);
							reportpaitElement.setExpanded(false);
							reportpaitElement.setMhasParent(true);
							reportpaitElement.setMhasChild(false);
						}
					}
					Message msg = new Message();
					msg.what = 1;
					if (findHandler != null) {
						findHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					MyTools.showPushXml(getApplicationContext());
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.read_report_jp:
			intent = new Intent(this, ReadReportGoodActivity.class);
			startActivity(intent);
			break;
		case R.id.read_report_ph:
			intent = new Intent(this, ReadReportPH.class);
			startActivity(intent);
			break;
		case R.id.read_report_fl:
			break;
		case R.id.read_report_mf:
			intent = new Intent(this, ReadReportMF.class);
			startActivity(intent);
			break;
		case R.id.read_report_more:
			pageindex++;
			if (((CeiApplication) getApplication()).isNet()) {
				String reportData = Service.queryAllClassTypeReport(nowId,
						pageindex + "");
				try {
					List<Report> reports = XmlUtil.parseReport(reportData);
					if(reports!=null){
						findData.addAll(reports);
					}
					// 保存数据库
					for (Report report : findData) {
						report.setPartitiontID(nowId);
						dataHelper.saveAllReport(report);
					}
					Message msg = new Message();
					msg.what = 3;
					msg.arg1 = reports.size();
					findHandler.sendMessage(msg);
				} catch (Exception e) {
					MyTools.showPushXml(getApplicationContext());
					e.printStackTrace();
				}
			}
			break;
		case R.id.ib_findbg_bookshelf:
			intent = new Intent(this, CeiShelfBookActivity.class);
			startActivity(intent);
			break;
		case R.id.ib_findbg_back:
			intent = new Intent(this, ReadReportMainActivity.class);
			startActivity(intent);
			break;
		case R.id.read_report_find:
			intent = new Intent(this, ReadReportFind.class);
			startActivity(intent);
			break;
		}
	}

	private void click(ReportpaitElement element, int index) {
		if (!element.isMhasChild()) {
			// 到达最低层。请求服务端数据
			pageindex = 1;
			moreText.setVisibility(View.VISIBLE);
			final String id = element.getId();
			new Thread() {
				@Override
				public void run() {
					if (((CeiApplication) getApplication()).isNet()) {
						String reportData = Service.queryAllClassTypeReport(id,
								pageindex + "");
						try {
							nowId = id;
							findData = XmlUtil.parseReport(reportData);
							// 保存数据库
							for (Report report : findData) {
								report.setPartitiontID(id);
								dataHelper.saveAllReport(report);
							}
							findHandler.sendEmptyMessage(2);
						} catch (Exception e) {
							MyTools.showPushXml(getApplicationContext());
							e.printStackTrace();
						}
					} else {
						findData.clear();
						findData.addAll(dataHelper.getAllReportListByID(id));
						findHandler.sendEmptyMessage(2);
					}
				}
			}.start();
			if (index == 2) {
				findViewById(R.id.read_report_fltable1)
						.setVisibility(View.GONE);
			}
			return;
		}
		if (index == 1) {
			for (ReportpaitElement reportElement : allData) {
				if (reportElement.getParent().equals(element.getId())) {
					firstData.add(reportElement);
				}
			}
		} else if (index == 2) {
			secondData.clear();
			for (ReportpaitElement reportElement : allData) {
				if (reportElement.getParent().equals(element.getId())) {
					secondData.add(reportElement);
				}
			}
			if (secondData.size() > 1) {
				findViewById(R.id.read_report_fltable1).setVisibility(
						View.VISIBLE);
			} else {
				findViewById(R.id.read_report_fltable1)
						.setVisibility(View.GONE);
			}
		}

	}

	private void imgLight() {
		goodImg.setBackgroundResource(R.drawable.read_report_jp1);
		paihangImg.setBackgroundResource(R.drawable.read_report_ph1);
		fenleiImg.setBackgroundResource(R.drawable.read_report_fl);
		mianfeiImg.setBackgroundResource(R.drawable.read_report_mf1);
		// homeImg.setBackgroundResource(R.drawable.home1);
		findImg.setBackgroundResource(R.drawable.read_report_find1);
	}

	@Override
	protected void onResume() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
		super.onResume();
	}
}
