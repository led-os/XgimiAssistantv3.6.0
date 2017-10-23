package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.AllChannel;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/9.
 */
public class AllChannelAdapter extends RecyclerView.Adapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<AllChannel.DataBean> mDatas;

    public AllChannelAdapter(Context context, List<AllChannel.DataBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public OnItemClicLisener mLisener;

    public void setOnitemLiener(OnItemClicLisener liener) {
        this.mLisener = liener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.live_left_channel, parent, false);
        return new ItemViewHolder(view);
    }

    private int mPostion = 0;

    public void dataChane(int postion) {
        if (postion != mPostion) {
            this.mPostion = postion;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mDatas != null) {
            ((ItemViewHolder) holder).tv.setText(mDatas.get(position).chinese_name);
        }
        if (position == mPostion) {
            ((ItemViewHolder) holder).ll.setBackgroundColor(mContext.getResources().getColor(R.color.baise));
            ((ItemViewHolder) holder).iv_movie.setVisibility(View.VISIBLE);
        } else {
            ((ItemViewHolder) holder).ll.setBackgroundColor(mContext.getResources().getColor(R.color.jiangese));
            ((ItemViewHolder) holder).iv_movie.setVisibility(View.GONE);
        }
        if (mLisener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLisener.OnItemLisener(position);
                    dataChane(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public void dataChange(List<AllChannel.DataBean> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {


        public TextView tv;
        public LinearLayout ll;
        public View iv_movie;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_channel);
            iv_movie = itemView.findViewById(R.id.iv_movie);
            ll = (LinearLayout) itemView.findViewById(R.id.ll);

        }
    }

    public interface OnItemClicLisener {
        void OnItemLisener(int i);
    }

}
