package com.hyrt.cei.ui.common;

import com.hyrt.cei.R;
import com.hyrt.cei.dzb.ui.HomePageDZB;
import com.hyrt.cei.ui.main.Announcement;
import com.hyrt.cei.ui.main.Disclaimer;
import com.hyrt.cei.ui.personcenter.PersonCenter;
import com.hyrt.cei.ui.witsea.WitSeaActivity;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.UserInfo;
import com.hyrt.cei.webservice.service.Service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 注册界面
 * 
 */
public class RegistActivity extends Activity implements OnClickListener{

	private final static int REPEAT_USER = 1;
	private final static int SAVE_USER = 2;
	private final static int SAVE_SUCCESS = 3;
	private final static int NO_NET = 4;
	private final static int REPEAT_EMAIL = 5;
	private final String[] types = { "身份证", "学生证", "工作证", "士兵证", "军官证","护照" };
	private RadioOnClick OnClick = new RadioOnClick(1);
	private EditText e6;
	// 用户名
	private String loginName;
	//支持身份证类型
	private final String ID_TYPES = "身份证学生证工作证士兵证军官证护照";
	
	@Override
	protected void onDestroy() {
		HomePageDZB.commonActivities.remove(this);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_regist);
		e6 = (EditText) findViewById(R.id.regist_6);
		e6.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				AlertDialog ad = new AlertDialog.Builder(RegistActivity.this)
						.setTitle("选择证件类型")
						.setSingleChoiceItems(types, OnClick.getIndex(),
								OnClick).create();
				ad.show();
			}
		});
		e6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog ad = new AlertDialog.Builder(RegistActivity.this)
						.setTitle("选择证件类型")
						.setSingleChoiceItems(types, OnClick.getIndex(),
								OnClick).create();
				ad.show();
			}
		});
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		for (int i = 0; i < HomePageDZB.commonActivities.size(); i++) {
			try {
				RegistActivity isRegistActivity = (RegistActivity) ( HomePageDZB.commonActivities.get(i));
				isRegistActivity.finish();
			} catch (Exception e) {
			}
		}
		HomePageDZB.commonActivities.add(this);

		findViewById(R.id.ui_regist_regist).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(RegistActivity.this,
								LoginActivity.class);
						startActivity(intent);
					}
				});
		findViewById(R.id.ui_regist_login).setOnClickListener(
				new OnClickListener() {

					private Handler handler;
					private UserInfo userInfo = new UserInfo();

					@Override
					public void onClick(View v) {
						handler = new Handler() {
							@Override
							public void dispatchMessage(final Message msg) {
								switch (msg.arg1) {
								case SAVE_USER:
									new Thread(new Runnable() {

										@Override
										public void run() {
											String rs = Service
													.saveUserInfo(userInfo);
											if (XmlUtil.parseReturnCode(rs)
													.equals("1")) {
												Message message = handler
														.obtainMessage();
												message.arg1 = REPEAT_USER;
												handler.sendMessage(message);
											} else if (XmlUtil.parseReturnCode(rs)
													.equals("2")) {
												Message message = handler
														.obtainMessage();
												message.arg1 = REPEAT_EMAIL;
												handler.sendMessage(message);
											} else if (XmlUtil.parseReturnCode(
													rs).equals("-1")) {
												Message message = handler
														.obtainMessage();
												message.arg1 = NO_NET;
												handler.sendMessage(message);
											} else {
												Message message = handler
														.obtainMessage();
												message.arg1 = SAVE_SUCCESS;
												SharedPreferences settings = getSharedPreferences(
														"loginInfo",
														Activity.MODE_PRIVATE);
												SharedPreferences.Editor editor = settings
														.edit();
												editor.putString("LOGINNAME",
														userInfo.getLoginName());
												editor.putString("PASSWORD",
														userInfo.getPassword());
												editor.commit();
												handler.sendMessage(message);
											}
										}
									}).start();
									break;
								case REPEAT_USER:
									Toast.makeText(RegistActivity.this,
											"用户名重复!", Toast.LENGTH_SHORT)
											.show();
									break;
								case SAVE_SUCCESS:
									Toast.makeText(RegistActivity.this,
											"注册成功!", Toast.LENGTH_SHORT).show();
									RegistActivity.this.finish();
									break;
								case NO_NET:
									Toast.makeText(RegistActivity.this,
											"网络有问题!", Toast.LENGTH_SHORT)
											.show();
									break;
								case REPEAT_EMAIL:
									Toast.makeText(RegistActivity.this,
											"邮箱重复，请换一个邮箱吧！", Toast.LENGTH_SHORT)
											.show();
									break;
								}
							}
						};
						LinearLayout editParents = (LinearLayout) findViewById(R.id.ui_regist_etparent);
						for (int i = 0; i < editParents.getChildCount() - 1; i++) {
							String editTextVal = "";
							RelativeLayout editParent = (RelativeLayout) editParents
									.getChildAt(i);
							if (i != 5) {
								editTextVal = ((EditText) editParent
										.getChildAt(1)).getText().toString()
										.trim();
								if (((EditText) editParent.getChildAt(1))
										.getText() == null
										|| editTextVal.equals("")) {
									if (editParent.getChildCount() == 3) {
										Toast.makeText(
												RegistActivity.this,
												((TextView) editParent
														.getChildAt(0))
														.getText()
														.toString()
														.trim()
														.subSequence(
																0,
																((TextView) editParent
																		.getChildAt(0))
																		.getText()
																		.toString()
																		.trim()
																		.length() - 1)
														+ "不能为空！",
												Toast.LENGTH_SHORT).show();
										return;
									}
								}
							}
							switch (i) {
							case 0:
								userInfo.setLoginName(editTextVal);
								break;
							case 1:
								userInfo.setPassword(editTextVal);
								break;
							case 3:
								userInfo.setName(editTextVal);
								break;
							case 4:
								userInfo.setEmail(editTextVal);
								break;
							case 5:
								RadioGroup sexgr = (RadioGroup) editParent
										.getChildAt(1);
								RadioButton sexrb = (RadioButton) sexgr.getChildAt(0);
								if (sexrb.isChecked())
									userInfo.setSex(1);
								else
									userInfo.setSex(0);
								break;
							case 6:
								userInfo.setIdType(editTextVal);
								break;
							case 7:
								userInfo.setIdNum(editTextVal);
								break;
							case 8:
								userInfo.setPhoneNum(editTextVal);
								break;
							}
						}
						if (!((EditText) ((RelativeLayout) editParents
								.getChildAt(1)).getChildAt(1))
								.getText()
								.toString()
								.trim()
								.equals(((EditText) ((RelativeLayout) editParents
										.getChildAt(2)).getChildAt(1))
										.getText().toString().trim())) {
							Toast.makeText(RegistActivity.this, "两次输入的密码不一致!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (!((EditText) ((RelativeLayout) editParents
								.getChildAt(4)).getChildAt(1)).getText()
								.toString().trim().contains("@")) {
							Toast.makeText(RegistActivity.this, "邮箱格式不对!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (!ID_TYPES.contains(((EditText) ((RelativeLayout) editParents
								.getChildAt(6)).getChildAt(1)).getText()
								.toString().trim())) {
							Toast.makeText(RegistActivity.this, "身份证类型不对!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						Message message = handler.obtainMessage();
						message.arg1 = SAVE_USER;
						handler.sendMessage(message);
					}
				});
		SharedPreferences settings = getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
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
			RegistActivity.this.finish();
			break;
		}
	}
	
	private void registBottomEvent(){
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
		RegistActivity.this.finish();
	}
	
	class RadioOnClick implements DialogInterface.OnClickListener {
		private int index;

		public RadioOnClick(int index) {
			this.index = index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public void onClick(DialogInterface dialog, int whichButton) {
			setIndex(whichButton);
			e6.setText(types[whichButton]);
			dialog.dismiss();
		}
	}
}
