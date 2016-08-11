package com.zl.slidview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zl.slidview.utils.SizeUtils;

/**
 * 侧滑的view
 * Created by zl on 16/8/11.
 */
public class SlidView extends RelativeLayout {
    private Context context;
    private ViewDragHelper mViewDragHelper;
    private int mHeight;//view 的高
    private int mWidth;//view的宽
    private ViewGroup mContentView;
    private int startX;
    private boolean isOpen;//打开关闭的状态
    private OnShowChildViewListener listener;
    private int mLeft;//距离左边的距离

    public SlidView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public SlidView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidView(Context context) {
        this(context, null);
    }

    /**
     * 初始化
     */
    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View arg0, int arg1) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return Math.max(0, left);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == mContentView) {
                    if (mContentView.getLeft() > SizeUtils.dip2px(getContext(), 100)) {
                        open();
                    } else {
                        close();
                    }
                }
                invalidate();
            }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int diff = moveX - startX;
                if (isOpen) {
                    mContentView.layout(mWidth + diff, 0, 2 * mWidth + diff, mHeight);//当打开的时候让view随着手指滑动
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL://100dp为界限，超过100dp松手自己打开或者关闭
                if (isOpen && mContentView.getLeft() < mWidth - SizeUtils.dip2px(getContext(), 100)) {
                    close();
                    isOpen = false;
                } else if (mContentView.getLeft() >= mWidth - SizeUtils.dip2px(getContext(), 100)) {
                    open();
                }
                break;

        }
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            mLeft = mContentView.getLeft();//这点很重要，我最近做的聊天室由于里面有点赞和聊天。都会造成view的重绘，我们要让view每次停留在原来的位置
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mContentView.offsetLeftAndRight(mLeft);//重绘的时候让距离左边的mleft距离
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = (ViewGroup) getChildAt(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    public void open() {//open annimation
        open(true);
    }

    public void close() {//close annimation
        close(true);
    }

    public void open(boolean isSmooth) {
        int finalLeft = mWidth;
        if (isSmooth) {
            if (mViewDragHelper.smoothSlideViewTo(mContentView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
                if (listener != null){
                    listener.open();
                }
                isOpen = true;
            }
        } else {
            mContentView.layout(finalLeft, 0, 2 * finalLeft, mHeight);
        }
    }

    public void close(boolean isSmooth) {
        int finalLeft = 0;
        if (isSmooth) {
            if (mViewDragHelper.smoothSlideViewTo(mContentView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
                if (listener != null){
                    listener.close();
                }
                isOpen = false;
            }

        } else {
            mContentView.layout(finalLeft, 0, mWidth, mHeight);
        }
    }

    public interface OnShowChildViewListener {
        void close();

        void open();
    }

    /**
     * 打开关闭的监听
     * @param listener
     */
    public void setOnShowChildViewListener(OnShowChildViewListener listener) {
        this.listener = listener;
    }
}
