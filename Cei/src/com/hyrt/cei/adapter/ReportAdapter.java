package com.hyrt.cei.adapter;

import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.ebook.ReadReportActivity;
import com.hyrt.cei.ui.ebook.view.GalleryFlow;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Report;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ReportAdapter extends BaseAdapter {

	private ReadReportActivity mContext;
	private AsyncImageLoader asyncImageLoader;
	private List<Report> data;
	private Gallery gallery;
	private HashMap<String,Drawable> drawables = new HashMap<String,Drawable>();

	public ReportAdapter(ReadReportActivity c, Gallery gallery,
			List<Report> data) {
		mContext = c;
		this.data = data;
		this.gallery = gallery;
		asyncImageLoader = ((CeiApplication) c.getApplication()).asyncImageLoader;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ImageView imageView;

		if (convertView == null) {
			imageView = new ImageView(mContext);
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setTag(data.get(position).getPpath());
		ImageResourse imageResource = new ImageResourse();
		imageResource.setIconUrl(data.get(position).getPpath());
		imageResource.setIconId(data.get(position).getId());
		imageResource.setIconTime(data.get(position).getProtime());
		if (drawables.containsKey(data.get(position).getPpath())
				&& drawables.get(data.get(position).getPpath()) != null) {
			imageView.setImageDrawable(drawables.get(
					data.get(position).getPpath()));
			drawables.remove(data.get(position).getPpath());
			Log.i("view", "缓存起作用");
		} else {
			asyncImageLoader.loadDrawable(imageResource, new ImageCallback() {

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					ImageView img = (ImageView) gallery.findViewWithTag(data
							.get(position).getPpath());
					if (img != null && imageDrawable != null) {
						drawables.put(data.get(position).getPpath(),
								imageDrawable);
						img.setLayoutParams(new Gallery.LayoutParams(300, 200));
					   // img.setScaleType(ImageView.ScaleType.FIT_CENTER);
						img.setImageDrawable(imageDrawable);
					}
				}
			});
		}
		imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
		return imageView;
	}

}
