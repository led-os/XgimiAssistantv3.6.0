package com.xgimi.zhushou.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.FragmentAdapter;
import com.xgimi.zhushou.bean.FMCancleCollect;
import com.xgimi.zhushou.bean.FMCollect;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.RadioInfo;
import com.xgimi.zhushou.fragment.radiodetailfragment.DetailFragment;
import com.xgimi.zhushou.fragment.radiodetailfragment.SameFragment;
import com.xgimi.zhushou.fragment.radiodetailfragment.ShowFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GaoSi;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.NetAbout;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/17.
 */
public class RadioDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView tupian;
    private TextView title;
    private ImageView back;
    private ImageView mcollect;
    public int id;
    private Subscription subscription;
    private Subscription subscription1;
    private Subscription subscription2;
    private RelativeLayout gaosi;
    private ViewPager viewPager;
    private ImageView indicator;
    private TextView zhuanji;
    public String name;
    private App app;
    public int user_id;
    private GimiUser gimuser;
    private boolean isLogo;
    private boolean isCollect;
    private int collect_id;
    private String fm_title, fm_cover, fm_author;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    public String cover_path;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private List<TextView> tvs = new ArrayList<>();
    private ImageView daohang;
    private View net_connect;
    private RelativeLayout rl_all;
    private ImageView bofang;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XGIMILOG.D("");
        setContentView(R.layout.activity_radio_detail);
        initExtra();
        initFragment();
        initView();
        initLocal();
        initData();
    }

    //获取缓存数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson(id + "");
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            RadioInfo data = new Gson().fromJson(readHomeJson, RadioInfo.class);
            loadHome(data);
        } else {
            if (!HttpUrl.isNetworkConnected(RadioDetailActivity.this)) {
                MissDilog();
                net_connect.setVisibility(View.VISIBLE);
                rl_all.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(RadioDetailActivity.this)) {
            getInfo(id, user_id);
        }
    }

    //加载数据
    private void loadHome(RadioInfo radioInfo) {
        XGIMILOG.E("开始加载数据");
//        LogUtil.d(XGIMILOG.getTag(), "---------" + new Gson().toJson(radioInfo));
        SaveData.getInstance().mDetail = radioInfo;
        loadData(radioInfo);
        rl_all.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
        MissDilog();
    }

    private void initExtra() {
        if (getIntent() != null) {
            id = (int) getIntent().getSerializableExtra("id");
            name = getIntent().getStringExtra("title");
            cover_path = getIntent().getStringExtra("cover_path");
            app = (App) getApplicationContext();
            if (app.getLoginInfo() != null) {
                gimuser = app.getLoginInfo();
                isLogo = true;
                user_id = Integer.valueOf(gimuser.data.uid);
            }
        }
    }

    private void initView() {
        rl_all = (RelativeLayout) findViewById(R.id.rl_all);
        tupian = (ImageView) findViewById(R.id.iv_radio);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        gaosi = (RelativeLayout) findViewById(R.id.rl_gaosi);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        indicator = (ImageView) findViewById(R.id.indicator);
        android.view.ViewGroup.LayoutParams layoutParams = indicator.getLayoutParams();
        layoutParams.width = NetAbout.getInstance().getScreenWidth(this) / 6;
        zhuanji = (TextView) findViewById(R.id.zhuanji);
        mcollect = (ImageView) findViewById(R.id.collect);
        mcollect.setOnClickListener(this);
        bofang = (ImageView) findViewById(R.id.bofang);
        bofang.setOnClickListener(this);
        tv1 = (TextView) findViewById(R.id.xiangqing);
        tv2 = (TextView) findViewById(R.id.jiemu);
        tv3 = (TextView) findViewById(R.id.xiangsi);
        tv2.setTextColor(Color.parseColor("#2F9BFF"));
        tvs.add(tv1);
        tvs.add(tv2);
        tvs.add(tv3);
        CollectOnclickListener listener = new CollectOnclickListener();
        tv1.setOnClickListener(listener);
        tv2.setOnClickListener(listener);
        tv3.setOnClickListener(listener);
        daohang = (ImageView) findViewById(R.id.daohang);
        daohang.setOnClickListener(this);
        net_connect = findViewById(R.id.netconnect);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpUrl.isNetworkConnected(RadioDetailActivity.this)) {
                    rl_all.setVisibility(View.GONE);
                    net_connect.setVisibility(View.GONE);
                    getInfo(id, user_id);
                }
            }
        });
        rl_all.setVisibility(View.GONE);
        showDilog("加载中...");
    }

    private void chanColor(int postion) {
        for (int i = 0; i < tvs.size(); i++) {
            TextView tv = tvs.get(i);
            if (i == postion) {
                tv.setTextColor(Color.parseColor("#2F9BFF"));
            } else {
                tv.setTextColor(Color.parseColor("#000000"));

            }
        }
    }

    private class CollectOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.xiangqing:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.jiemu:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.xiangsi:
                    viewPager.setCurrentItem(2);
                    break;
//                case R.id.bofang:
//                    if(SaveData.getInstance().bofang_type==null){
//                        Toast.makeText(RadioDetailActivity.this,"您暂无播放记录",Toast.LENGTH_SHORT).show();
//                    } else if(SaveData.getInstance().bofang_type.equals("MV")){
//                        Intent intent1=new Intent(RadioDetailActivity.this, MVDetailActivity.class);
//                        startActivity(intent1);
//                    }else if(SaveData.getInstance().bofang_type.equals("Radio")){
//                        Intent intent1=new Intent(RadioDetailActivity.this,RadioBoFangActivity.class);
//                        startActivity(intent1);
//                    }
//                    break;
            }
        }
    }

    //获取电台信息
    private void getInfo(int id, int user_id) {

        subscription = Api.getMangoApi().getRadioInfo(id, user_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<RadioInfo> observer = new Observer<RadioInfo>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(RadioInfo radioInfo) {
            XGIMILOG.E("获取电台详情成功");
//            LogUtil.d(XGIMILOG.getTag(), ".............." + new Gson().toJson(radioInfo));
            if (radioInfo != null && radioInfo.data != null) {
                SaveData.getInstance().dianTaiClass = radioInfo.data.get(0).category_name;
                app = (App) RadioDetailActivity.this.getApplicationContext();
                String savaHomeJson = new Gson().toJson(radioInfo);
                app.savaHomeJson(id + "", savaHomeJson);
                loadHome(radioInfo);
            }
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }

    }

    private void initData() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ObjectAnimator.ofFloat(indicator, "x", NetAbout.getInstance().getScreenWidth(RadioDetailActivity.this) / 3 * (position + positionOffset) + NetAbout.getInstance().getScreenWidth(RadioDetailActivity.this) / 11).setDuration(0).start();
            }

            @Override
            public void onPageSelected(int position) {
                chanColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    gaosi.setBackgroundDrawable(new BitmapDrawable(GaoSi.doBlur(
                            bitmap, 30, false)));
                    break;
            }
        }
    };

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    //加载数据
    private void loadData(final RadioInfo data) {
        title.setText(data.data.get(0).fm_title);
        SaveData.getInstance().fm_title = data.data.get(0).fm_title;
        SaveData.getInstance().category_id = data.data.get(0).category_id;
        fm_title = data.data.get(0).fm_title;
        fm_cover = data.data.get(0).medium_thumb;
        fm_author = data.data.get(0).fm_author;
        collect_id = Integer.valueOf(data.data.get(0).collect_id);
        if (collect_id > 0) {
            isCollect = true;
            mcollect.setImageResource(R.drawable.shoucanggequ);
        } else if (collect_id == 0) {
            isCollect = false;
            mcollect.setImageResource(R.drawable.shoucang_radio);
        }
        ImageLoaderUtils.display(RadioDetailActivity.this, tupian, data.data.get(0).medium_thumb);
        SaveData.getInstance().cover_path = data.data.get(0).medium_thumb;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext()).load(data.data.get(0).medium_thumb).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        tupian.setImageBitmap(resource);
                        Message message = handler.obtainMessage();
                        message.obj = resizeImage(resource, 100, 100);
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                });
            }
        });
        FragmentAdapter mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(1);
        showFragment.getShow(id);
        sameFragment.getClassRadio(data.data.get(0).category_id, 1);
    }

    //添加fragment
    private DetailFragment detailFragment;
    private ShowFragment showFragment;
    private SameFragment sameFragment;

    private void initFragment() {
        detailFragment = new DetailFragment();
        showFragment = new ShowFragment();
        sameFragment = new SameFragment();
        fragmentList.add(detailFragment);
        fragmentList.add(showFragment);
        fragmentList.add(sameFragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collect:
                if (app.getLoginInfo() == null) {
                    SaveData.getInstance().toLogo(RadioDetailActivity.this);
                } else {
                    if (!isLogo) {
                        return;
                    }
                    if (!isCollect) {
                        setCollectFM(id, user_id, fm_title, fm_cover, fm_author);
                    } else {
                        setCancleFM(collect_id, user_id);
                    }
                }
                break;
            case R.id.daohang:
                if (Constant.netStatus) {
                    Intent intent = new Intent(this, RemountActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, NewSearchDeviceActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    //电台收藏接口
    private void setCollectFM(int fm_id, int user_id, String fm_title, String fm_cover, String fm_author) {
        subscription1 = Api.getMangoApi().getFMCollect(fm_id, user_id, fm_title, fm_cover, fm_author).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }

    Observer<FMCollect> observer1 = new Observer<FMCollect>() {
        @Override
        public void onCompleted() {
            unRegist(subscription1);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(FMCollect fmCollect) {
            if (fmCollect.code == 200) {
                ToosUtil.getInstance().addEventUmeng(RadioDetailActivity.this, "event_music_radio_collect");
                isCollect = true;
                collect_id = Integer.valueOf(fmCollect.data.collect_id);
                mcollect.setImageResource(R.drawable.shoucanggequ);
                Toast.makeText(RadioDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //取消电台收藏接口
    private void setCancleFM(int collect_id, int user_id) {
        subscription2 = Api.getMangoApi().getCancleFM(collect_id, user_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);
    }

    Observer<FMCancleCollect> observer2 = new Observer<FMCancleCollect>() {
        @Override
        public void onCompleted() {
            unRegist(subscription2);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(FMCancleCollect cancleCollect) {
            if (cancleCollect.code == 200) {
                isCollect = false;
                mcollect.setImageResource(R.drawable.shoucang_radio);
                Toast.makeText(RadioDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (app.getLoginInfo() != null) {
            gimuser = app.getLoginInfo();
            isLogo = true;
            user_id = Integer.valueOf(gimuser.data.uid);
            getInfo(id, user_id);
        }
        if (Constant.netStatus) {
            daohang.setImageResource(R.drawable.yaokongqi);
        } else {
            daohang.setImageResource(R.drawable.gimi_yaokong);

        }
    }
}
