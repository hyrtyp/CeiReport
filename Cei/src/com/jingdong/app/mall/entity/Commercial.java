// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Commercial.java

package com.jingdong.app.mall.entity;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.jingdong.app.mall.utils.*;
import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONException;

public class Commercial
    implements Serializable
{

    public Commercial()
    {
    }

    public static boolean isAdd(Commercial commercial)
    {
        boolean flag;
        if(TextUtils.isEmpty(commercial.getId()))
            flag = false;
        else
            flag = true;
        return flag;
    }

    public Drawable getDrawable()
    {
        return drawable;
    }

    public String getFeature()
    {
        return feature;
    }

    public String getHorizontalImg()
    {
        return horizontalImg;
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public String getVerticalImg()
    {
        return verticalImg;
    }

    public String getWareIds()
    {
        return wareIds;
    }

    public void setDrawable(Drawable drawable1)
    {
        drawable = drawable1;
    }

    public void setFeature(String s)
    {
        feature = s;
    }

    public void setHorizontalImg(String s)
    {
        horizontalImg = s;
    }

    public void setId(String s)
    {
        id = s;
    }

    public void setTitle(String s)
    {
        title = s;
    }

    public void setVerticalImg(String s)
    {
        verticalImg = s;
    }

    public void setWareIds(String s)
    {
        wareIds = s;
    }

    public static final int FOCUSACTIVITY = 0;
    private static final long serialVersionUID = 1L;
    private Drawable drawable;
    private String feature;
    private String horizontalImg;
    private String id;
    private String title;
    private String verticalImg;
    private String wareIds;
}
