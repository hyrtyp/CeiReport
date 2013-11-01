// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ImageUtil.java

package com.jingdong.app.mall.utils;

import android.graphics.*;
import android.graphics.drawable.Drawable;

// Referenced classes of package com.jingdong.app.mall.utils:
//            DPIUtil

public class ImageUtil
{

    public ImageUtil()
    {
    }

    private static Bitmap drawableToBitmap(Drawable drawable)
    {
        int i = drawable.getIntrinsicWidth();
        int j = drawable.getIntrinsicHeight();
        android.graphics.Bitmap.Config config;
        Bitmap bitmap;
        Canvas canvas;
        if(drawable.getOpacity() != -1)
            config = android.graphics.Bitmap.Config.ARGB_8888;
        else
            config = android.graphics.Bitmap.Config.RGB_565;
        bitmap = Bitmap.createBitmap(i, j, config);
        canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, i, j);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmapFromByteArray(byte abyte0[], int i, int j)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length, options);
        options.inJustDecodeBounds = false;
        boolean flag = false;
        if(options.outHeight < options.outWidth)
            flag = true;
        int k;
        if(flag)
            k = options.outHeight / j;
        else
            k = options.outWidth / i;
        if(k <= 0)
            k = 1;
        options.inSampleSize = k;
        return BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length, options);
    }

    public static Bitmap getBitmapFromFile(String s, int i, int j)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(s, options);
        options.inJustDecodeBounds = false;
        boolean flag = false;
        if(options.outHeight < options.outWidth)
            flag = true;
        int k;
        if(flag)
            k = options.outHeight / j;
        else
            k = options.outWidth / i;
        if(k <= 0)
            k = 1;
        options.inSampleSize = k;
        return BitmapFactory.decodeFile(s, options);
    }

    public static Bitmap getRoundedCornerBitmap(Drawable drawable, float f)
    {
        Bitmap bitmap = drawableToBitmap(drawable);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectf = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawRoundRect(rectf, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return bitmap1;
    }

    public static Bitmap toRoundCorner(Bitmap bitmap)
    {
        return toRoundCorner(bitmap, 6);
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, int i)
    {
        int j = DPIUtil.dip2px(i);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectf = new RectF(rect);
        float f = j;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        canvas.drawRoundRect(rectf, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        return bitmap1;
    }

    public static final int DEFAULT_DP = 6;
    public static final int UN_KNOW = -1;
}
