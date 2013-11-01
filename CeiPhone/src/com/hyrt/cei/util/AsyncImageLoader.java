package com.hyrt.cei.util;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.vo.ImageResourse;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {

	private CeiApplication app;

	public AsyncImageLoader(CeiApplication app) {
		this.app = app;
	}

	public void loadDrawable(final ImageResourse imageResourse,
			final ImageCallback imageCallback) {
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageResourse.getIconUrl());
				
			}
		};
		// 建立新一个新的线程下载图片
		final Runnable runnable = new Runnable() {

			@Override
			public void run() {
				//去数据库中查是否存在该图片
				Drawable drawable;
				ImageResourse oldImageResource = 
						app.dataHelper.getImageResource(imageResourse.getIconId(),imageResourse.getType());
				if(oldImageResource != null){
					//如果数据库中存在该图片的话
					if(oldImageResource.getIconTime().
							equals(imageResourse.getIconTime()==null?"":imageResourse.getIconTime())){
						//如果图片没变化的话
						drawable = oldImageResource.getIcon();
						Message message = handler.obtainMessage(0, drawable);
						handler.sendMessage(message);
					}else{
						//如果图片变化的话则更新图片
						try {
							drawable = ImageUtil.getDrawableFromUrl(imageResourse.getIconUrl());
							//如果图片没下载下来的话
							if(drawable == null){
								drawable = oldImageResource.getIcon();
							}else{
								//如果图片下载下来的话则更新数据库
								imageResourse.setId(oldImageResource.getId());
								imageResourse.setIcon(drawable);
								app.dataHelper.updateImageResource(imageResourse);
								Message message = handler.obtainMessage(0, drawable);
								handler.sendMessage(message);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else{
					//如果数据库中不存在该图片的话
					try {
						drawable = ImageUtil.getDrawableFromUrl(imageResourse.getIconUrl().replace("61.233.18.68", "192.168.10.248"));
						if(drawable != null){
							imageResourse.setIcon(drawable);
							app.dataHelper.saveImageResource(imageResourse);
							Message message = handler.obtainMessage(0, drawable);
							handler.sendMessage(message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		};
		new Thread(runnable).start();
	}

	// 回调接口
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
}