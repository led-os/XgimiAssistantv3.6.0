package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.zhushou.aes.MyAesToGsonFactory;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.AllMovieAdapter;
import com.xgimi.zhushou.bean.AllMovie;
import com.xgimi.zhushou.bean.ChannelBean;
import com.xgimi.zhushou.bean.MovieByCategory;
import com.xgimi.zhushou.inter.OnitemClick;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.Statics;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChannelActivity extends BaseActivity {

    private String title;
    List<LinearLayout> linears = new ArrayList<LinearLayout>();
    private List<HorizontalScrollView> hors = new ArrayList<>();
    private Subscription subscription3;
    private List<String> mAreaString = new ArrayList<>();
    private List<String> mSortsString = new ArrayList<>();
    private List<String> mYearString = new ArrayList<>();
    private Subscription subscription2;
    private Subscription subscription1;
    private AllMovieAdapter mMovieAdapter;
    private List<AllMovie.DataBean> mDatas = new ArrayList<>();
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private int lastVisibleItem;
    private int mPage = 1;
    private int mPageSize = 30;
    private String mCategory;
    private String mCategoryId;
    private int msorts;
    private String mKind;
    private String mVideoKind;
    private String mArea;
    private String mYear;
    private String mKey;
    private View net_connect;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerview;
    private App app;
    private View load_false;
    private View prog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        initExras();
        initView();
        getAllSorts();
    }

//    private void initLocal() {
//        String readHomeJson = app.readHomeJson("mCategoryId");
//        if (!StringUtils.isEmpty(readHomeJson)) {
//            //dialog.dismiss();
//            MovieByCategory data = new Gson().fromJson(readHomeJson, MovieByCategory.class);
//            loadHome(data);
//        } else {
//            if (!HttpUrl.isNetworkConnected(ChannelActivity.this)) {
//                net_connect.setVisibility(View.VISIBLE);
//                mRecyclerview.setVisibility(View.GONE);
//                MissDilog();
//            }
//        }
//        if (HttpUrl.isNetworkConnected(ChannelActivity.this)) {
//            initData();
//        }
//    }

    private void loadHome(MovieByCategory channels) {
        if (channels != null && channels.data != null) {
            mMovieByCategorys = channels.data;
            mMovieAdapter.dataChange(1, channels.data);
        }
        mRecyclerview.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
        load_false.setVisibility(View.GONE);
    }

    private void initExras() {
        Intent intent = getIntent();
        if (intent != null) {
            mCategory = intent.getStringExtra("title");
            if (mCategory.equals("电影")) {
                mCategoryId = "1";
            } else if (mCategory.equals("动漫")) {
                mCategoryId = "4";
            } else if (mCategory.equals("纪录片")) {
                mCategoryId = "5";
            } else if (mCategory.equals("电视剧")) {
                mCategoryId = "2";
            } else {
                mCategoryId = "3";
            }
        }
    }

    private void initView() {
        app = (App) ChannelActivity.this.getApplicationContext();
        prog = findViewById(R.id.myprog);
        EventBus.getDefault().register(this);
        controlTitle(findViewById(R.id.id_toolbar), true, true, false, false);
        tv.setText(mCategory);
        mRecyclerview = (RecyclerView) findViewById(R.id.recyleview);
        mMovieAdapter = new AllMovieAdapter(this, mDatas, false, "channel");
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setAdapter(mMovieAdapter);
        load_false = findViewById(R.id.load_false);
        mMovieAdapter.setLisener(new OnitemClick() {
            @Override
            public void onClickLisener(String name, String id, String type) {
                Intent intent = new Intent(ChannelActivity.this, FilmDetailActivity.class);
                intent.putExtra("id", id);
                SaveData.getInstance().mSouceInsight = "7";
                Statics.getInstance().sendStatics(ChannelActivity.this, name, id, SaveData.getInstance().mSouceInsight, SaveData.getInstance().mSoucePage, null);

                startActivity(intent);
            }
        });
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mMovieAdapter.getItemCount()) {
                    if (!isMoreLoading) {
                        isMoreLoading = true;
                        mPage++;
                        initData(mPage, mCategory, mVideoKind, mArea, mYear);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        net_connect = findViewById(R.id.netconnect);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpUrl.isNetworkConnected(ChannelActivity.this)) {
                    mRecyclerview.setVisibility(View.VISIBLE);
                    net_connect.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);
                    getAllSorts();
                }
            }
        });
        if (!HttpUrl.isNetworkConnected(ChannelActivity.this)) {
            net_connect.setVisibility(View.VISIBLE);
            mRecyclerview.setVisibility(View.GONE);
            MissDilog();
        }
    }

    public void onEventMainThread(ChannelBean info) {
        prog.setVisibility(View.VISIBLE);
        mPage = 1;
        mKind = info.getId();
        mYear = info.getYear();
        mArea = info.getArea();
        mVideoKind = info.kind;
        XGIMILOG.D("" + mKind + ", " + mVideoKind);
        initData(mPage, mCategory, mVideoKind, mArea, mYear);
    }

    MovieByCategory.DataBean mMovieByCategorys;
    private void getAllSorts() {
        XGIMILOG.D("请求获取筛选条件");
        prog.setVisibility(View.VISIBLE);
        try {
            subscription3 = Api.getXgimiVideoApi(MyAesToGsonFactory.create(MovieByCategory.class))
                    .getMovieByCategory(Api.getEncodeParam(new String[]{"category_id"}, new String[]{mCategoryId}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer3);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    Observer<MovieByCategory> observer3 = new Observer<MovieByCategory>() {
        @Override
        public void onCompleted() {
            prog.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            prog.setVisibility(View.GONE);
            XGIMILOG.D(e.getMessage());
            e.printStackTrace();
            MissDilog();
            load_false.setVisibility(View.VISIBLE);
            Toast.makeText(ChannelActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(MovieByCategory channels) {
            prog.setVisibility(View.GONE);
            LogUtil.d(XGIMILOG.getTag(), "获取channels = " + new Gson().toJson(channels));
            if (channels != null && channels.data != null) {
                String savaHomeJson = new Gson().toJson(channels);
                app.savaHomeJson("mCategoryId", savaHomeJson);
                loadHome(channels);
                initData(mPage, mCategory, mVideoKind, mArea, mYear);
            } else {
                load_false.setVisibility(View.VISIBLE);
                net_connect.setVisibility(View.GONE);
                mRecyclerview.setVisibility(View.VISIBLE);
                MissDilog();
            }
        }
    };

    private void initData(int page, String category, String kind, String area, String year) {
        XGIMILOG.D("正在请求 page = " + page + ", category = " + category + ", kind = " + kind + ", area = " + area + ", year = " + year);
        prog.setVisibility(View.VISIBLE);
        try {
            subscription = Api.getXgimiVideoApi(MyAesToGsonFactory.create(AllMovie.class))
                    .getAllMovie(Api.getEncodeParam(
                            new String[]{"category", "page", "kind", "year", "area"},
                            new String[]{category, page+"", kind, year, area}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
            mMovieAdapter.showFootView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Observer<AllMovie> observer = new Observer<AllMovie>() {
        @Override
        public void onCompleted() {
            prog.setVisibility(View.GONE);
            isMoreLoading = false;
            MissDilog();
            prog.setVisibility(View.GONE);
//            unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            prog.setVisibility(View.GONE);
            e.printStackTrace();
            MissDilog();
            prog.setVisibility(View.GONE);
            load_false.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNext(AllMovie channels) {
            prog.setVisibility(View.VISIBLE);
//            LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(channels));
            if (channels != null && channels.data != null && channels.data.size() != 0) {
                XGIMILOG.D("channels.data = " + new Gson().toJson(channels.data));
                if (mPage == 1) {
                    XGIMILOG.D("");
                    mDatas.clear();
                    mMovieAdapter.dataChange(channels.data);
                    mDatas.addAll(channels.data);
                } else {
                    XGIMILOG.D("");
                    mDatas.addAll(channels.data);
                    mMovieAdapter.dataChange(mDatas);
                }
            } else {
                if (mPage == 1) {
                    XGIMILOG.D("");
//                    mRecyclerview.setVisibility(View.GONE);
                    mDatas.clear();
                    mMovieAdapter.dataChange(mDatas);
                    Toast.makeText(ChannelActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                } else {
                    XGIMILOG.D("");
                    Toast.makeText(ChannelActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    mPage--;
                    mMovieAdapter.dismissFootView();
                }
                load_false.setVisibility(View.VISIBLE);
                net_connect.setVisibility(View.GONE);
            }
            prog.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unsubscribe(subscription);
        unsubscribe(subscription1);
        unsubscribe(subscription2);
        unsubscribe(subscription3);
        finish();
    }
}
