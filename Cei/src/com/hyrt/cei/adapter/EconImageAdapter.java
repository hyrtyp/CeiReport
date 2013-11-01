package com.hyrt.cei.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.econdata.EconDataMain;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.New;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class EconImageAdapter extends BaseAdapter {
	private EconDataMain mContext;
	private List<New> data;
	public HashMap<Integer, Drawable> drawables = new HashMap<Integer, Drawable>();
	private AsyncImageLoader asyncImageLoader;
	private Gallery gallery;
	public EconImageAdapter(EconDataMain c,List<New> data,Gallery gallery,ImageView[] imageViews) {
		mContext = c;
		this.data=data;
		this.gallery=gallery;
		asyncImageLoader = ((CeiApplication) c.getApplication()).asyncImageLoader;
	}
	

	public int getCount() {
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {
		return data.get(position%5);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new ImageView(mContext);
		} 
		convertView.setTag(data.get(position%5).getPpath());
		convertView.setBackgroundResource(R.drawable.econ_data_big_banner);
		if (drawables.containsKey(Integer.valueOf(position%5))
				&& drawables.get(Integer.valueOf(position%5)) != null) {
			convertView.setBackgroundDrawable(drawables.get(Integer.valueOf(position%5)));
		}else{
		   ImageResourse imageResource = new ImageResourse();
		   imageResource.setIconUrl(data.get(position%5).getPpath());
		   imageResource.setIconId(data.get(position%5).getId());
		   imageResource.setIconTime(data.get(position%5).getTime());
		   asyncImageLoader.loadDrawable(imageResource, new ImageCallback() {

			   @Override
			   public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				   ImageView img = (ImageView) gallery.findViewWithTag(data
						   .get(position%5).getPpath());
				   if (img != null && imageDrawable != null) {
					   img.setBackgroundDrawable(imageDrawable);
					   drawables.put(Integer.valueOf(position%5),
							imageDrawable);
					   Log.i("view", position%5 + "aic");
				   }
			   }
		   });
		 }
		
		return convertView;
	}
	public void clearBitmaps(){
		Iterator<Integer> iterator = drawables.keySet().iterator();
		while(iterator.hasNext()){
			Integer path = iterator.next();
			Drawable drawable = drawables.get(path);
			if(((BitmapDrawable)drawable) != null)
				((BitmapDrawable)drawable).getBitmap().recycle();
			drawable = null;
		}
	}
}
