package com.xgimi.zhushou.fragment.radiodetailfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.util.SaveData;

/**
 * Created by Administrator on 2016/8/18.
 */
public class DetailFragment extends BaseFragment{
    private View view;
    private RatingBar ratingBar;
    private TextView summary;
    private TextView author;
//    private String fm_star,fm_description,fm_author;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_radio_detail,container,false);
        initView(view);
        return view;
    }
    public DetailFragment(){
    }
    private void initView(View view) {
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_one);
        author= (TextView) view.findViewById(R.id.radio_author);
        summary= (TextView) view.findViewById(R.id.summary);
        summary.setText(SaveData.getInstance().mDetail.data.get(0).fm_description);
        author.setText(SaveData.getInstance().mDetail.data.get(0).fm_author);
        ratingBar.setRating(((float) (Integer.valueOf(SaveData.getInstance().mDetail.data.get(0).fm_star) / 2f)));
    }


}
