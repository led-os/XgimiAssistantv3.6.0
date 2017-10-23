package com.xgimi.zhushou.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.RecommendViewHolder.MyViewPagerAdapter1;
import com.xgimi.zhushou.fragment.livefragment.LiveAllChannelFragment;
import com.xgimi.zhushou.fragment.livefragment.LiveFindFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LiveFragment extends BaseFragment implements ViewPager.OnPageChangeListener{

    @Bind(R.id.id_viewpager)
    ViewPager mViewpager;
    @Bind(R.id.id_tablayout)
    TabLayout mTabLayout;
    // TabLayout中的tab标题

    String[] mTitles=new String[]{"常用","推荐"};
    public boolean isInstalll;
    private List<Fragment> mFragments=new ArrayList<>();
    // ViewPager的数据适配器
    private MyViewPagerAdapter1 mViewPagerAdapter;
    public LiveFragment() {
        // Required empty public constructor
    }
    public static LiveFragment fragment;

    public static LiveFragment getInstance(){
        if(fragment==null){
            fragment=new LiveFragment();
        }
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_live, container, false);
        ButterKnife.bind(this, view);
        initFragment();
        initView();
        return view;
    }


    private void initFragment(){
        mFragments.add(new LiveFindFragment());
        mFragments.add(new LiveAllChannelFragment());
    }
    private void initView(){
        // 初始化ViewPager的适配器，并设置给它
        mViewPagerAdapter = new MyViewPagerAdapter1(getChildFragmentManager(), mTitles, mFragments);
        mViewpager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewpager.setOffscreenPageLimit(2);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewpager.addOnPageChangeListener(this);
        if(Build.VERSION.SDK_INT >= 21)
            mTabLayout.setElevation((float)0);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewpager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
        mViewpager.setCurrentItem(1);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("info","pnpageSecter0");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
