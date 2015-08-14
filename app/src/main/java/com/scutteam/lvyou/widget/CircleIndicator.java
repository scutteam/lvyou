package com.scutteam.lvyou.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.scutteam.lvyou.R;

import static android.support.v4.view.ViewPager.OnPageChangeListener;

public class CircleIndicator extends LinearLayout implements OnPageChangeListener {

    private final static int DEFAULT_INDICATOR_WIDTH = 5;
    private ViewPager mViewpager;
    private ImageView ivLeftArrow;
    private ImageView ivRightArrow;
    private View[] indicatorList;
    private OnPageChangeListener mViewPagerOnPageChangeListener;
    private int mIndicatorMargin;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorBackground = R.drawable.white_radius;
    private int mCurrentPosition = 1;
    private int mCurrentPage = 0;
    private int mTotalPage = 0;
    private int remainder = 0;
    private int mFirstPage = 0;
    private final int mDefaultCount = 5;
    AnimationSet animationOut;
    AnimationSet animationIn;
    private boolean isEnd = false;

    public CircleIndicator(Context context) {
        super(context);
        init(context, null);
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        handleTypedArray(context, attrs);
        initAnimation();
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray =
                    context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);
            mIndicatorWidth =
                    typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_width, -1);
            mIndicatorHeight =
                    typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_height, -1);
            mIndicatorMargin =
                    typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_margin, -1);
            mIndicatorBackground = typedArray.getResourceId(R.styleable.CircleIndicator_ci_drawable,
                    R.drawable.white_radius);
            typedArray.recycle();
        }
        mIndicatorWidth =
                (mIndicatorWidth == -1) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorWidth;
        mIndicatorHeight =
                (mIndicatorHeight == -1) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorHeight;
        mIndicatorMargin =
                (mIndicatorMargin == -1) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorMargin;
    }

    public void setViewPager(ViewPager viewPager, int position) {
        mCurrentPosition = position % 5 + 1;
        mViewpager = viewPager;
        createIndicators(viewPager);
        mViewpager.setOnPageChangeListener(this);
    }

    public void setViewPager(ViewPager viewPager) {
        setViewPager(viewPager, 0);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        if (mViewpager == null) {
            throw new NullPointerException("can not find Viewpager , setViewPager first");
        }
        mViewPagerOnPageChangeListener = onPageChangeListener;
        mViewpager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        if (mViewPagerOnPageChangeListener != null) {
            mViewPagerOnPageChangeListener.onPageScrolled(position, positionOffset,
                    positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mViewPagerOnPageChangeListener != null) {
            mViewPagerOnPageChangeListener.onPageSelected(position);
        }

        /*Animator mAnimIn = mAnimationIn;
        mAnimIn.setTarget(getChildAt(mCurrentPosition));
        mAnimIn.start();

        Animator mAnimOut = mAnimationOut;
        mAnimOut.setTarget(getChildAt(position % 5 + 1));
        mAnimOut.start();*/
        getChildAt(mCurrentPosition).startAnimation(animationOut);
        mCurrentPosition = position % 5 + 1;
        mCurrentPage = position / 5;
        checkPageCount();
        indicatorList[position % 5].startAnimation(animationIn);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mViewPagerOnPageChangeListener != null) {
            mViewPagerOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    private void createIndicators(ViewPager viewPager) {
        removeAllViews();
        isEnd = false;
        int count = viewPager.getAdapter().getCount();
        remainder = count % 5;
        mTotalPage = remainder == 0 ? count / mDefaultCount : count / mDefaultCount + 1;
        if (count <= 0) {
            return;
        }
        ivLeftArrow = addArrow(R.drawable.icon_left_arrow, View.INVISIBLE);
        indicatorList = new View[mDefaultCount];

        for (int i = 0; i < mDefaultCount; i++) {
            View Indicator = new View(getContext());
            Indicator.setBackgroundResource(mIndicatorBackground);
            indicatorList[i] = Indicator;
            addView(Indicator, mIndicatorWidth, mIndicatorHeight);
            LayoutParams lp = (LayoutParams) Indicator.getLayoutParams();
            lp.leftMargin = mIndicatorMargin;
            lp.rightMargin = mIndicatorMargin;
            Indicator.setLayoutParams(lp);
            //mAnimationOut.setTarget(Indicator);
            //mAnimationOut.start();
            Indicator.startAnimation(animationOut);
        }

        ivRightArrow = addArrow(R.drawable.icon_right_arrow, View.VISIBLE);


        getChildAt(mCurrentPosition).startAnimation(animationIn);

        checkPageCount();
    }

    /**
     * 根据页码数判断箭头的可视性
     */
    private void checkPageCount() {
        if (mCurrentPage + 1 <= mTotalPage - 1) {
            ivRightArrow.setVisibility(View.VISIBLE);
            if (isEnd && remainder != 0) {
                for (int i = remainder; i < mDefaultCount; i++) {
                    //indicatorList[i].startAnimation(animationOut);
                    indicatorList[i].setVisibility(View.VISIBLE);
                    indicatorList[i].startAnimation(animationOut);
                }
                isEnd = false;
            }
        } else {
            ivRightArrow.setVisibility(View.INVISIBLE);
            if (!isEnd && remainder != 0) {
                for (int i = remainder; i < mDefaultCount; i++) {
                    //indicatorList[i].startAnimation(animationGone);
                    indicatorList[i].clearAnimation();
                    indicatorList[i].setVisibility(View.GONE);
                }
                isEnd = true;
            }
        }
        if (mCurrentPage - 1 >= mFirstPage) {
            ivLeftArrow.setVisibility(View.VISIBLE);
        } else {
            ivLeftArrow.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 添加箭头
     */
    private ImageView addArrow(int resId, int visbility) {
        ImageView ivArrow = new ImageView(getContext());
        ivArrow.setImageResource(resId);
        addView(ivArrow, dip2px(7), dip2px(7));
        LayoutParams lp = (LayoutParams) ivArrow.getLayoutParams();
        lp.leftMargin = mIndicatorMargin;
        lp.rightMargin = mIndicatorMargin;
        ivArrow.setLayoutParams(lp);
        ivArrow.setVisibility(visbility);
        return ivArrow;
    }

    /**
     * 初始化Indicator动画
     */
    private void initAnimation() {
        animationOut = new AnimationSet(true);   //未选中动画
        animationIn = new AnimationSet(true);    //选中动画

        ScaleAnimation scaleIn = new ScaleAnimation(1.0f, 1.6f, 1.0f, 1.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleIn.setInterpolator(new LinearInterpolator());
        scaleIn.setFillAfter(true);
        animationIn.addAnimation(scaleIn);

        AlphaAnimation alphaIn = new AlphaAnimation(1.0f, 1.0f);
        alphaIn.setFillAfter(true);
        animationIn.addAnimation(alphaIn);
        animationIn.setFillAfter(true);
        animationIn.setDuration(0);

        ScaleAnimation scaleOut = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleOut.setInterpolator(new LinearInterpolator());
        scaleOut.setFillAfter(true);
        animationOut.addAnimation(scaleOut);

        AlphaAnimation alphaOut = new AlphaAnimation(0.5f, 0.5f);
        alphaOut.setFillAfter(true);
        animationOut.addAnimation(alphaOut);
        animationOut.setFillAfter(true);

    }

    private class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float value) {
            return Math.abs(1.0f - value);
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getmCurrentPosition() {
        return mCurrentPosition;
    }

    public void setmCurrentPosition(int mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition % 5 + 1;
    }

    public void setArrowVisbility(int visibility) {
        ivLeftArrow.setVisibility(visibility);
        ivRightArrow.setVisibility(visibility);
    }
}
