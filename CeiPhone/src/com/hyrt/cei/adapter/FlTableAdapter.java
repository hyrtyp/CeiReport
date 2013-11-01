package com.hyrt.cei.adapter;

import java.util.List;
import com.hyrt.cei.vo.ReportpaitElement;
import com.hyrt.ceiphone.R;
import com.hyrt.readreport.ReadReportFL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FlTableAdapter extends BaseAdapter {
	private List<ReportpaitElement> dataTable;
	private Context context;
	private LayoutInflater mInflater;
	private int index;

	public FlTableAdapter(Context context, List<ReportpaitElement> dataTable,int index) {
		this.dataTable = dataTable;
		this.context = context;
		this.index=index;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return dataTable.size();
	}

	public Object getItem(int position) {
		return dataTable.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.read_report_fltable, null);
		}
		TextView tv = (TextView) ((RelativeLayout) convertView).getChildAt(1);
		tv.setText(dataTable.get(position).getOutlineTitle());
		if(position==index){
			((ImageView) ((RelativeLayout) convertView).getChildAt(0))
			.setImageResource(R.drawable.phone_study_menu_select);
			tv.setTextColor(Color.WHITE);
		}else{
			((ImageView) ((RelativeLayout) convertView).getChildAt(0))
			.setImageResource(R.drawable.menu_transbg);
			 tv.setTextColor(Color.BLUE);
		}
		
		

		return convertView;
	}

}
