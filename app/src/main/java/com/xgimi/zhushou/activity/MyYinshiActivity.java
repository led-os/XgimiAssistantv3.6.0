package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MyYinshiAdapter;
import com.xgimi.zhushou.bean.MyYinShiBeen;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/12.
 */
public class MyYinshiActivity extends BaseActivity {
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
        setContentView(R.layout.activity_my_yinshi);
        initView();
        initData();
        initListen();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        back(back);
        title.setText("影视推送");
        lisetview = (ListView) findViewById(R.id.lisetview);
        nodata = (TextView) findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        adapter = new MyYinshiAdapter(mData, MyYinshiActivity.this);
        lisetview.setAdapter(adapter);
    }

    private void initData() {
        getSubTipsList();
    }

    private void initListen() {
        lisetview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mData != null) {
                    if (mData.data.get(position).type.equals("21")) {
                        Intent intent = new Intent(MyYinshiActivity.this, FilmDetailActivity.class);
                        intent.putExtra("id", mData.data.get(position).t_id);
                        startActivity(intent);
                    } else if (mData.data.get(position).type.equals("22")) {
                        Intent intent = new Intent(MyYinshiActivity.this, FilmZhuanTiActivity.class);
                        intent.putExtra("id", mData.data.get(position).t_id);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    //获取影视推送
    public void getSubTipsList() {
        subscription = Api.getMangoApi().getSubTipsList("1", "", "10", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<MyYinShiBeen> observer = new Observer<MyYinShiBeen>() {
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
            XGIMILOG.E("获取影视推送成功");
//            LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(myYinShiBeen));
            if (myYinShiBeen != null && myYinShiBeen.data != null && myYinShiBeen.data.size() > 0) {
                mData = myYinShiBeen;
                adapter.dataChange(myYinShiBeen);
                lisetview.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.GONE);
            } else {
                lisetview.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
            }
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }
}
