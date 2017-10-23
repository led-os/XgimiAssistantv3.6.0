package com.xgimi.zhushou.fragment.applyfragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.AppManagerActivity;
import com.xgimi.zhushou.activity.ApplyDetailActivity;
import com.xgimi.zhushou.activity.RemountActivity;
import com.xgimi.zhushou.adapter.ApplyRecommendAdapter;
import com.xgimi.zhushou.bean.ApplyDetail;
import com.xgimi.zhushou.bean.ApplyHomeBean;
import com.xgimi.zhushou.bean.ApplyIndexs;
import com.xgimi.zhushou.bean.TvApp;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.inter.ApplyFooterCLick;
import com.xgimi.zhushou.inter.OnApplyItemClick;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApplyRecomendFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ApplyRecomendFragment";

    private Subscription subscription;
    @Bind(R.id.recyleview)
    RecyclerView mRecylRecyclerView;
    private ApplyRecommendAdapter applyRecommendAdapter;
    private View net_connect;
    private AnimationDrawable animationDrawable;
    String url = "http://%s:16741/data/data/com.xgimi.vcontrol/app_appDatas/%s";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View load_false;
    private View prog;
    ApplyHomeBean mDatas = new ApplyHomeBean();

    public ApplyRecomendFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_recomend, container, false);
        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView: ");
        initView(view);
        initLocal();
        initData();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            MissDilog();
        }
    }

    private void initView(View view) {
        Log.d(TAG, "initView: ");
        EventBus.getDefault().register(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        applyRecommendAdapter = new ApplyRecommendAdapter(getActivity(), mDatas, mRecylRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecylRecyclerView.setAdapter(applyRecommendAdapter);
        mRecylRecyclerView.setLayoutManager(linearLayoutManager);
        net_connect = view.findViewById(R.id.netconnect);
        load_false = view.findViewById(R.id.load_false);
        prog = view.findViewById(R.id.myprog);
        prog.setVisibility(View.VISIBLE);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpUrl.isNetworkConnected(getActivity())) {
                    mRecylRecyclerView.setVisibility(View.VISIBLE);
                    net_connect.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);
                    getRecomend();
                }
            }
        });
    }

    //读取缓存本地数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson("ApplyHomeBean");
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            ApplyHomeBean mDatas = new Gson().fromJson(readHomeJson, ApplyHomeBean.class);
            Log.d(TAG, "initLocal: " + mDatas.toString());
            loadHome(mDatas);
        } else {
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                net_connect.setVisibility(View.VISIBLE);
                prog.setVisibility(View.GONE);
                mRecylRecyclerView.setVisibility(View.GONE);
                MissDilog();
            }

        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            getRecomend();
        }
    }

    private void loadHome(ApplyHomeBean Datas) {
        mDatas = Datas;
        if (SaveTVApp.getInstance(getActivity()).mTvApp != null) {
            ApplyHomeBean.DataBean.SubjectBean sb = new ApplyHomeBean.DataBean.SubjectBean();
            ArrayList<ApplyHomeBean.DataBean.SubjectBean.ContentBean> contentBeens = new ArrayList<>();
            for (int i = 0; i < SaveTVApp.getInstance(getActivity()).mTvApp.appList.size(); i++) {
                ApplyHomeBean.DataBean.SubjectBean.ContentBean contentBean = new ApplyHomeBean.DataBean.SubjectBean.ContentBean();
                TvApp.Appitem item = SaveTVApp.getInstance(getActivity()).mTvApp.appList.get(i);
                Log.d(TAG, "loadHome: " + item.packageName);
                contentBean.setName(item.appName);
                contentBean.setPacagename(item.packageName);
                contentBean.setApp_id("222");
                String icon = String.format(url, GMDeviceStorage.getInstance().getConnectedDevice().getIp(), item.packageName + ".xgimi");
                contentBean.setIcon(icon);
                contentBeens.add(contentBean);
            }
            sb.setTitle("已安装应用");
            sb.setId("22");
            sb.setContent(contentBeens);
            mDatas.data.subject.add(0, sb);
            applyRecommendAdapter.dataChange(mDatas, 1);
        } else {
            applyRecommendAdapter.dataChange(mDatas, 2);
        }
        net_connect.setVisibility(View.GONE);
        prog.setVisibility(View.GONE);
        mRecylRecyclerView.setVisibility(View.VISIBLE);
        load_false.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void initData() {
        applyRecommendAdapter.setmFooterClickLisener(new ApplyFooterCLick() {
            @Override
            public void OnFooterClickLisener(int section, int postion) {
                if (section == 1 && mDatas != null && mDatas.data != null && mDatas.data.subject != null && mDatas.data.subject.size() > 0 && mDatas.data.subject.get(0).getId().equals("22")) {
                    Intent intent = new Intent(getActivity(), AppManagerActivity.class);
                    startActivity(intent);

                } else {
                    if (section == mDatas.data.subject.size() + 1) {
                        EventBus.getDefault().post(new ApplyIndexs(1));
                    } else if (section == mDatas.data.subject.size() + 2) {
                        EventBus.getDefault().post(new ApplyIndexs(2));
                    }
                }
            }
        });
        applyRecommendAdapter.setmOnitemClick(new OnApplyItemClick() {
            @Override
            public void onItemClickLisene(String packagename, String id, int section, int position) {
                Log.d(TAG, "onItemClickLisene: " + packagename);
                if (position == 1 && section == 1) {
//                    GMAppController.getInstance().openApp(packagename);
                    if (ToosUtil.getInstance().isInstallTvControl(getActivity())) {
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                null, new VcontrolCmd.ControlCmd(7, 1, 0, packagename, null, null), null, null)));
                        Intent intent = new Intent(getActivity(), RemountActivity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "正在电视打开", Toast.LENGTH_SHORT).show();

                        Intent intent1 = new Intent(getContext(), RemountActivity.class);
                        getContext().startActivity(intent1);
                    }
                } else {
                    Intent intent = new Intent(getActivity(), ApplyDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            }
        });
    }


    long startTime = 0;

    //获取所有的分类
    private void getRecomend() {
        startTime = System.currentTimeMillis();

        subscription = Api.getMangoApi().getApplyRecomend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<ApplyHomeBean> observer = new Observer<ApplyHomeBean>() {


        @Override
        public void onCompleted() {
            unsubscribe(subscription);
            mSwipeRefreshLayout.setRefreshing(false);
            prog.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            XGIMILOG.E("applyRecommend");
            e.printStackTrace();
//            load_false.setVisibility(View.VISIBLE);
            prog.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
//            mRecylRecyclerView.setVisibility(View.GONE);
        }

        @Override
        public void onNext(ApplyHomeBean channels) {
            Log.e("info", "spare :" + (System.currentTimeMillis() - startTime));
            if (channels != null && channels.data != null) {
                App app = (App) getActivity().getApplicationContext();
                String allapp = new Gson().toJson(channels);
                app.savaHomeJson("ApplyHomeBean", allapp);
                loadHome(channels);
                //  mHandler.sendEmptyMessage(0);
            } else {
                XGIMILOG.E("");
                load_false.setVisibility(View.VISIBLE);
                prog.setVisibility(View.GONE);
                mRecylRecyclerView.setVisibility(View.GONE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
            prog.setVisibility(View.GONE);
        }
    };


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        getRecomend();
    }

    public void onEventMainThread(ApplyDetail applyDetail) {

        if (Constant.netStatus) {

            if (GMDeviceStorage.getInstance().getConnectedDevice() != null) {
                SaveTVApp.getInstance(getActivity()).getApp(GMDeviceStorage.getInstance().getConnectedDevice().getIp());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
