package com.zhy.stickynavlayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;
import com.zhy.stickynavlayout.view.HeaderAndFooterWrapper;
import com.zhy.stickynavlayout.view.LoadMoreWrapper;
import com.zhy.stickynavlayout.view.StickyNavLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    private List<String> mDatas = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LoadMoreWrapper mLoadMoreWrapper;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private StickyNavLayout mStickyNavLayout;

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
    }

    private void initDatas() {
        for (int i = 0; i < 20; i++) {
            mDatas.add("简介k" + " -> " + i);
        }
        TasksAdapter tasksAdapter = new TasksAdapter(mDatas);
//        mRecyclerView.setAdapter(tasksAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initHeaderAndFooter(tasksAdapter);
        initLoadMore();

    }

    private void initLoadMore(){
        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);

        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener()
        {
            @Override
            public void onLoadMoreRequested()
            {
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
//                        for (int i = 0; i < 10; i++)
//                        {
//                            mDatas.add("Add:" + i);
//                        }
//                        mLoadMoreWrapper.notifyDataSetChanged();
//                        mLoadMoreWrapper.setLoadMoreViewShow(false);
                        setNoMoreDatas();
                    }
                }, 3000);
            }
        });
        mRecyclerView.setAdapter(mLoadMoreWrapper);
    }

    private void setNoMoreDatas(){
        mLoadMoreWrapper.removeLoadMoreView();
        mLoadMoreWrapper.notifyDataSetChanged();

        mHeaderAndFooterWrapper.addFootView(R.layout.default_no_more_load_data);
        mLoadMoreWrapper.notifyDataSetChanged();
    }

    private void initHeaderAndFooter(RecyclerView.Adapter adapter)
    {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        mHeaderAndFooterWrapper.addHeaderView(R.layout.for_more_search_result_item);
        mHeaderAndFooterWrapper.setOnHeaderViewClickListener(new HeaderAndFooterWrapper.OnHeaderViewClickListener() {
            @Override
            public void onHeaderViewClick() {
                Log.d("MainActivity1", "mHeaderAndFooterWrapper onHeaderViewClick ");
                mStickyNavLayout.startUpScroll();
            }
        });
        mStickyNavLayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                Log.d("MainActivity1", "getViewTreeObserver OnScrollChangedListener ");
                int scrollY = mStickyNavLayout.getMScrollY();
                Log.d("MainActivity1", "mStickyNavLayout.getRecyclerViewY():" + scrollY);
                if(scrollY < 20){
                    if(mHeaderAndFooterWrapper.getHeadersCount() == 0) {
                        mHeaderAndFooterWrapper.addHeaderView(R.layout.for_more_search_result_item);
                        mLoadMoreWrapper.notifyDataSetChanged();
                    }
                }else {
                    if(mHeaderAndFooterWrapper.getHeadersCount() > 0){
                        mHeaderAndFooterWrapper.removeAllHeaderView();
                        mLoadMoreWrapper.notifyDataSetChanged();
                    }
                }
            }
        });
    }



    private static class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder>{

        private List<String> mTasks ;

        public TasksAdapter(List<String> tasks){
            mTasks = tasks;
        }

        public void refreshList(List<String> tasks){
            mTasks = tasks;
            notifyDataSetChanged();
        }

        @Override
        public TasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_search_item, parent, false);
            // 实例化viewholder
            TasksAdapter.ViewHolder viewHolder = new TasksAdapter.ViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("TasksAdapter", "Item click");
                }
            });
            return viewHolder;        }

        @Override
        public void onBindViewHolder(TasksAdapter.ViewHolder holder, final int position) {
            holder.mButton.setText(mTasks.get(position));
        }

        @Override
        public int getItemCount() {
            return mTasks == null ? 0 : mTasks.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {


            Button mButton;

            public ViewHolder(View itemView) {
                super(itemView);
                mButton = (Button)itemView.findViewById(R.id.button_view);
            }
        }



    }

}