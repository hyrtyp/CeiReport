package com.hyrt.cei.predownload;

import java.util.ArrayList;

/**
 *管理所有下载线程类 
 *
 */
public class DownloadThreadManager
{

    public DownloadThreadManager()
    {
    }

    public static void addThread(DownloadThread downloadthread)
    {
        threadList.add(downloadthread);
    }

    public static void clearThread()
    {
        if(threadList != null && !threadList.isEmpty()){
        	for(int i=0;i<threadList.size();i++){
        		DownloadThread downloadthread = threadList.get(i);
        		downloadthread.isStop = true;
        	}
        	threadList.clear();
        }
        	
    }

    private static ArrayList<DownloadThread> threadList = new ArrayList<DownloadThread>();

}
