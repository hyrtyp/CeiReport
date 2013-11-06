package com.hyrt.cei.ui.common;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.common.HomePageDZB;

public class GetpasswordActivity extends Activity{
	private int i1, i2;
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private EditText username, email;
	private String strusername, stremail;
	private int code;

	// 用户名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(0x1);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_getpassword);
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		init();
	}

	private void init() {
		username = (EditText) findViewById(R.id.ui_getpassword_username);
		email = (EditText) findViewById(R.id.ui_getpassword_email);
		findViewById(R.id.ui_regist_regist).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(GetpasswordActivity.this,
								LoginActivity.class);
						startActivity(intent);
						GetpasswordActivity.this.finish();
					}
				});
		refreshListData();
		findViewById(R.id.ui_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GetpasswordActivity.this,
						HomePageDZB.class);
				startActivity(intent);
				GetpasswordActivity.this.finish();
			}
		});
	}

	private void refreshListData() {
		username.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i1 == 0) {
					username.setText("");
					i1++;
				}
			}
		});
		email.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i2 == 0) {
					email.setText("");
					i2++;
				}
			}
		});
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
								GetpasswordActivity.this.finish();
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		GetpasswordActivity.this.finish();
	}
}
