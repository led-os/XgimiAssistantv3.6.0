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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistviewsample.SwipeMenu;
import com.baoyz.swipemenulistviewsample.SwipeMenuCreator;
import com.baoyz.swipemenulistviewsample.SwipeMenuItem;
import com.baoyz.swipemenulistviewsample.SwipeMenuListView;
import com.xgimi.zhushou.aes.MyAesToGsonFactory;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MyCollectMovieAdapter;
import com.xgimi.zhushou.bean.CancleMovieCollect;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.MovieCollectBeen;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/27.
 */
public class MyMovieCollectActivity extends BaseActivity {
    private SwipeMenuListView listView;
    private TextView title;
    private Subscription subscription;
    int page = 1;
    int psize = 1000;
    private App app;
    private String id;
    private int user_id;
    private GimiUser gimuser;
    private MyCollectMovieAdapter adapter;
    private String video_id;
    private RelativeLayout load;
    private ImageView load_iv;
    private AnimationDrawable animationDrawable;
    private List<MovieCollectBeen.data> mData = new ArrayList<>();
    private ImageView back;
    private View load_false;
    private TextView nodata;
    private int mCurrentCollectionPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_movie_collect);
        initExtra();
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getList(user_id);
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
                        video_id = mData.get(position).video_id;
                        mData.remove(position);
                        adapter.notifyDataSetChanged();
                        getCancleCollect(user_id, video_id);
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

    private void initExtra() {
        app = (App) getApplicationContext();
        if (app.getLoginInfo() != null) {
            gimuser = app.getLoginInfo();
            user_id = Integer.valueOf(gimuser.data.uid);
        }
    }

    private void initView() {
        listView = (SwipeMenuListView) findViewById(R.id.lisetview);
        title = (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        title.setText("影视收藏");
        adapter = new MyCollectMovieAdapter(0, MyMovieCollectActivity.this, mData);
        listView.setAdapter(adapter);
        leftSlide();
        load_false = findViewById(R.id.load_false);
        back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        showDilog("加载中...");
        nodata = (TextView) findViewById(R.id.nodata);
    }

    private void getList(int user_id) {
//        subscription = Api.getMangoApi().getMovieCollectList(page, psize, user_id).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
        XGIMILOG.D("开始获取影视收藏列表");
        if (mCurrentCollectionPage <= 1) {
            mCurrentCollectionPage = 1;
        } else {
            mCurrentCollectionPage++;
        }
        try {
            subscription = Api.getXgimiVideoApi(MyAesToGsonFactory.create(MovieCollectBeen.class))
                    .getMovieCollectList(Api.getEncodeParam(new String[]{"user_id", "page"}, new String[]{user_id + "", mCurrentCollectionPage + ""}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Observer<MovieCollectBeen> observer = new Observer<MovieCollectBeen>() {
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
        public void onNext(MovieCollectBeen movieCollectBeen) {
//             Gson gson = new Gson();
//            String json = gson.toJson(movieCollectBeen);
            if (movieCollectBeen != null && movieCollectBeen.data.size() != 0) {
                loadData(movieCollectBeen);
            } else {
                load_false.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                MissDilog();
            }
        }
    };

    //加载数据
    private void loadData(MovieCollectBeen movieCollectBeen) {
//        mData.addAll(movieCollectBeen.data);
        mData = movieCollectBeen.data;
        adapter.dataChange(mData);
//        load.setVisibility(View.GONE);
        MissDilog();
    }

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }

    private Subscription subscription3;

    //取消收藏
    private void getCancleCollect(int user_id, String video_id) {
        try {
            subscription3 = Api.getXgimiVideoApi(MyAesToGsonFactory.create(CancleMovieCollect.class))
                    .CancleMovieCollect(Api.getEncodeParam(new String[]{"user_id", "video_id"}, new String[]{user_id + "", video_id}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Observer<CancleMovieCollect> observer3 = new Observer<CancleMovieCollect>() {
        @Override
        public void onCompleted() {
            unRegist(subscription3);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(CancleMovieCollect cancleMovieCollect) {
            if (cancleMovieCollect != null && cancleMovieCollect.data != null && cancleMovieCollect.code == 200) {
                Toast.makeText(MyMovieCollectActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void leftSlide() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getApplicationContext());
                // set item background
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

    private void jump(int position) {
        Intent intent = new Intent(MyMovieCollectActivity.this, FilmDetailActivity.class);
        intent.putExtra("id", mData.get(position).video_id);
        startActivity(intent);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
