package com.hyrt.cei.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.ebook.ReadReportActivity;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Report;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReportAdapter1 extends BaseAdapter {

	private ReadReportActivity mContext;
	private AsyncImageLoader asyncImageLoader;
	private List<Report> data;
	private GridView gridView;
	private LayoutInflater inflater;
	private HashMap<String,Drawable> drawables = new HashMap<String,Drawable>();

	public ReportAdapter1(ReadReportActivity c, GridView gridView,
			List<Report> data) {
		mContext = c;
		this.data = data;
		this.gridView = gridView;
		inflater=LayoutInflater.from(c);
		asyncImageLoader = ((CeiApplication) c.getApplication()).asyncImageLoader;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView =inflater.inflate(R.layout.report_item, null);
		} 
		ImageView imageView=(ImageView) convertView.findViewById(R.id.report_item_reportImg);
		imageView.setTag(data.get(position).getSmallPpath());
		TextView textView=(TextView) convertView.findViewById(R.id.report_item_reportName);
		textView.setText(data.get(position).getName());//data.get(position).getName().length()>7?data.get(position).getName().substring(0,6)+"...":
		ImageResourse imageResource = new ImageResourse();
		imageResource.setIconUrl(data.get(position).getSmallPpath());
		imageResource.setIconId(data.get(position).getId());
		imageResource.setIconTime(data.get(position).getProtime());
		if (drawables.containsKey(data.get(position).getSmallPpath())
				&& drawables.get(data.get(position).getSmallPpath()) != null) {
			imageView.setImageDrawable(drawables.get(
					data.get(position).getSmallPpath()));
			Log.i("view", "缓存起作用");
		} else {
			asyncImageLoader.loadDrawable(imageResource, new ImageCallback() {

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					ImageView img = (ImageView) gridView.findViewWithTag(data.get(
							position).getSmallPpath());
					if(img!=null&&imageDrawable!=null){
						//img.setLayoutParams(new LinearLayout.LayoutParams(120, 150));
					    //img.setScaleType(ImageView.ScaleType.FIT_CENTER);
						img.setImageDrawable(imageDrawable);
						drawables.put(data.get(position).getSmallPpath(), imageDrawable);
					}
				}
			});
		}
		//imageView.setImageResource(R.drawable.bg0);
		//imageView.setLayoutParams(new LinearLayout.LayoutParams(120,150));
		return convertView;
	}
	
	public void clearBitmaps(){
		Iterator<String> iterator = drawables.keySet().iterator();
		while(iterator.hasNext()){
			String path = iterator.next();
			Drawable drawable = drawables.get(path);
			if(((BitmapDrawable)drawable) != null)
				((BitmapDrawable)drawable).getBitmap().recycle();
			drawable = null;
		}
	}

}
