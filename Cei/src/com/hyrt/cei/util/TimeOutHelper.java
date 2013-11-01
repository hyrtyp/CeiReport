package com.hyrt.cei.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 
 */
public class TimeOutHelper {
	// 用于绑定线程开始时间
	private static ThreadLocal<Long> timeThreadLocal = new ThreadLocal<Long>();
	// 用于绑定线程超时任务引用变量
	private static ThreadLocal<Runnable> taskThreadLocal = new ThreadLocal<Runnable>();
	// 用于存储所有的超时任务引用变量
	private static Map<Long, Runnable> taskContainer = new HashMap<Long, Runnable>();
	public Context context;
	public ProgressDialog progressDialog;
	// 超时时间
	public final static long timeout = 15000;
	// 超时通知消息标志位
	public final static int TIMEOUT_VALUE = 1;
	// 服务端异常标志位
	public final static String EXPSERVER_FLAG = "2";
	// 客户端异常标志位
	public final static String EXPCLIENT_FLAG = "3";
	// 没有数据标志位
	public final static String NODATA_FLAG = "4";
	// 数据加载完毕标志位
	public final static String ALDATA_FLAG = "5";
	// 用户名密码错误标志位
	public final static String PASSERROR_FLAG = "6";
	//用户名密码不能为空
	public final static String PASSNULL_FLAG = "7";
	//无法获取当前位置
	public final static String NOGIS_FLAG = "8";
	//成功标志当前企业
	public final static String SUCCGIS_FLAG = "9";
	//成功标志当前企业
	public final static String NOROLE_FLAG = "10";
	//设备号与账号不匹配
	public final static String ERROR_DEVICE_FLAG = "11";
	//设备号已经绑定
	public final static String DEVICEEXIST_FLAG = "12";

	public TimeOutHelper(Context context) {
		this.context = context;
	}

	/**
	 * 提示消息接收器
	 */
	public Handler messageHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(android.os.Message msg) {
			int value = msg.arg1;
			if (value == TIMEOUT_VALUE) {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				installFailerDialog(null);
				return;
			}
			uninstallDialog();
			AlertDialog.Builder builder = new Builder(context);
			builder.setTitle("提示");
			builder.setMessage(msg.obj.toString());
			builder.setPositiveButton("确认",
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			try {
				builder.create().show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	/**
	 * 定时器
	 */
	public void installTimerTask() {
	/*	Runnable timeOutRunnable = new Runnable() {
			@Override
			public void run() {
				Message msg = messageHandler.obtainMessage();
				msg.arg1 = TIMEOUT_VALUE;
				messageHandler.sendMessage(msg);
			}
		};
		messageHandler.postDelayed(timeOutRunnable, timeout);
		Long startTime = System.currentTimeMillis();
		timeThreadLocal.set(startTime);
		taskThreadLocal.set(timeOutRunnable);
		taskContainer.put(startTime, timeOutRunnable);*/
	}

	/**
	 * 进度框
	 */
	public void installProcessingDialog() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("提示");
		progressDialog.setMessage("正在加载...");
		progressDialog.setButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				progressDialog.dismiss();
				uninstallTimerTask(ALDATA_FLAG);
			}
		});
		try {
			progressDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 确认失败框
	 */
	public void installFailerDialog(DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("提示");
		builder.setMessage("当前网路不通,请稍后访问！");
		if (listener == null) {
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							((Activity)context).finish();
							dialog.dismiss();
						}
					});
		} else {
			builder.setPositiveButton("确认", listener);
		}
		try {
			builder.create().show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 卸载定时器
	 */
	public boolean uninstallTimerTask(String returnCode) {

	/*	if (timeThreadLocal.get() == null) {
			Iterator<Long> sets = taskContainer.keySet().iterator();
			while (sets.hasNext()) {
				Long keyL = sets.next();
				if (keyL < System.currentTimeMillis()) {
					messageHandler.removeCallbacks(taskContainer.get(keyL));
				}
			}
		} else {
			messageHandler.removeCallbacks(taskThreadLocal.get());
		}
		if (timeThreadLocal.get() == null
				|| System.currentTimeMillis() - timeThreadLocal.get() > timeout) {
			return false;
		}
		if (returnCode.trim().equals(EXPSERVER_FLAG)) {
			Message msg = messageHandler.obtainMessage();
			msg.obj = "服务端繁忙..";
			messageHandler.sendMessage(msg);
			return false;
		} else if (returnCode.trim().equals("")
				|| returnCode.trim().equals(EXPCLIENT_FLAG)) {
			Message msg = messageHandler.obtainMessage();
			msg.obj = "网络不稳定..";
			messageHandler.sendMessage(msg);
			return false;
		} else if (returnCode.trim().equals(NODATA_FLAG)) {
			Message msg = messageHandler.obtainMessage();
			msg.obj = "没有数据了..";
			messageHandler.sendMessage(msg);
			return false;
		}else if (returnCode.trim().equals(PASSERROR_FLAG)) {
			Message msg = messageHandler.obtainMessage();
			msg.obj = "用户名密码错误..";
			messageHandler.sendMessage(msg);
			return false;
		}else if (returnCode.trim().equals(PASSNULL_FLAG)) {
			Message msg = messageHandler.obtainMessage();
			msg.obj = "用户名密码不能为空..";
			messageHandler.sendMessage(msg);
			return false;
		}else if (returnCode.trim().equals(NOGIS_FLAG)) {
			Message msg = messageHandler.obtainMessage();
			msg.obj = "无法获取当前位置..";
			messageHandler.sendMessage(msg);
			return false;
		}else if (returnCode.trim().equals(SUCCGIS_FLAG)) {
			Message msg = messageHandler.obtainMessage();
			msg.obj = "标注成功..";
			messageHandler.sendMessage(msg);
		}else if (returnCode.trim().equals(NOROLE_FLAG)) {
			Message msg = messageHandler.obtainMessage();
			msg.obj = "此主体不在网格内无权操作..";
			messageHandler.sendMessage(msg);
			return false;
		}else if (returnCode.trim().equals(ERROR_DEVICE_FLAG)) {
			Message msg = messageHandler.obtainMessage();
			msg.obj = "设备号与账号不匹配..";
			messageHandler.sendMessage(msg);
			return false;
		}else if (returnCode.trim().equals(DEVICEEXIST_FLAG)) {
			Message msg = messageHandler.obtainMessage();
			msg.obj = "此设备号已绑定..";
			messageHandler.sendMessage(msg);
			return false;
		}
		*/
		
		return true;
	}

	/**
	 * 取消定时框
	 */
	public void uninstallDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}
