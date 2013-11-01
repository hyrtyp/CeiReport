package com.hyrt.cei.ui.phonestudy.anim;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.app.Activity;

public class RightOneItemRightCamera extends Animation {
	Camera camera = new Camera();
	private Context context;

	public RightOneItemRightCamera(Context context) {
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
		// camera.translate(10, 20,
		// 30)的意思是把物体右移10，上移20，向前移30（即让物体远离camera，这样物体将会变小）；
		camera.save();
		DisplayMetrics display = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(display);
		int width = display.widthPixels;
		if (width == 1280) {
			camera.translate(232.5f * interpolatedTime, -18.5f
					* interpolatedTime, 137.8f * interpolatedTime);
		} else {
			camera.translate(232.5f * interpolatedTime, -18.5f
					* interpolatedTime, 137.8f * interpolatedTime);
		}
		camera.getMatrix(matrix);
		camera.restore();
	}

}
