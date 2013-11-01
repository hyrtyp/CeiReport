package com.hyrt.ceiphone.adapter;

import java.util.HashMap;
import java.util.List;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.witsea.WitSeaActivity;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.webservice.service.Service;
import com.hyrt.ceiphone.R;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class WitSeaAdapter extends BaseAdapter {
	private CeiApplication application;
	private List<ColumnEntry> witSeaData;
	private LayoutInflater inflater;
	private WitSeaActivity context;
	private Handler handler;
	private GridView fullGridView;
	private HashMap<String, Drawable> drawables = new HashMap<String, Drawable>();

	public WitSeaAdapter(WitSeaActivity context, CeiApplication application,
			GridView fullGridView, List<ColumnEntry> data, Handler handler) {
		this.application = application;
		inflater = LayoutInflater.from(context);
		this.witSeaData = data;
		this.context = context;
		this.handler = handler;
		this.fullGridView = fullGridView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return witSeaData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return witSeaData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		String imageUrl = witSeaData.get(position).getOperationImage();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.collect_item, null);
			holder.img = (ImageView) convertView.findViewById(R.id.app_icon);
			holder.img.setTag(imageUrl);
			holder.bg = (ImageView) convertView
					.findViewById(R.id.download_icon);
			holder.text = (TextView) convertView
					.findViewById(R.id.toolbox_item_textview_name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if (!("").equals(witSeaData.get(position).getName()))
			holder.text.setText(witSeaData.get(position).getName());
		if (witSeaData.get(position).isSelected()) {
			holder.bg.setImageResource(R.drawable.wit_sea_item_unstall);
			holder.bg.setTag("0");
		} else {
			holder.bg.setImageResource(R.drawable.wit_sea_item_install);
			holder.bg.setTag("1");
		}
		holder.bg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				alertIsSurePop(new OnClickListener(){
					public void onClick(View v1) {
						popWin.dismiss();
						if (v.getTag().equals("1")) {
							new Thread() {
								@Override
								public void run() {
									String returnCode = Service.upDateWitSea(
											application.columnEntry.getUserId(),
											witSeaData.get(position).getId());
									handler.sendMessage(handler.obtainMessage(position,
											returnCode));
								}
							}.start();
						} else {
							handler.sendMessage(handler.obtainMessage(position));
						}
					}
				});
			}
		});
		holder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.alertBusinessIntro(witSeaData.get(position));
			}
		});
		if (drawables.containsKey(imageUrl) && drawables.get(imageUrl) != null) {
			holder.img.setImageDrawable(drawables.get(imageUrl));
		} else {
			ImageResourse imageResource = new ImageResourse();
			imageResource.setIconUrl(witSeaData.get(position)
					.getOperationImage());
			imageResource.setIconId(witSeaData.get(position).getId());
			imageResource.setIconTime(witSeaData.get(position).getIssueTime());
			application.asyncImageLoader.loadDrawable(imageResource,
					new ImageCallback() {
						@Override
						public void imageLoaded(Drawable imageDrawable,
								String imageUrl) {
							ImageView imageView = (ImageView) fullGridView
									.findViewWithTag(imageUrl);
							if (imageView != null && imageDrawable != null) {
								imageView.setImageDrawable(imageDrawable);
								drawables.put(imageUrl, imageDrawable);
							}
						}
					});
		}

		return convertView;
	}

	class Holder {
		ImageView img;
		ImageView bg;
		TextView text;
	}
	
	private PopupWindow popWin;

	private void alertIsSurePop(OnClickListener clickListener) {
		View popView = context.getLayoutInflater().inflate(
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
		popWin = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popWin.setFocusable(true);
		popWin.showAtLocation(context.findViewById(R.id.full_view), Gravity.CENTER, 0,
				0);
	}

}
