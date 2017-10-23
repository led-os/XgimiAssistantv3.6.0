package com.xgimi.zhushou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.SearchNameAdapter;
import com.xgimi.zhushou.aes.MyAesToGsonFactory;
import com.xgimi.zhushou.bean.SearchBean;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.MyEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearceWebVideoActivity extends BaseActivity {

    private static final String TAG = "SearceWebVideoActivity";
    @Bind(R.id.iv_cancel)
    TextView mCancel;
    @Bind(R.id.listview)
    ListView mKeyWordListView;
    @Bind(R.id.search)
    MyEditText mEdittextSearch;
    @Bind(R.id.ll_prompt)
    LinearLayout ll_prompt;
    private SearchNameAdapter mNameAdapter;
    SearchBean mSearchBeans = new SearchBean();
    private String myKey;
    //看是不是点击了
    private boolean isClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_searce_web_video);
        View view = View.inflate(this, R.layout.activity_searce_web_video, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        initView();
        initData();
        Log.d(TAG, "onCreate: ");
    }

    private void initView() {
        mNameAdapter = new SearchNameAdapter(this, mSearchBeans);
        mKeyWordListView.setAdapter(mNameAdapter);
    }

    private void initData() {
        mKeyWordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* 隐藏软键盘 */
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(
                            mKeyWordListView.getApplicationWindowToken(), 0);
                }

//                mEdittextSearch.setText(mNameAdapter.getItem(position));
//                mEdittextSearch.setSelection(mEdittextSearch.getText().toString().length());//将光标移至文字末尾
                Intent intent = new Intent(SearceWebVideoActivity.this, WebHtmlIQiYiActivity.class);
                intent.putExtra("url", mNameAdapter.getItem(position));
                startActivity(intent);
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
                        if (mEdittextSearch.getText().toString().trim().length() > 0) {
                            mKeyWordListView.setVisibility(View.GONE);
                            Intent intent = new Intent(SearceWebVideoActivity.this, WebHtmlIQiYiActivity.class);
                            intent.putExtra("url", myKey);
                            startActivity(intent);
                            ll_prompt.setVisibility(View.GONE);
//                            getKeyWords(mEdittextSearch.getText().toString().toString().trim());
                        }
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
                mKeyWordListView.setVisibility(View.GONE);
                if (mEdittextSearch.getText().toString().toString().length() > 0) {
                    ll_prompt.setVisibility(View.GONE);
                    mKeyWordListView.setVisibility(View.VISIBLE);
                    isClick = false;
                    getKeyWords(mEdittextSearch.getText().toString().toString().trim());
                }
                if (mEdittextSearch.getText().toString().trim().length() == 0) {
                    mKeyWordListView.setVisibility(View.GONE);
                    ll_prompt.setVisibility(View.VISIBLE);
                    //   jiance();
                }
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /* 隐藏软键盘 */
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(
                            v.getApplicationWindowToken(), 0);
                }
            }
        });
    }


    private void getKeyWords(String key) {
        myKey = "";
        myKey = key;
//        subscription = Api.getMangoApi().getFilmKeyWord(key, 30, "video")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer2);

        XGIMILOG.D("获取影视关键字 : " + key);
        try {
            subscription = Api.getXgimiVideoApi(MyAesToGsonFactory.create(SearchBean.class))
                    .getFilmKeyWord(Api.getEncodeParam(new String[]{"keyword"}, new String[]{key}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //获取关键字
    Observer<SearchBean> observer2 = new Observer<SearchBean>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(SearchBean channels) {
            if (channels != null && channels.data != null && channels.data.size() > 0) {
                mNameAdapter.dataChange(channels);
                mKeyWordListView.setVisibility(View.VISIBLE);
                ll_prompt.setVisibility(View.GONE);
            } else {
                mKeyWordListView.setVisibility(View.GONE);
                ll_prompt.setVisibility(View.VISIBLE);
            }
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }

    }
}
