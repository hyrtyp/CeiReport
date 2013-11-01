package com.hyrt.readreport;

import java.util.List;

import com.hyrt.cei.adapter.ReadReportAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.common.LoginActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Report;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.common.HomePageDZB;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ReadReportMF extends ContainerActivity implements OnClickListener {
	private ColumnEntry columnEntry;
	private ListView MFList;
	private ImageView goodImg, paihangImg, fenleiImg, mianfeiImg, homeImg,
			findImg, bookself;
	private List<Report> MFData;
	private StringBuilder colIDs = null;
	private int pageindex = 1;
	private TextView moreText, iconImg,backImg;
	private ReadReportAdapter adapter ;
	private Handler mfHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 12) {
				if(msg.arg2<20){
					moreText.setVisibility(View.GONE);
				}
				if(adapter!=null)
				adapter.notifyDataSetChanged();
			} else {
				adapter = new ReadReportAdapter(
						ReadReportMF.this, MFData, MFList);
				MFList.setAdapter(adapter);
				if(MFData.size()<20)
					moreText.setVisibility(View.GONE);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_report_find);
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		initView();
		imgLight();
		initData();
	}

	private void initView() {

		MFList = (ListView) findViewById(R.id.read_report_data_lv);
		goodImg = (ImageView) findViewById(R.id.read_report_jp);
		goodImg.setOnClickListener(this);
		paihangImg = (ImageView) findViewById(R.id.read_report_ph);
		paihangImg.setOnClickListener(this);
		fenleiImg = (ImageView) findViewById(R.id.read_report_fl);
		fenleiImg.setOnClickListener(this);
		mianfeiImg = (ImageView) findViewById(R.id.read_report_mf);
		mianfeiImg.setOnClickListener(this);
		/*
		 * homeImg = (ImageView) findViewById(R.id.read_report_home);
		 * homeImg.setOnClickListener(this);
		 */
		iconImg = (TextView) findViewById(R.id.read_report_topicon);
		iconImg.setText("免费报告");
		bookself = (ImageView) findViewById(R.id.ib_findbg_bookshelf);
		bookself.setOnClickListener(this);
		findImg = (ImageView) findViewById(R.id.read_report_find);
		findImg.setOnClickListener(this);
		moreText = (TextView) findViewById(R.id.read_report_more);
		moreText.setOnClickListener(this);
		backImg=(TextView) findViewById(R.id.ib_findbg_back);
		backImg.setOnClickListener(this);
	}

	private void initData() {
		if (((CeiApplication) getApplication()).isNet()) {
			new Thread() {

				@Override
				public void run() {
					String sortBg = "";
					ColumnEntry allColBg = columnEntry.getColByName(ReadReportMainActivity.MODEL_NAME);
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
							sortBg = Service.queryAllFreeReport(
									colIDs.toString().substring(0,
											colIDs.toString().length() - 1),
									pageindex + "", "");
						}
					}

					if (!sortBg.equals("")) {
						try {
							MFData = XmlUtil.parseReport(sortBg);
							WriteOrRead.write(sortBg, MyTools.nativeData,
									"findReport.xml");
							if (mfHandler != null) {
								mfHandler.sendEmptyMessage(1);
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
				MFData = XmlUtil.parseReport(WriteOrRead.read(
						MyTools.nativeData, "findReport.xml"));
				if (mfHandler != null) {
					mfHandler.sendEmptyMessage(1);
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
			intent = new Intent(this, ReadReportFL.class);
			startActivity(intent);
			break;
		case R.id.read_report_mf:
			break;
//		case R.id.read_report_home:
//			intent = new Intent(this, HomePageDZB.class);
//			startActivity(intent);
//			break;
		case R.id.ib_findbg_bookshelf:
			intent = new Intent(this, CeiShelfBookActivity.class);
			startActivity(intent);
			break;
		case R.id.read_report_find:
			intent = new Intent(this, ReadReportFind.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.ib_findbg_back:
			intent = new Intent(this, ReadReportMainActivity.class);
			startActivity(intent);
			break;
		case R.id.read_report_more:
			// 加载更多
			if (colIDs != null) {
				try {
					pageindex++;
					String newRetData = Service.queryAllFreeReport(
							colIDs.toString().substring(0,
									colIDs.toString().length() - 1),
							pageindex + "", "");
					// reportData.clear();
					MFData.addAll(XmlUtil.parseReport(newRetData));
					Message msg = new Message();
					msg.arg1 = 12;
					msg.arg2=XmlUtil.parseReport(newRetData).size();
					mfHandler.sendMessage(msg);
					return;
				} catch (Exception e) {
					MyTools.showPushXml(getApplicationContext());
					e.printStackTrace();
				}
			}
			break;
		}
	}

	private void imgLight() {
		goodImg.setBackgroundResource(R.drawable.read_report_jp1);
		paihangImg.setBackgroundResource(R.drawable.read_report_ph1);
		fenleiImg.setBackgroundResource(R.drawable.read_report_fl1);
		mianfeiImg.setBackgroundResource(R.drawable.read_report_mf);
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
