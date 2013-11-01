package com.hyrt.cei.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.ImageResourse;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class PhoneStudyGridAdapter extends BaseAdapter {

	private int itemLayout;
	private LayoutInflater inflater;
	private List<Courseware> coursewares;
	private AsyncImageLoader asyncImageLoader;
	private GridView gv;
	private HashMap<String, Drawable> drawables = new HashMap<String, Drawable>();

	public PhoneStudyGridAdapter(Activity activity, int itemLayout,
			List<Courseware> coursewares, GridView gv) {
		this.itemLayout = itemLayout;
		this.coursewares = coursewares;
		this.gv = gv;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		asyncImageLoader = ((CeiApplication) (activity.getApplication())).asyncImageLoader;
	}

	public int getCount() {
		return coursewares.size();
	}

	public Object getItem(int position) {
		return Integer.valueOf(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public void clearBitmaps() {
		Iterator<String> iterator = drawables.keySet().iterator();
		while (iterator.hasNext()) {
			String path = iterator.next();
			Drawable drawable = drawables.get(path);
			if (((BitmapDrawable) drawable) != null)
				((BitmapDrawable) drawable).getBitmap().recycle();
			drawable = null;
		}
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(itemLayout, null);
			holder.courseIcon = (ImageView) convertView
					.findViewById(R.id.phone_study_gird_item_pic);
			holder.tv1 = (TextView) convertView
					.findViewById(R.id.phone_study_gird_item_tv1);
			holder.tv2 = (TextView) convertView
					.findViewById(R.id.phone_study_gird_item_tv3);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (coursewares.size() != 0) {
			String imageUrl = coursewares.get(position).getSmallPath();
			holder.tv1
					.setText(coursewares.get(position).getName().length() > 10 ? coursewares
							.get(position).getName().substring(0, 10)
							+ "..."
							: coursewares.get(position).getName());
			holder.tv2.setText(coursewares.get(position).getTeacherName()
					+ "  " + coursewares.get(position).getProTime());
			holder.courseIcon.setTag(imageUrl);
			if (!imageUrl.toLowerCase().contains("null")
					&& drawables.containsKey(imageUrl)
					&& drawables.get(imageUrl) != null) {
				holder.courseIcon.setImageDrawable(drawables.get(imageUrl));
			} else {
				ImageResourse imageResource = new ImageResourse();
				imageResource.setIconUrl(imageUrl);
				imageResource.setIconId(coursewares.get(position).getClassId());
				asyncImageLoader.loadDrawable(imageResource,
						new AsyncImageLoader.ImageCallback() {

							@Override
							public void imageLoaded(Drawable drawable,
									String path) {
								ImageView imageView = (ImageView) gv
										.findViewWithTag(path);
								if (drawable != null && imageView != null) {
									imageView.setImageDrawable(drawable);
									drawables.put(path, drawable);
								}
							}
						});
			}
		}
		return convertView;
	}

	class ViewHolder {
		ImageView courseIcon;
		TextView tv1;
		TextView tv2;
	}
}
