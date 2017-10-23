package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.Interface.EndLessOnScrollListener;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MusicViewHolder.AllAdapter;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.MVList;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/13.
 */
public class NewMVItemActivity extends BaseActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener,View.OnTouchListener{
    private RecyclerView recyclerView;
    private AllAdapter adpter;
    private List<MVList.data> mvData=new ArrayList<>();
    private Subscription subscription;
    public int page=1;
    public int psize=12;
    private String area_id;
    private LinearLayoutManager mLayoutManager;
    private App app;
    public int user_id;
    private boolean isLogo;
    private GimiUser gimuser;
    private View net_connect;
    private boolean isRefresh=false;
    private String order;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View close;
    private View load_false;
    private String section;
    private ImageView back;
    private ImageView iv_remount;
    private TextView title;
    private String mytitle;
    public String mvLeixing;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mv_item);
        initExtra();
        initView();
//        initLocal();
        initData();
        handler.sendEmptyMessageDelayed(0,200);
    }
    private void initExtra() {
        app = App.getContext();
        if(app.getLoginInfo()!=null){
            gimuser = app.getLoginInfo();
            isLogo=true;
            user_id= Integer.parseInt(gimuser.data.uid);
        }
        if(getIntent()!=null){
            section=getIntent().getStringExtra("section");
            mytitle=getIntent().getStringExtra("title");
        }
        if(SaveData.getInstance().mTypes!=null) {
            order=SaveData.getInstance().mTypes.data.get(Integer.valueOf(section)).order;
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initLocal();
        }
    };
    //读取缓存本地数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson(mytitle);
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            MVList data = new Gson().fromJson(readHomeJson, MVList.class);
            loadHome(data);
        } else {
            if (!HttpUrl.isNetworkConnected(NewMVItemActivity.this)) {
                net_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(NewMVItemActivity.this)) {
            if(SaveData.getInstance().mTypes!=null) {
                getMV(page, psize, SaveData.getInstance().mTypes.data.get(Integer.valueOf(section)).area_id, user_id,order);
            }
        }
    }

    private void initData() {
        adpter.setOnClickListener(new AllAdapter.OnClickListener() {
            @Override
            public void onClick(Object object, int position) {
                Intent intent = new Intent(NewMVItemActivity.this, MVDetailActivity.class);
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

                SaveData.getInstance().mv_title=((MVList.data) object).mv_title;
                SaveData.getInstance().mv_artist=((MVList.data) object).mv_artist;
                SaveData.getInstance().bitmap=((MVList.data) object).mv_thumb;
                SaveData.getInstance().mvid=((MVList.data) object).mv_id;
                SaveData.getInstance().position=position;
                startActivity(intent);
                Log.e("MVtongji",mvData.get(position).mv_title+"--"+mvData.get(position).mv_id+"--");
                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,"2",
                        GMSdkCheck.appId, DeviceUtils.getappVersion(NewMVItemActivity.this),App.getContext().PACKAGENAME,new VcontrolCmd.ThirdPlay(mvData.get(position).mv_title,mvData.get(position).mv_id,0,null,
                        App.getContext().YINYUETAI),
                        new VcontrolCmd.CustomPlay(0,0,null,mPlyLists,position),
                        null,null,null)));
            }
        });
        recyclerView.addOnScrollListener(new EndLessOnScrollListener(false,mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if(SaveData.getInstance().mTypes!=null) {
                    getMV(currentPage, psize, SaveData.getInstance().mTypes.data.get(Integer.valueOf(section)).area_id, user_id,order);
                    isRefresh=true;
                }
            }
        });
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
            if(mvData.get(i).mv_type_name!=null){
                mvLeixing=mvData.get(i).mv_type_name;
            }else {
                mvLeixing="";
            }
            VcontrolCmd.CustomPlay.PlayList mPlayList=new VcontrolCmd.CustomPlay.PlayList(null,mvData.get(i).mv_id,null,mvData.get(i).mv_title,null,mvData.get(i).mv_play_address,null,0,null,
                    App.getContext().YINYUETAI);
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
    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView= (RecyclerView) findViewById(R.id.recyleview);
        adpter=new AllAdapter(mvData,NewMVItemActivity.this);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adpter);
        recyclerView.setLayoutManager(mLayoutManager);
        close=findViewById(R.id.close);
        load_false=findViewById(R.id.load_false);
        net_connect = findViewById(R.id.netconnect);
        back= (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        back(back);
        title.setText(mytitle);
        iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);
        setYaokongBackground(iv_remount,this,"qita");
        iv_remount.setOnTouchListener(this);
        if(Constant.netStatus){
            iv_remount.setImageResource(R.drawable.yaokongqi);
        }else{
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HttpUrl.isNetworkConnected(NewMVItemActivity.this)){
                    recyclerView.setVisibility(View.GONE);
                    net_connect.setVisibility(View.GONE);
                    if(SaveData.getInstance().mTypes!=null) {
                        getMV(page, psize, SaveData.getInstance().mTypes.data.get(Integer.valueOf(section)).area_id, user_id,order);
                    }
                }
            }
        });
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
                    String HongKong=new Gson().toJson(mvList);
                    App.getContext().savaHomeJson(mytitle,HongKong);
                }
                loadHome(mvList);
            }else {
                recyclerView.setVisibility(View.GONE);
                load_false.setVisibility(View.VISIBLE);
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

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_remount:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        iv_remount.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        iv_remount.setAlpha(1.0f);
                        break;
                }
                break;
        }
        return false;
    }
}
