package com.hyrt.cei.adapter;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hyrt.cei.R;

/**
 *通用Listview弹出框适配器
 * 
 */

public class WeatherPopAdapter extends BaseAdapter {
	private int itemLayout;
	private LayoutInflater inflater;
	private List<String[]> weathers;

	public WeatherPopAdapter(Activity activity, int itemLayout,List<String[]> weathers) {
		this.weathers = weathers;
		this.itemLayout = itemLayout;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return weathers.size();
	}

	public Object getItem(int position) {
		return weathers.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(itemLayout, null);
		((TextView)convertView.findViewById(R.id.weather_areaname)).setText(weathers.get(position)[0]);
		return convertView;
	}

}
