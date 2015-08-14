package com.scutteam.lvyou.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 该类可以设置ViewPager是否允许滑动
 */
public class ScrollableViewPager extends ViewPager {

    private boolean mScrollable = true;

    public ScrollableViewPager(Context context) {
        super(context);
    }

    public ScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setmScrollable(boolean isCanScroll) {
        this.mScrollable = isCanScroll;
    }


    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if (mScrollable) {
            return super.onTouchEvent(arg0);
        } else {
            return false;
        }

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if (mScrollable) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }


}