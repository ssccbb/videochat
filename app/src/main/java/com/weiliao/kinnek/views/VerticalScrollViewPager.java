package com.weiliao.kinnek.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by 神荼 on 2017/12/20.
 */

public class VerticalScrollViewPager extends VerticalViewPager {

    private final String TAG = VerticalScrollViewPager.class.getSimpleName();

    private float mLastY;
    private float mLastX;

    public VerticalScrollViewPager(Context context) {
        super(context);
        setViewPagerScroll();
    }

    public VerticalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setViewPagerScroll();
    }


    private void setViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller mViewPagerScroller = new ViewPagerScroller(getContext(), new AccelerateInterpolator());
            mViewPagerScroller.setDuration(1500);
            mScroller.set(this, mViewPagerScroller);

        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                // 手指按下
//                mLastY = ev.getY();
//                mLastX = ev.getX();
//                Log.e(TAG, "mLastY:" + mLastY);
//                Log.e(TAG, "mLastX:" + mLastX);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.e(TAG, "X:" + ev.getX());
//                Log.e(TAG, "Y:" + ev.getY());
//                float distanceY = Math.abs(ev.getY() - mLastY);
//                float distanceX = Math.abs(ev.getX() - mLastX);
//                if (distanceY > distanceX) {
//                    return super.dispatchTouchEvent(ev);
//                }
//                // 手指移动
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.e(TAG, "UP Y:" + ev.getY());
//                if (Math.abs(ev.getY() - mLastY) > 30) {
//                    return super.dispatchTouchEvent(ev);
//                }
//                // 手指抬起
//                break;
//        }
//        return false;
//    }

    /**
     *
     ＊由于ViewPager 默认的切换速度有点快，因此用一个Scroller 来控制切换的速度
     * <p>而实际上ViewPager 切换本来就是用的Scroller来做的，因此我们可以通过反射来</p>
     * <p>获取取到ViewPager 的 mScroller 属性，然后替换成我们自己的Scroller</p>
     */
    public static class ViewPagerScroller extends Scroller {
        private int mDuration = 800;// ViewPager默认的最大Duration 为600,我们默认稍微大一点。值越大越慢。
        private boolean mIsUseDefaultDuration = false;

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy,mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mIsUseDefaultDuration?duration:mDuration);
        }

        public void setUseDefaultDuration(boolean useDefaultDuration) {
            mIsUseDefaultDuration = useDefaultDuration;
        }

        public boolean isUseDefaultDuration() {
            return mIsUseDefaultDuration;
        }

        public void setDuration(int duration) {
            mDuration = duration;
        }


        public int getScrollDuration() {
            return mDuration;
        }
    }
}
