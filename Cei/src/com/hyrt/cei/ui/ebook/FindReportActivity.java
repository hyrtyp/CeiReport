package com.hyrt.cei.ui.ebook;

import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.GoodReportAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.ui.ebook.view.MyListView;
import com.hyrt.cei.ui.ebook.view.MyListView.OnRefreshListener;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Report;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FindReportActivity extends Activity implements OnClickListener {
	private ImageView homePage, homeReport, goodsView, sortView, partView,
			mianfeiReport, reportFind;
	private EditText reportName;
	private ImageButton back, bookSelf;
	private ListView findList;
	private ColumnEntry columnEntry;
	private List<Report> reportData = new ArrayList<Report>();
	private StringBuilder colIDs = null;
	private GoodReportAdapter adapter;
	private int pageSize = 1;
	private LinearLayout loadProgressBar;
	private TextView moreTextView;
	private LinearLayout morelayout;
	private LinearLayout prolayout;
	List<Report> addReport;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 2) {
				reportData.addAll(addReport);
				if (adapter != null)
					adapter.notifyDataSetChanged();
				if (msg.arg1 < 20) {
					moreTextView.setVisibility(View.GONE);
				} else {
					moreTextView.setVisibility(View.VISIBLE);
				}
				loadProgressBar.setVisibility(View.GONE);
			} else if (msg.arg1 == 5) {
				if (reportData.size() == 0)
					MyTools.exitShow(FindReportActivity.this,
							FindReportActivity.this.getWindow().getDecorView(),
							"没有查到您需要的信息!");
				if (reportData.size() >= 20
						&& findList.getFooterViewsCount() == 0) {
					addPageMore();
				} else {
					if(moreTextView != null)
					moreTextView.setVisibility(View.VISIBLE);
				}
				adapter = new GoodReportAdapter(FindReportActivity.this,
						reportData, findList);
				findList.setAdapter(adapter);
				reportFind.setEnabled(true);
			} else {
				loadProgressBar.setVisibility(View.GONE);
			}
			prolayout.setVisibility(View.GONE);
		}

	};

	private void addPageMore() {
		morelayout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.list_page_load, null);
		moreTextView = (TextView) morelayout.findViewById(R.id.more_id);
		loadProgressBar = (LinearLayout) morelayout.findViewById(R.id.load_id);
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
							pageSize++;
							String addBg = dataChange();
							addReport = XmlUtil.parseReport(addBg);
							// reportData.clear();
							if (addReport.size() == 0) {
								handler.sendEmptyMessage(1);
								return;
							}
							Message msg = new Message();
							msg.what = 2;
							msg.arg1 = addReport.size();
							handler.sendMessage(msg);

						} catch (Exception e) {
							MyTools.showPushXml(getApplicationContext());
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
		findList.addFooterView(morelayout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yjbg_findreport);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		initView();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		prolayout.setVisibility(View.GONE);
	}

	private void initData() {
		prolayout.setVisibility(View.VISIBLE);
		if (((CeiApplication) getApplication()).isNet()) {
			new Thread() {

				@Override
				public void run() {

					if (!dataChange().equals("")) {
						String sortBg = dataChange();
						try {
							reportData = XmlUtil.parseReport(sortBg);
							WriteOrRead.write(sortBg, MyTools.nativeData,
									"findReport.xml");
							if (handler != null) {
								Message msg = new Message();
								msg.arg1 = 5;
								msg.arg2 = XmlUtil.parseReport(sortBg).size();
								handler.sendMessage(msg);
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
				reportData = XmlUtil.parseReport(WriteOrRead.read(
						MyTools.nativeData, "findReport.xml"));
				if (handler != null) {
					Message msg = new Message();
					msg.arg1 = 5;
					msg.arg2 = reportData.size();
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				MyTools.showPushXml(getApplicationContext());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private String dataChange() {
		/*
		 * if(reportName.getText().toString().equals("")){ return ""; }
		 */
		ColumnEntry allColBg = columnEntry.getColByName(((CeiApplication) getApplication()).nowStart);
		if (allColBg != null && allColBg.getId() != null
				&& !allColBg.getId().equals("")) {
			String allBgId = allColBg.getId();
			colIDs = new StringBuilder();
			List<ColumnEntry> allCol = columnEntry
					.getEntryChildsForParent(allBgId);
			for (ColumnEntry columnEntry : allCol) {
				colIDs.append(columnEntry.getId() + ",");
			}
			if (colIDs != null && !colIDs.equals("")) {
				SharedPreferences settings = getSharedPreferences(
						"search_result", Activity.MODE_PRIVATE);
				Editor editor = settings.edit();
				editor.putString("history_report", reportName.getText()
						.toString());
				editor.commit();
				String sortBg = Service.queryReportByName(colIDs.toString()
						.substring(0, colIDs.toString().length() - 1), pageSize
						+ "", reportName.getText().toString());
				return sortBg;
			}
		}
		return "";
	}

	private void initView() {
		TextView title=(TextView)(findViewById(R.id.top).findViewById(R.id.title));
		title.setText("报告搜索");
		prolayout = (LinearLayout) findViewById(R.id.report_find_pro);
		findList = (ListView) findViewById(R.id.lv_findbg);
		reportFind = (ImageView) findViewById(R.id.yjbg_report_find_iv);
		reportFind.setOnClickListener(this);
		reportName = (EditText) findViewById(R.id.yjbg_report_find_et);
		SharedPreferences settings = getSharedPreferences("search_result",
				Activity.MODE_PRIVATE);
		String historyStr = settings.getString("history_report", "");
		reportName.setText(historyStr);
		bookSelf = (ImageButton) (findViewById(R.id.top).findViewById(R.id.ib_bg_bookshelf));
		bookSelf.setOnClickListener(this);
		back = (ImageButton) (findViewById(R.id.top).findViewById(R.id.ib_bg_back));
		back.setOnClickListener(this);
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
			Intent intent = new Intent(this, PartitionReportActivity.class);
			startActivity(intent);
			// saveActivity(PartitionReportActivity.class);
		} else if (v == mianfeiReport) {
			// 免费报告
			Intent intent = new Intent(this, MianfeiReportActivity.class);
			startActivity(intent);
			// saveActivity(MianfeiReportActivity.class);
		} else if (v == homePage) {
			// 首页
			Intent intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
			// ReadReportActivity.activitys.clear();
		} else if (v == back) {
			// 返回键
			this.finish();
		} else if (v == bookSelf) {
			// 右侧数据图片
			Intent intent = new Intent(this, CeiShelfBookActivity.class);
			startActivity(intent);
		} else if (v == reportFind) {
			// 查找
			if (reportName.getText() == null
					|| reportName.getText().toString().trim().equals(""))
				return;
			pageSize = 1;
			reportData.clear();
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(reportName.getWindowToken(), 0);
			initData();
			reportFind.setEnabled(false);
		}
	}

	@Override
	protected void onRestart() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
		super.onRestart();
	}

}
