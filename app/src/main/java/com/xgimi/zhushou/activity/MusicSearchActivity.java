package com.xgimi.zhushou.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.FragmentAdapter;
import com.xgimi.zhushou.adapter.MusicHistoryAdapter;
import com.xgimi.zhushou.adapter.SearchNameAdapter;
import com.xgimi.zhushou.bean.SearchBean;
import com.xgimi.zhushou.db.MusicHistoryDao;
import com.xgimi.zhushou.fragment.SearchMusicFragment.SearchMVFragment;
import com.xgimi.zhushou.fragment.SearchMusicFragment.SearchRadioFragment;
import com.xgimi.zhushou.fragment.SearchMusicFragment.SearchSongListFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.NetAbout;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.Statics;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.MyEditText;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/13.
 */
public class MusicSearchActivity extends BaseActivity {

    private static final String TAG = "MusicSearchActivity";
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private MyEditText mEditText;
    private int mIndex = 0;
    private ImageView indicator;
    private TextView mCancel;
    private LinearLayout liner;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private LinearLayout title;
    private ListView listview;
    public Subscription subscription;
    private SearchNameAdapter searchAdapter;
    SearchBean searchNames = new SearchBean();
    private List<TextView> tvs = new ArrayList<>();

    private MusicHistoryDao mMusicHistoryDao;
    private LinearLayout mMusicHistoryLL;
    private TextView mCleanMusicHistoryTv;
    private ListView mMusicHistoryLv;
    private List<String> mMusicHistoryList = new ArrayList<>();
    private MusicHistoryAdapter mMusicHistoryAdapter;


    private View.OnClickListener mOnCleanMusicRecordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMusicHistoryList.clear();
            mMusicHistoryDao.cleanAllRecord();
            mMusicHistoryLv.setVisibility(View.VISIBLE);
            mMusicHistoryLv.setVisibility(View.VISIBLE);
            mMusicHistoryAdapter.notifyDataSetChanged();
            hideMusicHistoryLayout();
        }
    };

    private AdapterView.OnItemClickListener mOnMusicHistoryItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            XGIMILOG.D("onItemClick: " + mMusicHistoryList.get(position));
            mNeedSearchHotWord = false;
            mEditText.setText(mMusicHistoryList.get(position));
            searchMusic(mMusicHistoryList.get(position));
//            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MusicSearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };

    private void searchMusic(String key) {
        Log.d(TAG, "searchMusic: " + key);
        mNeedSearchHotWord = true;
        hideMusicHistoryLayout();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MusicSearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Statics.getInstance().sendSearchStatics(MusicSearchActivity.this, key, key, "音乐");
        title.setVisibility(View.VISIBLE);
        indicator.setVisibility(View.VISIBLE);
        radioFragment.getSearchRadio(key, 1);
        songListFragment.search(null, null, null, key);
        liner.setVisibility(View.VISIBLE);
        listview.setVisibility(View.GONE);
        if (!key.equals("")) {
            Log.d(TAG, "searchMusic: insert record");
            mMusicHistoryDao.insertMusicSearchHistory(key);
            if (!mMusicHistoryList.contains(key)) {
                mMusicHistoryList.add(0, key);
            }
//            for (int i = 0; i < mMusicHistoryList.size(); i++) {
//                XGIMILOG.D(mMusicHistoryList.get(i));
//            }
        }
    }

    public void hideMusicHistoryLayout() {
        if (mMusicHistoryLL.getVisibility() == View.VISIBLE) {
            Log.d(TAG, "hideMusicHistoryLayout: ");
            mMusicHistoryLL.setVisibility(View.GONE);
        }
    }

    private void showMusicHistoryLayout() {
//        if (mMusicHistoryList.isEmpty()) {
//            Log.d(TAG, "showMusicHistoryLayout: mMusicHistoryList.isEmpty()");
//            return;
//        }
//        mMusicHistoryAdapter.notifyDataSetChanged();
//        mMusicHistoryLL.setVisibility(View.VISIBLE);

        if (mMusicHistoryAdapter != null) {
            XGIMILOG.D("");
//            mMusicHistoryList.clear();
//            mMusicHistoryList.addAll(mMusicHistoryDao.getMusicSearchRecordList());
//            mMusicHistoryList = mMusicHistoryDao.getMusicSearchRecordList();
            mMusicHistoryLL.setVisibility(View.VISIBLE);
            mMusicHistoryLv.setVisibility(View.VISIBLE);
            mMusicHistoryAdapter.notifyDataSetChanged();
        } else {
            initMusicHistory();
        }

//        initMusicHistory();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_search);
        initView();
        initMusicHistory();
        initData();
        Log.d(TAG, "onCreate: ");
    }


    private void initMusicHistory() {
        if (mMusicHistoryDao == null) {
            mMusicHistoryDao = new MusicHistoryDao(this);
        }
//        mMusicHistoryList = mMusicHistoryDao.getMusicSearchRecordList();
        List<String> recodList = mMusicHistoryDao.getMusicSearchRecordList();
        for (int i = recodList.size() - 1; i >= 0; i--) {
            if (mMusicHistoryList == null) {
                mMusicHistoryList = new ArrayList<>();
            }
            mMusicHistoryList.add(recodList.get(i));
        }
        XGIMILOG.D("initMusicHistory: history size = " + mMusicHistoryList.size());
        if (mMusicHistoryList.isEmpty()) {
            XGIMILOG.D("initMusicHistory: ");
            mMusicHistoryLL.setVisibility(View.GONE);
            return;
        }
        if (mMusicHistoryAdapter == null) {
            mMusicHistoryAdapter = new MusicHistoryAdapter(mMusicHistoryList, mMusicHistoryDao, this);
        }
        mMusicHistoryLv.setAdapter(mMusicHistoryAdapter);
        mMusicHistoryLv.setOnItemClickListener(mOnMusicHistoryItemClickListener);
        mCleanMusicHistoryTv.setOnClickListener(mOnCleanMusicRecordListener);
        mMusicHistoryLL.setVisibility(View.VISIBLE);
    }

    private void initView() {
        mMusicHistoryLL = (LinearLayout) findViewById(R.id.ll_music_history);
        mCleanMusicHistoryTv = (TextView) findViewById(R.id.tv_clean_music_search_record);
        mMusicHistoryLv = (ListView) findViewById(R.id.lv_music_history);

        title = (LinearLayout) findViewById(R.id.title);
        liner = (LinearLayout) findViewById(R.id.ll_jieguo);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        initFragment();
        FragmentAdapter mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(1);
        mEditText = (MyEditText) findViewById(R.id.edit_search);
        mEditText.setSelection(mEditText.getText().length());
        indicator = (ImageView) findViewById(R.id.indicator);
        android.view.ViewGroup.LayoutParams layoutParams = indicator.getLayoutParams();
        layoutParams.width = NetAbout.getInstance().getScreenWidth(this) / 2;
        mCancel = (TextView) findViewById(R.id.tv);
        listview = (ListView) findViewById(R.id.pop_listview);
        listview.setVisibility(View.GONE);
        searchAdapter = new SearchNameAdapter(this, searchNames);
        listview.setAdapter(searchAdapter);
//        tv1 = (TextView) findViewById(R.id.mv);
        tv2 = (TextView) findViewById(R.id.gedan);
        tv3 = (TextView) findViewById(R.id.radio);
//        tv1.setTextColor(Color.parseColor("#2F9BFF"));
//        tvs.add(tv1);
        tvs.add(tv2);
        tvs.add(tv3);
        CollectOnclickListener listener = new CollectOnclickListener();
//        tv1.setOnClickListener(listener);
        tv2.setOnClickListener(listener);
        tv3.setOnClickListener(listener);
    }

    private boolean mNeedSearchHotWord;

    private void initData() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ObjectAnimator.ofFloat(indicator, "x", NetAbout.getInstance().getScreenWidth(MusicSearchActivity.this) / 2 * (position + positionOffset)).setDuration(0).start();
//                ObjectAnimator.ofFloat(indicator, "x", NetAbout.getInstance().getScreenWidth(MusicSearchActivity.this) / 2 * (position + positionOffset) + NetAbout.getInstance().getScreenWidth(MusicSearchActivity.this) / 10).setDuration(0).start();
            }

            @Override
            public void onPageSelected(int position) {
                chanColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //软键盘影藏
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        Log.d(TAG, "onKey: keyCode == KeyEvent.KEYCODE_ENTER");
                        /* 隐藏软键盘 */
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isActive()) {
                            inputMethodManager.hideSoftInputFromWindow(
                                    v.getApplicationWindowToken(), 0);
                        }
                        if (mEditText.getText().toString().length() != 0) {
                            liner.setVisibility(View.VISIBLE);
                            title.setVisibility(View.VISIBLE);
                            indicator.setVisibility(View.VISIBLE);
                        } else {
                            title.setVisibility(View.GONE);
                            indicator.setVisibility(View.GONE);
                            Toast.makeText(MusicSearchActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                        }
                        searchMusic(mEditText.getText().toString().trim());
                        return true;
                    }

                }
                return false;
            }
        });



        //输入框内容改变监听
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEditText.getText().toString().length() == 0) {
                    liner.setVisibility(View.GONE);
                    listview.setVisibility(View.GONE);
                    showMusicHistoryLayout();
                } else {
                    if (mNeedSearchHotWord) {
                        guanJianZi(mEditText.getText().toString(), 20, SaveData.getInstance().searchMusic);
                    }
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(
                            v.getApplicationWindowToken(), 0);
                }
                finish();
//                mEditText.setText("");
            }
        });
        //关键字搜索跳转
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XGIMILOG.D(searchAdapter.getItem(position));
                Statics.getInstance().sendSearchStatics(MusicSearchActivity.this, searchAdapter.getItem(position), mEditText.getText().toString().trim(), "音乐");
                if (SaveData.getInstance().searchMusic.equals("mv")) {
                    XGIMILOG.D("");
                    hideMusicHistoryLayout();
                    viewPager.setCurrentItem(0);
                    liner.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);
                    indicator.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
//                    mEditText.setText(searchAdapter.getItem(position));
                    searchMusic(searchAdapter.getItem(position));
//                    mvFragment.getSearchMV(searchAdapter.getItem(position), 1);
                    //搜索优化
//                    radioFragment.getSearchRadio(searchAdapter.getItem(position), 1);
//                    songListFragment.search(null, null, null, searchAdapter.getItem(position));
//
//                    mMusicHistoryDao.insertMusicSearchHistory(searchAdapter.getItem(position));


//                    mEditText.setText(searchAdapter.getItem(position));
                } else if (SaveData.getInstance().searchMusic.equals("fm")) {
                    XGIMILOG.D("");
                    hideMusicHistoryLayout();
                    liner.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);
                    indicator.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                    viewPager.setCurrentItem(1);
                    searchMusic(searchAdapter.getItem(position));
//                    mvFragment.getSearchMV(mEditText.getText().toString().trim(), 1);
//                    radioFragment.getSearchRadio(searchAdapter.getItem(position), 1);
//                    songListFragment.search(null, null, null, searchAdapter.getItem(position));

                    mMusicHistoryDao.insertMusicSearchHistory(searchAdapter.getItem(position));
//                    mEditText.setText(searchAdapter.getItem(position));
                }
            }
        });
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

    private class CollectOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.mv:
//                    viewPager.setCurrentItem(0);
//                    break;
                case R.id.gedan:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.radio:
                    viewPager.setCurrentItem(1);
                    break;
            }
        }
    }

    //    private SearchMVFragment mvFragment;
    private SearchSongListFragment songListFragment;
    private SearchRadioFragment radioFragment;

    private void initFragment() {
//        mvFragment = new SearchMVFragment();
        songListFragment = new SearchSongListFragment();
        radioFragment = new SearchRadioFragment();
//        fragmentList.add(mvFragment);
        fragmentList.add(songListFragment);
        fragmentList.add(radioFragment);
    }

    private void guanJianZi(String key, int num, String type) {
//        subscription = Api.getMangoApi().getFilmKeyWord(key, num, type)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer2);
    }

    Observer<SearchBean> observer2 = new Observer<SearchBean>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(SearchBean searchBean) {
            if (searchBean != null && searchBean.data != null) {
                hideMusicHistoryLayout();
                searchAdapter.dataChange(searchBean);
                listview.setVisibility(View.VISIBLE);
                liner.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
