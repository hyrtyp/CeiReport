package com.hyrt.cei.adapter;

import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.ui.ebook.GoodsReportActivity;
import com.hyrt.cei.ui.ebook.SortReportActivity;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Report;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SortReportAdapter extends BaseAdapter {
	private List<Report> data;
	private LayoutInflater inflater;
	private AsyncImageLoader asyncImageLoader;
	private ListView goodList;
	public SortReportAdapter(SortReportActivity context,List<Report> data,ListView goodList){
		this.data=data;
		this.goodList=goodList;
		asyncImageLoader = ((CeiApplication) context.getApplication()).asyncImageLoader;
		inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=inflater.inflate(R.layout.good_report_item,null);
			holder.handImg=(ImageView) convertView.findViewById(R.id.iv_goodbg_hand);
			
			holder.title=(TextView) convertView.findViewById(R.id.tv_goodbg_title);
			holder.aName=(TextView) convertView.findViewById(R.id.tv_goodbg__aname);
			holder.price=(TextView) convertView.findViewById(R.id.tv_goodbg_price);
			holder.body=(TextView) convertView.findViewById(R.id.tv_goodbg_text);
			holder.download=(ImageButton) convertView.findViewById(R.id.ib_bg_download);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		final Report report=data.get(position);
		holder.handImg.setTag(report.getPpath());
		//holder.handImg.setImageResource(Integer.parseInt(data.get(position).get("hand").toString()));
		holder.title.setText(report.getName());
		holder.aName.setText(report.getAuthor());
		holder.price.setText(report.getPrice());
		holder.body.setText(report.getIntro());
		ImageResourse imageResource = new ImageResourse();
		imageResource.setIconUrl(data.get(position).getPpath()
				+ "/big.png");
		imageResource.setIconId(report.getId());
		imageResource.setIconTime(report.getProtime());
		asyncImageLoader.loadDrawable(imageResource, new ImageCallback() {

			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				ImageView img = (ImageView) goodList.findViewWithTag(report.getPpath());
				if(img!=null&&imageDrawable!=null){
					//img.setLayoutParams(new Gallery.LayoutParams(360, 160));
					img.setScaleType(ImageView.ScaleType.FIT_CENTER);
					img.setImageDrawable(imageDrawable);
				}
				
			}
		});
		holder.download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		return convertView;
	}
	public class Holder{
		ImageView handImg;
		TextView title;
		TextView aName;
		TextView price;
		TextView body;
		ImageButton download;
	}

}
