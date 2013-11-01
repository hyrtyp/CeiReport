package com.hyrt.cei.adapter;

import java.util.List;
import java.util.Map;

import com.hyrt.cei.R;
import com.hyrt.cei.ui.econdata.EconDataMain;
import com.hyrt.cei.ui.econdata.EconDateNumberActivity;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.New;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EconSecondDataAdapter extends BaseAdapter {
	Context mContext;
	private List<New> data;
	private LayoutInflater inflater;
	private String nowID;
	public EconSecondDataAdapter(Context context,List<New> data,String nowID){
		this.mContext=context;
		this.data=data;
		this.nowID=nowID;
		inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//if(convertView==null){
		convertView=inflater.inflate(R.layout.econ_data_ea_item_child, null);
	//}
	TextView tv=(TextView) convertView.findViewById(R.id.econ_data_ea_item_child_tv);
	if(data.get(position).getId().equals(nowID)){
		tv.setTextColor(Color.BLUE);
		EconDataMain.childViews.add(tv);
	}else{
		tv.setTextColor(Color.BLACK);
	}
	if(data.get(position).getTitle().length()>10){
		tv.setText(data.get(position).getTitle().substring(0,10)+"...");
	}else{
		tv.setText(data.get(position).getTitle());
	}
	
	return convertView;
	}

}
