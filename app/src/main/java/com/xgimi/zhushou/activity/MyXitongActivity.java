package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MyYinshiAdapter;
import com.xgimi.zhushou.bean.MyYinShiBeen;
import com.xgimi.zhushou.netUtil.Api;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/12.
 */
public class MyXitongActivity extends BaseActivity{
    private Subscription subscription;
    private MyYinshiAdapter adapter;
    private MyYinShiBeen mData;
    private ListView lisetview;
    private TextView nodata;
    private ImageView back;
    private TextView title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_xitong);
        initView();
        initData();
        initListen();
    }


    private void initView(){
        back= (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        back(back);
        title.setText("系统消息");
        lisetview= (ListView) findViewById(R.id.lisetview);
        nodata= (TextView) findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        adapter=new MyYinshiAdapter(mData,MyXitongActivity.this);
        lisetview.setAdapter(adapter);
    }
    private void initData(){
        getSubTipsList();
    }
    private void initListen() {
        lisetview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MyXitongActivity.this,WebViewActivity.class);
                intent.putExtra("url",mData.data.get(position).url);
                intent.putExtra("mytitle","系统消息");
                startActivity(intent);
            }
        });
    }

    //获取影视推送
    public void getSubTipsList(){
        subscription = Api.getMangoApi().getSubTipsList("3","","10","0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<MyYinShiBeen> observer=new Observer<MyYinShiBeen>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(MyYinShiBeen myYinShiBeen) {
            if(myYinShiBeen!=null&&myYinShiBeen.data!=null&&myYinShiBeen.data.size()>0){
                mData=myYinShiBeen;
                adapter.dataChange(myYinShiBeen);
                lisetview.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.GONE);
            }else {
                lisetview.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
            }
        }
    };
    private void unRegist(Subscription sub){
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();}
    }
}
