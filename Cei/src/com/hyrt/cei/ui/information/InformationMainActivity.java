package com.hyrt.cei.ui.information;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.CommonPopAdapter;
import com.hyrt.cei.adapter.InfAdapter;
import com.hyrt.cei.adapter.InfomationImageAdapter;
import com.hyrt.cei.adapter.WeatherPopAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.TimeOutHelper;
import com.hyrt.cei.util.WeatherUtil;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.vo.WeatherInfo;
import com.hyrt.cei.webservice.service.Service;

public class InformationMainActivity extends Activity implements
		OnClickListener, OnItemClickListener {
	private TimeOutHelper timeOutHelper;
	private String loginName;
	private boolean isGoUnline;
	private ImageView imgpoint0, imgpoint1, imgpoint2, imgpoint3, imgpoint4;
	private Button top1, top2, top3, top4, botton, botton1, botton2, botton3,
			botton4, botton5, botton6, moreBurron;
	private String strId;
	private TextView infoTitle;
	private LinearLayout prolayout;
	public static  String MODEL_NAME;
	private static final int LEFTTOP_READY = 1;
	private static final int LEFTBOTTOM_READY = 2;
	private static final int RIGHTTOP_READY = 3;
	private static final int RIGHTBOTTOM_READY = 4;
	private static final int SHOUCANG = 7;
	private static final int LEFTBOTTON_FIRST = 5;
	private static final int ALREADY_BUY = 6;
	private GridView gridview;
	private PopupWindow popWin;
	private List<String> nameList;
	private String intentId, jingcaiStrId;
	private Intent intent;
	private List<funId> funIds;
	private boolean alreadyBuy;
	private boolean isFirst;
	private boolean canRead;
	private List<ColumnEntry> columnEntries;
	private Gallery goodData;
	private ImageView[] imageViews;
	private InfomationImageAdapter goodAdapter;
	private boolean flage;
	// 弹出框信息列表
	private List<InfoNew> infoNews = new ArrayList<InfoNew>();
	private SharedPreferences settings;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MODEL_NAME=((CeiApplication)getApplication()).nowStart;//获取当前业务名称。
		setContentView(R.layout.informationmain);
		timeOutHelper = new TimeOutHelper(this);
		findViewById(R.id.econ_data_pro).setOnTouchListener(
				new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						return true;
					}
				});
		settings = getSharedPreferences("cityCode", Activity.MODE_PRIVATE);
		final String cityCode = settings.getString("cityCode", "");
		TextView tv = (TextView) findViewById(R.id.imageView1);
		tv.setText(MODEL_NAME);
		try {
			// 初始化公用的数据
			initCommonData();
			// 初始化其他的部分的数据
			initOtherData();
			// 初始化左上部的数据
			initLeftTopData();
			prolayout.setVisibility(View.GONE);
			if (!cityCode.equals("")) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						weatherInfo = XmlUtil.parseWeatherInfo(WeatherUtil
								.getWeather(cityCode));
						dataHandler.post(new Runnable() {

							@Override
							public void run() {
								try {
									ContentResolver cv = InformationMainActivity.this
											.getContentResolver();
									String strTimeFormat = android.provider.Settings.System
											.getString(
													cv,
													android.provider.Settings.System.DATE_FORMAT);
									SimpleDateFormat bartDateFormat = null;
									try {
										bartDateFormat = new SimpleDateFormat(
												strTimeFormat);
									} catch (Exception e) {
										bartDateFormat = new SimpleDateFormat(
												"yyyy-MM-dd   HH:mm:ss");
									}
									Date date = new Date(weatherInfo.getTime());
									((TextView) findViewById(R.id.weather_tv))
											.setText(weatherInfo.getProvince() + "  "
													+ weatherInfo.getCity() + "     "
													+ bartDateFormat.format(date)
													+ "\n" + "    "
													+ weatherInfo.getScene());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});

					}
				}).start();
				
			}
		} catch (Exception e) {
		}
	}

	private void initCommonData() {
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		prolayout = (LinearLayout) findViewById(R.id.econ_data_pro);
		prolayout.setVisibility(View.VISIBLE);
		infoTitle = (TextView) findViewById(R.id.good_report_title);
		goodData = (Gallery) findViewById(R.id.econ_data_top);
		imgpoint0 = (ImageView) findViewById(R.id.econ_data_img_point0);
		imgpoint1 = (ImageView) findViewById(R.id.econ_data_img_point1);
		imgpoint2 = (ImageView) findViewById(R.id.econ_data_img_point2);
		imgpoint3 = (ImageView) findViewById(R.id.econ_data_img_point3);
		imgpoint4 = (ImageView) findViewById(R.id.econ_data_img_point4);
		imageViews = new ImageView[] { imgpoint0, imgpoint1, imgpoint2,
				imgpoint3, imgpoint4 };
		alreadyBuy = false;
		isFirst = true;
		canRead = false;
		isGoUnline = true;
		flage = true;
		jingcaiStrId = "";
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		refreshListData(columnEntry.getUserId(), ALREADY_BUY);
		columnEntries = columnEntry.getEntryChildsForParent(columnEntry
				.getColByName(InformationMainActivity.MODEL_NAME).getId());
		asyncImageLoader = ((CeiApplication) (this.getApplication())).asyncImageLoader;
		list = columnEntry.getEntryChildsForParent(columnEntry.getColByName(
				MODEL_NAME).getId());
		strId = "";
		nameList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if (i + 1 == list.size()) {
				if (list.get(i).getName().endsWith("精彩推荐")) {
					jingcaiStrId = list.get(i).getId();
				}
				nameList.add(list.get(i).getName());
				strId += list.get(i).getId();
			} else {
				if (list.get(i).getName().endsWith("精彩推荐")) {
					jingcaiStrId = list.get(i).getId();
				}
				nameList.add(list.get(i).getName());
				strId += list.get(i).getId() + ",";
			}
		}
	}

	// 经济资讯的子集业务集合
	private List<ColumnEntry> list;
	// 所有业务数据
	private ColumnEntry columnEntry;
	// 左上部数据的集合
	List<InfoNew> leftTopNews = new ArrayList<InfoNew>();
	// 左下部数据的集合
	List<InfoNew> leftBottomNews = new ArrayList<InfoNew>();
	// 右上部数据的集合
	List<InfoNew> rightTopNews = new ArrayList<InfoNew>();
	// 右下部数据的集合
	List<InfoNew> rightBottomNews = new ArrayList<InfoNew>();
	// 加载图片的类
	private AsyncImageLoader asyncImageLoader;
	// 上下业务按钮的集合
	private List<Button> buttons = new ArrayList<Button>();
	// 左下部的listview
	private ListView leftListview;
	// 右上部的listview
	private ListView rightListview;
	// 右下部的父级元素
	private LinearLayout rightBottomParent;
	protected CommonPopAdapter adapter1;

	private void initLeftTopData() {
		new Thread() {

			@Override
			public void run() {
				try {
					if (((CeiApplication) InformationMainActivity.this
							.getApplication()).isNet()) {
						// 有图片的
						String leftTopResult = Service.queryNewsByFunctionId(
								jingcaiStrId, "40", columnEntry.getUserId());
						XmlUtil.getNewsList(leftTopResult, leftTopNews);
						if (leftTopNews.size() < 5) {
							leftTopNews.add(new InfoNew());
							leftTopNews.add(new InfoNew());
							leftTopNews.add(new InfoNew());
							leftTopNews.add(new InfoNew());
							leftTopNews.add(new InfoNew());
						}
						WriteOrRead.write(leftTopResult, MyTools.nativeData,
								"leftTopResult.xml");
					} else {
						String leftTopResult = WriteOrRead.read(
								MyTools.nativeData, "leftTopResult.xml");
						if (leftTopResult != null && !leftTopResult.equals("")) {
							XmlUtil.getNewsList(leftTopResult, leftTopNews);
						}
						if (leftTopNews.size() == 0) {
							leftTopNews.add(new InfoNew());
							leftTopNews.add(new InfoNew());
							leftTopNews.add(new InfoNew());
							leftTopNews.add(new InfoNew());
							leftTopNews.add(new InfoNew());
						}
					}
					Message message = dataHandler.obtainMessage();
					message.arg1 = LEFTTOP_READY;
					dataHandler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.run();
			}
		}.start();
	}

	public void initOtherData() {
		findViewById(R.id.home).setOnClickListener(this);
		findViewById(R.id.zjzx_shoucang).setOnClickListener(this);
		findViewById(R.id.zjzx_shuaxin).setOnClickListener(this);
		findViewById(R.id.zjzx_sousuo).setOnClickListener(this);
		leftListview = (ListView) findViewById(R.id.info_left_botton_listview);
		rightListview = (ListView) findViewById(R.id.info_right_top_list);
		leftListview.setOnItemClickListener(this);
		rightListview.setOnItemClickListener(this);
		findViewById(R.id.weather_Ll).setOnClickListener(this);
		rightBottomParent = (LinearLayout) findViewById(R.id.right_bottom_parent);

		botton = (Button) findViewById(R.id.info_left_botton_button1);
		botton1 = (Button) findViewById(R.id.info_left_botton_button2);
		botton1.setVisibility(View.GONE);
		if (list.size() > 0) {
			buttons.add(botton1);
			botton1.setText(list.get(0).getName());
			botton1.setVisibility(View.VISIBLE);
		}
		botton2 = (Button) findViewById(R.id.info_left_botton_button3);
		botton2.setVisibility(View.GONE);
		if (list.size() > 1) {
			botton2.setText(list.get(1).getName());
			buttons.add(botton2);
		}
		botton3 = (Button) findViewById(R.id.info_left_botton_button4);
		botton3.setVisibility(View.GONE);
		if (list.size() > 2) {
			botton3.setText(list.get(2).getName());
			buttons.add(botton3);
		}
		botton4 = (Button) findViewById(R.id.info_left_botton_button5);
		botton4.setVisibility(View.GONE);
		if (list.size() > 3) {
			botton4.setText(list.get(3).getName());
			buttons.add(botton4);
		}
		top1 = (Button) findViewById(R.id.info_right_top_botton1);
		top1.setVisibility(View.GONE);
		if (list.size() > 4) {
			top1.setText(list.get(4).getName());
			buttons.add(top1);
		}
		top2 = (Button) findViewById(R.id.info_right_top_botton2);
		top2.setVisibility(View.GONE);
		if (list.size() > 5) {
			top2.setText(list.get(5).getName());
			buttons.add(top2);
		}
		top3 = (Button) findViewById(R.id.info_right_top_botton3);
		top3.setVisibility(View.GONE);
		if (list.size() > 6) {
			top3.setText(list.get(6).getName());
			buttons.add(top3);
		}
		top4 = (Button) findViewById(R.id.info_right_top_botton4);
		top4.setVisibility(View.GONE);
		if (list.size() > 7) {
			top4.setText(list.get(7).getName());
			buttons.add(top4);
		}
		botton5 = (Button) findViewById(R.id.info_right_botton_button1);
		botton5.setVisibility(View.GONE);
		if (list.size() > 8) {
			botton5.setText(list.get(8).getName());
			buttons.add(botton5);
		}
		botton6 = (Button) findViewById(R.id.info_right_botton_button2);
		botton6.setVisibility(View.GONE);
		if (list.size() > 9) {
			buttons.add(botton6);
			botton6.setText(list.get(9).getName());
		}
		// 显示菜单按钮
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setVisibility(View.VISIBLE);
		}
		// 为业务按钮注册事件
		botton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prolayout.setVisibility(View.VISIBLE);
				moreBurron
						.setBackgroundResource(R.drawable.zjzx_left_botton_button);
				refreshListData(strId, LEFTBOTTON_FIRST);
				for (int i = 0; i < 4; i++) {
					if (list.size() > i)
						buttons.get(i).setBackgroundResource(
								R.drawable.zjzx_left_botton_button);
				}
				botton.setBackgroundResource(R.drawable.zjzx_left_botton_button_selected);
			}
		});
		for (int i = 0; i < buttons.size(); i++) {
			final int j = i;
			buttons.get(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					prolayout.setVisibility(View.VISIBLE);
					moreBurron
							.setBackgroundResource(R.drawable.zjzx_left_botton_button);
					// 获取该业务下的列表
					if (j < 4) {
						refreshListData(list.get(j).getId(), LEFTBOTTOM_READY);
						for (int i = 0; i < 4; i++) {
							if (list.size() > i) {
								if (i == j)
									buttons.get(i)
											.setBackgroundResource(
													R.drawable.zjzx_left_botton_button_selected);
								else
									buttons.get(i).setBackgroundResource(
											R.drawable.zjzx_left_botton_button);
							}
						}
						botton.setBackgroundResource(R.drawable.zjzx_left_botton_button);
					} else if (j >= 4 && j < 8) {
						refreshListData(list.get(j).getId(), RIGHTTOP_READY);
						for (int i = 4; i < 8; i++) {
							if (list.size() > i) {
								if (i == j)
									buttons.get(i)
											.setBackgroundResource(
													R.drawable.zjzx_right_top_button_selected);
								else
									buttons.get(i).setBackgroundResource(
											R.drawable.zjzx_right_top_button);
							}
						}
					} else {
						refreshListData(list.get(j).getId(), RIGHTBOTTOM_READY);
						for (int i = 8; i < 10; i++) {
							if (list.size() > i) {
								if (i == j)
									buttons.get(i)
											.setBackgroundResource(
													R.drawable.zjzx_left_botton_button_selected);
								else
									buttons.get(i).setBackgroundResource(
											R.drawable.zjzx_left_botton_button);
							}
						}
					}
				}
			});
		}
		moreBurron = (Button) findViewById(R.id.info_left_botton_button6);
		moreBurron.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				moreBurron
						.setBackgroundResource(R.drawable.zjzx_left_botton_button_selected);
				alertCommonListPop();
			}
		});
		if (list.size() > 0)
			refreshListData(strId, LEFTBOTTON_FIRST);
		if (list.size() > 4)
			refreshListData(list.get(4).getId(), RIGHTTOP_READY);
		if (list.size() > 8)
			refreshListData(list.get(8).getId(), RIGHTBOTTOM_READY);
	}

	Handler dataHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case LEFTBOTTON_FIRST:
				// 为左下部视图首个按钮附件数据并设置事件
				triggerLeftBottomView();
				break;
			case LEFTTOP_READY:
				// 为左上部视图附件数据并设置事件
				triggerLeftTopView();
				break;
			case LEFTBOTTOM_READY:
				// 为左下部视图附件数据并设置事件
				triggerLeftBottomView();
				break;
			case RIGHTTOP_READY:
				// 为右上部视图附件数据并设置事件
				triggerRightTopView();
				break;
			case RIGHTBOTTOM_READY:
				// 为右下部视图附件数据并设置事件
				triggerRightBottomView();
				break;
			case SHOUCANG:
				InformationMainActivity.this.adapter1 = new CommonPopAdapter(
						InformationMainActivity.this,
						R.layout.common_listpop_listview_item, infoNews);
				listView1.setAdapter(InformationMainActivity.this.adapter1);
				prolayout.setVisibility(View.GONE);
				break;
			}
		}
	};

	protected void triggerRightBottomView() {
		if (rightBottomNews.size() == 2) {
			for (int i = 0; i < rightBottomParent.getChildCount(); i++) {
				RelativeLayout rl = (RelativeLayout) rightBottomParent
						.getChildAt(i);
				final InfoNew infoNew = rightBottomNews.get(i);
				TextView tv = (TextView) rl.getChildAt(0);
				tv.setText(infoNew.getTitle());
				ImageView iv = (ImageView) rl.getChildAt(1);
				iv.setTag(infoNew);
				iv.setOnClickListener(this);
				// 加载图片
				ImageResourse imageResource = new ImageResourse();
				imageResource.setIconUrl(rightBottomNews.get(i).getImagepath());
				imageResource.setIconId(rightBottomNews.get(i).getId());
				imageResource.setIconTime(rightBottomNews.get(i).getTime());
				asyncImageLoader.loadDrawable(imageResource,
						new AsyncImageLoader.ImageCallback() {
							@Override
							public void imageLoaded(Drawable drawable,
									String path) {
								ImageView imageView = (ImageView) rightBottomParent
										.findViewWithTag(infoNew);
								if (imageView != null && drawable != null) {
									imageView.setBackgroundDrawable(drawable);
								}
							}
						});
			}
		}
		prolayout.setVisibility(View.GONE);
	}

	protected void triggerRightTopView() {
		InfAdapter adapter = new InfAdapter(this, R.layout.inf_list_item_2,
				rightTopNews);
		rightListview.setAdapter(adapter);
		prolayout.setVisibility(View.GONE);

	}

	private InfAdapter infoAdapter;

	protected void triggerLeftBottomView() {
		if (infoAdapter == null) {
			infoAdapter = new InfAdapter(this, R.layout.inf_list_item,
					leftBottomNews);
			leftListview.setAdapter(infoAdapter);
		} else {
			infoAdapter.notifyDataSetChanged();
		}
		prolayout.setVisibility(View.GONE);
	}

	private void chengeImg(int number) {
		for (int i = 0; i < imageViews.length; i++) {
			if (i == number) {
				imageViews[i]
						.setBackgroundResource(R.drawable.econ_data_img_point0);
			} else {
				imageViews[i]
						.setBackgroundResource(R.drawable.econ_data_img_point1);
			}
		}
	}

	protected void triggerLeftTopView() {
		if (leftTopNews.size() >= 5) {
			goodAdapter = new InfomationImageAdapter(
					InformationMainActivity.this, leftTopNews, goodData,
					imageViews);
			goodData.setAdapter(goodAdapter);
			goodData.setSelection(Integer.MAX_VALUE / 2 - 3);
			imageViews[(Integer.MAX_VALUE / 2 - 3) % 5]
					.setBackgroundResource(R.drawable.econ_data_img_point0);
			prolayout.setVisibility(View.GONE);
		}
		goodData.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				chengeImg(arg2 % 5);
				infoTitle.setText(leftTopNews.get(arg2 % 5).getTitle());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		goodData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (((CeiApplication) getApplication()).isNet() && isGoUnline) {
					InfoNew newTop = leftTopNews.get(arg2 % 5);
					intentId = newTop.getId();
					intent = new Intent();
					intent.putExtra("extra", intentId);
					for (int i = 0; i < columnEntries.size(); i++) {
						if (columnEntries.get(i).getId().equals(jingcaiStrId)) {
							intent.putExtra("topNum", i + "");
						}
					}
					intent.putExtra("functionId", jingcaiStrId);
					intent.setClass(InformationMainActivity.this,
							Information.class);
					if (newTop.getId() != null) {
						if (newTop.getIsfree().endsWith("1")) {
							InformationMainActivity.this.startActivity(intent);
						} else {
							for (int i = 0; i < funIds.size(); i++) {
								if (funIds.get(i).getFunid()
										.endsWith(jingcaiStrId)) {
									canRead = true;
									break;
								} else {
									canRead = false;
								}
							}
							if (canRead) {
								InformationMainActivity.this
										.startActivity(intent);
							} else {
								MyTools.exitShow(InformationMainActivity.this,
										getWindow().getDecorView(), "未购买该栏目！");
							}
						}
					}
				}
			}
		});
	}

	private void refreshListData(final String functionId, final int operationId) {
		final Handler selfHandler = new Handler() {

			@Override
			public void dispatchMessage(Message msg) {
				switch (operationId) {
				case LEFTBOTTON_FIRST:
					leftBottomNews.clear();
					try {
						XmlUtil.getNewsList(msg.obj.toString(), leftBottomNews);
					} catch (Exception e) {
						e.printStackTrace();
					}
					WriteOrRead.write(msg.obj.toString(), MyTools.nativeData,
							"leftBottonFirst.xml");
					break;
				case LEFTBOTTOM_READY:
					leftBottomNews.clear();
					try {
						XmlUtil.getNewsList(msg.obj.toString(), leftBottomNews);
					} catch (Exception e) {
						e.printStackTrace();
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
						for (int j = 0; j < leftBottomNews.size(); j++) {
							leftBottomNews.get(j).setIsfree("1");
						}
					}
					break;
				case RIGHTTOP_READY:
					rightTopNews.clear();
					XmlUtil.getNewsList(msg.obj.toString(), rightTopNews);
					WriteOrRead.write(msg.obj.toString(), MyTools.nativeData,
							"rightTopResult.xml");
					for (int i = 0; i < funIds.size(); i++) {
						if (funIds.get(i).getFunid().endsWith(functionId)) {
							alreadyBuy = true;
							break;
						} else {
							alreadyBuy = false;
						}
					}
					if (alreadyBuy) {
						for (int j = 0; j < leftBottomNews.size(); j++) {
							rightTopNews.get(j).setIsfree("1");
						}
					}
					break;
				case RIGHTBOTTOM_READY:
					rightBottomNews.clear();
					XmlUtil.getNewsList(msg.obj.toString(), rightBottomNews);
					WriteOrRead.write(msg.obj.toString(), MyTools.nativeData,
							"rightBottonResult.xml");
					for (int i = 0; i < funIds.size(); i++) {
						if (funIds.get(i).getFunid().endsWith(functionId)) {
							alreadyBuy = true;
							break;
						} else {
							alreadyBuy = false;
						}
					}
					if (alreadyBuy) {
						for (int j = 0; j < rightBottomNews.size(); j++) {
							rightBottomNews.get(j).setIsfree("1");
						}
					}
					break;
				case ALREADY_BUY:
					String result = Service.queryBuyNews(columnEntry.getUserId());
					try {
						funIds = XmlUtil.queryBuyNews(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
				Message message = dataHandler.obtainMessage();
				message.arg1 = operationId;
				dataHandler.sendMessage(message);
				super.dispatchMessage(msg);
			}

		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				timeOutHelper.installTimerTask();
				String result = "";
				switch (operationId) {
				case LEFTBOTTON_FIRST:
					isFirst = true;
					alreadyBuy = false;
					if (((CeiApplication) InformationMainActivity.this
							.getApplication()).isNet()) {
						String leftBottonFirst = Service.queryNewsList(strId);
						Message message = selfHandler.obtainMessage();
						message.obj = leftBottonFirst;
						selfHandler.sendMessage(message);
					} else {
						String leftBottonFirst = WriteOrRead.read(
								MyTools.nativeData, "leftBottonFirst.xml");
						leftBottomNews.clear();
						try {
							XmlUtil.getNewsList(leftBottonFirst, leftBottomNews);
						} catch (Exception e) {
							e.printStackTrace();
						}
						Message message = dataHandler.obtainMessage();
						message.arg1 = operationId;
						dataHandler.sendMessage(message);
					}
					break;
				case LEFTBOTTOM_READY:
					isFirst = false;
					result = Service.queryNewsByFunctionId(functionId, "15",
							columnEntry.getUserId());
					Message message = selfHandler.obtainMessage();
					message.obj = result;
					selfHandler.sendMessage(message);
					break;
				case RIGHTTOP_READY:
					if (((CeiApplication) InformationMainActivity.this
							.getApplication()).isNet()) {
						String rightTopResult = Service.queryNewsByFunctionId(
								functionId, "15", columnEntry.getUserId());
						Message message1 = selfHandler.obtainMessage();
						message1.obj = rightTopResult;
						selfHandler.sendMessage(message1);
					} else {
						String rightTopResult = WriteOrRead.read(
								MyTools.nativeData, "rightTopResult.xml");
						rightTopNews.clear();
						XmlUtil.getNewsList(rightTopResult, rightTopNews);
						Message message2 = dataHandler.obtainMessage();
						message2.arg1 = operationId;
						dataHandler.sendMessage(message2);
						for (int i = 0; i < funIds.size(); i++) {
							if (funIds.get(i).getFunid().endsWith(functionId)) {
								alreadyBuy = true;
								break;
							} else {
								alreadyBuy = false;
							}
						}
						if (alreadyBuy) {
							for (int j = 0; j < leftBottomNews.size(); j++) {
								rightTopNews.get(j).setIsfree("1");
							}
						}
					}
					break;
				case RIGHTBOTTOM_READY:
					if (((CeiApplication) InformationMainActivity.this
							.getApplication()).isNet()) {
						String rightBottonResult = Service
								.queryNewsByFunctionId(functionId, "2",
										columnEntry.getUserId());
						Message message3 = selfHandler.obtainMessage();
						message3.obj = rightBottonResult;
						selfHandler.sendMessage(message3);
					} else {
						String rightBottonResult = WriteOrRead.read(
								MyTools.nativeData, "rightTopResult.xml");
						rightBottomNews.clear();
						XmlUtil.getNewsList(rightBottonResult, rightBottomNews);
						for (int i = 0; i < funIds.size(); i++) {
							if (funIds.get(i).getFunid().endsWith(functionId)) {
								alreadyBuy = true;
								break;
							} else {
								alreadyBuy = false;
							}
						}
						if (alreadyBuy) {
							for (int j = 0; j < rightBottomNews.size(); j++) {
								rightBottomNews.get(j).setIsfree("1");
							}
						}
						Message message2 = dataHandler.obtainMessage();
						message2.arg1 = operationId;
						dataHandler.sendMessage(message2);
					}
					break;
				case ALREADY_BUY:
					funIds = new ArrayList<funId>();
					result = Service.queryBuyNews(functionId);
					Message message3 = selfHandler.obtainMessage();
					message3.obj = result;
					selfHandler.sendMessage(message3);
					break;
				}
				timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
				Message message = dataHandler.obtainMessage();
				message.arg1 = operationId;
				dataHandler.sendMessage(message);
			}
		}).start();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		switch (parent.getId()) {
		case R.id.common_listpop_listview:
			InfoNew newsss = infoNews.get(position);
			String intentId = newsss.getId();
			// 跳转到详细页
			intent = new Intent();
			intent.putExtra("extra", intentId);
			for (int i = 0; i < columnEntries.size(); i++) {
				if (columnEntries.get(i).getId().equals(newsss.getFunctionId())) {
					intent.putExtra("topNum", i + "");
				}
			}
			intent.putExtra("functionId", newsss.getFunctionId());
			intent.setClass(InformationMainActivity.this, Information.class);
			if (infoNews.get(position).getIsfree().endsWith("1")) {
				InformationMainActivity.this.finish();
				InformationMainActivity.this.startActivity(intent);
			} else {
				for (int i = 0; i < funIds.size(); i++) {
					if (funIds.get(i).getFunid()
							.endsWith(infoNews.get(position).getFunctionId())) {
						canRead = true;
						break;
					} else {
						canRead = false;
					}
				}
				if (canRead) {
					InformationMainActivity.this.finish();
					InformationMainActivity.this.startActivity(intent);
				} else {
					MyTools.exitShow(InformationMainActivity.this, getWindow()
							.getDecorView(), "未购买该栏目！");
				}
			}
			break;
		case R.id.info_left_botton_listview:
			InfoNew new2 = leftBottomNews.get(position);
			intentId = new2.getId();
			// 跳转到详细页
			intent = new Intent();
			intent.putExtra("extra", intentId);
			for (int i = 0; i < columnEntries.size(); i++) {
				if (columnEntries.get(i).getId().equals(new2.getFunctionId())) {
					intent.putExtra("topNum", i + "");
				}
			}
			// intent.putExtra("isMenu", false);
			intent.putExtra("functionId", new2.getFunctionId());
			intent.setClass(InformationMainActivity.this, Information.class);
			if (isFirst) {
				if (new2.getIsfree().endsWith("1")) {
					InformationMainActivity.this.startActivity(intent);
				} else {
					for (int i = 0; i < funIds.size(); i++) {
						if (funIds.get(i).getFunid()
								.endsWith(new2.getFunctionId())) {
							canRead = true;
							break;
						} else {
							canRead = false;
						}
					}
					if (canRead) {
						InformationMainActivity.this.startActivity(intent);
					} else {
						MyTools.exitShow(InformationMainActivity.this,
								getWindow().getDecorView(), "未购买该栏目！");
					}
				}
			} else {
				if (new2.getIsfree().equals("1")) {
					InformationMainActivity.this.startActivity(intent);
				} else {
					MyTools.exitShow(InformationMainActivity.this, getWindow()
							.getDecorView(), "未购买该栏目！");
				}
			}
			break;
		case R.id.info_right_top_list:
			InfoNew new1 = rightTopNews.get(position);
			intentId = new1.getId();
			// 跳转到详细页
			intent = new Intent();
			for (int i = 0; i < columnEntries.size(); i++) {
				if (columnEntries.get(i).getId().equals(new1.getFunctionId())) {
					intent.putExtra("topNum", i + "");
				}
			}
			intent.putExtra("extra", intentId);
			// intent.putExtra("isMenu", false);
			intent.putExtra("functionId", new1.getFunctionId());
			intent.setClass(InformationMainActivity.this, Information.class);
			if (new1.getIsfree().equals("1")) {
				InformationMainActivity.this.startActivity(intent);
			} else {
				MyTools.exitShow(InformationMainActivity.this, getWindow()
						.getDecorView(), "未购买该栏目！");
			}
			break;
		case R.id.weather_list_listview:
			if (key == 1) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						String provinceCode = areas.get(position)[1];
						areas.clear();
						areas.addAll(XmlUtil.parsePCCodes(WeatherUtil
								.getCityCodes(provinceCode)));
						Message message = handler.obtainMessage();
						message.arg1 = GET_CITY;
						handler.sendMessage(message);
					}
				}).start();
			} else if (key == 2) {
				String cityCode = areas.get(position)[1];
				Editor editor = settings.edit();
				editor.putString("cityCode", cityCode);
				editor.commit();
				weatherInfo = XmlUtil.parseWeatherInfo(WeatherUtil
						.getWeather(cityCode));
				Message message = handler.obtainMessage();
				message.arg1 = GET_WEATHER;
				handler.sendMessage(message);
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.weather_Ll:
			if (((CeiApplication) getApplication()).isNet() && isGoUnline) {
				alertWeatherPop();
			}
			break;
		case R.id.zjzx_sousuo:
			intent = new Intent(InformationMainActivity.this,
					InfoSearchActivity.class);
			startActivity(intent);
			break;
		case R.id.zjzx_shoucang:
			if (!loginName.equals("")) {
				alertShoucangListPop();
			} else {
				Toast.makeText(InformationMainActivity.this, "请登陆后查看！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.zjzx_shuaxin:
			if (flage) {
				InformationMainActivity.this.finish();
				Intent intent = new Intent(this, InformationMainActivity.class);
				startActivity(intent);
				flage = false;
			} else {
				return;
			}
			break;
		case R.id.common_listpop_edit:
			for (int i = 0; i < infoNews.size(); i++) {
				infoNews.get(i).setIsCollect("0");
			}
			adapter1.notifyDataSetChanged();
			break;
		case R.id.common_listpop_clear:
			new Thread(new Runnable() {

				@Override
				public void run() {
					Service.clearCollect(((CeiApplication) InformationMainActivity.this
							.getApplication()).columnEntry.getUserId());
				}
			}).start();
			infoNews.clear();
			adapter1.notifyDataSetChanged();
			break;
		case R.id.home:
			InformationMainActivity.this.finish();
			break;
		default:
			InfoNew infoNew = (InfoNew) v.getTag();
			if (infoNew.getIsfree().equals("0"))
				return;
			intent = new Intent();
			intent.setClass(InformationMainActivity.this, Information.class);
			intent.putExtra("extra", infoNew.getId());
			for (int i = 0; i < columnEntries.size(); i++) {
				if (columnEntries.get(i).getId()
						.equals(infoNew.getFunctionId())) {
					intent.putExtra("topNum", i + "");
				}
			}
			// intent.putExtra("isMenu", false);
			intent.putExtra("functionId", infoNew.getFunctionId());

			if (infoNew.getIsfree().equals("1")) {
				InformationMainActivity.this.startActivity(intent);
			} else {
				MyTools.exitShow(InformationMainActivity.this, getWindow()
						.getDecorView(), "未购买该栏目！");
			}
			break;
		}
	}

	// 气温所在地区弹出框
	private PopupWindow popWindow;
	// 气温所在地区listview
	private ListView listView;
	// 收藏列表listview
	private ListView listView1;
	// 弹出框信息列表
	private List<String[]> areas;
	// 气温所在地区列表适配器
	private WeatherPopAdapter adapter;
	// 是否是省份列表
	private int key;
	private Handler handler;
	private static final int GET_CITY = 1;
	private static final int GET_WEATHER = 2;
	private WeatherInfo weatherInfo;

	private void alertWeatherPop() {
		prolayout.setVisibility(View.VISIBLE);
		key = 0;
		handler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				switch (msg.arg1) {
				case GET_CITY:
					key = 2;
					adapter.notifyDataSetChanged();
					prolayout.setVisibility(View.GONE);
					break;
				case GET_WEATHER:
					popWindow.dismiss();
					ContentResolver cv = InformationMainActivity.this
							.getContentResolver();

					String strTimeFormat = android.provider.Settings.System
							.getString(
									cv,
									android.provider.Settings.System.DATE_FORMAT);
					SimpleDateFormat bartDateFormat = null;
					try {
						bartDateFormat = new SimpleDateFormat(strTimeFormat);
					} catch (Exception e) {
						bartDateFormat = new SimpleDateFormat(
								"yyyy-MM-dd   HH:mm:ss");
					}
					Date date = new Date(weatherInfo.getTime());
					((TextView) findViewById(R.id.weather_tv))
							.setText(weatherInfo.getProvince() + "  "
									+ weatherInfo.getCity() + "     "
									+ bartDateFormat.format(date) + "\n"
									+ "    " + weatherInfo.getScene());
					break;
				default:
					key = 1;
					adapter = new WeatherPopAdapter(
							InformationMainActivity.this,
							R.layout.weatherpop_listview_item, areas);
					listView.setAdapter(adapter);
					prolayout.setVisibility(View.GONE);
					listView.setOnItemClickListener(InformationMainActivity.this);
					break;
				}

			}
		};
		View popView = this.getLayoutInflater().inflate(
				R.layout.weather_list_pop, null);
		new Thread(new Runnable() {

			@Override
			public void run() {
				areas = XmlUtil.parsePCCodes(WeatherUtil.getProvinceCodes());
				handler.sendMessage(handler.obtainMessage());
			}
		}).start();
		listView = (ListView) popView.findViewById(R.id.weather_list_listview);
		popView.findViewById(R.id.weather_province).setOnClickListener(this);
		popView.findViewById(R.id.weather_sure).setOnClickListener(this);
		popView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popWindow.dismiss();
				return false;
			}
		});
		popWindow = new PopupWindow(popView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popWindow.setFocusable(true);
		popWindow.showAtLocation(findViewById(R.id.full_view), Gravity.CENTER,
				0, 0);
	}

	private void alertCommonListPop() {
		prolayout.setVisibility(View.VISIBLE);
		View popView = this.getLayoutInflater().inflate(
				R.layout.informationmain_pop, null);
		gridview = (GridView) popView.findViewById(R.id.info_main_gridview);
		ArrayAdapter<String> aa = new ArrayAdapter<String>(
				InformationMainActivity.this, R.layout.info_main_grid_item,
				R.id.info_main_grid_text, nameList);
		gridview.setAdapter(aa);
		prolayout.setVisibility(View.GONE);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				intent = new Intent();
				intent.setClass(InformationMainActivity.this, Information.class);
				intent.putExtra("extra", "more");
				intent.putExtra("topNum", arg2 + "");
				intent.putExtra("functionId", columnEntries.get(arg2).getId());
				startActivity(intent);
				popWin.dismiss();
				moreBurron
						.setBackgroundResource(R.drawable.zjzx_left_botton_button);
			}

		});
		popView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popWin.dismiss();
				moreBurron
						.setBackgroundResource(R.drawable.zjzx_left_botton_button);
				return false;
			}
		});
		popWin = new PopupWindow(popView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(findViewById(R.id.info_left_botton_listview),
				Gravity.CENTER, 0, 0);
	}

	private void alertShoucangListPop() {
		prolayout.setVisibility(View.VISIBLE);
		View popView = this.getLayoutInflater().inflate(
				R.layout.common_list_pop, null);
		listView1 = (ListView) popView
				.findViewById(R.id.common_listpop_listview);
		listView1.setOnItemClickListener(this);
		final Handler selfHandler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				infoNews.clear();
				XmlUtil.getNewsList(msg.obj.toString(), infoNews);
				for (int i = 0; i < infoNews.size(); i++) {
					infoNews.get(i).setIsCollect("1");
				}
				Message message = dataHandler.obtainMessage();
				message.arg1 = SHOUCANG;
				dataHandler.sendMessage(message);
				super.dispatchMessage(msg);
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = Service
						.queryCollect(((CeiApplication) InformationMainActivity.this
								.getApplication()).columnEntry.getUserId());
				Message messsage = selfHandler.obtainMessage();
				messsage.obj = result;
				selfHandler.sendMessage(messsage);
			}
		}).start();
		popView.findViewById(R.id.common_listpop_edit).setOnClickListener(this);
		popView.findViewById(R.id.common_listpop_clear)
				.setOnClickListener(this);
		popView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popWin.dismiss();
				return false;
			}
		});
		popWin = new PopupWindow(popView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(findViewById(R.id.full_view), Gravity.CENTER, 0,
				0);
	}
	
	@Override
	protected void onDestroy() {
		if(goodAdapter != null)
			goodAdapter.clearBitmaps();
		super.onDestroy();
	}
}
