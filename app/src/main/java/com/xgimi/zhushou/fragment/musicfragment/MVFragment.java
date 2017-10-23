package com.xgimi.zhushou.fragment.musicfragment;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MyViewPagerAdapter;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.SaveData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by Administrator on 2016/8/16.
 */
public class MVFragment extends BaseFragment implements ViewPager.OnPageChangeListener{
    @Bind(R.id.id_viewpager)
    ViewPager mViewpager;
    @Bind(R.id.id_tablayout)
    TabLayout mTabLayout;
    @Bind(R.id.view)
            View view_xian;
    String[] mTitle=new String[]{"最新","推荐","内地","港台","欧美","韩语","日语","二次元","其他"};
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments=new ArrayList<>();
    // ViewPager的数据适配器
    private MyViewPagerAdapter mViewPagerAdapter;
    private View view;
    private View main_mv;
    private View close;
    public MVFragment() {
        // Required empty public constructor
    }
    public static MVFragment fragment;
    public static MVFragment getInstance(){
        if(fragment==null){
            fragment=new MVFragment();
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_mv, container, false);
        ButterKnife.bind(this, view);
        initFragments();
        initView(view);
        return view;
    }

    private void initView(View view) {
        close=view.findViewById(R.id.close);
        main_mv=view.findViewById(R.id.main_mv);
        view_xian.setVisibility(View.GONE);
        // 初始化ViewPager的适配器，并设置给它
        mViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), mTitle, mFragments);
        mViewpager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewpager.setOffscreenPageLimit(9);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewpager.addOnPageChangeListener(this);
        mViewpager.setCurrentItem(1);
        if(Build.VERSION.SDK_INT >= 21)
            mTabLayout.setElevation((float)0);
        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewpager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
        mViewpager.setCurrentItem(1);
        if(HttpUrl.isNetworkConnected(getActivity())){
            if(SaveData.getInstance().mv==0){
                main_mv.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
            }else {
                main_mv.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);
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
    //加载fragment
    private void initFragments() {
        mFragments.add(new AllFragment());
        mFragments.add(new MVRecommendFragment());
        mFragments.add(new InlandFragment());
        mFragments.add(new HongKongFragment());
        mFragments.add(new EuropeFragment());
        mFragments.add(new KoreaFragmen());
        mFragments.add(new JapaneseFragment());
        mFragments.add(new ErCiYuanFragment());
        mFragments.add(new OtherFragment());
    }
}
