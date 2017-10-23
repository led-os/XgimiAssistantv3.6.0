package com.xgimi.zhushou.fragment.livefragment;

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
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.AllChannelAdapter;
import com.xgimi.zhushou.adapter.RecommendViewHolder.AllChannelDetailAdapter;
import com.xgimi.zhushou.bean.AllChannel;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LiveAllChannelFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.left_recylerview)
    RecyclerView mLeftRecylerview;
    @Bind(R.id.right_recylerview)
    RecyclerView mRightRecylerview;
    @Bind(R.id.rl_ll)
    RelativeLayout mRelativieLayout;
    private LinearLayoutManager mLayoutManager;
    private AllChannelDetailAdapter detailAdapter;
    HashMap<Integer, Integer> hasMaps = new HashMap<>();
    private AllChannelAdapter mAdapter;
    private View net_connect;
    private AnimationDrawable animationDrawable;
    private RelativeLayout ll;
    private View close;
    private View load_false;
    private View prog;
    private View mView;
    private SmartRefreshLayout mRefreshLayout;


    private static LiveAllChannelFragment mInstance;


    private OnRefreshListener mRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            getAllChannel();
        }
    };

    public static LiveAllChannelFragment getInstance() {
        if (mInstance == null) {
            mInstance = new LiveAllChannelFragment();
        }
        return mInstance;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initLocal();
        }
    };


    public LiveAllChannelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_live_all_channel, container, false);
            ButterKnife.bind(this, mView);
            initView(mView);
            mRelativieLayout.setVisibility(View.GONE);
            handler.sendEmptyMessageDelayed(0, 200);
        }
//        View view = inflater.inflate(R.layout.fragment_live_all_channel, container, false);
//        ButterKnife.bind(this, view);
//        initView(view);
//        mRelativieLayout.setVisibility(View.GONE);
//        handler.sendEmptyMessageDelayed(0, 200);
        return mView;
    }

    private boolean isFirst;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFirst) {
            isFirst = true;
        }
    }

    private boolean isInstall;

    private void initView(View view) {
        mRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.srf_live_fragment);
        mRefreshLayout.setOnRefreshListener(mRefreshListener);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new AllChannelAdapter(getActivity(), mdatas);
        mLeftRecylerview.setAdapter(mAdapter);
        mLeftRecylerview.setLayoutManager(mLayoutManager);
        ll = (RelativeLayout) view.findViewById(R.id.rl_ll);
        close = view.findViewById(R.id.close);
        load_false = view.findViewById(R.id.load_false);
        detailAdapter = new AllChannelDetailAdapter(getActivity(), mDetaisData, hasMaps);
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        mRightRecylerview.setAdapter(detailAdapter);
        mRightRecylerview.setLayoutManager(mLayoutManager1);
        mRightRecylerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        //获取第一个可见view的位置
                        int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                        if (firstItemPosition > hasMaps.get(1) - 1) {
                            mAdapter.dataChane(1);
                        } else {
                            mAdapter.dataChane(0);
                        }
                    }
                }
            }
        });
        prog = view.findViewById(R.id.myprog);
        prog.setVisibility(View.VISIBLE);
        mAdapter.setOnitemLiener(new AllChannelAdapter.OnItemClicLisener() {
            @Override
            public void OnItemLisener(int i) {
                if (i != 0) {
                    mRightRecylerview.scrollToPosition(hasMaps.get(i) + 5);
                } else {
                    mRightRecylerview.scrollToPosition(hasMaps.get(i));
                }

                ToosUtil.getInstance().addEventUmeng(getActivity(), "event_live");
            }
        });
        detailAdapter.setLisener(new AllChannelDetailAdapter.onItemClickLisener() {
            @Override
            public void onItemClickLisener(AllChannel.DataBean.ChannelsBean object, int postion) {
                isInstall = false;
                ToosUtil.getInstance().addEventUmeng(getActivity(), "event_channel_play");
                detailAdapter.changePosition(postion);
                if (Constant.netStatus) {
                    if (ToosUtil.getInstance().isInstallTvControl(getActivity())) {
                        for (int i = 0; i < ToosUtil.getInstance().mInstallPacageNames.size(); i++) {
                            if (object.gm_intent.p.equals(ToosUtil.getInstance().mInstallPacageNames.get(i))) {
                                isInstall = true;
                                VcontrolControl.getInstance().sendNewCommand(
                                        VcontrolControl.getInstance().getConnectControl(
                                                new VcontrolCmd(
                                                        30000,
                                                        "11",
                                                        GMSdkCheck.appId,
                                                        DeviceUtils.getappVersion(getActivity()),
                                                        App.getContext().PACKAGENAME,
                                                        new VcontrolCmd.ThirdPlay(
                                                                object.gm_intent.n, null, object.gm_intent.u,
                                                                object.gm_intent.p, object.gm_intent.gm_is.i
                                                                , object.gm_intent.gm_is.m,
                                                                mDetaisData.get(postion).name,
                                                                mDetaisData.get(postion).id
                                                        ),
                                                        null, null, null, null)
                                        )

                                );
                                Toast.makeText(getActivity(), "正在无屏电视上打开", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (!isInstall) {
                            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "11", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(object.gm_intent.n, null, object.gm_intent.u,
                                    object.gm_intent.p, object.gm_intent.gm_is.i
                                    , object.gm_intent.gm_is.m), null, null, null, null)));
                            if (!SaveData.getInstance().mIsInstall)
                                Toast.makeText(getActivity(), "正在无屏电视上安装", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(getActivity(), "正在无屏电视上打开", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    ToosUtil.getInstance().isConnectTv(getActivity());
                }
            }
        });
        net_connect = view.findViewById(R.id.netconnect);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpUrl.isNetworkConnected(getActivity())) {
                    ll.setVisibility(View.GONE);
                    net_connect.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);
                    close.setVisibility(View.GONE);
                    getAllChannel();
                }
            }
        });
    }

    //读取缓存本地数据
    private void initLocal() {

        String readHomeJson = app.readHomeJson("allchannel");

        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            AllChannel mDatas = new Gson().fromJson(readHomeJson, AllChannel.class);
            loadHome(mDatas);
            mRelativieLayout.setVisibility(View.VISIBLE);
            prog.setVisibility(View.GONE);
        } else {
            XGIMILOG.E("");
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                net_connect.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
            } else {
                getAllChannel();
            }
        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
//            getAllChannel();
            if (SaveData.getInstance().channel == 0 && SaveData.getInstance().gameLive == 0) {
                ll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
//                ll_live.setVisibility(View.GONE);
                XGIMILOG.E("close.setVisibility(View.VISIBLE)");
                close.setVisibility(View.VISIBLE);
                load_false.setVisibility(View.GONE);
                net_connect.setVisibility(View.GONE);
            }
        }
    }

    private void loadHome(AllChannel channels) {
        if (channels != null && channels.data != null) {
            mDetaisData.clear();
            hasMaps.clear();
            num = 0;
            mAdapter.dataChange(channels.data);
            for (int i = 0; i < channels.data.size(); i++) {
                for (int j = 0; j < channels.data.get(i).channels.size(); j++) {
                    AllChannel.DataBean.ChannelsBean beans = channels.data.get(i).channels.get(j);
                    mDetaisData.add(beans);
                }
                hasMaps.put(i, num);
                num += channels.data.get(i).channels.size();
            }
            detailAdapter.dataChange(mDetaisData);
            ll.setVisibility(View.VISIBLE);
            net_connect.setVisibility(View.GONE);
        }
        //  detailAdapter.dataChange(mDetaisData);
        ll.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
        load_false.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        if (HttpUrl.isNetworkConnected(getActivity())) {
            if (channels.code == 4444) {
                XGIMILOG.E("close.setVisibility(View.VISIBLE)");
                close.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
                net_connect.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            if (SaveData.getInstance().channel == 0) {
                XGIMILOG.E("close.setVisibility(View.VISIBLE)");
                close.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
                load_false.setVisibility(View.GONE);
                net_connect.setVisibility(View.GONE);
            }
        }

    }

    private List<AllChannel.DataBean> mdatas = new ArrayList<>();
    private List<AllChannel.DataBean.ChannelsBean> mDetaisData = new ArrayList<>();
    private int num;
    Observer<AllChannel> observer = new Observer<AllChannel>() {
        @Override
        public void onCompleted() {
            mRefreshLayout.finishRefresh();
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            mRefreshLayout.finishRefresh();
            prog.setVisibility(View.GONE);
            e.printStackTrace();
            load_false.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNext(AllChannel channels) {
            XGIMILOG.E("获取频道2成功");
//            LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(channels));
            prog.setVisibility(View.GONE);
            if (channels != null && channels.data != null) {
                App app = (App) getActivity().getApplicationContext();
                String livefind = new Gson().toJson(channels);
                app.savaHomeJson("allchannel", livefind);
                loadHome(channels);
            } else {
                ll.setVisibility(View.GONE);
                load_false.setVisibility(View.VISIBLE);
            }
            mRelativieLayout.setVisibility(View.VISIBLE);
            prog.setVisibility(View.GONE);
        }

    };

    private void getAllChannel() {
        XGIMILOG.E("获取频道2");
        subscription = Api.getMangoApi().getLives()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        getAllChannel();
    }

    public boolean isInstalll;

    public void onEventMainThread(FeedbackInfo info) {
        if (info.installInfo != null && info.installInfo.stat == 1) {
            if (info.installInfo.packagename.equals("com.elinkway.tvlive2")) {
                isInstalll = true;
            }
        }
    }

}