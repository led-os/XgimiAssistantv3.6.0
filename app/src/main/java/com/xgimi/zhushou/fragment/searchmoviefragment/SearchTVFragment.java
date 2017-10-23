package com.xgimi.zhushou.fragment.searchmoviefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.FilmDetailActivity;
import com.xgimi.zhushou.activity.RemountActivity;
import com.xgimi.zhushou.adapter.SearchMovieAdapter;
import com.xgimi.zhushou.bean.FilmDetailBean;
import com.xgimi.zhushou.bean.SearchMovieResult;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/19.
 */
public class SearchTVFragment extends BaseFragment {
    public SearchTVFragment() {

    }

    private SearchMovieAdapter adapter;
    private RecyclerView mrecyclerView;
    private LinearLayoutManager mLayoutManager;
    private View view;
    private TextView mNoResourceTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search_tv, container, false);
            initView(view);
        }
        return view;
    }

    private void onEventMainThread(SearchMovieResult searchMovieResult) {
        boFang();
    }

    private void initView(View view) {
        mNoResourceTv = (TextView) view.findViewById(R.id.tv_no_resource_search_tv);
        adapter = new SearchMovieAdapter(getActivity(), SaveData.getInstance().searchmovie, SaveData.getInstance().dsj);
        mrecyclerView = (RecyclerView) view.findViewById(R.id.detail_recylerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mrecyclerView.setAdapter(adapter);
        mrecyclerView.setLayoutManager(mLayoutManager);
        adapter.setLisener(new SearchMovieAdapter.OnitemClick() {
            @Override
            public void onItemCliceLisener(String id, String type) {
                if (type.equals("item")) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), FilmDetailActivity.class);
                    intent.putExtra("id", id);
                    SaveData.getInstance().mSouceInsight = "4";
                    SaveData.getInstance().mSoucePage = null;
                    SaveData.getInstance().mSourceInsightLocation = null;
                    startActivity(intent);
                }
            }
        });
    }

    private void boFang() {
        subscription = Api.getMangoApi().getMovieDetail(SaveData.getInstance().movieSearchId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<FilmDetailBean> observer = new Observer<FilmDetailBean>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(FilmDetailBean filmDetailBean) {
            liJiBoFang(filmDetailBean);
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }

    private boolean isInstall = true;
    private boolean mIsInstall = true;

    public void liJiBoFang(FilmDetailBean mDatas) {
        if (mDatas != null) {
            //  isInstall=false;
            if (Constant.netStatus) {
                if (ToosUtil.getInstance().isInstallTvControl(getActivity())) {
                    ToosUtil.getInstance().addEventUmeng(getActivity(), "event_movie_play");
                    if (mDatas.data != null && mDatas.data.source != null && mDatas.data.source.size() > 0) {
                        for (int i = 0; i < ToosUtil.getInstance().mInstallPacageNames.size(); i++) {
                            if (mDatas.data.source.get(0).gm_intent.p.equals(ToosUtil.getInstance().mInstallPacageNames.get(i))) {
                                isInstall = true;
                                if (mDatas.data.source.get(0).gm_intent != null && mDatas.data.source.get(0).gm_intent.gm_is != null && mDatas.data.source.get(0).gm_intent.gm_is != null && mDatas.data.source.get(0).gm_intent.gm_is != null) {
                                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(getActivity()), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(mDatas.data.source.get(0).gm_intent.n,
                                            mDatas.data.source.get(0).gm_intent.o,
                                            mDatas.data.source.get(0).gm_intent.u,
                                            mDatas.data.source.get(0).gm_intent.p,
                                            mDatas.data.source.get(0).gm_intent.gm_is.i,
                                            mDatas.data.source.get(0).gm_intent.gm_is.m), null, null, null, null)));
                                }
                                Intent inten = new Intent(getActivity(), RemountActivity.class);
                                startActivity(inten);

                            }
                        }
                        if (isInstall) {
                            Toast.makeText(getActivity(), "正在无屏电视上打开" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                        } else {
                            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(mDatas.data.source.get(0).gm_intent.n,
                                    mDatas.data.source.get(0).gm_intent.o,
                                    mDatas.data.source.get(0).gm_intent.u,
                                    mDatas.data.source.get(0).gm_intent.p,
                                    mDatas.data.source.get(0).gm_intent.gm_is.i,
                                    mDatas.data.source.get(0).gm_intent.gm_is.m), null, null, null, null)));
                            if (mIsInstall) {
                                Toast.makeText(getActivity(), "正在无屏电视上打开" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                                Intent inten = new Intent(getActivity(), RemountActivity.class);
                                startActivity(inten);
                            } else {
                                Toast.makeText(getActivity(), "正在无屏电视上安装" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

            } else {
                ToosUtil.getInstance().isConnectTv(getActivity());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SaveData.getInstance().searchmovie.get(SaveData.getInstance().dsj).list == null
                || SaveData.getInstance().searchmovie.get(SaveData.getInstance().dsj).list.size() == 0) {
            mNoResourceTv.setVisibility(View.VISIBLE);
            mrecyclerView.setVisibility(View.GONE);
        } else {
            mNoResourceTv.setVisibility(View.GONE);
            mrecyclerView.setVisibility(View.VISIBLE);
        }

    }
}
