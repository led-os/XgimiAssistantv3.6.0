package com.xgimi.zhushou.fragment.livefragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.WebHtmlActivity;
import com.xgimi.zhushou.adapter.FindUrlAdapter;
import com.xgimi.zhushou.adapter.LiveUsualAdapter;
import com.xgimi.zhushou.bean.UrlBean;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LiveFindFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private FindUrlAdapter mAdapter;
    public UrlBean data = new UrlBean();
    private LiveUsualAdapter mGridAdapter;
    private View net_connect;
    private AnimationDrawable animationDrawable;
    private RelativeLayout ll;
    private View close;
    private View load_false;
    private LinearLayout ll_live;
    private ScrollView scrollView;
    private View prog;

    public LiveFindFragment() {
        // Required empty public constructor
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initLocal();

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live_find, container, false);

        initView(view);
//        initLocal();
        //  showDilog("加载中...");

        handler.sendEmptyMessageDelayed(0, 200);

        return view;
    }

    //读取缓存本地数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson("UrlBean");
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            UrlBean mDatas = new Gson().fromJson(readHomeJson, UrlBean.class);
            loadHome(mDatas);
            scrollView.setVisibility(View.VISIBLE);
            MissDilog();
            prog.setVisibility(View.GONE);
        } else {
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                net_connect.setVisibility(View.VISIBLE);
                ll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                MissDilog();
                ll_live.setVisibility(View.GONE);
            }

        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            initData();
        }
    }

    private void loadHome(UrlBean channels) {
        data = channels;
        if (channels.data.recommend != null) {
            mGridAdapter.dataChange(channels);
            ll.setVisibility(View.VISIBLE);
            load_false.setVisibility(View.GONE);
        } else {
            ll.setVisibility(View.GONE);
        }
        if (channels.data.game_live != null) {
            mAdapter.dataChange(channels);
            load_false.setVisibility(View.GONE);
            ll_live.setVisibility(View.VISIBLE);
        } else {
            ll_live.setVisibility(View.GONE);
        }
        MissDilog();
        prog.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);
        if (HttpUrl.isNetworkConnected(getActivity())) {
            if (channels.code == 4444) {
                ll.setVisibility(View.GONE);
                ll_live.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
                net_connect.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            int a = SaveData.getInstance().channel;
            if (SaveData.getInstance().channel == 0) {
                ll.setVisibility(View.GONE);
            } else {
                ll.setVisibility(View.VISIBLE);
            }
            int b = SaveData.getInstance().gameLive;
            if (SaveData.getInstance().gameLive == 0) {
                ll_live.setVisibility(View.GONE);
            } else {
                ll_live.setVisibility(View.VISIBLE);
            }
            if (SaveData.getInstance().channel == 0 && SaveData.getInstance().gameLive == 0) {
                ll.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                ll_live.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
                load_false.setVisibility(View.GONE);
                net_connect.setVisibility(View.GONE);
            }
        }

    }

    private boolean isInstall;

    private void initView(View view) {
        scrollView = (ScrollView) view.findViewById(R.id.scrollview);
        scrollView.setVisibility(View.GONE);
        TextView mChannel = (TextView) view.findViewById(R.id.pindao).findViewById(R.id.tv_movie);
        mChannel.setText("常用频道");
        TextView mLive = (TextView) view.findViewById(R.id.zhibos).findViewById(R.id.tv_movie);
        mLive.setText("精彩直播");
        ll = (RelativeLayout) view.findViewById(R.id.rl_ll);
        ll_live = (LinearLayout) view.findViewById(R.id.ll_live);
        GridView mGridView = (GridView) view.findViewById(R.id.gridview);
        mGridAdapter = new LiveUsualAdapter(getActivity(), data);
        mGridView.setAdapter(mGridAdapter);
        close = view.findViewById(R.id.close);
        load_false = view.findViewById(R.id.load_false);
        ListView listView = (ListView) view.findViewById(R.id.listview);
        mAdapter = new FindUrlAdapter(getActivity(), data);
        prog = view.findViewById(R.id.myprog);
        prog.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Constant.netStatus) {
                    if (ToosUtil.getInstance().isInstallTvControl(getActivity())) {

                        ToosUtil.getInstance().addEventUmeng(getActivity(), "event_live" + (position + 1));
                        ToosUtil.getInstance().addEventUmeng(getActivity(), "event_live_play");
                        Intent intent = new Intent(getActivity(), WebHtmlActivity.class);
//                        intent.putExtra("url", "http://m.iqiyi.com");
                        intent.putExtra("url", mAdapter.getItem(position).getUrl());
                        startActivity(intent);
                    }
                } else {
                    ToosUtil.getInstance().isConnectTv(getActivity());
                }
            }
        });
        listView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
                isInstall = false;
                if (Constant.netStatus) {
                    if (ToosUtil.getInstance().isInstallTvControl(getActivity())) {

                        for (int i = 0; i < ToosUtil.getInstance().mInstallPacageNames.size(); i++) {
                            if (data.data.recommend.get(position).gm_intent.p.equals(ToosUtil.getInstance().mInstallPacageNames.get(i))) {
                                isInstall = true;
                                ToosUtil.getInstance().addEventUmeng(getActivity(), "event_usual_channel_play");
                                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "11", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(data.data.recommend.get(position).gm_intent.n, null, data.data.recommend.get(position).gm_intent.u, data.data.recommend.get(position).gm_intent.p, data.data.recommend.get(position).gm_intent.gm_is.i
                                        , data.data.recommend.get(position).gm_intent.gm_is.m), null, null, null, null)));
                            }
                        }
                        if (isInstall) {
                            Toast.makeText(getActivity(), "正在无屏电视上打开", Toast.LENGTH_SHORT).show();
                        } else {
                            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "11", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(data.data.recommend.get(position).gm_intent.n, null, data.data.recommend.get(position).gm_intent.u, data.data.recommend.get(position).gm_intent.p, data.data.recommend.get(position).gm_intent.gm_is.i
                                    , data.data.recommend.get(position).gm_intent.gm_is.m), null, null, null, null)));
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
                    ll_live.setVisibility(View.GONE);
                    net_connect.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);
                    initData();
                }
            }
        });
    }

    private void initData() {
        XGIMILOG.E("获取频道1");
        subscription = Api.getMangoApi().getFindLive()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<UrlBean> observer = new Observer<UrlBean>() {
        @Override
        public void onCompleted() {
            MissDilog();
            prog.setVisibility(View.GONE);
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            MissDilog();
            prog.setVisibility(View.GONE);
        }

        @Override
        public void onNext(UrlBean channels) {
            XGIMILOG.E("获取频道1成功");
//            LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(channels));
            if (channels != null && channels.data != null) {
                App app = (App) getActivity().getApplicationContext();
                String livefind = new Gson().toJson(channels);
                app.savaHomeJson("UrlBean", livefind);
                loadHome(channels);
                scrollView.setVisibility(View.VISIBLE);
                MissDilog();
                prog.setVisibility(View.GONE);
            } else if (channels == null || channels.data == null) {
                ll.setVisibility(View.GONE);
                ll_live.setVisibility(View.GONE);
                load_false.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        initData();
    }


}
