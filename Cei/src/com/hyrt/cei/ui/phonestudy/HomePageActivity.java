package com.hyrt.cei.ui.phonestudy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.PhoneStudyGridAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.ui.phonestudy.view.FlowRelativeyout;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.TimeOutHelper;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * 移动学习首页
 * 
 */
public class HomePageActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private TimeOutHelper timeOutHelper;
	// 用于更新视图的handler
	private Handler handler;
	// 精彩课件，显示位置顶部3D
	private List<Courseware> topCoursewares;
	// 所有课件的列表数据，按照时间排序
	private List<Courseware> newCoursewares;
	// 所有课件的中展示的课件集合
	private List<Courseware> currentCousewares;
	// 所有课件更新视图标志
	private static final int NEWCOURSE_KEY = 1;
	// 精彩课件更新视图标志
	private static final int TOP_KEY = 2;
	// 无网络标志
	private static final int NO_NET = 3;
	// 点击精彩课件图标进详细标志
	public static final int GO_DETAIL = 4;
	// 点击普通课件图标进详细标志
	public static final int GO_COMMON_DETAIL = 5;
	// 移动学习所有页面的容器，便于管理
	public static List<Activity> phoneStudyContainer = new ArrayList<Activity>();
	// 精彩课件3D课件的父级元素，用于3D效果的一些操作
	private FlowRelativeyout flowLayout;
	private static final String WELLCLASS_NAME = "置顶课件";
	public static String MODEL_NAME = "移动学习";
	public static final String PHONE_NAME = "移动课堂";
	public static final String FREE_NAME = "免费课件";
	// 用户加载图片的工具类
	private AsyncImageLoader asyncImageLoader;
	// 选中课件的信息
	private Courseware coursewareInfo;
	// 记录最热课件图片下载数量
	private int x = 0;
	// 所有课件的gridView控件
	private GridView allNewGridView;
	// gridview的适配器，控制分页
	private PhoneStudyGridAdapter bottomGirdViewAdapter;
	// 普通课件的页码
	private int pageIndex = 0;
	// 精彩课件离线文件名称
	private static final String WELLCLASS_FILENAME = "WELL_CLASS.xml";
	// 最新课件离线文件名称
	private static final String NEWCLASS_FILENAME = "NEW_CLASS.xml";
	// 用户名
	private String loginName;
	Map<Drawable, String> drawToPath = new HashMap<Drawable, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_study);
		timeOutHelper = new TimeOutHelper(this);
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		initBottom();
		asyncImageLoader = ((CeiApplication) (getApplication())).asyncImageLoader;
		for (int i = 0; i < HomePageActivity.phoneStudyContainer.size(); i++) {
			try {
				HomePageActivity isHomePageActivity = (HomePageActivity) (HomePageActivity.phoneStudyContainer
						.get(i));
				isHomePageActivity.finish();
			} catch (Exception e) {
			}
		}
		phoneStudyContainer.add(this);
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
		registEvent();
		loadDataForView();
	}

	@Override
	protected void onDestroy() {
		// if(bottomGirdViewAdapter != null)
		// bottomGirdViewAdapter.clearBitmaps();
		phoneStudyContainer.remove(this);
		// if(flowLayout != null)
		// flowLayout.clearBitmaps();
		super.onDestroy();
	}

	private void loadDataForView() {
		handler = new Handler() {
			@Override
			public void dispatchMessage(final Message msg) {
				switch (msg.arg1) {
				case NEWCOURSE_KEY:
					currentCousewares = new ArrayList<Courseware>();
					for (int i = pageIndex * 10; i < (pageIndex + 1) * 10
							&& i < newCoursewares.size(); i++) {
						if (i == newCoursewares.size() - 1)
							findViewById(R.id.phone_study_morebtn)
									.setVisibility(View.GONE);
						currentCousewares.add(newCoursewares.get(i));
					}
					bottomGirdViewAdapter = new PhoneStudyGridAdapter(
							HomePageActivity.this,
							R.layout.phone_study_center_bottom_grid_item,
							currentCousewares, allNewGridView);
					allNewGridView.setAdapter(bottomGirdViewAdapter);
					break;
				case TOP_KEY:
					// 为3D控件注册事件
					flowLayout.registEventForFlowRelativeyout(handler);
					final Drawable[] drawables = new Drawable[6];
					x = 0;
					for (int i = 0; i < topCoursewares.size() && i < 6; i++) {
						final String imageUrl = topCoursewares.get(i)
								.getBigPath();
						ImageResourse imageResource = new ImageResourse();
						imageResource.setIconUrl(imageUrl);
						imageResource.setIconId(topCoursewares.get(i)
								.getClassId());
						imageResource.setType("1");
						asyncImageLoader.loadDrawable(imageResource,
								new AsyncImageLoader.ImageCallback() {

									@Override
									public void imageLoaded(Drawable drawable,
											String path) {
										if (drawable != null && x <= 5) {
											drawables[x] = drawable;
											drawToPath.put(drawable, path);
											x++;
											// 如果图片都加载好了的话，就为图片增加事件
											if (x == 6) {
												flowLayout
														.loadImgForIv(drawables);
											}
										}
									}
								});
					}
					findViewById(R.id.phone_study_progressLl).setVisibility(
							View.GONE);
					break;
				case NO_NET:
					MyTools.exitShow(HomePageActivity.this,
							((Activity) HomePageActivity.this).getWindow()
									.getDecorView(), "sd卡不存在！");
					break;
				case GO_DETAIL:
					Intent intent = new Intent(HomePageActivity.this,
							CourseDetailActivity.class);
					Courseware courseware = null;
					if (topCoursewares.size() >= 6) {
						for (int i = 0; i < topCoursewares.size(); i++) {
							if (topCoursewares
									.get(i)
									.getBigPath()
									.equals(drawToPath
											.get(flowLayout.currentDrawable))) {
								courseware = topCoursewares.get(i);
							}
						}
						intent.putExtra("coursewareInfo", courseware);
						startActivity(intent);
					}
					break;
				case GO_COMMON_DETAIL:
					intent = new Intent(HomePageActivity.this,
							CourseDetailActivity.class);
					intent.putExtra("coursewareInfo", coursewareInfo);
					startActivity(intent);
					break;

				}
			}
		};
		getWellCourses();
		getGridViewData();
	}

	private void registEvent() {
		allNewGridView = (GridView) findViewById(R.id.phone_study_gridview);
		allNewGridView.setOnItemClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.phone_study_refresh).setOnClickListener(this);
		findViewById(R.id.phone_study_search_btn).setOnClickListener(this);
		findViewById(R.id.phone_study_downmanager).setOnClickListener(this);
		findViewById(R.id.phone_study_morebtn).setOnClickListener(this);
	}

	/**
	 * 获取最新课件排序的列表
	 */
	private void getGridViewData() {

		final Runnable runnable = new Runnable() {

			private StringBuilder functionIds;
			private ColumnEntry columnEntry;

			// 初始化请求数据
			private void initSendData() {
				columnEntry = ((CeiApplication) (HomePageActivity.this
						.getApplication())).columnEntry;
				ColumnEntry phoneStudyCol = columnEntry
						.getColByName(MODEL_NAME);
				functionIds = new StringBuilder(phoneStudyCol.getId());
				for (int i = 0; i < columnEntry.getColumnEntryChilds().size(); i++) {
					ColumnEntry entryChild = columnEntry.getColumnEntryChilds()
							.get(i);
					if (entryChild.getPath() != null
							&& entryChild.getPath().contains(
									phoneStudyCol.getId())) {
						functionIds.append("," + entryChild.getId());
					}
				}
			}

			public void run() {
				// timeOutHelper.installTimerTask();
				newCoursewares = new ArrayList<Courseware>();
				if (((CeiApplication) getApplication()).isNet()) {
					initSendData();
					String result = "";
					if (columnEntry.getColumnEntryChilds().size() > 0) {
						result = Service.queryClassByTime(columnEntry
								.getColumnEntryChilds().get(0).getId(),
								functionIds.toString());
						WriteOrRead.write(result, MyTools.nativeData,
								NEWCLASS_FILENAME);
					}
					if (XmlUtil.parseReturnCode(result).equals("")) {
						XmlUtil.parseCoursewares(result, newCoursewares);
						result = Service
								.queryCourse(((CeiApplication) (getApplication())).columnEntry
										.getUserId());
						Map<String, Courseware> coursewares = new HashMap<String, Courseware>();
						List<Courseware> selfselCoursewares = new ArrayList<Courseware>();
						XmlUtil.parseCoursewares(result, selfselCoursewares);
						for (int i = 0; i < newCoursewares.size(); i++) {
							for (int j = 0; j < selfselCoursewares.size(); j++) {
								if (newCoursewares
										.get(i)
										.getClassId()
										.equals(selfselCoursewares.get(j)
												.getClassId())) {
									newCoursewares.get(i).setSelfCourse(true);
								}
							}
							coursewares.put(newCoursewares.get(i).getClassId(),
									newCoursewares.get(i));
						}
						Message msg = handler.obtainMessage();
						msg.arg1 = NEWCOURSE_KEY;
						handler.sendMessage(msg);
					}/*
					 * else { Message msg = handler.obtainMessage(); msg.arg1 =
					 * NO_NET; handler.sendMessage(msg); }
					 */
				} else {
					String result = WriteOrRead.read(MyTools.nativeData,
							NEWCLASS_FILENAME);
					if (XmlUtil.parseReturnCode(result).equals("")) {
						XmlUtil.parseCoursewares(result, newCoursewares);
						Message msg = handler.obtainMessage();
						msg.arg1 = NEWCOURSE_KEY;
						handler.sendMessage(msg);
					}/*
					 * else { Message msg = handler.obtainMessage(); msg.arg1 =
					 * NO_NET; handler.sendMessage(msg); }
					 */
				}
				// timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
			}
		};
		new Thread(runnable).start();
	}

	/**
	 * 获取精彩课件的数据
	 */
	private void getWellCourses() {
		findViewById(R.id.phone_study_progressLl).setVisibility(View.VISIBLE);
		final Runnable runnable = new Runnable() {
			public void run() {
				topCoursewares = new ArrayList<Courseware>();
				ColumnEntry columnEntry = ((CeiApplication) getApplication()).columnEntry;
				if (columnEntry.getColByName(WELLCLASS_NAME, columnEntry
						.getColByName(HomePageActivity.MODEL_NAME).getId()) != null) {
					String wellClassParentId = columnEntry.getColByName(
							WELLCLASS_NAME,
							columnEntry.getColByName(
									HomePageActivity.MODEL_NAME).getId())
							.getId();
					if (((CeiApplication) getApplication()).isNet()) {
						String result = Service.queryPhoneFunctionTree(
								wellClassParentId, "kj");
						if (XmlUtil.parseReturnCode(result).equals("")) {
							XmlUtil.parseCoursewares(result, topCoursewares);
							WriteOrRead.write(result, MyTools.nativeData,
									WELLCLASS_FILENAME);
							Message message = handler.obtainMessage();
							message.arg1 = TOP_KEY;
							handler.sendMessage(message);
						} /*
						 * else { Message message = handler.obtainMessage();
						 * message.arg1 = NO_NET; handler.sendMessage(message);
						 * }
						 */
					} else {
						String result = WriteOrRead.read(MyTools.nativeData,
								WELLCLASS_FILENAME);
						if (XmlUtil.parseReturnCode(result).equals("")) {
							XmlUtil.parseCoursewares(result, topCoursewares);
							Message message = handler.obtainMessage();
							message.arg1 = TOP_KEY;
							handler.sendMessage(message);
						} /*
						 * else { Message message = handler.obtainMessage();
						 * message.arg1 = NO_NET; handler.sendMessage(message);
						 * }
						 */
					}
				}else{
					Message message = handler.obtainMessage();
					message.arg1 = TOP_KEY;
					handler.sendMessage(message);
				}
			};
		};
		new Thread(runnable).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		coursewareInfo = newCoursewares.get(position);
		Message message = handler.obtainMessage();
		message.arg1 = GO_COMMON_DETAIL;
		handler.sendMessage(message);
	}

	private void initBottom() {
		ImageView headIv = (ImageView) findViewById(R.id.phone_study);
		ImageView newIv = (ImageView) findViewById(R.id.phone_study_new);
		ImageView nominateIv = (ImageView) findViewById(R.id.phone_study_nominate);
		ImageView freeIv = (ImageView) findViewById(R.id.phone_study_free);
		ImageView kindIv = (ImageView) findViewById(R.id.phone_study_kind);
		ImageView selfIv = (ImageView) findViewById(R.id.phone_study_self);
		ImageView studyIv = (ImageView) findViewById(R.id.phone_study_study);
		ImageView sayIv = (ImageView) findViewById(R.id.phone_study_say);
		headIv.setOnClickListener(this);
		newIv.setOnClickListener(this);
		nominateIv.setOnClickListener(this);
		freeIv.setOnClickListener(this);
		kindIv.setOnClickListener(this);
		selfIv.setOnClickListener(this);
		studyIv.setOnClickListener(this);
		sayIv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back_btn:
			HomePageActivity.this.finish();
			break;
		case R.id.phone_study_morebtn:
			pageIndex++;
			for (int i = pageIndex * 10; i < (pageIndex + 1) * 10
					&& i < newCoursewares.size(); i++) {
				if (i == newCoursewares.size() - 1)
					findViewById(R.id.phone_study_morebtn).setVisibility(
							View.GONE);
				if (currentCousewares != null)
					currentCousewares.add(newCoursewares.get(i));
			}
			if (bottomGirdViewAdapter != null)
				bottomGirdViewAdapter.notifyDataSetChanged();
			break;
		case R.id.phone_study_downmanager:
			intent = new Intent(HomePageActivity.this, PreloadActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_search_btn:
			intent = new Intent(HomePageActivity.this,
					SearchCourseActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study:
			intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
			break;
		case R.id.phone_study_new:
			/*
			 * intent = new Intent(this, HomePageActivity.class);
			 * startActivity(intent);
			 */
			break;
		case R.id.phone_study_nominate:
			intent = new Intent(this, NominateActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_free:
			intent = new Intent(this, FreeActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_kind:
			intent = new Intent(this, KindsActivity.class);
			startActivity(intent);
			break;
		case R.id.phone_study_self:
			intent = new Intent(this, SelfSelectCourseActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				MyTools.exitShow(HomePageActivity.this,
						((Activity) HomePageActivity.this).getWindow()
								.getDecorView(), "请登陆后查看！");
			break;
		case R.id.phone_study_study:
			intent = new Intent(this, PlayRecordCourseActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				MyTools.exitShow(HomePageActivity.this,
						((Activity) HomePageActivity.this).getWindow()
								.getDecorView(), "请登陆后查看！");
			break;
		case R.id.phone_study_say:
			intent = new Intent(this, SayGroupListActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			else
				MyTools.exitShow(HomePageActivity.this,
						((Activity) HomePageActivity.this).getWindow()
								.getDecorView(), "请登陆后查看！");
			break;
		case R.id.phone_study_refresh:
			intent = new Intent(this, HomePageActivity.class);
			startActivity(intent);
			break;
		}
	}

}
