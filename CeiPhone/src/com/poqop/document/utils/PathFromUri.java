package com.poqop.document.utils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class PathFromUri
{
    public static String retrieve(ContentResolver resolver, Uri uri)
    {
    	Log.v("menu", "uri.getScheme()=="+uri.getScheme()+"....uri=="+uri);
        if (uri.getScheme().equals("file"))
        {
            return uri.getPath();
        }
        final Cursor cursor = resolver.query(uri, new String[]{"_data"}, null, null, null);
        Log.v("menu", "cursor=="+cursor);
        if (cursor.moveToFirst())
        {
            return cursor.getString(0);
        }
        throw new RuntimeException("Can't retrieve path from uri: " + uri.toString());
    }
}
