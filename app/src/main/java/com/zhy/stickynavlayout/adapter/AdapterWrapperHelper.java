package com.zhy.stickynavlayout.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.zhy.stickynavlayout.R;
import com.zhy.stickynavlayout.view.StickyNavLayout;

/**
 * Created by yangsimin on 2018/4/9.
 */

public class AdapterWrapperHelper{

    private LoadMoreWrapper mLoadMoreWrapper;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private StickyNavLayout mStickyNavLayout;
    private ILoadMoreCallback mILoadMoreCallback;

    public AdapterWrapperHelper(StickyNavLayout stickyNavLayout, RecyclerView.Adapter adapter){
        mStickyNavLayout = stickyNavLayout;
        initHeaderAndFooter(adapter);
        initLoadMore();
    }

    public void setLoadMoreCallback(ILoadMoreCallback iLoadMoreCallback){
        mILoadMoreCallback = iLoadMoreCallback;
    }

    public RecyclerView.Adapter getWrapperAdapter(){
        return mLoadMoreWrapper;
    }

    public void loadMoreResult(boolean hasMoreData) {
        if(hasMoreData){
            mLoadMoreWrapper.notifyDataSetChanged();
        }else {
            setNoMoreDatas();
        }
    }

    private void initLoadMore(){
        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);

        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener()
        {
            @Override
            public void onLoadMoreRequested()
            {
                mILoadMoreCallback.loadMoreCallback();
            }
        });
    }

    private void setNoMoreDatas(){
        mLoadMoreWrapper.removeLoadMoreView();
        mLoadMoreWrapper.notifyDataSetChanged();

        if(mHeaderAndFooterWrapper.getFootersCount() == 0){
            mHeaderAndFooterWrapper.addFootView(R.layout.default_no_more_load_data);
            mLoadMoreWrapper.notifyDataSetChanged();
        }
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

    public interface ILoadMoreCallback{
        void loadMoreCallback();
    }
}
