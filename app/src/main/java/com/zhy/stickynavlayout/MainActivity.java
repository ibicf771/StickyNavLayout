package com.zhy.stickynavlayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.zhy.stickynavlayout.adapter.AdapterWrapperHelper;
import com.zhy.stickynavlayout.adapter.BaseAdapter;
import com.zhy.stickynavlayout.adapter.HeaderAndFooterWrapper;
import com.zhy.stickynavlayout.adapter.LoadMoreWrapper;
import com.zhy.stickynavlayout.view.StickyNavLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private List<String> mDatas = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LoadMoreWrapper mLoadMoreWrapper;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private StickyNavLayout mStickyNavLayout;
    private AdapterWrapperHelper mAdapterWrapperHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initDatas();
    }

    private void initViews() {
        mRecyclerView = (RecyclerView)findViewById(R.id.id_stickynavlayout_innerscrollview);
        mStickyNavLayout = (StickyNavLayout) findViewById(R.id.sticky_layout);
        mStickyNavLayout.setTopActionViewHeight(200);

        findViewById(R.id.second_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    private void initDatas() {
        for (int i = 0; i < 20; i++) {
            mDatas.add("简介k" + " -> " + i);
        }
        BaseAdapter baseAdapter = new BaseAdapter(mDatas);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapterWrapperHelper = new AdapterWrapperHelper(mStickyNavLayout, baseAdapter);
        mRecyclerView.setAdapter(mAdapterWrapperHelper.getWrapperAdapter());

        mAdapterWrapperHelper.setLoadMoreCallback(new AdapterWrapperHelper.ILoadMoreCallback() {
            @Override
            public void loadMoreCallback() {

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(isMoreData > 0){
                            isMoreData--;
                            for (int i = 0; i < 10; i++) {
                                mDatas.add("简介More" + " -> " + i);
                            }
                            mAdapterWrapperHelper.loadMoreResult(true);
                        }else {
                            mAdapterWrapperHelper.loadMoreResult(false);
                        }
                    }
                }, 1500);

            }
        });
    }

    private int isMoreData = 2;
}