package com.hyrt.cei.util;

import android.graphics.ColorMatrixColorFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ImageButtonTool {
	/**
	 * 按钮被按下
	 */
	private final static float[] BUTTON_PRESSED = new float[] { 2.0f, 0, 0, 0,
			-50, 0, 2.0f, 0, 0, -50, 0, 0, 2.0f, 0, -50, 0, 0, 0, 5, 0 };

	/**
	 * 按钮恢复原状
	 */
	private final static float[] BUTTON_RELEASED = new float[] { 1, 0, 0, 0, 0,
			0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };

	private static final OnTouchListener touchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				((ImageView) v).getDrawable().setColorFilter(
						new ColorMatrixColorFilter(BUTTON_PRESSED));
				((ImageView) v)
						.setImageDrawable((((ImageView) v).getDrawable()));
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				((ImageView) v).getDrawable().setColorFilter(
						new ColorMatrixColorFilter(BUTTON_RELEASED));
				((ImageView) v)
						.setImageDrawable((((ImageView) v).getDrawable()));
			}
			return false;
		}
	};

	public static void setButtonStateChangeListener(View v) {
		v.setOnTouchListener(touchListener);
	}
}
