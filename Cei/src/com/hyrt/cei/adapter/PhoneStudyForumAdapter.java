package com.hyrt.cei.adapter;

import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.adapter.PhoneStudySelcoAdapter.ViewHolder;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.vo.Courseware;
import com.hyrt.cei.vo.Forum;
import com.hyrt.cei.vo.ImageResourse;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PhoneStudyForumAdapter extends BaseAdapter {
	private List<Courseware> coursewares;
	ListView lv;
	LayoutInflater inflater;
	AsyncImageLoader asyncImageLoader;
	private HashMap<Integer, Drawable> drawables = new HashMap<Integer, Drawable>();
    public PhoneStudyForumAdapter(Activity context,List<Courseware> coursewares,ListView lv){
    	this.coursewares=coursewares;
    	this.lv=lv;
    	inflater=LayoutInflater.from(context);
    	asyncImageLoader = ((CeiApplication) (context.getApplication())).asyncImageLoader;
    }
	@Override
	public int getCount() {
		return coursewares.size();
	}

	@Override
	public Object getItem(int arg0) {
		return coursewares.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return coursewares.get(arg0).hashCode();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHodler = null;
		if (convertView == null) {
			viewHodler = new ViewHolder();
			convertView = inflater.inflate(R.layout.phone_study_saygrouplist_listview_item, null);
			viewHodler.courseIcon = (ImageView) convertView.findViewById(R.id.phone_study_course_icon);
			viewHodler.tv1 = (TextView) convertView.findViewById(R.id.phone_study_gird_item_tv1);
			viewHodler.tv2 = (TextView) convertView.findViewById(R.id.phone_study_gird_item_tv2);
			viewHodler.tv3 = (TextView) convertView.findViewById(R.id.phone_study_gird_item_tv3);
			convertView.setTag(viewHodler);
		} else{
			viewHodler = (ViewHolder) convertView.getTag();
		}
		String imageUrl=coursewares.get(position).getSmallPath();
		viewHodler.tv1.setText(coursewares.get(position).getName());
		viewHodler.tv2.setText("讲师姓名 ： "
				+coursewares.get(position).getTeacherName());
		viewHodler.tv3.setText("发布时间 ： " +coursewares.get(position).getProTime());
		viewHodler.courseIcon.setTag(imageUrl);
		if (drawables.containsKey(Integer.valueOf(position))
				&& drawables.get(Integer.valueOf(position)) != null) {
			viewHodler.courseIcon.setImageDrawable(drawables.get(Integer.valueOf(position)));
		} else {
			ImageResourse imageResource = new ImageResourse();
			imageResource.setIconUrl(imageUrl);
			imageResource.setIconId(coursewares.get(position).getClassId());
			asyncImageLoader.loadDrawable(imageResource,
					new AsyncImageLoader.ImageCallback() {

						@Override
						public void imageLoaded(Drawable drawable,
								String path) {
							ImageView imageView = (ImageView) lv
									.findViewWithTag(path);
							if (drawable != null && imageView != null) {
								imageView.setImageDrawable(drawable);
								drawables.put(Integer.valueOf(position),
										drawable);
							}
						}
					});
		}
		return convertView;
	}
	
	class ViewHolder{
		ImageView courseIcon;
		TextView tv1;
		TextView tv2;
		TextView tv3;
	}

}
