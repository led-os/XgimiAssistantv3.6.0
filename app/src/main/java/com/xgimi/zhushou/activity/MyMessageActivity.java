package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;

/**
 * Created by Administrator on 2016/12/12.
 */
public class MyMessageActivity extends BaseActivity implements View.OnTouchListener,View.OnClickListener{
    private ImageView back;
    private TextView title;
    private ImageView iv_remount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        back(back);
        title.setText("消息");
        iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);
        setYaokongBackground(iv_remount,this,"qita");
        iv_remount.setOnTouchListener(this);
        ImageView image1= (ImageView) findViewById(R.id.yinshi).findViewById(R.id.image);
        image1.setImageResource(R.drawable.zhuantituijian);
        ImageView image2= (ImageView) findViewById(R.id.huodong).findViewById(R.id.image);
        image2.setImageResource(R.drawable.youhuihuodogn);
        ImageView image3= (ImageView) findViewById(R.id.xitong).findViewById(R.id.image);
        image3.setImageResource(R.drawable.xitongxiaoxi);
        TextView textView1= (TextView) findViewById(R.id.yinshi).findViewById(R.id.text);
        textView1.setText("影视推送");
        TextView textView2= (TextView) findViewById(R.id.huodong).findViewById(R.id.text);
        textView2.setText("优惠活动");
        TextView textView3= (TextView) findViewById(R.id.xitong).findViewById(R.id.text);
        textView3.setText("系统消息");
        View view1=findViewById(R.id.yinshi);
        view1.setOnClickListener(this);
        View view2=findViewById(R.id.huodong);
        view2.setOnClickListener(this);
        View view3=findViewById(R.id.xitong);
        view3.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) { switch (v.getId()) {
        // TODO Auto-generated method stub
        case R.id.iv_remount:
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    iv_remount.setAlpha(0.6f);
                    break;
                case MotionEvent.ACTION_UP:
                    iv_remount.setAlpha(1.0f);
                    break;
            }
            break;
    }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yinshi:
                Intent intent=new Intent(MyMessageActivity.this,MyYinshiActivity.class);
                startActivity(intent);
                break;
            case R.id.huodong:
                Intent intent1=new Intent(MyMessageActivity.this,MyHuodongActivity.class);
                startActivity(intent1);
                break;
            case R.id.xitong:
                Intent intent2=new Intent(MyMessageActivity.this,MyXitongActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
