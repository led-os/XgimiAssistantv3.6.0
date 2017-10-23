package com.xgimi.zhushou.adapter.ApplyRecommendViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.xgimi.zhushou.R;

/**
 * Created by 霍长江 on 2016/8/17.
 */
public class ApplyFooterViewHolder extends RecyclerView.ViewHolder {

    public  LinearLayout ll;

    public ApplyFooterViewHolder(View itemView) {
        super(itemView);
        ll = (LinearLayout) itemView.findViewById(R.id.more);
    }
}
