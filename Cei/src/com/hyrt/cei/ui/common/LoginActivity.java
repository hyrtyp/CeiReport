package com.hyrt.cei.ui.common;

import com.hyrt.cei.R;
import com.hyrt.cei.dzb.ui.HomePageDZB;

import com.hyrt.cei.ui.main.Announcement;
import com.hyrt.cei.ui.main.Disclaimer;
import com.hyrt.cei.ui.main.Welcome;
import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.witsea.WitSeaActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登录界面
 * 
 */
public class LoginActivity extends Activity  implements OnClickListener{

	private EditText accountEt;
	private EditText passwordEt;
	// 用户名
	private String loginName;
	
	@Override
	protected void onDestroy() {
		HomePageDZB.commonActivities.remove(this);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_login);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		for (int i = 0; i < HomePageDZB.commonActivities.size(); i++) {
			try {
				LoginActivity isLoginActivity = (LoginActivity) ( HomePageDZB.commonActivities.get(i));
				isLoginActivity.finish();
			} catch (Exception e) {
			}
		}
		HomePageDZB.commonActivities.add(this);
		accountEt = (EditText) findViewById(R.id.ui_login_username);
		passwordEt = (EditText) findViewById(R.id.ui_login_password);
		SharedPreferences settings = getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		accountEt.setText(settings.getString("LOGINNAME", ""));
		passwordEt.setText(settings.getString("PASSWORD", ""));

		findViewById(R.id.ui_login_regist).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(LoginActivity.this,
								RegistActivity.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.ui_getpassword).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(LoginActivity.this,
								GetpasswordActivity.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.ui_login_login).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						SharedPreferences settings = getSharedPreferences(
								"loginInfo", Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = settings.edit();
						if (accountEt.getText().toString().trim().equals("")
								|| passwordEt.getText().toString().trim()
										.equals("")) {
							Toast.makeText(LoginActivity.this, "用户名密码不能为空!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						LoginActivity.this.finish();
						editor.putString("LOGINNAME", accountEt.getText().toString().trim());
						editor.putString("PASSWORD", passwordEt.getText().toString().trim());
						editor.commit();
						Intent intent = new Intent(LoginActivity.this,
								Welcome.class);
						startActivity(intent);
					}
				});
		 registBottomEvent();
	}

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
			LoginActivity.this.finish();
			break;
		}
	}
	
	private void registBottomEvent(){
		findViewById(R.id.home_announcement).setOnClickListener(this);
		findViewById(R.id.home_witsea).setOnClickListener(this);
		findViewById(R.id.home_ceinet).setOnClickListener(this);
		findViewById(R.id.home_personcenter).setOnClickListener(this);
		findViewById(R.id.home_disclaimer).setOnClickListener(this);
		findViewById(R.id.back_back).setOnClickListener(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LoginActivity.this.finish();
	}
}
