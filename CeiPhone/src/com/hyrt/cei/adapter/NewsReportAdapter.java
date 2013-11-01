package com.hyrt.cei.adapter;

import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Report;
import com.hyrt.ceiphone.R;
import com.hyrt.readreport.ReadReportMainActivity;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsReportAdapter extends BaseAdapter {

	private AsyncImageLoader asyncImageLoader;
	private List<Report> data;
	private GridView gridView;
	private LayoutInflater inflater;
	private HashMap<String, Drawable> drawables = new HashMap<String, Drawable>();

	public NewsReportAdapter(ReadReportMainActivity c, GridView gridView,
			List<Report> data) {
		this.data = data;
		this.gridView = gridView;
		inflater = LayoutInflater.from(c);
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
		// if (convertView == null) {
		convertView = inflater.inflate(R.layout.read_report_main_news_item,
				null);
		// }
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.report_item_reportImg);
		imageView.setTag(data.get(position).getSmallPpath());
		TextView textView = (TextView) convertView
				.findViewById(R.id.report_item_reportName);
		textView.setText(data.get(position).getName().length()>15?data.get(position).getName().substring(0,14):data.get(position).getName());//
		ImageResourse imageResource = new ImageResourse();
		imageResource.setIconUrl(data.get(position).getSmallPpath());
		imageResource.setIconId(data.get(position).getId());
		imageResource.setIconTime(data.get(position).getProtime());
		if (drawables.containsKey(data.get(position).getSmallPpath())
				&& drawables.get(data.get(position).getSmallPpath()) != null) {
			imageView.setBackgroundDrawable(drawables.get(data.get(position)
					.getSmallPpath()));
			Log.i("view", "缓存起作用");
		} else {
			asyncImageLoader.loadDrawable(imageResource, new ImageCallback() {

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					ImageView img = (ImageView) gridView.findViewWithTag(data
							.get(position).getSmallPpath());
					if (img != null && imageDrawable != null) {
						// img.setLayoutParams(new
						// LinearLayout.LayoutParams(120, 150));
						// img.setScaleType(ImageView.ScaleType.FIT_CENTER);
						// img.setImageDrawable(imageDrawable);
						img.setBackgroundDrawable(imageDrawable);
						drawables.put(data.get(position).getSmallPpath(),
								imageDrawable);
					}
				}
			});
		}
		// imageView.setImageResource(R.drawable.bg0);
		// convertView.setLayoutParams(new A.LayoutParams(120,150));
		return convertView;
	}

}
