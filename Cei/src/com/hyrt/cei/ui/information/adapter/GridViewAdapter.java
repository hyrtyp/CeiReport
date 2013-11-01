package com.hyrt.cei.ui.information.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyrt.cei.R;
import com.hyrt.cei.vo.ColumnEntry;

public class GridViewAdapter extends BaseAdapter {

	private List<ColumnEntry> columnEntries;
	private LayoutInflater inflater;
	private int width;
	private Context context;

	public GridViewAdapter(Context context, List<ColumnEntry> columnEntries) {
		this.columnEntries = columnEntries;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		width = metric.widthPixels;
		this.context = context;
	}

	@Override
	public int getCount() {
		return columnEntries.size();
	}

	@Override
	public Object getItem(int position) {
		return columnEntries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout item = (RelativeLayout) inflater.inflate(
				R.layout.phone_study_gridview_item, null);
		item.setLayoutParams(new GridView.LayoutParams(
				getCount() >= 7 ? width / 7 : width / getCount(), 40));
		// if(position == 0 &&
		// ((Activity)context).getIntent().getBooleanExtra(("isMenu"), true))
		// ((ImageView)item.getChildAt(0)).setImageResource(R.drawable.common_menubg_select);
		if (((Activity) context).getIntent().getStringExtra("topNum") != null) {
			int num = Integer.parseInt(((Activity) context).getIntent()
					.getStringExtra("topNum"));
			num = 0;
			if (position == num) {
				((ImageView)item.getChildAt(0)).setImageResource(R.drawable.common_menubg_select);
			}
		}
		TextView tv = (TextView) item.getChildAt(1);
		tv.setText(columnEntries.get(position).getName());
		return item;
	}

}
