package com.hyrt.cei.application;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.exception.CrashHandler;
import com.hyrt.cei.ui.information.funId;
import com.hyrt.cei.ui.main.Welcome;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.WriteOrRead;
import com.hyrt.cei.util.XmlUtil;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.ReportColumn;
import com.hyrt.cei.webservice.service.Service;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CeiApplication extends Application {
	public String nowStart;
    public int bgindex;
	public AsyncImageLoader asyncImageLoader;
	public DataHelper dataHelper;
	public ColumnEntry columnEntry;
	private Object INSTANCE_LOCK = new Object();
	public List<funId> buyEconData;
	public List<funId> buyReportData;
	public List<ReportColumn> ReportColumns;
	public  List<Class> activitys;
	@Override
	public void onCreate() {
		activitys = new ArrayList<Class>();
		asyncImageLoader = new AsyncImageLoader(this);
		columnEntry = new ColumnEntry();
		buyEconData=new ArrayList<funId>();
		buyReportData=new ArrayList<funId>();
		ReportColumns=new ArrayList<ReportColumn>();
		synchronized (INSTANCE_LOCK) {
			if (dataHelper == null) {
				dataHelper = new DataHelper(this);
			}
		}
		initSdCard();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						if(columnEntry != null && dataHelper != null && columnEntry.getUserId() != null){
							 List<Courseware> coursewares = dataHelper.getStudyRecord();
							 for(int i=0;i<coursewares.size();i++){
								 if(("1").equals(coursewares.get(i).getIscompleted() )
										 //&& ("-1").equals(coursewares.get(i).getTimePoint())
										 && coursewares.get(i).getUploadTime() > 0){
									 String rs = Service.saveUserClassTime(columnEntry.getUserId(),coursewares.get(i));
									 if(XmlUtil.parseReturnCode(rs).equals("1")){
										 coursewares.get(i).setTimePoint("0");
										 coursewares.get(i).setUploadTime(0);
										 dataHelper.updatePlayRecord(coursewares.get(i));
									 }
								 }
							 }
						}
						Thread.sleep(100000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		//CrashHandler crashHandler = CrashHandler.getInstance();
		//crashHandler.init(getApplicationContext());
		super.onCreate();
	}

	private void initSdCard() {
		File filePath = new File(MyTools.RESOURCE_PATH );
		if(!filePath.exists())
			filePath.mkdirs();
		File coursewareFilePath = new File(MyTools.RESOURCE_PATH + MyTools.KJ_PARTPATH );
		if(!coursewareFilePath.exists())
			coursewareFilePath.mkdirs();
		File bgFilePath = new File(MyTools.RESOURCE_PATH + "pdf/" );
		if(!bgFilePath.exists())
			bgFilePath.mkdirs();
		File pingguoPath = new File("/mnt/sdcard/yepeng/");
		if(!pingguoPath.exists())
			pingguoPath.mkdirs();
		
		
	}

	public boolean isNet() {
		if(Welcome.isGoUnline)
			return false;
		ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 当内存被清除时，加载此方法
	 */
	@Override
	public void onLowMemory() {
		columnEntry.getColumnEntryChilds().clear();
		columnEntry.getSelectColumnEntryChilds().clear();
		String result = WriteOrRead.read(MyTools.nativeData,Welcome.INITRESOURCES_FILENAME);
		XmlUtil.parseInitResources(result, columnEntry);
		// 请求个人资源100%
		result = WriteOrRead.read(MyTools.nativeData,Welcome.INITSELFRESOURCES_FILENAME);
		XmlUtil.parseInitSelfResources(result, columnEntry);
		super.onLowMemory();
	}

}
