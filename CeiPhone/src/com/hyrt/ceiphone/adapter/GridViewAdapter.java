package com.hyrt.ceiphone.adapter;

import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.vo.ColumnEntry;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.ceiphone.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {

	private int itemLayout;
	private LayoutInflater inflater;
	private List<ColumnEntry> columnEntries;
	private AsyncImageLoader asyncImageLoader;
	private GridView gv;
	private HashMap<String, Drawable> drawables = new HashMap<String, Drawable>();

	public GridViewAdapter(Activity activity, int itemLayout,List<ColumnEntry> columnEntries, GridView gv) {
		this.itemLayout = itemLayout;
		this.columnEntries = columnEntries;
		this.gv = gv;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		asyncImageLoader = ((CeiApplication) (activity.getApplication())).asyncImageLoader;
	}

	public int getCount() {
		return columnEntries.size();
	}

	public Object getItem(int position) {
		return Integer.valueOf(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(itemLayout, null);
			holder.serviceIcon = (ImageView) convertView.findViewById(R.id.main_service_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(columnEntries.size() != 0){
			String imageUrl = columnEntries.get(position).getOperationImage();
			holder.serviceIcon.setTag(imageUrl);
			if (!imageUrl.toLowerCase().contains("null") && drawables.containsKey(imageUrl)&& drawables.get(imageUrl) != null){
				holder.serviceIcon.setImageDrawable(drawables.get(imageUrl));
			}else{
				ImageResourse imageResource = new ImageResourse();
				imageResource.setIconUrl(imageUrl);
				imageResource.setIconId(columnEntries.get(position).getId());
				imageResource.setIconTime(columnEntries.get(position).getIssueTime());
				asyncImageLoader.loadDrawable(imageResource,
						new AsyncImageLoader.ImageCallback() {
	
							@Override
							public void imageLoaded(Drawable drawable, String path) {
								ImageView imageView = (ImageView) gv.findViewWithTag(path);
								if (drawable != null && imageView != null) {
									imageView.setImageDrawable(drawable);
									drawables.put(path,drawable);
								}
							}
						});
			}
		}
		return convertView;
	}

	class ViewHolder {
		ImageView serviceIcon;
	}
}
