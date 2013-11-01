package com.hyrt.ceiphone.adapter;

import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.vo.Courseware;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseDetailAboutAdapter extends BaseAdapter {

	private List<Courseware> coursewares;
	private LayoutInflater inflater;
	private AsyncImageLoader asyncImageLoader;
	private HashMap<String, Drawable> drawables = new HashMap<String, Drawable>();
	private GridView gv;
	
	public CourseDetailAboutAdapter(Activity activity, List<Courseware> coursewares,GridView gv) {
		this.coursewares = coursewares;
		this.gv = gv;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		asyncImageLoader = ((CeiApplication) (activity.getApplication())).asyncImageLoader;
	}

	@Override
	public int getCount() {
		return coursewares.size();
	}

	@Override
	public Object getItem(int position) {
		return coursewares.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.phone_studydetail_gridview_item, null);
			holder.classIcon = (ImageView) ((LinearLayout)convertView).getChildAt(0);
			holder.title = (TextView) ((LinearLayout)convertView).getChildAt(1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String imageUrl = coursewares.get(position).getSmallPath();
		holder.classIcon.setTag(imageUrl);
		holder.title.setText("第"+ coursewares.get(position).getClassLevel()+"集");
		if (drawables.containsKey(imageUrl) && drawables.get(imageUrl) != null) {
			holder.classIcon.setImageDrawable(drawables.get(imageUrl));
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
								drawables.put(path,drawable);
							}
						}
					});
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView classIcon;
		TextView title;
	}

}
