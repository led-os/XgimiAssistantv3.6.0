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
public class AppFooterViewHolder extends RecyclerView.ViewHolder{

    public LinearLayout ll;
    public  TextView tv_more;


    public  ImageView iv,iv1,iv2;
    public  TextView tv,tv1,tv2,tv_detail_one,tv_detail_two,tv_detail_three;
    public  LinearLayout ll1;
    public  LinearLayout ll_all,ll_all_one;
    public  View view;
    public  LinearLayout ll_one;
    public  LinearLayout ll_two;
    public  TextView tv_title;

    public AppFooterViewHolder(View itemView) {
        super(itemView);
        ll = (LinearLayout) itemView.findViewById(R.id.more);
        tv_more = (TextView) itemView.findViewById(R.id.tv_more);



        iv = (ImageView) itemView.findViewById(R.id.iv);
        iv1= (ImageView) itemView.findViewById(R.id.iv1);
        iv2= (ImageView) itemView.findViewById(R.id.iv2);
        ll1 = (LinearLayout) itemView.findViewById(R.id.ll);
        tv = (TextView) itemView.findViewById(R.id.tv);
        tv1 = (TextView) itemView.findViewById(R.id.tv1);
        tv2 = (TextView) itemView.findViewById(R.id.tv2);
        tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        tv_detail_one = (TextView) itemView.findViewById(R.id.tv_detail_one);
        tv_detail_two = (TextView) itemView.findViewById(R.id.tv_detail_two);
        tv_detail_three = (TextView) itemView.findViewById(R.id.tv_detail_three);

        ll_all = (LinearLayout) itemView.findViewById(R.id.ll_all);
        ll_all_one = (LinearLayout) itemView.findViewById(R.id.ll_all_one);

        view = itemView.findViewById(R.id.last_xian);


        ll_one = (LinearLayout) itemView.findViewById(R.id.ll_one);
        ll_two = (LinearLayout) itemView.findViewById(R.id.ll_two);



    }
}
