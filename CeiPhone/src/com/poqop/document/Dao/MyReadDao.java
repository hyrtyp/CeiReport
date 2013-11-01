package com.poqop.document.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.poqop.document.DBHelper.MyDBHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyReadDao {
	private MyDBHelper myDBHelper;
	
	public MyReadDao(Context context){
		super();
		this.myDBHelper = new MyDBHelper(context);
	}
	
	public long addMyRead(String pdfPath,String pageNo,String key){
		SQLiteDatabase database = myDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("key", key);
		values.put("book_path", pdfPath);
		values.put("pageNo", pageNo);
		long id = database.insert("myRead_table", null, values);
		return id;
	}
	public boolean getMyRead(String page,String path){
		SQLiteDatabase database = myDBHelper.getWritableDatabase();
		Cursor cursor = database.query("myRead_table", null, "pageNo = ? and book_path = ?", new String[]{page,path}, null, null, null);
		boolean b = cursor.moveToFirst();
		cursor.getColumnCount();
		cursor.close();
		return b;
	}
	public List<Map<String, String>> getAllRead(String book_path){
		List<Map<String, String>> data = new ArrayList<Map<String,String>>();
		SQLiteDatabase db = myDBHelper.getReadableDatabase();
		Cursor cursor = db.query("myRead_table", null,  "book_path = ?", 
				new String[]{book_path}, null, null, "id desc");
		cursor.moveToPosition(-1);
		while(cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("key",  cursor.getString(cursor.getColumnIndex("key")));
			map.put("bookName", cursor.getString(cursor.getColumnIndex("book_path")));
			map.put("pageNo", cursor.getString(cursor.getColumnIndex("pageNo")));
			data.add(map);
		}
		return data;
	}
	
	public  int updateMyRead(String pdfPath,String pageNo,String title){
		ContentValues values = new ContentValues();
		values.put("book_path", pdfPath);
		values.put("pageNo", pageNo);
		values.put("title", title);
		return myDBHelper.getWritableDatabase().update("myRead_table", values,
				"book_path=?", new String[] { pdfPath});
	}
	
	public void deleteMyRead(String pdfPath) {
		SQLiteDatabase db = myDBHelper.getReadableDatabase();
		db.delete("myRead_table", "book_path=?", new String[]{pdfPath});
	}
	
	public HashMap<String, String>  queryRead(String book_path){
		HashMap<String, String> hashMap = new HashMap<String, String>();
		Cursor cursor = myDBHelper.getReadableDatabase().query("myRead_table", null, "book_path = ?", 
				new String[]{book_path}, null, null, null);
		cursor.moveToPosition(-1);
		while(cursor.moveToNext()){
			hashMap.put("bookName", cursor.getString(1));
			hashMap.put("pageNo", cursor.getString(2));
			hashMap.put("title", cursor.getString(3));
			
		}
		return hashMap;
	}
	
	public long addMyRecentRead(Uri uri){
		SQLiteDatabase database = myDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("book_path", uri.toString());
		long id = database.insert("myRecentRead_table", null, values);
		return id;
	}
	
	public void deleteMyRecentRead(int id) {
		SQLiteDatabase db = myDBHelper.getReadableDatabase();
		db.delete("myRecentRead_table", "id=?", new String[]{id+""});
	}
	
	public List<Uri> getAllRecentRead(){
		List<Uri> data = new ArrayList<Uri>();
		SQLiteDatabase db = myDBHelper.getReadableDatabase();
		Cursor cursor = db.query("myRecentRead_table", null, null, null, null, null, "id desc");
		cursor.moveToPosition(-1);
		while(cursor.moveToNext()) {
			
			Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex("book_path")));
			data.add(uri);
		}
		return data;
	}
	
	public Cursor getRecentRead(String book_path){
		SQLiteDatabase db = myDBHelper.getReadableDatabase();
		Cursor cursor = db.query("myRecentRead_table", null, "book_path = ?", new String[]{book_path}, null, null, null);
		return cursor;
	}
}