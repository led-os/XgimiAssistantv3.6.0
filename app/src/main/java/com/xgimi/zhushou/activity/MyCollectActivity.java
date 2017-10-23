package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xgimi.zhushou.aes.MyAesToGsonFactory;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.CollectNum;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.XGIMILOG;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/24.
 */
public class MyCollectActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MyCollectActivity";
    private TextView tv1, tv3, tv4, tv5, title;
    private View view1, view3, view4, view5;
    private ImageView image1, image3, image4, image5;
    private TextView num1, num3, num4, num5;
    private ImageView back;
    private TextView danwei1, danwei2;
    private Subscription subscription;
    private App app;
    public int user_id;
    private GimiUser gimuser;
    private View xian;
    private View net_connect;
    private RelativeLayout rl_all;
    public static CollectNum mCollectNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollect);
        initExtea();
        initView();
        initLocal();
    }

    //读取缓存数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson("CollectNum");
        Log.d(TAG, "initLocal: " + readHomeJson);
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            CollectNum data = new Gson().fromJson(readHomeJson, CollectNum.class);
            mCollectNum = data;
            loadHome(data);
        } else {
            if (!HttpUrl.isNetworkConnected(MyCollectActivity.this)) {
                net_connect.setVisibility(View.VISIBLE);
                rl_all.setVisibility(View.GONE);
                MissDilog();
            }
        }
        if (HttpUrl.isNetworkConnected(MyCollectActivity.this)) {
            XGIMILOG.D("");
            getNum(user_id);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCollectNum != null) {
            getNum(user_id);
        }
    }

    private void loadHome(CollectNum data) {
        loadData(data);
    }

    private void initExtea() {
        app = (App) getApplicationContext();
        if (app.getLoginInfo() != null) {
            gimuser = app.getLoginInfo();
            user_id = Integer.valueOf(gimuser.data.uid);
        }
    }


    private void initView() {
        back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        title = (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        title.setText("我的收藏");
        tv1 = (TextView) findViewById(R.id.item1).findViewById(R.id.text1);
        image1 = (ImageView) findViewById(R.id.item1).findViewById(R.id.image);
        image1.setImageResource(R.drawable.movie_collect);
        tv1.setText("我收藏的影视");
        view1 = findViewById(R.id.item1);
        view1.setOnClickListener(this);
        ((TextView) findViewById(R.id.item1).findViewById(R.id.text3)).setText("部");
//        tv2 = (TextView) findViewById(R.id.item2).findViewById(R.id.text1);
//        image2 = (ImageView) findViewById(R.id.item2).findViewById(R.id.image);
//        image2.setImageResource(R.drawable.mv_collect);
//        tv2.setText("我收藏的MV");
//        view2 = findViewById(R.id.item2);
//        view2.setOnClickListener(this);
        tv3 = (TextView) findViewById(R.id.item3).findViewById(R.id.text1);
        image3 = (ImageView) findViewById(R.id.item3).findViewById(R.id.image);
        image3.setImageResource(R.drawable.radio_collect);
        tv3.setText("我收藏的电台");
        view3 = findViewById(R.id.item3);
        view3.setOnClickListener(this);
        tv4 = (TextView) findViewById(R.id.item4).findViewById(R.id.text1);
        image4 = (ImageView) findViewById(R.id.item4).findViewById(R.id.image);
        image4.setImageResource(R.drawable.app_collect);
        tv4.setText("我收藏的应用");
        danwei1 = (TextView) findViewById(R.id.item4).findViewById(R.id.text3);
        danwei1.setText("款");
        view4 = findViewById(R.id.item4);
        view4.setOnClickListener(this);
        tv5 = (TextView) findViewById(R.id.item5).findViewById(R.id.text1);
        image5 = (ImageView) findViewById(R.id.item5).findViewById(R.id.image);
        image5.setImageResource(R.drawable.game_collect);
        tv5.setText("我收藏的游戏");
        danwei2 = (TextView) findViewById(R.id.item5).findViewById(R.id.text3);
        danwei2.setText("款");
        view5 = findViewById(R.id.item5);
        view5.setOnClickListener(this);
        num1 = (TextView) findViewById(R.id.item1).findViewById(R.id.text2);
//        num2 = (TextView) findViewById(R.id.item2).findViewById(R.id.text2);
        num3 = (TextView) findViewById(R.id.item3).findViewById(R.id.text2);
        num4 = (TextView) findViewById(R.id.item4).findViewById(R.id.text2);
        num5 = (TextView) findViewById(R.id.item5).findViewById(R.id.text2);
        xian = findViewById(R.id.item5).findViewById(R.id.xian);
        xian.setVisibility(View.GONE);
        rl_all = (RelativeLayout) findViewById(R.id.rl_all);
        rl_all.setVisibility(View.GONE);
        net_connect = findViewById(R.id.netconnect);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpUrl.isNetworkConnected(MyCollectActivity.this)) {
                    rl_all.setVisibility(View.VISIBLE);
                    net_connect.setVisibility(View.GONE);
                }
            }
        });
        showDilog("加载中...");
    }


    private void getNum(int user_id) {
//        subscription = Api.getMangoApi().getCollectNum(user_id).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
        try {
            XGIMILOG.D("获取应用收藏数量 userID : " + user_id);
            subscription = Api.getXgimiVideoApi(MyAesToGsonFactory.create(CollectNum.class))
                    .getCollectNum(Api.getEncodeParam(new String[]{"user_id"}, new String[]{user_id + ""}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Observer<CollectNum> observer = new Observer<CollectNum>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(CollectNum collectNum) {
            XGIMILOG.D("获取成功 : " + new Gson().toJson(collectNum));
            if (collectNum != null && collectNum.data != null) {
                mCollectNum = collectNum;
                app = (App) MyCollectActivity.this.getApplicationContext();
                String savaHomeJson = new Gson().toJson(collectNum);
                Log.d(TAG, "onNext: " + savaHomeJson);
                app.savaHomeJson("CollectNum", savaHomeJson);
                loadHome(collectNum);
            }
        }
    };

    private void loadData(CollectNum collectNum) {
        num1.setText(collectNum.data.video);
//        num2.setText(collectNum.data.mv);
        num3.setText(collectNum.data.fm);
        num4.setText(collectNum.data.app);
        num5.setText(collectNum.data.game);
        MissDilog();
        rl_all.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
    }

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item1:
                Intent intent1 = new Intent(MyCollectActivity.this, MyMovieCollectActivity.class);
                startActivity(intent1);
                break;
//            case R.id.item2:
//                Intent intent2 = new Intent(MyCollectActivity.this, MyMvCollectActivity.class);
//                startActivity(intent2);
//                break;
            case R.id.item3:
                Intent intent3 = new Intent(MyCollectActivity.this, MyFMCollectActivity.class);
                startActivity(intent3);
                break;
            case R.id.item4:
                Intent intent4 = new Intent(MyCollectActivity.this, MyAppCollectActivity.class);
                startActivity(intent4);
                break;
            case R.id.item5:
                Intent intent5 = new Intent(MyCollectActivity.this, MyGameCollectActivity.class);
                startActivity(intent5);
                break;
        }
    }
}
