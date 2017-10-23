package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistviewsample.SwipeMenu;
import com.baoyz.swipemenulistviewsample.SwipeMenuCreator;
import com.baoyz.swipemenulistviewsample.SwipeMenuItem;
import com.baoyz.swipemenulistviewsample.SwipeMenuListView;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MyCollectAppAdapter;
import com.xgimi.zhushou.bean.AppCollect;
import com.xgimi.zhushou.bean.AppCollectBeen;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.netUtil.Api;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/27.
 */
public class MyAppCollectActivity extends BaseActivity{
    private SwipeMenuListView listView;
    private TextView title;
    private Subscription subscription;
    int page=1;
    int psize=1000;
    int is_game=0;
    private App app;
    private int user_id;
    private GimiUser gimuser;
    private MyCollectAppAdapter adapter;
    private String app_id;
    private List<AppCollectBeen.data> mData=new ArrayList<>();
    private ImageView back;
    private View load_false;
    private TextView nodata;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_app_collect);
        initExtra();
        initView();
        getList(user_id,page,psize,is_game);
        initData();
    }
    private void initExtra() {
        app = (App) getApplicationContext();
        if (app.getLoginInfo() != null) {
            gimuser = app.getLoginInfo();
            user_id = Integer.valueOf(gimuser.data.uid);
        }
    }
    private void initView() {
        listView= (SwipeMenuListView) findViewById(R.id.lisetview);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        title.setText("应用收藏");
        adapter=new MyCollectAppAdapter(0,MyAppCollectActivity.this, mData);
        listView.setAdapter(adapter);
        leftSlide();
        load_false=findViewById(R.id.load_false);
        back= (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        showDilog("加载中...");
        nodata= (TextView) findViewById(R.id.nodata);
    }
    private void getList(int user_id,int page,int psize,int is_game) {
        subscription= Api.getMangoApi().getAppCollectList(user_id,page,psize,is_game).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<AppCollectBeen> observer=new Observer<AppCollectBeen>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
            MissDilog();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            MissDilog();
            load_false.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
        }
        @Override
        public void onNext(AppCollectBeen appCollectBeen) {
            if(appCollectBeen!=null&&appCollectBeen.data!=null){
                mData.addAll(appCollectBeen.data);
                adapter.dataChange(mData);
            }else {
                load_false.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
            MissDilog();
        }
    };
    private void unRegist(Subscription sub){
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }
    //跳转逻辑
    private void initData() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              jump(position);
            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
//                    case 0:
//                        // open
//                        jump(position);
//                        break;
                    case 0:
                        // delete
                        //delete(item);
                        app_id=mData.get(position).id;
                        mData.remove(position);
                        adapter.notifyDataSetChanged();
                        getCancleAppCollect(user_id,app_id);
                        break;
                }
            }
        });
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {

            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }
    private void leftSlide() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(120);
//                // set item title
//                openItem.setTitle("打开");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(80));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        listView.setMenuCreator(creator);
    }
    private void jump(int position){
        Intent intent=new Intent(MyAppCollectActivity.this, ApplyDetailActivity.class);
        intent.putExtra("id",adapter.getItem(position).id);
        startActivity(intent);
    }
    private Subscription subscription3;
    //取消收藏
    private void getCancleAppCollect(int user_id,String app_id) {
        subscription3 = Api.getMangoApi().getCancleAppCollect(user_id,app_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer3);
    }
    Observer<AppCollect> observer3 =new Observer<AppCollect>() {
        @Override
        public void onCompleted() {
            unRegist(subscription3);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(AppCollect appCollect) {
            if(appCollect!=null&&appCollect.data!=null){
                if(appCollect.code==200&&appCollect.data.equals("success")){
                    Toast.makeText(MyAppCollectActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
