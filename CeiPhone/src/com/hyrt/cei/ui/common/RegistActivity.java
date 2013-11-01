package com.hyrt.cei.ui.common;

import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.UserInfo;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.ContainerActivity;
import com.hyrt.ceiphone.R;
import com.hyrt.ceiphone.WelcomeActivity;
import com.hyrt.ceiphone.common.HomePageDZB;

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
import android.widget.Button;
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
public class RegistActivity extends ContainerActivity {
	private int i1, i2, i3, i4, i5, i6, i7, i8;
	private final static int REPEAT_USER = 1;
	private final static int SAVE_USER = 2;
	private final static int SAVE_SUCCESS = 3;
	private final static int NO_NET = 4;
	private final static int REPEAT_EMAIL = 5;
	private Button back;
	private EditText e1, e2, e3, e4, e5, e6, e7, e8;
	// 支持身份证类型
	private final String ID_TYPES = "身份证学生证工作证士兵证军官证护照";
	private final String[] types = { "身份证", "学生证", "工作证", "士兵证", "军官证","护照" };
	private RadioOnClick OnClick = new RadioOnClick(1);

	@Override
	protected void onDestroy() {
		HomePageDZB.commonActivities.remove(this);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_regist);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		for (int i = 0; i < HomePageDZB.commonActivities.size(); i++) {
			try {
				RegistActivity isRegistActivity = (RegistActivity) (HomePageDZB.commonActivities
						.get(i));
				isRegistActivity.finish();
			} catch (Exception e) {
			}
		}
		HomePageDZB.commonActivities.add(this);
		e1 = (EditText) findViewById(R.id.regist_1);
		e2 = (EditText) findViewById(R.id.regist_2);
		e3 = (EditText) findViewById(R.id.regist_3);
		e4 = (EditText) findViewById(R.id.regist_4);
		e5 = (EditText) findViewById(R.id.regist_5);
		e6 = (EditText) findViewById(R.id.regist_6);
		e7 = (EditText) findViewById(R.id.regist_7);
		e8 = (EditText) findViewById(R.id.regist_8);
		back = (Button) findViewById(R.id.ui_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RegistActivity.this,
						HomePageDZB.class);
				startActivity(intent);
			}
		});
		e1.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i1 == 0) {
					e1.setText("");
					i1++;
				}
			}
		});
		e2.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i2 == 0) {
					e2.setText("");
					i2++;
				}
			}
		});
		e3.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i3 == 0) {
					e3.setText("");
					i3++;
				}
			}
		});
		e4.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i4 == 0) {
					e4.setText("");
					i4++;
				}
			}
		});
		e5.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i5 == 0) {
					e5.setText("");
					i5++;
				}
			}
		});
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
		e7.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i7 == 0) {
					e7.setText("");
					i7++;
				}
			}
		});
		e8.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && i8 == 0) {
					e8.setText("");
					i8++;
				}
			}
		});
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
											}else if (XmlUtil.parseReturnCode(rs)
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
									Toast.makeText(RegistActivity.this,"用户名重复，请换一个用户名吧!", Toast.LENGTH_SHORT).show();
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

						for (int i = 0; i < editParents.getChildCount(); i++) {
							String editTextVal = "";
							if (i != 5) {
								RelativeLayout editParent = (RelativeLayout) editParents
										.getChildAt(i);
								editTextVal = ((EditText) editParent
										.getChildAt(0)).getText().toString()
										.trim();
								if (((EditText) editParent.getChildAt(0))
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
								LinearLayout editParent = (LinearLayout) editParents
										.getChildAt(i);
								RadioGroup sexgr = (RadioGroup) editParent
										.getChildAt(1);
								RadioButton sexrb = (RadioButton) sexgr
										.getChildAt(0);
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
								.getChildAt(1)).getChildAt(0))
								.getText()
								.toString()
								.trim()
								.equals(((EditText) ((RelativeLayout) editParents
										.getChildAt(2)).getChildAt(0))
										.getText().toString().trim())) {
							Toast.makeText(RegistActivity.this, "两次输入的密码不一致!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (!((EditText) ((RelativeLayout) editParents
								.getChildAt(4)).getChildAt(0)).getText()
								.toString().trim().contains("@")) {
							Toast.makeText(RegistActivity.this, "邮箱格式不对!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (!ID_TYPES
								.contains(((EditText) ((RelativeLayout) editParents
										.getChildAt(6)).getChildAt(0))
										.getText().toString().trim())) {
							Toast.makeText(RegistActivity.this, "身份证类型不对!",
									Toast.LENGTH_SHORT).show();
							return;
						}
						Message message = handler.obtainMessage();
						message.arg1 = SAVE_USER;
						handler.sendMessage(message);
					}
				});
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
