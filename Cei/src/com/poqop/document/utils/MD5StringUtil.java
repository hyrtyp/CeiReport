package com.poqop.document.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class MD5StringUtil
{
    private static final MessageDigest digest;

    static
    {
        try
        {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String md5StringFor(String s)
    {
        final byte[] hash = digest.digest(s.getBytes());
        final StringBuilder builder = new StringBuilder();
        for (byte b : hash)
        {
            builder.append(Integer.toString(b & 0xFF, 16));    
        }
        return builder.toString();
    }
    
    public static String inputStreamToString(InputStream inStream)throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {// 把字符串读取到buffer，并判断是否读取到结尾
			outSteam.write(buffer, 0, len);// 把buffer写入到输出流
		}
		outSteam.close();// 读取完毕关闭输出流
		// 把输出流根据UTF-8编码转为字符串对象返回
		return new String(outSteam.toByteArray(), "utf-8");
	}
    
    public static List<HashMap<String,String>> parseXml(InputStream is){
    	List<HashMap<String, String>> list = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		NodeList nodeList;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(is);
			Element root = document.getDocumentElement();
			nodeList = root.getElementsByTagName("que");
			if(nodeList==null || nodeList.getLength()==0) {
				return null;
			}
			list = new ArrayList<HashMap<String, String>>();
			for(int i=0;i<nodeList.getLength();i++) {
				Element element = (Element) nodeList.item(i); //<person>
				HashMap<String, String> map = new HashMap<String, String>();
				
				NodeList nameList = element.getElementsByTagName("name");
				Element nameEle = (Element) nameList.item(0);  //<name>
				String name = nameEle.getTextContent();
				map.put("name", name);
				
				NodeList ageList = element.getElementsByTagName("page");
				Element ageEle = (Element) ageList.item(0);  //<name>
				String page = ageEle.getTextContent();
				map.put("page", page);
				list.add(map);
			    }
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  return list;
		}
}
