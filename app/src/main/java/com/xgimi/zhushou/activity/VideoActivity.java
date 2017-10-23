package com.xgimi.zhushou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.HttpServer;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.socket.UdpManager;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.VideoTransferAdapter;
import com.xgimi.zhushou.bean.VideoInfo;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/12/2.
 */
public class VideoActivity extends BaseActivity {
    private ImageView back;
    private TextView title;
    private GridView gridview;
    private VideoTransferAdapter adapter;
    private List<VideoInfo> VideoList = new ArrayList<>();
    private RelativeLayout rl_chuanshu;
    private RelativeLayout tishi;
    private TextView downName;
    private TextView downProgress;
    //保存选中的视频信息
    private List<VideoInfo> mVideoList = new ArrayList<>();
    private List<VideoInfo> mVideoList1 = new ArrayList<>();
    public static HashMap<String, List<VideoInfo>> vGruopMap = new HashMap<String, List<VideoInfo>>();
    //保存需要传的视频的信息
    private List<VideoInfo> selectVideoList1 = new ArrayList<>();
    private String path = null;
    //这个boolean类型是解决下载应用时，这边会收到进度从而弹出受影响
    private boolean isClick;
    private int mySize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        initListener();
    }

    /*
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    //去掉重复的数据
    public Map<String, Object> myMap = new ArrayMap<>();
    public Map<String, Object> myMap1 = new ArrayMap<>();

    private List<Map<String, Object>> removeDuplicate(List<Map<String, Object>> data) {
        for (int i = 0; i < data.size(); i++) {
            myMap = data.get(i);
            for (int j = data.size() - 1; j > i; j--) {
                myMap1 = data.get(j);
                if (getFileNameNoEx(myMap1.get("filepath").toString()).equals(getFileNameNoEx(myMap.get("filepath").toString())) || myMap1.get("title").toString().equals(myMap.get("title").toString())) {
                    data.remove(j);
                }
            }
        }
        return data;
    }

    private List<VideoInfo> removeDuplicate1(List<VideoInfo> data) {
        for (int i = 0; i < data.size(); i++) {
            for (int j = data.size() - 1; j > i; j--) {
                if (getFileNameNoEx(data.get(i).title.toString()).equals(getFileNameNoEx(data.get(j).title.toString()))) {
                    data.remove(j);
                }
            }
        }
        return data;
    }

    private void initView() {
        EventBus.getDefault().register(this);
        back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        gridview = (GridView) findViewById(R.id.gridview);
        rl_chuanshu = (RelativeLayout) findViewById(R.id.rl_chuanshu);
        tishi = (RelativeLayout) findViewById(R.id.tishi);
        tishi.setVisibility(View.GONE);
        downName = (TextView) findViewById(R.id.downName);
        downProgress = (TextView) findViewById(R.id.downProgress);
        back(back);
        title.setText("视频");
        position = getIntent().getIntExtra("position", 0);
        playLists = new ArrayList<>();

        if (GlobalConsts.videoList != null && GlobalConsts.videoList.size() > 0) {
            removeDuplicate(GlobalConsts.videoList);
            mySize = GlobalConsts.videoList.size();
            if (GlobalConsts.videoList.size() != GlobalConsts.mVideoKeyList.size()) {
                mySize = GlobalConsts.mVideoKeyList.size();
            }
            for (int j = 0; j < mySize; j++) {
                if (GlobalConsts.mVideoKeyList != null && GlobalConsts.mVideoKeyList.size() > 0) {
                    path = GlobalConsts.mVideoKeyList.get(j);
                    VideoList = GlobalConsts.mVideoMap.get(path);
                    mVideoList1.addAll(VideoList);
                }
            }
        }
        for (int i = 0; i < mVideoList1.size(); i++) {
            mVideoList1.get(i).setType(0);
        }
        adapter = new VideoTransferAdapter(VideoActivity.this, mVideoList1);
        gridview.setAdapter(adapter);
        write();
    }

    private void initListener() {
        adapter.setLisener(new VideoTransferAdapter.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, VideoInfo> beans, Map<Integer, VideoInfo> cleanBeen, boolean isAdd) {
//                mVideoList=addVideoList(beans);
                //注释的这段代码可以实现，按图片点击的先后顺序加入（另一个项目中可以，这个项目中未验证）
                if (isAdd) {
                    addVideoList(cleanBeen);
                    mVideoList.addAll(video);
                } else {
                    deleteImage(cleanBeen);
                    for (int i = 0; i < mVideoList.size(); i++) {
                        if ((deletvideo.get(0).getId()) == (mVideoList.get(i).getId())) {
                            mVideoList.remove(i);
                        }
                    }
                }
                cleanBeen.clear();
            }
        });
        rl_chuanshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideoList1 = mVideoList;
                if (mVideoList.size() > 0) {
                    isClick = true;
                    Toast.makeText(VideoActivity.this, "准备中", Toast.LENGTH_SHORT).show();
                    onSendBtn(mVideoList);
                }

            }
        });
    }

    private List<VideoInfo> video = new ArrayList<>();
    private List<VideoInfo> deletvideo = new ArrayList<>();

    //打钩时的正常遍历添加
    private List<VideoInfo> addVideoList(Map<Integer, VideoInfo> beans) {
        video.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            VideoInfo val = (VideoInfo) entry.getValue();
            video.add(new VideoInfo(val.getId(), val.getFilePath()));
        }
        return video;
    }

    //取消打钩的遍历，从集合中找到他并删除他个狗日的，妈卖批
    private List<VideoInfo> deleteImage(Map<Integer, VideoInfo> beans) {
        deletvideo.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            VideoInfo val = (VideoInfo) entry.getValue();
            deletvideo.add(new VideoInfo(val.getId(), val.getFilePath()));
        }
        return deletvideo;
    }

    private ArrayList<VcontrolCmd.CustomPlay.PlayList> playLists;

    //传视频的方法
    private void onSendBtn(List<VideoInfo> data) {
//        showDilog("传输中...");
        ToosUtil.getInstance().addEventUmeng(VideoActivity.this, "event_file_send_video");
        ToosUtil.getInstance().addEventUmeng(VideoActivity.this, "event_file_send");
        selectVideoList1 = data;
        for (int i = 0; i < data.size(); i++) {
            String id = GlobalConsts.VIDEO_PREFIX + data.get(i).getId();
            String title = data.get(i).getFilePath();
            String sendCommand = "http://" + App.mPhoneIP + ":" + HttpServer.PORT + "/" + id + UdpManager.separator + title;
            //GMDeviceController.getInstance().SendJC(sendCommand);
            VcontrolCmd.CustomPlay.PlayList playList = new VcontrolCmd.CustomPlay.PlayList("." + getHouZhui(title), null, null, sendCommand, getFileName(title), null);
            playLists.add(playList);
            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                    "2", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(20, 0, null, playLists, 0), null, null, null)));

        }

    }

    //从url中获取图片的文件名
    public String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    //从url中获取图片后缀名
    public String getHouZhui(String url) {
        String prefix = url.substring(url.lastIndexOf(".") + 1);
        return prefix;
    }

    private FeedbackInfo myInfo;

    public void onEventMainThread(FeedbackInfo info) {
        if (isClick) {
            rl_chuanshu.setVisibility(View.GONE);
            tishi.setVisibility(View.VISIBLE);
            myInfo = info;
            if (myInfo != null && myInfo.downloadInfo != null) {
                Message message = mhanHandler.obtainMessage();
                message.obj = myInfo;
                message.what = 0;
                mhanHandler.sendMessage(message);
            }
        }
    }

    private int position = 0;
    Handler mhanHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (downProgress != null && downName != null && isClick) {
                FeedbackInfo info = (FeedbackInfo) msg.obj;
                if (info.downloadInfo != null) {
                    rl_chuanshu.setVisibility(View.GONE);
                    downProgress.setText(info.downloadInfo.progress + "%");
                    downName.setText(info.downloadInfo.filename);
                    tishi.setVisibility(View.VISIBLE);
                    if (info.downloadInfo.progress == 100) {
                        position++;
                    }
                    if (selectVideoList1.size() == position && isClick) {
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
                                "2", new VcontrolCmd.ControlCmd(30, 0, 0), 20000)));
                        MissDilog();
                        finish();
                        Toast.makeText(VideoActivity.this, "传输完成", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    /**
     * 按时间排序类
     */

    private void filePaixu() {

        //            Collections.sort(info.filespath, new FileComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。

        Collections.sort(mVideoList1, new Comparator<VideoInfo>() {
            /**
             *
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(VideoInfo lhs, VideoInfo rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getTime());
                Date date2 = DateUtil.stringToDate(rhs.getTime());
                // 对日期字段进行升序before，如果欲降序可采用after方法
                if (date1.before(date2)) {
                    return 1;
                } else if (date1.after(date2)) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        for (int k = 0; k < mVideoList1.size(); k++) {
//            Log.e("shijianpaixu"+k+"",mVideoList1.get(k).time+mVideoList1.get(k).title);
        }
        removeDuplicate1(mVideoList1);
        adapter.dataChange(mVideoList1);
    }

    /**
     * 将获得的数据写入自己的类
     */
    public void write() {
        for (int i = 0; i < mVideoList1.size(); i++) {
            String filePath = mVideoList1.get(i).filePath;
            File file = new File(filePath);
            long time = 0;
            if (file.exists()) {
                time = file.lastModified();
            }
            mVideoList1.get(i).setTime(StringUtils.getfullTime(time));
//            Log.e("shijian"+i+"",StringUtils.getfullTime(time)+file.getName());
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
