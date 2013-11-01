package com.hyrt.cei.ui.phonestudy.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hyrt.cei.R;
import com.hyrt.cei.ui.phonestudy.HomePageActivity;
import com.hyrt.cei.ui.phonestudy.anim.CenterItemLeftCamera;
import com.hyrt.cei.ui.phonestudy.anim.CenterItemRightCamera;
import com.hyrt.cei.ui.phonestudy.anim.LeftOneItemLeftCamera;
import com.hyrt.cei.ui.phonestudy.anim.LeftOneItemRightCamera;
import com.hyrt.cei.ui.phonestudy.anim.LeftTwoItemRightCamera;
import com.hyrt.cei.ui.phonestudy.anim.RightOneItemLeftCamera;
import com.hyrt.cei.ui.phonestudy.anim.RightOneItemRightCamera;
import com.hyrt.cei.ui.phonestudy.anim.RightTwoItemLeftCamera;
import com.hyrt.cei.util.ImageUtil;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.view.View.OnTouchListener;

;

public class FlowRelativeyout extends RelativeLayout implements
		AnimationListener, OnTouchListener {

	private static final int SWIPE_MIN_DISTANCE = 10;
	private LinkedList<Integer> linkList = new LinkedList<Integer>();;
	Drawable[] drawables = new Drawable[6];
	//List<Drawable> otherDrawables = new ArrayList<Drawable>();
	public ImageView centerImageView;
	public ImageView leftOneImageView;
	public ImageView leftTwoImageView;
	public ImageView rightOneImageView;
	public ImageView rightTwoImageView;
	public ImageView leftOperationIv;
	public ImageView rightOperationIv;
	public LinearLayout indexParent;
	public ScrollView scrollview;
	public Drawable currentDrawable;
	private int index = 0;

	public FlowRelativeyout(Context context) {
		super(context);
	}

	public FlowRelativeyout(Context context, AttributeSet attrs) {
		super(context, attrs);
		for (int i = 0; i < drawables.length; i++) {
			drawables[i] = getResources().getDrawable(
					R.drawable.courseware_default_icon);
			if (i != drawables.length - 1)
				linkList.add(i);
		}
	}

	public FlowRelativeyout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LinkedList<Integer> getLinkList() {
		return linkList;
	}

	public void setLinkList(LinkedList<Integer> linkList) {
		this.linkList = linkList;
	}

	public void registEventForFlowRelativeyout(final Handler handler) {
		leftOperationIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initLeft();
			}
		});
		rightOperationIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initRight();
			}
		});
		centerImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message message = handler.obtainMessage();
				centerImageView.getDrawable();
				message.arg1 = HomePageActivity.GO_DETAIL;
				handler.sendMessage(message);
			}
		});
		centerImageView.setOnTouchListener(this);
	}

	/**
	 * 向左滑动效果
	 */
	private void initLeft() {
		indexParent.getChildAt(index).setSelected(false);
		index = index + 1 > 5 ? 0 : index + 1;
		indexParent.getChildAt(index).setSelected(true);
		Integer value = linkList.getFirst() - 1;
		if (value.intValue() < 0) {
			value = 5;
		}
		linkList.removeFirst();
		linkList.addLast(value);
		CenterItemLeftCamera animation = new CenterItemLeftCamera(getContext());
		animation.setAnimationListener(FlowRelativeyout.this);
		centerImageView.startAnimation(animation);
		LeftOneItemLeftCamera leftOneItemLeftCamera = new LeftOneItemLeftCamera(
				getContext());
		leftOneImageView.startAnimation(leftOneItemLeftCamera);
		RightOneItemLeftCamera rightOneItemLeftCamera = new RightOneItemLeftCamera(
				getContext());
		rightOneImageView.startAnimation(rightOneItemLeftCamera);
		RightTwoItemLeftCamera rightTwoItemLeftCamera = new RightTwoItemLeftCamera();
		rightTwoImageView.startAnimation(rightTwoItemLeftCamera);
		rightTwoItemLeftCamera.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
				alphaAnimation.setDuration(700);
				rightTwoImageView.startAnimation(alphaAnimation);
			}
		});
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
		alphaAnimation.setDuration(600);
		leftTwoImageView.startAnimation(alphaAnimation);
	}

	/**
	 * 向右滑动效果
	 */
	private void initRight() {
		indexParent.getChildAt(index).setSelected(false);
		index = index - 1 < 0 ? 5 : index - 1;
		indexParent.getChildAt(index).setSelected(true);
		Integer value = linkList.getLast() + 1;
		if (value.intValue() > 5) {
			value = 0;
		}
		linkList.removeLast();
		linkList.addFirst(value);
		CenterItemRightCamera animation = new CenterItemRightCamera(
				getContext());
		animation.setAnimationListener(FlowRelativeyout.this);
		centerImageView.startAnimation(animation);
		LeftOneItemRightCamera leftOneAnimation = new LeftOneItemRightCamera(
				getContext());
		leftOneImageView.startAnimation(leftOneAnimation);
		LeftTwoItemRightCamera leftTwoAnimation = new LeftTwoItemRightCamera();
		leftTwoImageView.startAnimation(leftTwoAnimation);
		leftTwoAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
				alphaAnimation.setDuration(700);
				leftTwoImageView.startAnimation(alphaAnimation);
			}
		});
		RightOneItemRightCamera rightOneAnimation = new RightOneItemRightCamera(
				getContext());
		rightOneImageView.startAnimation(rightOneAnimation);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
		alphaAnimation.setDuration(600);
		rightTwoImageView.startAnimation(alphaAnimation);
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		loadImgForIv();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return event.dispatch(this);
	}

	public void loadImgForIv(Drawable[] drawables) {
		for (int i = 0; i < this.drawables.length; i++) {
			this.drawables[i] = drawables[i];
		}
		loadImgForIv();
	}

	/**
	 * 将加载好的图片放入集合
	 */
	private void loadImgForIv() {
		Drawable drawable1 = ImageUtil.zoomDrawable(
				drawables[linkList.get(2)], 394, 177);
		rightOneImageView.setImageDrawable(drawable1);
		//otherDrawables.add(drawable1);
		Drawable drawable2 = ImageUtil.zoomDrawable(
				drawables[linkList.get(3)], 342, 149);
		rightTwoImageView.setImageDrawable(drawable2);
		//otherDrawables.add(drawable2);
		Drawable drawable3 = ImageUtil.zoomDrawable(
				drawables[linkList.get(1)], 449, 205);
		centerImageView.setImageDrawable(drawable3);
		//otherDrawables.add(drawable3);
		Drawable drawable4 =  ImageUtil.zoomDrawable(
				drawables[linkList.get(0)], 394, 177);
		leftOneImageView.setImageDrawable(drawable4);
		//otherDrawables.add(drawable4);
		Drawable drawable5 =  ImageUtil.zoomDrawable(
				drawables[linkList.get(4) == 5 ? 0 : linkList.get(4) + 1], 342,
				149);
		leftTwoImageView.setImageDrawable(drawable5);
		//otherDrawables.add(drawable5);
		currentDrawable = drawables[linkList.get(1)];
	}
	
	public void clearBitmaps(){
		/*for(int i=0;i<drawables.length;i++){
			Drawable drawable = drawables[i];
			if(((BitmapDrawable)drawable) != null)
				((BitmapDrawable)drawable).getBitmap().recycle();
			drawable = null;
		}
		for(int i=0;i<otherDrawables.size();i++){
			Drawable drawable = otherDrawables.get(0);
			if(((BitmapDrawable)drawable) != null)
				((BitmapDrawable)drawable).getBitmap().recycle();
			drawable = null;
		}*/
	}

	private float oldX;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP)
			scrollview.requestDisallowInterceptTouchEvent(false);
		else
			scrollview.requestDisallowInterceptTouchEvent(true);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			oldX = event.getRawX();
			System.out.println("down : " + oldX);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			System.out.println(event.getRawX());
			if (oldX - event.getRawX() > SWIPE_MIN_DISTANCE) {
				initLeft();
			} else if (event.getRawX() - oldX > SWIPE_MIN_DISTANCE) {
				initRight();
			}
		}
		return true;
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		onTouchEvent(event);
		return false;
	}

}
