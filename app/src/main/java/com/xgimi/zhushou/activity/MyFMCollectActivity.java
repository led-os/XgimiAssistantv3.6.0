package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
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
import com.xgimi.zhushou.adapter.MyCollectFMAdapter;
import com.xgimi.zhushou.bean.FMCancleCollect;
import com.xgimi.zhushou.bean.FMCollectList;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.netUtil.Api;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/26.
 */
public class MyFMCollectActivity extends BaseActivity{
    private SwipeMenuListView listView;
    private TextView title;
    private Subscription subscription;
    int page=1;
    int psize=1000;
    private int type=1;
    private App app;
    private int user_id;
    private GimiUser gimuser;
    private MyCollectFMAdapter adapter;
    private int collect_id;
    private ImageView back;
    private AnimationDrawable animationDrawable;
    private View load_false;
    private TextView nodata;
    private List<FMCollectList.data> mData=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fm_collect);
        initExtra();
        initView();
        getList(page,psize,user_id,type);
        initData();
    }
    private void initExtra() {
        app = (App) getApplicationContext();
        if(app.getLoginInfo()!=null){
            gimuser = app.getLoginInfo();
            user_id= Integer.valueOf(gimuser.data.uid);
        }
    }
    private void initView() {
        listView= (SwipeMenuListView) findViewById(R.id.lisetview);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        title.setText("电台收藏");
        adapter=new MyCollectFMAdapter(0,MyFMCollectActivity.this, mData);
        listView.setAdapter(adapter);
        leftSlide();
        load_false=findViewById(R.id.load_false);
        back= (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        showDilog("加载中...");
        nodata= (TextView) findViewById(R.id.nodata);
    }
    private void getList(int page,int psize,int user_id, int type) {
        subscription= Api.getMangoApi().getFMCollectList(page,psize,user_id,type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<FMCollectList> observer=new Observer<FMCollectList>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
            MissDilog();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            load_false.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
            MissDilog();
        }

        @Override
        public void onNext(FMCollectList fmCollectList) {
            if(fmCollectList!=null&&fmCollectList.data!=null){
                loadData(fmCollectList);
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
    //加载数据
    private void loadData(FMCollectList fmCollectList) {
        mData.addAll(fmCollectList.data);
        adapter.dataChange(mData);
        MissDilog();
    }
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
                        collect_id=Integer.valueOf(mData.get(position).collect_id);
                        mData.remove(position);
                        adapter.notifyDataSetChanged();
                        setCancleFM(collect_id,user_id);
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
        Intent intent = new Intent(MyFMCollectActivity.this, RadioDetailActivity.class);
        Bundle bundle = new Bundle();
        int mf_id=Integer.valueOf(mData.get(position).mf_id);
        bundle.putSerializable("id",mf_id);
        intent.putExtras(bundle);
        intent.putExtra("title", "专辑");
        startActivity(intent);
    }
    private Subscription subscription2;
    //取消电台收藏接口
    private void setCancleFM(int collect_id,int user_id) {
        subscription2= Api.getMangoApi().getCancleFM(collect_id,user_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);
    }
    Observer<FMCancleCollect> observer2=new Observer<FMCancleCollect>() {
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
            if(cancleCollect.code==200){
                Toast.makeText(MyFMCollectActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
