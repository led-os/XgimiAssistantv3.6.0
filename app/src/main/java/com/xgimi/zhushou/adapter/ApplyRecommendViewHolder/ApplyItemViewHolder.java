package com.xgimi.zhushou.adapter.ApplyRecommendViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;

/**
 * Created by 霍长江 on 2016/8/17.
 */
public class ApplyItemViewHolder extends RecyclerView.ViewHolder{
    public TextView name,name1,name2,name3;
    public ImageView image;
    public ImageView image1;
    public ImageView image2,image3;
    public View views;
    public ApplyItemViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }
    private void initView(View view){
        image = (ImageView) view.findViewById(R.id.image);
        image1 = (ImageView) view.findViewById(R.id.image1);
        image2 = (ImageView) view.findViewById(R.id.image2);
        image3 = (ImageView) view.findViewById(R.id.image3);
        name = (TextView) view.findViewById(R.id.name);
        name1 = (TextView) view.findViewById(R.id.name1);
        name2 = (TextView) view.findViewById(R.id.name2);
        name3 = (TextView) view.findViewById(R.id.name3);
        views=view.findViewById(R.id.view);

    }
}
