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
import android.widget.ProgressBar;
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


public class MovieFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;

    TextView tv_channel;
    private List<AllMovie.DataBean> mDatas = new ArrayList<>();
    private int mPage = 1;
    private int mPageSize = 30;
    private String mCategory = "电影";
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
    private boolean isRefresh = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View load_false;
    private View close;
    private ProgressBar mProgressBar;

    public MovieFragment() {
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
        mProgressBar = (ProgressBar) view.findViewById(R.id.pgb_fragment_movie);
        close = view.findViewById(R.id.close);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyleview);
        mMovieAdapter = new AllMovieAdapter(getActivity(), mDatas, true, "电影");
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(mMovieAdapter);
        load_false = view.findViewById(R.id.load_false);
        mMovieAdapter.setLisener(new OnitemClick() {
            @Override
            public void onClickLisener(String name, String id, String type) {
                Intent intent = new Intent(getActivity(), FilmDetailActivity.class);
                intent.putExtra("id", id);
                SaveData.getInstance().mSouceInsight = "3";
                SaveData.getInstance().mSoucePage = "2";
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
        String readHomeJson = app.readHomeJson("allmovie");
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
//        mProgressBar.setVisibility(View.VISIBLE);
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
            mProgressBar.setVisibility(View.GONE);
//            unsubscribe();
        }

        @Override
        public void onError(Throwable e) {
            mProgressBar.setVisibility(View.GONE);
            e.printStackTrace();
            if (HttpUrl.isNetworkConnected(getActivity())) {
                load_false.setVisibility(View.VISIBLE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onNext(AllMovie channels) {
            mProgressBar.setVisibility(View.GONE);
            if (channels != null && channels.data != null) {
                if (refreshpage == 1) {
//                    App app = (App) getActivity().getApplicationContext();
                    String allmovie = new Gson().toJson(channels);
                    app.savaHomeJson("allmovie", allmovie);
                }
                loadHome(channels);
            } else {
                if (HttpUrl.isNetworkConnected(getActivity())) {
                    load_false.setVisibility(View.VISIBLE);
                }
                net_connect.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };


//    //显示popwindow
//    private void showPopupWindow() {
//        // 一个自定义的布局，作为显示的内容
//       final StringBuffer sb=new StringBuffer();
//        final StringBuffer sb1=new StringBuffer();
//       final  StringBuffer sb2=new StringBuffer();
//        View contentView = LayoutInflater.from(getActivity()).inflate(
//                R.layout.movie_pop, null);
//        // 设置按钮的点击事件
//        if(popupWindow==null) {
//            popupWindow = new PopupWindow(contentView,
//                    ToosUtil.getInstance().getScreenWidth(getActivity()), ViewGroup.LayoutParams.WRAP_CONTENT);
//
//            Button button = (Button) contentView.findViewById(R.id.cancel);
//            Button sure = (Button) contentView.findViewById(R.id.sure);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (msorts == 0) {
//                        mPopAdapter.selsetDatasSorts.clear();
//                    } else if (msorts == 1) {
//                        mPopAdapter.selsetDatasArea.clear();
//                    } else if (msorts == 2) {
//                        mPopAdapter.selsetDatasYear.clear();
//                    }
//                    mArea = "";
//                    isSorts = false;
//                    mYear = "";
//                    mKind = "";
//                    initData(mPage, mPageSize, mCategory, mKind, mArea, mYear, mKey);
//                    mPopAdapter.dataChange();
//                }
//            });
//            sure.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    popupWindow.dismiss();
//                    mPage = 0;
//                    sb.setLength(0);
//                    sb1.setLength(0);
//                    sb2.setLength(0);
//                    isSorts = true;
//                    for (int i = 0; i < mPopAdapter.selsetDatasSorts.size(); i++) {
//                        for (int j = 0; j < mMovieByCategorys.size(); j++) {
//                            if (mPopAdapter.selsetDatasSorts.get(i).equals(mMovieByCategorys.get(j).name)) {
//                                String id = mMovieByCategorys.get(j).id;
//                                sb.append(id);
//                            }
//                        }
//                    }
//                    for (int i = 0; i < mPopAdapter.selsetDatasArea.size(); i++) {
//                        sb1.append(mPopAdapter.selsetDatasArea.get(i));
//
//                    }
//                    for (int i = 0; i < mPopAdapter.selsetDatasYear.size(); i++) {
//                        sb2.append(mPopAdapter.selsetDatasYear.get(i));
//
//                    }
//                    mArea = sb1.toString();
//                    mYear = sb2.toString();
//                    mKind = sb.toString();
//
//                    initData(mPage, mPageSize, mCategory, sb.toString(), sb1.toString(), sb2.toString(), mKey);
//                }
//            });
//
//            RecyclerView pop_recylerview = (RecyclerView) contentView.findViewById(R.id.pop_recylerview);
//            mPopAdapter = new MovieSortsAdapter(mSortsString, getActivity());
//            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
//            pop_recylerview.setAdapter(mPopAdapter);
//            pop_recylerview.setLayoutManager(layoutManager);
//            popupWindow.setTouchable(true);
//            popupWindow.setOutsideTouchable(true);
//
//            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    Log.i("mengdd", "onTouch : ");
//
//                    return false;
//
//                }
//            });
//            // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
//            // 我觉得这里是API的一个bug
//            popupWindow.setBackgroundDrawable(new ColorDrawable(0000000000));
//            // 设置好参数之后再show
//        }
//    }
//
//    private void popShow(View view){
//        popupWindow.showAsDropDown(view);
//
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_channel:
//                Intent intent=new Intent(getActivity(), ChannelActivity.class);
//                intent.putExtra("title","电影");
//                startActivity(intent);
//
//                break;

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
