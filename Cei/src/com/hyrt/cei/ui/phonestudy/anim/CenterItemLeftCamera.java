package com.hyrt.cei.ui.phonestudy.anim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

public class CenterItemLeftCamera extends Animation {
	Camera camera = new Camera();
	private Context context;

	public CenterItemLeftCamera(Context context) {
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
		t.setAlpha(0.8f);
		final Matrix matrix = t.getMatrix();
		camera.save();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		int widthPixels = displaymetrics.widthPixels;
		if (widthPixels == 1280) {
			camera.translate(-299 * interpolatedTime, -6.5f * interpolatedTime,
					38f * interpolatedTime);
		} else {
			camera.translate(-170 * interpolatedTime, -7 * interpolatedTime,
					60 * interpolatedTime);
		}
		camera.getMatrix(matrix);
		camera.restore();
	}

}
