package com.hyrt.cei.adapter;

import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.ui.econdata.EconGoodDataActivity;
import com.hyrt.cei.ui.econdata.EconZBQueryActivity;
import com.hyrt.cei.vo.New;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EconDataZBAdapter extends BaseAdapter {
	EconGoodDataActivity context;
	List<New> data;
	private LayoutInflater inflater;
	private String nowId;
	private EconZBQueryActivity activity;
public EconDataZBAdapter(EconGoodDataActivity context,List<New> data,String nowId){
	this.context=context;
	this.data=data;
	this.nowId=nowId;
	inflater=LayoutInflater.from(context);
}

public EconDataZBAdapter(EconZBQueryActivity activity,List<New> data,String nowId){
	this.activity = activity;
	this.data=data;
	this.nowId=nowId;
	inflater=LayoutInflater.from(activity);
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
		//if(arg1==null){
			arg1=inflater.inflate(R.layout.econ_data_ea_item_group, null);
		//}
		
		TextView tv=(TextView) arg1.findViewById(R.id.econ_data_ea_item_group_tv);
		if(data.get(arg0).getId().equals(nowId==null?data.get(0).getId():nowId)){
			tv.setBackgroundResource(R.drawable.econ_data_ea_item_group1);
			if(activity != null)
				activity.groupViews.add(arg1);
			if(context != null)
				context.groupViews.add(arg1);
		}
		if(data.get(arg0).getTitle().length()>13){
			tv.setText(data.get(arg0).getTitle().substring(0, 12)+"...");
		}else{
			tv.setText(data.get(arg0).getTitle());
		}
		
		return arg1;
	}

}
