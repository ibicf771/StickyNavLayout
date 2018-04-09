package com.zhy.stickynavlayout.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.OverScroller;

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
     * 回弹速度
     */
    private static int SPEED = 4000;

    public StickyViewHelper(StickyNavLayout stickyNavLayout){
        mStickyNavLayout = stickyNavLayout;
        mOverScroller = mStickyNavLayout.getScroller();
        mRecyclerView = mStickyNavLayout.getRecyclerView();
        mTopViewHeight = mStickyNavLayout.getTopViewHeight();
        srcollThreshold = getScrollHeight()/10;
        mLastScrollState = getScrollHeight() + getTopActionViewHeight();
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
        setLastScrollState(direction, minY, maxY);
    }

    /**
     * 设置滑块view停留的位置
     * @param direction
     * @param minY
     * @param maxY
     */
    private void setLastScrollState(int direction, int minY, int maxY){
        if(direction > 0){
            mLastScrollState = minY;
        }else {
            mLastScrollState = maxY;
        }
        Log.d("StickyViewHelper", "mLastScrollState:" + mLastScrollState);
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
//            mStickyNavLayout.mFlingSpeed = 0;
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
        if(getRecyclerViewY() > getScrollHeight()/2){
            return getScrollHeight()/2;
        }else {
            return 0;
        }
    }

    /**
     * 可滑动区域划分成两个区间，下半区间最大值滑动区间值， 上半区间最大值滑动区间值的一半
     * @return
     */
    private int getMaxY(){
        if (getRecyclerViewY() > getScrollHeight()/2){
            return getScrollHeight() + getTopActionViewHeight();
        }else {
            return getScrollHeight()/2;
        }
    }

    private void startFlingAnimation(int direction, int minY, int maxY){
        Log.d("StickyViewHelper", "minY:" + minY + " maxY:" + maxY + " direction:" + direction);
        mOverScroller.fling(0, getScrollY(),
                0, SPEED * direction, 0, 0,
                getScrollHeight() - maxY, getScrollHeight() - minY);
        mStickyNavLayout.invalidate();
    }

    public void startUpScrollAnimation(){
        mOverScroller.fling(0, getScrollY(),
                0, SPEED, 0, 0,
                0, getScrollHeight()/2);
        mStickyNavLayout.invalidate();
    }
}
