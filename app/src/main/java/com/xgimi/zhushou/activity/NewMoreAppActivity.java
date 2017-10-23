package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MoreAppRcvAdapter;
import com.xgimi.zhushou.bean.ApplySearc;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.ToastUtil;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XGIMI on 2017/9/13.
 */

public class NewMoreAppActivity extends BaseActivity {


    private static final String[] APP_CLASSIFY_ARR = {"全部", "实用工具", "其他", "美食健康", "便捷生活", "音乐图片", "儿童教育", "新闻阅读"};
    private static final String[] GAME_CLASSIFY_ARR = {"全部", "射击分类", "竞速体育", "益智休闲", "动作冒险", "棋牌游戏", "模拟器", "游戏大厅", "角色扮演"};

    private static final int PAGE_SIZE = 32;

    private String[] mSelectionTitle;
    private List<TextView> mTitleTvList;

    private FrameLayout mNetDisconnectFl;
    private RelativeLayout mLoadFalseRl;
    private RelativeLayout mDataRl;
    private LinearLayout mSelectionLl;
    private RecyclerView mRcv;
    private SmartRefreshLayout mRefreshLayout;
    private List<ApplySearc.ApplySearchItem> mAppDataList = new ArrayList<>();
    private MoreAppRcvAdapter mAdapter;

    private int mCurrentPage = 1;
    private String mIsGame;
    private String mClassify = "全部";
    private boolean mJustPullDown;
    private boolean mOtherClassify;

    /**
     * Intent intent = new Intent(MoreAppActivity.this,
     * ApplyDetailActivity.class);
     * intent.putExtra("id", yingYongAdapter.getItem(position).id);
     * startActivity(intent);
     */

    private MoreAppRcvAdapter.OnAppItemClickListener mOnAppItemClickListener = new MoreAppRcvAdapter.OnAppItemClickListener() {
        @Override
        public void onAppItemClick(ApplySearc.ApplySearchItem item) {
            Intent intent = new Intent(NewMoreAppActivity.this,
                    ApplyDetailActivity.class);
            intent.putExtra("id", item.id);
            startActivity(intent);
        }
    };

    private View.OnClickListener mOnNetDisConnectClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (NetUtil.isNetConnected(NewMoreAppActivity.this)) {
                mNetDisconnectFl.setVisibility(View.GONE);
                mLoadFalseRl.setVisibility(View.GONE);
                mDataRl.setVisibility(View.VISIBLE);
                mCurrentPage = 1;
                showDilog("加载中...");
                initData(mCurrentPage);
            } else {
                ToastUtil.getToast("网络已断开，请稍后再试", NewMoreAppActivity.this).show();
            }
        }
    };

    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            XGIMILOG.E("");
            mJustPullDown = true;
            mCurrentPage = 1;
            initData(mCurrentPage);
        }
    };

    private OnLoadmoreListener mOnLoadMoreListener = new OnLoadmoreListener() {
        @Override
        public void onLoadmore(RefreshLayout refreshlayout) {
            XGIMILOG.E("");
            mJustPullDown = false;
            mCurrentPage++;
            initData(mCurrentPage);
        }
    };

    public Subscription mSubscription;
    private Observer<ApplySearc> mObs = new Observer<ApplySearc>() {
        @Override
        public void onCompleted() {
            unsubscribe(mSubscription);
            MissDilog();
        }

        @Override
        public void onError(Throwable e) {
            if (mJustPullDown) {
                mRefreshLayout.finishRefresh();
            } else {
                mRefreshLayout.finishLoadmore();
            }
        }

        @Override
        public void onNext(ApplySearc applySearc) {
            XGIMILOG.E("获取数据完成");

//            LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(applySearc));
            if (applySearc != null && applySearc.data != null) {
                loadData(applySearc);
            }
            if (mJustPullDown) {
                mRefreshLayout.finishRefresh();
            } else {
                mRefreshLayout.finishLoadmore();
            }
        }
    };

    private void loadData(ApplySearc applySearc) {
        if (mAdapter == null) {
            mAdapter = new MoreAppRcvAdapter(mAppDataList, LayoutInflater.from(this));
            mAdapter.setOnAppItemClickListener(mOnAppItemClickListener);
            mRcv.setAdapter(mAdapter);
        }
        if (mOtherClassify) {
            XGIMILOG.E("mOtherClassify");
            mAppDataList.clear();
            mAppDataList.addAll(applySearc.data);
            mOtherClassify = false;
        } else {
            if (!mJustPullDown) {
                XGIMILOG.E("上拉");
                mAppDataList.addAll(applySearc.data);
            } else {
                XGIMILOG.E("下拉");
                mAppDataList.clear();
                mAppDataList.addAll(applySearc.data);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_more_app_activity_layout);
        initExtra();
        initView();
        initSelectionLayout();
        if (NetUtil.isNetConnected(this)) {
            mLoadFalseRl.setVisibility(View.GONE);
            mNetDisconnectFl.setVisibility(View.GONE);
            mDataRl.setVisibility(View.VISIBLE);
            showDilog("加载中...");
            initData(mCurrentPage);

        } else {
            mLoadFalseRl.setVisibility(View.GONE);
            mNetDisconnectFl.setVisibility(View.VISIBLE);
            mDataRl.setVisibility(View.GONE);
        }

    }

    private void initSelectionLayout() {
        for (int i = 0; i < mSelectionTitle.length; i++) {
            String title = mSelectionTitle[i];
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    ToosUtil.getInstance().getScreenHeight(this) / 19);
            params.gravity = Gravity.CENTER;
            params.leftMargin = 30;
            params.topMargin = 20;
            params.bottomMargin = 10;
            TextView titleTv = new TextView(this);
            titleTv.setTextSize(15);
            if (title.equals(mSelectionTitle[0])) {
                titleTv.setTextColor(Color.parseColor("#388AEF"));
            } else {
                titleTv.setTextColor(Color.parseColor("#ADADAD"));
            }
            titleTv.setText(title);
            titleTv.setOnClickListener(new OnTitleTvClickListener(i));
            titleTv.setLayoutParams(params);
            if (mTitleTvList == null) {
                mTitleTvList = new ArrayList<>();
            }
            mTitleTvList.add(titleTv);
            mSelectionLl.addView(titleTv);
        }
    }

    private void initExtra() {
        Intent intent = getIntent();
        if (intent != null) {
            mIsGame = intent.getStringExtra("type");
            if (mIsGame.equals("0")) {
                mSelectionTitle = APP_CLASSIFY_ARR;
            } else {
                mSelectionTitle = GAME_CLASSIFY_ARR;
            }

        }
    }

    /**
     * private void getApplySearch(int page, int pageSize, String isGame,
     * String kind){
     */
    private void initData(int page) {
        XGIMILOG.E(mCurrentPage + "");

        subscription = Api.getMangoApi().getApply(mIsGame, mClassify, null, PAGE_SIZE, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObs);
    }

    private void initView() {
        controlTitle(findViewById(R.id.title), true, true, false, false);
        if (mIsGame.equals("0")) {
            tv.setText("应用");
        } else {
            tv.setText("游戏");
        }
        mDataRl = (RelativeLayout) findViewById(R.id.rl_data_more_app_activity);
        mNetDisconnectFl = (FrameLayout) findViewById(R.id.fl_net_disconnect_more_app_activity);
        mNetDisconnectFl.setOnClickListener(mOnNetDisConnectClickListener);
        mLoadFalseRl = (RelativeLayout) findViewById(R.id.rl_load_false_more_app_activity);
        mRcv = (RecyclerView) findViewById(R.id.rcv_more_app_activity);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.view_fresh_more_app_activity);
        mSelectionLl = (LinearLayout) findViewById(R.id.ll_selection_more_app_activity);
        mNetDisconnectFl = (FrameLayout) findViewById(R.id.fl_net_disconnect_more_app_activity);

        mRefreshLayout.setOnLoadmoreListener(mOnLoadMoreListener);
        mRefreshLayout.setOnRefreshListener(mOnRefreshListener);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRcv.setLayoutManager(layoutManager);
    }

    private class OnTitleTvClickListener implements View.OnClickListener {

        private int postion;

        public OnTitleTvClickListener(int postion) {
            this.postion = postion;
        }

        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTitleTvList.size(); i++) {
                mTitleTvList.get(i).setTextColor(Color.parseColor("#ADADAD"));
            }
            ((TextView) v).setTextColor(Color.parseColor("#388AEF"));
            XGIMILOG.E(mSelectionTitle[postion]);
            showDilog("加载中...");
            mOtherClassify = true;
            mClassify = mSelectionTitle[postion];
            initData(1);
        }
    }
}
