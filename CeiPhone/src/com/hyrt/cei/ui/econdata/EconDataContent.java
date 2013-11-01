package com.hyrt.cei.ui.econdata;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.MyTools;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import com.hyrt.cei.application.CeiApplication;

/**
 * 内容阅读页
 * 
 * @author tmy
 * 
 */
public class EconDataContent extends ContainerActivity {
	private ImageView img_econ, img_econ_search;
	private WebView web;
	private Context context = EconDataContent.this;
	private CeiApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		application = (CeiApplication) getApplication();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.econ_data_reader);
		initView();
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		System.out.println("设备号" + tm.getDeviceId());
		System.out.println("init-getIntent");
		if (application.isNet()) {
			if (getIntent().getStringExtra("id") != null
					&& !getIntent().getStringExtra("id").equals("")) {
				String url = MyTools.newsHtml;
				String htmlUrl = MyTools.newsHtml
						+ getIntent().getStringExtra("id");
				web.loadUrl(htmlUrl);

			}
		} else {
			web.loadDataWithBaseURL("about:blank", "现在是离线状态，请链接网络后获取数据！",
					"text/html", "utf-8", null);
		}
		/*
		 * if (getIntent().getStringExtra("id") != null &&
		 * !getIntent().getStringExtra("id").equals("")) { String url =
		 * MyTools.newsHtml; String htmlUrl = MyTools.newsHtml +
		 * getIntent().getStringExtra("id"); if (application.isNet()) {
		 * 
		 * web.loadUrl(htmlUrl);
		 * 
		 * } else { web.loadDataWithBaseURL("about:blank",
		 * "现在是离线状态，请链接网络后获取数据！", "text/html", "utf-8", null); } }
		 */

	}

	private void initView() {
		System.out.println("initView");
		img_econ = (ImageView) findViewById(R.id.econ_reaed_top_text1);
		// img_econ_search = (ImageView) findViewById(R.id.econ_main_top_tv2);
		web = (WebView) findViewById(R.id.econ_read_web_content);
		// webview缩放设置
		WebSettings settings = web.getSettings();
		settings.setJavaScriptEnabled(true);// JavaScript可用
		web.setInitialScale(100);// 初始显示比例100%
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		img_econ.setOnClickListener(new clicks());
		// img_econ_search.setOnClickListener(new clicks());

	}

	class clicks implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == img_econ) {
				Intent intent = new Intent(context, EconDataMain.class);
				startActivity(intent);

			}

		}

	}

}
