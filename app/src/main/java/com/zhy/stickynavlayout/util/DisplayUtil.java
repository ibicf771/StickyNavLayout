package com.zhy.stickynavlayout.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by yangsimin on 2018/4/10.
 */

public class DisplayUtil {

    /**
     * 获取控件的高度
     * @return
     */
    public static int getViewHeight(Context context, int layoutId){
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(layoutId, null);

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的宽度
     * @return
     */
    public static int getViewWidth(Context context, int layoutId){
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(layoutId, null);

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredWidth();
    }

    /**
     * 获取控件的高度
     * @return
     */
    public static int getViewHeight(Context context, View view){

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    /**
     * 获取控件的高度
     * @return
     */
    public static int getViewWidth(Context context, View view){

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredWidth();
    }

    /**
     * 获取屏幕的高度
     * @return
     */
    public static int getDisplayHeiget(Context context){
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;       // 屏幕高度（像素）
    }

    /**
     * 获取状态栏的高度
     * @return
     */
    public static int getStatusBarHeight(Context context){
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

}
