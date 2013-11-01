
package com.jingdong.app.mall.utils;

import android.view.MotionEvent;
import android.view.ViewParent;

public class IGestureListener
    implements android.view.GestureDetector.OnGestureListener
{
	private TouchFlingActionListener onListener;
    private ViewParent parent;
	
    public static interface TouchFlingActionListener
    {

        public abstract void next();

        public abstract void previous();

        public abstract void startActivity();
    }


    public IGestureListener(ViewParent viewparent, TouchFlingActionListener touchflingactionlistener)
    {
        onListener = touchflingactionlistener;
        parent = viewparent;
    }

    public boolean onDown(MotionEvent motionevent)
    {
        return false;
    }

    public boolean onFling(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
    {
    	 if(motionevent == null || motionevent1 == null) 
         	return false;
         if(motionevent.getX() - motionevent1.getX() >= 5F) 
         	onListener.previous();
 		else if(motionevent.getX() - motionevent1.getX() < -5F)
             onListener.next();
         return false;
    }

    public void onLongPress(MotionEvent motionevent)
    {
    }

    public boolean onScroll(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
    {
    	  boolean flag;
          flag = false;
          parent.requestDisallowInterceptTouchEvent(true);
          if(Math.abs(f) <= Math.abs(f1))
          	flag = true;
          if(motionevent == null || motionevent1 == null)
          	return flag;
          else if(motionevent.getX() - motionevent1.getX() >= 50F)
          	onListener.previous();
          if(motionevent.getX() - motionevent1.getX() < -50F)
              onListener.next();
          return flag;
    }

    public void onShowPress(MotionEvent motionevent)
    {
    }

    public boolean onSingleTapUp(MotionEvent motionevent)
    {
        onListener.startActivity();
        return false;
    }

}
