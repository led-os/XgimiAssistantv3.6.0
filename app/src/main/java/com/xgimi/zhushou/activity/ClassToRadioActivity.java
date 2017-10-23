package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MusicViewHolder.ClassToRadioAdapter;
import com.xgimi.zhushou.bean.ClassToRadio;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/17.
 */
public class ClassToRadioActivity extends BaseActivity{
    private RecyclerView recyclerView;
    private ClassToRadioAdapter adapter;
    private List<ClassToRadio.data> mData=new ArrayList<>();
    private Subscription subscription;
    private LinearLayoutManager mLayoutManager;
    private int id;
    private ImageView iv_remount;
    private TextView title;
    private String fenlei;
    private int page=1;
    private View net_connect;
    private boolean isRefresh=false;
    private ImageView back;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private int lastVisibleItem;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_to_radio);
        initExtra();
        initView();
        initLocal();
        initData();
    }
    //读取缓存数据
    private void initLocal() {
        String readHomeJson=App.getContext().readHomeJson("AllRadioClassActivity");
        if (!StringUtils.isEmpty(readHomeJson)) {
            ClassToRadio data = new Gson().fromJson(readHomeJson, ClassToRadio.class);
            loadHome(data);
        } else {
            if (!HttpUrl.isNetworkConnected(ClassToRadioActivity.this)) {
                MissDilog();
                net_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(ClassToRadioActivity.this)) {
            getClassRadio(id,page);
        }
    }

    private void loadHome(ClassToRadio data) {
        if(isRefresh){
            mData.addAll(data.data);
        }else {
            mData=data.data;
        }

        adapter.dataChange(mData);
        recyclerView.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
        MissDilog();
    }


    //获取id
    private void initExtra() {
        if(getIntent()!=null){
            id = (int) getIntent().getSerializableExtra("id");
            fenlei=getIntent().getStringExtra("title");
        }
    }

    private void initView() {
        showDilog("加载中...");
        iv_remount= (ImageView) findViewById(R.id.local_yingyong_title).findViewById(R.id.iv_remount);
        iv_remount.setVisibility(View.GONE);
        title= (TextView) findViewById(R.id.local_yingyong_title).findViewById(R.id.tv_titile);
        title.setText(fenlei);
        recyclerView= (RecyclerView) findViewById(R.id.recyleview);
        adapter=new ClassToRadioAdapter(mData,ClassToRadioActivity.this);
        mLayoutManager = new LinearLayoutManager(ClassToRadioActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);
        net_connect = findViewById(R.id.netconnect);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HttpUrl.isNetworkConnected(ClassToRadioActivity.this)){
                    recyclerView.setVisibility(View.GONE);
                    net_connect.setVisibility(View.GONE);
                    getClassRadio(id,page);
                }
            }
        });
        back= (ImageView) findViewById(R.id.local_yingyong_title).findViewById(R.id.back);
        back(back);
    }
    //跳转等逻辑
    private void initData() {
        adapter.setOnClickListener(new ClassToRadioAdapter.OnClickListener() {
            @Override
            public void onClick(Object object, int position) {
                Intent intent = new Intent(ClassToRadioActivity.this, RadioDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id",((ClassToRadio.data) object).fm_id);
                intent.putExtras(bundle);
                intent.putExtra("title", fenlei);
                intent.putExtra("cover_path",((ClassToRadio.data) object).medium_thumb);
                startActivity(intent);
            }
        });
//        recyclerView.addOnScrollListener(new EndLessOnScrollListener(false,mLayoutManager) {
//            @Override
//            public void onLoadMore(int currentPage) {
//                getClassRadio(id,currentPage);
//                isRefresh=true;
//            }
//        });
        if (HttpUrl.isNetworkConnected(ClassToRadioActivity.this)) {
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                        if (!isMoreLoading) {
                            isMoreLoading = true;
                            page++;
                            isRefresh=true;
                            getClassRadio(id,page);
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
    }
    private int mpage;
    //获取分类下的电台
    private void getClassRadio(int id,int page) {
        mpage=page;
        subscription= Api.getMangoApi().getRadio(id,page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<ClassToRadio> observer=new Observer<ClassToRadio>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
            MissDilog();
            isMoreLoading = false;
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(ClassToRadio classToRadio) {
            if(classToRadio!=null&&classToRadio.data!=null){
                if(mpage==1){
                    String savaHomeJson=new Gson().toJson(classToRadio);
                    App.getContext().savaHomeJson(id+"",savaHomeJson);
                }
                loadHome(classToRadio);
            }
        }
    };
    private void unRegist(Subscription sub){
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }

    }
}
