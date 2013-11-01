package com.hyrt.cei.util;

import java.io.File;
import java.io.RandomAccessFile;

public class EncryptDecryption {

	/**
	 * 加密报告pdf文件
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void EncryptReport(String filePath) throws Exception {
		RandomAccessFile randomaccessfile = new RandomAccessFile(filePath,
				"rwd");
		byte[] bt = new byte[400];
		for (int i = 0; i < bt.length; i++) {
			bt[i] = 0;
		}
		randomaccessfile.seek(0);
		randomaccessfile.write(bt);
	}

	/**
	 * 解密pdf文件
	 * 
	 * @param filePath
	 * @param keys
	 * @throws Exception
	 */
	public static void DecryptionReport(String filePath, String keys)
			throws Exception {
		RandomAccessFile randomaccessfile = new RandomAccessFile(filePath,
				"rwd");
		randomaccessfile.seek(0);
		String[] key = keys.split(",");
		byte[] bt = new byte[key.length];
		for (int i = 0; i < key.length; i++) {
			bt[i] = (byte) (Integer.parseInt(key[i]) - 40);
		}
		if(bt!=null)
		randomaccessfile.write(bt);
	}

	public static void Decryption(String filePath, String keys)
			throws Exception {
		new File(filePath).renameTo(new File(filePath.replace(".yepeng",".mp3")));
		filePath = filePath.replace(".yepeng",".mp3");
		RandomAccessFile randomaccessfile = new RandomAccessFile(filePath,"rwd");
		randomaccessfile.seek(0);
		byte[] bt = new byte[4];
		String[] key = keys.split(",");
		for (int i = 0; i < bt.length; i++) {
			System.out.println(key[i] + " : " + "key");
			bt[i] = (byte) (Integer.parseInt(key[i]) - 40);
		}
		randomaccessfile.write(bt);
	}

	public static String Encrypt(String filePath) throws Exception {
		new File(filePath).renameTo(new File(filePath.replace(".mp3",".yepeng")));
		filePath = filePath.replace(".mp3",".yepeng");
		RandomAccessFile randomaccessfile = new RandomAccessFile(filePath,"rwd");
		byte[] bt = new byte[4];
		randomaccessfile.read(bt, 0, 4);
		StringBuilder keys = new StringBuilder();
		for (int i = 0; i < bt.length; i++) {
			keys.append("," + bt[i]);
		}
		bt[0] = 0;
		bt[1] = 0;
		bt[2] = 0;
		bt[3] = 0;
		randomaccessfile.seek(0);
		randomaccessfile.write(bt);
		return keys.toString().substring(1);
	}
	
	public static void EncryptCourseware(String filePath) throws Exception {
		for(int i=0;i<MyTools.KJ_ENCRYPATH.split("!@").length;i++){
			File encryFile = new File(filePath + MyTools.KJ_ENCRYPATH.split("!@")[i]);
			for (int j = 0; j < encryFile.list().length; j++) {
				System.out.println(encryFile.getAbsolutePath() + File.separator + encryFile.list()[j]);
				Encrypt(encryFile.getAbsolutePath() + File.separator + encryFile.list()[j]);
			}
		}
	}

	public static void DecryptionCourseware(String filePath, String keys)
			throws Exception {
		keys = keys.split("@!")[0];
		if(keys != null && !keys.equals("")){
			for(int i=0;i<MyTools.KJ_ENCRYPATH.split("!@").length;i++){
				File encryFile = new File(filePath + MyTools.KJ_ENCRYPATH.split("!@")[i]);
				for (int j = 0; j < encryFile.list().length; j++) {
					System.out.println(encryFile.getAbsolutePath() + File.separator + encryFile.list()[j] +" : "+ keys.replace("|", "!@").split("!@")[i==1?2:i]);
					Decryption(encryFile.getAbsolutePath() + File.separator + encryFile.list()[j], keys.replace("|", "!@").split("!@")[i==1?2:i]);
				}
			}
		}
	}
}
