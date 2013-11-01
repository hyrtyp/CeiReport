package com.hyrt.cei.ui.information;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrt.cei.adapter.InfomationGalleryAdapter;
import com.hyrt.cei.adapter.InformationAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.information.adapter.GridViewAdapter;
import com.hyrt.cei.ui.information.view.GGridView;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.vo.funId;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.common.HomePageDZB;

public class InformationActivity extends ContainerActivity implements
		OnClickListener, OnItemClickListener {
	private RelativeLayout rl;
	// 上部Gallery数据的集合
	List<InfoNew> GalleryDate = new ArrayList<InfoNew>();
	// 当前查询的业务资讯id
	private String currentFunctionId;
	// 横向的菜单
	private GGridView gGridView;
	private ListView list;
	private Intent intent;
	private boolean alreadyBuy, canRead;
	private List<funId> funIds;
	private TextView title;
	private ColumnEntry columnEntry;
	private List<ColumnEntry> columnEntries;
	public static  String MODEL_NAME;
	private static final int LISTVIEW = 1;
	private static final int GALLERY = 2;
	private static final int ALREADY_BUY = 3;
	private ImageView point1, point2, point3, point4, point5;
	private String jingcaiStrId;
	private String intentId;
	private Gallery goodReport;
	private String loginName;
	// 资讯列表数据集合
	private List<InfoNew> news = new ArrayList<InfoNew>();
	private Handler dataHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.arg1) {
			case LISTVIEW:
				InformationAdapter adapter = new InformationAdapter(
						InformationActivity.this, R.layout.inf_list_item, news,
						currentFunctionId);
				list.setAdapter(adapter);
				break;
			}
		}
	};

	private Handler viewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			final InfomationGalleryAdapter adapter = new InfomationGalleryAdapter(
					InformationActivity.this, GalleryDate, goodReport);
			goodReport.setAdapter(adapter);
			goodReport.setSelection(Integer.MAX_VALUE/2-3);
			point1.setBackgroundResource(R.drawable.read_report_index_select);
			if (GalleryDate.get(0).getTitle() != null) {
				System.out.println("informa");
				title.setText(GalleryDate.get(0).getTitle().length() > 10 ? GalleryDate
						.get(0).getTitle().substring(0, 9)
						+ "..."
						: GalleryDate.get(0).getTitle());
			}
			goodReport.setOnItemClickListener(InformationActivity.this);
			goodReport.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					InfoNew info = (InfoNew) arg0.getAdapter().getItem(arg2);
					if (info.getTitle() != null) {
						title.setText(info.getTitle().length() > 10 ? info
								.getTitle().substring(0, 9) + "..." : info
								.getTitle());

					} else {
						title.setText("");
					}
					if (arg2 % 5 == 0) {
						point1.setBackgroundResource(R.drawable.read_report_index_select);
						point2.setBackgroundResource(R.drawable.home_img_ratio);
						point3.setBackgroundResource(R.drawable.home_img_ratio);
						point4.setBackgroundResource(R.drawable.home_img_ratio);
						point5.setBackgroundResource(R.drawable.home_img_ratio);
					}
					if (arg2 % 5 == 1) {
						point2.setBackgroundResource(R.drawable.read_report_index_select);
						point1.setBackgroundResource(R.drawable.home_img_ratio);
						point3.setBackgroundResource(R.drawable.home_img_ratio);
						point4.setBackgroundResource(R.drawable.home_img_ratio);
						point5.setBackgroundResource(R.drawable.home_img_ratio);
					}
					if (arg2 % 5 == 2) {
						point3.setBackgroundResource(R.drawable.read_report_index_select);
						point1.setBackgroundResource(R.drawable.home_img_ratio);
						point2.setBackgroundResource(R.drawable.home_img_ratio);
						point4.setBackgroundResource(R.drawable.home_img_ratio);
						point5.setBackgroundResource(R.drawable.home_img_ratio);
					}
					if (arg2 % 5 == 3) {
						point4.setBackgroundResource(R.drawable.read_report_index_select);
						point1.setBackgroundResource(R.drawable.home_img_ratio);
						point3.setBackgroundResource(R.drawable.home_img_ratio);
						point2.setBackgroundResource(R.drawable.home_img_ratio);
						point5.setBackgroundResource(R.drawable.home_img_ratio);
					}
					if (arg2 % 5 == 4) {
						point5.setBackgroundResource(R.drawable.read_report_index_select);
						point1.setBackgroundResource(R.drawable.home_img_ratio);
						point3.setBackgroundResource(R.drawable.home_img_ratio);
						point4.setBackgroundResource(R.drawable.home_img_ratio);
						point2.setBackgroundResource(R.drawable.home_img_ratio);
					}
					System.out.println("长度" + GalleryDate.size());
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infomation_main);
		MODEL_NAME=((CeiApplication)getApplication()).nowStart;//获取当前业务名称。
		((TextView)findViewById(R.id.zjzx_title)).setText(MODEL_NAME);
		init();
		initData();
	}

	private void init() {
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		alreadyBuy = false;
		findViewById(R.id.imageView1).setOnClickListener(this);
		findViewById(R.id.zjzx_search).setOnClickListener(this);
		goodReport = (Gallery) findViewById(R.id.read_report_main_ga);
		title = (TextView) findViewById(R.id.read_report_title);
		point1 = (ImageView) findViewById(R.id.read_report_point1);
		point2 = (ImageView) findViewById(R.id.read_report_point2);
		point3 = (ImageView) findViewById(R.id.read_report_point3);
		point4 = (ImageView) findViewById(R.id.read_report_point4);
		point5 = (ImageView) findViewById(R.id.read_report_point5);
		gGridView = (GGridView) findViewById(R.id.zjzx_info_gridview);
		gGridView.setOnItemClickListener(this);
		list = (ListView) findViewById(R.id.read_report_main_lv);
		list.setOnItemClickListener(InformationActivity.this);
		findViewById(R.id.zjzx_botton_1).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_2).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_3).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_4).setOnClickListener(this);
	};

	private void initData() {
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		columnEntries = columnEntry.getEntryChildsForParent(columnEntry
				.getColByName(InformationActivity.MODEL_NAME).getId());
		if(columnEntries.size()==0) return;
		GridViewAdapter gridViewAdapter = new GridViewAdapter(this,
				columnEntries);
		gGridView.setAdapter(gridViewAdapter);
		for (int i = 0; i < columnEntries.size(); i++) {
			if (columnEntries.get(i).getName().endsWith("精彩推荐")) {
				jingcaiStrId = columnEntries.get(i).getId();
			}
		}
		if (((CeiApplication) InformationActivity.this.getApplication())
				.isNet()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < columnEntries.size(); i++) {
						String result = Service.queryNewsByFunctionId(
								columnEntries.get(i).getId(), "40",
								columnEntry.getUserId());
						String xmlName = "zjzx" + columnEntries.get(i).getId()
								+ ".xml";
						WriteOrRead.write(result, MyTools.nativeData, xmlName);
					}
					String gallaryresult = Service.queryNewsByFunctionId(
							jingcaiStrId, "5", columnEntry.getUserId());
					WriteOrRead.write(gallaryresult, MyTools.nativeData,
							"zjzxgallaryXmlName.xml");
					String yigoumai = Service.queryBuyNews(columnEntry
							.getUserId());
					WriteOrRead.write(yigoumai, MyTools.nativeData,
							"yigoumaiXmlName.xml");

				}
			}).start();
		}
		String firstID = columnEntries.get(0).getId();
		refreshListData(columnEntry.getUserId(), ALREADY_BUY,true);
		refreshListData(jingcaiStrId, GALLERY,true);
		refreshListData(firstID, LISTVIEW,true);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.zjzx_info_gridview:
			String currentFunctionId = columnEntries.get(arg2).getId();
			refreshListData(currentFunctionId, LISTVIEW);
			for (int i = 0; i < arg0.getChildCount(); i++) {
				rl = (RelativeLayout) arg0.getChildAt(i);
				if (i == arg2) {
					((ImageView) rl.getChildAt(0))
							.setImageResource(R.drawable.phone_study_menu_select);
					((TextView) rl.getChildAt(1)).setTextColor(Color.WHITE);
				} else {
					((ImageView) rl.getChildAt(0)).setImageDrawable(null);
					((TextView) rl.getChildAt(1)).setTextColor(Color.BLUE);
				}
			}
			break;
		case R.id.read_report_main_lv:
			InfoNew new2 = news.get(arg2);
			intentId = new2.getId();
			// 跳转到详细页
			intent = new Intent();
			intent.putExtra("extra", intentId);
			intent.putExtra("functionId", new2.getFunctionId());
			intent.setClass(InformationActivity.this,
					InformationReadActivity.class);
			if (new2.getIsfree().equals("1")) {
				InformationActivity.this.startActivity(intent);
			} else {
				MyTools.exitShow(InformationActivity.this, getWindow()
						.getDecorView(), "未购买该栏目！");
			}
			break;
		case R.id.read_report_main_ga:
			if (GalleryDate.size() >= 5) {
				InfoNew newa = GalleryDate.get(arg2 % 5);
				String intentId = newa.getId();
				if (intentId != null) {
					intent = new Intent();
					intent.putExtra("extra", intentId);
					intent.putExtra("functionId", newa.getFunctionId());
					intent.setClass(InformationActivity.this,
							InformationReadActivity.class);
					if (newa.getIsfree().endsWith("1")) {
						InformationActivity.this.startActivity(intent);
					} else {
						for (int i = 0; i < funIds.size(); i++) {
							if (funIds.get(i).getFunid()
									.endsWith(newa.getFunctionId())) {
								canRead = true;
								break;
							} else {
								canRead = false;
							}
						}
						if (canRead) {
							InformationActivity.this.startActivity(intent);
						} else {
							MyTools.exitShow(InformationActivity.this,
									getWindow().getDecorView(), "未购买该栏目！");
						}
					}
				}
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView1:
			InformationActivity.this.finish();
			intent = new Intent(InformationActivity.this, HomePageDZB.class);
			startActivity(intent);
			break;
		case R.id.zjzx_search:
			intent = new Intent(InformationActivity.this,
					InfoSearchActivity.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_1:
			intent = new Intent(InformationActivity.this, InformationOne.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_2:
			intent = new Intent(InformationActivity.this, InformationTwo.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_3:
			intent = new Intent(InformationActivity.this,
					InformationThree.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_4:
			if (!loginName.equals("")) {
				intent = new Intent(InformationActivity.this,
						InformationCollect.class);
				startActivity(intent);
			} else {
				Toast.makeText(this, "请登录后查看！", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void refreshListData(final String functionId, final int operationId) {
		news.clear();
		new Handler().post((new Runnable() {
			@Override
			public void run() {
				String result = "";
				switch (operationId) {
				case LISTVIEW:
					if (((CeiApplication) InformationActivity.this
							.getApplication()).isNet()) {
						result = Service.queryNewsByFunctionId(functionId,
								"40", columnEntry.getUserId());
						news.clear();
						XmlUtil.getNewsList(result, news);
					} else {
						String xmlName = "zjzx" + functionId + ".xml";
						String xmlNameresult = WriteOrRead.read(
								MyTools.nativeData, xmlName);
						news.clear();
						XmlUtil.getNewsList(xmlNameresult, news);
					}
					if (!funIds.isEmpty()) {
						for (int i = 0; i < funIds.size(); i++) {
							if (funIds.get(i).getFunid().endsWith(functionId)) {
								alreadyBuy = true;
								break;
							} else {
								alreadyBuy = false;
							}
						}
					}
					if (alreadyBuy) {
						for (int j = 0; j < news.size(); j++) {
							news.get(j).setIsfree("1");
						}
					}
					Message message = dataHandler.obtainMessage();
					message.arg1 = LISTVIEW;
					dataHandler.sendMessage(message);
					break;
				case GALLERY:
					if (((CeiApplication) InformationActivity.this
							.getApplication()).isNet()) {
						String leftTopResult = Service.queryNewsByFunctionId(
								jingcaiStrId, "5", columnEntry.getUserId());
						XmlUtil.getNewsList(leftTopResult, GalleryDate);
						if (GalleryDate.size() < 5) {
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
						}
					} else {
						String galNameresult = WriteOrRead.read(
								MyTools.nativeData, "zjzxgallaryXmlName.xml");
						news.clear();
						XmlUtil.getNewsList(galNameresult, GalleryDate);
						if (GalleryDate.size() < 5) {
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
						}
					}
					message = viewHandler.obtainMessage();
					message.arg1 = GALLERY;
					viewHandler.sendMessage(message);
					break;
				case ALREADY_BUY:
					if (((CeiApplication) InformationActivity.this
							.getApplication()).isNet()) {
						funIds = new ArrayList<funId>();
						result = Service.queryBuyNews(functionId);
						try {
							funIds = XmlUtil.queryBuyNews(result);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						String galNameresult = WriteOrRead.read(
								MyTools.nativeData, "yigoumaiXmlName.xml");
						news.clear();
						try {
							funIds = XmlUtil.queryBuyNews(galNameresult);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
		}));
	}
	
	private void refreshListData(final String functionId, final int operationId,boolean isThread) {
		news.clear();
		new Thread((new Runnable() {
			@Override
			public void run() {
				String result = "";
				switch (operationId) {
				case LISTVIEW:
					if (((CeiApplication) InformationActivity.this
							.getApplication()).isNet()) {
						result = Service.queryNewsByFunctionId(functionId, "40",
								columnEntry.getUserId());
						news.clear();
						XmlUtil.getNewsList(result, news);
					} else {
						String xmlName = "zjzx" + functionId + ".xml";
						String xmlNameresult = WriteOrRead.read(
								MyTools.nativeData, xmlName);
						news.clear();
						XmlUtil.getNewsList(xmlNameresult, news);
					}
					if(!funIds.isEmpty()){
						for (int i = 0; i < funIds.size(); i++) {
							if (funIds.get(i).getFunid().endsWith(functionId)) {
								alreadyBuy = true;
								break;
							} else {
								alreadyBuy = false;
							}
						}
					}
					if (alreadyBuy) {
						for (int j = 0; j < news.size(); j++) {
							news.get(j).setIsfree("1");
						}
					}
					Message message = dataHandler.obtainMessage();
					message.arg1 = LISTVIEW;
					dataHandler.sendMessage(message);
					break;
				case GALLERY:
					if (((CeiApplication) InformationActivity.this
							.getApplication()).isNet()) {
						String leftTopResult = Service.queryNewsByFunctionId(
								jingcaiStrId, "5", columnEntry.getUserId());
						XmlUtil.getNewsList(leftTopResult, GalleryDate);
						if (GalleryDate.size() < 5) {
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
						}
					} else {
						String galNameresult = WriteOrRead.read(
								MyTools.nativeData, "zjzxgallaryXmlName.xml");
						news.clear();
						XmlUtil.getNewsList(galNameresult, GalleryDate);
						if (GalleryDate.size() < 5) {
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
							GalleryDate.add(new InfoNew());
						}
					}
					message = viewHandler.obtainMessage();
					message.arg1 = GALLERY;
					viewHandler.sendMessage(message);
					break;
				case ALREADY_BUY:
					if (((CeiApplication) InformationActivity.this
							.getApplication()).isNet()) {
						funIds = new ArrayList<funId>();
						result = Service.queryBuyNews(functionId);
						try {
							funIds = XmlUtil.queryBuyNews(result);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						String galNameresult = WriteOrRead.read(
								MyTools.nativeData, "yigoumaiXmlName.xml");
						news.clear();
						try {
							funIds = XmlUtil.queryBuyNews(galNameresult);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
		})).start();
	}
}
