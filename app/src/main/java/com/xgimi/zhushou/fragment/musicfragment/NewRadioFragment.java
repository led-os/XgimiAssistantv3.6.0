package com.xgimi.zhushou.fragment.musicfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.AllRadioClassActivity;
import com.xgimi.zhushou.activity.RadioDetailActivity;
import com.xgimi.zhushou.adapter.RadioRcvAdapter;
import com.xgimi.zhushou.bean.HotRadio;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.ToastUtil;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XGIMI on 2017/9/13.
 */

public class NewRadioFragment extends BaseFragment implements IRadioFragmentView, OnRefreshListener, OnLoadmoreListener {

    private View mView;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRcv;
    private RadioRcvAdapter mAdapter;
    private Subscription mSubscription;
    private List<HotRadio.data> mDataList = new ArrayList<>();
    private TextView mClassifyTv;
    private int mPage = 1;

    private boolean mJustPullUp;

    private View.OnClickListener mOnClassifyTvClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ToosUtil.getInstance().addEventUmeng(getContext(), "event_music_radio_more_category");
            Intent intent = new Intent(getContext(), AllRadioClassActivity.class);
            getContext().startActivity(intent);
        }
    };

    private Observer<HotRadio> mObs = new Observer<HotRadio>() {

        @Override
        public void onCompleted() {
            if (mJustPullUp) {
                mRefreshLayout.finishLoadmore();
            } else {
                mRefreshLayout.finishRefresh();
            }
        }

        @Override
        public void onError(Throwable e) {
            XGIMILOG.E(e.getMessage());
            if (mJustPullUp) {
                mRefreshLayout.finishLoadmore();
            } else {
                mRefreshLayout.finishRefresh();
            }
            Toast.makeText(getContext(), "网络开小差了，请稍后再试", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(HotRadio hotRadio) {
            XGIMILOG.E("获取电台数据成功");
//            LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(hotRadio));
            if (hotRadio != null && hotRadio.data != null && !"no more data or data is null".equals(hotRadio.message)) {
                if (!mJustPullUp) {
                    app.saveRadio(hotRadio);
                    mDataList.clear();
                }
                mDataList.addAll(hotRadio.data);
                mAdapter.notifyDataSetChanged();
            }

            if (hotRadio != null && mJustPullUp && "no more data or data is null".equals(hotRadio.message)) {
                ToastUtil.getToast("没有更多数据了", getContext()).show();
                mPage--;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_radio_layout, container, false);
            initView();
            initCacheData();
            initData();
        }
        return mView;
    }

    private void initCacheData() {
        HotRadio radio = app.getRadioCache();
        if (radio != null) {
            mDataList.addAll(radio.data);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initData() {
        if (NetUtil.isNetConnected(getContext())) {
            mSubscription = Api.getMangoApi().getHotRadio(mPage).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObs);
        } else {
            ToastUtil.getToast("网络已断开，请稍后再试", getContext()).show();
        }
    }

    private void initView() {
        mClassifyTv = (TextView) mView.findViewById(R.id.tv_radio_home_classify);
        mClassifyTv.setOnClickListener(mOnClassifyTvClickListener);
        mRcv = (RecyclerView) mView.findViewById(R.id.rcv_radio_fragment);
        mRefreshLayout = (SmartRefreshLayout) mView.findViewById(R.id.view_srf_radio_fragment);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadmoreListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mRcv.setLayoutManager(layoutManager);
        mAdapter = new RadioRcvAdapter(this, mDataList);
        mRcv.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mPage = 1;
        mJustPullUp = false;
        initData();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mPage++;
        mJustPullUp = true;
        initData();
    }

    @Override
    public void onRadioItemClick(HotRadio.data data) {
        XGIMILOG.E("" + new Gson().toJson(data));
        Intent intent = new Intent(getContext(), RadioDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("id", data.fm_id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
