package com.hyrt.cei.ui.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.hyrt.cei.R;
import com.hyrt.cei.adapter.AnnouncementListAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.common.WebViewUtil;
import com.hyrt.cei.ui.ebook.BaseActivity;
import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.witsea.WitSeaActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.AnnouncementNews;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.webservice.service.Service;

/**
 * 通知公告
 * 
 * @author Administrator
 * 
 */
public class Announcement extends BaseActivity implements OnClickListener {
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private String htmlHade = MyTools.noticeHtml;
	private ListView list;
	private List<AnnouncementNews> announcementNews;
	private Intent i;
	private WebView view;
	private String loginName;
	private boolean isload;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		init();
		view = (WebView) findViewById(R.id.tzgg_web);
		view.getSettings().setDefaultTextEncodingName("gbk");
		WebSettings webSettings = view.getSettings();
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		view.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				view.loadUrl(url);// 载入网页
				return true;
			}// 重写点击动作,用webview载入
		});
		SharedPreferences settings1 = getSharedPreferences("announcementCount",
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings1.edit();
		editor.putInt("old", settings1.getInt("new", 0));
		editor.commit();
	}

	public void init() {
//		findViewById(R.id.home).setOnClickListener(this);
//		findViewById(R.id.home_witsea).setOnClickListener(this);
//		findViewById(R.id.home_ceinet).setOnClickListener(this);
//		findViewById(R.id.home_personcenter).setOnClickListener(this);
//		findViewById(R.id.home_disclaimer).setOnClickListener(this);
		list = (ListView) findViewById(R.id.tzgg_list);
		refreshListData();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				for (int i = 0; i < list.getChildCount(); i++) {
					if (i == position)
						list.getChildAt(i).setBackgroundColor(Color.WHITE);
					else
						list.getChildAt(i).setBackgroundDrawable(null);
				}
				view.loadUrl(htmlHade + announcementNews.get(position).getId());
			}
		});
		
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			AnnouncementListAdapter adapter = new AnnouncementListAdapter(
					Announcement.this, R.layout.tzgg_list_item,
					announcementNews);
			list.setAdapter(adapter);
			if(!isload)
				view.loadUrl("file:///android_asset/load.html");
			isload = true;
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if (announcementNews.size() > 0) {
						view.loadUrl(htmlHade + announcementNews.get(0).getId());
					}
				}
			}, 1000);
			
		}
	};

	private void refreshListData() {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				announcementNews = new ArrayList<AnnouncementNews>();
				String rs = "";
				ColumnEntry columnEntry = ((CeiApplication) getApplication()).columnEntry;
				rs = Service.queryNotice(columnEntry.getUserId());
				try {
					announcementNews = XmlUtil.getAnnouncement(rs);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Message msg = newsHandler.obtainMessage();
				newsHandler.sendMessage(msg);
			}
		});
	}

	protected void onPause() {
		super.onPause();
		Announcement.this.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home:
			Announcement.this.finish();
			break;
		case R.id.home_witsea:
			i = new Intent(Announcement.this, WitSeaActivity.class);
			if (!loginName.equals(""))
				startActivity(i);
			break;
		case R.id.home_ceinet:
			i = new Intent(Announcement.this, WebViewUtil.class);
			i.putExtra("path", "http://mob.cei.gov.cn/");
			startActivity(i);
			break;
		case R.id.home_personcenter:
			i = new Intent(Announcement.this, PersonCenter.class);
			if (!loginName.equals(""))
				startActivity(i);
			break;
		case R.id.home_disclaimer:
			i = new Intent(Announcement.this, Disclaimer.class);
			startActivity(i);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		executorService.shutdown();
		super.onDestroy();
	}

}
