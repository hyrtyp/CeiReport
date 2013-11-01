package com.hyrt.cei.adapter;

import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.ui.ebook.PartitionReportActivity;
import com.hyrt.cei.vo.ReportpaitElement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TreeViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<ReportpaitElement> mfilelist;
	private Bitmap has;
	private Bitmap noHas;
	private int index;
	public TreeViewAdapter(Context context,
			List<ReportpaitElement> mfilelist,int index) {
		mInflater = LayoutInflater.from(context);
		this.mfilelist = mfilelist;
		this.index=index;
		has = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.yjbg_pait_piont);
		noHas = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.yjbg_pait_piont1);

	}

	public int getCount() {
		return mfilelist.size();
	}

	public Object getItem(int position) {
		return mfilelist.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.outline, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int level = mfilelist.get(position).getLevel();
			holder.icon.setPadding(25 * (level + 1), holder.icon
				.getPaddingTop(), 0, holder.icon.getPaddingBottom());
		holder.text.setText(mfilelist.get(position).getOutlineTitle());
		if(position==index){
        	holder.text.setTextColor(Color.BLACK);
        	PartitionReportActivity.thrFenL.add(holder.text);
		}
		if(mfilelist.get(position).isMhasChild()){
			holder.icon.setImageBitmap(has);
		}else{
			holder.icon.setImageBitmap(noHas);
		}
		/*if (mfilelist.get(position).isMhasChild()
				&& (mfilelist.get(position).isExpanded() == false)) {
			holder.icon.setImageBitmap(mIconCollapse);
		} else if (mfilelist.get(position).isMhasChild()
				&& (mfilelist.get(position).isExpanded() == true)) {
			holder.icon.setImageBitmap(mIconExpand);
		} */
		return convertView;
	}

	class ViewHolder {
		TextView text;
		ImageView icon;

	}

}
