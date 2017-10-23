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
import com.xgimi.zhushou.adapter.MusicViewHolder.AllAdapter;
import com.xgimi.zhushou.bean.GimiUser;
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
public class AllFragment extends BaseFragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private AllAdapter adpter;
    private List<MVList.data> mvData=new ArrayList<>();
    private Subscription subscription;
    public int page=1;
    public int psize=12;
    public String keywords;
    private String area_id;
    private LinearLayoutManager mLayoutManager;
    private App app;
    public int user_id;
    private GimiUser gimuser;
    private boolean isLogo;
    private View net_connect;
    private AnimationDrawable animationDrawable;
    private String order;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View close;
    private View load_false;
    private int lastVisibleItem;
    private boolean isMoreLoading = false;
    private boolean isRefresh=false;
    public AllFragment(){

    }
    public static AllFragment mvfragment;
//    public static AllFragment getInstance(){
//        if(mvfragment==null){
//            mvfragment=new AllFragment();
//        }
//        return mvfragment;
//    }
Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        initLocal();
    }
};

    public View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_music_filter, container, false);
        initExtra();
        initView(view);
        handler.sendEmptyMessageDelayed(0,200);
        initData();

        return view;
    }

    private  boolean isFirst;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isFirst&&isVisibleToUser){
            isFirst=true;

        }
    }

    private void initExtra() {
        app = (App) getActivity().getApplicationContext();
        if(app.getLoginInfo()!=null){
            gimuser = app.getLoginInfo();
            isLogo=true;
            user_id= Integer.parseInt(gimuser.data.uid);
        }
        if(SaveData.getInstance().mTypes!=null) {
            order=SaveData.getInstance().mTypes.data.get(0).order;
        }
    }


    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyleview);
        adpter=new AllAdapter(mvData,getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adpter);
        recyclerView.setLayoutManager(mLayoutManager);
        net_connect = view.findViewById(R.id.netconnect);
        close=view.findViewById(R.id.close);
        load_false=view.findViewById(R.id.load_false);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HttpUrl.isNetworkConnected(getActivity())){
                    recyclerView.setVisibility(View.VISIBLE);
                    net_connect.setVisibility(View.GONE);
                    if(SaveData.getInstance().mTypes!=null) {
                        getMV(page, psize, SaveData.getInstance().mTypes.data.get(0).area_id, user_id,order);
                    }
                }
            }
        });
    }
    //读取缓存本地数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson("All");
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            MVList data = new Gson().fromJson(readHomeJson, MVList.class);
            loadHome(data);
        } else {
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                net_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            if(SaveData.getInstance().mTypes!=null) {
                getMV(page, psize, SaveData.getInstance().mTypes.data.get(0).area_id, user_id,order);
            }
        }
    }
    private void loadHome(MVList mvList) {
        if(isRefresh){
            mvData.addAll(mvList.data);
        }else{
            if (mvList.data!=null){
                mvData=mvList.data;
            }
        }
        adpter.dataChange(mvData);
        mPlyLists.clear();
        for (int i = 0; i < mvData.size(); i++) {
            VcontrolCmd.CustomPlay.PlayList mPlayList=new VcontrolCmd.CustomPlay.PlayList(null,null,null,mvData.get(i).mv_title,null,mvData.get(i).mv_play_address,null);
            mPlyLists.add(mPlayList);
        }
        recyclerView.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
        load_false.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        if(mvList.code==4444){
            close.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            load_false.setVisibility(View.GONE);
            net_connect.setVisibility(View.GONE);
        }
    }
    private void initData() {
        adpter.setOnClickListener(new AllAdapter.OnClickListener() {
            @Override
            public void onClick(Object object, int position) {

                Intent intent = new Intent(getActivity(), MVDetailActivity.class);
                intent.putExtra("title",((MVList.data) object).mv_title);
                intent.putExtra("artist",((MVList.data) object).mv_artist);
                intent.putExtra("bitmap",((MVList.data) object).mv_thumb);
                intent.putExtra("mv_play_address",((MVList.data) object).mv_play_address);
                intent.putExtra("areaid",area_id);
                Bundle bundle = new Bundle();
                bundle.putSerializable("type",1);
                intent.putExtras(bundle);
                SaveData.getInstance().mList=mvData;
                intent.putExtra("collect_id",((MVList.data) object).collect_id);
                intent.putExtra("mv_id",((MVList.data) object).mv_id);
                SaveData.getInstance().mv_id=mvData.get(position).mv_id;
                SaveData.getInstance().MV_type=1;
                ToosUtil.getInstance().addEventUmeng(getActivity(),"event_music_mv_new");

                SaveData.getInstance().mv_title=((MVList.data) object).mv_title;
                SaveData.getInstance().mv_artist=((MVList.data) object).mv_artist;
                SaveData.getInstance().bitmap=((MVList.data) object).mv_thumb;
                SaveData.getInstance().mvid=((MVList.data) object).mv_id;
                SaveData.getInstance().position=position;
                startActivity(intent);
                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,"2",
                        GMSdkCheck.appId,null,
                        new VcontrolCmd.CustomPlay(0,0,null,mPlyLists,position),
                        null,null,null)));
            }
        });
        if (HttpUrl.isNetworkConnected(getActivity())) {
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adpter.getItemCount()) {
                        if (!isMoreLoading) {
                            isMoreLoading = true;
                            page++;
                            if(SaveData.getInstance().mTypes!=null) {
                                getMV(page, psize, SaveData.getInstance().mTypes.data.get(0).area_id, user_id,order);
                                isRefresh=true;
                            }
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                }
            });
        }
//        recyclerView.addOnScrollListener(new EndLessOnScrollListener(false,mLayoutManager) {
//            @Override
//            public void onLoadMore(int currentPage) {
//                if(SaveData.getInstance().mTypes!=null) {
//                    getMV(currentPage, psize, SaveData.getInstance().mTypes.data.get(0).area_id, user_id,order);
//                    isRefresh=true;
//                }
//            }
//        });
    }
    private int mpage;
    private void getMV(int page,int psize,String area_type,int user_id,String order) {
        mpage=page;
        area_id=area_type;
        subscription= Api.getMangoApi().getMVList(page, psize, area_type,user_id,order).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    List< VcontrolCmd.CustomPlay.PlayList> mPlyLists=new ArrayList<>();

    //获取MV推荐数据
    Observer<MVList> observer=new Observer<MVList>() {

        @Override
        public void onCompleted() {
            isMoreLoading = false;
            unRegist(subscription);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            load_false.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onNext(MVList mvList) {
            if(mvList!=null&&mvList.data!=null){
                if(mpage==1){
                    App app = (App) getActivity().getApplicationContext();
                    String all=new Gson().toJson(mvList);
                    app.savaHomeJson("All",all);
                }
                loadHome(mvList);
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
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        if(SaveData.getInstance().mTypes!=null) {
            getMV(page, psize, SaveData.getInstance().mTypes.data.get(0).area_id, user_id,order);
        }
    }
}
