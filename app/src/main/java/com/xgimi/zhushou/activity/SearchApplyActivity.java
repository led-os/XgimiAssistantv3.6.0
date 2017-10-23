package com.xgimi.zhushou.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xgimi.device.device.GMDeviceConnector;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.RecordAdapter1;
import com.xgimi.zhushou.adapter.SearchApplyAdapter;
import com.xgimi.zhushou.adapter.SearchNameAdapter;
import com.xgimi.zhushou.bean.ApplySearc;
import com.xgimi.zhushou.bean.MovieLeiBie;
import com.xgimi.zhushou.bean.Record;
import com.xgimi.zhushou.bean.SearchBean;
import com.xgimi.zhushou.db.RecordDao;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.Statics;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.FlowLayout;
import com.xgimi.zhushou.widget.MyEditText;
import com.xgimi.zhushou.widget.MyListview;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchApplyActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "SearchApplyActivity";
    ArrayList<Record> mRecords = new ArrayList<>();
    private MyListview recordlistview;
    private RecordAdapter1 adapter;
    public MyEditText edittext;
    private RecordDao recordDao;
    private FlowLayout hotMovie;
    //	private MyEditText etSearch;
    private String record;
    private ListView detailListview;
    private SearchApplyAdapter sAdapter;
    private ApplySearc mSearch = new ApplySearc();
    private Subscription subscription;
    private Subscription subscription1;
    private Subscription subscription2;
    private View nodata;
    private TextView tishi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_apply);
        initInstallListener();
        initView();
        initData();
        initLocal();
        Log.d(TAG, "onCreate: ");
    }

    private void initInstallListener() {
//        GMDeviceConnector.getInstance().addAppInstallListener();
    }

    private void initView() {
        EventBus.getDefault().register(this);
        rl_flow = (RelativeLayout) findViewById(R.id.rl_flowat);
        rl_flow.setVisibility(View.GONE);
        TextView tishi = (TextView) findViewById(R.id.remen_1).findViewById(R.id.tv_tishi);
        tishi.setText("搜索历史");
        cancel = (TextView) findViewById(R.id.iv_cancel);
        nodata = findViewById(R.id.nodata);
        tishi = (TextView) findViewById(R.id.nodata).findViewById(R.id.no_data);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        recordlistview = (MyListview) findViewById(R.id.record_listview);
        edittext = (MyEditText) findViewById(R.id.search);
        recordDao = new RecordDao(this);
        TextView remen = (TextView) findViewById(R.id.remen).findViewById(R.id.tv_tishi);
        remen.setText("热门");
        hotMovie = (FlowLayout) findViewById(R.id.remen_movie);
//		etSearch = (MyEditText) findViewById(R.id.et_search);
        listview = (ListView) findViewById(R.id.pop_listview);
        edittext.setHint("搜索应用");
        searchAdapter = new SearchNameAdapter(this, searchNames);
        listview.setAdapter(searchAdapter);
        TextView delete = (TextView) findViewById(R.id.tv_clean_record);
        delete.setOnClickListener(this);
        lishi = (RelativeLayout) findViewById(R.id.lishi);
        Editable etext = edittext.getText();
        Selection.setSelection(etext, etext.length());

        detailListview = (ListView) findViewById(R.id.listview_movie);
        sAdapter = new SearchApplyAdapter(this, mSearch, ToosUtil.getInstance().getScreenHeight(this), ToosUtil.getInstance().getScreenWidth(this));
        detailListview.setAdapter(sAdapter);
    }

    private void initLocal() {
        loadHotCi();
        jiance();
    }

    private void jiance() {
        if (recordDao.getApplyRecord().isEmpty()) {
            lishi.setVisibility(View.GONE);
        } else {
            lishi.setVisibility(View.VISIBLE);
        }
    }

    private void loadHotCi() {
        subscription = Api.getMangoApi().getAppHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<MovieLeiBie> observer = new Observer<MovieLeiBie>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(MovieLeiBie data) {
            if (data != null && data.data != null && data.data.size() > 0) {
                loadLocal(data);
            } else {
                nodata.setVisibility(View.VISIBLE);
            }
        }
    };

    private void loadLocal(MovieLeiBie data) {
        if (data != null) {
            initHotCi(data.data);
        }
    }

    private void initHotCi(List<String> list) {
        rl_flow.setVisibility(View.VISIBLE);
        hotMovie.removeAllViews();
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final TextView tv = new TextView(this);
            tv.setText(list.get(i));
            tv.setBackgroundResource(R.drawable.hot_selector);
            tv.setSingleLine(true);
//			tv.setMaxWidth(width/2);
            layoutParams.leftMargin = 20;
            layoutParams.rightMargin = 20;
            layoutParams.topMargin = 30;
            tv.setTextColor(Color.rgb(94, 94, 94));
            tv.setTextSize(18);
            tv.setLayoutParams(layoutParams);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
//					getSearchGuanjian(tv.getText().toString().trim());

                    showDilog("加载中...");
                    Statics.getInstance().sendSearchStatics(SearchApplyActivity.this, tv.getText().toString().trim(), "", "应用");
                    ApplyDetail(tv.getText().toString().trim());
                    edittext.setText(tv.getText().toString().trim());
                    edittext.setSelection(edittext.getText().toString().length());
//					Intent intent=new Intent(ApplySearchActivity.this,SearcApplyActivity.class);
//					intent.putExtra("key",tv.getText().toString().trim());
//					startActivity(intent);
                    addSql(tv.getText().toString().trim());
                }
            });
            hotMovie.addView(tv);
        }
    }

    private void initData() {


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//				finish();
                /* 隐藏软键盘 */

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(
                            v.getApplicationWindowToken(), 0);
                }
                finish();
//                record = edittext.getText().toString().trim();
//                if(!record.equals("")){
//                    showDilog("数据加载中...");
//                    addSql(record);
//                    ApplyDetail(record);
//                }
//				Intent intent=new Intent(ApplySearchActivity.this,SearcApplyActivity.class);
//				intent.putExtra("key",record);
//				startActivity(intent);
            }
        });
        adapter = new RecordAdapter1(this, mRecords);
        recordlistview.setAdapter(adapter);
        recordlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                showDilog("数据加载中...");
                addSql(adapter.getItem(position).name);
                Statics.getInstance().sendSearchStatics(SearchApplyActivity.this, adapter.getItem(position).name, "", "应用");
                ApplyDetail(adapter.getItem(position).name);
                nodata.setVisibility(View.GONE);
                edittext.setText(adapter.getItem(position).name + "");
                edittext.setSelection(edittext.getText().toString().length());//将光标移至文字末尾
                ToosUtil.getInstance().addEventUmeng(SearchApplyActivity.this, "event_app_search");
//				Intent intent=new Intent(ApplySearchActivity.this,SearcApplyActivity.class);
//				intent.putExtra("key",adapter.getItem(position).name);
//				startActivity(intent);
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                showDilog("数据加载中...");
                addSql(searchAdapter.getItem(position));
                Statics.getInstance().sendSearchStatics(SearchApplyActivity.this, searchAdapter.getItem(position), edittext.getText().toString().trim(), "应用");
                ApplyDetail(searchAdapter.getItem(position));
                edittext.setText(searchAdapter.getItem(position));
                ToosUtil.getInstance().addEventUmeng(SearchApplyActivity.this, "event_app_search");
//				Intent intent=new Intent(ApplySearchActivity.this,SearcApplyActivity.class);
//				intent.putExtra("key",searchAdapter.getItem(position));
//				startActivity(intent);
            }
        });
        edittext.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
						/* 隐藏软键盘 */

                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isActive()) {
                            inputMethodManager.hideSoftInputFromWindow(
                                    v.getApplicationWindowToken(), 0);
                        }
                        record = edittext.getText().toString().trim();
                        if (!record.equals("")) {
                            showDilog("数据加载中...");
                            addSql(record);
                            Statics.getInstance().sendSearchStatics(SearchApplyActivity.this, record, record, "应用");
                            ApplyDetail(record);
                        }
                        nodata.setVisibility(View.GONE);
                        ToosUtil.getInstance().addEventUmeng(SearchApplyActivity.this, "event_app_search");
//						Intent intent=new Intent(ApplySearchActivity.this,SearcApplyActivity.class);
//						intent.putExtra("key",record);
//						startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });
        edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                jiance();
                nodata.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (edittext.getText().toString().trim().length() > 0) {
                    listview.setVisibility(View.VISIBLE);
                    scrollview.setVisibility(View.GONE);
                    detailListview.setVisibility(View.GONE);
                    getGuanjianzi(edittext.getText().toString().trim());
                } else {
                    listview.setVisibility(View.GONE);
                    scrollview.setVisibility(View.VISIBLE);
                    detailListview.setVisibility(View.GONE);
                }
                jiance();
            }
        });


    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (recordDao.getApplyRecord().size() == 0) {
            lishi.setVisibility(View.GONE);
            recordlistview.setVisibility(View.GONE);
        } else {
            lishi.setVisibility(View.VISIBLE);
            recordlistview.setVisibility(View.VISIBLE);
        }
        listview.setVisibility(View.GONE);
        scrollview.setVisibility(View.VISIBLE);
        deleteMore();
        adapter.changeData((ArrayList<Record>) recordDao.getApplyRecord());
        super.onResume();
    }

    //删除超过的条数
    public void deleteMore() {
        if (recordDao.getApplyRecord().size() > 20) {
            delete(recordDao.getApplyRecord().get(0).name);
        }
    }

    //添加经数据库
    public void addSql(String s) {
        List<Record> record2 = recordDao.getApplyRecord();
        for (int j = 0; j < record2.size(); j++) {
            Record re = record2.get(j);
            if (re.name.equals(s)) {
                delete(s);
            }
        }
        recordDao.addApplyRecord(s);
        jiance();
    }

    //删除存在的数据
    public void delete(String name) {
        recordDao.deletName(name);
    }

    SearchBean searchNames = new SearchBean();
    private ScrollView scrollview;
    private ListView listview;
    private SearchNameAdapter searchAdapter;
    private TextView cancel;
    private RelativeLayout lishi;
    private RelativeLayout rl_flow;

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        super.onPause();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(Record record) {
        recordDao.deleteapplyAlread(record.name);
        adapter.changeData((ArrayList<Record>) recordDao.getApplyRecord());
        jiance();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_clean_record:
                recordDao.deleteApplyRecord();
                lishi.setVisibility(View.GONE);
                recordlistview.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }


    public void getGuanjianzi(String s) {
        subscription1 = Api.getMangoApi().getApplyGuanJianJi(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }

    Observer<SearchBean> observer1 = new Observer<SearchBean>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription1);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(SearchBean data) {
            if (data != null && data.data != null) {
                searchAdapter.dataChange(data);
            }
        }
    };
    String mEditTextName;

    /**
     * 搜索应用
     * @param s
     */
    public void ApplyDetail(final String s) {
//        mEditTextName=s;
        subscription2 = Api.getMangoApi().getApply(null, null, s, 12, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);

    }

    Observer<ApplySearc> observer2 = new Observer<ApplySearc>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription2);
        }

        @Override
        public void onError(Throwable e) {
            MissDilog();
            listview.setVisibility(View.GONE);
            scrollview.setVisibility(View.GONE);
            detailListview.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
//            tishi.setText("数据加载失败");
            e.printStackTrace();
        }

        @Override
        public void onNext(ApplySearc data) {
//            LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(data));
            if (data != null && data.data != null) {
                MissDilog();
//                edittext.setText(mEditTextName);
                Editable etext = edittext.getText();
                Selection.setSelection(etext, etext.length());
                if (data.data.size() > 0) {
                    sAdapter.dataChange(data);
                    nodata.setVisibility(View.GONE);
                    scrollview.setVisibility(View.GONE);
                    listview.setVisibility(View.GONE);
                    detailListview.setVisibility(View.VISIBLE);
                } else {
                    nodata.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
                    scrollview.setVisibility(View.GONE);
                    detailListview.setVisibility(View.GONE);

                }
            }
        }
    };

}
