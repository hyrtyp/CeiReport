package com.hyrt.cei.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private List<Integer> data;
	private String str;

	public ImageAdapter(Context c,List<Integer> data,String str) {
		mContext = c;
		this.data=data;
		this.str=str;
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

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);
			if(str.equals("ebook")){
			imageView.setLayoutParams(new GridView.LayoutParams(90, 200));
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			}
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageResource(data.get(position));
		return imageView;
	}
}
