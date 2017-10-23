package com.xgimi.zhushou.fragment.musicfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.MVDetailActivity;
import com.xgimi.zhushou.activity.NewMVItemActivity;
import com.xgimi.zhushou.adapter.MVNewAdapter;
import com.xgimi.zhushou.bean.NewMvList;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/13.
 */
public class MVNewFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private View view;
    @Bind(R.id.recyleview)
    RecyclerView mRecyclerView;
    NewMvList mDatas=new NewMvList();
    private View net_connect;
    private MVNewAdapter mvNewAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View load_false;
    private LinearLayoutManager mLayoutManager;
    //统计那儿mv的类型
    private String mvLeixing;
    private Subscription subscription;
    List< VcontrolCmd.CustomPlay.PlayList> mPlyLists=new ArrayList<>();
    public MVNewFragment() {
        // Required empty public constructor
    }
    public static MVNewFragment fragment;
    public static MVNewFragment getInstance(){
        if(fragment==null){
            fragment=new MVNewFragment();
        }
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_mv_new, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initLocal();
        initData();
        initListen();
        return view;
    }

    private void initView(View view) {
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recyleview);
        mvNewAdapter=new MVNewAdapter(getActivity(),mDatas);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(mvNewAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        net_connect = view.findViewById(R.id.netconnect);
        load_false=view.findViewById(R.id.load_false);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HttpUrl.isNetworkConnected(getActivity())){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    net_connect.setVisibility(View.GONE);
                    initData();
                }
            }
        });
    }
    //加入缓存
    private void initLocal() {
        String readHomeJson = App.getContext().readHomeJson("NewMvList");
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            NewMvList mDatas = new Gson().fromJson(readHomeJson, NewMvList.class);
            loadHome(mDatas);
        } else {
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                net_connect.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }

        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            initData();
        }
    }
    //加载数据
    private void loadHome(NewMvList Datas) {
        if(Datas!=null&&Datas.data!=null){
            mDatas=Datas;
            mvNewAdapter.dataChange(Datas);
            net_connect.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            load_false.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }


    }
    private void initListen(){
        mvNewAdapter.setOnClickListener(new MVNewAdapter.OnClickListener() {
            @Override
            public void onClick(int position,int section) {
                Intent intent = new Intent(getActivity(), MVDetailActivity.class);
                intent.putExtra("title",(mDatas.data.get(section)).info.get(position).mv_title);
                intent.putExtra("artist",(mDatas.data.get(section)).info.get(position).mv_artist);
                intent.putExtra("bitmap",(mDatas.data.get(section)).info.get(position).mv_thumb);
                intent.putExtra("mv_play_address",(mDatas.data.get(section)).info.get(position).mv_play_address);
                intent.putExtra("areaid",(mDatas.data.get(section)).area_id);
                intent.putExtra("ivplaytype","true");
                SaveData.getInstance().isPlay=true;
                Bundle bundle = new Bundle();
                bundle.putSerializable("type",1);
                intent.putExtras(bundle);
                SaveData.getInstance().mList=mDatas.data.get(section).info;
                intent.putExtra("collect_id",(mDatas.data.get(section)).info.get(position).collect_id);
                intent.putExtra("mv_id",(mDatas.data.get(section)).info.get(position).mv_id);
                SaveData.getInstance().mv_id=mDatas.data.get(section).info.get(position).mv_id;
                SaveData.getInstance().MV_type=1;
                ToosUtil.getInstance().addEventUmeng(getActivity(),"event_music_mv_new");

                SaveData.getInstance().mv_title=mDatas.data.get(section).info.get(position).mv_title;
                SaveData.getInstance().mv_artist=mDatas.data.get(section).info.get(position).mv_artist;
                SaveData.getInstance().bitmap=mDatas.data.get(section).info.get(position).mv_thumb;
                SaveData.getInstance().mvid=mDatas.data.get(section).info.get(position).mv_id;
                SaveData.getInstance().position=position;
                SaveData.getInstance().mv_play_address=mDatas.data.get(section).info.get(position).mv_play_address;
                SaveData.getInstance().collect_id=mDatas.data.get(section).info.get(position).collect_id;
                startActivity(intent);
                mPlyLists.clear();
                for (int i = 0; i < mDatas.data.get(section).info.size(); i++) {
                    if(mDatas.data.get(section).info.get(i).mv_type_name!=null){
                        mvLeixing=mDatas.data.get(section).info.get(i).mv_type_name;
                    }else {
                        mvLeixing="";
                    }
                    VcontrolCmd.CustomPlay.PlayList mPlayList=new VcontrolCmd.CustomPlay.PlayList(null,mDatas.data.get(section).info.get(i).mv_id,null,mDatas.data.get(section).info.get(i).mv_title,null,mDatas.data.get(section).info.get(i).mv_play_address,null
                            ,0,null,App.getContext().YINYUETAI);
                    mPlyLists.add(mPlayList);
                }
                Log.e("MVtongji",mDatas.data.get(section).info.get(position).mv_title+"--"+mDatas.data.get(section).info.get(position).mv_id+"--"+mDatas.data.get(section).area_name);
                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,"2",
                        GMSdkCheck.appId, DeviceUtils.getappVersion(getActivity()),App.getContext().PACKAGENAME,new VcontrolCmd.ThirdPlay(mDatas.data.get(section).info.get(position).mv_title,mDatas.data.get(section).info.get(position).mv_id
                ,0,null,App.getContext().YINYUETAI),
                        new VcontrolCmd.CustomPlay(0,0,null,mPlyLists,position),
                        null,null,null)));
            }
        });
        mvNewAdapter.setmFooterLisener(new MVNewAdapter.OnFooterClick() {
            @Override
            public void clickLisener(int area_id,int section) {
                Intent intent=new Intent(getActivity(), NewMVItemActivity.class);
                intent.putExtra("section",section+"");
                if(mDatas!=null&&mDatas.data!=null){
                    intent.putExtra("title",mDatas.data.get(section).area_name);
                }
                startActivity(intent);
            }
        });
    }
    private void initData(){
        subscription= Api.getMangoApi().getMV().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<NewMvList> observer=new Observer<NewMvList>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onNext(NewMvList newMvList) {
            if(newMvList!=null){
                String mvlist=new Gson().toJson(newMvList);
                App.getContext().savaHomeJson("NewMvList",mvlist);
                loadHome(newMvList);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };
    private void unRegist(Subscription sub){
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }

    }

    @Override
    public void onRefresh() {
        initData();
    }
}
