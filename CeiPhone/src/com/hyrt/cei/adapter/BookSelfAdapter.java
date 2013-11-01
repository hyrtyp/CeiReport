package com.hyrt.cei.adapter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Report;
import com.hyrt.ceiphone.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BookSelfAdapter extends BaseAdapter {
	private List<Report> data;
	private GridView gridView;
	private LayoutInflater inflater;
	private AsyncImageLoader asyncImageLoader;
	DataHelper dataHelper;
	private List<String> indexs = new ArrayList<String>();

	 private HashMap<String, SoftReference<Drawable>> drawables = new
	 HashMap<String, SoftReference<Drawable>>();

	public BookSelfAdapter(Activity c, List<Report> data, GridView gridView) {
		this.data = data;
		this.gridView = gridView;
		inflater = LayoutInflater.from(c);
		asyncImageLoader = ((CeiApplication) c.getApplication()).asyncImageLoader;
		dataHelper = ((CeiApplication) c.getApplication()).dataHelper;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// ImageView imageView;
		indexs.add(position, "no");
		final ProgressBar bar;
		final TextView tv;
		final Report report = data.get(position);
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.read_report_book_item, null);
		}
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.yjbg_book_item_iv);
		imageView.setTag(report.getSmallPpath());
		imageView.setImageResource(R.drawable.read_report_report1);
		final ImageResourse imageResource = new ImageResourse();
		imageResource
				.setIconUrl(report.getSmallPpath().replace("/big.png", ""));
		imageResource.setIconId(report.getId());
		imageResource.setIconTime(report.getProtime());
		
		  if (drawables.containsKey(report.getSmallPpath()) &&
		  drawables.get(report.getSmallPpath()) != null) {
		  imageView.setImageDrawable(drawables.get(
		  report.getSmallPpath()).get()); Log.i("view", position + "起作用了"); }
		  else {
		 

		asyncImageLoader.loadDrawable(imageResource, new ImageCallback() {

			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				ImageView img = (ImageView) gridView.findViewWithTag(report
						.getSmallPpath());
				if (img != null && imageDrawable != null) {
					
					  drawables.put(report.getSmallPpath(), new
					  SoftReference<Drawable>(imageDrawable));
					 
					img.setLayoutParams(new LinearLayout.LayoutParams(120, 150));
					img.setScaleType(ImageView.ScaleType.FIT_CENTER);
					img.setImageDrawable(imageDrawable);
					Log.i("view", position + "aic");
				}
			}
		});

		 }
		if (report.getIsLoad() != null && report.getIsLoad().equals("yes")) {
			tv = (TextView) convertView.findViewById(R.id.yjbg_book_item_tv);
			bar = (ProgressBar) convertView
					.findViewById(R.id.yjbg_book_item_pro);
			bar.setVisibility(View.GONE);
			tv.setText("100%");
		}
		convertView.setLayoutParams(new AbsListView.LayoutParams(125, 210));
		return convertView;
	}
}
