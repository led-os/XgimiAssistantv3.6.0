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
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MyCollectMVAdapter;
import com.xgimi.zhushou.bean.CancleCollect;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.MVCollectList;
import com.xgimi.zhushou.bean.MVList;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/25.
 */
public class MyMvCollectActivity extends BaseActivity {
    private SwipeMenuListView listView;
    private TextView title;
    private MyCollectMVAdapter adapter;
    private Subscription subscription;
    private int user_id;
    private int collect_id;
    int page = 1;
    int psize = 1000;
    private int type = 0;
    private App app;
    private GimiUser gimuser;
    private Subscription subscription1;
    private RelativeLayout load;
    private ImageView load_iv;
    private AnimationDrawable animationDrawable;
    private MVList.data collectList;
    private List<MVList.data> collectList1 = new ArrayList<>();
    private List<MVCollectList.data> mData = new ArrayList<>();

    private ImageView back;
    private View load_false;
    private TextView nodata;
    List<VcontrolCmd.CustomPlay.PlayList> mPlyLists = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mv_collect);
        initExtra();
        initView();
        getList(page, psize, user_id, type);
        initData();
    }

    private void initData() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Constant.netStatus) {
                    jump(position);
                } else {
                    ToosUtil.getInstance().isConnectTv(MyMvCollectActivity.this);
                }
            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                int item = position;
                switch (index) {
//                    case 0:
//                        // open
//                        jump(item);
//                        break;
                    case 0:
                        // delete
                        //delete(item);
                        collect_id = Integer.valueOf(mData.get(position).collect_id);
                        mData.remove(position);
                        adapter.notifyDataSetChanged();
                        setCancelMV(collect_id, user_id);
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

    //取消收藏
    public void setCancelMV(int collect_id, int user_id) {
        subscription1 = Api.getMangoApi().getCancleMV(collect_id, user_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }

    Observer<CancleCollect> observer1 = new Observer<CancleCollect>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(CancleCollect collect) {
            if (collect != null) {
                if (collect.code == 200) {
                    if (Integer.parseInt(collect.data.status) == 1) {
                        Toast.makeText(MyMvCollectActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    private void initExtra() {
        app = (App) getApplicationContext();
        if (app.getLoginInfo() != null) {
            gimuser = app.getLoginInfo();
            user_id = Integer.valueOf(gimuser.data.uid);
        }
    }

    private void initView() {
//        mData = getPackageManager().getInstalledApplications(0);
        title = (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        title.setText("MV收藏");
        listView = (SwipeMenuListView) findViewById(R.id.lisetview);
        adapter = new MyCollectMVAdapter(0, MyMvCollectActivity.this, mData);
        listView.setAdapter(adapter);
        leftSlide();
        load_false = findViewById(R.id.load_false);
        back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        showDilog("加载中...");
        nodata = (TextView) findViewById(R.id.nodata);
    }

    private void getList(int page, int psize, int user_id, int type) {
        subscription = Api.getMangoApi().getMVCollectList(page, psize, user_id, type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<MVCollectList> observer = new Observer<MVCollectList>() {
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
        public void onNext(MVCollectList mvCollectList) {
            if (mvCollectList != null && mvCollectList.data.size() != 0) {
                loadData(mvCollectList);
            } else {
                load_false.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                MissDilog();
            }
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }

    //加载数据
    private void loadData(MVCollectList mvCollectList) {
        mData.addAll(mvCollectList.data);
        adapter.dataChange(mData);
//        load.setVisibility(View.GONE);
        MissDilog();
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

    public MVList list;

    private void jump(int position) {
        Intent intent = new Intent(MyMvCollectActivity.this, MVDetailActivity.class);
        intent.putExtra("title", mData.get(position).mf_title);
        intent.putExtra("artist", mData.get(position).mf_author);
        intent.putExtra("bitmap", mData.get(position).mf_cover);
        intent.putExtra("mv_play_address", mData.get(position).mf_play_address);
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", 1);
        intent.putExtras(bundle);
        intent.putExtra("collect_id", mData.get(position).collect_id);
        intent.putExtra("mv_id", mData.get(position).mf_id);
        SaveData.getInstance().mv_id = mData.get(position).mf_id;
        intent.putExtra("ivplaytype", "true");
        SaveData.getInstance().isPlay = true;


        SaveData.getInstance().mv_title = mData.get(position).mf_title;
        SaveData.getInstance().mv_artist = mData.get(position).mf_author;
        SaveData.getInstance().bitmap = mData.get(position).mf_cover;
        SaveData.getInstance().mvid = mData.get(position).mf_id;
        SaveData.getInstance().position = position;
        SaveData.getInstance().mv_play_address = mData.get(position).mf_play_address;
        SaveData.getInstance().collect_id = mData.get(position).collect_id;
        startActivity(intent);
        mPlyLists.clear();
        collectList1.clear();
        for (int i = 0; i < mData.size(); i++) {
            collectList = new MVList.data(mData.get(i).collect_id, mData.get(i).mf_title, mData.get(i).mf_author, mData.get(i).mf_cover, mData.get(i).mf_play_address, mData.get(i).collect_id, mData.get(i).mf_type);
            collectList1.add(collectList);
        }
        SaveData.getInstance().mList = collectList1;
        for (int i = 0; i < mData.size(); i++) {
            VcontrolCmd.CustomPlay.PlayList mPlayList = new VcontrolCmd.CustomPlay.PlayList(null, mData.get(i).mf_id, null, mData.get(i).mf_title, null, mData.get(i).mf_play_address, null
                    , 0, null, App.getContext().YINYUETAI);
            mPlyLists.add(mPlayList);
        }

        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                GMSdkCheck.appId, DeviceUtils.getappVersion(MyMvCollectActivity.this), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(mData.get(position).mf_title, mData.get(position).mf_id
                , 0, null, App.getContext().YINYUETAI),
                new VcontrolCmd.CustomPlay(0, 0, null, mPlyLists, position),
                null, null, null)));
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
