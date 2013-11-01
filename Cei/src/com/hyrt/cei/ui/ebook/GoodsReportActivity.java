package com.hyrt.cei.ui.ebook;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.GoodReportAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Report;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GoodsReportActivity extends Activity implements OnClickListener {
	private ImageView homePage, homeReport, goodsView, sortView, partView,
			mianfeiReport, findReport;
	private ImageButton back, bookSelf;
	private ListView goodList;
	private List<Report> goodData;
	private ColumnEntry columnEntry;
	private GoodReportAdapter adapter;
	private int pageSize = 1;
	private LinearLayout loadProgressBar;
	private TextView moreTextView;
	private LinearLayout prolayout;
	private String bgId;
	private Handler goodHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 2) {
				if (msg.arg1 < 20) {
					moreTextView.setVisibility(View.GONE);
				} else {
					moreTextView.setVisibility(View.VISIBLE);
				}
				loadProgressBar.setVisibility(View.GONE);
				if (adapter != null)
					adapter.notifyDataSetChanged();
			}
			if (msg.what == 3) {
				loadProgressBar.setVisibility(View.GONE);
				moreTextView.setVisibility(View.GONE);
			} else {
				adapter = new GoodReportAdapter(GoodsReportActivity.this,
						goodData, goodList);
				if (goodData.size() >= 20)
					addPageMore();
				goodList.setAdapter(adapter);
			}
			prolayout.setVisibility(View.GONE);
		}

	};

	private void addPageMore() {
		View view = LayoutInflater.from(this).inflate(R.layout.list_page_load,
				null);
		moreTextView = (TextView) view.findViewById(R.id.more_id);
		loadProgressBar = (LinearLayout) view.findViewById(R.id.load_id);
		moreTextView.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 隐藏"加载更多"
				moreTextView.setVisibility(View.GONE);
				// 显示进度条
				loadProgressBar.setVisibility(View.VISIBLE);

				new Thread(new Runnable() {
					@Override
					public void run() {
						// 加载模拟数据：下一页数据， 在正常情况下，上面的休眠是不需要，直接使用下面这句代码加载相关数据
						try {
							if (bgId != null&&!bgId.equals("")) {
								pageSize++;
								String addBg = Service.queryReport(bgId, "bg",
										pageSize + "");
								List<Report> addReport = XmlUtil
										.parseReport(addBg);
								if (addReport.size() == 0) {
									goodHandler.sendEmptyMessage(3);
									return;
								}
								goodData.addAll(addReport);
								Message msg = new Message();
								msg.what = 2;
								msg.arg1 = addReport.size();
								goodHandler.sendMessage(msg);
							}

						} catch (Exception e) {
							MyTools.showPushXml(getApplicationContext());
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
		goodList.addFooterView(view);
	}

	@Override
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
		columnEntry = null;
		goodHandler = null;
		goodData = null;
		adapter = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yjbg_jingpin);
		// overridePendingTransition(R.anim.push_in, R.anim.push_out);
		initView();
		prolayout.setVisibility(View.VISIBLE);
		initData();
	}

	private void initView() {
		TextView title=(TextView)(findViewById(R.id.top).findViewById(R.id.title));
		title.setText("精彩报告");
		prolayout = (LinearLayout) findViewById(R.id.report_jingp_pro);
		goodList = (ListView) findViewById(R.id.yjbg_goodreport_list);

		homePage = (ImageView) findViewById(R.id.iv_good_report_page_home);
		homePage.setOnClickListener(this);
		homeReport = (ImageView) findViewById(R.id.iv_good_report_home);
		homeReport.setOnClickListener(this);
		goodsView = (ImageView) findViewById(R.id.iv_good_report_goods);
		goodsView.setOnClickListener(this);
		sortView = (ImageView) findViewById(R.id.iv_good_report_sort);
		sortView.setOnClickListener(this);
		partView = (ImageView) findViewById(R.id.iv_good_report_partition);
		partView.setOnClickListener(this);
		mianfeiReport = (ImageView) findViewById(R.id.iv_good_report_miamfei);
		mianfeiReport.setOnClickListener(this);
		findReport = (ImageView) findViewById(R.id.iv_good_report_find);
		findReport.setOnClickListener(this);
		bookSelf = (ImageButton) (findViewById(R.id.top).findViewById(R.id.ib_bg_bookshelf));
		bookSelf.setOnClickListener(this);
		back = (ImageButton) (findViewById(R.id.top).findViewById(R.id.ib_bg_back));
		back.setOnClickListener(this);
	}

	private void initData() {
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		goodData = new ArrayList<Report>();
		// 更据业务ID查询业务里面的数据
		if (ReadReportActivity.allColBg != null
				&& ReadReportActivity.allColBg.getId() != null
				&& !ReadReportActivity.allColBg.getId().equals("")) {
			String allBgId = ReadReportActivity.allColBg.getId();
			List<ColumnEntry> allCol = columnEntry
					.getEntryChildsForParent(allBgId);
			for (ColumnEntry columnEntry : allCol) {
				if (columnEntry.getName().equals("精品推荐")) {
					bgId = columnEntry.getId();
				}
			}
		}
		if (((CeiApplication) getApplication()).isNet()) {
			new Thread() {
				@Override
				public void run() {
					// 所有精彩报告
					if (bgId != null && !bgId.equals("")) {
						String retData = Service.queryReport(bgId, "bg",
								pageSize + "");
						try {
							goodData.clear();
							goodData.addAll(XmlUtil.parseReport(retData));
							if (goodHandler != null) {
								goodHandler.sendEmptyMessage(1);
							}
						} catch (Exception e) {
							MyTools.showPushXml(getApplicationContext());
							e.printStackTrace();
						}
					}
				}

			}.start();
		} else {
			try {
				goodData.addAll(XmlUtil.parseReport(WriteOrRead.read(
						MyTools.nativeData, "goodReport.xml")));
				if (goodHandler != null) {
					goodHandler.sendEmptyMessage(1);
				}
			} catch (Exception e) {
				MyTools.showPushXml(getApplicationContext());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onClick(View v) {
		// if (ReadReportActivity.activitys != null) {
		if (v == homeReport) {
			// 阅读报告页
			Intent intent = new Intent(this, ReadReportActivity.class);
			startActivity(intent);
			// saveActivity(ReadReportActivity.class);

		} else if (v == goodsView) {
			/*
			 * Intent intent = new Intent(this, GoodsReportActivity.class);
			 * startActivity(intent); saveActivity(GoodsReportActivity.class);
			 */
			return;
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
			// ReadReportActivity.activitys.clear();
		} else if (v == back) {
			// 返回键
			/*
			 * if (ReadReportActivity.activitys.size() > 0) {
			 * ReadReportActivity.activitys .remove(ReadReportActivity.activitys
			 * .get(ReadReportActivity.activitys.size() - 1)); Intent intent =
			 * new Intent( this, ReadReportActivity.activitys
			 * .get(ReadReportActivity.activitys.size() - 1));
			 * startActivity(intent); }
			 */
		} else if (v == bookSelf) {
			// 右侧数据图片
			Intent intent = new Intent(this, CeiShelfBookActivity.class);
			startActivity(intent);
			// saveActivity(CeiShelfBookActivity.class);
		}
		// this.finish();
	}

	/*
	 * private void saveActivity(Class<?> clas) { if
	 * (ReadReportActivity.activitys.contains(clas)) {
	 * ReadReportActivity.activitys.remove(clas);
	 * ReadReportActivity.activitys.add(clas); } else {
	 * ReadReportActivity.activitys.add(clas); } }
	 */
	@Override
	protected void onRestart() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
		super.onRestart();
	}
}
