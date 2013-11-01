package com.poqop.document;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import com.hyrt.cei.util.MyTools;
import com.poqop.document.events.ZoomListener;
import com.poqop.document.models.CurrentPageModel;
import com.poqop.document.models.DecodingProgressModel;
import com.poqop.document.models.ZoomModel;
import com.poqop.document.multitouch.MultiTouchZoom;

/*  liming
 *  documentview我们可以认为是一个显示容器,它里面存放的内容是一个个的页（page），而每个页是由若干个pagetreenode组成的
 */
public class DocumentView extends ImageView implements ZoomListener{
    final ZoomModel zoomModel;
    private final CurrentPageModel currentPageModel;
    DecodeService decodeService;
    public static final HashMap<Integer, Page> pages = new HashMap<Integer, Page>();
    private boolean isInitialized = false;
    private int pageToGoTo;
    private float lastX;
    private float lastY;
    private VelocityTracker velocityTracker;//���ٴ����ٶ�
    private final Scroller scroller;
    DecodingProgressModel progressModel;
    private RectF viewRect;
    private boolean inZoom;
    private long lastDownEventTime;
    private static final int DOUBLE_TAP_TIME = 500;
    private MultiTouchZoom multiTouchZoom;
    private GestureDetector mGestureDetector;
    private Context context;
    private boolean flage;

    public DocumentView(Context context, final ZoomModel zoomModel, DecodingProgressModel progressModel, CurrentPageModel currentPageModel) {
        super(context);
        this.context=context;
        this.zoomModel = zoomModel;
        this.progressModel = progressModel;
        this.currentPageModel = currentPageModel;
        setKeepScreenOn(true);
        scroller = new Scroller(getContext());
        setFocusable(true);
        setFocusableInTouchMode(true);
        initMultiTouchZoomIfAvailable(zoomModel);
        mGestureDetector = new GestureDetector(new LearnGestureListener());
    }

    private void initMultiTouchZoomIfAvailable(ZoomModel zoomModel) {
        try {
            multiTouchZoom = (MultiTouchZoom) Class.forName("org.vudroid.core.multitouch.MultiTouchZoomImpl").getConstructor(ZoomModel.class).newInstance(zoomModel);
        } catch (Exception e) {
            System.out.println("Multi touch zoom is not available: " + e);
        }
    }

    public void setDecodeService(DecodeService decodeService) {
        this.decodeService = decodeService;
    }

    private void init() {
        if (isInitialized) {
            return;
        }
        final int width = decodeService.getEffectivePagesWidth();
        final int height = decodeService.getEffectivePagesHeight();
        for (int i = 0; i < decodeService.getPageCount(); i++) {
            pages.put(i, new Page(this, i));
            pages.get(i).setAspectRatio(width, height);
        }
        isInitialized = true;
        invalidatePageSizes();
        goToPageImpl(pageToGoTo);
    }

    private void goToPageImpl(final int toPage) {
        scrollTo(0, pages.get(toPage).getTop());
    }
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // bounds could be not updated
        post(new Runnable() {
            public void run() {
                currentPageModel.setCurrentPageIndex(getCurrentPage());
            }
        });
        if (inZoom) {
            return;
        }
        // on scrollChanged can be called from scrollTo just after new layout applied so we should wait for relayout
        post(new Runnable() {
            public void run() {
                updatePageVisibility();
            }
        });
    }

    private void updatePageVisibility() {
        for (Page page : pages.values()) {
            page.updateVisibility();
        }
    }

    public void commitZoom() {
        for (Page page : pages.values()) {
            page.invalidate();
        }
        inZoom = false;
    }

    public void showDocument() {
        // use post to ensure that document view has width and height before decoding begin
        post(new Runnable() {
            public void run() {
					try {
						init();
						updatePageVisibility();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.v("menu", "������Ϣ�ǡ�������"+e.getMessage());
						e.printStackTrace();
					}
            }
        });
    }

    public void goToPage(int toPage) {
        if (isInitialized) {
            goToPageImpl(toPage);
        } else {
            pageToGoTo = toPage;
        }
    }

    public int getCurrentPage() {
        for (Map.Entry<Integer, Page> entry : pages.entrySet()) {
            if (entry.getValue().isVisible()) { 
                return entry.getKey();
            }
        }
        return 0;
    }

    public void zoomChanged(float newZoom, float oldZoom) {
    	Log.v("menu", "zoomChanged===");
        inZoom = true;
        stopScroller();
        final float ratio = newZoom / oldZoom;
        invalidatePageSizes();
        scrollTo((int) ((getScrollX() + getWidth() / 2) * ratio - getWidth() / 2), (int) ((getScrollY() + getHeight() / 2) * ratio - getHeight() / 2));
        postInvalidate();//Android�����п���ʹ�õĽ���ˢ�·�����}�֣��ֱ�������Handler������postInvalidate()4ʵ�����߳���ˢ�½��档 
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        if (ev.getPointerCount() == 2) {
			scaleWithFinger(ev);
		} else {
        if (multiTouchZoom != null) {
            if (multiTouchZoom.onTouchEvent(ev)) {
                return true;
            }

            if (multiTouchZoom.isResetLastPointAfterZoom()) {
                setLastPosition(ev);
                multiTouchZoom.setResetLastPointAfterZoom(false);
            }
        }

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();//����VelocityTracker����
        }
        velocityTracker.addMovement(ev);//����ǰ���ƶ��¼����ݸ�VelocityTracker����

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            	 //zoomModel.increaseZoom();
            	 Log.v("LM3", "���¡�����");
                stopScroller();
                setLastPosition(ev);
                if (ev.getEventTime() - lastDownEventTime < DOUBLE_TAP_TIME) {
                    zoomModel.toggleZoomControls();
                } else {
                    lastDownEventTime = ev.getEventTime();
                }
                break;
            case MotionEvent.ACTION_MOVE:
            	
            	 Log.v("LM3", "�����С�����");
                scrollBy((int) (lastX - ev.getX()), (int) (lastY - ev.getY()));
                setLastPosition(ev);
                break;
            case MotionEvent.ACTION_UP:
            	
            	 Log.v("LM3", "̧�𡣡���");
                velocityTracker.computeCurrentVelocity(1000);//���㵱ǰ���ٶȣ�1000��ʾÿ��������أ�pix/second)
                //getXVelocity ()��getYVelocity ()��õ�ǰ���ٶ�
                scroller.fling(getScrollX(), getScrollY(), (int) -velocityTracker.getXVelocity(), (int) -velocityTracker.getYVelocity(), getLeftLimit(), getRightLimit(), getTopLimit(), getBottomLimit());
                velocityTracker.recycle();
                velocityTracker = null;

                break;
        }}
        return   mGestureDetector.onTouchEvent(ev);
    }

    private void setLastPosition(MotionEvent ev) {
        lastX = ev.getX();
        lastY = ev.getY();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    lineByLineMoveTo(1);
                    return true;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    lineByLineMoveTo(-1);
                    return true;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    verticalDpadScroll(1);
                    return true;
                case KeyEvent.KEYCODE_DPAD_UP:
                    verticalDpadScroll(-1);
                    return true;
                    
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void verticalDpadScroll(int direction) {
        scroller.startScroll(getScrollX(), getScrollY(), 0, direction * getHeight() / 2);
        invalidate();
    }

    private void lineByLineMoveTo(int direction) {
        if (direction == 1 ? getScrollX() == getRightLimit() : getScrollX() == getLeftLimit()) {
            scroller.startScroll(getScrollX(), getScrollY(), direction * (getLeftLimit() - getRightLimit()), (int) (direction * pages.get(getCurrentPage()).bounds.height() / 50));
        } else {
            scroller.startScroll(getScrollX(), getScrollY(), direction * getWidth() / 2, 0);
        }
        invalidate();
    }

    private int getTopLimit() {
        return 0;
    }

    private int getLeftLimit() {
        return 0;
    }

    private int getBottomLimit() {
    	if(pages.size()==0){
    		//Toast.makeText(context, "文件错误！", 2);
    		//MyTools.exitShow(context, this, "文件错误！");
    		return -1;
    	}
        return (int) pages.get(pages.size() - 1).bounds.bottom - getHeight();
    }

    private int getRightLimit() {
        return (int) (getWidth() * zoomModel.getZoom()) - getWidth();
    }

    @Override
    public void scrollTo(int x, int y) {
    	if(getBottomLimit()==-1){
    		if(!flage){
    		MyTools.exitShow(context, this, "文件错误！");
    		flage=true;
    		}
    		
    		return;
    	}
        super.scrollTo(Math.min(Math.max(x, getLeftLimit()), getRightLimit()), Math.min(Math.max(y, getTopLimit()), getBottomLimit()));
        viewRect = null;
    }

    RectF getViewRect() {
        if (viewRect == null) {
            viewRect = new RectF(getScrollX(), getScrollY(), getScrollX() + getWidth(), getScrollY() + getHeight());
        }
        return viewRect;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Page page : pages.values()) {
            page.draw(canvas);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float scrollScaleRatio = getScrollScaleRatio();
        invalidatePageSizes();
        invalidateScroll(scrollScaleRatio);
        commitZoom();
    }

    void invalidatePageSizes() {
        if (!isInitialized) {
            return;
        }
        float heightAccum = 0;
        int width = getWidth();
        float zoom = zoomModel.getZoom();
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            float pageHeight = page.getPageHeight(width, zoom);
            page.setBounds(new RectF(0, heightAccum, width * zoom, heightAccum + pageHeight));
            heightAccum += pageHeight;
        }
    }

    private void invalidateScroll(float ratio) {
        if (!isInitialized) {
            return;
        }
        stopScroller();
        final Page page = pages.get(0);
        if (page == null || page.bounds == null) {
            return;
        }
        scrollTo((int) (getScrollX() * ratio), (int) (getScrollY() * ratio));
    }

    private float getScrollScaleRatio() {
        final Page page = pages.get(0);
        if (page == null || page.bounds == null) {
            return 0;
        }
        final float v = zoomModel.getZoom();
        return getWidth() * v / page.bounds.width();
    }

    private void stopScroller() {
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
        }
        
    }
    public HashMap<Integer, Page> getPageMap(){
    	return pages;
    }
    
    public void putMap(){
    	 final int width = decodeService.getEffectivePagesWidth();
         final int height = decodeService.getEffectivePagesHeight();
         for (int i = 0; i < decodeService.getPageCount(); i++) {
             pages.put(i, new Page(this, i));
             pages.get(i).setAspectRatio(width, height);
         }
        
         invalidatePageSizes();
         showDocument();
    }
    
    

    class LearnGestureListener extends GestureDetector.SimpleOnGestureListener {
    	
        @Override
        public boolean onSingleTapUp(MotionEvent ev) {
        	
            return true;
        }

        @Override
        public void onShowPress(MotionEvent ev) {
        	
        }

        @Override
        public void onLongPress(MotionEvent ev) {
        	zoomModel.decreaseZoom();
            invalidatePageSizes();
            postInvalidate();
            updatePageVisibility();
            commitZoom();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onDown(MotionEvent ev) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            return true;
        }
        
        @Override
        public boolean onDoubleTap(MotionEvent event){//˫����Ļʱ����
            Log.d("LM2","onDoubleTap");
            
            zoomModel.increaseZoom();
            invalidatePageSizes();
            postInvalidate();
            updatePageVisibility();
            commitZoom();
            return true;
        }
    }
 // 两点触屏后之间的长度
 	private float beforeLenght;
 	private float afterLenght;

 	/*
 	 * 通过多点触屏放大或缩小图像 beforeLenght用来保存前一时间两点之间的距离 afterLenght用来保存当前时间两点之间的距离
 	 */
 	public void scaleWithFinger(MotionEvent event) {
 		float moveX = event.getX(1) - event.getX(0);
 		float moveY = event.getY(1) - event.getY(0);

 		switch (event.getAction()) {
 		case MotionEvent.ACTION_DOWN:
 			beforeLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));
 			break;
 		case MotionEvent.ACTION_MOVE:
 			// 得到两个点之间的长度
 			afterLenght = (float) Math.sqrt((moveX * moveX) + (moveY * moveY));

 			float gapLenght = afterLenght - beforeLenght;

 			if (gapLenght == 0) {
 				break;
 			}

 			// 如果当前时间两点距离大于前一时间两点距离，则传0，否则传1
 			if (gapLenght > 1) {
 				float size = zoomModel.getZoom();
 				zoomModel.setZoom(size + 0.2f);
 				zoomModel.commit();
 			} else if (gapLenght < 1) {
 				float size = zoomModel.getZoom();
 				if (size > 1) {
 					zoomModel.setZoom(size - 0.2f);
 					zoomModel.commit();
 				}
 			}

 			beforeLenght = afterLenght;
 			break;
 		}
 	}
}


