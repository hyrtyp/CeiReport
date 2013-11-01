package com.hyrt.ceiphone.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.witsea.WitSeaActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;

/**
 * 通知公告
 * 
 * @author Administrator
 * 
 */
public class AnnouncementRead extends ContainerActivity implements OnClickListener {
	private WebView view;
	private Intent intent;
	private String url;
	private String htmlHade = MyTools.noticeHtml;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.announcementread);
		overridePendingTransition(R.anim.push_in, R.anim.push_out);
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
		intent = getIntent();
		url = intent.getStringExtra("extra");
		view.loadUrl(htmlHade + url);
		LinearLayout bottomsLl = (LinearLayout) findViewById(R.id.bottoms_Ll);
		for (int i = 0; i < bottomsLl.getChildCount(); i++) {
			((RelativeLayout) (bottomsLl.getChildAt(i))).getChildAt(0)
					.setOnClickListener(this);
		}
	}

	protected void onPause() {
		super.onPause();
		AnnouncementRead.this.finish();
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
