package com.hyrt.cei.ui.econdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.EconAdapter;
import com.hyrt.cei.adapter.EconImageAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.ui.econdata.view.EconDataView;
import com.hyrt.cei.ui.information.funId;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.TimeOutHelper;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.New;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 经济数据主页
 * 
 * @author heyx
 * 
 */
public class EconDataMain extends Activity implements OnClickListener {
	public static String MODEL_NAME;
	// 经济数据的子集业务集合
	public static ColumnEntry allColBg;
	private TimeOutHelper timeOutHelper;
	public static List<View> groupViews = new ArrayList<View>();
	public static List<TextView> childViews = new ArrayList<TextView>();
	private HashMap<String, Drawable> drawables = new HashMap<String, Drawable>();
	private ColumnEntry columnEntry;
	private List<New> goodNewsList, newNewsList, zhibiaoList;
	private StringBuilder colIDs;
	private ImageView imgpoint0, imgpoint1, imgpoint2, imgpoint3, imgpoint4,
			zhibiao, shuju;
	private ImageView zxImg, szImg, zjImg, fxImg, zbImg, sjImg, homeImg,
			backImg, shuax;
	private TextView tv_left;
	private Gallery goodData;
	private ImageView[] imageViews;
	private GridView newsData;
	private TextView zhibtext, goodReportTitle;
	private AsyncImageLoader asyncImageLoader;
	private String zhiBid;
	private String bgId;
	private CeiApplication application;
	private int zhibcont;
	EconImageAdapter goodAdapter;
	private Runnable runnable;
	private LinearLayout prolayout;
	private Handler handler = new Handler();
	private boolean flage;

	@Override
	protected void onDestroy() {
		if (goodAdapter != null)
			goodAdapter.clearBitmaps();
		if (drawables != null) {
			Iterator<String> iterator = drawables.keySet().iterator();
			while (iterator.hasNext()) {
				String path = iterator.next();
				Drawable drawable = drawables.get(path);
				if (((BitmapDrawable) drawable) != null)
					((BitmapDrawable) drawable).getBitmap().recycle();
				drawable = null;
			}
		}
		super.onDestroy();
		handler.removeCallbacks(runnable);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_data_main);
		MODEL_NAME = ((CeiApplication) getApplication()).nowStart;// 获取当前业务名称。
		timeOutHelper = new TimeOutHelper(this);
		application = (CeiApplication) getApplication();
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		asyncImageLoader = ((CeiApplication) this.getApplication()).asyncImageLoader;
		initView();
		prolayout.setVisibility(View.VISIBLE);
		initData();

	}

	private void initView() {
		// 低栏图标
		prolayout = (LinearLayout) findViewById(R.id.econ_data_pro);
		goodReportTitle = (TextView) findViewById(R.id.good_report_title);
		zxImg = (ImageView) findViewById(R.id.econ_data_zx);
		zxImg.setOnClickListener(this);
		szImg = (ImageView) findViewById(R.id.econ_data_sz);
		szImg.setOnClickListener(this);
		zjImg = (ImageView) findViewById(R.id.econ_data_zz);
		zjImg.setOnClickListener(this);
		fxImg = (ImageView) findViewById(R.id.econ_data_fx);
		fxImg.setOnClickListener(this);
		zbImg = (ImageView) findViewById(R.id.econ_data_zb);
		zbImg.setOnClickListener(this);
		sjImg = (ImageView) findViewById(R.id.econ_data_sj);
		sjImg.setOnClickListener(this);
		homeImg = (ImageView) findViewById(R.id.econ_data_home);
		homeImg.setOnClickListener(this);
		tv_left = (TextView) findViewById(R.id.tv_left);
		tv_left.setText(MODEL_NAME);
		//tv_left.setOnClickListener(this);
		shuax = (ImageView) findViewById(R.id.econ_data_shuax);
		shuax.setOnClickListener(this);
		shuju = (ImageView) findViewById(R.id.econ_data_data_img);
		shuju.setOnClickListener(this);
		zhibiao = (ImageView) findViewById(R.id.econ_data_zhib_img);
		zhibtext = (TextView) findViewById(R.id.econ_data_zhib_title);
		goodData = (Gallery) findViewById(R.id.econ_data_top);
		newsData = (GridView) findViewById(R.id.econ_data_news);
		imgpoint0 = (ImageView) findViewById(R.id.econ_data_img_point0);
		imgpoint1 = (ImageView) findViewById(R.id.econ_data_img_point1);
		imgpoint2 = (ImageView) findViewById(R.id.econ_data_img_point2);
		imgpoint3 = (ImageView) findViewById(R.id.econ_data_img_point3);
		imgpoint4 = (ImageView) findViewById(R.id.econ_data_img_point4);
		imageViews = new ImageView[] { imgpoint0, imgpoint1, imgpoint2,
				imgpoint3, imgpoint4 };
	}

	private void initData() {
		goodNewsList = new ArrayList<New>();
		newNewsList = new ArrayList<New>();
		zhibiaoList = new ArrayList<New>();
		// 更据业务ID查询业务里面的数据
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
					if (columnEntry.getName().equals("指标查询")) {
						zhiBid = columnEntry.getId();
					}
					colIDs.append(forId + ",");
				}
			}
		}

		/*if (allColBg != null && allColBg.getId() != null
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
					colIDs.append(forId + ",");
				}
			}
		}*/

		new Thread() {

			@Override
			public void run() {
				try {
					timeOutHelper.installTimerTask();
					if (((CeiApplication) EconDataMain.this.getApplication())
							.isNet()) {
						// 有图片的
						if (bgId != null && !bgId.equals("")) {
							String news = Service.querydbsImage(bgId);// queryNewsImage(bgId);
							goodNewsList = XmlUtil.getNews(news);
							if (goodNewsList.isEmpty()
									|| goodNewsList.size() < 5) {
								goodNewsList.add(new New());
								goodNewsList.add(new New());
								goodNewsList.add(new New());
								goodNewsList.add(new New());
								goodNewsList.add(new New());
							}
							WriteOrRead.write(news, MyTools.nativeData,
									"goodEcon.xml");
						}
						// 所有的
						if (colIDs != null && !colIDs.toString().equals("")) {
							String newNews = Service.querydbsList(colIDs
									.toString());
							newNewsList = XmlUtil.getNews(newNews);
							WriteOrRead.write(newNews, MyTools.nativeData,
									"newsEcon.xml");
						}
						// 28个queryNewsByFunctionId
						if (zhiBid != null && !zhiBid.equals("")) {
							String zhib = Service.querydbsByFunctionId(zhiBid,
									"28");
							zhibiaoList = XmlUtil.getNews(zhib);
							WriteOrRead.write(zhib, MyTools.nativeData,
									"ZbEcon.xml");
						}
						String buyEcon = Service.queryBuyDbNews(columnEntry
								.getUserId());
						WriteOrRead.write(buyEcon, MyTools.nativeData,
								"buyEcon.xml");
						application.buyEconData.clear();
						application.buyEconData.addAll(XmlUtil
								.queryBuyNews(buyEcon));
					} else {
						// 离线
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
						String zBEcon = WriteOrRead.read(MyTools.nativeData,
								"ZbEcon.xml");
						if (zBEcon != null && !zBEcon.equals("")) {
							zhibiaoList = XmlUtil.getNews(zBEcon);
						}
						String buyEcon = WriteOrRead.read(MyTools.nativeData,
								"buyEcon.xml");
						if (buyEcon != null && !buyEcon.equals("")) {
							application.buyEconData.clear();
							application.buyEconData.addAll(XmlUtil
									.queryBuyNews(buyEcon));
						}
					}
					timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
					if (handler != null) {
						runnable = new Runnable() {
							@Override
							public void run() {
								dosomeThing();
								zhibcont++;
								handler.postDelayed(this, 5000);
								flage = true;
							}
						};

						// 1.初回実行
						handler.postDelayed(runnable, 1000);

					}
				} catch (Exception e) {
					timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
					e.printStackTrace();
				}
				super.run();
			}

		}.start();

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

	@Override
	public void onClick(View v) {
		if (v == zxImg) {
			return;
		} else if (v == szImg) {
			Intent intent = new Intent(this, EconDateNumberActivity.class);
			startActivity(intent);
			// 数字快讯
		} else if (v == zjImg) {
			// 中经指数
			Intent intent = new Intent(this, EconZZDataActivity.class);
			startActivity(intent);
		} else if (v == fxImg) {
			// 分析预测
			Intent intent = new Intent(this, EconFXDataActivity.class);
			startActivity(intent);
		} else if (v == zbImg) {
			// 指标查询
			Intent intent = new Intent(this, EconZBQueryActivity.class);
			startActivity(intent);
		} else if (v == sjImg) {
			// 数据查询
			Intent intent = new Intent(this, EconDataQueryActivity.class);
			startActivity(intent);
		} else if (v == homeImg) {
			// 首页
			Intent intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
			this.finish();
		} else if (v == tv_left) {
			return;
		} else if (v == shuju) {
			// 右侧数据图片
			Intent intent = new Intent(this, EconDataQueryActivity.class);
			startActivity(intent);
		} else if (v == shuax) {
			if (flage) {
				flage=false;
				Intent intent = new Intent(this, EconDataMain.class);
				startActivity(intent);
				this.finish();
			} else {
				return;
			}
		}
	}

	private boolean isHasfunID(String funid) {
		for (funId id : application.buyEconData) {
			if (funid.equals(id.getFunid())) {
				return true;
			}
		}
		return false;
	}

	private void dosomeThing() {
		if (zhibcont == 0) {
			goodAdapter = new EconImageAdapter(EconDataMain.this, goodNewsList,
					goodData, imageViews);
			goodData.setAdapter(goodAdapter);
			goodData.setSelection(Integer.MAX_VALUE / 2 - 3);
			imageViews[(Integer.MAX_VALUE / 2 - 3) % 5]
					.setBackgroundResource(R.drawable.econ_data_img_point0);
			goodData.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					chengeImg(arg2 % 5);
					goodReportTitle.setText(goodNewsList.get(arg2 % 5)
							.getTitle());
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});
			goodData.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					New oneNew = goodNewsList.get(arg2 % 5);
					if (oneNew.getId() != null) {
						if (isHasfunID(bgId) || oneNew.getIsfree().equals("1")) {
							Intent intent = new Intent(EconDataMain.this,
									EconGoodDataActivity.class);
							if (oneNew != null) {
								intent.putExtra("id", oneNew.getId());
							}
							startActivity(intent);
							if (application.activitys
									.contains(EconGoodDataActivity.class)) {
								application.activitys
										.remove(EconGoodDataActivity.class);
								application.activitys
										.add(EconGoodDataActivity.class);
							} else {
								application.activitys
										.add(EconGoodDataActivity.class);
							}
						} else {
							// 栏目没有购买
							MyTools.exitShow(EconDataMain.this, getWindow()
									.getDecorView(), "未购买该栏目！");
						}
					}
				}
			});
			newsData.setAdapter(new EconAdapter(EconDataMain.this, newNewsList));
			newsData.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					New oneNew = (New) arg0.getAdapter().getItem(arg2);
					Class<?> clas = null;
					String otherId = "";
					String nowID="";
					if (oneNew.getFunname().equals("精彩数据")
							|| oneNew.getFunname().equals("指标查询")) {
						if (oneNew.getFunname().equals("精彩数据")) {
							clas = EconGoodDataActivity.class;
							otherId = columnEntry.getColByName("精彩数据").getId();
						} else if (oneNew.getFunname().equals("指标查询")) {
							clas = EconZBQueryActivity.class;
							otherId = columnEntry.getColByName("指标查询").getId();
						}
					} else {
						String parentID = columnEntry.getColByName(
								oneNew.getFunname()).getParentId();
						nowID=columnEntry.getColByName(
								oneNew.getFunname(),columnEntry.getColByName(MODEL_NAME).getId()).getId();
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
									findViewById(R.id.econ_data_main_all),
									"后台业务名称不匹配！");
							return;
						}
					}
					if (isHasfunID(otherId) ||isHasfunID(nowID)|| oneNew.getIsfree().equals("1")) {
						if (clas == null) {
							MyTools.exitShow(EconDataMain.this, getWindow()
									.getDecorView(), "后台业务名称不匹配！");
							return;
						}
						Intent intent = new Intent(EconDataMain.this, clas);
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
			prolayout.setVisibility(View.GONE);
		}
		// 指标数据
		if (zhibiaoList.size() > 0) {
			final New theNew = zhibiaoList.get(zhibcont % zhibiaoList.size());
			zhibtext.setText(theNew.getTitle());
			ImageResourse imageResource = new ImageResourse();
			imageResource.setIconUrl(theNew.getPpath());
			imageResource.setIconId(theNew.getId());
			imageResource.setIconTime(theNew.getTime());
			if (drawables.containsKey(theNew.getPpath())
					&& drawables.get(theNew.getPpath()) != null) {
				zhibiao.setImageDrawable(drawables.get(theNew.getPpath()));
				Log.i("view", "缓存起作用");
			} else {
				asyncImageLoader.loadDrawable(imageResource,
						new ImageCallback() {

							@Override
							public void imageLoaded(Drawable imageDrawable,
									String imageUrl) {

								if (zhibiao != null && imageDrawable != null) {
									zhibiao.setImageDrawable(imageDrawable);
									drawables.put(theNew.getPpath(),
											imageDrawable);
								}
							}
						});
			}
			zhibiao.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(EconDataMain.this,
							EconZBQueryActivity.class);
					if (theNew != null) {
						intent.putExtra("id", theNew.getId());
					}
					startActivity(intent);
				}
			});
		}
		/*
		 * if(goodNewsList.size()>0){
		 * goodData.setSelection(zhibcont%goodNewsList.size()); }
		 */
	}

}
