package com.poqop.document;

import java.util.List;
import java.util.Map;

import org.vudroid.pdfdroid.PdfViewerActivity;

import com.hyrt.cei.R;
import com.poqop.document.Dao.MyReadDao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class YourReadAdapter extends BaseAdapter {
	private List<Map<String, String>> list;
	private Context context;
	private MyReadDao dao;
	private LayoutInflater factory;
	private Uri uri;
	private String name;
	public YourReadAdapter(){
		super();
	}
	public YourReadAdapter(Context context,String path,String name) {
		this.context = context;
		dao = new MyReadDao(context);
		list = dao.getAllRead(path);
		this.name=name;
		factory = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		/*int size = list.size();
		int a = size / 3;
		int b = size % 3;
		if(b > 0){
			a++;
		}*/
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public Uri getUri(){
		return uri;
	}
	
	public void deleteItem(int position){
		list.remove(position);
	}

	public String getBookName(int position){
		return list.get(position).get("bookName");
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = factory.inflate(R.layout.yjbg_pop_shuqian_item, null);
		}
		final Map<String, String> map = list.get(position);
		TextView textView=(TextView) convertView.findViewById(R.id.yjbg_pop_shuqian_title);
		textView.setText((name.length()>15?name.substring(0, 15)+"...":name)+ (Integer.parseInt(map.get("pageNo"))+1)+"页");//map.get("bookName").substring(map.get("bookName").lastIndexOf("/")+1, map.get("bookName").length())
		ImageView imageButton = (ImageView) convertView.findViewById(R.id.yjbg_report_shuqian_bianji);
		imageButton.setOnClickListener(new OnClickListener() {
				
		@Override
		public void onClick(View v) {
		// TODO Auto-generated method stub
			new AlertDialog.Builder(context).setTitle("删除")
			.setMessage("您确认要删除此书签吗？").setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
											int which) {
						dao.deleteMyRead(map.get("bookName"));
					    deleteItem(position);
					    notifyDataSetChanged();
									}
						}).setNegativeButton("取消", null).create().show();
					
					}
				});
		 return convertView;
	}
}