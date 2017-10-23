package com.xgimi.zhushou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.MainActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.AdvertisementBean;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.StringUtils;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends Activity {
    private ImageView mIvAdver;
    private Button mSkip;
    private boolean isCache=false;
    private int countDown=3;
    AdvertisementBean mDatas;
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    break;
                case 1:
                    mSkip.setText("跳过 "+countDown);
                    if(countDown==1){
                        Intent intent1=new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                    if(countDown>0){
                        mHandler.sendEmptyMessageDelayed(1,1000);
                    }
                    countDown--;
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();
    }
    private void initView(){
        mIvAdver = (ImageView) findViewById(R.id.iv_adver);
        mSkip = (Button) findViewById(R.id.skip);
        mSkip.setText("跳过 "+countDown);
        mHandler.sendEmptyMessageDelayed(1,1000);
        mIvAdver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isOpen=false;
                if(mDatas!=null&&mDatas.data!=null&&mDatas.data.size()>0){
                    if(mDatas.data.get(0).type.equals("msg")){
                        isOpen=true;
                    }else if(mDatas.data.get(0).type.equals("urltoweb")){
                        App.getContext().advserMent=1;
                        App.getContext().url=mDatas.data.get(0).ad_actionurl;
                        App.getContext().title=mDatas.data.get(0).ad_title;

                    }else if(mDatas.data.get(0).type.equals("urltolive")){
                        App.getContext().advserMent=2;
                        App.getContext().url=mDatas.data.get(0).ad_actionurl;
                        App.getContext().title=mDatas.data.get(0).ad_title;
                    }else if(mDatas.data.get(0).type.equals("urltopalyvideo")){
                        App.getContext().advserMent=3;
                        App.getContext().url=mDatas.data.get(0).ad_actionurl;
                        App.getContext().title=mDatas.data.get(0).ad_title;
                    }else if(mDatas.data.get(0).type.equals("urltoapp")){
                        App.getContext().advserMent=4;
                        App.getContext().url=mDatas.data.get(0).ad_actionurl;
                        App.getContext().title=mDatas.data.get(0).ad_title;
                    }
                }
                if(!isOpen) {
                    mHandler.removeMessages(0);
                    mHandler.removeMessages(1);
                    Intent intent1 = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }
        });
        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
    private Long current;
    private void initData(){
        String readHomeJson = App.getContext().readHomeJson("adver");
        if (!StringUtils.isEmpty(readHomeJson)) {
            isCache=true;
            AdvertisementBean data = new Gson().fromJson(readHomeJson, AdvertisementBean.class);
            mDatas=data;
            ImageLoader.getInstance().displayImage(NetUtil.IMAGEURL+data.data.get(0).ad_imgurl,mIvAdver);
            mSkip.setVisibility(View.VISIBLE);
            current=System.currentTimeMillis();
            mHandler.sendEmptyMessageDelayed(0,3000);
        }

        if(HttpUrl.isNetworkConnected(this)) {
            getAdvertisement();
        }
        if(!HttpUrl.isNetworkConnected(this)){
            mHandler.sendEmptyMessage(0);
        }
    }
    private Subscription subscription;
    //获取应用详情页面
    public void getAdvertisement(){
        subscription = Api.getMangoApi().getAdvertisement()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    Observer<AdvertisementBean> observer = new Observer<AdvertisementBean>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
        @Override
        public void onNext(AdvertisementBean channels) {
            if(channels!=null&&channels.data!=null&&channels.data.size()>0){
                App.getContext().savaHomeJson("adver",new Gson().toJson(channels));
         if(!isCache) {
             ImageLoader.getInstance().displayImage(NetUtil.IMAGEURL + channels.data.get(0).ad_imgurl, mIvAdver);
             mSkip.setVisibility(View.VISIBLE);
             mHandler.sendEmptyMessageDelayed(0, 3000);
             mDatas=channels;
             mHandler.sendEmptyMessage(0);
            }
            }else{
                mHandler.sendEmptyMessageDelayed(0,0);
            }
        }
    };
    private void unRegist(Subscription sub){
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }
}
