package com.hyrt.cei.adapter;

import java.util.List;
import java.util.Map;

import com.hyrt.cei.R;
import com.hyrt.cei.vo.New;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EconAdapter extends BaseAdapter {
	Context mContext;
	List<New> data;
	LayoutInflater inflater;

	public EconAdapter(Context context, List<New> data) {
		this.mContext = context;
		this.data = data;
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.econ_item, null);
		}
		TextView title = (TextView) convertView
				.findViewById(R.id.econ_data_title);
		TextView time = (TextView) convertView
				.findViewById(R.id.econ_data_time);
		TextView content = (TextView) convertView
				.findViewById(R.id.econ_data_content);
		if (data.get(position).getTitle().length() > 13) {
			title.setText(data.get(position).getTitle().replace("\n", "")
					.trim().substring(0, 12)
					+ "...");
		} else {
			title.setText(data.get(position).getTitle());
		}
		if (data.get(position).getTime().length() > 10) {
			time.setText(data.get(position).getTime().replace("\n", "").trim()
					.substring(0, 10));
		} else {
			time.setText(data.get(position).getTime());
		}
		if (data.get(position).getSubhead().length() > 36) {
			content.setText(data.get(position).getSubhead().replace("\n", "")
					.trim().substring(0, 35)
					+ "...");
		} else {
			content.setText(data.get(position).getSubhead());
		}

		// content.setText(data.get(position).getSubhead());

		return convertView;
	}

}
