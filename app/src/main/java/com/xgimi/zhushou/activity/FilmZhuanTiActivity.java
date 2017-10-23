package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.SpecialAdapter;
import com.xgimi.zhushou.aes.MyAesToGsonFactory;
import com.xgimi.zhushou.bean.ZhuanTiBean;
import com.xgimi.zhushou.netUtil.Api;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FilmZhuanTiActivity extends BaseActivity {
    private GridView listview;
    private SpecialAdapter speadapter;
    private String mId;
    ZhuanTiBean mZhuanTi=new ZhuanTiBean();
    private SpecialAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_zhuan_ti);
        initExras();
        initView();
        initData(mId);
    }
    private void initExras(){
        Intent intent=getIntent();
        mId = intent.getStringExtra("id");
    }
    private void initView(){
        controlTitle(findViewById(R.id.id_toolbar),true,true,false,false);
        listview = (GridView) findViewById(R.id.gridview);
        mAdapter = new SpecialAdapter(this,mZhuanTi);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZhuanTiBean.DataBean.ContentsBean  item = mAdapter.getItem(position);
                Intent intent = new Intent(FilmZhuanTiActivity.this, FilmDetailActivity.class);
                intent.putExtra("id", item.content_id);
                startActivity(intent);
            }
        });
    }
    private void initData(String id){
//        subscription = Api.getMangoApi().getFilmZhuanti(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
        try {
            subscription = Api.getXgimiVideoApi(MyAesToGsonFactory.create(ZhuanTiBean.class))
                    .getFilmZhuanti(Api.getEncodeParam(new String[]{"id"}, new String[]{id}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Observer<ZhuanTiBean> observer = new Observer<ZhuanTiBean>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(ZhuanTiBean channels) {
            if (channels != null && channels.data != null) {
                mAdapter.dataChange(channels);
                tv.setText(channels.data.title);
            }
        }
    };
}
