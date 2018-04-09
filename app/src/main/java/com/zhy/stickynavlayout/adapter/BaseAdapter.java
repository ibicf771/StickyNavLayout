package com.zhy.stickynavlayout.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhy.stickynavlayout.R;

import java.util.List;

/**
 * Created by yangsimin on 2018/4/9.
 */

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    private List<String> mTasks;

    public BaseAdapter(List<String> tasks) {
        mTasks = tasks;
    }

    public void refreshList(List<String> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public BaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_search_item, parent, false);
        // 实例化viewholder
        BaseAdapter.ViewHolder viewHolder = new BaseAdapter.ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TasksAdapter", "Item click");
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseAdapter.ViewHolder holder, final int position) {
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
            mButton = (Button) itemView.findViewById(R.id.button_view);
        }
    }
}

