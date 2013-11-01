package com.hyrt.cei.ui.econdata;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.cei.adapter.EconAdapter;
import com.hyrt.cei.adapter.EconImageAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.vo.New;
import com.hyrt.cei.vo.funId;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.common.HomePageDZB;

public class EconDataMain extends ContainerActivity implements OnClickListener {
	public static String MODEL_NAME;
	public static ColumnEntry allColBg;
	ImageView econ_zzjk_icon, econ_data_sousuo_icon, sszk_icon, zjzs_icon,
			fxyc_icon, zbsc_icon;
	ImageView img1, img2, img3, img4, img5;
	TextView tv_title;
	TextView tv_left,tv_center,tv_right;
	private Context context = EconDataMain.this;
	private LinearLayout prolayout;
	private ImageView[] imageviews;
	private Gallery goodData;
	private CeiApplication application;
	private List<New> goodNewsList, newNewsList;
	private ColumnEntry columnEntry;// 业务类实体
	private String bgId;// 精彩数据的业务ID，图片类新闻的ID
	private StringBuilder colIDs;// GridView业务中的ID
	private Handler handler = new Handler();
	private Runnable runnable;
	private int zhibcont;
	private boolean flage;
	private EconImageAdapter goodAdapter;
	private ListView econ_data_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_data_main);
		MODEL_NAME = ((CeiApplication) getApplication()).nowStart;// 获取当前业务名称。
		application = (CeiApplication) getApplication();
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		System.out.println("设备号" + tm.getDeviceId());
		initView();
		// prolayout.setVisibility(View.INVISIBLE);
		initData();

	}

	// 初始化
	private void initView() {
		// 加载progressbar按钮
		// prolayout = (LinearLayout) findViewById(R.id.econ_data_pro);
		// 中经智库图标
		tv_left = (TextView) findViewById(R.id.econ_main_top_icon);
		tv_center=(TextView)findViewById(R.id.econ_main_top_img);
		tv_center.setText(MODEL_NAME);
		tv_left.setOnClickListener(this);
		// 搜索图标
		tv_right = (TextView) findViewById(R.id.econ_main_top_img2);
		tv_right.setOnClickListener(this);
		// 数字快讯
		sszk_icon = (ImageView) findViewById(R.id.econ_main_botton_2);
		sszk_icon.setOnClickListener(this);
		// 中经指数
		zjzs_icon = (ImageView) findViewById(R.id.econ_main_botton_3);
		zjzs_icon.setOnClickListener(this);
		// 分析预测
		fxyc_icon = (ImageView) findViewById(R.id.econ_main_botton_4);
		fxyc_icon.setOnClickListener(this);
		// 指标速查
		zbsc_icon = (ImageView) findViewById(R.id.econ_main_botton_5);
		zbsc_icon.setOnClickListener(this);
		
		// imageview 点图标
		img1 = (ImageView) findViewById(R.id.econ_main_button1);
		img2 = (ImageView) findViewById(R.id.econ_main_button2);
		img3 = (ImageView) findViewById(R.id.econ_main_button3);
		img4 = (ImageView) findViewById(R.id.econ_main_button4);
		img5 = (ImageView) findViewById(R.id.econ_main_button5);
		// galler新闻标题
		tv_title = (TextView) findViewById(R.id.econ_main_lin_title_text);
		// 改变图片的imageviw数组
		imageviews = new ImageView[] { img1, img2, img3, img4, img5 };
		// gallery
		goodData = (Gallery) findViewById(R.id.econ_main_grid);
		// listview
		econ_data_list = (ListView) findViewById(R.id.econ_data_pro);

	}

	private void initData() {
		System.out.println("initData");
		// gallery中的新闻实体
		goodNewsList = new ArrayList<New>();
		newNewsList = new ArrayList<New>();
		// 根据业务ID查询业务里面的数据，精彩数据对应图片中的新闻

		
		// 根据业务ID查询业务里面的数据
				allColBg = columnEntry.getColByName(MODEL_NAME);
				if (allColBg != null && allColBg.getId() != null
						&& !allColBg.getId().equals("")) {
					String allBgId = allColBg.getId();
					colIDs = new StringBuilder();
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
							if (columnEntry.getName().equals("精彩数据")) {
								bgId = columnEntry.getId();
							}
							colIDs.append(forId + ",");
						}
					}
				}
		
		
		
		
		
		/*ColumnEntry colBg = columnEntry.getColByName("精彩数据");

		if (colBg != null) {
			// 业务ID
			bgId = colBg.getId();
		}
		// listview中经济数据业务实体
		ColumnEntry allColBg = columnEntry.getColByName(MODEL_NAME);
		if (allColBg != null && allColBg.getId() != null
				&& !allColBg.getId().equals("")) {
			// 经济数据的ID
			String allBgId = allColBg.getId();
			// 经济数据中listview中的子业务业务ID
			colIDs = new StringBuilder();
			// 获得经济数据下的所有业务
			List<ColumnEntry> allCol = columnEntry
					.getEntryChildsForParent(allBgId);
			// 遍历所有业务
			for (ColumnEntry columnEntry : allCol) {
				// 获取某一个业务的ID,例如分析预测等，forID经济数据下子业务ID
				String forId = columnEntry.getId();
				// 判断是否有子业务
				if (this.columnEntry.getEntryChildsForParent(forId).size() != 0
						&& this.columnEntry.getEntryChildsForParent(forId) != null) {
					// 有子业务
					List<ColumnEntry> childCols = this.columnEntry
							.getEntryChildsForParent(forId);
					for (ColumnEntry columnEntry2 : childCols) {
						colIDs.append(columnEntry2.getId() + ",");
					}
				} else {
					colIDs.append(forId + ",");
				}

			}
		}*/
		new Thread() {

			@Override
			public void run() {
				try {
					// 判断是否联网
					if (((CeiApplication) EconDataMain.this.getApplication())
							.isNet()) {
						// 精彩数据的业务ID
						if (bgId != null && !bgId.equals("")) {
							System.out.println("ceiphone news bgId" + bgId);
							// 通过图片类新闻的ID获取对应新闻
							String news = Service.querydbsImage(bgId);
							System.out.println("ceiphone news " + news);// 得到的是(newsGroup)五条新闻信息
							// 解析到集合中
							goodNewsList = XmlUtil.getNews(news);
							// 判断集合是否为空
							if (goodNewsList.isEmpty()
									|| goodNewsList.size() < 5) {
								// 如果为空
								goodNewsList.add(new New());
								goodNewsList.add(new New());
								goodNewsList.add(new New());
								goodNewsList.add(new New());
								goodNewsList.add(new New());
							}
							// 把新闻写到本地文件中
							WriteOrRead.write(news, MyTools.nativeData,
									"goodEcon.xml");
							// GridView中的新闻
							if (colIDs != null && !colIDs.toString().equals("")) {
								// 通过colID生成新闻的字符串
								System.out.println("colId" + colIDs);
								String newNews = Service.querydbsList(colIDs
										.toString());
								newNewsList = XmlUtil.getNews(newNews);
								WriteOrRead.write(newNews, MyTools.nativeData,
										"newsEcon.xml");
							}
							// 查询购买的经济数据
							String buyEcon = Service.queryBuyDbNews(columnEntry
									.getUserId());
							WriteOrRead.write(buyEcon, MyTools.nativeData,
									"buyEcon.xml");
							// 清空集合中的数据
							application.buyEconData.clear();
							// 把购买的数据加入集合中
							application.buyEconData.addAll(XmlUtil
									.queryBuyNews(buyEcon));
						}
					} else {
						// 离线，读取本地保存细嫩
						String news = WriteOrRead.read(MyTools.nativeData,
								"goodEcon.xml");
						if (news != null && !news.equals("")) {
							goodNewsList = XmlUtil.getNews(news);
						}
						if (goodNewsList.isEmpty() || goodNewsList.size() < 5) {
							goodNewsList.add(new New());
							goodNewsList.add(new New());
							goodNewsList.add(new New());
							goodNewsList.add(new New());
							goodNewsList.add(new New());
						}
						String newsEcon = WriteOrRead.read(MyTools.nativeData,
								"newsEcon.xml");
						if (newsEcon != null && !newsEcon.equals("")) {
							newNewsList = XmlUtil.getNews(newsEcon);
						}
						// GridView中存放的是购买的新闻
						String buyEcon = WriteOrRead.read(MyTools.nativeData,
								"buyEcon.xml");
						if (buyEcon != null && !buyEcon.equals("")) {
							application.buyEconData.clear();
							application.buyEconData.addAll(XmlUtil
									.queryBuyNews(buyEcon));
						}

					}
					if (handler != null) {
						runnable = new Runnable() {

							@Override
							public void run() {
								// 在线程中进行与图片滑动有关的操作,每隔几秒图片自动切换
								dosomeThing();
								// zhibcont++;
								// handler.postDelayed(this, 5000);
								flage = true;
								System.out.println("不进行自动切换");

							}
						};
						handler.postDelayed(runnable, 1000);

					}

				} catch (Exception e) {
				}
				super.run();
			}

		}.start();

	}

	private void dosomeThing() {
		if (zhibcont == 0) {
			// goodNewList:图片新闻 goodData:gallery

			goodAdapter = new EconImageAdapter(EconDataMain.this, goodNewsList,
					goodData, imageviews);
			goodData.setAdapter(goodAdapter);
			/*
			 * long m = Integer.MAX_VALUE / 2 - 3; System.out.println("m:"+m);
			 */
			goodData.setSelection(Integer.MAX_VALUE / 2 - 3);

			// Gallery的选择事件
			goodData.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					New info = goodNewsList.get(arg2 % 5);
					change(arg2 % 5);
					// Gallery中的新闻标题
					if (info.getTitle() != null) {
						tv_title.setText(goodNewsList.get(arg2 % 5).getTitle()
								.length() > 10 ? goodNewsList.get(arg2 % 5)
								.getTitle().substring(0, 9)
								+ "..." : goodNewsList.get(arg2 % 5).getTitle());
					} else {
						tv_title.setText("");
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
			// Gallery的点击事件
			goodData.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// 取得某一条新闻的实体
					New oneNew = goodNewsList.get(arg2 % 5);
					// 判断此信息的ID
					if (oneNew.getId() != null) {
						if (isHasfunID(bgId) || oneNew.getIsfree().equals("1")) {
							Intent intent = new Intent(EconDataMain.this,
									EconDataContent.class);
							if (oneNew != null) {
								intent.putExtra("id", oneNew.getId());
							}
							startActivity(intent);
							if (application.activitys
									.contains(EconDataContent.class)) {
								application.activitys
										.remove(EconDataContent.class);
								application.activitys
										.add(EconDataContent.class);
							} else {
								application.activitys
										.add(EconDataContent.class);
							}
						} else {
							// 栏目没有购买
							MyTools.exitShow(EconDataMain.this, getWindow()
									.getDecorView(), "未购买该栏目");
						}
					}

				}
			});
			imageviews[0]
					.setBackgroundResource(R.drawable.econ_data_img_point0);
			// --------------------------------------------------------------------------------------------------------------------------------------
			// listview新闻
			System.out.println("经济主页listview");
			System.out.println("经济主页s" + newNewsList);
			econ_data_list.setAdapter(new EconAdapter(EconDataMain.this,
					newNewsList));
			System.out.println("经济主页点击监听");
			// 执行跳转，以及判断是否已经购买
			econ_data_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					System.out.println("经济主页listviwe新闻");
					// 点击的新闻
					New oneNew = newNewsList.get(arg2);
					Class<?> clas = null;
					String otherId = "";
					String nowID="";
					if (oneNew.getFunname().equals("精彩数据")
							|| oneNew.getFunname().equals("指标查询")) {
						if (oneNew.getFunname().equals("精彩数据")) {
							clas = EconDataContent.class;
							otherId = columnEntry.getColByName("精彩数据").getId();
						} else if (oneNew.getFunname().equals("指标查询")) {
							clas = EconZBQueryActivity.class;
							otherId = columnEntry.getColByName("指标查询").getId();
						}
					} else {
						String parentID = columnEntry.getColByName(
								oneNew.getFunname()).getParentId();
						String GrandParentId=columnEntry.getColByName(MODEL_NAME).getId();
						nowID=columnEntry.getColByName(
								oneNew.getFunname(),GrandParentId).getId();
						if (columnEntry.getFunNameByID(parentID).equals("分析预测")) {
							clas = EconFXDataActivity.class;
							otherId = columnEntry.getColByName("分析预测").getId();
						} else if (columnEntry.getFunNameByID(parentID).equals(
								"数字快讯")) {
							clas = EconDateNumberActivity.class;
							otherId = columnEntry.getColByName("数字快讯").getId();
						} else if (columnEntry.getFunNameByID(parentID).equals(
								"中经指数")) {
							clas = EconZZDataActivity.class;
							otherId = columnEntry.getColByName("中经指数").getId();
						} else {
							MyTools.exitShow(EconDataMain.this,
									findViewById(R.id.econ_main_top),
									"后台业务名称不匹配！");
							return;
						}
					}
					// 判断是否免费，1代表免费
					if (isHasfunID(otherId) ||isHasfunID(nowID)|| oneNew.getIsfree().equals("1")) {
						if (clas == null) {
							MyTools.exitShow(EconDataMain.this, getWindow()
									.getDecorView(), "后台业务名称不匹配！");
							return;
						}
						Intent intent = new Intent(EconDataMain.this,
								EconDataContent.class);
						intent.putExtra("id", oneNew.getId());
						startActivity(intent);
						if (application.activitys.contains(clas)) {
							application.activitys.remove(clas);
							application.activitys.add(clas);
						} else {
							application.activitys.add(clas);
						}
					} else {
						// 栏目没有购买
						MyTools.exitShow(EconDataMain.this, getWindow()
								.getDecorView(), "未购买该栏目！");
					}
				}
			});
			// prolayout.setVisibility(View.GONE);
		}
		/*
		 * if (goodNewsList.size() > 1) { // 设置gallery所选择的图片
		 * goodData.setSelection(zhibcont % 5); }
		 */

	};

	// 改变图片旋转
	private void change(int num) {
		for (int i = 0; i < imageviews.length; i++) {
			if (i == num) {
				imageviews[i]
						.setBackgroundResource(R.drawable.read_report_index_select);
			} else {
				imageviews[i].setBackgroundResource(R.drawable.home_img_ratio);

			}
		}
	}

	// 是否付费
	private boolean isHasfunID(String funid) {
		for (funId id : application.buyEconData) {
			if (funid.equals(id.getFunid())) {
				return true;
			}
		}
		return false;

	}

	@Override
	public void onClick(View v) {
		if (v == tv_left) {
			// 中经网主页
			Intent intent = new Intent(EconDataMain.this, HomePageDZB.class);
			startActivity(intent);

		} else if (v == tv_right) {
			// 刷新
			Intent intent = new Intent(EconDataMain.this,
					EconDataSearchActivity.class);
			startActivity(intent);

		} else if (v == sszk_icon) {
			// 数字快讯
			Intent intent = new Intent(EconDataMain.this,
					EconDateNumberActivity.class);
			startActivity(intent);
		} else if (v == zjzs_icon) {
			// 中经指数
			Intent intent = new Intent(EconDataMain.this,
					EconZZDataActivity.class);
			startActivity(intent);
		} else if (v == fxyc_icon) {
			// 分析预测
			Intent intent = new Intent(EconDataMain.this,
					EconFXDataActivity.class);
			startActivity(intent);
		} else if (v == zbsc_icon) {
			// 指标速查
			Intent intent = new Intent(EconDataMain.this,
					EconZBQueryActivity.class);
			startActivity(intent);
		}
	}
}
