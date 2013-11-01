package com.hyrt.cei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyrt.cei.vo.AnnouncementNews;
import com.hyrt.ceiphone.R;

/**
 * 通知公告中List的适配器
 * 
 * 
 */
public class AnnouncementListAdapter extends BaseAdapter {
	private int itemLayout;
	private LayoutInflater inflater;
	private List<AnnouncementNews> announcementNews;

	public AnnouncementListAdapter(Activity activity, int itemLayout,
			List<AnnouncementNews> announcementNews) {
		super();
		this.itemLayout = itemLayout;
		this.announcementNews = announcementNews;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return announcementNews.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return announcementNews.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = inflater.inflate(itemLayout, null);
			holder = new ViewHolder();
			holder.time = (TextView) convertView.findViewById(R.id.tzgg_list_title);
			holder.title = (TextView) convertView.findViewById(R.id.tzgg_list_inf);
			convertView.setTag(holder);
			
		}if(announcementNews.get(position).getTitle()!=null){
			holder.title.setText(announcementNews.get(position).getTitle().toString());
		}
		if(announcementNews.get(position).getTime()!=null){
		holder.time.setText(announcementNews.get(position).getTime().toString());
		}
		return convertView;
	}

	class ViewHolder {
		TextView title;
		TextView time;
	}
}
