// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DPIUtil.java

package com.jingdong.app.mall.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
//import com.jingdong.app.mall.MyApplication;

// Referenced classes of package com.jingdong.app.mall.utils:
//            Log

public class DPIUtil
{

    public DPIUtil()
    {
    }

    public static int dip2px(float f)
    {
        return (int)(0.5F + f * mDensity);
    }

    public static Display getDefaultDisplay()
    {
        //if(defaultDisplay == null)
            //defaultDisplay = ((WindowManager)MyApplication.getInstance().getSystemService("window")).getDefaultDisplay();
        return defaultDisplay;
    }

    public static float getDensity()
    {
        return mDensity;
    }

    public static int getHeight()
    {
        return getDefaultDisplay().getHeight();
    }

    public static int getWidth()
    {
        return getDefaultDisplay().getWidth();
    }

    public static int percentHeight(float f)
    {
        return (int)(f * (float)getHeight());
    }

    public static int percentWidth(float f)
    {
        return (int)(f * (float)getWidth());
    }

    public static int px2dip(Context context, float f)
    {
        return (int)(0.5F + f / mDensity);
    }

    public static void setDensity(float f)
    {
        mDensity = f;
    }

    private static Display defaultDisplay;
    private static float mDensity;
}
