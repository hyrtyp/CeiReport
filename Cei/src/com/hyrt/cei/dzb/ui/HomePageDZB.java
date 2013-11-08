package com.hyrt.cei.dzb.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.vo.LRBitmap;
import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.common.LoginActivity;
import com.hyrt.cei.ui.common.WebViewUtil;
import com.hyrt.cei.ui.ebook.ReadReportActivity;
import com.hyrt.cei.ui.econdata.EconDataMain;
import com.hyrt.cei.ui.information.InformationMainActivity;
import com.hyrt.cei.ui.main.Announcement;
import com.hyrt.cei.ui.main.Disclaimer;
import com.hyrt.cei.ui.phonestudy.HomePageActivity;
import com.hyrt.cei.ui.phonestudy.PreloadActivity;
import com.hyrt.cei.ui.witsea.WitSeaActivity;
import com.hyrt.cei.update.UpdateManager;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.AnnouncementNews;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Preload;
import com.hyrt.cei.webservice.service.Service;

public class HomePageDZB extends Activity implements OnClickListener,
		OnTouchListener {
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private List<AnnouncementNews> announcementNews;
	private TextView textNum;
	private List<ColumnEntry> columnEnties;
	private ImageView botton1, botton2, botton3, botton4, botton5;
	private ImageView[] leftImageView = new ImageView[3];
	private ImageView[] rightImageView = new ImageView[3];
	private AsyncImageLoader asyncImageLoader;
	private int index;
	private GestureDetector gestureDetector;
	private int announcementCount;
	// 通用页面管理集合
	public static List<Activity> commonActivities = new ArrayList<Activity>();
	// 用户名
	private String loginName;
	// 左右元素的父级元素
	private RelativeLayout leftre;
	private RelativeLayout righttre;
	// 全局的菜单集合
	private ColumnEntry columnEntry;
	// 4大业务模块
	public static final String[] MODELS = { "移动学习", "政经资讯", "经济数据", "研究报告" };
	// 首页背景
	public RelativeLayout home_page_re;
	//一级业务
	private List<ColumnEntry> firstColumnEntries;
	
	@Override
	protected void onDestroy() {
		HomePageDZB.commonActivities.remove(this);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page_dzb);
		/*new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				boolean isDowning = false;
				List<Preload> preloads = ((CeiApplication) getApplication()).dataHelper
						.getPreloadList();
				for (int i = 0; i < preloads.size(); i++)
					if (preloads.get(i).getLoadFinish() != 1)
						isDowning = true;
				if (isDowning)
					alertIsSurePop(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							popWin.dismiss();
							Intent intent = new Intent(HomePageDZB.this,
									PreloadActivity.class);
							startActivity(intent);
						}
					});
			}
		}, 200);*/
		UpdateManager manager = new UpdateManager(HomePageDZB.this);
		// 检查软件更新
		manager.isUpdate();
		for (int i = 0; i < HomePageDZB.commonActivities.size(); i++) {
			try {
				HomePageDZB isHomePageDZB = (HomePageDZB) (HomePageDZB.commonActivities
						.get(i));
				isHomePageDZB.finish();
			} catch (Exception e) {
			}
		}
		commonActivities.add(this);
		asyncImageLoader = ((CeiApplication) (this.getApplication())).asyncImageLoader;
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		index = 0;
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		Typeface fontFace = Typeface.createFromAsset(getAssets(),
				"fonts/FZCQJW.TTF");
		TextView tv = (TextView) findViewById(R.id.homepage_dzb_login_text);
		TextView tv2 = (TextView) findViewById(R.id.homepage_dzb_login_text_2);
		tv.setTypeface(fontFace);
		tv2.setTypeface(fontFace);
		if (!loginName.equals("")) {
			tv.setText("欢迎您...");
			tv2.setText(loginName);
		} else {

		}
		findViewById(R.id.homepage_dzb_yjbg).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(HomePageDZB.this,
								ReadReportActivity.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.homepage_dzb_login).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (loginName.equals("")) {
							Intent intent = new Intent(HomePageDZB.this,
									LoginActivity.class);
							startActivity(intent);
						}
					}
				});
		installDataForView();
		// 检测图片的加载个数，超过2秒的显示加载好的图片
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				initRLRView();
			}
		}, 2000);
		
		for(int i=0;i<firstColumnEntries.size();i++){
			if(firstColumnEntries.get(i).getName().contains("报告")){
				((CeiApplication)(getApplication())).nowStart=firstColumnEntries.get(i).getName();
			}
		}
		startActivity(new Intent().setClass(this, ReadReportActivity.class));
		this.finish();
	}

	@Override
	protected void onResume() {
		initRLRView();
		super.onResume();
	}

	/**
	 * 加载数据
	 */
	private void installDataForView() {

		textNum = (TextView) findViewById(R.id.home_page_main_num);
		findViewById(R.id.button1).setOnClickListener(this);
		botton1 = (ImageView) findViewById(R.id.home_announcement);
		botton1.setOnClickListener(this);
		botton2 = (ImageView) findViewById(R.id.home_witsea);
		botton2.setOnClickListener(this);
		botton3 = (ImageView) findViewById(R.id.home_ceinet);
		botton3.setOnClickListener(this);
		botton4 = (ImageView) findViewById(R.id.home_personcenter);
		botton4.setOnClickListener(this);
		botton5 = (ImageView) findViewById(R.id.home_disclaimer);
		botton5.setOnClickListener(this);
		leftre = (RelativeLayout) findViewById(R.id.homepage_re_left);
		leftre.setOnTouchListener(this);
		righttre = (RelativeLayout) findViewById(R.id.homepage_re_right);
		righttre.setOnTouchListener(this);
		leftImageView[0] = (ImageView) findViewById(R.id.homepage_left_top);
		leftImageView[1] = (ImageView) findViewById(R.id.homepage_left_mid);
		leftImageView[2] = (ImageView) findViewById(R.id.homepage_left_botton);
		rightImageView[0] = (ImageView) findViewById(R.id.homepage_right_top);
		rightImageView[1] = (ImageView) findViewById(R.id.homepage_right_mid);
		rightImageView[2] = (ImageView) findViewById(R.id.homepage_right_botton);
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		columnEnties = new ArrayList<ColumnEntry>();
		firstColumnEntries = columnEntry
				.getEntryChildsForParent(null);
		this.columnEntry.getWitSeaColumns().clear();
		for (int i = 0; i < firstColumnEntries.size(); i++) {
			boolean isModels = false;
			ColumnEntry columnEntry = firstColumnEntries.get(i);
			for (int j = 0; j < MODELS.length; j++) {
				if ((MODELS[j]).equals(columnEntry.getName())) {
					isModels = true;
					columnEnties.add(columnEntry);
				}
			}
			if (!isModels && !columnEntry.getName().equals("关于我们"))
				this.columnEntry.getWitSeaColumns().add(columnEntry);
		}
		columnEnties.addAll(columnEntry.getSelectColumnEntryChilds());
		// 加载左右的图片
		calculateRLBitmap(columnEnties);
		home_page_re = (RelativeLayout) findViewById(R.id.home_page_re);
		home_page_re.setBackgroundResource(R.drawable.welcome_background);
		ImageResourse imageResource = new ImageResourse();
		imageResource.setIconUrl(columnEntry.getBackground());
		imageResource.setIconId(columnEntry.getBackground());
		asyncImageLoader.loadDrawable(imageResource,
				new AsyncImageLoader.ImageCallback() {

					@Override
					public void imageLoaded(Drawable drawable, String path) {
						home_page_re.setBackgroundDrawable(drawable);
					}
				});
		refreshListData();
	}

	/**
	 * 加载业务图片
	 * 
	 * @param list
	 * @param iv
	 * @param index
	 */
	private void initLRView(List<Drawable> list, ImageView[] iv, int index) {
		for (int y = 0; y < iv.length; y++) {
			iv[y].setVisibility(View.INVISIBLE);
		}
		if ((list.size() % 3 == 0 ? list.size() / 3 : list.size() / 3 + 1) >= index) {
			int x = 0;
			for (int j = index * 3; j < list.size() && x < 3; j++) {
				final ImageView currentIv = iv[x];
				currentIv.setVisibility(View.VISIBLE);
				Drawable currentDrawable = list.get(j);
				x++;
				currentIv.setBackgroundDrawable(currentDrawable);
				Iterator<LRBitmap> itr = lrbs.keySet().iterator();
				while (itr.hasNext()) {
					LRBitmap lrKey = itr.next();
					Drawable dra = lrbs.get(lrKey);
					// 为模块设置跳转事件
					if (dra.equals(currentDrawable)) {
						currentIv.setTag(lrKey);
						currentIv.setOnClickListener(this);
					}
				}
			}
		}
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(final View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.home_announcement:
			intent = new Intent(HomePageDZB.this, Announcement.class);
			if (!loginName.equals("")) {
				startActivity(intent);
				textNum.setVisibility(View.GONE);
			} else {
				Toast.makeText(this, "请登陆后查看！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.home_witsea:
			intent = new Intent(HomePageDZB.this, WitSeaActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				Toast.makeText(this, "请登陆后查看！", Toast.LENGTH_SHORT).show();
			break;
		case R.id.home_ceinet:
			intent = new Intent(HomePageDZB.this, WebViewUtil.class);
			intent.putExtra("path", "http://mob.cei.gov.cn/");
			startActivity(intent);
			break;
		case R.id.home_personcenter:
			intent = new Intent(HomePageDZB.this, PersonCenter.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				Toast.makeText(this, "请登陆后查看！", Toast.LENGTH_SHORT).show();
			break;
		case R.id.home_disclaimer:
			intent = new Intent(HomePageDZB.this, Disclaimer.class);
			startActivity(intent);
			break;
		case R.id.button1:
			intent = new Intent(HomePageDZB.this, InformationMainActivity.class);
			startActivity(intent);
			break;
		default:
			goModelAc(((LRBitmap) view.getTag()).getName());
			break;
		}
	}

	/**
	 * 根据name跳转activity
	 * 
	 * @param name
	 */
	public void goModelAc(String name) {
		if (name == null)
			return;
		if (name.equals("移动学习") || name.contains("学习")) {
			HomePageActivity.MODEL_NAME = name;
			Intent intent = new Intent(HomePageDZB.this, HomePageActivity.class);
			startActivity(intent);
		}
		if (name.equals("政经资讯") || name.contains("资讯")) {
			Intent intent = new Intent(HomePageDZB.this,
					InformationMainActivity.class);
			intent.putExtra("tag", name);
			startActivity(intent);
		}
		if (name.equals("研究报告") || name.contains("报告")) {
			Intent intent = new Intent(HomePageDZB.this,
					ReadReportActivity.class);
			startActivity(intent);
		}
		if (name.equals("经济数据") || name.contains("数据")) {
			Intent intent = new Intent(HomePageDZB.this, EconDataMain.class);
			startActivity(intent);
		}
		((CeiApplication) (getApplication())).nowStart = name;
		addLog(name);
	}

	/**
	 * 加入操作日志
	 */
	private void addLog(final String title) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ColumnEntry columnEntry = new ColumnEntry();
				for (int i = 0; i < ((CeiApplication) (HomePageDZB.this
						.getApplication())).columnEntry.getColumnEntryChilds()
						.size(); i++) {
					if (title.equals(((CeiApplication) (HomePageDZB.this
							.getApplication())).columnEntry
							.getColumnEntryChilds().get(i).getName()))
						columnEntry.setId(((CeiApplication) (HomePageDZB.this
								.getApplication())).columnEntry
								.getColumnEntryChilds().get(i).getId());
				}
				columnEntry.setLoginid(((CeiApplication) (HomePageDZB.this
						.getApplication())).columnEntry.getLoginid());
				columnEntry.setName(title);
				Service.addLog(columnEntry);
			}
		}).start();
	}

	class MyGestureDetector extends SimpleOnGestureListener {

		private static final int SWIPE_MIN_DISTANCE = 10;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
				updateViewByIndex(upper);
			} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && index > 0) {
				updateViewByIndex(downward);
			}
			return true;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		HomePageDZB.this.onTouchEvent(event);
		return true;
	}

	private void upAnimation(View v) {
		Animation up = AnimationUtils.loadAnimation(this, R.anim.push_up);
		v.setAnimation(up);
	}

	private void downAnimation(View v) {
		Animation down = AnimationUtils.loadAnimation(this, R.anim.push_down);
		v.setAnimation(down);
	}

	private void refreshListData() {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				announcementCount = 0;
				announcementNews = new ArrayList<AnnouncementNews>();
				String rs = "";
				rs = Service
						.queryNotice(((CeiApplication) getApplication()).columnEntry
								.getUserId());
				try {
					announcementNews = XmlUtil.getAnnouncement(rs);
					announcementCount = announcementNews.size();
					SharedPreferences settings = getSharedPreferences(
							"announcementCount", Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("new", announcementCount);
					editor.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = newsHandler.obtainMessage();
				newsHandler.sendMessage(msg);
			}
		});
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			SharedPreferences settings = getSharedPreferences(
					"announcementCount", Activity.MODE_PRIVATE);
			int changeCount = settings.getInt("new", 0)
					- settings.getInt("old", 0);
			if (changeCount > 0) {
				textNum.setText(changeCount + "");
				textNum.setVisibility(View.VISIBLE);
			} else {
				textNum.setVisibility(View.GONE);
			}
		}
	};

	/**
	 * 判断是否为左面的图片样式
	 * 
	 * @param drawable
	 * @return
	 */
	private boolean decernBitmpaL(Drawable drawable) {
		Bitmap targetBitmap = ((BitmapDrawable) drawable).getBitmap();
		int width = targetBitmap.getWidth();
		int height = targetBitmap.getHeight();
		int pixel = targetBitmap.getPixel(width - 3, height - 1);
		if (pixel == 0)
			return false;
		return true;
	}

	/**
	 * 统计左边与右边的Bitmap
	 * 
	 * @param columnEnties
	 */
	private void calculateRLBitmap(List<ColumnEntry> columnEnties) {
		for (int i = 0; i < columnEnties.size(); i++) {
			getDrawables(columnEnties.get(i), columnEnties.size(), i);
		}
	}

	// 记录图片完成加载的个数
	private int alLoadIvCount;
	// 左右两边的图片缓存集合
	private Map<LRBitmap, Drawable> lrbs = new HashMap<LRBitmap, Drawable>();
	// 左边的图片集合
	private List<Drawable> leftDrawables = new ArrayList<Drawable>();
	// 右边的图片集合
	private List<Drawable> rightDrawables = new ArrayList<Drawable>();

	/**
	 * 获取所有业务图片，并分开左右
	 * 
	 * @param path
	 * @param convertView
	 */
	public void getDrawables(ColumnEntry columnEntry, final int totalNum,
			int imageLevel) {
		final LRBitmap lrb = new LRBitmap();
		lrb.setLevel(imageLevel);
		lrb.setName(columnEntry.getName());
		ImageResourse imageResource = new ImageResourse();
		imageResource.setIconUrl(columnEntry.getOperationImage());
		imageResource.setIconId(columnEntry.getId());
		imageResource.setIconTime(columnEntry.getIssueTime());
		asyncImageLoader.loadDrawable(imageResource,
				new AsyncImageLoader.ImageCallback() {

					@Override
					public void imageLoaded(Drawable drawable, String path) {
						alLoadIvCount++;
						if (drawable != null) {
							// if (decernBitmpaL(drawable))
							if (lrb.getLevel() % 2 == 0)
								lrb.setLeft(true);
							lrbs.put(lrb, drawable);
						}
						// 图片加载完则更新视图
						if (alLoadIvCount == totalNum)
							initRLRView();
					}
				});
	}

	/**
	 * 更新已经加载好的图片
	 */
	private void initRLRView() {
		index = 0;
		Set<LRBitmap> itr = lrbs.keySet();
		leftDrawables.clear();
		rightDrawables.clear();
		LRBitmap[] lRBitmaps = new LRBitmap[itr.size()];
		Iterator<LRBitmap> iterator = itr.iterator();
		for (int i = 0; i < lRBitmaps.length; i++) {
			lRBitmaps[i] = iterator.next();
		}
		bubbleSort(lRBitmaps);
		for (int i = 0; i < lRBitmaps.length; i++) {
			LRBitmap lrKey = lRBitmaps[i];
			Drawable dra = lrbs.get(lrKey);
			if (lrKey.isLeft())
				leftDrawables.add(dra);
			else
				rightDrawables.add(dra);
		}
		initLRView(rightDrawables, rightImageView, 0);
		initLRView(leftDrawables, leftImageView, 0);
	}

	// 冒泡排序, pnData要排序的数据， nLen数据的个数
	private int bubbleSort(LRBitmap[] lRBitmaps) {
		boolean isOk = false; // 设置排序是否结束的哨兵

		// i从[0,nLen-1)开始冒泡，确定第i个元素
		for (int i = 0; i < lRBitmaps.length - 1 && !isOk; ++i) {
			isOk = true; // 假定排序成功

			// 从[nLen - 1, i）检查是否比上面一个小，把小的冒泡浮上去
			for (int j = lRBitmaps.length - 1; j > i; --j) {
				if (lRBitmaps[j].getLevel() < lRBitmaps[j - 1].getLevel()) // 如果下面的比上面小，交换
				{
					LRBitmap nTemp = lRBitmaps[j];
					lRBitmaps[j] = lRBitmaps[j - 1];
					lRBitmaps[j - 1] = nTemp;
					isOk = false;
				}
			}
		}
		return 1;
	}

	// 向上操作
	private final static int upper = 1;
	// 向下操作
	private final static int downward = 2;

	/**
	 * 根据页码更新左右两方的视图
	 */
	private void updateViewByIndex(int operationKey) {
		switch (operationKey) {
		case upper:
			upAnimation(leftre);
			upAnimation(righttre);
			break;
		case downward:
			downAnimation(leftre);
			downAnimation(righttre);
			break;
		}
		boolean isChangeIndex = false;
		if ((index + 1) < (leftDrawables.size() % 3 == 0 ? leftDrawables.size() / 3
				: leftDrawables.size() / 3 + 1)
				|| (operationKey == downward && index >= 0)) {
			isChangeIndex = true;
			switch (operationKey) {
			case upper:
				index++;
				break;
			case downward:
				index--;
				break;
			}
			initLRView(leftDrawables, leftImageView, index);
			initLRView(rightDrawables, rightImageView, index);
		}
	}

	private PopupWindow popWin;

	private void alertIsSurePop(OnClickListener clickListener) {
		try{
		View popView = this.getLayoutInflater().inflate(
				R.layout.phone_study_issure, null);
		popView.findViewById(R.id.phone_study_issure_sure_btn)
				.setOnClickListener(clickListener);
		popView.findViewById(R.id.phone_study_issure_cancel_btn)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						popWin.dismiss();
					}
				});
		((TextView) popView.findViewById(R.id.issure_title))
				.setText("是否继续下载课件？");
		popWin = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER,
				0, 0);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
