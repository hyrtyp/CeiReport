package com.hyrt.ceiphone.common;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.common.LoginActivity;
import com.hyrt.cei.ui.econdata.EconDataMain;
import com.hyrt.cei.ui.information.InformationActivity;
import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.witsea.WitSeaActivity;
import com.hyrt.cei.update.UpdateManager;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.AnnouncementNews;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Preload;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.adapter.GridViewAdapter;
import com.hyrt.ceiphone.phonestudy.FoundationActivity;
import com.hyrt.ceiphone.phonestudy.PhoneStudyActivity;
import com.hyrt.ceiphone.phonestudy.PreloadActivity;
import com.hyrt.readreport.ReadReportMainActivity;

public class HomePageDZB extends ContainerActivity implements OnClickListener,
		OnItemClickListener {
	// 首页背景
	public RelativeLayout home_page_re;
	private String loginName;
	// 通用页面管理集合
	public static List<Activity> commonActivities = new ArrayList<Activity>();
	// 消息通告的集合
	private List<AnnouncementNews> announcementNews;
	// 消息数量
	private int announcementCount;
	// 全局的菜单集合
	private ColumnEntry columnEntry;
	// 4大业务模块
	public static final String[] MODELS = { "移动学习", "政经资讯", "经济数据", "研究报告" };
	// 消息更新的数量textview
	private TextView textNum;
	private Intent intent;
	// 一级业务的数据
	private List<ColumnEntry> columnEntries;
	private AsyncImageLoader asyncImageLoader;
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
				List<Preload> preloads = ((CeiApplication) getApplication()).dataHelper.getPreloadList();
				for(int i=0;i<preloads.size();i++)
					if(preloads.get(i).getLoadFinish()!=1)
						isDowning = true;
				if(isDowning)
				alertIsSurePop(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						popWin.dismiss();
						Intent intent = new Intent(HomePageDZB.this,PreloadActivity.class);
						startActivity(intent);
					}
				});
			}
		}, 200);*/
		UpdateManager manager = new UpdateManager(HomePageDZB.this);
		// 检查软件更新
		manager.isUpdate();
		findViewById(R.id.main_login).setOnClickListener(this);
		textNum = (TextView) findViewById(R.id.home_page_main_num);
		LinearLayout bottomsLl = (LinearLayout) findViewById(R.id.bottoms_Ll);
		for (int i = 0; i < bottomsLl.getChildCount(); i++) {
			((RelativeLayout) (bottomsLl.getChildAt(i))).getChildAt(0)
					.setOnClickListener(this);
		}
		refreshListData();
		showLoginBtnByUserName();
		installMainGridView();
	}

	// 根据登陆与否判断是否显示登陆按钮
	private void showLoginBtnByUserName() {
		// 获取登陆名
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		// 根据登录名来显示登陆按钮那个位置
		if (!loginName.equals("")) {
			((TextView) findViewById(R.id.main_login_tv))
					.setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.main_login_tv)).setText(loginName
					+ "欢迎您！");
			findViewById(R.id.main_login).setVisibility(View.GONE);
		}
	}

	// 更新消息提示
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

	// 更新消息的数量
	private void refreshListData() {
		new Thread(new Runnable() {
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
		}).start();
	}

	/**
	 * 初始化主业务的gridview控件
	 */
	private void installMainGridView() {
		asyncImageLoader = ((CeiApplication) (this.getApplication())).asyncImageLoader;
		// 获取一级业务的数据
		columnEntry = ((CeiApplication) getApplication()).columnEntry;
		columnEntries = new ArrayList<ColumnEntry>();
		List<ColumnEntry> firstColumnEntries = columnEntry
				.getEntryChildsForParent(null);
		this.columnEntry.getWitSeaColumns().clear();
		for (int i = 0; i < firstColumnEntries.size(); i++) {
			boolean isModels = false;
			ColumnEntry columnEntry = firstColumnEntries.get(i);
			for (int j = 0; j < MODELS.length; j++) {
				if ((MODELS[j]).equals(columnEntry.getName())) {
					isModels = true;
					columnEntries.add(columnEntry);
				}
			}
			if (!isModels && !columnEntry.getName().equals("关于我们")){
				System.out.println(columnEntry.getName() + " : " +columnEntry.getOperationImage());
				this.columnEntry.getWitSeaColumns().add(columnEntry);
			}
				
		}
		columnEntries.addAll(columnEntry.getSelectColumnEntryChilds());
		GridView gridView = (GridView) findViewById(R.id.main_service_gridview);
		GridViewAdapter gaAdapter = new GridViewAdapter(this,
				R.layout.main_griview_item, columnEntries, gridView);
		gridView.setAdapter(gaAdapter);
		gridView.setOnItemClickListener(this);
		home_page_re = (RelativeLayout) findViewById(R.id.welcome);
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_login:
			// 根据登录名来显示登陆按钮那个位置
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.main_rl:
			// 跳转到主页
			break;
		case R.id.notice_rl:
			intent = new Intent(this, Announcement.class);
			if (!loginName.equals("")) {
				startActivity(intent);
				textNum.setVisibility(View.GONE);
			} else {
				Toast.makeText(this, "请登录后查看！", Toast.LENGTH_SHORT).show();
			}
			// 跳转到通知公告页
			break;
		case R.id.collect_rl:
			// 跳转到应用收藏页
			intent = new Intent(this, WitSeaActivity.class);
			if (!loginName.equals("")) {
				startActivity(intent);
			} else {
				Toast.makeText(this, "请登录后查看！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.psc_rl:
			intent = new Intent(this, PersonCenter.class);
			if (!loginName.equals("")) {
				startActivity(intent);
			} else {
				Toast.makeText(this, "请登录后查看！", Toast.LENGTH_SHORT).show();
			}
			// 跳转到个人中心页
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String serviceName = columnEntries.get(position).getName();
		addLog(serviceName);
		((CeiApplication)(getApplication())).nowStart=serviceName;
		if (serviceName.equals("移动学习")||serviceName.contains("学习")) {
			FoundationActivity.MODEL_NAME = serviceName;
			// 跳转到移动学习页面
			Intent intent = new Intent(this, PhoneStudyActivity.class);
			startActivity(intent);
		} else if (serviceName.equals("政经资讯")||serviceName.contains("资讯")) {
			Intent intent = new Intent(this, InformationActivity.class);
			startActivity(intent);
			// 跳转到政经资讯页面
		} else if (serviceName.equals("经济数据")||serviceName.contains("数据")) {
			Intent intent = new Intent(this, EconDataMain.class);
			startActivity(intent);
			// 跳转到经济数据页面
		} else if (serviceName.equals("研究报告")||serviceName.contains("报告")) {
			intent = new Intent(this, ReadReportMainActivity.class);
			startActivity(intent);
			// 跳转到研究报告页面
		}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "服务平台");
		menu.add(0, 2, 2, "关于我们");
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 1) {
			intent = new Intent(HomePageDZB.this, WebViewUtil.class);
			intent.putExtra("path", "http://mob.cei.gov.cn/");
			startActivity(intent);
		} else if (item.getItemId() == 2) {
			intent = new Intent(this, Disclaimer.class);
			//if (!loginName.equals("")) {
			startActivity(intent);
			//} else {
				//Toast.makeText(this, "请登录后查看！", Toast.LENGTH_SHORT).show();
			//}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			destroyActivities();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private PopupWindow popWin;

	private void alertIsSurePop(OnClickListener clickListener) {
		View popView = this.getLayoutInflater().inflate(
				R.layout.phone_study_issure, null);
		popView.findViewById(R.id.phone_study_issure_sure_btn).setOnClickListener(clickListener);
		popView.findViewById(R.id.phone_study_issure_cancel_btn)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						popWin.dismiss();
					}
				});
		((TextView)popView.findViewById(R.id.issure_title)).setText("是否继续下载课件？");
		popWin = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(this.getWindow().getDecorView(),
				Gravity.CENTER, 0, 0);
	}

}
