package com.hyrt.cei.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.hyrt.cei.R;
import com.hyrt.cei.ui.ebook.CeiShelfBookActivity;
import com.hyrt.cei.ui.ebook.ReportIntro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

//import com.hyrt.cei.R;

/**
 * 工具类
 * 
 * 
 */
public class MyTools {

	public static String url;
	public static String netTimeOut;
	public static String RESOURCE_PATH;
	public static String KJ_PARTPATH;
	public static String KJ_ENCRYPATH;
	public static String nativeData;
	public static String newsHtml;
	public static String noticeHtml;
	static {
		loadConfigurations();
	}

	public static void loadConfigurations() {
		InputStream in = MyTools.class
				.getResourceAsStream("/assets/configuration.properties");
		Properties p = new Properties();
		try {
			p.load(in);
			url = p.getProperty("URL");
			netTimeOut = p.getProperty("NET_OUTTIME");
			RESOURCE_PATH = p.getProperty("RESOURCE_PATH");
			KJ_PARTPATH = p.getProperty("KJ_PATH");
			KJ_ENCRYPATH = p.getProperty("KJ_ENCRYPATH");
			nativeData = p.getProperty("NATIVEDATA_PATH");
			newsHtml = p.getProperty("NEWS_HTML");
			noticeHtml = p.getProperty("NOTICE_HTML");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void exitShow(final Context context, View showView, final String title) {
		try {
			View view = LayoutInflater.from(context).inflate(
					R.layout.pop_exit_show, null);
			final PopupWindow mPopupWindow = new PopupWindow(view,
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setTouchable(true);
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			TextView textTitle = (TextView) view
					.findViewById(R.id.pop_exit_show_title);
			textTitle.setText(title);
			view.findViewById(R.id.pop_exit_show_yes).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							mPopupWindow.dismiss();
                            if(title.equals("文件还没有下载完成，请到书架下载！")){
                            	Intent intent = new Intent(context,
        								CeiShelfBookActivity.class);
                            	context.startActivity(intent);
                            }
						}
					});
			view.findViewById(R.id.pop_exit_show_exit).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							mPopupWindow.dismiss();

						}
					});
			mPopupWindow.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void popExitActivity(final Context context) {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				try {
					View view = LayoutInflater.from(context).inflate(
							R.layout.pop_exit_show, null);
					final PopupWindow mPopupWindow = new PopupWindow(view,
							LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					mPopupWindow.setFocusable(true);
					mPopupWindow.setTouchable(true);
					mPopupWindow.setOutsideTouchable(true);
					mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
					TextView textTitle = (TextView) view
							.findViewById(R.id.pop_exit_show_title);
					textTitle.setText("网络不通，无法查看此内容！");
					view.findViewById(R.id.pop_exit_show_yes).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									mPopupWindow.dismiss();
									((Activity)context).finish();
								}
							});
					view.findViewById(R.id.pop_exit_show_exit).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									mPopupWindow.dismiss();
									((Activity)context).finish();
								}
							});
					mPopupWindow.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		},100);
	   }

	public static void showPushXml(final Context context) {
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				System.out.println("in run");
				Toast toast = Toast.makeText(context, "没有数据！",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		});
	}
}
