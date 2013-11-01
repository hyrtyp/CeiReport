package com.hyrt.cei.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class DataQueryAdapter extends BaseAdapter {
	List<String> data;
	Context context;
   public DataQueryAdapter(Context context,List<String> data){
	   this.data=data;
	   this.context=context;
   }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return data.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view=new TextView(context);
		view.setText(data.get(position));
		view.setTextSize(30);
		view.setTextColor(Color.BLACK);
		view.setLayoutParams(new GridView.LayoutParams(150, 30));
		return view;
	}

}
