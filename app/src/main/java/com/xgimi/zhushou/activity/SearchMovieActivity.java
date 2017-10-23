package com.xgimi.zhushou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.aes.MyAesToGsonFactory;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MyViewPagerAdapter;
import com.xgimi.zhushou.adapter.RecordAdatper;
import com.xgimi.zhushou.adapter.SearchNameAdapter;
import com.xgimi.zhushou.bean.SearchBean;
import com.xgimi.zhushou.bean.SearchLiveRecord;
import com.xgimi.zhushou.bean.SearchMovieResult;
import com.xgimi.zhushou.db.RecordDao;
import com.xgimi.zhushou.fragment.searchmoviefragment.SearchAllFragment;
import com.xgimi.zhushou.fragment.searchmoviefragment.SearchDongManFragment;
import com.xgimi.zhushou.fragment.searchmoviefragment.SearchJiLuPianFragment;
import com.xgimi.zhushou.fragment.searchmoviefragment.SearchMovieFragment;
import com.xgimi.zhushou.fragment.searchmoviefragment.SearchTVFragment;
import com.xgimi.zhushou.fragment.searchmoviefragment.SearchZongYiFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.Statics;
import com.xgimi.zhushou.util.ToastUtil;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.MyEditText;
import com.xgimi.zhushou.widget.MyListview;

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
import static android.view.View.X;

public class SearchMovieActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = "SearchMovieActivity";
    public static final String SEARCH_EXTRA_KEY = "search_text";
    @Bind(R.id.search)
    MyEditText mEdittextSearch;
    @Bind(R.id.hortory_listview)
    MyListview mHoslistView;
    //    @Bind(R.id.detail_recylerview)
//    RecyclerView mDetailListview;
    @Bind(R.id.iv_cancel)
    TextView mCancel;
    @Bind(R.id.scrollview)
    ScrollView mScrollview;
    @Bind(R.id.webvedio)
    Button mWebVideo;
    private TextView clean;
    private RelativeLayout lishi;
    private RecordDao recordDao;
    private RecordAdatper mRecordAdapter;
    private List<SearchMovieResult.DataBean> mDatas;
    private List<SearchMovieResult.DataBean> movieList = new ArrayList<>();
    private int lastVisibleItem;
    private View nodata;
    private LinearLayoutManager linearLayoutManager;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private View close;
    private int mPage = 1, mPagetsize = 10;
    //    private SearchMovieAdapter mDetailAdapter;
    private boolean isclose = true;
    SearchBean mSearchBeans = new SearchBean();
    private ListView mKeyWordListView;
    private SearchNameAdapter mNameAdapter;
    private boolean isRecord = false;
    private View search_result;
    private ViewPager mViewpager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragments = new ArrayList<>();
    private MyViewPagerAdapter mViewPagerAdapter;
    private Subscription subscription1;
    private View myprog;
    private TextView tishi;
    private String mBehavior;
    private boolean mCanKeyWordShow = true;

    private String mExtraSearchText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_search_movie, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        initExtra();
        initView();
        initData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                XGIMILOG.E("onResume中搜索 : " + mExtraSearchText);
                if (mExtraSearchText != null && !"".equals(mExtraSearchText)) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(
                                mEdittextSearch.getApplicationWindowToken(), 0);
                    }
                    mPage = 1;
                    movieList.clear();
                    isMoreLoading = false;
                    search_result.setVisibility(View.GONE);
                    mKeyWordListView.setVisibility(View.GONE);
                    ToosUtil.getInstance().addEventUmeng(SearchMovieActivity.this, "event_movie_search");
                    myprog.setVisibility(View.VISIBLE);
                    search_result.setVisibility(View.GONE);
                    mScrollview.setVisibility(View.GONE);
                    mKeyWordListView.setVisibility(View.GONE);
                    Statics.getInstance().sendSearchStatics(SearchMovieActivity.this, mEdittextSearch.getText().toString(), mEdittextSearch.getText().toString(), "影视");
                    initGetTv(mExtraSearchText);
                    isRecord = true;
                    mEdittextSearch.setText(mExtraSearchText);
                    addSql(mExtraSearchText);
                    mExtraSearchText = "";
                }
            }
        }, 10);
    }

    private void initExtra() {
        Intent intent = getIntent();
        if (intent != null) {
            mExtraSearchText = intent.getStringExtra(SEARCH_EXTRA_KEY);
            XGIMILOG.E("搜索关键字 : " + mExtraSearchText);
        }
    }


    private SearchAllFragment allfragment;
    private SearchMovieFragment moviefragment;
    private SearchTVFragment tvfragment;
    private SearchDongManFragment dongManFragment;
    private SearchJiLuPianFragment jiLuPianFragment;
    private SearchZongYiFragment zongyifragment;

    private void initFragments() {
        mFragments.clear();
        if (allfragment == null) {
            XGIMILOG.D("");
            allfragment = new SearchAllFragment();
        } else {
            XGIMILOG.D("");
            allfragment.dataChange();
        }
        moviefragment = new SearchMovieFragment();
        tvfragment = new SearchTVFragment();
        jiLuPianFragment = new SearchJiLuPianFragment();
        dongManFragment = new SearchDongManFragment();
        zongyifragment = new SearchZongYiFragment();
        if (mDatas != null) {
            mFragments.add(allfragment);
        }
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).type.equals("电影")) {
                mFragments.add(moviefragment);
                SaveData.getInstance().dy = i;
            }
            if (mDatas.get(i).type.equals("电视剧")) {
                mFragments.add(tvfragment);
                SaveData.getInstance().dsj = i;
            }
            if (mDatas.get(i).type.equals("综艺")) {
                mFragments.add(zongyifragment);
                SaveData.getInstance().zy = i;
            }
            if (mDatas.get(i).type.equals("动漫")) {
                mFragments.add(dongManFragment);
                SaveData.getInstance().dm = i;
            }
            if (mDatas.get(i).type.equals("纪录片")) {
                mFragments.add(jiLuPianFragment);
                SaveData.getInstance().jlp = i;
            }
        }
    }

    private void initView() {
        recordDao = new RecordDao(this);
        EventBus.getDefault().register(this);
        List<SearchLiveRecord> historyList = recordDao.getRecord1();
        Log.d(TAG, "initView: " + historyList.size());
        mRecordAdapter = new RecordAdatper(this, historyList);
        mHoslistView.setAdapter(mRecordAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.main_film).findViewById(R.id.id_tablayout);
        myprog = findViewById(R.id.myprog);
//        mDetailAdapter = new SearchMovieAdapter(this,mDatas);
        linearLayoutManager = new LinearLayoutManager(this);
        nodata = findViewById(R.id.nodata);
        close = findViewById(R.id.close);
        tishi = (TextView) findViewById(R.id.tihsi);
        if (SaveData.getInstance().video == 0) {
            close.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
            mWebVideo.setVisibility(View.GONE);

            search_result.setVisibility(View.GONE);
            mScrollview.setVisibility(View.GONE);
            isclose = true;
        } else {
            isclose = false;
        }
//        mDetailListview.setAdapter(mDetailAdapter);
//        mDetailListview.setLayoutManager(linearLayoutManager);
        search_result = findViewById(R.id.main_film);


        mKeyWordListView = (ListView) findViewById(R.id.listview);
        mNameAdapter = new SearchNameAdapter(this, mSearchBeans);
        mKeyWordListView.setAdapter(mNameAdapter);
        mWebVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(SearchMovieActivity.this, WebHtmlIQiYiActivity.class);
                inten.putExtra("url", mEdittextSearch.getText().toString().trim());
                startActivity(inten);
            }
        });
        mKeyWordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                XGIMILOG.D("关键字搜索影视 : " + mNameAdapter.getItem(position));
                /* 隐藏软键盘 */
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(
                            mKeyWordListView.getApplicationWindowToken(), 0);
                }
                mPage = 1;
                movieList.clear();
                isMoreLoading = false;
                isRecord = true;
                mEdittextSearch.setText(mNameAdapter.getItem(position));
                mEdittextSearch.setSelection(mEdittextSearch.getText().toString().length());//将光标移至文字末尾
                ToosUtil.getInstance().addEventUmeng(SearchMovieActivity.this, "event_movie_search");
                Statics.getInstance().sendSearchStatics(SearchMovieActivity.this, mNameAdapter.getItem(position), mEdittextSearch.getText().toString(), "影视");

                initGetTv(mNameAdapter.getItem(position));
                myprog.setVisibility(View.VISIBLE);
                addSql(mNameAdapter.getItem(position));
                mScrollview.setVisibility(View.GONE);
                search_result.setVisibility(View.GONE);
                nodata.setVisibility(View.GONE);
                mWebVideo.setVisibility(View.GONE);
                mKeyWordListView.setVisibility(View.GONE);
            }
        });
//        mDetailAdapter.setLisener(new SearchMovieAdapter.OnitemClick() {
//            @Override
//            public void onItemCliceLisener(String id) {
//                Intent intent=new Intent(SearchMovieActivity.this, FilmDetailActivity.class);
//                intent.putExtra("id",id);
//                startActivity(intent);
//            }
//        });
        clean = (TextView) findViewById(R.id.tv_clean_record);
        lishi = (RelativeLayout) findViewById(R.id.lishi);
        jiance();
    }

    private void sousuo() {

        mViewpager = null;
        mViewpager = (ViewPager) findViewById(R.id.main_film).findViewById(R.id.id_viewpager);

        String[] mTitles = new String[mDatas.size() + 1];
        mTitles[0] = "全部";
        for (int i = 0; i < mDatas.size(); i++) {
            mTitles[i + 1] = mDatas.get(i).type;
        }
        initFragments();
//        String[] mTitles=new String[]{mDatas.get(0).type,mDatas.get(1).type,mDatas.get(2).type,mDatas.get(3).type};
//        mTabLayout=null;
//
//        mTabLayout= (TabLayout) findViewById(R.id.main_film).findViewById(R.id.id_tablayout);

        // 初始化ViewPager的适配器，并设置给它
        if (mViewPagerAdapter == null) {
            mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
            mViewpager.setAdapter(mViewPagerAdapter);
        } else {
            mViewPagerAdapter.dataChange(mTitles, mFragments);
            mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
            mViewpager.setAdapter(mViewPagerAdapter);
        }
        // 设置ViewPager最大缓存的页面个数
        mViewpager.setOffscreenPageLimit(mTitles.length);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewpager.addOnPageChangeListener(this);

        if (Build.VERSION.SDK_INT >= 21) {
            mTabLayout.setElevation((float) 0);
        }
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来

        mTabLayout.setupWithViewPager(mViewpager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
//        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
        if (mViewPagerAdapter != null) {
            mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
        }
    }

    public void onEventMainThread(SearchLiveRecord record) {
        recordDao.deleteAlread1(record.name);
        mRecordAdapter.changeData((ArrayList<SearchLiveRecord>) recordDao.getRecord1());
        jiance();
    }

    private void jiance() {
        List<SearchLiveRecord> record3 = recordDao.getRecord1();
        if (record3.isEmpty()) {
            lishi.setVisibility(View.GONE);
        } else {
            if (!isclose) {
                lishi.setVisibility(View.VISIBLE);
                mScrollview.setVisibility(View.VISIBLE);
                tishi.setVisibility(View.GONE);
            }
        }
    }

    private void initData() {

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(
                            v.getApplicationWindowToken(), 0);
                }
                finish();
            }
        });
        mEdittextSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        /* 隐藏软键盘 */

                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isActive()) {
                            inputMethodManager.hideSoftInputFromWindow(
                                    v.getApplicationWindowToken(), 0);
                        }
                        mPage = 1;
                        movieList.clear();
                        isMoreLoading = false;
                        search_result.setVisibility(View.GONE);
                        mKeyWordListView.setVisibility(View.GONE);
                        ToosUtil.getInstance().addEventUmeng(SearchMovieActivity.this, "event_movie_search");
                        myprog.setVisibility(View.VISIBLE);
                        if (mEdittextSearch.getText().toString().trim().length() > 0) {
                            search_result.setVisibility(View.GONE);
                            mScrollview.setVisibility(View.GONE);
                            mKeyWordListView.setVisibility(View.GONE);
                            Statics.getInstance().sendSearchStatics(SearchMovieActivity.this, mEdittextSearch.getText().toString(), mEdittextSearch.getText().toString(), "影视");
                            initGetTv(mEdittextSearch.getText().toString().trim());
                        } else {
                            mScrollview.setVisibility(View.VISIBLE);
                            search_result.setVisibility(View.GONE);
                            mKeyWordListView.setVisibility(View.GONE);
                            ToastUtil.getToast("输入内容不能为空", SearchMovieActivity.this).show();
                        }
                        // jiance();
                        String record = mEdittextSearch.getText().toString().trim();

                        if (!record.equals("")) {
                            addSql(record);
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        mEdittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                XGIMILOG.D("");

                tishi.setVisibility(View.GONE);
                search_result.setVisibility(View.GONE);
                mKeyWordListView.setVisibility(View.GONE);
                if (mEdittextSearch.getText().toString().toString().length() > 0 && !isRecord) {
                    getKeyWords(mEdittextSearch.getText().toString().toString().trim());
                    mCanKeyWordShow = true;
                }
                isRecord = false;
                if (mEdittextSearch.getText().toString().trim().length() == 0) {
                    search_result.setVisibility(View.GONE);
                    mKeyWordListView.setVisibility(View.GONE);
                    mScrollview.setVisibility(View.VISIBLE);
                    if (recordDao.getRecord1() != null) {
                        mRecordAdapter.changeData((ArrayList<SearchLiveRecord>) recordDao.getRecord1());
//                        mScrollview.setVisibility(View.VISIBLE);
                    }
                    nodata.setVisibility(View.GONE);
                    mWebVideo.setVisibility(View.GONE);
                    //   jiance();
                }
            }
        });
        mHoslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                XGIMILOG.D("影视搜索历史点击 : " + mRecordAdapter.getItem(position));
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(
                            mHoslistView.getApplicationWindowToken(), 0);
                }
                SearchLiveRecord mRecord = mRecordAdapter.getItem(position);
                addSql(mRecord.name);
                myprog.setVisibility(View.VISIBLE);
                mPage = 1;
                isRecord = true;
                isMoreLoading = false;
                movieList.clear();
                nodata.setVisibility(View.GONE);
                mWebVideo.setVisibility(View.GONE);
                search_result.setVisibility(View.GONE);
                mEdittextSearch.setText(mRecord.name);
                mEdittextSearch.setSelection(mEdittextSearch.getText().toString().length());//将光标移至文字末尾
                mScrollview.setVisibility(View.GONE);
                mKeyWordListView.setVisibility(View.GONE);
                Statics.getInstance().sendSearchStatics(SearchMovieActivity.this, mRecord.name, "", "影视");
                initGetTv(mRecord.name);

            }
        });
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordDao.deleteRecord1();
                lishi.setVisibility(View.GONE);
            }
        });

    }

    //删除超过的条数
    public void deleteMore() {
        if (recordDao.getRecord1().size() > 20) {
            delete(recordDao.getRecord1().get(0).name);
        }
    }

    //添加经数据库
    public void addSql(String s) {
        List<SearchLiveRecord> record2 = recordDao.getRecord1();
        for (int j = 0; j < record2.size(); j++) {
            SearchLiveRecord re = record2.get(j);
            if (re.name.equals(s)) {
                delete(s);
            }
        }
        recordDao.addRecord1(s);
    }

    //删除存在的数据
    public void delete(String name) {
        recordDao.deleteAlread1(name);
    }

    private String myKey;

    private void initGetTv(String key) {
        myKey = key;
        XGIMILOG.D("开始搜索影视 : " + key);
        mCanKeyWordShow = false;
        mWebVideo.setVisibility(View.GONE);
        myprog.setVisibility(View.VISIBLE);
        try {
            subscription1 = Api.getXgimiVideoApi(MyAesToGsonFactory.create(SearchMovieResult.class))
                    .getSearchMovie(Api.getEncodeParam(new String[]{"keyword"}, new String[]{key}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取所有的年份
    Observer<SearchMovieResult> observer1 = new Observer<SearchMovieResult>() {
        @Override
        public void onCompleted() {
            unRegist(subscription1);
            myprog.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            isMoreLoading = false;
            Log.e("wrong", e + "");
            initGetTv(myKey);
            myprog.setVisibility(View.GONE);
        }

        @Override
        public void onNext(SearchMovieResult channels) {
            isRecord = false;
            XGIMILOG.E("搜索影视完成");
//            LogUtil.d(XGIMILOG.getTag(), "搜索影视完成 : " + new Gson().toJson(channels));
            if (channels != null && channels.data != null && channels.data.size() > 0) {
                XGIMILOG.D("影视搜索结果大小 : " + channels.data.size() + "");
                boolean isSearchResultEmpty = true;
                for (int i = 0; i < channels.data.size(); i++) {
                    if (channels.data.get(i).getList() != null && channels.data.get(i).getList().size() > 0) {
                        isSearchResultEmpty = false;
                        break;
                    }
                }
                if (isSearchResultEmpty) {//搜索结果为空
                    nodata.setVisibility(View.GONE);
                    mWebVideo.setVisibility(View.VISIBLE);
                    close.setVisibility(View.GONE);
                    mScrollview.setVisibility(View.GONE);
                    search_result.setVisibility(View.GONE);
                } else {
                    search_result.setVisibility(View.VISIBLE);
                    SaveData.getInstance().searchmovie = channels.data;
                    mDatas = channels.data;
                    sousuo();
                    nodata.setVisibility(View.GONE);
                    mWebVideo.setVisibility(View.GONE);
                    close.setVisibility(View.GONE);
                    tishi.setVisibility(View.GONE);
                    mKeyWordListView.setVisibility(View.GONE);
                }
            } else {
                nodata.setVisibility(View.GONE);
                mWebVideo.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);
                mScrollview.setVisibility(View.GONE);
                search_result.setVisibility(View.GONE);
            }
            if (isclose) {
                nodata.setVisibility(View.GONE);
                mWebVideo.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
                mScrollview.setVisibility(View.GONE);
                search_result.setVisibility(View.GONE);
            }
            myprog.setVisibility(View.GONE);
            isMoreLoading = false;
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }

    private void getKeyWords(String key) {
//        subscription = Api.getMangoApi().getFilmKeyWord(key, 20, "video")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer2);

        XGIMILOG.D("获取影视关键字 : " + key);
        mWebVideo.setVisibility(View.GONE);
        try {
            subscription = Api.getXgimiVideoApi(MyAesToGsonFactory.create(SearchBean.class))
                    .getFilmKeyWord(Api.getEncodeParam(new String[]{"keyword"}, new String[]{key}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取关键字
    Observer<SearchBean> observer2 = new Observer<SearchBean>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
//            myprog.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            isMoreLoading = false;
            myprog.setVisibility(View.GONE);
        }

        @Override
        public void onNext(SearchBean channels) {
            XGIMILOG.D("影视关键字获取完成 : " + new Gson().toJson(channels));
            if (mCanKeyWordShow) {
                if (channels != null && channels.data != null && channels.data.size() > 0) {
                    mNameAdapter.dataChange(channels);
                    close.setVisibility(View.GONE);
                    mScrollview.setVisibility(View.GONE);
                    mKeyWordListView.setVisibility(View.VISIBLE);
                    tishi.setVisibility(View.GONE);
                    mScrollview.setVisibility(View.GONE);
                    search_result.setVisibility(View.GONE);
                    myprog.setVisibility(View.GONE);
                    mWebVideo.setVisibility(View.GONE);
                } else {
                    nodata.setVisibility(View.GONE);
                    mWebVideo.setVisibility(View.VISIBLE);
                    close.setVisibility(View.GONE);
                    mScrollview.setVisibility(View.GONE);
                    search_result.setVisibility(View.GONE);
                    mKeyWordListView.setVisibility(View.GONE);
                    myprog.setVisibility(View.GONE);
                }
            } else {
//                nodata.setVisibility(View.GONE);
//                mWebVideo.setVisibility(View.VISIBLE);
//                close.setVisibility(View.GONE);
//                mScrollview.setVisibility(View.GONE);
//                search_result.setVisibility(View.GONE);
//                mKeyWordListView.setVisibility(View.GONE);
//                myprog.setVisibility(View.GONE);
                XGIMILOG.D("已经开始搜索，不显示关键字");
            }
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
