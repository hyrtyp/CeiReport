package com.hyrt.cei.util;

import java.util.Comparator;

import com.hyrt.cei.vo.Report;

@SuppressWarnings("rawtypes")
public class ComparatorReport implements Comparator {
	private String str;
    public ComparatorReport(String str){
    	this.str=str;
    }
	public int compare(Object arg0, Object arg1) {
		Report user0 = (Report) arg0;
		Report user1 = (Report) arg1;
		if(str!=null&&str.equals("name")){
			return user0.getName().compareTo(user1.getName());
		}else if(str!=null&&str.equals("time")){
			return user1.getReadtime().compareTo(user0.getReadtime());
		}
		return 0;
	}
}
