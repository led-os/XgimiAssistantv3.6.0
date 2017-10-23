package com.xgimi.zhushou.adapter;

/**
 * Created by Administrator on 2017/1/6.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.Individuality;
import com.xgimi.zhushou.inter.OnitemClick;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;

import java.util.List;


public class IndividualityAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Individuality.DataBean.DataBean1> indivData;
    public LayoutInflater mlLayoutInflater;
    private static final int ITEM_VIEW=1;
    private static final int FOOT_VIEW=2;
    private boolean isHave;
    public IndividualityAdapter(Context context,List<Individuality.DataBean.DataBean1> indivData){
        this.context=context;
        this.indivData=indivData;
        mlLayoutInflater=LayoutInflater.from(context);
    }
    public void dataChange(List<Individuality.DataBean.DataBean1> indivData){
        this.indivData=indivData;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOT_VIEW;
        } else {
            return ITEM_VIEW;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_VIEW) {
            View view = mlLayoutInflater.inflate(R.layout.all_movie_item, parent, false);
            return new ItemViewHolder(view);
        }else if (viewType == FOOT_VIEW) {
            View view = mlLayoutInflater.inflate(R.layout.instance_load_more_layout, parent, false);
            return new AllMovieAdapter.FootViewHolder(view);
        }

        View view = mlLayoutInflater.inflate(R.layout.all_movie_item, parent, false);
        return new ItemViewHolder(view);
    }
    public OnitemClick mLisener;

    public void setLisener(OnitemClick lisener){
        this.mLisener=lisener;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        if(indivData!=null&&indivData.size()>0&&holder instanceof ItemViewHolder){
            if(indivData.size()>(position)*3){
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).iv, indivData.get((position)*3).image);
                ((ItemViewHolder) holder).tvName.setText(indivData.get((position)*3).title);
            }else {
                ((ItemViewHolder) holder).iv.setVisibility(View.GONE);
                ((ItemViewHolder) holder).tvName.setVisibility(View.GONE);
            }
            if(indivData.size()>(position)*3 + 1) {
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).iv1, indivData.get((position) * 3 + 1).image);
                ((ItemViewHolder) holder).tvName1.setText(indivData.get((position) * 3 + 1).title);
            }else {
                ((ItemViewHolder) holder).iv1.setVisibility(View.GONE);
                ((ItemViewHolder) holder).tvName1.setVisibility(View.GONE);
            }
            if(indivData.size()>(position)*3 + 2) {
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).iv2, indivData.get((position) * 3 + 2).image);
                ((ItemViewHolder) holder).tvName2.setText(indivData.get((position) * 3 + 2).title);
            }else {
                ((ItemViewHolder) holder).iv2.setVisibility(View.GONE);
                ((ItemViewHolder) holder).tvName2.setVisibility(View.GONE);
            }
            if(indivData.size()>(position)*3) {
                ((ItemViewHolder) holder).iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLisener.onClickLisener(indivData.get((position) * 3).title,indivData.get((position) * 3).id, null);
                    }
                });
            }
            if(indivData.size()>(position)*3 + 1) {
                ((ItemViewHolder) holder).iv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLisener.onClickLisener(indivData.get((position) * 3 + 1).title,indivData.get((position) * 3 + 1).id, null);
                    }
                });
            }
            if(indivData.size()>(position)*3 + 2) {
                ((ItemViewHolder) holder).iv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLisener.onClickLisener(indivData.get((position) * 3 + 2).title,indivData.get((position) * 3 + 2).id, null);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return indivData!=null&&indivData.size()>0&&indivData.size()%3==0?indivData.size()/3:indivData.size()/3+1;
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public final TextView tvName,tvName1,tvName2;
        public final ImageView iv,iv1,iv2;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            iv1 = (ImageView) itemView.findViewById(R.id.iv1);
            iv2 = (ImageView) itemView.findViewById(R.id.iv2);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvName1 = (TextView) itemView.findViewById(R.id.tv_name1);
            tvName2 = (TextView) itemView.findViewById(R.id.tv_name2);
        }
    }
}
