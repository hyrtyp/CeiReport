package com.jingdong.app.mall.utils;


public class ContentConvertUtil
{

    public ContentConvertUtil()
    {
    }

    public static byte[] toByteArray(String s)
    {
        byte abyte0[] = new byte[s.length() / 2];
        int i = 0;
        int j = 0;
        do
        {
            if(j >= abyte0.length)
                return abyte0;
            int k = i + 1;
            int l = Character.digit(s.charAt(i), 16) << 4;
            i = k + 1;
            abyte0[j] = (byte)(l | Character.digit(s.charAt(k), 16));
            j++;
        } while(true);
    }
}
