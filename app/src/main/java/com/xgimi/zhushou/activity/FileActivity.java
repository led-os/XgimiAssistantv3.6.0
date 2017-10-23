package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.FileTypeAdapter;
import com.xgimi.zhushou.util.GlobalConsts;

/**
 * Created by Administrator on 2016/12/2.
 */
public class FileActivity extends BaseActivity{
    private ImageView back;
    private TextView title;
    private FileTypeAdapter adapter;
    private ListView listview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        initView();
    }


    private void initView() {
        back= (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        back(back);
        title.setText("文档");
        listview = (ListView) findViewById(R.id.listView);
        adapter = new FileTypeAdapter(FileActivity.this, GlobalConsts.files);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getItemSize(position) == 0) {
                    return;
                }
                Intent intent = new Intent(FileActivity.this, FileTransferDetailActivity.class);
                intent.putExtra("position", position);
                App.getContext().addDestoryActivity(FileActivity.this,"FileTransferDetailActivity");
                intent.putExtra("type_title", adapter.getItem(position).toString());
                startActivity(intent);

            }
        });
    }
}
