package com.xgimi.zhushou.fragment.musicfragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.AllRadioClassActivity;
import com.xgimi.zhushou.activity.RadioDetailActivity;
import com.xgimi.zhushou.adapter.MusicViewHolder.HotRadioAdapter;
import com.xgimi.zhushou.bean.HotRadio;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/16.
 */
public class MusicRadioFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private RecyclerView recyleview;
    //    private TextView hotradio;
    private TextView allclass;
    private HotRadioAdapter adapter;
    private List<HotRadio.data> mData = new ArrayList<>();
    private Subscription subscription;
    private LinearLayoutManager mLayoutManager;
    private int page = 1;
    private View net_connect;
    private RelativeLayout ll;
    private AnimationDrawable animationDrawable;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isRefresh = false;
    private View load_false;
    private int lastVisibleItem;
    private View prog;

    public MusicRadioFragment() {
        // Required empty public constructor
    }

    public static MusicRadioFragment fragment;

    public static MusicRadioFragment getInstance() {
        if (fragment == null) {
            fragment = new MusicRadioFragment();
        }
        return fragment;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initLocal();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_radio, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initData();
        handler.sendEmptyMessageDelayed(1, 200);
        return view;
    }

    private boolean isFisrt;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isFisrt && isVisibleToUser) {
            isFisrt = true;
        }
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        ll = (RelativeLayout) view.findViewById(R.id.ll);
        recyleview = (RecyclerView) view.findViewById(R.id.recyleview);
        adapter = new HotRadioAdapter(mData, getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyleview.setAdapter(adapter);
        recyleview.setLayoutManager(mLayoutManager);
        load_false = view.findViewById(R.id.load_false);
        allclass = (TextView) view.findViewById(R.id.allclass);
//        hotradio= (TextView) view.findViewById(R.id.remen);
        allclass.setOnClickListener(this);
        //  hotradio.setOnClickListener(this);
        net_connect = view.findViewById(R.id.netconnect);
        prog = view.findViewById(R.id.myprog);
        prog.setVisibility(View.VISIBLE);

        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpUrl.isNetworkConnected(getActivity())) {
                    // load.setVisibility(View.VISIBLE);
                    prog.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                    net_connect.setVisibility(View.GONE);
                    getHotRadio(page);
                }
            }
        });
    }

    //读取缓存本地数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson("hotradio");
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            HotRadio data = new Gson().fromJson(readHomeJson, HotRadio.class);
            loadHome(data);
        } else {
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                net_connect.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            getHotRadio(page);
        }
//        hotradio.setOnClickListener(this);
    }

    private void loadHome(HotRadio hotRadio) {
        if (isRefresh) {
            mData.addAll(hotRadio.data);
        } else {
            if (hotRadio.data != null) {
                mData = hotRadio.data;
            }
        }
        adapter.dataChange(mData);
        ll.setVisibility(View.VISIBLE);
        prog.setVisibility(View.GONE);
        load_false.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //跳转等逻辑
    private void initData() {
        adapter.setOnClickListener(new HotRadioAdapter.OnClickListener() {

            @Override
            public void onClick(Object object, int position) {
                Intent intent = new Intent(getActivity(), RadioDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id", ((HotRadio.data) object).fm_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
//        recyleview.addOnScrollListener(new EndLessOnScrollListener(false, mLayoutManager) {
//            @Override
//            public void onLoadMore(int currentPage) {
//                currentPage++;
//
//
//            }
//        });
        if (HttpUrl.isNetworkConnected(getActivity())) {
            recyleview.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                        page++;
                        isRefresh = true;
                        getHotRadio(page);
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                }
            });
        }
    }

    private int mpage;

    //获取热门电台
    private void getHotRadio(int page) {
        mpage = page;
        subscription = Api.getMangoApi().getHotRadio(page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<HotRadio> observer = new Observer<HotRadio>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
            prog.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            load_false.setVisibility(View.VISIBLE);
            prog.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onNext(HotRadio hotRadio) {
            XGIMILOG.E("电台数据");

//            LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(hotRadio));
            if (hotRadio != null & hotRadio.data != null) {
                if (mpage == 1) {
                    App app = (App) getActivity().getApplicationContext();
                    String hotradio = new Gson().toJson(hotRadio);
                    app.savaHomeJson("hotradio", hotradio);
                }
                loadHome(hotRadio);
            } else {
                load_false.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            }
            prog.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allclass:
                Intent intent = new Intent(getActivity(), AllRadioClassActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        getHotRadio(page);
    }
}
