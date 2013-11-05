package com.hyrt.ceiphone.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hyrt.cei.adapter.AnnouncementListAdapter;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.witsea.WitSeaActivity;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.AnnouncementNews;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;

/**
 * 通知公告
 * 
 * @author Administrator
 * 
 */
public class Announcement extends ContainerActivity implements OnClickListener {
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private ListView list;
	private List<AnnouncementNews> announcementNews;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcement);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		init();
		SharedPreferences settings = getSharedPreferences(
				"announcementCount", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("old", settings.getInt("new", 0));
		editor.commit();
	}

	public void init() {
//		LinearLayout bottomsLl = (LinearLayout) findViewById(R.id.bottoms_Ll);
//		for (int i = 0; i < bottomsLl.getChildCount(); i++) {
//			((RelativeLayout) (bottomsLl.getChildAt(i))).getChildAt(0)
//					.setOnClickListener(this);
//		}
		list = (ListView) findViewById(R.id.tzgg_list);
		refreshListData();
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				intent = new Intent();
				intent.putExtra("extra", announcementNews.get(position).getId());
				intent.setClass(Announcement.this, AnnouncementRead.class);
				Announcement.this.startActivity(intent);
			}
		});
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			AnnouncementListAdapter adapter = new AnnouncementListAdapter(
					Announcement.this, R.layout.tzgg_list_item,
					announcementNews);
			list.setAdapter(adapter);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_rl:
			intent = new Intent(this, HomePageDZB.class);
			startActivity(intent);
			break;
		case R.id.notice_rl:
			intent = new Intent(this, Announcement.class);
			startActivity(intent);
			break;
		case R.id.collect_rl:
			intent = new Intent(this, WitSeaActivity.class);
			startActivity(intent);
			break;
		case R.id.psc_rl:
			intent = new Intent(this, PersonCenter.class);
			startActivity(intent);
			break;
		}
	}

}
