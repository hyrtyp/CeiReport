package com.hyrt.cei.ui.ebook;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.GoodReportAdapter;
import com.hyrt.cei.adapter.TreeViewAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.vo.ReportpaitElement;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.ReportpaitUtil;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Report;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PartitionReportActivity extends Activity implements
		OnClickListener {
	public static List<TextView> friFenL = new ArrayList<TextView>(),
			secFenL = new ArrayList<TextView>(),
			thrFenL = new ArrayList<TextView>();
	private ImageView homePage, homeReport, goodsView, sortView, partView,
			mianfeiReport, findReport;
	private StringBuilder colIDs;
	private ImageButton back, bookSelf;
	private ListView rightList;
	private ListView leftList;
	private List<Report> rightDate;
	private List<ReportpaitElement> leftData, allData;
	private ColumnEntry columnEntry;
	private TreeViewAdapter treeViewAdapter;
	private GoodReportAdapter adapter;
	private DataHelper dataHelper;
	private int pageIndex = 1, index = 1;;
	private TextView more;
	private String nowId;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				addExpLeftData();
				leftList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						for (TextView textView : thrFenL) {
							textView.setTextColor(Color.WHITE);
						}
						TextView view = (TextView) arg1.findViewById(R.id.text);
						view.setTextColor(Color.BLACK);
						thrFenL.add(view);

						pageIndex = 1;
						more.setVisibility(View.VISIBLE);
						if (!leftData.get(arg2).isMhasChild()) {
							// 到达最低层。请求服务端数据
							ReportpaitElement element = (ReportpaitElement) arg0
									.getAdapter().getItem(arg2);
							nowId = element.getId();
							new Thread() {

								@Override
								public void run() {
									if (((CeiApplication) getApplication())
											.isNet()) {
										String reportData = Service
												.queryAllClassTypeReport(nowId,
														pageIndex + "");
										try {
											rightDate = XmlUtil
													.parseReport(reportData);
											// 保存数据库
											for (Report report : rightDate) {
												report.setPartitiontID(nowId);
												dataHelper
														.saveAllReport(report);
											}
											handler.sendEmptyMessage(2);
										} catch (Exception e) {
											MyTools.showPushXml(
													getApplicationContext());
											e.printStackTrace();
										}
									} else {
										rightDate.clear();
										rightDate.addAll(dataHelper
												.getAllReportListByID(nowId));
										handler.sendEmptyMessage(2);
									}

								}

							}.start();
							return;
						}
						if (leftData.get(arg2).isExpanded()) {
							leftData.get(arg2).setExpanded(false);
							ReportpaitElement Element = leftData.get(arg2);
							ArrayList<ReportpaitElement> temp = new ArrayList<ReportpaitElement>();

							for (int i = arg2 + 1; i < leftData.size(); i++) {
								if (Element.getLevel() >= leftData.get(i)
										.getLevel()) {
									break;
								}
								temp.add(leftData.get(i));
							}

							leftData.removeAll(temp);
							treeViewAdapter = new TreeViewAdapter(
									PartitionReportActivity.this, leftData, -1);
							leftList.setAdapter(treeViewAdapter);
						} else {
							leftData.get(arg2).setExpanded(true);
							int level = leftData.get(arg2).getLevel();
							int nextLevel = level + 1;
							for (ReportpaitElement reportElement : allData) {
								int j = 1;
								if (reportElement.getParent().equals(
										leftData.get(arg2).getId())) {
									reportElement.setLevel(nextLevel);
									reportElement.setExpanded(false);
									leftData.add(arg2 + j, reportElement);
									j++;
								}
							}
							treeViewAdapter = new TreeViewAdapter(
									PartitionReportActivity.this, leftData, -1);
							leftList.setAdapter(treeViewAdapter);
						}
					}
				});
			} else if (msg.what == 2) {
				adapter = new GoodReportAdapter(PartitionReportActivity.this,
						rightDate, rightList);
				if(rightDate.size()<20)
					more.setVisibility(View.GONE);
				rightList.setAdapter(adapter);
			} else if (msg.what == 404) {
				//Toast.makeText(PartitionReportActivity.this, "分类下没有数据！", 2).show();
				MyTools.exitShow(PartitionReportActivity.this,PartitionReportActivity.this.
						getWindow().getDecorView(),  "分类下没有数据！");
			}else if (msg.what == 3) {
				if (msg.arg1 < 20) {
					more.setVisibility(View.GONE);
				}
				if (adapter != null)
					adapter.notifyDataSetChanged();
			}else if (msg.what == 4) {
					more.setVisibility(View.GONE);
			}
		}

	};

	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		homePage = null;
		homeReport = null;
		goodsView = null;
		sortView = null;
		partView = null;
		mianfeiReport = null;
		findReport = null;
		bookSelf = null;
		back = null;
		handler = null;
		rightList = null;
		leftList = null;
		rightDate = null;
		leftData = null;
		allData = null;
		treeViewAdapter = null;
		adapter = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yjbg_fenlei);
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		dataHelper = ((CeiApplication) getApplication()).dataHelper;
		initView();
		initData();
	}

	private void initData() {
		leftData = new ArrayList<ReportpaitElement>();
		rightDate = new ArrayList<Report>();
		colIDs = new StringBuilder();
		ColumnEntry allColBg = columnEntry.getColByName(((CeiApplication) getApplication()).nowStart);
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
						handler.sendEmptyMessage(404);
						return;
					}
					for (ReportpaitElement reportpaitElement : allData) {
						if (reportpaitElement.getParent() == null
								|| reportpaitElement.getParent().equals("")) {
							reportpaitElement.setLevel(0);
							reportpaitElement.setExpanded(false);
							reportpaitElement.setMhasParent(false);
							reportpaitElement.setMhasChild(true);
							leftData.add(reportpaitElement);
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
					if (handler != null) {
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					MyTools.showPushXml(getApplicationContext());
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void initView() {
		TextView title=(TextView)(findViewById(R.id.top).findViewById(R.id.title));
		title.setText("分类报告");
		more = (TextView) findViewById(R.id.more);
		more.setOnClickListener(this);
		homePage = (ImageView) findViewById(R.id.iv_fenlei_report_page_home);
		homePage.setOnClickListener(this);
		homeReport = (ImageView) findViewById(R.id.iv_fenlei_report_home);
		homeReport.setOnClickListener(this);
		goodsView = (ImageView) findViewById(R.id.iv_fenlei_report_goods);
		goodsView.setOnClickListener(this);
		sortView = (ImageView) findViewById(R.id.iv_fenlei_report_sort);
		sortView.setOnClickListener(this);
		partView = (ImageView) findViewById(R.id.iv_fenlei_report_partition);
		partView.setOnClickListener(this);
		mianfeiReport = (ImageView) findViewById(R.id.iv_fenlei_report_miamfei);
		mianfeiReport.setOnClickListener(this);
		findReport = (ImageView) findViewById(R.id.iv_fenlei_report_find);
		findReport.setOnClickListener(this);
		bookSelf = (ImageButton) (findViewById(R.id.top).findViewById(R.id.ib_bg_bookshelf));
		bookSelf.setOnClickListener(this);
		back = (ImageButton) (findViewById(R.id.top).findViewById(R.id.ib_bg_back));
		back.setOnClickListener(this);
		leftList = (ListView) findViewById(R.id.yjbg_feilei_list_left);
		rightList = (ListView) findViewById(R.id.yjbg_feilei_list_right);

	}

	@Override
	public void onClick(View v) {
		// if(ReadReportActivity.activitys!=null){
		if (v == homeReport) {
			// 阅读报告页
			Intent intent = new Intent(this, ReadReportActivity.class);
			startActivity(intent);
			// saveActivity(ReadReportActivity.class);

		} else if (v == goodsView) {
			Intent intent = new Intent(this, GoodsReportActivity.class);
			startActivity(intent);
			// saveActivity(GoodsReportActivity.class);
			// 精彩报告页
		} else if (v == sortView) {
			// 排序报告页
			Intent intent = new Intent(this, SortReportActivity.class);
			startActivity(intent);
			// saveActivity(SortReportActivity.class);
		} else if (v == partView) {
			// 分类报告页
			/*
			 * Intent intent = new Intent(this, PartitionReportActivity.class);
			 * startActivity(intent);
			 * saveActivity(PartitionReportActivity.class);
			 */
			return;
		} else if (v == mianfeiReport) {
			// 免费报告
			Intent intent = new Intent(this, MianfeiReportActivity.class);
			startActivity(intent);
			// saveActivity(MianfeiReportActivity.class);
		} else if (v == findReport) {
			// 报告查询查询
			Intent intent = new Intent(this, FindReportActivity.class);
			startActivity(intent);
			// saveActivity(FindReportActivity.class);
		} else if (v == homePage) {
			// 首页
			Intent intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
			// ReadReportActivity.activitys.clear();
		} else if (v == more) {
			//
			new Thread() {

				@Override
				public void run() {
					pageIndex++;
					if (((CeiApplication) getApplication()).isNet()) {
						String reportData = Service.queryAllClassTypeReport(
								nowId, pageIndex + "");
						try {
							List<Report> reports = XmlUtil
									.parseReport(reportData);
							if(reports.size()==0){
								handler.sendEmptyMessage(4);
								return;
							}
							rightDate.addAll(reports);
							// 保存数据库
							for (Report report : rightDate) {
								report.setPartitiontID(nowId);
								dataHelper.saveAllReport(report);
							}
							Message msg = new Message();
							msg.what = 3;
							msg.arg1 = reports.size();
							handler.sendMessage(msg);
						} catch (Exception e) {
							MyTools.showPushXml(getApplicationContext());
							e.printStackTrace();
						}
					}
				}

			}.start();

		} else if (v == bookSelf) {
			// 右侧数据图片
			Intent intent = new Intent(this, CeiShelfBookActivity.class);
			startActivity(intent);
			// saveActivity(CeiShelfBookActivity.class);
		}
	}

	private void addExpLeftData() {
		for (int i = 0; i < 2; i++) {
			if (leftData.size() > 0 && leftData.get(i).isMhasChild()
					&& !leftData.get(i).isExpanded()) {
				leftData.get(i).setExpanded(true);
				int level = leftData.get(i).getLevel();
				int nextLevel = level + 1;
				for (ReportpaitElement reportElement : allData) {
					int j = 1;
					if (reportElement.getParent().equals(
							leftData.get(i).getId())) {
						reportElement.setLevel(nextLevel);
						reportElement.setExpanded(false);
						leftData.add(i + j, reportElement);
						j++;
					}
				}
			}
		}
		if (leftData.get(1).isMhasChild()) {
			nowId = leftData.get(2).getId();
			index = 2;
		} else {
			nowId = leftData.get(1).getId();
		}

		treeViewAdapter = new TreeViewAdapter(PartitionReportActivity.this,
				leftData, index);
		leftList.setAdapter(treeViewAdapter);
		new Thread() {

			@Override
			public void run() {
				if (((CeiApplication) getApplication()).isNet()) {
					String reportData = Service.queryAllClassTypeReport(nowId,
							pageIndex + "");
					try {
						rightDate = XmlUtil.parseReport(reportData);
						// 保存数据库
						for (Report report : rightDate) {
							report.setPartitiontID(nowId);
							dataHelper.saveAllReport(report);
						}
						Message msg = new Message();
						msg.arg1 = index;
						msg.what = 2;
						handler.sendMessage(msg);
					} catch (Exception e) {
						MyTools.showPushXml(getApplicationContext());
						e.printStackTrace();
					}
				}
			}

		}.start();

	}

	@Override
	protected void onRestart() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
		super.onRestart();
	}
}
