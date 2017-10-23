package com.xgimi.zhushou.adapter.MusicViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.AllRadioClassActivity;
import com.xgimi.zhushou.adapter.AllMovieAdapter;
import com.xgimi.zhushou.bean.HotRadio;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class HotRadioAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HotRadio.data> data;
    private LayoutInflater layoutInflater;

    public HotRadioAdapter(List<HotRadio.data> data, Context context) {
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void dataChange(List<HotRadio.data> data) {
        if (data != null) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    public OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = layoutInflater.inflate(R.layout.home_item, parent, false);
            return new AllMovieAdapter.FistHomeViewHolder(view);
        }
        if (viewType == 2) {
            View view = layoutInflater.inflate(R.layout.fragment_hot_radio, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == 3) {
            View view = layoutInflater.inflate(R.layout.instance_load_more_layout, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == 0 && holder instanceof AllMovieAdapter.FistHomeViewHolder) {
            ((AllMovieAdapter.FistHomeViewHolder) holder).tv_channel.setText("更多分类 >");
            ((AllMovieAdapter.FistHomeViewHolder) holder).tv_channel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToosUtil.getInstance().addEventUmeng(context, "event_music_radio_more_category");
                    Intent intent = new Intent(context, AllRadioClassActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        if (data != null) {
            if (holder instanceof ItemViewHolder) {
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).image1, data.get((position - 1) * 3).medium_thumb);
                ((ItemViewHolder) holder).tv1.setText(data.get((position - 1) * 3).fm_title);
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).image2, data.get((position - 1) * 3 + 1).medium_thumb);
                ((ItemViewHolder) holder).tv2.setText(data.get((position - 1) * 3 + 1).fm_title);
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).image3, data.get((position - 1) * 3 + 2).medium_thumb);
                ((ItemViewHolder) holder).tv3.setText(data.get((position - 1) * 3 + 2).fm_title);
            }
        }
        if (mOnClickListener != null && holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClick(data.get((position - 1) * 3), position);
                }
            });
            ((ItemViewHolder) holder).image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClick(data.get((position - 1) * 3 + 1), position);
                }
            });
            ((ItemViewHolder) holder).image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClick(data.get((position - 1) * 3 + 2), position);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        if (position + 1 == getItemCount()) {
            return 3;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() / 3 : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView image1, image2, image3;
        private TextView tv1, tv2, tv3;

        public ItemViewHolder(View itemView) {
            super(itemView);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
            tv1 = (TextView) itemView.findViewById(R.id.text1);
            tv2 = (TextView) itemView.findViewById(R.id.text2);
            tv3 = (TextView) itemView.findViewById(R.id.text3);
        }
    }

    public interface OnClickListener {
        void onClick(Object object, int position);
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;

        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv = (TextView) view.findViewById(R.id.foot_view_item_tv);
        }
    }
}
