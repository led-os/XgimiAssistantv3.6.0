package com.xgimi.zhushou.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.AppManageAdapter;
import com.xgimi.zhushou.bean.TvApp;
import com.xgimi.zhushou.util.SaveTVApp;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends BaseActivity {
    private String appurl = "http://%s:16740/data/data/com.xgimi.filefly/app_appDatas/list";
    private RecyclerView mRecyclerView;
    List<TvApp.Appitem> mDatas=new ArrayList<>();
    private AppManageAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        initView();
    }
    private void initView(){
        controlTitle(findViewById(R.id.id_toolbar),true,true,false,false);
        tv.setText("应用管理");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyleview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mAdapter = new AppManageAdapter(this, SaveTVApp.getInstance(this).mTvApp.appList, GMDeviceStorage.getInstance().getConnectedDevice().getIp());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }
    private void initData(){

    }
}
