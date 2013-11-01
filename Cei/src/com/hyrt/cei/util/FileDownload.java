package com.hyrt.cei.util;

import java.io.File;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import com.hyrt.report.network.DownloadProgressListener;
import com.hyrt.report.network.FileDownloader;

public class FileDownload {

	public static void download(final Activity activity,final String path, final File savedir,final Handler handler,final maxDownLoad backMax) {
		 
        new Thread(new Runnable() {            
 
            @Override
 
            public void run() {
 
                try {
                	  FileDownloader loader = new FileDownloader(activity, path, savedir,1);
                      //progressBar.setMax(loader.getFileSize());//设置进度条的最大刻度为文件的长度
                      backMax.maxDownload(loader.getFileSize());
                    loader.download(new DownloadProgressListener() {
                        @Override
 
                        public void onDownloadSize(int size) {//实时获知文件已经下载的数据长度
 
                            Message msg = new Message();
 
                            msg.what = 1;
 
                            msg.getData().putInt("size", size);
 
                            handler.sendMessage(msg);//发送消息
 
                        }
 
                    });
 
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.obtainMessage(-1).sendToTarget();
 
                }
 
            }
 
        }).start();
 
    }
	public interface maxDownLoad{
		public void maxDownload(int max);
	}
}
