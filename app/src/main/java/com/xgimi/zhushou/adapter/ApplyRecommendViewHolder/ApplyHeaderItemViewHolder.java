package com.xgimi.zhushou.adapter.ApplyRecommendViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.widget.ViewFlow;

import java.util.ArrayList;

/**
 * Created by 霍长江 on 2016/8/17.
 */
public class ApplyHeaderItemViewHolder extends ApplyItemViewHolder{
    public ViewFlow viewFlow;
    public ArrayList<ImageView> indexs = new ArrayList<>();
    public final LinearLayout ll_dian;
    public ApplyHeaderItemViewHolder(View itemView) {
        super(itemView);
        viewFlow = (ViewFlow) itemView.findViewById(R.id.viewpager);
        ll_dian = (LinearLayout) itemView.findViewById(R.id.ll_dian);
        viewFlow.startAutoFlowTimer();
//        indexs.add((ImageView) itemView.findViewById(R.id.red));
//        indexs.add((ImageView) itemView.findViewById(R.id.grey));
//        indexs.add((ImageView) itemView.findViewById(R.id.grey_two));
//        viewFlow.setLiserner(new ViewFlow.postion() {
//
//            @Override
//            public void postion(int postin) {
//                // TODO Auto-generated method stub
//                for (int i = 0; i < indexs.size(); i++) {
//                    if(postin % 3==i){
//                        indexs.get(i).setImageResource(R.drawable.baidian);
//                    }else{
//                        indexs.get(i).setImageResource(R.drawable.hongdian);
//                    }
//                }
//            }
//        });
    }
}
