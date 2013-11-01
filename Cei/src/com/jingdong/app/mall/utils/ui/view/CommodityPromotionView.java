package com.jingdong.app.mall.utils.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.LinearLayout;
import com.jingdong.app.mall.entity.Commercial;
import com.jingdong.app.mall.utils.*;
import com.jingdong.app.mall.utils.ui.anim.Rotate3d;
import java.util.ArrayList;
import java.util.HashMap;

public class CommodityPromotionView implements Runnable {

	public int commIndex;
	private LinearLayout commLayout;
	public HashMap commercialCache;
	public ArrayList commercialsList;
	private Thread currentThread;
	private boolean isActivityLoader;
	private boolean isDoUp;
	private boolean isDoing;
	private boolean isDown;
	private boolean isLoaderFinish;
	private boolean isOnline;
	private Rotate3d leftAnimation;
	private boolean lockTouch;
	private Activity mActivity;
	private Context mContext;
	private ImageSwitcher mSwitcher;
	private int nightLampWith;
	private boolean onWait;
	private Rotate3d rightAnimation;
	private int type;

	public static interface OnCommercialListener {

		public abstract void onError(int i, String s,
				com.jingdong.app.mall.utils.HttpGroup.HttpError httperror);

		public abstract void onFinish();

		public abstract void onSuccess(int i, String s);

		public abstract void onSyncronizotionParams(ArrayList arraylist);
	}

	public CommodityPromotionView(Activity myactivity,
			ImageSwitcher imageswitcher, LinearLayout linearlayout) {
		isOnline = true;
		mActivity = myactivity;
		mContext = myactivity;
		mSwitcher = imageswitcher;
		commLayout = linearlayout;
		currentThread = new Thread(this);
		initActivities();
	}

	private void getActivitesImage(final int index, final ArrayList commercialList, Commercial commercial, final HttpGroup pool, final OnCommercialListener listener)
    {
        final String key;
        key = commercial.getHorizontalImg();
        com.jingdong.app.mall.utils.HttpGroup.HttpSetting httpsetting = new com.jingdong.app.mall.utils.HttpGroup.HttpSetting();
        httpsetting.setType(5000);
        httpsetting.setSampleSize(false);
        httpsetting.setEffect(0);
        httpsetting.setNeedImageCorner(false);
        httpsetting.setUrl(key);
        httpsetting.setListener(new com.jingdong.app.mall.utils.HttpGroup.CustomOnAllListener() {

            private void loadNext(int i)
            {
                if(i < commercialList.size())
                    getActivitesImage(i, commercialList, (Commercial)commercialList.get(i), pool, listener);
                else
                    isOnline = false;
            }

            public void onEnd(com.jingdong.app.mall.utils.HttpGroup.HttpResponse httpresponse)
            {
                BitmapDrawable bitmapdrawable = null;
                try
                {
                    Bitmap bitmap = httpresponse.getBitmap();
                    if(bitmap != null)
                    {
                        int i = mSwitcher.getWidth();
                        if(i <= 0)
                            i = DPIUtil.getWidth() - DPIUtil.dip2px(10F);
                        float f = bitmap.getWidth();
                        float f1 = bitmap.getHeight();
                        float f2 = (float)i / f;
                        bitmapdrawable = new BitmapDrawable(bitmap);
                    }
                }
                catch(Exception exception1)
                {
                    exception1.printStackTrace();
                    bitmapdrawable = (BitmapDrawable)httpresponse.getDrawable();
                }
                if(bitmapdrawable != null && !TextUtils.isEmpty(key))
                {
                    commercialCache.put(key, bitmapdrawable);
                }
                listener.onSuccess(index, key);
                loadNext(1 + index);
            }

            public void onError(com.jingdong.app.mall.utils.HttpGroup.HttpError httperror)
            {
                listener.onError(index, key, httperror);
                loadNext(1 + index);
            }

            public void onProgress(int i, int j)
            {
            }

            public void onStart()
            {
            }
            
        });
        pool.add(httpsetting);
    }

	private ImageSwitcher initActivities() {
		int i = DPIUtil.getWidth() / 2;
		leftAnimation = new Rotate3d(0F, -90F, i, 0F);
		rightAnimation = new Rotate3d(90F, 0F, i, 0F);
		leftAnimation.setFillAfter(true);
		leftAnimation.setDuration(500L);
		rightAnimation.setFillAfter(true);
		rightAnimation.setDuration(500L);
		android.view.animation.Animation.AnimationListener animationlistener = new android.view.animation.Animation.AnimationListener() {

			public void onAnimationEnd(Animation animation) {
				lockTouch = false;
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationStart(Animation animation) {
				lockTouch = true;
			}
		};
		leftAnimation.setAnimationListener(animationlistener);
		rightAnimation.setAnimationListener(animationlistener);
		setAnimation(0);
		mSwitcher.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View view) {

			}
		});
		initCommercial();
		return mSwitcher;
	}

	private void initCommercial() {
		final GestureDetector detector = new GestureDetector(
				new IGestureListener(
						mSwitcher.getParent(),
						new com.jingdong.app.mall.utils.IGestureListener.TouchFlingActionListener() {

							public void next() {
								doNext(false);
							}

							public void previous() {
								doPrevious(false);
							}

							public void startActivity() {
							}
						}));
		mSwitcher.setOnTouchListener(new android.view.View.OnTouchListener() {

			private void doUp() {
				isDown = false;
				onWait = true;
				isDoing = false;
				currentThread.notify();
			}

			public boolean onTouch(View view, MotionEvent motionevent) {
				boolean flag;
				flag = false;
				switch (motionevent.getAction()) {
				case MotionEvent.ACTION_UP:
					isDown = false;
					notifyCPV();
					break;
				case MotionEvent.ACTION_MOVE:
					isDoUp = false;
					break;
				case MotionEvent.ACTION_DOWN:
					if (!lockTouch)
						flag = detector.onTouchEvent(motionevent);
					if (isDoUp) {
						doUp();
						isDoUp = false;
					}
					break;
				case MotionEvent.ACTION_CANCEL:
					isDoUp = false;
					break;
				}
				return flag;
			}
		});
	}

	private Drawable next(Drawable drawable)
    {
		switch(type){
		case 0:
			 leftAnimation.reverseTransformation(false);
		     rightAnimation.reverseTransformation(false);
			break;
		case 1:
			 push_left();
			break;
		}
		return drawable;
    }

	private void notifyCPV() {
		currentThread.notify();
	}

	private void onChange() {
		new Handler().post(new Runnable() {

			public void run() {
				doPrevious(true);
			}
		});
	}

	private Drawable previous(Drawable drawable){
		switch(type){
		case 0:
			 leftAnimation.reverseTransformation(false);
		     rightAnimation.reverseTransformation(false);
			break;
		case 1:
			push_right();
			break;
		}
		return drawable;
    }

	private void push_left() {
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext,
				0x7f040002));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext,
				0x7f040003));
	}

	private void push_right() {
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext,
				0x7f040004));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext,
				0x7f040005));
	}

	private void rotate3d() {
		mSwitcher.setInAnimation(rightAnimation);
		mSwitcher.setOutAnimation(leftAnimation);
	}

	private void setAnimation(int i)
    {
		switch(i){
		case 0:
			rotate3d();
			break;
		case 1:
			push_left();
			break;
		case 2:
			push_right();
			break;
		}
		rotate3d();
		type = i;
    }

	private void setImage(Drawable drawable) {
		if (drawable != null) {
			mSwitcher.setImageDrawable(drawable);
		} 
	}

	public View addNightGap() {
		View view = new View(mContext);
		view.setLayoutParams(new android.view.ViewGroup.LayoutParams(4, -1));
		view.setBackgroundColor(0xccffffff);
		return view;
	}

	public View addNightLamp(Object obj, int i, int j) {
		View view = new View(mContext);
		view.setTag(obj);
		view.setLayoutParams(new android.view.ViewGroup.LayoutParams(i, j));
		view.setBackgroundResource(0x7f020003);
		return view;
	}

	public void clearDrawable() {
		if (commercialCache != null)
			commercialCache.clear();
	}

	public void closeNight(int i)
    {
        if(commLayout != null && i>0){
        	 View view = commLayout.findViewWithTag(Integer.valueOf(i));
             if(view != null)
                 view.setBackgroundResource(0x7f020003);
             i++;
        }
    }

	public void doNext(boolean flag) {
		if (isDoing) {
			if (!flag)
				isDoing = true;
			if (!isEmpty()) {
				commIndex--;
				if (commIndex < 0)
					commIndex = size() - 1;
				setImage(previous(getCacheDrawable(((Commercial) commercialsList
						.get(commIndex)).getHorizontalImg())));
				if (size() > 1) {
					closeNight(size());
					View view = commLayout.findViewWithTag(Integer
							.valueOf(commIndex));
					if (view != null)
						view.setBackgroundResource(0x7f020004);
				}
			} else {
				mSwitcher.startAnimation(AnimationUtils.loadAnimation(mContext,
						0x7f040006));
			}
		}
	}

	public void doPrevious(boolean flag) {
		if (!isDoing) {
			if (!flag)
				isDoing = true;
			if (!isEmpty()) {
				int i = 1 + commIndex;
				commIndex = i;
				int j;
				if (i > -1 + size()) {
					j = 0;
				} else {
					j = commIndex;
					commIndex = j + 1;
				}
				commIndex = j;
				setImage(next(getCacheDrawable(((Commercial) commercialsList.get(commIndex)).getHorizontalImg())));
				if (size() > 1) {
					closeNight(size());
					View view = commLayout.findViewWithTag(Integer
							.valueOf(commIndex));
					if (view != null)
						view.setBackgroundResource(0x7f020004);
				}
			} else {
				mSwitcher.startAnimation(AnimationUtils.loadAnimation(mContext,
						0x7f040006));
			}
		}
	}

	public Drawable getCacheDrawable(String s) {
		Drawable drawable;
		if (commercialCache == null || TextUtils.isEmpty(s))
			drawable = null;
		else
			drawable = (Drawable) commercialCache.get(s);
		return drawable;
	}

	public boolean isActivityLoader() {
		return isActivityLoader;
	}

	public boolean isEmpty() {
		boolean flag;
		if (commercialsList == null)
			flag = true;
		else
			flag = false;
		return flag;
	}

	   /* com.jingdong.app.mall.utils.HttpGroup.HttpSetting httpsetting = new com.jingdong.app.mall.utils.HttpGroup.HttpSetting();
        httpsetting.setFunctionId("indexFocusActivity");
        httpsetting.putJsonParam("page", "1");
        httpsetting.putJsonParam("pagesize", "12");
        httpsetting.setListener(oncommonlistener);
        httpsetting.setLocalFileCache(true);
        httpsetting.setLocalFileCacheTime(0x493e0L);
        httpsetting.setNeedGlobalInitialization(false);
        mActivity.getHttpGroupaAsynPool().add(httpsetting);*/

	public void removeDrawable(String s) {
		if (commercialCache != null)
			commercialCache.remove(s);
	}

	public void run()
    {
        while(isDown) {
        	synchronized(currentThread){
                try {
					currentThread.wait(5000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                if(isDown){
                    synchronized(currentThread){
                        try {
							currentThread.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                    }
                    if(onWait)
                    	onWait = false;
                    else
                    	onChange();
                }
            }
        }
    }

	public void setNightLampWidth(int i) {
		nightLampWith = i;
	}

	public void show() {
		new Handler().post(new Runnable() {

			public void run() {
				int i = 0;
				do {
					if (i >= size() || size() <= 1) {
						mSwitcher.setVisibility(0);
						commLayout.setVisibility(0);
						mSwitcher.setImageDrawable(null);
						if (commLayout.findViewWithTag(Integer.valueOf(0)) != null)
							commLayout.findViewWithTag(Integer.valueOf(0))
									.setBackgroundResource(0x7f020004);
						return;
					}
					int j = (commLayout.getWidth() - 4 * (-1 + size()))
							/ size();
					if (nightLampWith < 1)
						nightLampWith = j;
					commLayout.addView(addNightLamp(Integer.valueOf(i),
							nightLampWith, DPIUtil.dip2px(3F)));
					if (i != -1 + size())
						commLayout.addView(addNightGap());
					i++;
				} while (true);
			}

		});
	}

	public void showActivities(final int index, final String key) {
		new Handler().post(new Runnable() {

			public void run() {
				Object obj;
				if (commercialCache != null && !TextUtils.isEmpty(key))
					obj = (Drawable) commercialCache.get(key);
				else
					obj = null;
				mSwitcher.setImageDrawable(((Drawable) (obj)));
				if (commercialsList.size() > 1)
					commLayout.findViewWithTag(Integer.valueOf(index))
							.setBackgroundResource(0x7f020004);
				rotate3d();
			}
		});
	}

	public int size() {
		int i;
		if (commercialsList == null)
			i = 0;
		else
			i = commercialsList.size();
		return i;
	}

	public void start() {
		if (isLoaderFinish) {
			if (currentThread != null && !currentThread.isAlive())
				currentThread.start();
			isDown = false;
			onWait = true;
			isDoing = false;
			isDoUp = true;
			notifyCPV();
		}
	}

	public void stop() {
		if (isLoaderFinish) {
			isDown = true;
			onChange();
		}
	}
}
