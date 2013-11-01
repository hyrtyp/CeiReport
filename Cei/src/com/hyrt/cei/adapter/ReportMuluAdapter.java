package com.hyrt.cei.adapter;

import java.util.List;

import com.hyrt.cei.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReportMuluAdapter extends BaseAdapter {
	private List<String> data;
	private Activity context;
   public ReportMuluAdapter(Activity context,List<String> data){
	   this.context=context;
	   this.data=data;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=new TextView(context);
		}
		TextView view=(TextView) convertView;
		view.setPadding(10, 10, 0, 0);
		view.setText(data.get(position));
		return convertView;
	}

}
