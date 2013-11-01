// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TimerUntil.java
package com.jingdong.app.mall.utils;

public class TimerUntil {

	public static final String CHANGED_TIME = "changed_time";
	public static final String USER_TIME = "user_time";
	public static long endTime;
	public static boolean isChangedDate;
	public static long startTime;

	public TimerUntil() {
	}

	public static long getUserTime() {
		long l = 0L;
		if ((endTime - startTime) / 1000L > l)
			l = (endTime - startTime) / 1000L;
		return l;
	}

}
