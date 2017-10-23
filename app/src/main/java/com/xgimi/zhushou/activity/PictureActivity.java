package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.PictureListAdapter;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GlobalConsts;

/**
 * Created by Administrator on 2016/12/2.
 */
public class PictureActivity extends BaseActivity implements View.OnTouchListener{
    private ImageView back;
    private TextView title;
    private ImageView iv_remount;
    private ListView listview;
    public PictureListAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        initView();
        initListener();
    }
    private void initView() {
        back= (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        back(back);
        title.setText("图片");
        iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);
        setYaokongBackground(iv_remount,this,"qita");
        iv_remount.setOnTouchListener(this);
        if(Constant.netStatus){
            iv_remount.setImageResource(R.drawable.yaokongqi);
        }else{
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }
        //加载本地图片
        listview = (ListView) findViewById(R.id.listView);

        adapter = new PictureListAdapter(PictureActivity.this,
                GlobalConsts.mImgKeyList, GlobalConsts.mImgMap);

        listview.setAdapter(adapter);

//        View emptyView = inflater.inflate(R.layout.local_empty, container,
//                false);
//
//        ((ViewGroup) listview.getParent()).addView(emptyView);
//
//        listview.setEmptyView(emptyView);
//
//        return v;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
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
    private void initListener() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(PictureActivity.this,
                        PictureGridViewActivity.class);
                Bundle b = new Bundle();
                b.putInt("position", position);
                intent.putExtras(b);
                App.getContext().addDestoryActivity(PictureActivity.this,"PictureGridViewActivity");
                startActivity(intent);

            }
        });
    }
}
