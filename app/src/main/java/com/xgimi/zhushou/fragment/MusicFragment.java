package com.xgimi.zhushou.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MyViewPagerAdapter;
import com.xgimi.zhushou.adapter.RecommendViewHolder.MyViewPagerAdapter1;
import com.xgimi.zhushou.fragment.musicfragment.MVNewFragment;
import com.xgimi.zhushou.fragment.musicfragment.MusicRadioFragment;
import com.xgimi.zhushou.fragment.musicfragment.NewRadioFragment;
import com.xgimi.zhushou.fragment.musicfragment.SongListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class MusicFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    private MVNewFragment mvNewFragment;
    private SongListFragment songListFragment;
    //    private MusicRadioFragment musicRadioFragment;
    private NewRadioFragment musicRadioFragment;
    TabLayout mTabLayout;
    private List<TextView> tvs = new ArrayList<>();
    private ViewPager mViewPager;
    private View view;
    private View main_film;
    private View xian_view;
    //    String[] mTitles=new String[]{"MV","歌单","电台"};
    String[] mTitles = new String[]{"歌单", "电台"};
    // 填充到ViewPager中的Fragment
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    // ViewPager的数据适配器
    private MyViewPagerAdapter mViewPagerAdapter;


    public MusicFragment() {
        // Required empty public constructor
    }

    public static MusicFragment fragment;

    public static MusicFragment getInstance() {
        if (fragment == null) {
            fragment = new MusicFragment();
        }
        return fragment;
    }

    private void initFragments() {
//        mvNewFragment = new MVNewFragment();
        songListFragment = new SongListFragment();
        musicRadioFragment = new NewRadioFragment();
//        fragmentList.add(mvNewFragment);
        fragmentList.add(songListFragment);
        fragmentList.add(musicRadioFragment);
//        fragmentList.add(MVNewFragment.getInstance());
//        fragmentList.add(SongListFragment.getInstance());
//        fragmentList.add(MusicRadioFragment.getInstance());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_music, container, false);
        ButterKnife.bind(this, view);
        initFragments();
        initView(view);
        return view;
    }

    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.main_film).findViewById(R.id.id_viewpager);
        MyViewPagerAdapter1 mViewPagerAdapter = new MyViewPagerAdapter1(getChildFragmentManager(), mTitles, fragmentList);
        mViewPager.setAdapter(mViewPagerAdapter);
        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.main_film).findViewById(R.id.id_tablayout);
        // 初始化ViewPager的适配器，并设置给它
//        mViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), mTitles, fragmentList);

        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(3);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);

        if (Build.VERSION.SDK_INT >= 21)
            mTabLayout.setElevation((float) 0);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        //设置他的高度为0
//        mTabLayout.setSelectedTabIndicatorHeight(0);
        //  mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
    }


    //    private void initData() {
//
////        showFragmentWithoutBackStackAndAnim(MVFragment.getInstance(),last_fragment);
////        last_fragment=MVFragment.getInstance();
//
//
//        mv.setTextColor(Color.parseColor("#2F9BFF"));
//        mv.setOnClickListener(this);
//        songlist.setOnClickListener(this);
//        radio.setOnClickListener(this);
//        mviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//        if(position==0){
//            SaveData.getInstance().searchMusic="mv";
//            mv.setTextColor(getActivity().getResources().getColor(R.color.color_bule));
//            radio.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//            songlist.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//        }else if(position==1){
//            songlist.setTextColor(getActivity().getResources().getColor(R.color.color_bule));
//            mv.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//            radio.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//        }else{
//            SaveData.getInstance().searchMusic="fm";
//            radio.setTextColor(getActivity().getResources().getColor(R.color.color_bule));
//            mv.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//            songlist.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//        }
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.mv:
//                mviewpager.setCurrentItem(0);
//                SaveData.getInstance().searchMusic="mv";
//                mv.setTextColor(getActivity().getResources().getColor(R.color.color_bule));
//                radio.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//                songlist.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//                break;
//            case R.id.song_list:
//                mviewpager.setCurrentItem(1);
//                songlist.setTextColor(getActivity().getResources().getColor(R.color.color_bule));
//                mv.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//                radio.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//                break;
//            case R.id.music_radio:
//                mviewpager.setCurrentItem(2);
//                radio.setTextColor(getActivity().getResources().getColor(R.color.color_bule));
//                mv.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//                songlist.setTextColor(getActivity().getResources().getColor(R.color.text_color_bule));
//                SaveData.getInstance().searchMusic="fm";
//                break;
            default:
                break;
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
