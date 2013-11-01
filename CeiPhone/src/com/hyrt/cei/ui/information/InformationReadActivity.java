package com.hyrt.cei.ui.information;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.common.GetpasswordActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;

public class InformationReadActivity extends ContainerActivity implements
		OnClickListener {
	private WebView view;
	private String htmlHade = MyTools.newsHtml;
	private String adress;
	private Intent intent;
	public static String MODEL_NAME;
	private ImageButton shoucang;
	private int collect;
	private String loginName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_read);
		MODEL_NAME = ((CeiApplication) getApplication()).nowStart;// 获取当前业务名称。
		((TextView) findViewById(R.id.back)).setText(MODEL_NAME);
		view = (WebView) findViewById(R.id.inf_web);
		view.getSettings().setDefaultTextEncodingName("gbk");
		WebSettings webSettings = view.getSettings();
		webSettings.setJavaScriptEnabled(true);
		view.setInitialScale(100);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		view.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				view.loadUrl(url);// 载入网页
				return true;
			}// 重写点击动作,用webview载入
		});
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		if (loginName.equals("")) {
			findViewById(R.id.zjzx_shoucang).setVisibility(View.GONE);
		}
		intent = getIntent();
		adress = htmlHade + intent.getStringExtra("extra");
		collect = 0;
		if (((CeiApplication) getApplication()).isNet()) {
			view.loadUrl(adress);
		} else {
			view.loadDataWithBaseURL("about:blank", "现在是离线状态，请链接网络后获取数据！",
					"text/html", "utf-8", null);
		}
		shoucang = (ImageButton) findViewById(R.id.zjzx_shoucang);
		shoucang.setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
	}

	protected void onPause() {
		super.onPause();
		InformationReadActivity.this.finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			InformationReadActivity.this.finish();
			intent = new Intent(InformationReadActivity.this,
					InformationActivity.class);
			startActivity(intent);
			break;
		case R.id.zjzx_shoucang:
			if (collect == 1) {
				Toast.makeText(InformationReadActivity.this, "已收藏本条资讯",
						Toast.LENGTH_LONG).show();
			} else {
				AlertDialog alertDialog = new AlertDialog.Builder(this)
						.setTitle("收藏")
						.setMessage("您确定收藏本条资讯吗？")
						.setIcon(R.drawable.icon)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										new Thread(new Runnable() {
											@Override
											public void run() {
												Service.saveCoolect(
														((CeiApplication) getApplication()).columnEntry
																.getUserId(),
														intent.getStringExtra("extra"),
														intent.getStringExtra("functionId"));
												collect = 1;
											}
										}).start();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).create();
				alertDialog.show();
			}
			break;
		}
	}
}
