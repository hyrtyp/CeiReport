package com.hyrt.cei.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.View.MeasureSpec;

public class ImageUtil {

	public final static Integer WIDTH = 390;
	public final static Integer HEIGHT = 300;
	private final static Integer FIX_SIZE = 54298;

	/**
	 * 获取最佳的缩放率
	 * 
	 * @param photoSize
	 * @return
	 */
	public static Integer parsePhotoZip(Integer photoSize) {
		Integer SCALE_SIZE = 100;
		if (photoSize < FIX_SIZE){
			SCALE_SIZE = 100;
		}else if(photoSize / FIX_SIZE == 1) {
			SCALE_SIZE = 50;
		} else {
			SCALE_SIZE = SCALE_SIZE / ((photoSize) / FIX_SIZE);
			if (SCALE_SIZE < 30) {
				SCALE_SIZE = 30;
			}
		}
		return SCALE_SIZE;
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;
	}

	/**
	 * 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		Bitmap newBmp=null;
		if(bitmap!=null){
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidth = ((float) w / width);
			float scaleHeight = ((float) h / height);
			matrix.postScale(scaleWidth, scaleHeight);
			newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
					matrix, true);
		}
		
		return newBmp;
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static void saveBytesByFile(byte[] b, String outputFile)
			throws Exception {
		BufferedOutputStream stream = null;
		try {
			File ret = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Bitmap getBitmapByPath(String path)
			throws FileNotFoundException {
		Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(path));
		return bitmap;
	}

	/**
	 * 
	 * @param maxWidth 图片最大的宽度
	 * @param maxHeight 图片最大的高度
	 * @param bitmap 目标图片
	 * @return 宽和高
	 */
	public static int[] getFinalBitmapByWidth(int maxWidth,int maxHeight,Bitmap bitmap) {
		//初始化图片的高和宽
		double finalWidth = bitmap.getWidth();
		double finalHeight = bitmap.getHeight();
		//如果图片是宽的话
		if(bitmap.getWidth() > bitmap.getHeight()){
			//如果图片的宽度大约屏幕的宽度的话
			if(bitmap.getWidth() > maxWidth){
				double than = (double)maxWidth/(double)(bitmap.getWidth());
				finalWidth = maxWidth;
				finalHeight = finalHeight*than;
			}
		//如果图片是长的话
		}else if(bitmap.getHeight()>bitmap.getWidth()){
			//如果图片的高度大于屏幕的高度的话
			if(bitmap.getHeight() > maxHeight){
				double than = (double)maxHeight/(double)(bitmap.getHeight());
				finalHeight = maxHeight;
				finalWidth = finalWidth*than;
				//如果处理后的宽度还是大于屏幕宽度的话
				if(finalWidth>maxWidth){
					double tn = (double)maxWidth/finalWidth;
					finalWidth = maxWidth;
					finalHeight = finalHeight*tn;
				}
			}
			//如果图片是正方的话
		}else if(bitmap.getHeight() == bitmap.getWidth()){
			if(bitmap.getHeight() >maxHeight){
				double than = (double)maxHeight/(double)(bitmap.getHeight());
				finalHeight = maxHeight;
				finalWidth = finalWidth*than;
				if(finalWidth>maxWidth){
					double tn = (double)maxWidth/finalWidth;
					finalWidth = maxWidth;
					finalHeight = finalHeight*tn;
				}
			}else if(bitmap.getWidth() > maxWidth){
				double than = (double)maxWidth/(double)(bitmap.getWidth());
				finalWidth = maxWidth;
				finalHeight = finalHeight*than;
			}
		}
		return new int[]{(int)finalWidth, (int)finalHeight};
	}

	public static InputStream getRequest(String url) throws Exception {
		HttpEntity entity = null;
		// 创建客户端
		HttpClient client = new DefaultHttpClient();
		// 创建请求
		HttpUriRequest request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		// 判断响应码
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 获得响应实体
			entity = response.getEntity();
			if(entity.getContentLength()>500000){
				return null;
			};
			return entity.getContent();
		}
		return null;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1096];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	public static Drawable getDrawableFromUrl(String url) throws Exception {
		return Drawable.createFromStream(getRequest(url), null);
	}

	public static Bitmap getBitmapFromUrl(String url) throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		return byteToBitmap(bytes);
	}

	public static Bitmap getRoundBitmapFromUrl(String url, int pixels)
			throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		Bitmap bitmap = byteToBitmap(bytes);
		return toRoundCorner(bitmap, pixels);
	}

	public static Drawable geRoundDrawableFromUrl(String url, int pixels)
			throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) byteToDrawable(bytes);
		return toRoundCorner(bitmapDrawable, pixels);
	}

	public static byte[] getBytesFromUrl(String url) throws Exception {
		return readInputStream(getRequest(url));
	}

	public static Bitmap byteToBitmap(byte[] byteArray) {
		if (byteArray.length != 0) {
			return BitmapFactory
					.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	public static Drawable byteToDrawable(byte[] byteArray) {
		ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
		return Drawable.createFromStream(ins, null);
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}

	/**
	 * 放大缩小图片 缩放
	 * 
	 * @param path
	 *            资源路径
	 * @param w
	 *            缩放后宽
	 * @param h
	 *            缩放后高
	 */
	public static Bitmap zoomBitmap(String path, int w, int h) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		long tohight = (w <= h ? (options.outHeight * h / options.outWidth)
				: (options.outHeight * w / options.outWidth));
		options.inSampleSize = (int) (options.outHeight / tohight);
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}

	/**
	 * 将图片转换为圆型的并加上光晕效果
	 * 
	 * @param drawable
	 * @return
	 */
	public static Drawable getCircleDrawable(Drawable drawable) {
		int targetWidth = 100;
		int targetHeight = 100;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);
		canvas.clipPath(path);
		Bitmap sourceBitmap = ((BitmapDrawable) drawable).getBitmap();
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		drawable = new BitmapDrawable(targetBitmap);
		return drawable;
	}

	/**
	 * 给图片加光晕
	 * 
	 * @param drawable
	 * @return
	 */
	public static Drawable addHaloForDrawable(Drawable drawable) {
		Paint p = new Paint();
		p.setColor(Color.RED);// 红色的光晕
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
				bd.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b.extractAlpha(), 0, 0, p);
		StateListDrawable sld = new StateListDrawable();
		sld.addState(new int[] { android.R.attr.state_pressed },
				new BitmapDrawable(bitmap));
		return sld;
	}

	/**
	 * 把布局转为bitmap
	 */
	public static Bitmap getBitmapByLayout(View targetView) {
		targetView.setDrawingCacheEnabled(true);
		targetView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		targetView.layout(0, 0, targetView.getMeasuredWidth(), targetView.getMeasuredHeight());
		targetView.buildDrawingCache();
		return targetView.getDrawingCache();
	}
	
	/**
	 * 缩放drawable
	 * @param drawable
	 * @param w
	 * @param h
	 * @return
	 */
	public static Drawable zoomDrawable(Drawable drawable, int w, int h)
     {
               int width = drawable.getIntrinsicWidth();
               int height= drawable.getIntrinsicHeight();
               Bitmap oldbmp = drawableToBitmap(drawable);// drawable转换成bitmap
               Matrix matrix = new Matrix();   // 创建操作图片用的Matrix对象
               float scaleWidth = ((float)w / width);   // 计算缩放比例
               float scaleHeight = ((float)h / height);
               matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例
               Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);       // 建立新的bitmap，其内容是对原bitmap的缩放后的图
               return new BitmapDrawable(newbmp);       // 把bitmap转换成drawable并返回
     }


}
