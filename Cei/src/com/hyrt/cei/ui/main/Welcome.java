package com.hyrt.cei.ui.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;

import com.hyrt.cei.dzb.ui.HomePageDZB;

import com.hyrt.cei.ui.common.LoginActivity;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.TimeOutHelper;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.ReportColumn;
import com.hyrt.cei.webservice.service.Service;

/**
 * 欢迎页
 * 
 */
public class Welcome extends Activity {

	private TimeOutHelper timeOutHelper;
	private ImageView progressIv;
	private AnimationDrawable animationDrawable;
	public static final String INITRESOURCES_FILENAME = "initResources.xml";
	public static final String INITSELFRESOURCES_FILENAME = "initSelfResources.xml";
	// 是否进入离线模式
	public static boolean isGoUnline;
	// 离线是否已经提示过了
	private boolean isNotice;
	private String str;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		isGoUnline = false;
		timeOutHelper = new TimeOutHelper(this);
		installProAnim();
		installData();
	}

	public static final int UPDATE_CENT = 1;
	public static final int GO_MAIN = 2;
	public static final int IS_NET = 3;
	public static final int NO_DATA = 4;
	public static final int USER_ERROR = 5;
	public static final int DEVICE_ERROR = 6;
	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.arg1) {
			case UPDATE_CENT:
				// 更新百分数。
				str = msg.arg2 + "%";
				tv.setText(str);
				break;
			case GO_MAIN:
				Intent intent = new Intent(Welcome.this, HomePageDZB.class);
				startActivity(intent);
				Welcome.this.finish();
				break;
			case IS_NET:
				isNotice = true;
				AlertDialog.Builder builder = new Builder(Welcome.this);
				timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
				builder.setTitle("提示");
				builder.setMessage("网络不通，是否进入离线模式！");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								isGoUnline = true;
								installData();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								Welcome.this.finish();
								for (int i = 0; i < HomePageDZB.commonActivities
										.size(); i++) {
									HomePageDZB.commonActivities.get(i)
											.finish();
								}
							}
						});
				builder.create().show();
				break;
			case NO_DATA:
				AlertDialog.Builder noDataBuilder = new Builder(Welcome.this);
				noDataBuilder.setTitle("提示");
				noDataBuilder.setMessage("无缓存数据，请退出应用！");
				noDataBuilder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								Welcome.this.finish();
							}
						});
				noDataBuilder.create().show();
				break;
			case DEVICE_ERROR:
				AlertDialog.Builder deviceErrorBuilder = new Builder(
						Welcome.this);
				deviceErrorBuilder.setTitle("提示");
				deviceErrorBuilder.setMessage("设备号与用户不匹配,请点确认进入默认版！");
				deviceErrorBuilder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								Welcome.this.finish();
							}
						});
				deviceErrorBuilder.create().show();
				SharedPreferences settings1 = getSharedPreferences("loginInfo",
						Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor1 = settings1.edit();
				editor1.putString("LOGINNAME", "");
				editor1.putString("PASSWORD", "");
				editor1.commit();
				break;
			case USER_ERROR:
				SharedPreferences settings = getSharedPreferences("loginInfo",
						Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("LOGINNAME", "");
				editor.putString("PASSWORD", "");
				editor.commit();
				AlertDialog.Builder errorUserBuilder = new Builder(Welcome.this);
				errorUserBuilder.setTitle("提示");
				errorUserBuilder.setMessage("用户名密码错误,请点确认进入默认版！");
				errorUserBuilder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								Welcome.this.finish();
							}
						});
				errorUserBuilder.create().show();
				break;

			}
		}
	};

	private void installData() {
		// 检查sd卡是否存在不存在的话，则退出
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, R.string.not_exist_sd, Toast.LENGTH_LONG)
					.show();
			this.finish();
			return;
		}
		final ColumnEntry columnEntry = ((CeiApplication) getApplication()).columnEntry;
		final Runnable runnable = new Runnable() {

			@Override
			public void run() {
				timeOutHelper.installTimerTask();
				// 如果判断用户是否有登陆
				SharedPreferences settings = getSharedPreferences("loginInfo",
						Activity.MODE_PRIVATE);
				if (settings.getString("LOGINNAME", "").equals("")) {
					columnEntry.setLoginName("");
					columnEntry.setPassword("");
				} else {
					columnEntry.setLoginName(settings
							.getString("LOGINNAME", ""));
					columnEntry.setPassword(settings.getString("PASSWORD", ""));
				}
				columnEntry.getColumnEntryChilds().clear();
				columnEntry.getSelectColumnEntryChilds().clear();
				try {
					if (((CeiApplication) getApplication()).isNet()
							&& !isGoUnline) {
						// 请求初始化资源 50%
						long startTime = System.currentTimeMillis();
						String result = Service.initResources(columnEntry,
								Welcome.this);
						long endTime = System.currentTimeMillis();
						if (endTime - startTime < 1000) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						if (XmlUtil.parseReturnCode(result).equals("-1")) {
							Message message = handler.obtainMessage();
							message.arg1 = IS_NET;
							handler.sendMessage(message);
							timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
							return;
						} else if (XmlUtil.parseReturnCode(result).equals("1")
								&& !settings.getString("LOGINNAME", "").equals(
										"")) {
							WriteOrRead.write(result, MyTools.nativeData,
									INITRESOURCES_FILENAME);
							XmlUtil.parseInitResources(result, columnEntry);
							Message message = handler.obtainMessage();
							message.arg1 = USER_ERROR;
							handler.sendMessage(message);
							timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
							return;
						} else if (XmlUtil.parseReturnCode(result).equals("2")) {
							WriteOrRead.write(result, MyTools.nativeData,
									INITRESOURCES_FILENAME);
							XmlUtil.parseInitResources(result, columnEntry);
							Message message = handler.obtainMessage();
							message.arg1 = DEVICE_ERROR;
							handler.sendMessage(message);
							timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
							return;
						}
						WriteOrRead.write(result, MyTools.nativeData,
								INITRESOURCES_FILENAME);
						XmlUtil.parseInitResources(result, columnEntry);
						Message message = handler.obtainMessage();
						message.arg1 = UPDATE_CENT;
						message.arg2 = 50;
						handler.sendMessage(message);
						// 请求个人资源100%
						result = Service.initSelfResources(columnEntry);
						WriteOrRead.write(result, MyTools.nativeData,
								INITSELFRESOURCES_FILENAME);
						XmlUtil.parseInitSelfResources(result, columnEntry);
						// 请求智慧海业务
						// 获取版本号
						// 将设备号写入SDCARD中
						File deviceIdFile = new File(MyTools.RESOURCE_PATH
								+ "deviceId");
						if (!deviceIdFile.exists()) {
							FileWriter fw = null;
							try {
								fw = new FileWriter(deviceIdFile);
								TelephonyManager tm = (TelephonyManager) Welcome.this
										.getSystemService(Context.TELEPHONY_SERVICE);
								WifiManager wifi = (WifiManager) Welcome.this
										.getSystemService(Context.WIFI_SERVICE);
								WifiInfo info = wifi.getConnectionInfo();
								fw.write((info.getMacAddress() + tm
										.getDeviceId()));
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								try {
									fw.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						message = handler.obtainMessage();
						message.arg1 = UPDATE_CENT;
						message.arg2 = 100;
						handler.sendMessage(message);
						// 获取版本号，调用接口得到报告的各个属性；
						// 获取版本号；
						String id = columnEntry.getColumnEntryChilds().get(0)
								.getId();
						((CeiApplication) getApplication()).ReportColumns
								.add(new ReportColumn());
						String returnCode = Service.queryReportName(id);

						if (returnCode != null) {
							if (returnCode.equals("0")) {
								((CeiApplication) getApplication()).ReportColumns
										.add(new ReportColumn());
							} else {
								((CeiApplication) getApplication()).ReportColumns = XmlUtil
										.parseReportColumn(returnCode);
								WriteOrRead
										.write(returnCode, MyTools.nativeData,
												"ReportColumns.xml");
							}
						}
					} else {
						// 请求初始化资源 50%
						String result = WriteOrRead.read(MyTools.nativeData,
								INITRESOURCES_FILENAME);
						if (!isNotice) {
							Message message = handler.obtainMessage();
							message.arg1 = IS_NET;
							handler.sendMessage(message);
							return;
						}
						if (result.equals("")
								|| XmlUtil.parseReturnCode(result).equals("-1")) {
							Message message = handler.obtainMessage();
							message.arg1 = NO_DATA;
							handler.sendMessage(message);
							timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
							return;
						}
						if (XmlUtil.parseReturnCode(result).equals("2")) {
							Message message = handler.obtainMessage();
							message.arg1 = DEVICE_ERROR;
							XmlUtil.parseInitResources(result, columnEntry);
							handler.sendMessage(message);
							timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
							return;
						}
						XmlUtil.parseInitResources(result, columnEntry);
						Message message = handler.obtainMessage();
						message.arg1 = UPDATE_CENT;
						message.arg2 = 50;
						handler.sendMessage(message);
						// 请求个人资源100%
						result = WriteOrRead.read(MyTools.nativeData,
								INITSELFRESOURCES_FILENAME);
						XmlUtil.parseInitSelfResources(result, columnEntry);
						message = handler.obtainMessage();
						message.arg1 = UPDATE_CENT;
						message.arg2 = 100;
						handler.sendMessage(message);

						String returnCode = WriteOrRead.read(
								MyTools.nativeData, "ReportColumns.xml");
						;

						if (returnCode != null) {
							if (returnCode.equals("0")) {
								((CeiApplication) getApplication()).ReportColumns
										.add(new ReportColumn());
							} else {
								((CeiApplication) getApplication()).ReportColumns = XmlUtil
										.parseReportColumn(returnCode);
								WriteOrRead
										.write(returnCode, MyTools.nativeData,
												"ReportColumns.xml");
							}
						}
					}
					timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
					Message message = handler.obtainMessage();
					message.arg1 = GO_MAIN;
					handler.sendMessage(message);
				} catch (Exception e) {
					timeOutHelper.uninstallTimerTask(TimeOutHelper.ALDATA_FLAG);
					e.printStackTrace();
				}
			}

		};
		new Thread(runnable).start();
	}

	private void installProAnim() {
		Typeface fontFace = Typeface.createFromAsset(getAssets(),
				"fonts/FZCQJW.TTF");
		tv = (TextView) findViewById(R.id.welcome_text_min);
		tv.setTypeface(fontFace);
		progressIv = (ImageView) findViewById(R.id.welcome_iv);
		animationDrawable = (AnimationDrawable) progressIv.getBackground();
		progressIv.getViewTreeObserver().addOnPreDrawListener(opdl);
	}

	OnPreDrawListener opdl = new OnPreDrawListener() {
		@Override
		public boolean onPreDraw() {
			animationDrawable.start();
			return true;
		}
	};

}
