package com.hyrt.cei.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class WriteOrRead {
	public static void write(String retXML, String where, String name) {
		File dir = new File(where);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		try {
			OutputStream out = new FileOutputStream(new File(dir, name));
			out.write(retXML.getBytes());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String read(String where, String name) {
		String data = "";
		try {
			File file = new File(where + "/" + name);
			if (!file.exists()) {
				return "";
			} else {
				FileInputStream in = new FileInputStream(file);
				BufferedReader read = new BufferedReader(new InputStreamReader(
						in));
				StringBuffer buffer = new StringBuffer();
				while ((data = read.readLine()) != null) {
					buffer.append(data);
				}
				return buffer.toString();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
