package com.xgimi.zhushou.fragment.applyfragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.ApplyDetailActivity;
import com.xgimi.zhushou.adapter.ApplyYingYongAdapter;
import com.xgimi.zhushou.bean.ApplyFeilei;
import com.xgimi.zhushou.bean.ApplySearc;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ApplyYingYongragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public ApplyYingYongAdapter mAdapter;
    private ListView mListview;
    private Subscription subscription1;
    private View net_connect;
    private AnimationDrawable animationDrawable;
    private View load_false;
    private View prog;
    private SwipeRefreshLayout mSrf;


    public ApplyYingYongragment() {
        // Required empty public constructor
    }

    private ApplySearc mData = new ApplySearc();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_ying_yongragment, container, false);
        initView(view);
        getFenLei();
        return view;
    }

    private boolean isFirst;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && !isFirst) {
            prog.setVisibility(View.VISIBLE);
            initLocal();
            isFirst = true;

        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initView(View view) {
        EventBus.getDefault().register(this);
        mSrf = (SwipeRefreshLayout) view.findViewById(R.id.srf_application_all_fragment);
        mSrf.setOnRefreshListener(this);
        mListview = (ListView) view.findViewById(R.id.listview);
        mAdapter = new ApplyYingYongAdapter(getActivity(), mData);
        mListview.setAdapter(mAdapter);
        mListview.setFocusable(false);
        load_false = view.findViewById(R.id.load_false);
        prog = view.findViewById(R.id.myprog);
        prog.setVisibility(View.VISIBLE);
//        LinearLayout textView= (LinearLayout) view.findViewById(R.id.more);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ApplyDetailActivity.class);
                intent.putExtra("id", mAdapter.getItem(position).id);
                ToosUtil.getInstance().addEventUmeng(getActivity(), "event_app_list");
                startActivity(intent);
            }
        });

        net_connect = view.findViewById(R.id.netconnect);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpUrl.isNetworkConnected(getActivity())) {
                    net_connect.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);
                    initData();
                }
            }
        });
    }

    //读取缓存本地数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson("ApplySearc");
        if (!StringUtils.isEmpty(readHomeJson)) {
            ApplySearc mDatas = new Gson().fromJson(readHomeJson, ApplySearc.class);
            loadHome(mDatas);
        } else {
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                net_connect.setVisibility(View.VISIBLE);
                prog.setVisibility(View.GONE);
                MissDilog();
            }
        }
        String readHomeJson1 = app.readHomeJson("applyfeilei");
        if (!StringUtils.isEmpty(readHomeJson1)) {
            ApplyFeilei mDatas = new Gson().fromJson(readHomeJson1, ApplyFeilei.class);
            loadHome1(mDatas);
        } else {
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                //    net_connect.setVisibility(View.VISIBLE);
            }
        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            initData();
        }
    }

    private void loadHome1(ApplyFeilei channels) {
        SaveTVApp.getInstance(getActivity()).feilei = channels;
        net_connect.setVisibility(View.GONE);
    }

    private void loadHome(ApplySearc channels) {
        mAdapter.dataChange(channels);
        MissDilog();
        prog.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);
        load_false.setVisibility(View.GONE);
    }

    Observer<ApplySearc> observer = new Observer<ApplySearc>() {

        @Override
        public void onCompleted() {
            mSrf.setRefreshing(false);
            unsubscribe(subscription);
            MissDilog();
            prog.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            mSrf.setRefreshing(false);
            e.printStackTrace();
//            load_false.setVisibility(View.VISIBLE);
//            mListview.setVisibility(View.GONE);
            prog.setVisibility(View.GONE);
            MissDilog();
        }

        @Override
        public void onNext(ApplySearc channels) {
            mSrf.setRefreshing(false);
            if (channels != null && channels.data != null) {
                App app = (App) getActivity().getApplicationContext();
                String ApplySearc = new Gson().toJson(channels);
                app.savaHomeJson("ApplySearc", ApplySearc);
                mListview.setVisibility(View.VISIBLE);
                load_false.setVisibility(View.GONE);
                loadHome(channels);
            } else {
//                load_false.setVisibility(View.VISIBLE);
            }
            MissDilog();
        }

    };

    Observer<ApplyFeilei> observer1 = new Observer<ApplyFeilei>() {

        @Override
        public void onCompleted() {
            mSrf.setRefreshing(false);
            unsubscribe(subscription1);
            prog.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            mSrf.setRefreshing(false);
            XGIMILOG.E("");
            e.printStackTrace();
            MissDilog();
            prog.setVisibility(View.GONE);
        }

        @Override
        public void onNext(ApplyFeilei channels) {
            if (channels != null && channels.data != null) {
                App app = (App) getActivity().getApplicationContext();
                String ApplyFeilei = new Gson().toJson(channels);
                app.savaHomeJson("applyfeilei", ApplyFeilei);
                loadHome1(channels);
                prog.setVisibility(View.GONE);
            }
        }
    };

    private void initData() {
        subscription = Api.getMangoApi().getApply("0", "全部", null, 20, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //返回的下载进度
    public void onEventMainThread(FeedbackInfo infor) {
        mAdapter.setText(infor);
    }

    private void getFenLei() {
        subscription1 = Api.getMangoApi().getAppClass()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        initData();
        getFenLei();
    }
}
