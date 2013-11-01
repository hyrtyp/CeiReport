package com.hyrt.cei.predownload;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.vo.Preload;

/**
 *下载课件的线程
 *
 */

public class DownloadThread extends Thread {

	private URL downUrl;
	private File saveFile;
	private Preload preload;
	public boolean isStop = false;

	public DownloadThread(URL url, File file,
			Preload preload,DataHelper dataHelper) {
		HttpURLConnection httpurlconnection = null;
		downUrl = url;
		saveFile = file;
		this.preload = preload;
		try {
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setConnectTimeout(5000);
			httpurlconnection.setRequestMethod("GET");
			httpurlconnection
					.setRequestProperty(
							"Accept",
							"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			httpurlconnection.setRequestProperty("Accept-Language", "zh-CN");
			httpurlconnection.setRequestProperty("Charset", "UTF-8");
			httpurlconnection
					.setRequestProperty(
							"User-Agent",
							"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			httpurlconnection.setRequestProperty("Connection", "Keep-Alive");
			httpurlconnection.connect();
			if (httpurlconnection.getResponseCode() == 200){
				preload.setLoadSumByte(httpurlconnection.getContentLength());
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}finally{
			httpurlconnection.disconnect();
		}
	}

	public void run() {
		RandomAccessFile randomaccessfile = null;
		HttpURLConnection httpurlconnection = null;
		InputStream inputstream = null;
		try {
			httpurlconnection = (HttpURLConnection) downUrl.openConnection();
			httpurlconnection.setConnectTimeout(5000);
			httpurlconnection.setRequestMethod("GET");
			httpurlconnection.setRequestProperty("Accept","image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			httpurlconnection.setRequestProperty("Accept-Language", "zh-CN");
			httpurlconnection.setRequestProperty("Referer", downUrl.toString());
			httpurlconnection.setRequestProperty("Charset", "UTF-8");
			httpurlconnection
					.setRequestProperty("Range", (new StringBuilder("bytes="))
							.append(preload.getLoadCurrentByte()).append("-").append(preload.getLoadSumByte()).toString());
			httpurlconnection
					.setRequestProperty(
							"User-Agent",
							"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			httpurlconnection.setRequestProperty("Connection", "Keep-Alive");
			httpurlconnection.connect();
			inputstream = httpurlconnection.getInputStream();
			randomaccessfile = new RandomAccessFile(saveFile, "rwd");
			byte[] buffer = new byte[0x19000];
			int length = -1;
			randomaccessfile.seek(preload.getLoadCurrentByte());
			while ((length = inputstream.read(buffer)) != -1) {
				if(preload.getLoading() != 1 || isStop){
					return;
				}
				randomaccessfile.write(buffer, 0, length);
				preload.setLoadCurrentByte(length + preload.getLoadCurrentByte());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				randomaccessfile.close();
				inputstream.close();
				httpurlconnection.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
