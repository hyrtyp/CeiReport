package com.hyrt.cei.ui.ebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.ReportAdapter1;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.ui.ebook.view.CustomScrollView;
import com.hyrt.cei.ui.ebook.view.CustomScrollView.OnLoadListener;
import com.hyrt.cei.ui.ebook.view.CustomScrollView.OnRefreshListener;
import com.hyrt.cei.ui.phonestudy.HomePageActivity;
import com.hyrt.cei.ui.phonestudy.view.FlowRelativeyout;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.TimeOutHelper;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Report;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ReadReportActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener {
	// public static List<Class> activitys;
	public static boolean bbStart;
	public static String MODEL_NAME;
	// 研究报告的子集业务集合
	public static ColumnEntry allColBg;
	private String jcBgId;
	private TimeOutHelper timeOutHelper;
	private int pageIndex = 1;
	private ImageView homePage, homeReport, goodsView, sortView, partView,
			mianfeiReport, findReport;
	private GridView book_gv;
	private ImageButton bookSelf, back;
	private ColumnEntry columnEntry;
	private CeiApplication application;
	private List<Report> goodReportData, reportData;
	private ReportAdapter1 newAdapter;
	private Button moreReport;
	// 精彩3D报告的父级元素，用于3D效果的一些操作
	private FlowRelativeyout flowLayout;
	// 用户加载图片的工具类
	private AsyncImageLoader asyncImageLoader;
	// 记录最热报告图片下载数量
	private int x = 0;
	private StringBuilder colIDs = null;
	private LinearLayout prolayout;
	private CustomScrollView scrollView = null;
	Map<Drawable, String> drawToPath = new HashMap<Drawable, String>();
	private Handler viewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == HomePageActivity.GO_DETAIL
					&& flowLayout.getLinkList().get(1) < goodReportData.size()) {
				Report report = null;
				for (int i = 0; i < goodReportData.size(); i++) {
					if (x < 6) {
						return;
					}
					if (goodReportData.get(i).getPpath()
							.equals(drawToPath.get(flowLayout.currentDrawable))) {
						report = goodReportData.get(i);
					}
				}
				Intent intent = new Intent(ReadReportActivity.this,
						ReportIntro.class);// OpenBookActivity
				intent.putExtra("report", report);
				startActivity(intent);
			} else if (msg.arg1 == 12) {
				/*
				 * newAdapter = new ReportAdapter1(ReadReportActivity.this,
				 * book_gv, reportData); book_gv.setAdapter(newAdapter);
				 * book_gv.setOnItemClickListener(ReadReportActivity.this);
				 */
				if (msg.arg2 < 20 || reportData.size() >= 40) {
					moreReport.setVisibility(View.GONE);
					scrollView.setOnLoadListener(null);
				}
				if (newAdapter != null)
					newAdapter.notifyDataSetChanged();
			} else {
				// 为3D控件注册事件
				flowLayout.registEventForFlowRelativeyout(viewHandler);
				final Drawable[] drawables = new Drawable[6];
				x = 0;
				for (int i = 0; i < goodReportData.size() && i<6; i++) {
					final String imageUrl = goodReportData.get(i).getPpath();
					ImageResourse imageResource = new ImageResourse();
					imageResource.setIconUrl(imageUrl);
					imageResource.setIconId(goodReportData.get(i).getId());
					imageResource.setType("1");
					asyncImageLoader.loadDrawable(imageResource,
							new AsyncImageLoader.ImageCallback() {

								@Override
								public void imageLoaded(Drawable drawable,
										String path) {
									if (drawable != null && x <= 5) {
										drawables[x] = drawable;
										x++;
										drawToPath.put(drawable, path);
										// 如果图片都加载好了的话，就为图片增加事件
										if (x == 6) {
											flowLayout.loadImgForIv(drawables);
										}
									}
								}
							});
				}
				newAdapter = new ReportAdapter1(ReadReportActivity.this,
						book_gv, reportData);
				if (reportData.size() < 20) {
					moreReport.setVisibility(View.GONE);
				}
				book_gv.setAdapter(newAdapter);
				book_gv.setOnItemClickListener(ReadReportActivity.this);
				prolayout.setVisibility(View.GONE);
				// scrollView=()findViewById(R.id.phone_study_scrollview);
				scrollView.smoothScrollTo(0, 0);
			}
		}

	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (flowLayout != null)
			flowLayout.clearBitmaps();
		if (newAdapter != null)
			newAdapter.clearBitmaps();
		homePage = null;
		homeReport = null;
		goodsView = null;
		sortView = null;
		partView = null;
		mianfeiReport = null;
		findReport = null;
		book_gv = null;
		bookSelf = null;
		back = null;
		columnEntry = null;
		goodReportData = null;
		reportData = null;
		viewHandler = null;
		newAdapter = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yjbg_main);
		MODEL_NAME = ((CeiApplication) getApplication()).nowStart;// 获取当前业务名称。
		timeOutHelper = new TimeOutHelper(this);
		// overridePendingTransition(R.anim.push_in, R.anim.push_out);
		// activitys = new ArrayList<Class>();
		// activitys.add(ReadReportActivity.class);
		application = (CeiApplication) getApplication();
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		if(columnEntry.getColumnEntryChilds().size()>0){
		bbStart=columnEntry.getColumnEntryChilds().get(0).getName().equals("通用版")?true:false;
		}
		asyncImageLoader = ((CeiApplication) (getApplication())).asyncImageLoader;
		flowLayout = (FlowRelativeyout) findViewById(R.id.phone_study_top_ivparent);
		flowLayout.indexParent = (LinearLayout) findViewById(R.id.phone_study_indexParent);
		flowLayout.indexParent.getChildAt(0).setSelected(true);
		flowLayout.leftOperationIv = (ImageView) findViewById(R.id.phone_study_leftoperation);
		flowLayout.rightOperationIv = (ImageView) findViewById(R.id.phone_study_rightoperation);
		flowLayout.centerImageView = (ImageView) findViewById(R.id.phone_study_top_3);
		flowLayout.leftOneImageView = (ImageView) findViewById(R.id.phone_study_top_2);
		flowLayout.leftTwoImageView = (ImageView) findViewById(R.id.phone_study_top_1);
		flowLayout.rightOneImageView = (ImageView) findViewById(R.id.phone_study_top_4);
		flowLayout.rightTwoImageView = (ImageView) findViewById(R.id.phone_study_top_5);
		flowLayout.scrollview = (ScrollView) findViewById(R.id.phone_study_scrollview);
		initView();
		initData();

	}

	private void initData() {
		// 更据业务ID查询业务里面的数据
		allColBg = columnEntry.getColByName(MODEL_NAME);
		if (allColBg != null && allColBg.getId() != null
				&& !allColBg.getId().equals("")) {
			String allBgId = allColBg.getId();
			colIDs = new StringBuilder();
			List<ColumnEntry> allCol = columnEntry
					.getEntryChildsForParent(allBgId);
			for (ColumnEntry columnEntry : allCol) {
				if (columnEntry.getName().equals("置顶报告")) {
					jcBgId = columnEntry.getId();
				}
				colIDs.append(columnEntry.getId() + ",");
			}
		}

		new Thread() {

			@Override
			public void run() {
				timeOutHelper.installTimerTask();
				if (jcBgId != null && !jcBgId.equals("")) {
					try {
						if (((CeiApplication) (getApplication())).isNet()) {
							String retData = Service.queryReport(jcBgId, "bg",
									"1");
							goodReportData = new ArrayList<Report>();
							goodReportData.addAll(XmlUtil.parseReport(retData));

							String newRetData = Service.queryNewReport(
									colIDs.toString().substring(0,
											colIDs.toString().length() - 1),
									pageIndex + "");
							Log.i("sys", newRetData.length() + "");
							reportData = XmlUtil.parseReport(newRetData);
							// 本地缓存
							WriteOrRead.write(retData, MyTools.nativeData,
									"goodReport.xml");
							WriteOrRead.write(newRetData, MyTools.nativeData,
									"newReport.xml");
							String buyReport = Service
									.queryBuyReport(columnEntry.getUserId());
							WriteOrRead.write(buyReport, MyTools.nativeData,
									"buyReport.xml");
							application.buyReportData.clear();
							application.buyReportData.addAll(XmlUtil
									.queryBuyReports(buyReport));// 此处缺解析方法
						} else {
							String gooddata = WriteOrRead.read(
									MyTools.nativeData, "goodReport.xml");
							String newsdata = WriteOrRead.read(
									MyTools.nativeData, "newReport.xml");
							String buydata = WriteOrRead.read(
									MyTools.nativeData, "buyReport.xml");
							goodReportData = XmlUtil.parseReport(gooddata);
							reportData = XmlUtil.parseReport(newsdata);
							application.buyReportData.clear();
							application.buyReportData.addAll(XmlUtil
									.queryBuyNews(buydata));
						}

						if (viewHandler != null) {
							viewHandler.sendEmptyMessage(1);
						}
						timeOutHelper
								.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
					} catch (Exception e) {
						MyTools.showPushXml(getApplicationContext());
						timeOutHelper
								.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
						e.printStackTrace();
					}
				}

			}

		}.start();

	}

	private void initView() {
		TextView title=(TextView)(findViewById(R.id.top).findViewById(R.id.title));
		title.setText(MODEL_NAME);
		prolayout = (LinearLayout) findViewById(R.id.yjbg_read_pro);
		moreReport = (Button) findViewById(R.id.yjbg_more_report);
		moreReport.setOnClickListener(this);
		book_gv = (GridView) findViewById(R.id.yjbg_gv_book);
//		homePage = (ImageView) findViewById(R.id.iv_report_page_home);
//		homePage.setOnClickListener(this);
//		homeReport = (ImageView) findViewById(R.id.iv_report_home);
//		homeReport.setOnClickListener(this);
//		goodsView = (ImageView) findViewById(R.id.iv_report_goods);
//		goodsView.setOnClickListener(this);
//		sortView = (ImageView) findViewById(R.id.iv_report_sort);
//		sortView.setOnClickListener(this);
//		partView = (ImageView) findViewById(R.id.iv_report_partition);
//		partView.setOnClickListener(this);
//		mianfeiReport = (ImageView) findViewById(R.id.iv_report_miamfei);
//		mianfeiReport.setOnClickListener(this);
//		findReport = (ImageView) findViewById(R.id.iv_report_find);
//		findReport.setOnClickListener(this);
		bookSelf = (ImageButton) (findViewById(R.id.top).findViewById(R.id.ib_bg_bookshelf));
		bookSelf.setOnClickListener(this);
		back = (ImageButton) (findViewById(R.id.top).findViewById(R.id.ib_bg_back));
		back.setOnClickListener(this);
		scrollView = (CustomScrollView) findViewById(R.id.phone_study_scrollview);
		scrollView.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							// Thread.sleep(1500);
							ReadReportActivity.this.finish();
							startActivity(new Intent(ReadReportActivity.this,
									ReadReportActivity.class));
							Log.i("scl", "setOnRefreshListener");
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						scrollView.onRefreshComplete();
					}

				}.execute();
			}
		});
		// 到底加载更多

		scrollView.setOnLoadListener(new OnLoadListener() {
			public void onLoad() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try { // Thread.sleep(3000);
							Log.i("scl", "setOnLoadListener");
							// 加载更多
							if (colIDs != null) {
								try {
									pageIndex++;

									String newRetData = Service
											.queryNewReport(
													colIDs.toString()
															.substring(
																	0,
																	colIDs.toString()
																			.length() - 1),
													pageIndex + "");
									// reportData.clear();

									reportData.addAll(XmlUtil
											.parseReport(newRetData));
									Message msg = new Message();
									msg.arg1 = 12;
									msg.arg2 = XmlUtil.parseReport(newRetData)
											.size();
									viewHandler.sendMessage(msg);
								} catch (Exception e) {
									MyTools.showPushXml(getApplicationContext());
									e.printStackTrace();
								}
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						scrollView.onLoadComplete();
					}

				}.execute();
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (v == homeReport) {
			// 阅读报告页
			/*
			 * Intent intent = new Intent(this, ReadReportActivity.class);
			 * startActivity(intent); saveActivity(ReadReportActivity.class);
			 */
			return;

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
			Intent intent = new Intent(this, PartitionReportActivity.class);
			startActivity(intent);
			// saveActivity(PartitionReportActivity.class);
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
			// activitys.clear();
		} else if (v == back) {
			// 返回键
			/*
			 * if (activitys.size() > 1) {
			 * activitys.remove(activitys.get(activitys.size() - 1)); Intent
			 * intent = new Intent(this, activitys.get(activitys.size() - 1));
			 * startActivity(intent); }
			 */
		} else if (v == bookSelf) {
			// 书架
			// this.finish();
			Intent intent = new Intent(this, CeiShelfBookActivity.class);
			startActivity(intent);
			// saveActivity(CeiShelfBookActivity.class);
			// activitys.clear();
			return;
		} else if (v == moreReport) {
			// 加载更多
			if (colIDs != null) {
				try {
					pageIndex++;

					String newRetData = Service.queryNewReport(
							colIDs.toString().substring(0,
									colIDs.toString().length() - 1), pageIndex
									+ "");
					// reportData.clear();

					reportData.addAll(XmlUtil.parseReport(newRetData));
					Message msg = new Message();
					msg.arg1 = 12;
					msg.arg2 = XmlUtil.parseReport(newRetData).size();
					viewHandler.sendMessage(msg);
					return;
				} catch (Exception e) {
					MyTools.showPushXml(getApplicationContext());
					e.printStackTrace();
				}
			}
			return;
		}
		// this.finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Report report = (Report) arg0.getAdapter().getItem(arg2);
		Intent intent = new Intent(this, ReportIntro.class);// OpenBookActivity
		intent.putExtra("report", report);
		startActivity(intent);
	}

	/*
	 * private void saveActivity(Class<?> clas) { if (activitys.contains(clas))
	 * { activitys.remove(clas); activitys.add(clas); } else {
	 * activitys.add(clas); } }
	 */
}
