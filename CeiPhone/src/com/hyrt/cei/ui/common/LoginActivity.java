package com.hyrt.cei.ui.common;

import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.WelcomeActivity;
import com.hyrt.ceiphone.common.HomePageDZB;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登录界面
 * 
 */
public class LoginActivity extends Activity {
	private int i1, i2;
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
		requestWindowFeature(0x1);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_login);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		for (int i = 0; i < HomePageDZB.commonActivities.size(); i++) {
			try {
				LoginActivity isLoginActivity = (LoginActivity) (HomePageDZB.commonActivities
						.get(i));
				isLoginActivity.finish();
			} catch (Exception e) {
			}
		}
		HomePageDZB.commonActivities.add(this);
		accountEt = (EditText) findViewById(R.id.ui_login_username);
		passwordEt = (EditText) findViewById(R.id.ui_login_password);
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		// accountEt.setText(settings.getString("LOGINNAME", ""));
		// passwordEt.setText(settings.getString("PASSWORD", ""));
		accountEt.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i1 == 0) {
					accountEt.setText("");
					i1++;
				}
			}
		});
		passwordEt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i2 == 0) {
					passwordEt.setText("");
					i2++;
				}
			}
		});
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
						editor.putString("LOGINNAME", accountEt.getText()
								.toString().trim());
						editor.putString("PASSWORD", passwordEt.getText()
								.toString().trim());
						editor.commit();
						Intent intent = new Intent(LoginActivity.this,
								WelcomeActivity.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.ui_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoginActivity.this.finish();
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LoginActivity.this.finish();
	}
}
