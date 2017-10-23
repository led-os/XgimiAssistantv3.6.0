package com.xgimi.zhushou.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.HotRadio;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.fragment.musicfragment.IRadioFragmentView;

import java.util.List;

/**
 * Created by XGIMI on 2017/9/13.
 */

public class RadioRcvAdapter extends RecyclerView.Adapter<RadioRcvAdapter.RadioViewHolder>{

    private IRadioFragmentView mView;
    private List<HotRadio.data> mDatalList;

    public RadioRcvAdapter(IRadioFragmentView mView, List<HotRadio.data> mDatalList) {
        this.mView = mView;
        this.mDatalList = mDatalList;
    }

    @Override
    public RadioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(((BaseFragment)mView).getContext()).inflate(R.layout.item_radio_fragment, null);
        return new RadioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RadioViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(mDatalList.get(position).medium_thumb, holder.ivCover);
        holder.tvName.setText(mDatalList.get(position).fm_title);
        holder.itemView.setOnClickListener(new OnItemClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mDatalList.size();
    }

    public static class RadioViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCover;
        private TextView tvName;
        public RadioViewHolder(View itemView) {
            super(itemView);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_radio_item_cover);
            tvName = (TextView) itemView.findViewById(R.id.tv_radio_item_name);
        }
    }

    private class OnItemClickListener implements View.OnClickListener {

        private int position;

        public OnItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            mView.onRadioItemClick(mDatalList.get(position));
        }
    }
}
