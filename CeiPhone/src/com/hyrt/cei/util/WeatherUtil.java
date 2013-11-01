package com.hyrt.cei.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WeatherUtil {
	private static String SERVICES_HOST = "www.webxml.com.cn";
	private static String WEATHER_SERVICES_URL = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/";
	private static String PROVINCE_CODE_URL = WEATHER_SERVICES_URL
			+ "getRegionProvince";
	private static String CITY_CODE_URL = WEATHER_SERVICES_URL
			+ "getSupportCityString?theRegionCode=";
	private static String WEATHER_QUERY_URL = WEATHER_SERVICES_URL
			+ "getWeather?theUserID=&theCityCode=";

	private WeatherUtil() {
	}

	public static void main(String[] args) throws Exception {
		//getProvinceCodes();
		//System.out.println(getCityCodes("311101")); 
		//System.out.println(getWeather("1601"));
	}

	/**
	 * 查询中国的省份码集合
	 * @return
	 */
	public static String getProvinceCodes() {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader in = null;
		try {
			inputStream = getSoapInputStream(PROVINCE_CODE_URL); 
			in = new BufferedReader(new InputStreamReader(inputStream,"UTF8"));
			while(true){
				String lineStr = in.readLine();
				if(lineStr == null)
					return sb.toString();
				sb.append(lineStr + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 查询某个省份下的城市码集合
	 * @param provinceCode 省份码
	 * @return
	 */
	public static String getCityCodes(String provinceCode) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader in = null;
		try {
			inputStream = getSoapInputStream(CITY_CODE_URL + provinceCode); 
			in = new BufferedReader(new InputStreamReader(inputStream,"UTF8"));
			while(true){
				String lineStr = in.readLine();
				if(lineStr == null)
					return sb.toString();
				sb.append(lineStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static InputStream getSoapInputStream(String url) {
		InputStream inputStream = null;
		try {
			URL urlObj = new URL(url);
			URLConnection urlConn = urlObj.openConnection();
			urlConn.setRequestProperty("Host", SERVICES_HOST); // 具体webService相关
			urlConn.connect();
			inputStream = urlConn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	/**
	 * 查询天气情况
	 * @param cityCode 城市代码
	 * @return
	 */
	public static String getWeather(String cityCode) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		BufferedReader in = null;
		try {
			inputStream = getSoapInputStream(WEATHER_QUERY_URL + cityCode);
			in = new BufferedReader(new InputStreamReader(inputStream,"UTF8"));
			while(true){
				String lineStr = in.readLine();
				if(lineStr == null)
					return sb.toString();
				sb.append(lineStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
