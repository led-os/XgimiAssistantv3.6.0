package com.xgimi.zhushou.adapter.RecommendViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.AllChannel;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 霍长江 on 2016/8/9.
 */
public class AllChannelDetailAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<AllChannel.DataBean.ChannelsBean> mdata;
    private int mPostion = -1;
    private HashMap<Integer, Integer> mMap;

    public AllChannelDetailAdapter(Context context, List<AllChannel.DataBean.ChannelsBean> data, HashMap<Integer, Integer> hasMaps) {
        this.mContext = context;
        this.mdata = data;
        this.mMap = hasMaps;
        mLayoutInflater = LayoutInflater.from(context);
    }

    onItemClickLisener mLisener;

    public void setLisener(onItemClickLisener lisener) {
        this.mLisener = lisener;

    }

    public void changePosition(int postion) {
        this.mPostion = postion;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.live_right_channel, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mdata != null) {
//            ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).iv, mdata.get(position).icon);
            Glide.with(mContext).load(mdata.get(position).icon).into(((ItemViewHolder) holder).iv);
            ((ItemViewHolder) holder).tv_name.setText(mdata.get(position).name);
            ((ItemViewHolder) holder).itemView.setOnFocusChangeListener(new MyFocusChangeListener(position));
            if (position == mPostion) {
                ((ItemViewHolder) holder).tv_name.setTextColor(mContext.getResources().getColor(R.color.color_bule));
            } else {
                ((ItemViewHolder) holder).tv_name.setTextColor(mContext.getResources().getColor(R.color.text_color_bule));
            }
        }
        if (mLisener != null) {
            ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLisener.onItemClickLisener(mdata.get(position), position);
                }
            });
        }
    }

    public void dataChange(List<AllChannel.DataBean.ChannelsBean> data) {
        this.mdata = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mdata != null ? mdata.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv;
        public TextView tv_name;
        public TextView tv_smallTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_name = (TextView) itemView.findViewById(R.id.tv_channel);
            tv_smallTitle = (TextView) itemView.findViewById(R.id.tv_smalltitle);
        }
    }

    public interface onItemClickLisener {
        void onItemClickLisener(AllChannel.DataBean.ChannelsBean object, int postion);
    }

    public class MyFocusChangeListener implements View.OnFocusChangeListener {

        private int postion;

        public MyFocusChangeListener(int postion) {
            this.postion = postion;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                XGIMILOG.E(postion + "");
            }
        }
    }
}
