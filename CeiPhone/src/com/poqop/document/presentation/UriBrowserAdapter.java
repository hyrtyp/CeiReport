package com.poqop.document.presentation;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyrt.ceiphone.R;
import com.poqop.document.Dao.MyReadDao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UriBrowserAdapter extends BaseAdapter{
	private List<Uri> uris;
	private Context context;
	private MyReadDao dao;
	private LayoutInflater factory;
	private Uri uri;
	public UriBrowserAdapter(){
		super();
	}
	public UriBrowserAdapter(Context context) {
		this.context = context;
		dao = new MyReadDao(context);
		uris = dao.getAllRecentRead();
		factory = LayoutInflater.from(context);
	}
    //private List<Uri> uris = Collections.emptyList();

    public int getCount()
    {Log.v("menu", "uris.size==="+uris.size()+"...uri=="+uris);
    	 return uris.size();
         
    }

    public Uri getItem(int i)
    {
        return uris.get(i);
    }

    public long getItemId(int i)
    {
        return i; 
    }
    
    public void deleteItem(int i){
    	uris.remove(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup)
    {
        final View browserItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.read_report_browseritem, viewGroup, false);
        final ImageView imageView = (ImageView) browserItem.findViewById(R.id.browserItemIcon);
        final Uri uri = uris.get(i);
        final TextView textView = (TextView) browserItem.findViewById(R.id.browserItemText);
        textView.setText(uri.getLastPathSegment());
        imageView.setImageResource(R.drawable.icon);
        return browserItem;
    }

//    public void setUris(List<Uri> uris)
//    {
//        this.uris = uris;
//        notifyDataSetInvalidated();
//    }
}
