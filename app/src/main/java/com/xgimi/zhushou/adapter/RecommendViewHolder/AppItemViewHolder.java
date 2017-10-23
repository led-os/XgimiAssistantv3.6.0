package com.xgimi.zhushou.adapter.RecommendViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgimi.zhushou.R;

/**
 * Created by 霍长江 on 2016/8/8.
 */
public class AppItemViewHolder extends RecyclerView.ViewHolder{

    public  ImageView iv,iv1,iv2;
    public  TextView tv,tv1,tv2,tv_detail_one,tv_detail_two,tv_detail_three;
    public  LinearLayout ll;
    public  View xianview;

    public AppItemViewHolder(View itemView) {
        super(itemView);
        iv = (ImageView) itemView.findViewById(R.id.iv);
        iv1= (ImageView) itemView.findViewById(R.id.iv1);
        iv2= (ImageView) itemView.findViewById(R.id.iv2);
        ll = (LinearLayout) itemView.findViewById(R.id.ll);
        tv = (TextView) itemView.findViewById(R.id.tv);
        tv1 = (TextView) itemView.findViewById(R.id.tv1);
        tv2 = (TextView) itemView.findViewById(R.id.tv2);
        tv_detail_one = (TextView) itemView.findViewById(R.id.tv_detail_one);
        tv_detail_two = (TextView) itemView.findViewById(R.id.tv_detail_two);
        tv_detail_three = (TextView) itemView.findViewById(R.id.tv_detail_three);
        xianview = itemView.findViewById(R.id.xian_view);
    }
}
