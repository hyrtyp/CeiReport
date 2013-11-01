package com.jingdong.app.mall.utils;

import com.jingdong.app.mall.utils.HttpGroup.HttpGroupSetting;

public class HttpGroupUtils {

	public HttpGroupUtils() {
	}

	public static HttpGroup getHttpGroupaAsynPool() {
		return getHttpGroupaAsynPool(HttpGroupSetting.TYPE_JSON);
	}

	public static HttpGroup getHttpGroupaAsynPool(int i) {
		HttpGroup.HttpGroupSetting httpgroupsetting = new HttpGroup.HttpGroupSetting();
		httpgroupsetting.setType(i);
		return getHttpGroupaAsynPool(httpgroupsetting);
	}

	public static HttpGroup getHttpGroupaAsynPool(HttpGroup.HttpGroupSetting httpgroupsetting) {
		return new HttpGroup.HttpGroupaAsynPool(httpgroupsetting);
	}
}