package com.hyrt.cei.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CommonUtil {
	
	/**
	 * 数组去重
	 * 
	 * @param userId
	 * @param response
	 */
	public static String[] array_unique(String[] strs) {
		// array_unique
		List<String> list = new LinkedList<String>();
		for (int i = 0; i < strs.length; i++) {
			if (!list.contains(strs[i])) {
				list.add(strs[i]);
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	// 日期格式化
	public static String formatDate(Date time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(time);
	}
	
	public static String getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		// s = s * EARTH_RADIUS;
		s = s * 6378.137;
		// s = Math.round(s * 10000) / 10000;
		// 精确到米
		s = s * 1000;
		String targetStr = s+"";
		if(targetStr.indexOf(".")!=-1){
			targetStr = targetStr.substring(0,targetStr.lastIndexOf("."));
		}
		return targetStr;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
}
