package com.xgimi.zhushou.fragment.musicfragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.xgimi.zhushou.adapter.MusicViewHolder.MVRecommendAdapter;
import com.xgimi.zhushou.bean.MVList;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/12.
 */
public class MVRecommendFragment extends BaseFragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private MVRecommendAdapter adpter;
    private List<MVList.data> mvData=new ArrayList<>();
    private Subscription subscription;
    private LinearLayoutManager mLayoutManager;
    private View net_connect;
    private AnimationDrawable animationDrawable;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View close;
    private View load_false;
    private View prog;
    public MVRecommendFragment(){
    }
    public static MVRecommendFragment mvfragment;
    public static MVRecommendFragment getInstance(){
        if(mvfragment==null){
            mvfragment=new MVRecommendFragment();
        }
        return mvfragment;
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initLocal();
        }
    };
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_music_filter, container, false);
      //  getMv();
        initView(view);
//        initLocal();
        handler.sendEmptyMessageDelayed(0,200);
        initData();
        return view;
    }


    List< VcontrolCmd.CustomPlay.PlayList> mPlyLists=new ArrayList<>();
    private void initData() {
        adpter.setOnClickListener(new MVRecommendAdapter.OnClickListener() {
            @Override
            public void onClick(Object object, int position) {
                Intent intent = new Intent(getActivity(), MVDetailActivity.class);
                intent.putExtra("title",((MVList.data) object).mv_title);
                intent.putExtra("artist",((MVList.data) object).mv_artist);
                intent.putExtra("bitmap",((MVList.data) object).mv_thumb);
                intent.putExtra("mv_play_address",((MVList.data) object).mv_play_address);
                Bundle bundle = new Bundle();
                bundle.putSerializable("type",2);
                intent.putExtras(bundle);
                SaveData.getInstance().tuijianList=mvData;
                intent.putExtra("collect_id",((MVList.data) object).collect_id);
                intent.putExtra("mv_id",((MVList.data) object).mv_id);
                SaveData.getInstance().mv_id=mvData.get(position).mv_id;
                ToosUtil.getInstance().addEventUmeng(getActivity(),"event_music_mv_recommend");

                SaveData.getInstance().mv_title=((MVList.data) object).mv_title;
                SaveData.getInstance().mv_artist=((MVList.data) object).mv_artist;
                SaveData.getInstance().bitmap=((MVList.data) object).mv_thumb;
                SaveData.getInstance().mv_id=((MVList.data) object).mv_id;
                SaveData.getInstance().position=position;
                startActivity(intent);
                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,"2",
                        GMSdkCheck.appId,null,
                        new VcontrolCmd.CustomPlay(0,0,null,mPlyLists,position),
                        null,null,null)));
            }
        });
    }
    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyleview);
        adpter=new MVRecommendAdapter(mvData,getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adpter);
        recyclerView.setLayoutManager(mLayoutManager);
        close=view.findViewById(R.id.close);
        load_false=view.findViewById(R.id.load_false);
        net_connect = view.findViewById(R.id.netconnect);
        prog=view.findViewById(R.id.myprog);
        prog.setVisibility(View.VISIBLE);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HttpUrl.isNetworkConnected(getActivity())){
                    recyclerView.setVisibility(View.VISIBLE);
                    prog.setVisibility(View.VISIBLE);
                    net_connect.setVisibility(View.GONE);

                }
            }
        });
//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adpter.getItemCount()) {
//                    if (!isMoreLoading) {
//                        isMoreLoading = true;
//                        isSorts=false;
////                        getMv();
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
//            }
//        });
    }
    //读取缓存本地数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson("mvTuijian");
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            MVList data = new Gson().fromJson(readHomeJson, MVList.class);
            loadHome(data);
        } else {
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                net_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
            }

        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            getMv();
        }
    }

    private void loadHome(MVList mData) {
        mvData.addAll(mData.data);
        adpter.dataChange(mvData);
        data=mData;
        SaveData.getInstance().mList=mvData;
        for (int i = 0; i < data.data.size(); i++) {
            VcontrolCmd.CustomPlay.PlayList mPlayList=new VcontrolCmd.CustomPlay.PlayList(null,null,null,data.data.get(i).mv_title,null,data.data.get(i).mv_play_address,null);
            mPlyLists.add(mPlayList);
        }
        recyclerView.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
        load_false.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        if(mData.code==4444){
            close.setVisibility(View.VISIBLE);
            load_false.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            net_connect.setVisibility(View.GONE);
        }
        prog.setVisibility(View.GONE);
    }

    private void getMv() {
        subscription= Api.getMangoApi().getMVTuijian().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    MVList data=new MVList();
    //获取MV推荐数据
    Observer<MVList> observer=new Observer<MVList>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
            mSwipeRefreshLayout.setRefreshing(false);
            prog.setVisibility(View.GONE);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            prog.setVisibility(View.GONE);
            if(HttpUrl.isNetworkConnected(getActivity())) {
                load_false.setVisibility(View.VISIBLE);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onNext(MVList mvTuijian) {
            if(mvTuijian!=null && mvTuijian.data!=null){
                App app = (App) getActivity().getApplicationContext();
                String mvtuijian=new Gson().toJson(mvTuijian);
                app.savaHomeJson("mvTuijian",mvtuijian);
                mvData.clear();
                loadHome(mvTuijian);
            }else {
                recyclerView.setVisibility(View.GONE);
                if(HttpUrl.isNetworkConnected(getActivity())) {
                    load_false.setVisibility(View.VISIBLE);
                }
            }
            prog.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };
    private void unRegist(Subscription sub){
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();

        }

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        getMv();
    }
}
