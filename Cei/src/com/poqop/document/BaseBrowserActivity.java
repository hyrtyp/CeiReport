package com.poqop.document;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;


import com.hyrt.cei.R;
import com.poqop.document.Dao.MyReadDao;
import com.poqop.document.presentation.BrowserAdapter;
import com.poqop.document.presentation.UriBrowserAdapter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vudroid.pdfdroid.PdfViewerActivity;

public abstract class BaseBrowserActivity extends Activity{
    //private BrowserAdapter adapter;
    private static final String CURRENT_DIRECTORY = "currentDirectory";
    private MyReadDao dao;
    private ListView listView1;
    private ListView listView2;
    private ListView listView3;
    private  YourReadAdapter yourReadAdapter;
    File[] files;
    private ArrayList<HashMap<String, String>> name;
    private LinearLayout linearLayout;
    private View view;
    private ProgressDialog dialog;
    ArrayList<HashMap<String, String>> list;
    private int delePos;
    private int pos;
    private UriBrowserAdapter adapter;
    private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what){
    		case 1:
    			 BrowserAdapter adapter = new BrowserAdapter(BaseBrowserActivity.this,list);
		      	 listView1.setAdapter(adapter);
		      	 break;
    		case 2:
    			 Toast.makeText(BaseBrowserActivity.this, "��ǰû��pdf�ļ���", 0).show();
	    		 dialog.dismiss();
	    		 break;
    		}
    	};
    };
//    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener(){
//        @SuppressWarnings({"unchecked"})
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
//        	
//            final File file = ((AdapterView<BrowserAdapter>)adapterView).getAdapter().getItem(i);
//            if (file.isDirectory()) {
//            	
//                setCurrentDir(file);
//            }
//            else{
//            	
//                showDocument(file);
//            }
//        }
//    };
    
    private UriBrowserAdapter recentAdapter;
    private ViewerPreferences viewerPreferences;
    protected final FileFilter filter;

    public BaseBrowserActivity(){
        this.filter = createFileFilter();
    }

    protected abstract FileFilter createFileFilter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);
        Log.v("menu", "���view====");
        dialog = new ProgressDialog(BaseBrowserActivity.this);
        dialog.setMessage("���ڱ���sd���µ�����ļ�...");
        dialog.show();
        linearLayout = (LinearLayout) findViewById(R.id.ll_main);
        
//        view = getLayoutInflater().inflate(
//				R.layout.loading_dialog2, null);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//        linearLayout.addView(view, layoutParams);
        
        viewerPreferences = new ViewerPreferences(this);
        dao = new MyReadDao(this);
        name = new ArrayList<HashMap<String,String>>();
        
        final ListView browseList = initBrowserListView();
        final ListView recentListView = initRecentListView();
        final ListView yourReadListView = initYourReadListView();
        TabHost tabHost = (TabHost)findViewById(R.id.browserTabHost2);
        Log.v("menu", "...tabHost=="+tabHost);
        tabHost.setup();
        //Intent intent = new Intent(BaseBrowserActivity.this,PdfViewerActivity.class);
        tabHost.addTab(tabHost.newTabSpec("�����Ķ�").setIndicator("�����Ķ�").setContent(new TabHost.TabContentFactory()
        {
            public View createTabContent(String s)
            {
                return browseList;
            }
        }));
        tabHost.addTab(tabHost.newTabSpec("����Ķ�").setIndicator("����Ķ�").setContent(new TabHost.TabContentFactory()
        {
            public View createTabContent(String s)
            {
                return recentListView;
            }
        }));
        tabHost.addTab(tabHost.newTabSpec("��ǩ").setIndicator("��ǩ").setContent(new TabHost.TabContentFactory()
        {
            public View createTabContent(String s)
            {
                return yourReadListView;
            }
        }));
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        final File sdcardPath = new File("/sdcard");
//        if (sdcardPath.exists())
//        {
//            setCurrentDir(sdcardPath);
//        }
//        else
//        {
//            setCurrentDir(new File("/"));
//        }
//        if (savedInstanceState != null)
//        {
//            final String absolutePath = savedInstanceState.getString(CURRENT_DIRECTORY);
//            if (absolutePath != null) {
//                setCurrentDir(new File(absolutePath));
//            }
//        }
//    }

    private ListView initBrowserListView() {
    	listView1 = new ListView(this);
        //adapter = new BrowserAdapter(this, filter);
    	Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 if (Environment.getExternalStorageState().equals(  
		                 Environment.MEDIA_MOUNTED)) {  
		             File path = Environment.getExternalStorageDirectory();// ���SD��·��   
		             // File path = new File("/mnt/sdcard/");   
		             files = path.listFiles();// ��ȡ  
		    	 }
		    	 list = getFileName(files);
		    	 if(list.size() == 0){
		    		 Message msg = handler.obtainMessage(2);
			    	 handler.sendMessage(msg);
		    		 return;
		    	 }
		    	 Log.v("menu", "list===="+list);
		    	 Message msg = handler.obtainMessage(1);
		    	 handler.sendMessage(msg);
//		    	 BrowserAdapter adapter = new BrowserAdapter(BaseBrowserActivity.this,list);
//		      	 listView1.setAdapter(adapter);
		         listView1.setCacheColorHint(0);
		         dialog.dismiss();
			}
		});
    	thread.start();
    	
       // listView1.setOnItemClickListener(onItemClickListener);
        listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				 showDocument(((AdapterView<BrowserAdapter>) adapterView).getAdapter().getUri(arg2));
				 Cursor cursor = dao.getRecentRead(((AdapterView<BrowserAdapter>) adapterView).getAdapter().getUri(arg2).toString());
				 if(cursor.getCount()>0){
					 
				 }else{
					 long id = dao.addMyRecentRead(((AdapterView<BrowserAdapter>) adapterView).getAdapter().getUri(arg2));
					 Log.v("menu", "�����Ƿ�ɹ���������"+id);
				 }
			}
        	
		});
        listView1.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        return listView1;
    }
    
    private ListView initYourReadListView()  {
        listView2 = new ListView(BaseBrowserActivity.this);
        listView2.setCacheColorHint(0);
	    //yourReadAdapter = new YourReadAdapter(BaseBrowserActivity.this);
	    listView2.setAdapter(yourReadAdapter);
	    listView2.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        
        return listView2;
    }

    
    private ListView initRecentListView() {
    	listView3 = new ListView(this);
        recentAdapter = new UriBrowserAdapter(BaseBrowserActivity.this);
        listView3.setCacheColorHint(0);
        listView3.setAdapter(recentAdapter);
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @SuppressWarnings({"unchecked"})
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                showDocument(((AdapterView<UriBrowserAdapter>) adapterView).getAdapter().getItem(i));
               // dao.addMyRecentRead(recentAdapter.getItem(i));
//                Cursor cursor = dao.getRecentRead(((AdapterView<UriBrowserAdapter>) adapterView).getAdapter().getItem(i).toString());
//                cursor.moveToFirst();
//                delePos = cursor.getInt(0);
//                pos = i;
                view.setTag(i);
            }
        });
        listView3.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        
        listView3.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.add(0, 0, 0, "ɾ��");
				menu.add(0, 1, 0, "ȡ��");
				AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
				pos = info.position;
			    adapter = new UriBrowserAdapter(BaseBrowserActivity.this);
				Log.v("menu", "Ҫɾ���λ��...pos=="+pos+"....adapter(pos)=="+adapter.getItem(pos)); 
				Cursor cursor = dao.getRecentRead(adapter.getItem(pos).toString());
	            cursor.moveToFirst();
	            delePos = cursor.getInt(0);
	            Log.v("menu", "delePos===="+delePos);
			}
		});
        
        return listView3;
    }
    
    @Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int i = item.getItemId();
		switch(i){
		case 0:
			
			adapter.deleteItem(pos);
			dao.deleteMyRecentRead(delePos);
			adapter.notifyDataSetChanged();
			listView3.setAdapter(adapter);
			break;
		}
		return super.onContextItemSelected(item);
	}
    
    public void showDocument(File file)
    {
        showDocument(Uri.fromFile(file));
    }

    public abstract void showDocument(Uri uri);

//    private void setCurrentDir(File newDir)
//    {
//        adapter.setCurrentDirectory(newDir);
//        getWindow().setTitle(newDir.getAbsolutePath());
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState)
//    {
//        super.onSaveInstanceState(outState);
//        outState.putString(CURRENT_DIRECTORY, adapter.getCurrentDirectory().getAbsolutePath());
//    }

    @Override
    protected void onResume()
    {
        super.onResume();
        int color = viewerPreferences.getColor();
        if(color == 0){
        	listView1.setBackgroundColor(Color.GREEN);
        	listView2.setBackgroundColor(Color.GREEN);
        	listView3.setBackgroundColor(Color.GREEN);
        }else{
        	listView1.setBackgroundColor(Color.GRAY);
        	listView2.setBackgroundColor(Color.GRAY);
        	listView3.setBackgroundColor(Color.GRAY);
        }
         //YourReadAdapter yourReadAdapter = new YourReadAdapter(BaseBrowserActivity.this);
         listView2.setAdapter(yourReadAdapter);
         UriBrowserAdapter adapter = new UriBrowserAdapter(BaseBrowserActivity.this);
         listView3.setAdapter(adapter);
        // recentAdapter.setUris(viewerPreferences.getRecent());
    }
    
    public ArrayList<HashMap<String, String>> getFileName(File[] files) {  
        if (files != null) {// ���ж�Ŀ¼�Ƿ�Ϊ�գ�����ᱨ��ָ��   
            for (File file : files) {  
                if (file.isDirectory()) {  
                	
                    getFileName(file.listFiles());
                    
                } else {  
                    String fileName = file.getName();  
                    if (fileName.endsWith(".pdf")) { 
                    	
                       HashMap<String, String> map = new HashMap<String, String>();  
                       map.put("Name", fileName.substring(0 , fileName.lastIndexOf(".")));  
                       map.put("filePath", "file:///"+file.getPath().toString().substring(5));  
                       name.add(map);  
                       Log.i("menu", "�ļ�Ŀ¼==" + "file:///"+file.getPath().toString().substring(5));  
                       Log.i("menu", "�ļ�����==" + "file:///"+fileName.substring(0 , fileName.lastIndexOf(".")));  
                    }  
                }  
            }  
        }  
        return name;
    }  
}
