package com.zhy.stickynavlayout.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.zhy.stickynavlayout.R;


/**
 * 布局中StickyNavLayout必须铺满整个屏幕，既heigth必须为match
 */
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

        setTopViewHeight();
    }


    /**
     * 设置recyclerView上面控件的高度，也就是recyclerView的初始高度
     */
    private void setTopViewHeight(){
        ViewGroup.LayoutParams layoutParamsTop = mTop.getLayoutParams();
        layoutParamsTop.height = getDisplayHeiget() - getStatusBarHeight() - getFirstItemHeight();
        mTop.setLayoutParams(layoutParamsTop);
    }

    /**
     * 获取recyclerView列表header的高度
     * @return
     */
    private int getFirstItemHeight(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.for_more_search_result_item, null);

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    /**
     * 获取屏幕的高度
     * @return
     */
    private int getDisplayHeiget(){
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;       // 屏幕高度（像素）
    }

    /**
     * 获取状态栏的高度
     * @return
     */
    private int getStatusBarHeight(){
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTopViewHeight = mTop.getMeasuredHeight();

        ViewGroup.LayoutParams layoutParams = mRecyclerView.getLayoutParams();
        layoutParams.height = getMeasuredHeight() - getTopActionViewHeight();
        mRecyclerView.setLayoutParams(layoutParams);

        mStickyViewHelper = new StickyViewHelper(this);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.for_more_search_result_item, null);

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();

        Log.d("StickyNavLayout", "view height:" + height
                + " getMeasuredHeight() " + getMeasuredHeight()
                + " mTop.getMeasuredHeight() " + mTop.getMeasuredHeight()
                + " gmTopViewHeight " + mTopViewHeight);

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