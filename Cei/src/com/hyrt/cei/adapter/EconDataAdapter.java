package com.hyrt.cei.adapter;

import java.util.List;
import java.util.Map;

import com.hyrt.cei.R;
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

public class EconDataAdapter extends BaseAdapter {
	Context mContext;
	private List<ColumnEntry> data;
	private LayoutInflater inflater;
	private String nowId;

	public EconDataAdapter(Context context, List<ColumnEntry> data, String nowId) {
		this.mContext = context;
		this.data = data;
		this.nowId = nowId;
		inflater = LayoutInflater.from(context);
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
		// if(convertView==null){
		convertView = inflater.inflate(R.layout.econ_data_ea_item_group, null);
		// }
		if (nowId == null && position == 0) {
			convertView.findViewById(R.id.econ_data_ea_item_group_tv)
					.setBackgroundResource(R.drawable.econ_data_ea_item_group1);

		}else if (data.get(position).getId().equals(nowId)) {
			convertView.findViewById(R.id.econ_data_ea_item_group_tv)
					.setBackgroundResource(R.drawable.econ_data_ea_item_group1);
		}
		final TextView tv = (TextView) convertView
				.findViewById(R.id.econ_data_ea_item_group_tv);
		// tv.setBackgroundColor(Color.BLUE);
		if (data.get(position).getName().length() > 10) {
			tv.setText(data.get(position).getName().substring(0, 10) + "...");
		} else {
			tv.setText(data.get(position).getName());
		}

		return convertView;
	}

}
