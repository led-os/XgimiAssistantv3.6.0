package com.xgimi.zhushou.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.RecordAdatper;
import com.xgimi.zhushou.adapter.SearchLiveAdapter;
import com.xgimi.zhushou.bean.SearchLiveRecord;
import com.xgimi.zhushou.bean.SearchLiveTv;
import com.xgimi.zhushou.db.RecordDao;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.Statics;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.MyEditText;
import com.xgimi.zhushou.widget.MyListview;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchLiveActivity extends BaseActivity {


    private static final String TAG = "SearchLiveActivity";
    @Bind(R.id.search)
    MyEditText mEdittextSearch;
    @Bind(R.id.hortory_listview)
    MyListview mHoslistView;
    @Bind(R.id.detail_recylerview)
    ListView mDetailListview;
    @Bind(R.id.iv_cancel)
    TextView mCancel;
    @Bind(R.id.scrollview)
    ScrollView mScrollview;
    private RecordDao recordDao;
    private RecordAdatper mRecordAdapter;
    private List<SearchLiveTv.DataBean> mDatas;
    private int mPage = 1, mPagetsize = 30;
    private SearchLiveAdapter mDetailAdapter;
    private TextView clean;
    private RelativeLayout lishi;
    private View nodata;
    private View close;
    private boolean isclose = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_search_live, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        initView();
        initData();
        Log.d(TAG, "onCreate: ");
    }

    private void initView() {
        recordDao = new RecordDao(this);
        EventBus.getDefault().register(this);
        Editable etext = mEdittextSearch.getText();
        Selection.setSelection(etext, etext.length());
        mRecordAdapter = new RecordAdatper(this, recordDao.getRecord());
        mHoslistView.setAdapter(mRecordAdapter);
        mDetailAdapter = new SearchLiveAdapter(this, mDatas);
        mDetailListview.setAdapter(mDetailAdapter);
        clean = (TextView) findViewById(R.id.tv_clean_record);
        lishi = (RelativeLayout) findViewById(R.id.lishi);
        nodata = findViewById(R.id.nodata);
        close = findViewById(R.id.close);
        if (SaveData.getInstance().channel == 0) {
            close.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
            mDetailListview.setVisibility(View.GONE);
            mScrollview.setVisibility(View.GONE);
            isclose = true;
        } else {
            isclose = false;
        }
        jiance();
    }

    public void onEventMainThread(SearchLiveRecord record) {
        recordDao.deleteAlread(record.name);
        mRecordAdapter.changeData((ArrayList<SearchLiveRecord>) recordDao.getRecord());
        jiance();
    }

    private void jiance() {
        List<SearchLiveRecord> record3 = recordDao.getRecord();
        if (record3.isEmpty()) {
            lishi.setVisibility(View.GONE);
        } else {
            lishi.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        mDetailListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Constant.netStatus) {
                    if (ToosUtil.getInstance().isInstallTvControl(SearchLiveActivity.this)) {

                        Toast.makeText(SearchLiveActivity.this, "正在电视上打开", Toast.LENGTH_SHORT).show();
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "11", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(mDetailAdapter.getItem(position).gm_intent.n, null, mDetailAdapter.getItem(position).gm_intent.u, mDetailAdapter.getItem(position).gm_intent.p, mDetailAdapter.getItem(position).gm_intent.gm_is.i
                                , mDetailAdapter.getItem(position).gm_intent.gm_is.m), null, null, null, null)));
                    }
                } else {
                    ToosUtil.getInstance().isConnectTv(SearchLiveActivity.this);
                }
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(
                            v.getApplicationWindowToken(), 0);
                }
                finish();
            }
        });
        mEdittextSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        /* 隐藏软键盘 */
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isActive()) {
                            inputMethodManager.hideSoftInputFromWindow(
                                    v.getApplicationWindowToken(), 0);
                        }
                        mDetailListview.setVisibility(View.GONE);
                        showDilog("加载中...");
                        Statics.getInstance().sendSearchStatics(SearchLiveActivity.this, mEdittextSearch.getText().toString().trim(), mEdittextSearch.getText().toString().trim(), "发现");
                        initGetTv(mPage, mPagetsize, mEdittextSearch.getText().toString().trim());
                        String record = mEdittextSearch.getText().toString().trim();
                        nodata.setVisibility(View.GONE);
                        if (!record.equals("")) {
                            addSql(record);
                            mScrollview.setVisibility(View.GONE);
                            //mDetailListview.setVisibility(View.VISIBLE);
//						Intent intent=new Intent(SearchMTVActivity.this,FtDetailActivity.class);
//						intent.putExtra("key",record);
//						startActivity(intent);

                        }
                        jiance();
                        return true;
                    }
                }
                return false;
            }
        });
        mEdittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEdittextSearch.getText().toString().trim().length() > 0) {
                    mDetailListview.setVisibility(View.GONE);
                    mScrollview.setVisibility(View.GONE);
                    nodata.setVisibility(View.GONE);
                    // initGetTv(mPage,mPagetsize,mEdittextSearch.getText().toString().trim());
                } else {
                    mScrollview.setVisibility(View.VISIBLE);
                    nodata.setVisibility(View.GONE);
                    mDetailListview.setVisibility(View.GONE);
                    if (recordDao.getRecord() != null) {
                        mRecordAdapter.changeData((ArrayList<SearchLiveRecord>) recordDao.getRecord());
                        mScrollview.setVisibility(View.VISIBLE);
                    }
                }
                jiance();
            }
        });
        mHoslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	/* 隐藏软键盘 */
                SearchLiveRecord mRecord = mRecordAdapter.getItem(position);
                addSql(mRecord.name);
                mEdittextSearch.setText(mRecord.name);
                mEdittextSearch.setSelection(mEdittextSearch.getText().toString().length());//将光标移至文字末尾
                mDetailListview.setVisibility(View.GONE);
                showDilog("加载中...");
                Statics.getInstance().sendSearchStatics(SearchLiveActivity.this, mRecord.name, "", "发现");
                initGetTv(mPage, mPagetsize, mRecord.name);
                mScrollview.setVisibility(View.GONE);
//						Intent intent=new Intent(SearchMTVActivity.this,FtDetailActivity.class);
//						intent.putExtra("key",record);
//						startActivity(intent);
            }
        });
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordDao.deleteRecord();
                lishi.setVisibility(View.GONE);
            }
        });
    }


    //删除超过的条数
    public void deleteMore() {
        if (recordDao.getRecord().size() > 20) {
            delete(recordDao.getRecord().get(0).name);
        }
    }

    //添加经数据库
    public void addSql(String s) {
        List<SearchLiveRecord> record2 = recordDao.getRecord();
        for (int j = 0; j < record2.size(); j++) {
            SearchLiveRecord re = record2.get(j);
            if (re.name.equals(s)) {
                delete(s);
            }
        }
        recordDao.addRecord(s);


    }

    //删除存在的数据
    public void delete(String name) {
        recordDao.deleteAlread(name);
    }

    private void initGetTv(int page, int pagetsize, String key) {
        XGIMILOG.E("开始搜索 : " + key);
        subscription1 = Api.getMangoApi().getAllSearchTv(key, page, pagetsize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }

    private Subscription subscription1;
    //获取所有的年份
    Observer<SearchLiveTv> observer1 = new Observer<SearchLiveTv>() {
        @Override
        public void onCompleted() {
            unRegist(subscription1);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            XGIMILOG.E("搜索出错 : " + e.getMessage());
        }

        @Override
        public void onNext(SearchLiveTv channels) {
            XGIMILOG.E("搜索完成");
//            LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(channels));
            if (channels != null && channels.data != null && channels.data.size() > 0) {
                mDetailAdapter.dataChange(channels.data);
                nodata.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                mDetailListview.setVisibility(View.VISIBLE);
                MissDilog();
            } else if (mPage == 1) {
                mDetailListview.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);
                MissDilog();
            }
            if (isclose) {
                nodata.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
                mScrollview.setVisibility(View.GONE);
                mDetailListview.setVisibility(View.GONE);
                MissDilog();
            }
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }

    protected void onDestroy() {
        // TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
