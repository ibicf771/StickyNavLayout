package com.zhy.stickynavlayout.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.OverScroller;

import com.zhy.stickynavlayout.R;

/**
 * Created by yangsimin on 2018/4/6.
 * StickyNavLayout的辅助类，用于滑动动画的处理
 */

public class StickyViewHelper {

    private StickyNavLayout mStickyNavLayout;

    private OverScroller mOverScroller;

    private RecyclerView mRecyclerView;

    private int mTopViewHeight;

    /**
     * StickyNavLayout相对屏幕的位置
     */
    private int[] mLayoutLocation = new int[2];
    /**
     * recycleView相对屏幕的位置
     */
    private int[] mRecyclerViewLocation = new int[2];

    private int mLastScrollState;

    /**
     * recyclerView 滑动停下的三个位置
     */
    private int RECYCLER_VIEW_SCROLL_LOCATION_TOP;

    private int RECYCLER_VIEW_SCROLL_LOCATION_MIDDLE;

    private int RECYCLER_VIEW_SCROLL_LOCATION_BOTTOM;

    /**
     * 回弹速度
     */
    private static int SPEED = 4000;


    private static StickyViewHelper instance;

    public static StickyViewHelper getInstance(StickyNavLayout stickyNavLayout) {
        if (instance == null) {
            instance = new StickyViewHelper(stickyNavLayout);
        }
        return instance;
    }

    private StickyViewHelper(StickyNavLayout stickyNavLayout){
        Log.d("StickyViewHelper", "StickyViewHelper create");

        mStickyNavLayout = stickyNavLayout;
        mOverScroller = mStickyNavLayout.getScroller();
        mRecyclerView = mStickyNavLayout.getRecyclerView();
        mTopViewHeight = mStickyNavLayout.getTopViewHeight();
        srcollThreshold = getScrollHeight()/10;
        mLastScrollState = getScrollHeight() ;
        initScrollLocation();


        mStickyNavLayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                //因为Scroll的fling方法没有滑动结束的回调，所以只能的在滚动的回调里记录滑动停下的位置
                int location = getRecyclerViewY() - getLayoutY();
                Log.d("StickyViewHelper", "OnScrollChangedListener getRecyclerViewY():" + (getRecyclerViewY() - getLayoutY()));
                if(location == RECYCLER_VIEW_SCROLL_LOCATION_TOP
                        || location == RECYCLER_VIEW_SCROLL_LOCATION_MIDDLE
                        || location == RECYCLER_VIEW_SCROLL_LOCATION_BOTTOM){
                    mLastScrollState = location;
                }
            }
        });
    }

    private void initScrollLocation(){
        RECYCLER_VIEW_SCROLL_LOCATION_TOP = 0;
        RECYCLER_VIEW_SCROLL_LOCATION_MIDDLE = getScrollHeight()/2;
        RECYCLER_VIEW_SCROLL_LOCATION_BOTTOM = getScrollHeight() ;
        Log.d("StickyViewHelper", "RECYCLER_VIEW_SCROLL_LOCATION_BOTTOM:" + RECYCLER_VIEW_SCROLL_LOCATION_BOTTOM);
    }

    private int getRecyclerViewY(){
        mRecyclerView.getLocationOnScreen(mRecyclerViewLocation);
        return mRecyclerViewLocation[1] - getTopActionViewHeight();
    }

    private int getLayoutY(){
        mStickyNavLayout.getLocationOnScreen(mLayoutLocation);
        return mLayoutLocation[1];
    }

    private int getLayoutHeight(){
        return mStickyNavLayout.getHeight();
    }

    private int getScrollY(){
        return getLayoutY() + mRecyclerView.getTop() - getRecyclerViewY() - getTopActionViewHeight();
    }

    private int getScrollHeight(){
        return mTopViewHeight - getTopActionViewHeight();
    }

    private int getTopActionViewHeight(){
        return mStickyNavLayout.getTopActionViewHeight();
    }

    /**
     * 开始滑动动画
     */
    public void startStickAnimation(){
        int direction = getDirection();
        int minY = getMinY();
        int maxY = getMaxY();
        startFlingAnimation(direction, minY, maxY);
    }


    private int getLastScrollState(){
        return mLastScrollState;
    }

    private int srcollThreshold;


    /**
     * 1是向上滚动，-1是向下滚动
     * @return
     */
    private int getDirection(){
        return getSmoothScrollDirection();
    }


    /**
     * 1是向上滚动，-1是向下滚动,mDirection只能是1或者-1
     */
    private int mDirection = 1;
    /**
     * 类似百度和高德的滑动回弹的方向判断策略
     * @return
     */
    private int getSmoothScrollDirection(){
        int lastScrollState = getLastScrollState();
        int recyclerViewY = getRecyclerViewY();
        Log.d("StickyViewHelper", "getLastScrollState():" + lastScrollState);
        Log.d("StickyViewHelper", "getRecyclerViewY():" + recyclerViewY);
        Log.d("StickyViewHelper", "mStickyNavLayout.mFlingSpeed:" + mStickyNavLayout.mFlingSpeed);

        if(lastScrollState - recyclerViewY> 0){
            //上一次的停留点在现在view的位置之下，手势滑动向上
            return getDirectionFromLocation(1);
        }else {
            //上一次的停留点在现在view的位置之上，手势滑动向下
            return getDirectionFromLocation(-1);
        }
    }

    /**
     * 根据速度和手势滑动距离判断继续滑动的方向
     * @param direction
     * @return
     */
    private int getDirectionFromLocation(int direction){
        //滑动速度大于一定值时，不用判断滑动距离是否大于阀值，直接返回方向
        if(Math.abs(mStickyNavLayout.mFlingSpeed) > 500){
            //滑动的距离如果小于阀值，则方向返回速度的方向，增加这个判断，防止抖动
            if(Math.abs(getLastScrollState() - getRecyclerViewY()) < srcollThreshold){
                if(mStickyNavLayout.mFlingSpeed > 0){
                    return 1;
                }else {
                    return -1;
                }
            }
            return direction;
        }
        if(Math.abs(getLastScrollState() - getRecyclerViewY())> srcollThreshold){
            return direction;
        }else {
            return -direction;
        }
    }

    /**
     * 将可滑动区域划分成两个区间，下半区间最小值滑动区间值的一半， 上半区间最小值0
     * @return
     */
    private int getMinY(){
        if(getRecyclerViewY() > RECYCLER_VIEW_SCROLL_LOCATION_MIDDLE){
            return RECYCLER_VIEW_SCROLL_LOCATION_MIDDLE;
        }else {
            return 0;
        }
    }

    /**
     * 可滑动区域划分成两个区间，下半区间最大值滑动区间值， 上半区间最大值滑动区间值的一半
     * @return
     */
    private int getMaxY(){
        if (getRecyclerViewY() > RECYCLER_VIEW_SCROLL_LOCATION_MIDDLE){
            return RECYCLER_VIEW_SCROLL_LOCATION_BOTTOM;
        }else {
            return RECYCLER_VIEW_SCROLL_LOCATION_MIDDLE;
        }
    }

    private void startFlingAnimation(int direction, int minY, int maxY){
        Log.d("StickyViewHelper", "minY:" + minY + " maxY:" + maxY + " direction:" + direction);
        mOverScroller.fling(0, getScrollY(),
                0, SPEED * direction, 0, 0,
                getScrollHeight() - maxY, getScrollHeight() - minY);
        mStickyNavLayout.invalidate();
    }

    /**
     * 直接从起始位置滑动到中间位置
     */
    public void startUpScrollAnimation(){
        mOverScroller.fling(0, getScrollY(),
                0, SPEED, 0, 0,
                0, RECYCLER_VIEW_SCROLL_LOCATION_MIDDLE);
        mStickyNavLayout.invalidate();
    }
}
