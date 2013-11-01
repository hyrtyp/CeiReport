package com.hyrt.cei.ui.phonestudy.anim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

public class RightOneItemLeftCamera extends Animation {

	Camera camera = new Camera();
	private Context context;

	public RightOneItemLeftCamera(Context context) {
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
		Matrix matrix = t.getMatrix();
		camera.save();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		int widthPixels = displaymetrics.widthPixels;
		if (widthPixels == 1280) {
			camera.translate(-290 * interpolatedTime, 7 * interpolatedTime, -35
					* interpolatedTime);
		} else {
			// System.out.println("台电RightOneItemLeft");
			camera.translate(-170 * interpolatedTime, 5 * interpolatedTime, -31
					* interpolatedTime);
		}
		camera.getMatrix(matrix);
		camera.restore();
	}

}
