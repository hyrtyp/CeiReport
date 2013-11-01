package com.hyrt.cei.ui.phonestudy.anim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.app.Activity;

public class LeftOneItemLeftCamera extends Animation {
	Camera camera = new Camera();
	private Context context;

	public LeftOneItemLeftCamera(Context context) {
		this.context = context;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		setDuration(600);
		setFillAfter(false);
		setInterpolator(new DecelerateInterpolator());
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final Matrix matrix = t.getMatrix();
		camera.save();
		DisplayMetrics display = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(display);
		int width = display.widthPixels;
		if (width == 1280) {
			camera.translate(-152.5f * interpolatedTime, -17.8f
					* interpolatedTime, 137.8f * interpolatedTime);
		} else {
			camera.translate(-152.5f * interpolatedTime, -17.8f
					* interpolatedTime, 137.8f * interpolatedTime);
		}
		camera.getMatrix(matrix);
		camera.restore();
	}
}
