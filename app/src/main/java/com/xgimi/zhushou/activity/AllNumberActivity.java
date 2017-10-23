package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.AllNumberAdapter;
import com.xgimi.zhushou.bean.FilmDetailBean;

public class AllNumberActivity extends BaseActivity {

    private FilmDetailBean.DataBean data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_number);
        initExras();
        initViesw();
    }
    private void initExras(){
        Intent intent=getIntent();
        if(intent!=null){
            data = (FilmDetailBean.DataBean) intent.getSerializableExtra("number");
        }
    }
    private void initViesw(){

        ImageView iv= (ImageView) findViewById(R.id.back);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tv= (TextView) findViewById(R.id.tv_titile);
        tv.setText("选集");

        GridView mGridView=(GridView) findViewById(R.id.gridview);
        AllNumberAdapter mAdapter = new AllNumberAdapter(this, data);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0,R.anim.activity_out);
    }
}
