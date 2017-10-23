package com.xgimi.zhushou.adapter.RecommendViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xgimi.zhushou.R;

/**
 * Created by 霍长江 on 2016/8/8.
 */
public class AppHeaderViewHolder extends RecyclerView.ViewHolder{
    public TextView tv;
    public AppHeaderViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.tv_movie);
    }
}
