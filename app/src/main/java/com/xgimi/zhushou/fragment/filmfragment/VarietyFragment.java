package com.xgimi.zhushou.fragment.filmfragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xgimi.zhushou.aes.MyAesToGsonFactory;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.FilmDetailActivity;
import com.xgimi.zhushou.adapter.AllMovieAdapter;
import com.xgimi.zhushou.adapter.MovieSortsAdapter;
import com.xgimi.zhushou.bean.AllMovie;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.inter.OnitemClick;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.Statics;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class VarietyFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    RecyclerView recyclerView;
    TextView tv_channel;

    private List<AllMovie.DataBean> mDatas = new ArrayList<>();
    private int mPage = 1;
    private int mPageSize = 30;
    private String mCategory = "综艺";
    private int msorts;
    private String mKind;
    private String mArea;
    private String mYear;
    private String mKey;
    private AllMovieAdapter mMovieAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean isSorts;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private int lastVisibleItem;
    private View view;
    private PopupWindow popupWindow;
    private MovieSortsAdapter mPopAdapter;
    private Subscription subscription1;
    private Subscription subscription2;
    private Subscription subscription3;
    private View net_connect;
    private AnimationDrawable animationDrawable;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View load_false;
    private View close;

    public VarietyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_movie, container, false);
        initView(view);
        initLocal();
        return view;
    }

    private void initView(View view) {
        close = view.findViewById(R.id.close);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyleview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyleview);
        tv_channel = (TextView) view.findViewById(R.id.tv_channel);
        mMovieAdapter = new AllMovieAdapter(getActivity(), mDatas, true, "综艺");
        mLayoutManager = new LinearLayoutManager(getActivity());
        load_false = view.findViewById(R.id.load_false);
        recyclerView.setAdapter(mMovieAdapter);
        mMovieAdapter.setLisener(new OnitemClick() {
            @Override
            public void onClickLisener(String name, String id, String type) {
                Intent intent = new Intent(getActivity(), FilmDetailActivity.class);
                intent.putExtra("id", id);
                SaveData.getInstance().mSouceInsight = "3";
                SaveData.getInstance().mSoucePage = "4";
                Statics.getInstance().sendStatics(getActivity(), name, id, SaveData.getInstance().mSouceInsight, SaveData.getInstance().mSoucePage, null);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(mLayoutManager);
        if (HttpUrl.isNetworkConnected(getActivity())) {
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mMovieAdapter.getItemCount()) {
                        if (!isMoreLoading) {
                            isMoreLoading = true;
                            mPage++;
                            isSorts = false;
                            initData(mPage, mCategory, mKind, mArea, mYear);
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                }
            });
        }
        net_connect = view.findViewById(R.id.netconnect);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpUrl.isNetworkConnected(getActivity())) {
                    recyclerView.setVisibility(View.GONE);
                    net_connect.setVisibility(View.GONE);
                    initData(mPage, mCategory, mKind, mArea, mYear);
                }
            }
        });
    }

    //读取缓存本地数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson("varietyfragment");
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            AllMovie data = new Gson().fromJson(readHomeJson, AllMovie.class);
            loadHome(data);
        } else {
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                net_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            initData(mPage, mCategory, mKind, mArea, mYear);
        }
    }

    private void loadHome(AllMovie channels) {
        if (isMoreLoading) {
            if (isSorts) {
                mDatas.clear();
                mDatas.addAll(channels.data);
            } else {
                mDatas.addAll(channels.data);
            }
        } else {
            mDatas = channels.data;
        }
        mMovieAdapter.dataChange(mDatas);
        load_false.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
//        if(SaveData.getInstance().video==0){
//            recyclerView.setVisibility(View.GONE);
//            net_connect.setVisibility(View.GONE);
//            close.setVisibility(View.VISIBLE);
//            load_false.setVisibility(View.GONE);
//        }
    }

    private int refreshpage;

    private void initData(int page, String category, String kind, String area, String year) {
        refreshpage = page;
//        XGIMILOG.D("正在请求 page = " + page + ", category = " + category + ", kind = " + kind + ", area = " + area + ", year = " + year);
//        if (kind == null) {
//            kind = "";
//        }
//        if (area == null) {
//            area = "";
//        }
//        if (year == null) {
//            year = "";
//        }
        try {
            subscription = Api.getXgimiVideoApi(MyAesToGsonFactory.create(AllMovie.class))
                    .getAllMovie(Api.getEncodeParam(
                            new String[]{"category", "page", "kind", "year", "area"},
                            new String[]{category, page + "", kind, year, area}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Observer<AllMovie> observer = new Observer<AllMovie>() {
        @Override
        public void onCompleted() {
            isMoreLoading = false;
            mSwipeRefreshLayout.setRefreshing(false);
//            unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            if (HttpUrl.isNetworkConnected(getActivity())) {
                load_false.setVisibility(View.VISIBLE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onNext(AllMovie channels) {
            if (channels != null && channels.data != null) {
                if (refreshpage == 1) {
//                    App app = (App) getActivity().getApplicationContext();
                    String varietyfragment = new Gson().toJson(channels);
                    app.savaHomeJson("varietyfragment", varietyfragment);
                }
                loadHome(channels);
            } else {
                if (HttpUrl.isNetworkConnected(getActivity())) {
                    load_false.setVisibility(View.VISIBLE);
                }
                recyclerView.setVisibility(View.GONE);
                net_connect.setVisibility(View.GONE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }

    }


    @Override
    public void onRefresh() {
        mPage = 1;
        initData(mPage, mCategory, mKind, mArea, mYear);

    }
}
