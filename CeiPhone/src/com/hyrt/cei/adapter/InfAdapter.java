package com.hyrt.cei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.ceiphone.R;

/**
 * 政经资讯中List的适配器
 * 
 * @author Administrator
 * 
 */
public class InfAdapter extends BaseAdapter {
	private int itemLayout;
	private LayoutInflater inflater;
	private List<InfoNew> news;

	public InfAdapter(Activity activity, int itemLayout, List<InfoNew> news) {
		this.news = news;
		this.itemLayout = itemLayout;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return news.size();
	}

	public Object getItem(int position) {
		return news.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = inflater.inflate(itemLayout, null);
			holder = new ViewHolder();

			holder.subhead = (TextView) convertView.findViewById(R.id.info_subhead);
			holder.title = (TextView) convertView.findViewById(R.id.info_title);
			holder.time = (TextView) convertView.findViewById(R.id.info_time);
			convertView.setTag(holder);
		}
		if (news.size() != 0) {
			if (news.get(position).getTitle() != null) {
				holder.title.setText(news.get(position).getTitle());
			}
			if (news.get(position).getSubhead() != null) {
				holder.subhead.setText(news.get(position).getSubhead());
			}
			if (news.get(position).getTime() != null) {
				holder.time.setText(news.get(position).getTime());
			}

		}
		return convertView;
	}

	class ViewHolder {
		TextView title;
		TextView subhead;
		TextView time;
	}
}
