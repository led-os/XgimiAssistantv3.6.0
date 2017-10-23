package com.xgimi.zhushou.adapter.RecommendViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.widget.ViewFlow;

import java.util.ArrayList;


public class AppCountItemFirstViewHolder extends AppItemViewHolder{
    public ViewFlow viewFlow;
    public ArrayList<ImageView> indexs = new ArrayList<>();
    public final LinearLayout ll_dian;
    public  LinearLayout ll_file;
    public  LinearLayout ll_phone;
    public  LinearLayout ll_tv;
    public  LinearLayout ll_video;
    public  LinearLayout ll_rukou;
    public  LinearLayout ll_image;

    public AppCountItemFirstViewHolder(View itemView) {
        super(itemView);
        viewFlow = (ViewFlow) itemView.findViewById(R.id.viewpager);
        ll_dian = (LinearLayout) itemView.findViewById(R.id.ll_dian);
        viewFlow.startAutoFlowTimer();

        ll_file = (LinearLayout) itemView.findViewById(R.id.ll_file);
        ll_phone = (LinearLayout) itemView.findViewById(R.id.ll_phone);
        ll_tv = (LinearLayout) itemView.findViewById(R.id.ll_tv);
        ll_video = (LinearLayout) itemView.findViewById(R.id.ll_video);
        ll_rukou = (LinearLayout) itemView.findViewById(R.id.lll_rukou);
        ll_image = (LinearLayout) itemView.findViewById(R.id.ll_image);
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