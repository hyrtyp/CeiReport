package com.hyrt.cei.ui.phonestudy.anim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

public class RightTwoItemLeftCamera extends Animation {

	Camera camera = new Camera();

	public RightTwoItemLeftCamera() {

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
		camera.translate(-148 * interpolatedTime, 12 * interpolatedTime, -108
				* interpolatedTime);
		camera.getMatrix(matrix);
		camera.restore();
	}

}
