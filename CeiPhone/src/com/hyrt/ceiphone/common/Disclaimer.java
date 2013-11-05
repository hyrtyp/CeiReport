package com.hyrt.ceiphone.common;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.witsea.WitSeaActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;

/**
 * 关于我们
 * 
 * @author Administrator
 * 
 */
public class Disclaimer extends ContainerActivity implements OnClickListener {
	private Intent intent;
	private ColumnEntry columnEntry;
	private String rs;
	public static final String MODEL_NAME = "关于我们";
	private List<InfoNew> news = new ArrayList<InfoNew>();
	private WebView view;
	private String htmlHade = MyTools.newsHtml;
	private String loginName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.disclaimer);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		init();
		view = (WebView) findViewById(R.id.disclaimer_web);
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
	}

	private void init() {
//		LinearLayout bottomsLl = (LinearLayout) findViewById(R.id.bottoms_Ll);
//		for (int i = 0; i < bottomsLl.getChildCount(); i++) {
//			((RelativeLayout) (bottomsLl.getChildAt(i))).getChildAt(0)
//					.setOnClickListener(this);
//		}
		findViewById(R.id.abouts_us).setOnClickListener(this);
		findViewById(R.id.usesinfo).setOnClickListener(this);
		findViewById(R.id.mazminfo).setOnClickListener(this);
		refreshListData();
	}

	private void refreshListData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				columnEntry = ((CeiApplication) getApplication()).columnEntry;
				if (columnEntry.getColByName(Disclaimer.MODEL_NAME) == null)
					return;
				rs = Service
						.queryNewsByFunctionId(
								columnEntry.getColByName(Disclaimer.MODEL_NAME)
										.getId(), "", columnEntry.getUserId());
				XmlUtil.getNewsList(rs, news);
				news.size();
				Message msg = newsHandler.obtainMessage();
				newsHandler.sendMessage(msg);
			}
		}).start();
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (news.size() >= 3) {
				for (int i = 0; i < news.size(); i++) {
					if (news.get(i).getTitle().endsWith(MODEL_NAME)) {
						view.loadUrl(htmlHade + news.get(i).getId());
					}
				}
			}
		}
	};

	protected void onPause() {
		super.onPause();
		Disclaimer.this.finish();
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
			if (!loginName.equals("")) {
				startActivity(intent);
			} else {
				Toast.makeText(this, "请登录后查看！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.collect_rl:
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
			break;
		case R.id.abouts_us:
			findViewById(R.id.abouts_us).setBackgroundResource(
					R.drawable.gywm_1_0);
			findViewById(R.id.usesinfo).setBackgroundResource(
					R.drawable.gywm_2_1);
			findViewById(R.id.mazminfo).setBackgroundResource(
					R.drawable.gywm_3_1);
			if (news.size() >= 3) {
				for (int i = 0; i < news.size(); i++) {
					if (news.get(i).getTitle().endsWith(MODEL_NAME)) {
						view.loadUrl(htmlHade + news.get(i).getId());
					}
				}
			}
			break;
		case R.id.usesinfo:
			findViewById(R.id.abouts_us).setBackgroundResource(
					R.drawable.gywm_1_1);
			findViewById(R.id.usesinfo).setBackgroundResource(
					R.drawable.gywm_2_0);
			findViewById(R.id.mazminfo).setBackgroundResource(
					R.drawable.gywm_3_1);
			if (news.size() >= 3) {
				for (int i = 0; i < news.size(); i++) {
					if (news.get(i).getTitle().endsWith("使用说明")) {
						view.loadUrl(htmlHade + news.get(i).getId());
					}
				}
			}
			break;
		case R.id.mazminfo:
			findViewById(R.id.abouts_us).setBackgroundResource(
					R.drawable.gywm_1_1);
			findViewById(R.id.usesinfo).setBackgroundResource(
					R.drawable.gywm_2_1);
			findViewById(R.id.mazminfo).setBackgroundResource(
					R.drawable.gywm_3_0);
			if (news.size() >= 3) {
				for (int i = 0; i < news.size(); i++) {
					if (news.get(i).getTitle().endsWith("免责声明")) {
						view.loadUrl(htmlHade + news.get(i).getId());
					}
				}
			}
			break;
		}
	}
}
