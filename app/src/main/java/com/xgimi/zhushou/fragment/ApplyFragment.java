package com.xgimi.zhushou.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.RecommendViewHolder.MyViewPagerAdapter1;
import com.xgimi.zhushou.bean.ApplyIndexs;
import com.xgimi.zhushou.fragment.applyfragment.ApplyGameFragment;
import com.xgimi.zhushou.fragment.applyfragment.ApplyRecomendFragment;
import com.xgimi.zhushou.fragment.applyfragment.ApplyYingYongragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


public class ApplyFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private ViewPager mViewPager;
    private ApplyYingYongragment applyYingYongragment;
    private ApplyGameFragment applyGameFragment;
    private ApplyRecomendFragment applyRecomendFragment;
    private ImageView indicator;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private List<TextView> tvs = new ArrayList<>();
    String[] mTitles = new String[]{"全部", "应用", "游戏"};

    public ApplyFragment() {
        // Required empty public constructor
    }

    public static ApplyFragment fragment;

    public static ApplyFragment getInstance() {
        if (fragment == null) {
            fragment = new ApplyFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_app, container, false);
        ButterKnife.bind(this, view);
        initFragments();
        initView(view);
        return view;
    }


    private void initFragments() {
        EventBus.getDefault().register(this);
        applyRecomendFragment = new ApplyRecomendFragment();
        applyYingYongragment = new ApplyYingYongragment();
        applyGameFragment = new ApplyGameFragment();
        fragmentList.add(applyRecomendFragment);
        fragmentList.add(applyYingYongragment);
        fragmentList.add(applyGameFragment);
    }

    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        MyViewPagerAdapter1 mViewPagerAdapter = new MyViewPagerAdapter1(getChildFragmentManager(), mTitles, fragmentList);
        mViewPager.setAdapter(mViewPagerAdapter);
        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.id_tablayout);

        mViewPager.setOffscreenPageLimit(3);

        mViewPager.addOnPageChangeListener(this);
        if (Build.VERSION.SDK_INT >= 21)
            mTabLayout.setElevation((float) 0);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
    }

    public void onEventMainThread(ApplyIndexs postion) {
        mViewPager.setCurrentItem(postion.getIndexs());
    }

    public void onEventMainThread(FeedbackInfo info) {
        if (applyYingYongragment != null && applyGameFragment != null) {
            if (applyYingYongragment.mAdapter != null && applyGameFragment.mAdapter != null) {
                applyYingYongragment.mAdapter.setText(info);
                applyGameFragment.mAdapter.setText(info);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void chanColor(int postion) {
        for (int i = 0; i < tvs.size(); i++) {
            TextView tv = tvs.get(i);
            if (i == postion) {
                tv.setTextColor(Color.parseColor("#2F9BFF"));
            } else {
                tv.setTextColor(Color.parseColor("#000000"));

            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
