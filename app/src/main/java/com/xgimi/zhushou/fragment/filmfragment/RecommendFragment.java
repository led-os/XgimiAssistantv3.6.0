/**
 * 影视-精选
 */

package com.xgimi.zhushou.fragment.filmfragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.GMDevice;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.aes.MyAesToGsonFactory;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.FilmDetailActivity;
import com.xgimi.zhushou.activity.FilmZhuanTiActivity;
import com.xgimi.zhushou.activity.WebHtmlActivity;
import com.xgimi.zhushou.activity.XgimiCommunityActivity;
import com.xgimi.zhushou.adapter.RecommendAdapter;
import com.xgimi.zhushou.bean.MovieRecommend;
import com.xgimi.zhushou.bean.SelecterBean;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.fragment.FilmFragment;
import com.xgimi.zhushou.inter.OnFooterClick;
import com.xgimi.zhushou.inter.OnitemClick;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.Statics;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RecommendFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "RecommendFragment";

    @Bind(R.id.recyleview)
    RecyclerView mRecyclerView;
    MovieRecommend mDatas = new MovieRecommend();
    private View net_connect;
    private AnimationDrawable animationDrawable;
    private RecommendAdapter mRecommendAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View load_false;
    private View close;
    private boolean isFirstOpen = false;
    List<VcontrolCmd.CustomPlay.PlayList> mPlyLists = new ArrayList<>();

    public RecommendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initLocal();
        return view;
    }

    private boolean isInstall;
    private SharedPreferences mySp;

    private void initView(View view) {
        EventBus.getDefault().register(this);
        //实例化SharedPreferences对象
        SharedPreferences sp = getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
        boolean isFirst = sp.getBoolean("isFirst", true);
        if (isFirst == true) {
            //实例化SharedPreferences.Editor对象
            SharedPreferences.Editor edit = sp.edit();
            //用putString的方法保存数据
            edit.putBoolean("isFirst", false);
            //提交当前数据
            edit.commit();
            //应用首次启动
        } else {
            //应用非首次启动
        }
        //实例化SharedPreferences对象,保存上次的连接时的mac地址
        mySp = getActivity().getSharedPreferences("macData", getActivity().MODE_PRIVATE);
        SaveData.getInstance().deviceMac = mySp.getString("deviceMac", SaveData.getInstance().deviceMac);

        close = view.findViewById(R.id.close);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecommendAdapter = new RecommendAdapter(getActivity(), mDatas, mRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mRecommendAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        net_connect = view.findViewById(R.id.netconnect);
        load_false = view.findViewById(R.id.load_false);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpUrl.isNetworkConnected(getActivity())) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    net_connect.setVisibility(View.GONE);
                    if (SaveData.getInstance().deviceMac != null)
                        initData(SaveData.getInstance().deviceMac);
                    else {
                        initData("");
                    }
                }
            }
        });
        mRecommendAdapter.setLisener(new OnitemClick() {
            @Override
            public void onClickLisener(String name, String id, String type) {
                XGIMILOG.D(name + ", " + id +" , " + type );
                Statics.getInstance().sendStatics(getActivity(), name, id, SaveData.getInstance().mSouceInsight, SaveData.getInstance().mSoucePage, SaveData.getInstance().mSourceInsightLocation);
                if (type == null || type.equals("") || type.equals("video")) {
                    XGIMILOG.D(" Intent intent = new Intent(getActivity(), FilmDetailActivity.class)");
                    Intent intent = new Intent(getActivity(), FilmDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } else {
                    XGIMILOG.D("Intent intent = new Intent(getActivity(), FilmZhuanTiActivity.class)");
                    Intent intent = new Intent(getActivity(), FilmZhuanTiActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }

            }
        });
        mRecommendAdapter.setmFooterLisener(new OnFooterClick() {
            @Override
            public void clickLisener(int secton, MovieRecommend.DataBean.LiveBean.ListBean bean) {
                if (bean == null) {
                    XGIMILOG.D("bean == null");
                    EventBus.getDefault().post(new SelecterBean(secton));
                } else {
                    XGIMILOG.D(" bean.urlTypeAction = " + bean.urlTypeAction);
                    if (bean.urlTypeAction.equals("1")) {

                    } else if (bean.urlTypeAction.equals("3")) {
                        Intent itent = new Intent(getActivity(), XgimiCommunityActivity.class);
                        itent.putExtra("url", bean.url);
                        itent.putExtra("name", bean.title);
                        startActivity(itent);
                    } else if (bean.urlTypeAction.equals("4")) {
                        Intent itent = new Intent(getActivity(), WebHtmlActivity.class);
                        itent.putExtra("url", bean.url);
                        startActivity(itent);
                    } else if (bean.urlTypeAction.equals("5")) {
                        if (Constant.netStatus) {
                            if (ToosUtil.getInstance().isInstallTvControl(getActivity())) {
                                mPlyLists.clear();
                                VcontrolCmd.CustomPlay.PlayList mPlayList = new VcontrolCmd.CustomPlay.PlayList(null, null, null, bean.title, null, bean.url, null);
                                mPlyLists.add(mPlayList);
                                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                                        GMSdkCheck.appId, null,
                                        new VcontrolCmd.CustomPlay(0, 0, null, mPlyLists, 0),
                                        null, null, null)));
                            }
                        } else {
                            ToosUtil.getInstance().isConnectTv(getActivity());
                        }
                    } else if (bean.urlTypeAction.equals("6")) {
                        if (Constant.netStatus) {
                            if (ToosUtil.getInstance().isInstallTvControl(getActivity())) {
                                isInstall = false;
                                for (int i = 0; i < ToosUtil.getInstance().mInstallPacageNames.size(); i++) {
                                    if (bean.channel.gm_intent.p.equals(ToosUtil.getInstance().mInstallPacageNames.get(i))) {
                                        isInstall = true;
                                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "11", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(bean.channel.gm_intent.n, null, bean.channel.gm_intent.u, bean.channel.gm_intent.p, bean.channel.gm_intent.gm_is.i
                                                , bean.channel.gm_intent.gm_is.m), null, null, null, null)));
                                        Toast.makeText(getActivity(), "正在无屏电视上打开", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (!isInstall) {
                                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "11", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(bean.channel.gm_intent.n, null, bean.channel.gm_intent.u, bean.channel.gm_intent.p, bean.channel.gm_intent.gm_is.i
                                            , bean.channel.gm_intent.gm_is.m), null, null, null, null)));
                                    if (!isInstalll) {
                                        Toast.makeText(getActivity(), "正在无屏电视上安装电视家", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "正在无屏电视上打开", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            ToosUtil.getInstance().isConnectTv(getActivity());
                        }
                    }
                }
            }
        });
    }

    //读取缓存本地数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson("MovieRecommend");
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            MovieRecommend mDatas = new Gson().fromJson(readHomeJson, MovieRecommend.class);
            loadHome(mDatas);
        } else {
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                net_connect.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }

        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            if (StringUtils.isEmpty(readHomeJson)) {
                showDilog("加载中...");
            }
            if (SaveData.getInstance().deviceMac != null) {
                initData(SaveData.getInstance().deviceMac);
            } else {
                initData("");
            }
        }
    }

    private void loadHome(MovieRecommend mDatas) {
        mRecommendAdapter.dataChange(mDatas);
        net_connect.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        load_false.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void initData(String mac) {
        XGIMILOG.D("获取首页视频推荐");

        try {
            Api.getXgimiVideoApi(MyAesToGsonFactory.create(MovieRecommend.class))
                    .getMovieRecmmendTest(Api.getEncodeParam(new String[]{"mac"}, new String[]{mac}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Observer<MovieRecommend> observer = new Observer<MovieRecommend>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            MissDilog();
            XGIMILOG.D("获取首页出错 : " + e.getMessage());
            e.printStackTrace();
            if (HttpUrl.isNetworkConnected(getActivity())) {
                load_false.setVisibility(View.VISIBLE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onNext(MovieRecommend channels) {
            XGIMILOG.D("获取首页数据完成");
            MissDilog();
            if (channels != null && channels.getData() != null) {
                String movie = new Gson().toJson(channels);
                app.savaHomeJson("MovieRecommend", movie);
                loadHome(channels);
            } else {
                XGIMILOG.D("获取首页数据为空");
                if (HttpUrl.isNetworkConnected(getActivity())) {
                    load_false.setVisibility(View.VISIBLE);
                }
                net_connect.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onClick(View v) {
    }

    String mac = "";

    @Override
    public void onRefresh() {
        if (SaveData.getInstance().deviceMac != null) {
            initData(SaveData.getInstance().deviceMac);
        } else {
            initData("");
        }
        FilmFragment.getInstance().checkData();
    }

    public boolean isInstalll;

    //连接后更新页面。统计那儿的更懂你专题
    public void onEventMainThread(GMDevice info) {
//        Toast.makeText(getActivity(),"eventbus来了",Toast.LENGTH_SHORT).show();
        if (SaveData.getInstance().deviceMac != null) {
//            Toast.makeText(getActivity(),"成功"+SaveData.getInstance().deviceMac,Toast.LENGTH_SHORT).show();
            initData(SaveData.getInstance().deviceMac);
        } else {
//            Toast.makeText(getActivity(),"失败了",Toast.LENGTH_SHORT).show();
            initData("");
        }
    }

    public void onEventMainThread(FeedbackInfo info) {
        if (info.installInfo != null && info.installInfo.stat == 1) {
            if (info.installInfo.packagename.equals("com.elinkway.tvlive2")) {
                isInstalll = true;
            }
        }
    }
//    public void onEventMainThread(GMDevice gmDevice) {
//
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
