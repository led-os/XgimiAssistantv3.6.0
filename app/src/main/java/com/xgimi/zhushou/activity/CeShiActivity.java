package com.xgimi.zhushou.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;

/**
 * Created by Administrator on 2016/12/8.
 */
public class CeShiActivity extends BaseActivity {
    private ImageView image;
    private TextView title;
    private ImageView back;
    private ImageView iv_remount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceshi);
        image= (ImageView) findViewById(R.id.image);
        image.setImageResource(R.drawable.chuanshihelp);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        title.setText("如何传输文件");
        back= (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);
        setYaokongBackground(iv_remount,this,"qita");
    }
}