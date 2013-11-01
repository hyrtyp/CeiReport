package com.hyrt.cei.ui.econdata.adapter;

import java.util.List;

import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.ceiphone.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	private List<ColumnEntry> columnEntries;
	private LayoutInflater inflater;
	private int width;
	private Context context;

	public GridViewAdapter(Context context, List<ColumnEntry> columnEntries) {
		this.columnEntries = columnEntries;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		width = metric.widthPixels;
		this.context = context;
	}

	@Override
	public int getCount() {
		return columnEntries.size();
	}

	@Override
	public Object getItem(int position) {
		return columnEntries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout item = (RelativeLayout) inflater.inflate(
				R.layout.phone_study_gridview_item, null);
		item.setLayoutParams(new GridView.LayoutParams(
				getCount() >= 4 ? width / 4 : width / getCount(), 40));
		if (((Activity) context).getIntent().getStringExtra("topNum") != null 
				&& Integer.parseInt(((Activity) context).getIntent().getStringExtra("topNum"))==position) {
				((ImageView) item.getChildAt(0)).setImageResource(R.drawable.phone_study_menu_select);
				((TextView) item.getChildAt(1)).setTextColor(Color.WHITE);
		}else if(position == 0){
			((ImageView)item.getChildAt(0)).setImageResource(R.drawable.phone_study_menu_select);
			((TextView) item.getChildAt(1)).setTextColor(Color.WHITE);
		}else{
			((TextView) item.getChildAt(1)).setTextColor(Color.BLUE);
		}
		TextView tv = (TextView) item.getChildAt(1);
		tv.setText(columnEntries.get(position).getName());
		return item;
	}
	
}
