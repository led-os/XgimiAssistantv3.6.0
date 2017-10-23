package com.xgimi.zhushou.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ApplySearc;
import com.xgimi.zhushou.bean.MovieByCategory;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by XGIMI on 2017/9/15.
 */

public class MoreAppRcvAdapter extends RecyclerView.Adapter<MoreAppRcvAdapter.MoreAppViewHolder>{

    private List<ApplySearc.ApplySearchItem> mDataList;
    private LayoutInflater mInflater;
    private OnAppItemClickListener mOnAppItemClickListener;

    public MoreAppRcvAdapter(List<ApplySearc.ApplySearchItem> mDataList, LayoutInflater mInflater) {
        this.mDataList = mDataList;
        this.mInflater = mInflater;
    }

    @Override
    public MoreAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_more_app_layout, null);
        return new MoreAppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoreAppViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(mDataList.get(position).icon, holder.ivCover);
        holder.tvName.setText(mDataList.get(position).title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAppItemClickListener != null) {
                    mOnAppItemClickListener.onAppItemClick(mDataList.get(position));
                }
            }
        });
    }

    public void setOnAppItemClickListener(OnAppItemClickListener onAppItemClickListener) {
        this.mOnAppItemClickListener = onAppItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public static class MoreAppViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivCover;
        private TextView tvName;

        public MoreAppViewHolder(View itemView) {
            super(itemView);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_more_app_item_layout);
            tvName = (TextView) itemView.findViewById(R.id.tv_more_app_item_layout);
        }
    }

    public interface OnAppItemClickListener {
        void onAppItemClick(ApplySearc.ApplySearchItem item);
    }


}
