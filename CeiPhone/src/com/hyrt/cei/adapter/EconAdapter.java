package com.hyrt.cei.adapter;

import java.util.List;
import java.util.Map;

import com.hyrt.cei.vo.New;
import com.hyrt.ceiphone.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @经济数据主页listview中适配的数据
 * @author tmy
 * 
 */

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
		return data.get(position % 2);
	}

	@Override
	public long getItemId(int position) {

		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.econ_data_listview, null);
			holder = new ViewHolder();
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.econ_news_title_text);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.econ_news_time_text);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.econ_news_content_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_title.setText(data.get(position).getTitle());

		if (data.get(position).getTime().length() > 10) {
			holder.tv_time.setText(data.get(position).getTime()
					.replace("\n", "").trim().substring(0, 10));
		} else {
			holder.tv_time.setText(data.get(position).getTime());
		}

		if (data.get(position).getSubhead() != null
				&& !data.get(position).getSubhead().equals("")) {
			if (data.get(position).getSubhead().length() > 43) {
				holder.tv_content.setText(data.get(position).getSubhead()
						.replace("\n", "").trim().substring(0, 42)
						+ "...");
			} else if (data.get(position).getSubhead().length() > 20
					&& data.get(position).getSubhead().length() < 23) {

				holder.tv_content.setText(data.get(position).getSubhead()
						.replace("\n", "").trim().substring(0, 20)
						+ "...");
			} else {
				holder.tv_content.setText(data.get(position).getSubhead());
			}
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_title;
		TextView tv_time;
		TextView tv_content;
	}

}
