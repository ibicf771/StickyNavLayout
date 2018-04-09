package com.zhy.stickynavlayout.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.zhy.stickynavlayout.R;


public class StickyNavLayout extends LinearLayout implements android.support.v4.view.NestedScrollingParent {

    private NestedScrollingParentHelper parentHelper = new NestedScrollingParentHelper(this);
    private View mTop;
    private int mTopViewHeight;
    private OverScroller mScroller;
    private RecyclerView mRecyclerView;
    private Context mContext;

    private StickyViewHelper mStickyViewHelper;

    private int mTopActionViewHeight = 100;

    public StickyNavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOrientation(LinearLayout.VERTICAL);

        mScroller = new OverScroller(context);
    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    public OverScroller getScroller(){
        return mScroller;
    }

    public int getTopViewHeight(){
        return mTopViewHeight;
    }

    public int getTopActionViewHeight(){
        return mTopActionViewHeight;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTop = findViewById(R.id.id_stickynavlayout_topview);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_stickynavlayout_innerscrollview);
//        final HeaderAndFooterWrapper headerAndFooterWrapper = (HeaderAndFooterWrapper)mRecyclerView.getAdapter();
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                setOnScrollChangeListener(new OnScrollChangeListener() {
//                    @Override
//                    public void onScrollChange(View view, int i, int i1, int i2, int i3)
//                    {
//                        Log.d("StickyNavLayout", "mTopViewHeight:" + mTopViewHeight);
//                        Log.d("StickyNavLayout", "getScaleY():" + getScaleY());
//                        Log.d("StickyNavLayout", "StickyNavLayout i:" + i + " i1:" + i1);
//                        if(i1 < 200){
//                            if(headerAndFooterWrapper.getHeadersCount() == 0){
//                                Button t1 = new Button(mContext);
//                                t1.setText("Header 1");
//                                headerAndFooterWrapper.addHeaderView(t1);
//                            }
//                        }else {
//                            if(headerAndFooterWrapper.getHeadersCount() > 0){
//                                headerAndFooterWrapper.removeAllHeaderView();
//                            }
//                        }
//                    }
//                });
//            }
//        }, 1000);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    private Handler mHandler = new Handler();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTopViewHeight = mTop.getMeasuredHeight();
        ViewGroup.LayoutParams layoutParams = mRecyclerView.getLayoutParams();
        layoutParams.height = getMeasuredHeight() - getTopActionViewHeight();
        mRecyclerView.setLayoutParams(layoutParams);
        mStickyViewHelper = new StickyViewHelper(this);
    }

    private int[] mRecyclerViewLocation = new int[2];
    public int getRecyclerViewY(){
        mRecyclerView.getLocationOnScreen(mRecyclerViewLocation);
        return mRecyclerViewLocation[1];
    }
    private int[] mLayoutLocation = new int[2];

    private int getLayoutY(){
        getLocationOnScreen(mLayoutLocation);
        return mLayoutLocation[1];
    }

    public int getMScrollY(){
        return getLayoutY() + mRecyclerView.getTop() - getRecyclerViewY();
    }

    public void startUpScroll(){
        mStickyViewHelper.startUpScrollAnimation();
    }

    public void setTopActionViewHeight(int topActionViewHeight){
        mTopActionViewHeight = topActionViewHeight;
    }

    @Override
    public void scrollTo(int x, int y) {
        //限制滚动范围
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }



    //实现NestedScrollParent接口-------------------------------------------------------------------------

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        parentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.d("StickyNavLayout", "onStopNestedScroll");
        mStickyViewHelper.startStickAnimation();
        mFlingSpeed = 0;
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight - getTopActionViewHeight();
        boolean showTop = dy < 0 && getScrollY() >=  getTopActionViewHeight() && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    //boolean consumed:子view是否消耗了fling
    //返回值：自己是否消耗了fling。可见，要消耗只能全部消耗
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.d("StickyNavLayout", "onNestedFling velocityY:" + velocityY);
        return false;
    }

    public int mFlingSpeed;

    //返回值：自己是否消耗了fling。可见，要消耗只能全部消耗
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.d("StickyNavLayout", "onNestedPreFling velocityY:" + velocityY);
        if (getScrollY() < mTopViewHeight -  getTopActionViewHeight()) {
//            mStickyViewHelper.startFling((int)velocityY);
            mFlingSpeed = (int)velocityY;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return parentHelper.getNestedScrollAxes();
    }
}