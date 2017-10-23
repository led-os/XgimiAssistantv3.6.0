package com.xgimi.zhushou.activity;

import android.app.Activity;
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
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.PlayRecordAdapter;
import com.xgimi.zhushou.bean.FilmDetailBean;
import com.xgimi.zhushou.bean.PlayHostory;
import com.xgimi.zhushou.db.RecordDao;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.widget.SignOutDilog;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlayHostoryActivity extends Activity {
    private TextView tv_clean;
    private PlayRecordAdapter recordAdapter;
    private SwipeMenuListView mSwipeMenuListView;
    private RecordDao dao;
    private App app;
    //    private FilmDetailBean mDatas;
    private Subscription subscription;
    private List<PlayHostory> mPlayHistoryList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_hostory);
        initView();
        initData();
        initListView();
    }

    private void initView() {
        if (dao == null) {
            dao = new RecordDao(this);
        }

        mPlayHistoryList = dao.ChaXunPlay();
        app = (App) getApplicationContext();
        EventBus.getDefault().register(this);
        mSwipeMenuListView = (SwipeMenuListView) findViewById(R.id.gridview);
        tv_clean = (TextView) findViewById(R.id.tv_clean);
        recordAdapter = new PlayRecordAdapter(this, mPlayHistoryList);
        mSwipeMenuListView.setAdapter(recordAdapter);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSwipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlayHostoryActivity.this, FilmDetailActivity.class);
                PlayHostory item = recordAdapter.getItem(position);
                intent.putExtra("id", item.getId());
                startActivity(intent);
            }
        });
    }

    private boolean isInstall = true;
    private boolean mIsInstall = true;

    public void onEventMainThread(FilmDetailBean filmDetailBean) {
        boFang();
    }

    private void initData() {
        tv_clean.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SignOutDilog sin = new SignOutDilog(PlayHostoryActivity.this, "确定删除记录么?");
                sin.show();
                sin.setOnLisener(new SignOutDilog.onListern() {
                    @Override
                    public void send() {
                        // TODO Auto-generated method stub
                        dao.deletPlayHostory();
                        mPlayHistoryList = new ArrayList<PlayHostory>();
                        recordAdapter.dataChange(mPlayHistoryList);
                    }
                });
            }
        });
    }


    private void initListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                // SwipeMenuItem openItem = new SwipeMenuItem(
                // getApplicationContext());
                // // set item background
                // openItem.setBackground(new ColorDrawable(Color.rgb(0xC9,
                // 0xC9,
                // 0xCE)));
                // // set item width
                // openItem.setWidth(dp2px(90));
                // // set item title
                // openItem.setTitle("Open");
                // // set item title fontsize
                // openItem.setTitleSize(18);
                // // set item title font color
                // openItem.setTitleColor(Color.WHITE);
                // // add to menu
                // menu.addMenuItem(openItem);

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
        mSwipeMenuListView.setMenuCreator(creator);

        // step 2. listener item click event
        mSwipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                // Map<String, Object> item = fileMapGroup.get(position);
                switch (index) {
                    // case 0:
                    // // open
                    // Toast.makeText(ApplyShouActivity.this, "aa"+position,
                    // 0).show();
                    // break;
                    case 0:
                        dao.deletPlayHostory(recordAdapter.getItem(position));
                        if (mPlayHistoryList != null && mPlayHistoryList.size() - 1 - position >= 0) {
                            mPlayHistoryList.remove(mPlayHistoryList.size() - 1 - position);
                        }
                        recordAdapter.dataChange(mPlayHistoryList);
                        break;
                }
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void boFang() {
        subscription = Api.getMangoApi().getMovieDetail(SaveData.getInstance().hositoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<FilmDetailBean> observer = new Observer<FilmDetailBean>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(FilmDetailBean filmDetailBean) {
            liJiBoFang(filmDetailBean);
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }

    public void liJiBoFang(FilmDetailBean mDatas) {
        if (mDatas != null) {
            //  isInstall=false;
            if (Constant.netStatus) {
                if (ToosUtil.getInstance().isInstallTvControl(this)) {
                    ToosUtil.getInstance().addEventUmeng(PlayHostoryActivity.this, "event_movie_play");
                    if (mDatas.data != null && mDatas.data.source != null && mDatas.data.source.size() > 0) {
                        for (int i = 0; i < ToosUtil.getInstance().mInstallPacageNames.size(); i++) {
                            if (mDatas.data.source.get(0).gm_intent.p.equals(ToosUtil.getInstance().mInstallPacageNames.get(i))) {
                                isInstall = true;
//                                addSql(mId);
                                String type = mDatas.data.kind + "." + mDatas.data.category;
                                if (mDatas.data.source.get(0).gm_intent != null && mDatas.data.source.get(0).gm_intent.gm_is != null && mDatas.data.source.get(0).gm_intent.gm_is != null) {
                                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(PlayHostoryActivity.this), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(
                                            type, mDatas.data.title, mDatas.data.id, 0, null, null,
                                            DeviceUtils.getappVersion(PlayHostoryActivity.this),
                                            mDatas.data.source.get(0).gm_intent.n,
                                            mDatas.data.source.get(0).gm_intent.o,
                                            mDatas.data.source.get(0).gm_intent.u,
                                            mDatas.data.source.get(0).gm_intent.p,
                                            mDatas.data.source.get(0).gm_intent.gm_is.i,
                                            mDatas.data.source.get(0).gm_intent.gm_is.m), null, null, null, null)));
                                }
//                                    Toast.makeText(FilmDetailActivity.this, "正在无屏电视上打开" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                                Intent inten = new Intent(PlayHostoryActivity.this, RemountActivity.class);
                                startActivity(inten);

                            }
                        }
                        if (isInstall) {
                            Toast.makeText(PlayHostoryActivity.this, "正在无屏电视上打开" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                        } else {
                            String type1 = mDatas.data.kind + "." + mDatas.data.category;
//                                Toast.makeText(FilmDetailActivity.this, "正在无屏电视上安装" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(PlayHostoryActivity.this), App.getContext().PACKAGENAME,
                                    new VcontrolCmd.ThirdPlay(
                                            type1, mDatas.data.title, mDatas.data.id, 0, null, null,
                                            DeviceUtils.getappVersion(PlayHostoryActivity.this),
                                            mDatas.data.source.get(0).gm_intent.n,
                                            mDatas.data.source.get(0).gm_intent.o,
                                            mDatas.data.source.get(0).gm_intent.u,
                                            mDatas.data.source.get(0).gm_intent.p,
                                            mDatas.data.source.get(0).gm_intent.gm_is.i,
                                            mDatas.data.source.get(0).gm_intent.gm_is.m), null, null, null, null)));
                            if (mIsInstall) {
                                Toast.makeText(PlayHostoryActivity.this, "正在无屏电视上打开" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                                Intent inten = new Intent(PlayHostoryActivity.this, RemountActivity.class);
                                startActivity(inten);
                            } else {
                                Toast.makeText(PlayHostoryActivity.this, "正在无屏电视上安装" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                            }
//                                Intent inten = new Intent(FilmDetailActivity.this, RemountActivity.class);
//                                startActivity(inten);
                        }

//                            else{
//                                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(mDatas.data.source.get(0).gm_intent.n,
//                                        mDatas.data.source.get(0).gm_intent.o,
//                                        mDatas.data.source.get(0).gm_intent.u,
//                                        mDatas.data.source.get(0).gm_intent.p,
//                                        mDatas.data.source.get(0).gm_intent.gm_is.get(0).i,
//                                        mDatas.data.source.get(0).gm_intent.gm_is.get(0).m), null, null, null, null)));

//                            }
                    }
                }

            } else {
                ToosUtil.getInstance().isConnectTv(this);
            }
        }
    }
}
