package com.hyrt.cei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.information.Information;
import com.hyrt.cei.vo.InfoNew;
import com.hyrt.cei.webservice.service.Service;

/**
 * 政经资讯中List的适配器
 * 
 * @author Administrator
 * 
 */
public class InformationAdapter extends BaseAdapter {
	private String loginName;
	private int itemLayout;
	private LayoutInflater inflater;
	private List<InfoNew> news;
	private Activity activity;
	private String functionid;
	private String nowCount;

	public InformationAdapter(Activity activity, int itemLayout,
			List<InfoNew> news, String functionid, String nowCount) {
		this.news = news;
		this.itemLayout = itemLayout;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.activity = activity;
		this.functionid = functionid;
		this.nowCount = nowCount;
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
		// if (convertView != null) {
		// holder = (ViewHolder) convertView.getTag();
		// } else {
		convertView = inflater.inflate(itemLayout, null);
		holder = new ViewHolder();
		convertView.setTag(holder);
		holder.subhead = (TextView) convertView.findViewById(R.id.info_subhead);
		holder.title = (TextView) convertView.findViewById(R.id.info_title);
		holder.time = (TextView) convertView.findViewById(R.id.info_time);
		holder.collectMark = (ImageView) convertView
				.findViewById(R.id.info_mark);
		// }
		if(position == 0)
			convertView.setBackgroundColor(Color.WHITE);
		if (news.size() != 0) {
			if (position == 0)
			convertView.setBackgroundDrawable(null);
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
		final InfoNew infoNew = news.get(position);
		SharedPreferences settings = activity.getSharedPreferences("loginInfo",
				Activity.MODE_PRIVATE);
		loginName = settings.getString("LOGINNAME", "");
		if (loginName.equals("")) {
			holder.collectMark.setVisibility(View.GONE);
		} else {
			if (infoNew.getIsCollect().equals("0")) {
				holder.collectMark.setImageResource(R.drawable.zjzx_selectored);
				holder.collectMark.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertIsSurePop(new OnClickListener() {

							@Override
							public void onClick(View v) {
								popWin.dismiss();
								new Thread(new Runnable() {

									@Override
									public void run() {
										Service.delCollect(
												((CeiApplication) activity
														.getApplication()).columnEntry
														.getUserId(),
												news.get(position).getId(),
												news.get(position)
														.getFunctionId());
									}
								}).start();
								infoNew.setIsCollect("1");
								InformationAdapter.this.notifyDataSetChanged();
							}
						});
					}
				});
			} else {
				holder.collectMark
						.setImageResource(R.drawable.zjzx_selectored_no);
				holder.collectMark.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertIsSurePop(new OnClickListener() {

							@Override
							public void onClick(View v) {
								popWin.dismiss();
								new Thread(new Runnable() {

									@Override
									public void run() {
										Service.saveCoolect(
												((CeiApplication) activity
														.getApplication()).columnEntry
														.getUserId(),
												news.get(position).getId(),
												news.get(position)
														.getFunctionId());
									}
								}).start();
								infoNew.setIsCollect("0");
								InformationAdapter.this.notifyDataSetChanged();
							}
						});
					}
				});
			}
		}
		if (nowCount != null && nowCount.equals(news.get(position).getId())) {
			convertView.setBackgroundColor(Color.WHITE);
			Information.groupViews.add(convertView);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView collectMark;
		TextView title;
		TextView subhead;
		TextView time;
	}

	private PopupWindow popWin;

	private void alertIsSurePop(OnClickListener clickListener) {
		View popView = activity.getLayoutInflater().inflate(
				R.layout.phone_study_issure, null);
		popView.findViewById(R.id.phone_study_issure_sure_btn)
				.setOnClickListener(clickListener);
		popView.findViewById(R.id.phone_study_issure_cancel_btn)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						popWin.dismiss();
					}
				});
		popWin = new PopupWindow(popView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(activity.findViewById(R.id.full_view),
				Gravity.CENTER, 0, 0);
	}
}
