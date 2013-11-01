package com.jingdong.app.mall.utils;

import java.io.*;

public class IOUtil
{
    public static interface ProgressListener
    {

        public abstract void notify(int i, int j);
    }


    public IOUtil()
    {
    }

    public static byte[] readAsBytes(InputStream inputstream, ProgressListener progresslistener)
        throws Exception
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try{
        	byte[]  buf = new byte[bufferSize];
        	int len = 0;
        	int progress = 0;
        	while((len = inputstream.read(buf)) !=-1){
        		os.write(buf,0,len);
        		progress += len;
        		progresslistener.notify(progress,len);
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	os.close();
        	inputstream.close();
        }
        return os.toByteArray();
    }

    public static String readAsString(InputStream inputstream, String s)
        throws Exception
    {
        return readAsString(inputstream, s, null);
    }

    public static String readAsString(InputStream inputstream, String s, ProgressListener progresslistener)
        throws Exception
    {
        String s1;
        try
        {
            s1 = new String(readAsBytes(inputstream, progresslistener), s);
        }
        catch(UnsupportedEncodingException unsupportedencodingexception)
        {
            s1 = null;
        }
        return s1;
    }
    
    public static void readAsFile(InputStream inputstream, FileOutputStream fileoutputstream, ProgressListener progresslistener, HttpGroup.StopController stopcontroller)
            throws Exception
        {
             try{
             	byte[]  buf = new byte[bufferSize];
             	int len = 0;
             	int progress = 0;
             	while(!stopcontroller.isStop() && (len = inputstream.read(buf)) !=-1){
             		fileoutputstream.write(buf,0,len);
             		progress += len;
             		progresslistener.notify(progress,len);
             	}
             }catch(Exception e){
             	e.printStackTrace();
             }finally{
            	fileoutputstream.close();
             	inputstream.close();
             }
        }

    private static int bufferSize = 16384;

}
