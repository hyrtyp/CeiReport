package com.hyrt.cei.adapter;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.application.CeiApplication;
import com.hyrt.cei.db.DataHelper;
import com.hyrt.cei.util.AsyncImageLoader;
import com.hyrt.cei.util.MyTools;
import com.hyrt.cei.util.NewFileDownload;
import com.hyrt.cei.util.AsyncImageLoader.ImageCallback;
import com.hyrt.cei.vo.ImageResourse;
import com.hyrt.cei.vo.Report;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BookSelfListAdapter extends BaseAdapter {
	private List<Report> data;
	private ListView listView;
	private AsyncImageLoader asyncImageLoader;
	private LayoutInflater inflater;
	DataHelper dataHelper;
	private HashMap<String, Drawable> drawables = new HashMap<String,Drawable>();

	public BookSelfListAdapter(Activity c, List<Report> data, ListView listView) {
		this.data = data;
		this.listView = listView;
		inflater = LayoutInflater.from(c);
		asyncImageLoader = ((CeiApplication) c.getApplication()).asyncImageLoader;
		dataHelper = ((CeiApplication) c.getApplication()).dataHelper;
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ProgressBar bar;
		final TextView tv;
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.yjbg_book_shelf_item, null);
			holder.handImg = (ImageView) convertView
					.findViewById(R.id.yjbg_book_shelf_hand_img);

			holder.title = (TextView) convertView
					.findViewById(R.id.yjbg_book_shelf_title);
			holder.aName = (TextView) convertView
					.findViewById(R.id.yjbg_book_shelf_aname);
			holder.price = (TextView) convertView
					.findViewById(R.id.yjbg_book_shelf_price);
			holder.body = (TextView) convertView
					.findViewById(R.id.yjbg_book_shelf_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final Report report = data.get(position);
		holder.handImg.setTag(report.getPpath());
		holder.handImg.setImageResource(R.drawable.report1);
		holder.title.setText(report.getName());
		holder.aName.setText(report.getAuthor());
		holder.price.setText(report.getPrice());
		if(report.getIntro().length()>100){
			holder.body.setText(report.getIntro().replace("\n","").subSequence(0,100)+"...");
		}else{
			holder.body.setText(report.getIntro().replace("\n",""));
		}
		
		ImageResourse imageResource = new ImageResourse();
		imageResource.setIconUrl(data.get(position).getSmallPpath().replace("/big.png", ""));
		imageResource.setIconId(data.get(position).getId());
		imageResource.setIconTime(data.get(position).getProtime());
		if (drawables.containsKey(report.getSmallPpath()
				.replace("/big.png", ""))
				&& drawables
						.get(report.getSmallPpath().replace("/big.png", "")) != null) {
			holder.handImg.setImageDrawable(drawables.get(report.getSmallPpath()
					.replace("/big.png", "")));
			Log.i("view", position + "起作用了");
		} else{
		asyncImageLoader.loadDrawable(imageResource, new ImageCallback() {

			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				ImageView img = (ImageView) listView.findViewWithTag(report
						.getPpath());
				if (img != null && imageDrawable != null) {
					// img.setLayoutParams(new Gallery.LayoutParams(360, 160));
					drawables.put(
							report.getSmallPpath().replace("/big.png", ""),
							imageDrawable);
					img.setScaleType(ImageView.ScaleType.FIT_CENTER);
					img.setImageDrawable(imageDrawable);
				}

			}
		});
		}
		tv = (TextView) convertView.findViewById(R.id.yjbg_book_list_item_tv);
		bar = (ProgressBar) convertView.findViewById(R.id.yjbg_book_list_item_pro);
		/*if (!report.getIsLoad().equals("yes")) {
			tv.setVisibility(View.VISIBLE);
			bar.setVisibility(View.VISIBLE);
			Handler handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					bar.setProgress(msg.arg1);
					tv.setText(msg.arg1 + "%");
					if (msg.arg1 == 100) {
						tv.setText("100%");
						report.setIsLoad("yes");
						dataHelper.UpdateReportZT(report);
					}
				}

			};

			String path = report.getDownpath().toString();
			File savedir = new File(Report.SD_PATH+ report.getName());
			// Environment.getExternalStorageDirectory().getAbsolutePath() + "/cei/bg/" 
			if (!savedir.exists()) {
				savedir.mkdirs();
			}

			try {
				NewFileDownload.download(path, savedir, handler);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		if (report.getIsLoad().equals("yes")) {
			tv.setText("100%");
		}
		return convertView;
	}

	public class Holder {
		ImageView handImg;
		TextView title;
		TextView aName;
		TextView price;
		TextView body;
		ImageButton download;
		ProgressBar bar;
		TextView resultView;
	}
}
