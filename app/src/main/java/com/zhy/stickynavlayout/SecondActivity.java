package com.zhy.stickynavlayout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.zhy.stickynavlayout.view.StickyNavLayout;

/**
 * Created by yangsimin on 2018/11/28.
 */

public class SecondActivity extends FragmentActivity {
//    private StickyNavLayout mStickyNavLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initViews();


    }

    private void initViews() {
//        mStickyNavLayout = (StickyNavLayout) findViewById(R.id.sticky_layout);
//        View view  = findViewById(R.id.id_stickynavlayout_innerscrollview);
//        mStickyNavLayout.setTopActionViewHeight(200);
    }
}
