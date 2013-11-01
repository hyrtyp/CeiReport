// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EditTextUtils.java

package com.jingdong.app.mall.utils;

import android.widget.EditText;

public class EditTextUtils
{

    public EditTextUtils()
    {
    }

    public static void setTextWithSelection(EditText edittext, String s)
    {
        edittext.setText(s);
        edittext.setSelection(s.length());
    }
}
