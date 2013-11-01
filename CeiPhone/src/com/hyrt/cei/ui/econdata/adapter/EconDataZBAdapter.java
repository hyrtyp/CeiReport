package com.hyrt.cei.ui.econdata.adapter;

import java.util.List;

import com.hyrt.cei.vo.New;
import com.hyrt.ceiphone.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EconDataZBAdapter extends BaseAdapter {
	Context context;
	List<New> data;
	private LayoutInflater inflater;
public EconDataZBAdapter(Context context,List<New> data){
	this.context=context;
	this.data=data;
	inflater=LayoutInflater.from(context);
}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(arg1==null){
			arg1=inflater.inflate(R.layout.econ_data_ea_item_group, null);
		}
		TextView tv = (TextView) arg1
				.findViewById(R.id.econ_data_ea_item_group_tv);
		if(data.get(arg0).getTitle().length()>12){
			tv.setText(data.get(arg0).getTitle().substring(0, 10)+"...");
		}else{
			tv.setText(data.get(arg0).getTitle());
		}
		
		return arg1;
	}

}
