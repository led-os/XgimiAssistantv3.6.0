package com.xgimi.zhushou.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 霍长江 on 2016/8/11.
 */
public class MovieSortsAdapter extends RecyclerView.Adapter{
    private List<String> mDatas;
    private Context mContext;
    private LayoutInflater mlLayoutInflater;
    private int leixing;
    public HashMap<Integer,Boolean> mHasMap=new HashMap<>();
    public List<String> selsetDatasSorts=new ArrayList<>();
    public List<String> selsetDatasArea=new ArrayList<>();
    public List<String> selsetDatasYear=new ArrayList<>();

    public MovieSortsAdapter(List<String> data,Context context){
        this.mDatas=data;
        this.mContext=context;
        mlLayoutInflater=LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mlLayoutInflater.inflate(R.layout.movie_sort_class,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(mDatas!=null) {

            if(mHasMap.get(position)!=null) {
                if (mHasMap.get(position)) {
                    ((ItemViewHolder) holder).iv.setVisibility(View.VISIBLE);
                }
                else {
                    ((ItemViewHolder) holder).iv.setVisibility(View.GONE);                }
            }else{
                ((ItemViewHolder) holder).iv.setVisibility(View.GONE);
                mHasMap.put(position,false);
            }

            ((ItemViewHolder) holder).tvName.setText(mDatas.get(position));
            ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mHasMap.get(position)!=null) {
                        if (!mHasMap.get( position)) {
                            ((ItemViewHolder) holder).iv.setVisibility(View.VISIBLE);
//                            selsetDatasSorts.clear();
//                            selsetDatasArea.clear();
//                            selsetDatasYear.clear();
                            if(leixing==0){
                                selsetDatasSorts.add(mDatas.get(position));
                            }else if(leixing==1){
                                selsetDatasArea.add(mDatas.get(position));
                            }else if(leixing==2){
                                selsetDatasYear.add(mDatas.get(position));
                            }
                            mHasMap.put(position, true);
                        } else {
                            ((ItemViewHolder) holder).iv.setVisibility(View.GONE);
                            if(leixing==0){
                                selsetDatasSorts.remove(mDatas.get(position));
                            }else if(leixing==1){
                                selsetDatasArea.remove(mDatas.get(position));
                            }else if(leixing==2){
                                selsetDatasYear.remove(mDatas.get(position));
                            }
                            mHasMap.put(position, false);
                        }
                    }else{
                        ((ItemViewHolder) holder).iv.setVisibility(View.VISIBLE);
                        mHasMap.put(position, true);
                    }
                }
            });
        }
    }
    public void dataChange(List<String> data,int leixing){
        this.mDatas=data;
        this.leixing=leixing;
//        mHasMap.clear();
        notifyDataSetChanged();
    }


    public void dataChange(){
        mHasMap.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mDatas!=null?mDatas.size():0;
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public final TextView tvName;
        public final ImageView iv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tvName = (TextView) itemView.findViewById(R.id.tv);
        }
    }


    public interface onItemClicLisener{
        void onitemClicLisener();
    }
}
