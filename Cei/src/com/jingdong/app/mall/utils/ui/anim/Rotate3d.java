// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Rotate3d.java

package com.jingdong.app.mall.utils.ui.anim;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3d extends Animation {
	
	public static final int ROTATE_X = 0;
	public static final int ROTATE_XY = 3;
	public static final int ROTATE_XYZ = 6;
	public static final int ROTATE_XZ = 4;
	public static final int ROTATE_Y = 1;
	public static final int ROTATE_YZ = 5;
	public static final int ROTATE_Z = 2;
	private Camera mCamera;
	private float mCenterX;
	private float mCenterY;
	private float mFromDegree;
	private float mSaveFromDegree;
	private float mSaveToDegree;
	private float mToDegree;
	private int type;

	public Rotate3d(float f, float f1, float f2, float f3) {
		mFromDegree = f;
		mToDegree = f1;
		mCenterX = f2;
		mCenterY = f3;
		mSaveFromDegree = f;
		mSaveToDegree = f1;
		type = 1;
	}

	protected void applyTransformation(float f, Transformation transformation) {
		float f1 = mFromDegree + f * (mToDegree - mFromDegree);
		float f2 = mCenterX;
		float f3 = mCenterY;
		Matrix matrix = transformation.getMatrix();
		if (f1 <= -76F) {
			mCamera.save();
			rotate(-90F);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		} else if (f1 >= 76F) {
			mCamera.save();
			rotate(90F);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		} else {
			mCamera.save();
			mCamera.translate(0F, 0F, f2);
			rotate(f1);
			mCamera.translate(0F, 0F, -f2);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		}
		matrix.preTranslate(-f2, -f3);
		matrix.postTranslate(f2, f3);
	}

	public int getType() {
		return type;
	}

	public void initialize(int i, int j, int k, int l) {
		super.initialize(i, j, k, l);
		mCamera = new Camera();
	}

	public void reverseTransformation(boolean flag) {
		if (flag) {
			mFromDegree = -mSaveFromDegree;
			mToDegree = -mSaveToDegree;
		} else {
			mFromDegree = mSaveFromDegree;
			mToDegree = mSaveToDegree;
		}
	}

	public void rotate(float f) {
		switch (type) {
		case ROTATE_X:
			mCamera.rotateX(f);
			break;
		case ROTATE_Y:
			mCamera.rotateY(f);
			break;
		case ROTATE_Z:
			mCamera.rotateZ(f);
			break;
		case ROTATE_XY:
			mCamera.rotateX(f);
			mCamera.rotateY(f);
			break;
		case ROTATE_XZ:
			mCamera.rotateX(f);
			mCamera.rotateZ(f);
			break;
		case ROTATE_YZ:
			mCamera.rotateY(f);
			mCamera.rotateZ(f);
			break;
		case ROTATE_XYZ:
			mCamera.rotateX(f);
			mCamera.rotateY(f);
			mCamera.rotateZ(f);
			break;
		}
	}

	public void setType(int i) {
		type = i;
	}
}
