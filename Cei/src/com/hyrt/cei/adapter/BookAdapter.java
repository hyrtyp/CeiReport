package com.hyrt.cei.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.ui.ebook.view.util.IAdapter;
import com.poqop.document.Page;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;



public class BookAdapter implements IAdapter{
	private ArrayList<String> strList = new ArrayList<String>();
	
	private Context mContext;
	public BookAdapter(Context context) {
		super();
		this.mContext = context;
	}
	public void addItem(List<String> list){
		strList.addAll(list);
	}
	public int getCount() {
		//return strList.size();
		return strList.size();
	}

	public String getItem(int position) {
		return strList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position) {
		ImageView image=new ImageView(mContext);
		//BitmapDrawable bitmap=(BitmapDrawable) BitmapDrawable.createFromPath("sdcard/pdfimage/"+position+".jpg");
		//image.setImageDrawable(bitmap);
		switch (position) {
		case 0:
			image.setImageResource(R.drawable.book_0);
			break;
		case 1:
			image.setImageResource(R.drawable.book_1);
			break;
		case 2:
			image.setImageResource(R.drawable.book_2);
			break;
		case 3:
			image.setImageResource(R.drawable.book_3);
			break;
		case 4:
			image.setImageResource(R.drawable.book_4);
			break;
		case 5:
			image.setImageResource(R.drawable.book_5);
			break;
		case 6:
			image.setImageResource(R.drawable.book_6);
			break;
		case 7:
			image.setImageResource(R.drawable.book_7);
			break;
		case 8:
			image.setImageResource(R.drawable.book_8);
			break;
		case 9:
			image.setImageResource(R.drawable.book_0);
			break;
		default:
			image.setImageResource(R.drawable.book_0);
			break;
		}
		//image.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		image.setLayoutParams(new FrameLayout.LayoutParams(1024,768));
		return image;
		
	}

}
