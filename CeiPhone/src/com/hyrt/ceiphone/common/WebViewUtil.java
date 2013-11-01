package com.hyrt.ceiphone.common;

import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.predownload.DownloadThreadManager;
import com.hyrt.cei.util.EncryptDecryption;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.Preload;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.phonestudy.FoundationActivity;
import com.hyrt.ceiphone.phonestudy.PreloadActivity;

public class WebViewUtil extends ContainerActivity {

	private WebView webView;
	private String path;
	private String classId;
	// 课件入口文件名称
	private Courseware courseware;
	private int isCreated = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		webView = (WebView) findViewById(R.id.web_view);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		//webSettings.setSupportZoom(true);
		webSettings.setPluginState(PluginState.ON);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webView.setWebViewClient(new WebViewClient() {

			public boolean shouldOverrideUrlLoading(final WebView view,
					final String url) {
				webView.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

				new Handler().post(new Runnable() {

					@Override
					public void run() {
						try {
							findViewById(R.id.webview_progress).setVisibility(
									View.GONE);
							findViewById(R.id.web_view)
									.setVisibility(View.GONE);
							MyTools.exitShow(WebViewUtil.this, WebViewUtil.this
									.getWindow().getDecorView(), "此资源无法正常查看..");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				});

				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		path = getIntent().getStringExtra("path");
		// 如果是读本地课件的话
		courseware = (Courseware) getIntent().getSerializableExtra("class");
		if (courseware == null) {
			classId = getIntent().getStringExtra("classId");
		} else {
			classId = courseware.getClassId();
		}
		DataHelper dataHelper = ((CeiApplication) (WebViewUtil.this
				.getApplication())).dataHelper;
		if (!path.contains("ftp") && !path.contains("file:///")) {
			if (!((CeiApplication) this.getApplication()).isNet()) {
				MyTools.popExitActivity(this);
				return;
			}
			new AdressLoadTask().execute((Void) null);
		} else if (classId != null && !classId.equals("") && path != null
				&& path.contains("file:///")) {
			DownloadThreadManager.clearThread();
			Courseware cw = new Courseware();
			cw.setClassId(classId);
			webView.requestFocus();
			webView.requestFocusFromTouch();
			courseware = cw;
			findViewById(R.id.webview_progress).setVisibility(View.VISIBLE);
			findViewById(R.id.web_view).setVisibility(View.GONE);
			loadLocalVideoByGetKey();
			webView.setWebChromeClient(new MyWebChromeClient());
		} else if (dataHelper.getPreload(classId) != null
				&& dataHelper.getPreload(classId).getLoadFinish() == 1) {
			DownloadThreadManager.clearThread();
			Preload preload = dataHelper.getPreload(classId);
			path = "file:///"
					+ preload.getLoadLocalPath().replace(
							PreloadActivity.FLASH_POSTFIX,
							PreloadActivity.FLASH_GATE);
			webView.requestFocus();
			webView.requestFocusFromTouch();
			loadLocalVideoByGetKey();
			webView.setWebChromeClient(new MyWebChromeClient());
		} else if (classId != null && !classId.equals("") && path != null
				&& path.contains("http://")) {
			DownloadThreadManager.clearThread();
			if (!((CeiApplication) this.getApplication()).isNet()) {
				MyTools.popExitActivity(this);
				return;
			}
			if (dataHelper.getStudyRecord(courseware)) {
				path += "?userid="
						+ ((CeiApplication) (this.getApplication())).columnEntry
								.getUserId() + "&classid=" + classId
						+ "&native=0" + "&location="
						+ courseware.getTimePoint() + "&totaltime="
						+ courseware.getStudyTime();
			} else {
				path += "?userid="
						+ ((CeiApplication) (this.getApplication())).columnEntry
								.getUserId() + "&classid=" + classId
						+ "&native=0" + "&location=0" + "&totaltime=0";
			}
			validStatusCode(path.replace("/apad.html",
					FoundationActivity.FLASH_GATE));
		}
		
	}

	private void validStatusCode(final String url) {
		findViewById(R.id.webview_progress).setVisibility(View.VISIBLE);
		findViewById(R.id.web_view).setVisibility(View.GONE);
		final Handler handler = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				findViewById(R.id.webview_progress).setVisibility(View.GONE);
				switch (msg.arg1) {
				case 1:
					findViewById(R.id.web_view).setVisibility(View.GONE);
					new Handler().post(new Runnable() {

						@Override
						public void run() {
							MyTools.exitShow(WebViewUtil.this, WebViewUtil.this
									.getWindow().getDecorView(), "无法查看,资源不存在..");
						}
					});
					break;
				case 2:
					findViewById(R.id.web_view).setVisibility(View.VISIBLE);
					webView.loadUrl(path.replace("/apad.html",
							FoundationActivity.FLASH_GATE));
					webView.requestFocus();
					webView.requestFocusFromTouch();
					webView.setWebChromeClient(new MyWebChromeClient());
					break;
				}

				super.dispatchMessage(msg);
			}
		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					HttpURLConnection.setFollowRedirects(false);
					URL validatedURL = new URL(url);
					HttpURLConnection con = (HttpURLConnection) validatedURL
							.openConnection();
					con.setRequestMethod("HEAD");
					int responseCode = con.getResponseCode();
					if (responseCode == 404 || responseCode == 405
							|| responseCode == 504) {
						Message msg = handler.obtainMessage();
						msg.arg1 = 1;
						handler.sendMessage(msg);
					} else {
						Message msg = handler.obtainMessage();
						msg.arg1 = 2;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	// 解密后载入页面
	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			DataHelper dataHelper = ((CeiApplication) (WebViewUtil.this
					.getApplication())).dataHelper;
			if (dataHelper.getStudyRecord(courseware)) {
				path += "?userid="
						+ ((CeiApplication) (WebViewUtil.this.getApplication())).columnEntry
								.getUserId() + "&classid=" + classId
						+ "&native=1" + "&location="
						+ courseware.getTimePoint() + "&totaltime="
						+ courseware.getStudyTime();
			} else {
				path += "?userid="
						+ ((CeiApplication) (WebViewUtil.this.getApplication())).columnEntry
								.getUserId() + "&classid=" + classId
						+ "&native=1" + "&location=0" + "&totaltime=0";
			}
			findViewById(R.id.webview_progress).setVisibility(View.GONE);
			findViewById(R.id.web_view).setVisibility(View.VISIBLE);
			System.out.println(path);
			webView.loadUrl(path.replace("/apad.html",
					FoundationActivity.FLASH_GATE));
		}
	};

	/**
	 * 获取解密码后加载本地视频
	 */
	private void loadLocalVideoByGetKey() {
		// 根据课件id，获取密钥并解密
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 从数据库获取密钥
				DataHelper dataHelper = ((CeiApplication) (WebViewUtil.this
						.getApplication())).dataHelper;
				Preload preload = dataHelper.getPreload(classId);
				if (preload != null) {
					try {
						EncryptDecryption.DecryptionCourseware(
								path.replace(FoundationActivity.FLASH_GATE, "")
										.replace("file:////", "")
										.replace("/apad.html", ""),
								preload.getPassKey());
					} catch (Exception e) {
						e.printStackTrace();
					}
					handler.sendMessage(handler.obtainMessage());
				}
			}
		}).start();
	}

	final class MyWebChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			Toast.makeText(WebViewUtil.this, message,Toast.LENGTH_LONG).show();
			// 让浏览器弹出框包含cei_location，并把学习记录保存在数据库里.
			try {
				if (courseware.getStudyTime() == null)
					courseware.setStudyTime("0");
				if (courseware.getTimePoint() == null)
					courseware.setTimePoint("0");
				int studyTime = 0;
				String studyTimeStr = message.split(":")[1];
				if (studyTimeStr.contains(".")) {
					studyTime = Integer.parseInt(studyTimeStr.substring(0,
							studyTimeStr.lastIndexOf(".")));
				} else {
					studyTime = Integer.parseInt(studyTimeStr);
				}
				courseware.setStudyTime(studyTime + "");
				courseware.setTimePoint(message.split(":")[0]);
				if (!path.contains("http://")) {
					courseware.setUploadTime(studyTime
							+ courseware.getUploadTime());
					String str = WebViewUtil.this.getIntent().getStringExtra(
							"bdclass");
					if (str != null && str.contains("讲师姓名")
							&& str.contains("发布时间") && str.contains("|")) {
						courseware.setName(str.substring(str.indexOf("|") + 1));
						courseware.setTeacherName(str.substring(
								str.indexOf("讲师姓名") + 7, str.indexOf("发布时间")));
						courseware.setProTime(str.substring(
								str.indexOf("发布时间") + 7, str.indexOf("|")));
						courseware.setClassLength(getIntent().getStringExtra(
								"classLength"));
					}
					if ("-1".equals(courseware.getTimePoint())) {
						courseware.setIscompleted("1");
					}
					courseware.setOrderTime(500);
				} else {
					courseware.setUploadTime(0);
				}
				CeiApplication ceiApplication = (CeiApplication) (WebViewUtil.this
						.getApplication());
				// 去学习记录的数据库里查询并更新相应的课件学习时间
				ceiApplication.dataHelper.insertOrUpdateStudyRecord(courseware);
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.confirm();
			return true;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& getIntent().getBooleanExtra("isRecord", false)) {
					webView.loadUrl("javascript:saveTimePointForClass()");
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// 对课件进行再次加密
		webView.destroy();
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (path != null && path.contains("file://")) {
					try {
						EncryptDecryption.EncryptCourseware(path
								.replace(PreloadActivity.FLASH_GATE, "")
								.replace("file:////", "")
								.substring(
										0,
										path.replace(
												PreloadActivity.FLASH_GATE, "")
												.replace("file:////", "")
												.indexOf("?")));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}).start();

		super.onDestroy();
	}

	private PowerManager.WakeLock wakeLock = null;

	// 获取锁
	public void acquireWakeLock(Context context) {
		if (wakeLock == null) {
			PowerManager powerManager = (PowerManager) (context
					.getSystemService(Context.POWER_SERVICE));
			wakeLock = powerManager.newWakeLock(
					PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
			wakeLock.acquire();
		}
	}

	// 释放锁
	public void releaseWakeLock() {
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}
	}

	@Override
	public void onResume() {
		acquireWakeLock(this);
		isCreated++;
		if(isCreated >1){
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				webView.loadUrl("javascript:resizeErrorScreen()");
			}
		}, 3000);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				webView.loadUrl("javascript:hidePanelForScreen()");
			}
		}, 4000);
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		webView.loadUrl("javascript:setVideoPause()");
		releaseWakeLock();
		super.onPause();
	}

	private String result;

	public class AdressLoadTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				result = Service
						.queryFunctionAddress(((CeiApplication) WebViewUtil.this
								.getApplication()).columnEntry
								.getColumnEntryChilds().get(0).getId());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				path = XmlUtil.parseAddress(result);
				if (path.equals("-1")) {
					MyTools.exitShow(WebViewUtil.this, WebViewUtil.this
							.getWindow().getDecorView(), "此版本下无门户网站！");
					return;
				}
				webView.loadUrl(path);
				webView.requestFocus();
				webView.requestFocusFromTouch();
			}
		}
	}

}
