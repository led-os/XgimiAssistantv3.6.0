package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.xgimi.device.device.GMDeviceController;
import com.xgimi.device.device.GMKeyCommand;
import com.xgimi.device.socket.UdpManager;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.YunLocalimageGridAdapter;
import com.xgimi.zhushou.bean.ImageInfo;
import com.xgimi.zhushou.netUtil.HttpServer;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.PageViewImage;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ImageGridViewActivity extends BaseActivity implements OnClickListener {
    private GridView gridView;


    private String path = null;
    private int mPosition;
    private List<ImageInfo> childPathList = new ArrayList<>();
    private TextView title;
    private Button back;
    private YunLocalimageGridAdapter yunAdapter;
    private Handler mFinishHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                XGIMILOG.D("");
                Intent intent = new Intent();
                intent.putExtra("imgHttpUrl", (String) msg.obj);
                ImageGridViewActivity.this.setResult(1, intent);
                ImageGridViewActivity.this.finish();
            }
        }
    };

//	private TextView title;
//	private Button  back;
//	private List<ImageInfo> childPathList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photobrowser_layout);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.photoTitle);
        gmKeyCommand = new GMKeyCommand();
        mPosition = getIntent().getIntExtra("position", -1);
        if (GlobalConsts.mImgKeyList != null && GlobalConsts.mImgKeyList.size() > 0 && mPosition >= 0) {
            path = GlobalConsts.mImgKeyList.get(mPosition);
            childPathList = GlobalConsts.mImgMap.get(path);
            title.setText((new File(path)).getName());
            XGIMILOG.D("path = " + path + ", size = " + childPathList.size());
        } else {
            title.setText("选择照片");
            for (int i = 0; i < GlobalConsts.mImgKeyList.size(); i++) {
                String p = GlobalConsts.mImgKeyList.get(i);
                childPathList.addAll(GlobalConsts.mImgMap.get(p));
            }
        }
        back = (Button) findViewById(R.id.photoBack);
        gridView = (GridView) findViewById(R.id.local_photogridView);
        yunAdapter = new YunLocalimageGridAdapter(this, childPathList);
        gridView.setAdapter(yunAdapter);
        back.setOnClickListener(this);
        write();
        childPathList = GlobalConsts.mImgMap.get(path);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                // TODO Auto-generated method stub
                if (mPosition != -1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                PageViewImage page = new PageViewImage(ImageGridViewActivity.this, path, position);
                                page.show();
                                isShow = true;
                        }
                    });
                    ToosUtil.getInstance().addEventUmeng(ImageGridViewActivity.this, "event_local_picture");
                } else {
                    String imgId = GlobalConsts.IMAGE_PREFIX + yunAdapter.getItem(position).getId();
                    String title = yunAdapter.getItem(position).getUrl();
                    String httpUrl = "http://" + App.mPhoneIP + ":" + HttpServer.PORT + "/" + imgId + UdpManager.separator + title;
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = httpUrl;
                    mFinishHandler.sendMessage(msg);
                }

            }
        });
    }

    private GMKeyCommand gmKeyCommand;
    //是否投影在了TV端
    private boolean isShow;

    //a按返回键的时候也停止投屏
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isShow) {
            gmKeyCommand.setKey(GMKeyCommand.GMKeyEventBack);
            GMDeviceController.getInstance().sendCommand(gmKeyCommand);
            isShow = false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 按时间排序类
     */
    private void filePaixu() {
        //            Collections.sort(info.filespath, new FileComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。
        Collections.sort(childPathList, new Comparator<ImageInfo>() {
            /**
             *
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(ImageInfo lhs, ImageInfo rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getTime());
                Date date2 = DateUtil.stringToDate(rhs.getTime());
                // 对日期字段进行升序before，如果欲降序可采用after方法
                if (date1.before(date2)) {
                    return 1;
                } else if (date1.after(date2)){
                    return -1;
                } else {
                    return 0;
                }
            }
        });
//        for (int j = 0; j < childPathList.size(); j++) {
//            Log.e("tupianxian", childPathList.get(j).getTime() + childPathList.get(j).url);
//        }
        yunAdapter.dataChange(childPathList);
    }

    /**
     * 将获得的数据写入自己的类
     */
    public void write() {
        for (int i = 0; i < childPathList.size(); i++) {
            String filePath = childPathList.get(i).url;
            File file = new File(filePath);
            long time = 0;
            if (file.exists()) {
                time = file.lastModified();
            }
            childPathList.get(i).setTime(StringUtils.getfullTime(time));
//            Log.e("shijian" + i + "", StringUtils.getfullTime(time) + file.getName());
        }
        filePaixu();
    }

    public static class DateUtil {

        public static Date stringToDate(String dateString) {
            ParsePosition position = new ParsePosition(0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateValue = simpleDateFormat.parse(dateString, position);
            return dateValue;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.photoBack:
                finish();
                break;
            default:
                break;
        }
    }


}
