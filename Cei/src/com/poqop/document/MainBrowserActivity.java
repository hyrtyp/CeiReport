package com.poqop.document;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import org.vudroid.pdfdroid.PdfViewerActivity;

public class MainBrowserActivity extends BaseBrowserActivity{
    private final static HashMap<String, Class<? extends Activity>> extensionToActivity = new HashMap<String, Class<? extends Activity>>();
    Context context;
    static
    {
        extensionToActivity.put("pdf", PdfViewerActivity.class);
    }

    @Override
    protected FileFilter createFileFilter()
    {
        return new FileFilter()
        {
            public boolean accept(File pathname)
            {
                for (String s : extensionToActivity.keySet())
                {
                    if (pathname.getName().endsWith("." + s)) return true;
                }
                return pathname.isDirectory();
            }
        };
    }

    public  void showDocument(Uri uri)
    {
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        String uriString = uri.toString();
        String extension = uriString.substring(uriString.lastIndexOf('.') + 1);
        Log.v("menu", "extension=="+extension);
        intent.setClass(this, extensionToActivity.get(extension));
        startActivity(intent);
        ViewerPreferences preferences = new ViewerPreferences(MainBrowserActivity.this);
        preferences.putYourReads(uriString);
      
    }
    
    public MainBrowserActivity (){
    	super();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if(keyCode == KeyEvent.KEYCODE_BACK ){
    		new AlertDialog.Builder(MainBrowserActivity.this).setTitle("退出")
			.setMessage("您确认要离开吗？").setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int which) {
							MainBrowserActivity.this.finish();
						}
					}).setNegativeButton("取消", null).create().show();
    	}
    	return super.onKeyDown(keyCode, event);
    }
}
