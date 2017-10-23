package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.YingYongAdapter;
import com.xgimi.zhushou.bean.ApplySearc;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MoreAppActivity extends BaseActivity implements View.OnClickListener,
        OnRefreshListener2<ScrollView>, View.OnTouchListener {

    private HorizontalScrollView horscrollview;
    private MyGridView appLieBiao;
    private List<ApplySearc.ApplySearchItem> applySearc = new ArrayList<>();
    private YingYongAdapter yingYongAdapter;
    private String type;
    private LinearLayout linearLayout;
    private String kind="全部";
    private int page = 1;
    private int pageSize = 32;
    private PullToRefreshScrollView pull;
    private int chushihua = -1;
    private RelativeLayout ll_have_net;
    private View net_connect;
    private App app;
    private View prog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_app);
        initExras();
        initView();
        initData();
        initLocal();
        addRemen();
    }

    private void initExras() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
    }

    private void initView() {
        controlTitle(findViewById(R.id.id_toolbar),true,true,false,false);
        app= (App) MoreAppActivity.this.getApplicationContext();
        if(type.equals("0")) {
            tv.setText("应用");
        }else{
            tv.setText("游戏");
        }
        ll_have_net = (RelativeLayout) findViewById(R.id.ll_have_net);
        horscrollview = (HorizontalScrollView) findViewById(R.id.apply_horscrollview);
        appLieBiao = (MyGridView) findViewById(R.id.app_liebiao);
        pull = (PullToRefreshScrollView) findViewById(R.id.scrollview);
        pull.setOnRefreshListener(this);
        appLieBiao.setFocusable(false);
        net_connect = findViewById(R.id.netconnect);
        prog=findViewById(R.id.myprog);
        prog.setVisibility(View.VISIBLE);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HttpUrl.isNetworkConnected(MoreAppActivity.this)){
                    ll_have_net.setVisibility(View.GONE);
                    net_connect.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);
                    getApplySearch(page, pageSize, type, kind);
                }
            }
        });
    }
    //读取缓存数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson(type);
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            ApplySearc data = new Gson().fromJson(readHomeJson, ApplySearc.class);
            loadHome(data);
        } else {
            if (!HttpUrl.isNetworkConnected(MoreAppActivity.this)) {
                prog.setVisibility(View.GONE);
                net_connect.setVisibility(View.VISIBLE);
                ll_have_net.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(MoreAppActivity.this)) {
            getApplySearch(page, pageSize, type, kind);
        }
    }

    private void loadHome(ApplySearc data) {
        loadApply(data);
    }

    private void initData() {
        yingYongAdapter = new YingYongAdapter(this, applySearc);
        appLieBiao.setAdapter(yingYongAdapter);
        appLieBiao.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MoreAppActivity.this,
                        ApplyDetailActivity.class);
                intent.putExtra("id", yingYongAdapter.getItem(position).id);
                startActivity(intent);
            }
        });
    }

    // 添加头部
    private void addRemen() {
        List<String> titile = new ArrayList<String>();
        titile.add("全部分类:");
        if(SaveTVApp.getInstance(this).feilei!=null&&SaveTVApp.getInstance(this).feilei.data!=null){
            for (int j = 0; j < SaveTVApp.getInstance(this).feilei.data.size(); j++) {
                if (SaveTVApp.getInstance(this).feilei.data.get(j).type
                        .equals(type)) {
                    titile.add(SaveTVApp.getInstance(this).feilei.data.get(j).name);
                }
            }
        }
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < titile.size(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, ToosUtil.getInstance().getScreenHeight(this) / 19);
            if (kind.equals(titile.get(i))) {
                chushihua = i;
            } else if (kind.equals("全部")) {
                chushihua = 0;
            }

            final TextView tv = new TextView(this);
            tv.setTextSize(15);
            final int postion = i;
            layoutParams.gravity = Gravity.CENTER;
            tv.setTextColor(Color.parseColor("#ADADAD"));
            if (i == chushihua) {
                tv.setTextColor(Color.parseColor("#388AEF"));
            }

            linearLayout.addView(tv);
            if (i != 0) {
                layoutParams.leftMargin = 30;
            }
            tv.setText(titile.get(i));
            tv.setLayoutParams(layoutParams);
            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    chanColor(postion);
                    page = 1;
                    isShangLa = false;
                    applySearc.clear();
                    kind = tv.getText().toString().trim();
                    if (postion == 0) {
                        kind = null;
                    }
                    prog.setVisibility(View.VISIBLE);
                    appLieBiao.setVisibility(View.GONE);
                    getApplySearch(page, pageSize, type, kind);
                }
            });
        }
        horscrollview.addView(linearLayout);
    }

    public void chanColor(int a) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            TextView tv = (TextView) linearLayout.getChildAt(i);
            if (a == i) {
                // if(a==0){
                // return;
                // }else{
                tv.setTextColor(Color.parseColor("#388AEF"));
                // }
            } else {
                tv.setTextColor(Color.parseColor("#ADADAD"));
            }
        }
    }

    Observer<ApplySearc> observer = new Observer<ApplySearc>() {

        @Override
        public void onCompleted() {
            unsubscribe(subscription);
            pull.onRefreshComplete();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(ApplySearc data) {

            if (data != null && data.data != null) {
                if(mpage==1){
                    app= (App) MoreAppActivity.this.getApplicationContext();
                    String savaHomeJson=new Gson().toJson(data);
                    app.savaHomeJson(type,savaHomeJson);
                    isShangLa=false;
                }
                loadHome(data);
            }
        }
    };
    private int mpage;
    private void getApplySearch(int page, int pageSize, String isGame,
                                String kind){
        mpage=page;
        subscription = Api.getMangoApi().getApply(isGame,kind,null,pageSize,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
//    // 获取应用
//    public void getApplySearch(int page, int pageSize, String isGame
//                               ) {
//        HttpRequest.getInstance(this).getAppSearch(isGame, kind, null,
//                pageSize, page, new CommonCallBack<ApplySearc>() {
//
//                    @Override
//                    public void onSuccess(ApplySearc data) {
//                        // TODO Auto-generated method stub
//                        if (!isFirst) {
//                            applySearc.clear();
//                            isFirst = true;
//                        }
//                        loadApply(data);
//                    }
//
//                    @Override
//                    public void onStart() {
//                        // TODO Auto-generated method stub
//                    }
//
//                    @Override
//                    public void onFailed(String reason) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        // TODO Auto-generated method stub

        isShangLa = false;
        page = 1;
        applySearc.clear();
        getApplySearch(page, pageSize, type, kind);

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        // TODO Auto-generated method stub

        isShangLa = true;
        page++;
        getApplySearch(page, pageSize, type, kind);
    }
    private boolean isShangLa=false;
    private ImageView yaokonqi;
    private ImageView ivback;

    private void loadApply(ApplySearc data) {
        prog.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);
        ll_have_net.setVisibility(View.VISIBLE);
        if (isShangLa) {
            applySearc.addAll(data.data);
        } else {
            applySearc = data.data;
        }
        yingYongAdapter.dataChange(applySearc);
        appLieBiao.setVisibility(View.VISIBLE);
        pull.onRefreshComplete();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        return false;
    }

}
