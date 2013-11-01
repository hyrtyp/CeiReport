package com.hyrt.cei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.webservice.service.Service;

/**
 * 通用Listview弹出框适配器
 * 
 */

public class CommonPopAdapter extends BaseAdapter {
	private int itemLayout;
	private LayoutInflater inflater;
	private List<InfoNew> news;
	private Activity activity;

	public CommonPopAdapter(Activity activity, int itemLayout,
			List<InfoNew> news) {
		this.activity = activity;
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

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = inflater.inflate(itemLayout, null);
			holder.subhead = (TextView) convertView
					.findViewById(R.id.info_subhead);
			holder.title = (TextView) convertView.findViewById(R.id.info_title);
			holder.time = (TextView) convertView.findViewById(R.id.info_time);
			holder.mark = (ImageView) convertView
					.findViewById(R.id.common_listpop_listviewitem_mark);
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
		if (news.get(position).getIsCollect().equals("0")) {
			holder.mark.setVisibility(View.VISIBLE);
		} else {
			holder.mark.setVisibility(View.GONE);
		}
		holder.mark.setOnClickListener(new OnClickListener() {

			Handler handler = new Handler() {
				@Override
				public void dispatchMessage(Message msg) {
					news.remove(position);
					// 进行移除操作
					CommonPopAdapter.this.notifyDataSetChanged();
				}
			};

			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						Service.delCollect(((CeiApplication) activity
								.getApplication()).columnEntry.getUserId(),
								news.get(position).getId(), news.get(position).getFunctionId());
						handler.sendMessage(handler.obtainMessage());
					}
				}).start();

			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView mark;
		TextView title;
		TextView subhead;
		TextView time;
	}
}
