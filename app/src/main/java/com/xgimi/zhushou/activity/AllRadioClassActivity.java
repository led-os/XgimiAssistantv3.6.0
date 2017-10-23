package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.AllClassGridAdapter;
import com.xgimi.zhushou.bean.AllRadioClass;
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
public class AllRadioClassActivity extends BaseActivity{
    private GridView recyclerView;
    private AllClassGridAdapter classadapter;
    private List<AllRadioClass.data> classData=new ArrayList<>();
    private Subscription subscription1;
    private LinearLayoutManager mLayoutManager;
    private ImageView iv_remount;
    private TextView title;
    private App app;
    private View net_connect;
    private ImageView back;
    private View load_false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_radio_class);
        initView();
        initLocal();
        initData();
    }
    private void initView() {
        app= (App) AllRadioClassActivity.this.getApplicationContext();
        iv_remount= (ImageView) findViewById(R.id.local_yingyong_title).findViewById(R.id.iv_remount);
        iv_remount.setVisibility(View.GONE);
        title= (TextView) findViewById(R.id.local_yingyong_title).findViewById(R.id.tv_titile);
        title.setText("全部分类");
        recyclerView= (GridView) findViewById(R.id.recyleview);
        load_false=findViewById(R.id.load_false);
        classadapter=new AllClassGridAdapter(AllRadioClassActivity.this,classData);
//        mLayoutManager = new LinearLayoutManager(AllRadioClassActivity.this);
//        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(classadapter);
        net_connect = findViewById(R.id.netconnect);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HttpUrl.isNetworkConnected(AllRadioClassActivity.this)){
                    recyclerView.setVisibility(View.GONE);
                    net_connect.setVisibility(View.GONE);
                    getAllClass();
                }
            }
        });
        back= (ImageView) findViewById(R.id.local_yingyong_title).findViewById(R.id.back);
        back(back);
        showDilog("加载中...");
        recyclerView.setVisibility(View.GONE);
    }
    //读取缓存数据
    private void initLocal() {
        String readHomeJson=app.readHomeJson("AllRadioClassActivity");
        if (!StringUtils.isEmpty(readHomeJson)) {
            AllRadioClass data = new Gson().fromJson(readHomeJson, AllRadioClass.class);
            loadHome(data);
        } else {
            if (!HttpUrl.isNetworkConnected(AllRadioClassActivity.this)) {
                MissDilog();
                net_connect.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(AllRadioClassActivity.this)) {
            getAllClass();
        }
    }

    private void loadHome(AllRadioClass data) {
        classData=data.data;
//        classData.addAll(data.data);
        classadapter.dataChange(classData);
        MissDilog();
        load_false.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
    }

    //跳转等逻辑
    private void initData() {
//        classadapter.setOnClickListener(new AllClassAdapter.OnClickListener() {
//            @Override
//            public void onClick(Object object, int position) {
//                Intent intent = new Intent(AllRadioClassActivity.this, ClassToRadioActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("id",((AllRadioClass.data) object).category_id);
//                intent.putExtras(bundle);
//                intent.putExtra("title", ((AllRadioClass.data) object).category_name);
//                startActivity(intent);
//            }
//        });
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AllRadioClassActivity.this, ClassToRadioActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id",classData.get(position).category_id);
                intent.putExtras(bundle);
                intent.putExtra("title", classData.get(position).category_name);
                startActivity(intent);
            }
        });
    }
    //获取所有分类电台
    private void getAllClass() {

        subscription1= Api.getMangoApi().getAllClass().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }
    Observer<AllRadioClass> observer1=new Observer<AllRadioClass>() {
        @Override
        public void onCompleted() {
            unRegist(subscription1);
            MissDilog();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            load_false.setVisibility(View.VISIBLE);
            MissDilog();
        }

        @Override
        public void onNext(AllRadioClass allRadioClass) {
            if(allRadioClass!=null&&allRadioClass.data!=null){
                app= (App) AllRadioClassActivity.this.getApplicationContext();
                String allclass=new Gson().toJson(allRadioClass);
                app.savaHomeJson("AllRadioClassActivity",allclass);
                loadHome(allRadioClass);
            }else {
                load_false.setVisibility(View.VISIBLE);
                MissDilog();
                recyclerView.setVisibility(View.GONE);
                net_connect.setVisibility(View.GONE);
            }
        }
    };
    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }
}
