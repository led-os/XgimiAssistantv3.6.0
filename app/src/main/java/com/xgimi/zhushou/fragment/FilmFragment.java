package com.xgimi.zhushou.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.xgimi.zhushou.MainActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MyViewPagerAdapter;
import com.xgimi.zhushou.bean.CkeckData;
import com.xgimi.zhushou.bean.SelecterBean;
import com.xgimi.zhushou.fragment.filmfragment.ComicFragment;
import com.xgimi.zhushou.fragment.filmfragment.MovieFragment;
import com.xgimi.zhushou.fragment.filmfragment.RecommendFragment;
import com.xgimi.zhushou.fragment.filmfragment.RecordFragment;
import com.xgimi.zhushou.fragment.filmfragment.TVPlayFragment;
import com.xgimi.zhushou.fragment.filmfragment.VarietyFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;


public class FilmFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    // TODO: Rename parameter arguments, choose names that match


    @Bind(R.id.id_viewpager)
    ViewPager mViewpager;
    @Bind(R.id.id_tablayout)
    TabLayout mTabLayout;
    // TabLayout中的tab标题
    private String[] mTitles;
    // 填充到ViewPager中的Fragment
    private List<Fragment> mFragments = new ArrayList<>();
    // ViewPager的数据适配器
    private MyViewPagerAdapter mViewPagerAdapter;
    private View main_film;
    private View close;
    private Subscription subscription;
    private View xian_view;
    //是否有个性推荐
    private boolean isShow;

    public FilmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static FilmFragment fragment;

    public static FilmFragment getInstance() {
        if (fragment == null) {
            fragment = new FilmFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_film, container, false);
        ButterKnife.bind(this, view);
        initExtra();
        checkData();
        initFragments();
        initView(view);
        return view;
    }

    private void initExtra() {

        if (SaveData.getInstance().saveIndivi != null && SaveData.getInstance().saveIndivi.data != null &&
                SaveData.getInstance().saveIndivi.data.data != null && SaveData.getInstance().saveIndivi.data.data.size() > 0) {
            isShow = true;
        } else {
            isShow = false;
        }
    }


    private void initView(View view) {
        EventBus.getDefault().register(this);
        close = view.findViewById(R.id.close);
        main_film = view.findViewById(R.id.main_film);
        xian_view = view.findViewById(R.id.view);
        xian_view.setVisibility(View.GONE);
        // 初始化ViewPager的适配器，并设置给它
//        if(!isShow){
        mTitles = new String[]{"精选", "电影", "电视剧", "综艺", "动漫", "纪录片"};
//        }else {
//          mTitles=new String[]{"精选",SaveData.getInstance().saveIndivi.data.title,"电影","电视剧","综艺","动漫","纪录片"};
//        }
        mViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), mTitles, mFragments);
        mViewpager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewpager.setOffscreenPageLimit(mTitles.length);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewpager.addOnPageChangeListener(this);

        if (Build.VERSION.SDK_INT >= 21)
            mTabLayout.setElevation((float) 0);
        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewpager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
    }


    private void initFragments() {
//    mFragments.clear();
//    if(isShow){
//        mFragments.add(new RecommendFragment());
//        mFragments.add(new IndividualityFragement());
//        mFragments.add(new MovieFragment());
//        mFragments.add(new TVPlayFragment());
//        mFragments.add(new VarietyFragment());
//        mFragments.add(new ComicFragment());
//        mFragments.add(new RecordFragment());
//    }else {
        mFragments.add(new RecommendFragment());
        mFragments.add(new MovieFragment());
        mFragments.add(new TVPlayFragment());
        mFragments.add(new VarietyFragment());
        mFragments.add(new ComicFragment());
        mFragments.add(new RecordFragment());
//    }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            xian_view.setVisibility(View.GONE);
        } else {
            xian_view.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void onEventMainThread(SelecterBean postion) {
        mViewpager.setCurrentItem(postion.getPostion());
    }


    //    public void onEventMainThread(GMDevice gmDevice) {
//        isShow=true;
//        initFragments();
//        String title=SaveData.getInstance().saveIndivi.data.title;
//        mTitles=new String[]{"精选",SaveData.getInstance().saveIndivi.data.title,"电影","电视剧","综艺","动漫","纪录片"};
//        mTitles=new String[]{"精选",SaveData.getInstance().saveIndivi.data.title,"电影","电视剧","综艺","动漫","纪录片"};
//        mViewPagerAdapter.dataChange(mTitles,mFragments);
//        mViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), mTitles, mFragments);
//        mViewpager.setAdapter(mViewPagerAdapter);
//        // 设置ViewPager最大缓存的页面个数
//        mViewpager.setOffscreenPageLimit(mTitles.length);
//        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
//        mViewpager.addOnPageChangeListener(this);
//
//        if(Build.VERSION.SDK_INT >= 21)
//            mTabLayout.setElevation((float)0);
//        mTabLayout.setTabMode(MODE_SCROLLABLE);
//        // 将TabLayout和ViewPager进行关联，让两者联动起来
//        mTabLayout.setupWithViewPager(mViewpager);
//        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
//        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //检测贫道是否关闭
    public void checkData() {
        XGIMILOG.E("开始获取频道开关");
        if (app.getChannelData() != null && app.getChannelData().data != null) {
            XGIMILOG.E("获取频道开关缓存成功 : " + new Gson().toJson(app.getChannelData()));
            CkeckData ckeckData = app.getChannelData();
            SaveData.getInstance().gameLive = ckeckData.data.gameLive;
            SaveData.getInstance().channel = ckeckData.data.channel;
            SaveData.getInstance().video = ckeckData.data.video;
            SaveData.getInstance().mv = ckeckData.data.mv;
            if (ckeckData.data.video == 0) {
                close.setVisibility(View.VISIBLE);
                main_film.setVisibility(View.GONE);
            }
            if (SaveData.getInstance().channel == 1) {
                ((MainActivity)getContext()).showLiveFragment();
            } else {
                ((MainActivity)getContext()).hideLiveFragment();
            }
        }
        if (NetUtil.isNetConnected(getContext())) {
            subscription = Api.getMangoApi().checkData().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {

        }

    }

    Observer<CkeckData> observer = new Observer<CkeckData>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
        }

        @Override
        public void onError(Throwable e) {
            XGIMILOG.E("获取频道开关失败 : " + e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onNext(CkeckData ckeckData) {
            XGIMILOG.E("获取频道开关成功");
            XGIMILOG.D("ckeckData = " + new Gson().toJson(ckeckData));
            if (ckeckData != null && ckeckData.data != null) {
                app.saveChannelData(new Gson().toJson(ckeckData));
                SaveData.getInstance().gameLive = ckeckData.data.gameLive;
                SaveData.getInstance().channel = ckeckData.data.channel;
                SaveData.getInstance().video = ckeckData.data.video;
                SaveData.getInstance().mv = ckeckData.data.mv;
                if (ckeckData.data.video == 0) {
                    close.setVisibility(View.VISIBLE);
                    main_film.setVisibility(View.GONE);
                }
                if (SaveData.getInstance().channel == 1) {
                    ((MainActivity)getContext()).showLiveFragment();
                } else {
                    ((MainActivity)getContext()).hideLiveFragment();
                }
            }
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }

}
