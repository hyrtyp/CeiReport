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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class InformationTwo extends ContainerActivity implements OnClickListener,
		OnItemClickListener {
	private String loginName;
	private RelativeLayout rl;
	public static  String MODEL_NAME;
	// 上部Gallery数据的集合
	List<InfoNew> GalleryDate = new ArrayList<InfoNew>();
	// 当前查询的业务资讯id
	private String currentFunctionId;
	// 横向的菜单
	private GGridView gGridView;
	private ListView list;
	private Intent intent;
	private boolean alreadyBuy;
	private List<funId> funIds;
	private ColumnEntry columnEntry;
	private List<ColumnEntry> columnEntries;
	private List<ColumnEntry> columnEntriesnew;
	//public static final String MODEL_NAME = "政经资讯";
	private static final int LISTVIEW = 1;
	private static final int ALREADY_BUY = 3;
	private String intentId;
	// 资讯列表数据集合
	private List<InfoNew> news = new ArrayList<InfoNew>();
	private Handler dataHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.arg1) {
			case LISTVIEW:
				InformationAdapter adapter = new InformationAdapter(
						InformationTwo.this, R.layout.inf_list_item, news,
						currentFunctionId);
				list.setAdapter(adapter);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infomation_two);
		MODEL_NAME=((CeiApplication)getApplication()).nowStart;//获取当前业务名称。
		((TextView)findViewById(R.id.imageView1)).setText(MODEL_NAME);
		init();
		initData();
	}

	private void init() {
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		findViewById(R.id.imageView1).setOnClickListener(this);
		findViewById(R.id.zjzx_search).setOnClickListener(this);
		alreadyBuy = false;
		gGridView = (GGridView) findViewById(R.id.zjzx_info_gridview);
		gGridView.setOnItemClickListener(this);
		list = (ListView) findViewById(R.id.read_report_main_lv);
		list.setOnItemClickListener(InformationTwo.this);
		findViewById(R.id.zjzx_botton_1).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_2).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_3).setOnClickListener(this);
		findViewById(R.id.zjzx_botton_4).setOnClickListener(this);
	};

	private void initData() {
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		columnEntries = columnEntry.getEntryChildsForParent(columnEntry
				.getColByName(InformationActivity.MODEL_NAME).getId());
		columnEntriesnew = new ArrayList<ColumnEntry>();
		if(columnEntries.size()>=4){
			columnEntriesnew.add(columnEntries.get(0));
			columnEntriesnew.add(columnEntries.get(1));
			columnEntriesnew.add(columnEntries.get(2));
			columnEntriesnew.add(columnEntries.get(3));
		}else{
			for (ColumnEntry columnEntry : columnEntries) {
				columnEntriesnew.add(columnEntry);
			}
		}
		
		GridViewAdapter gridViewAdapter = new GridViewAdapter(this,
				columnEntriesnew);
		gGridView.setAdapter(gridViewAdapter);
		String firstID = columnEntriesnew.get(0).getId();
		refreshListData(columnEntry.getUserId(), ALREADY_BUY);
		refreshListData(firstID, LISTVIEW);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.zjzx_info_gridview:
			String currentFunctionId = columnEntriesnew.get(arg2).getId();
			refreshListData(currentFunctionId, LISTVIEW);
			for (int i = 0; i < arg0.getChildCount(); i++) {
				rl = (RelativeLayout) arg0.getChildAt(i);
				if (i == arg2) {
					((ImageView) rl.getChildAt(0)).setImageResource(R.drawable.phone_study_menu_select);
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
			intent.setClass(InformationTwo.this, InformationReadActivity.class);
			if (new2.getIsfree().equals("1")) {
				InformationTwo.this.startActivity(intent);
			} else {
				MyTools.exitShow(InformationTwo.this, getWindow()
						.getDecorView(), "未购买该栏目！");
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView1:
			InformationTwo.this.finish();
			intent = new Intent(InformationTwo.this, InformationActivity.class);
			startActivity(intent);
			break;
		case R.id.zjzx_search:
			intent = new Intent(InformationTwo.this, InfoSearchActivity.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_1:
			intent = new Intent(InformationTwo.this, InformationOne.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_2:
			break;
		case R.id.zjzx_botton_3:
			intent = new Intent(InformationTwo.this, InformationThree.class);
			startActivity(intent);
			break;
		case R.id.zjzx_botton_4:
			if (!loginName.equals("")) {
				intent = new Intent(InformationTwo.this,
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
		new Thread((new Runnable() {
			@Override
			public void run() {
				String result = "";
				switch (operationId) {
				case LISTVIEW:
					if (((CeiApplication) InformationTwo.this.getApplication())
							.isNet()) {
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
					for (int i = 0; i < funIds.size(); i++) {
						if (funIds.get(i).getFunid().endsWith(functionId)) {
							alreadyBuy = true;
							break;
						} else {
							alreadyBuy = false;
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
				case ALREADY_BUY:
					if (((CeiApplication) InformationTwo.this.getApplication())
							.isNet()) {
						funIds = new ArrayList<funId>();
						result = Service.queryBuyNews(functionId);
						try {
							funIds = XmlUtil.queryBuyNews(result);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						String galNameresult = WriteOrRead.read(
								MyTools.nativeData, "zjzxalreadyBuy.xml");
						news.clear();
						try {
							funIds = XmlUtil.queryBuyNews(galNameresult);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
			}
		})).start();
	}
}
