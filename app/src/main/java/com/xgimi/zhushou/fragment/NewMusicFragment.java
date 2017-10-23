package com.xgimi.zhushou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.RecommendViewHolder.MyViewPagerAdapter1;
import com.xgimi.zhushou.fragment.musicfragment.NewBaiduMusicFragment;
import com.xgimi.zhushou.fragment.musicfragment.NewRadioFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linzh on 2017/10/23.
 */

public class NewMusicFragment extends BaseFragment {



    private static final String[] TITLES = {"音乐", "电台"};
    private View mView;
    private ViewPager mViewPager;
    private TabLayout mTab;
    private List<Fragment> mFragmentList;
    private MyViewPagerAdapter1 mAdapter;

    private static NewMusicFragment mInstance;

    public static NewMusicFragment getInstance() {
        if (mInstance == null) {
            synchronized (NewMusicFragment.class) {
                mInstance = new NewMusicFragment();
            }
        }
        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_new_music_layout, container, false);
            initView();
            initData();
        }
        return mView;
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new NewBaiduMusicFragment());
        mFragmentList.add(new NewRadioFragment());
        mAdapter = new MyViewPagerAdapter1(getFragmentManager(), TITLES, mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mTab.addTab(mTab.newTab().setText("音乐"));
        mTab.addTab(mTab.newTab().setText("电台"));
        mTab.setupWithViewPager(mViewPager);
    }

    private void initView() {
        mViewPager = (ViewPager) mView.findViewById(R.id.vp_new_music_fragment);
        mTab = (TabLayout) mView.findViewById(R.id.tab_new_music_fragment);
    }
}
