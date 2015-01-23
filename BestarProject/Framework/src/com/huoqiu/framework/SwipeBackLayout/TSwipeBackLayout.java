
package com.huoqiu.framework.SwipeBackLayout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.*;
import android.widget.FrameLayout;
import com.huoqiu.framework.R;
import com.huoqiu.framework.SwipeBackLayout.app.Fragment.SwipeBackFragment;

public class TSwipeBackLayout extends FrameLayout {
    private Context mContext;
    //private View mContentView;
    private VelocityTracker mVelocityTracker;
    private ViewConfiguration mViewConfiguration;

    /**触发宽度 触发范围0-mWidth*/
    private int mWidth;
    /**触发高度 触发范围mHight-屏幕高度*/
    private int mHight;

    public static final int SCROLL_STATE_NO_ALLOW = 0;
    public static final int SCROLL_STATE_ALLOW = 1;
    private int mScrollState = 0;

    private int mVelocityValue = 0;

    private boolean mEnableSwipe = true;

    private Activity mActivity;

    private static final int AttachToActivity = 0;

    private Fragment mFrament;

    /**
     *the SwipeBackLayout will attach to Fragment
     */
    private static final int AttachToFragment = 2;

    /**
     * SwipeBackLayout will attach to Activity or Fragment
     */
    private int mAttachToMode = AttachToActivity;

    public TSwipeBackLayout(Context context) {
        this(context, null);
    }

    public TSwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SwipeBackLayoutStyle);
    }

    public TSwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mContext = context;
        mViewConfiguration = ViewConfiguration.get(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeBackLayout, defStyle,
                R.style.SwipeBackLayout);
        int edgeSize = a.getDimensionPixelSize(R.styleable.SwipeBackLayout_edge_size, -1);
        int startAltitude = a.getDimensionPixelSize(R.styleable.SwipeBackLayout_start_altitude, -1);
        setEdgeSize(edgeSize,startAltitude);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (x <= mWidth && mEnableSwipe && y >= mHight) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        obtainVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (mEnableSwipe && x <= mWidth && y >= mHight) {
                    mScrollState = SCROLL_STATE_ALLOW;
                    return true;
                } else {
                    mScrollState = SCROLL_STATE_NO_ALLOW;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.computeCurrentVelocity(1000, mViewConfiguration.getScaledMaximumFlingVelocity());
                mVelocityValue = (int) mVelocityTracker.getXVelocity();
                break;

            case MotionEvent.ACTION_UP:
                if (mScrollState == SCROLL_STATE_ALLOW) {
                    if (mVelocityValue > 2000) {
                        callSwipeAction();
                    }
                }
                releaseVelocityTracker();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void callSwipeAction(){
        if(getAttchMode() == AttachToActivity){
            finishActivity();
        }else {
            removeFragment();
        }
    }

    private void finishActivity(){
        if(null != mActivity){
            mActivity.finish();
        }
    }

    private void removeFragment(){
        if(null != mFrament){
            ((SwipeBackFragment)mFrament).scrollToRemoveFragment();
        }

    }

    public void attachToActivity(Activity activity) {
        setAttchMode(AttachToActivity);
        mActivity = activity;
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decor.removeView(decorChild);
        addView(decorChild);
        decor.addView(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void attachToFragment(Fragment fragment) {
        setAttchMode(AttachToFragment);
        mFrament = fragment;
        ViewGroup decor = (ViewGroup) fragment.getView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decor.removeView(decorChild);
        addView(decorChild);
        decor.addView(this);
    }
    /**
     * 设置触发边缘
     * @param width
     * @param hight
     */
    public void setEdgeSize(int width,int hight) {
        if (width > 0){
            mWidth = width;
            //mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        }
        if(hight > 0){
            mHight = hight;
        }
    }

    public boolean ismEnableSwipe() {
        return mEnableSwipe;
    }

    public void setmEnableSwipe(boolean mEnableSwipe) {
        this.mEnableSwipe = mEnableSwipe;
    }

    public int getAttchMode() {
        return mAttachToMode;
    }

    public void setAttchMode(int attchMode) {
        this.mAttachToMode = attchMode;
    }

    private void obtainVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
