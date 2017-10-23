package com.xgimi.zhushou.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;

/**
 * Created by Administrator on 2016/12/13.
 */
public class NewMVItemViewHolder extends RecyclerView.ViewHolder{
    public ImageView iv,iv1;
    public TextView tv1,tv3;
    public TextView tv2,tv4;
    public NewMVItemViewHolder(View itemView) {
        super(itemView);
        iv= (ImageView) itemView.findViewById(R.id.mv_image);
        iv1 =(ImageView) itemView.findViewById(R.id.mv_image1);
        tv1= (TextView) itemView.findViewById(R.id.mv_title);
        tv2= (TextView) itemView.findViewById(R.id.mv_artist);
        tv3= (TextView) itemView.findViewById(R.id.mv_title1);
        tv4= (TextView) itemView.findViewById(R.id.mv_artist1);
    }
}
