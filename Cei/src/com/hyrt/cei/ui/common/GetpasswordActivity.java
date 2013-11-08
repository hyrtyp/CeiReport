package com.hyrt.cei.ui.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hyrt.cei.R;
import com.hyrt.cei.ui.main.Announcement;
import com.hyrt.cei.ui.main.Disclaimer;
import com.hyrt.cei.ui.main.Welcome;
import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.witsea.WitSeaActivity;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class GetpasswordActivity extends Activity implements OnClickListener {

	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private EditText username, email;
	private String strusername, stremail;
	private int code;
	// 用户名
	private String loginName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_getpassword);
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		registBottomEvent();
		init();
	}

	private void init() {
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		username = (EditText) findViewById(R.id.ui_getpassword_username);
		email = (EditText) findViewById(R.id.ui_getpassword_email);
		findViewById(R.id.ui_regist_regist).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(GetpasswordActivity.this,
								LoginActivity.class);
						startActivity(intent);
					}
				});
		refreshListData();
	}

	private void refreshListData() {
		findViewById(R.id.ui_login_login).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (username.getText().toString().trim().equals("")
								|| email.getText().toString().trim().equals("")) {
							Toast.makeText(GetpasswordActivity.this,
									"用户名或邮箱不能为空!", Toast.LENGTH_SHORT).show();
							return;
						}

						strusername = username.getText().toString().trim();
						stremail = email.getText().toString().trim();
						executorService.submit(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								String rs = "";
								rs = Service.updateUserInfoPassWord(
										strusername, stremail);
								code = Integer.parseInt(XmlUtil
										.parseReturnCode(rs).equals("") ? "0"
										: XmlUtil.parseReturnCode(rs));
								Message msg = newsHandler.obtainMessage();
								newsHandler.sendMessage(msg);
							}
						});
					}
				});
	}

	Handler newsHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (code == 1) {
				Toast.makeText(GetpasswordActivity.this, "密码找回成功，请至邮箱查看!",
						Toast.LENGTH_SHORT).show();
				GetpasswordActivity.this.finish();
			} else if (code == -2) {
				Toast.makeText(GetpasswordActivity.this, "email不存在!",
						Toast.LENGTH_SHORT).show();
			} else if (code == 0) {
				Toast.makeText(GetpasswordActivity.this, "系统错误!",
						Toast.LENGTH_SHORT).show();
			} else if (code == -1) {
				Toast.makeText(GetpasswordActivity.this, "网络错误!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onClick(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.home_announcement:
			intent = new Intent(this, Announcement.class);
			if (!loginName.equals(""))
				startActivity(intent);
			break;
		case R.id.home_witsea:
			intent = new Intent(this, WitSeaActivity.class);
			if (!loginName.equals(""))
				startActivity(intent);
			break;
		case R.id.home_ceinet:
			intent = new Intent(this, WebViewUtil.class);
			intent.putExtra("path", "http://mob.cei.gov.cn/");
			startActivity(intent);
			break;
		case R.id.home_personcenter:
			intent = new Intent(this, PersonCenter.class);
			if (!loginName.equals(""))
				startActivity(intent);
			break;
		case R.id.home_disclaimer:
			intent = new Intent(this, Disclaimer.class);
			startActivity(intent);
			break;
		case R.id.back_back:
			GetpasswordActivity.this.finish();
			break;
		}
	}

	private void registBottomEvent() {
//		findViewById(R.id.home_announcement).setOnClickListener(this);
//		findViewById(R.id.home_witsea).setOnClickListener(this);
//		findViewById(R.id.home_ceinet).setOnClickListener(this);
//		findViewById(R.id.home_personcenter).setOnClickListener(this);
//		findViewById(R.id.home_disclaimer).setOnClickListener(this);
		findViewById(R.id.back_back).setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		GetpasswordActivity.this.finish();
	}
}
