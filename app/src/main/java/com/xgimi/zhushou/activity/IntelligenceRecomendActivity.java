package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.IndividualityAdapter;
import com.xgimi.zhushou.bean.Individuality;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.inter.OnitemClick;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.Statics;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.ArrayList;
import java.util.List;

public class IntelligenceRecomendActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    List<Individuality.DataBean.DataBean1> mDatas=new ArrayList<>();
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private int lastVisibleItem;
    private int mPage=1;
    private boolean isSorts;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private IndividualityAdapter mAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private String mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligence_recomend);
        initExras();
        initView();
        initData(SaveData.getInstance().deviceMac,mPage,15);
    }

    private void initExras(){
        Intent intent=getIntent();
        if(intent!=null){
            mTitle = intent.getStringExtra("mtitle");
        }
    }
    private void initView(){
        ImageView back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        TextView title = (TextView) findViewById(R.id.tv_titile);
       title.setText(mTitle+"");
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyleview);
        mAdapter = new IndividualityAdapter(this,mDatas);
        recyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                 if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    if (!isMoreLoading) {
                        isMoreLoading = true;
                        mPage++;
                        isSorts = false;
                        initData(SaveData.getInstance().deviceMac,mPage,15);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
        mAdapter.setLisener(new OnitemClick() {
            @Override
            public void onClickLisener(String name,String id,String type) {
                Intent intent=new Intent(IntelligenceRecomendActivity.this, FilmDetailActivity.class);
                intent.putExtra("id",id);
                SaveData.getInstance().mSouceInsight="1";
                Statics.getInstance().sendStatics(IntelligenceRecomendActivity.this,name,id,SaveData.getInstance().mSouceInsight,SaveData.getInstance().mSoucePage,null);
                startActivity(intent);
             }
        }
        );
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    isMoreLoading = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 1:
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    private void initData(String mac,int page, int pagesize) {
        ToosUtil.getInstance().getIndividualityVideo1(new CommonCallBack<List<Individuality.DataBean.DataBean1>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(final List<Individuality.DataBean.DataBean1> data) {
                datass=data;
                mHandler11.sendEmptyMessage(0);
                mSwipeRefreshLayout.setRefreshing(false);


            }
            @Override
            public void onFailed(String reason) {
                Toast.makeText(IntelligenceRecomendActivity.this, reason, Toast.LENGTH_SHORT).show();
            }
        }, this, mac, page, pagesize);
    }

    List<Individuality.DataBean.DataBean1> datass=new ArrayList<>();
Handler mHandler11=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        loadHome(datass);
//        mSwipeRefreshLayout.setRefreshing(false);

    }
};
    @Override
    public void onRefresh() {
        mPage=1;
        isSorts=true;
        initData(SaveData.getInstance().deviceMac,mPage,15);
    }
    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
    private void loadHome(List<Individuality.DataBean.DataBean1> channels) {
        if(isMoreLoading){
            if(isSorts){
                mDatas.clear();
                mDatas.addAll(channels);
            }else{
                mDatas.addAll(channels);
            }
        }else {
            mDatas=channels;
        }
        if(mDatas!=null&&mDatas.size()>0){
            isMoreLoading=false;
            mAdapter.dataChange(mDatas);
        }
    }
}
