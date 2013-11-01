package com.hyrt.cei.predownload;

import java.io.File;
import java.net.URL;

import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.util.ThreadPoolWrap;
import com.hyrt.cei.vo.Preload;

/**
 * 监视下载量，并通知视图更新条目长度
 *
 */
public class FileDownloader {
	
	private DataHelper dataHelper;
	private Preload preload;
	public int waitCheck;

	public FileDownloader(DataHelper dataHelper, Preload preload) throws Exception {
		waitCheck = 1000;
		this.dataHelper = dataHelper;
		this.preload = preload;
	}

	public int download(DownloadProgressListener downloadprogresslistener)
			throws Exception {
		File file = new File(preload.getLoadLocalPath() + ".yepeng");
		URL url = new URL(preload.getLoadUrl());
		DownloadThread dt = new DownloadThread(url, file, preload,dataHelper);
		dt.setPriority(7);
		DownloadThreadManager.addThread(dt);
		ThreadPoolWrap.getThreadPool().executeTask(dt);
		while (true) {
			dataHelper.updatePreload(preload);
			if (preload.getLoading() == 1 && !dt.isStop) {
				Thread.sleep(waitCheck);			
				downloadprogresslistener.onDownloadSize();
				if (preload.getLoadFinish()==1) {
					return preload.getLoadCurrentByte();
				}
			} else {
				return preload.getLoadCurrentByte();
			}
		}
	}

	public Preload getPreload() {
		return preload;
	}

	public void setPreload(Preload preload) {
		this.preload = preload;
	}
}
