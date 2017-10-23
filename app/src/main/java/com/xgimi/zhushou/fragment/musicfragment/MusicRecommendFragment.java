package com.xgimi.zhushou.fragment.musicfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.fragment.BaseFragment;

/**
 * Created by Administrator on 2016/8/12.
 */
public class MusicRecommendFragment extends BaseFragment{
    public MusicRecommendFragment(){

    }
    public static AllFragment mvfragment;
    public static AllFragment getInstance(){
        if(mvfragment==null){
            mvfragment=new AllFragment();
        }
        return mvfragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_filter, container, false);
    }
}
