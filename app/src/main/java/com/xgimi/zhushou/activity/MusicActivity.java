package com.xgimi.zhushou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.xgimi.zhushou.adapter.MusicTransferAdapter;
import com.xgimi.zhushou.bean.ImageInfo;
import com.xgimi.zhushou.bean.Mp3Info;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/12/2.
 */
public class MusicActivity extends BaseActivity {
    private ImageView back;
    private TextView title;
    private ListView listViewMusic;
    private MusicTransferAdapter adapter;
    private List<Mp3Info> mData = new ArrayList<>();
    private List<Mp3Info> allData = new ArrayList<>();
    private List<Mp3Info> mData1 = new ArrayList<>();
    private List<Mp3Info> mData2 = new ArrayList<>();
    private RelativeLayout rl_chuanshu;
    private RelativeLayout tishi;
    private TextView downName;
    private TextView downProgress;
    //这个boolean类型是解决下载应用时，这边会收到进度从而弹出受影响
    private boolean isClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        EventBus.getDefault().register(this);
        back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        back(back);

        rl_chuanshu = (RelativeLayout) findViewById(R.id.rl_chuanshu);
        title.setText("音乐");
        listViewMusic = (ListView) findViewById(R.id.listview_music);

        tishi = (RelativeLayout) findViewById(R.id.tishi);
        tishi.setVisibility(View.GONE);
        downName = (TextView) findViewById(R.id.downName);
        downProgress = (TextView) findViewById(R.id.downProgress);

        mData1 = GlobalConsts.mp3Infos;
        for (int i = 0; i < mData1.size(); i++) {
            mData1.get(i).setType(0);
        }
        adapter = new MusicTransferAdapter(MusicActivity.this, mData1, false);
        listViewMusic.setAdapter(adapter);

    }

    private void initData() {
    }

    private void initListener() {
        //有没得打钩的，打钩一个传一个过来
        adapter.setLisener(new MusicTransferAdapter.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, Mp3Info> beans, Map<Integer, Mp3Info> cleanBeen, boolean isAdd) {
                mData = addImage(beans);
                //注释的这段代码可以实现，按图片点击的先后顺序加入（另一个项目中可以，这个项目中未验证）
                if (isAdd) {
                    addImage(cleanBeen);
                    allData.addAll(mp3);
                } else {
                    deleteImage(cleanBeen);
                    for (int i = 0; i < allData.size(); i++) {
                        if ((deletmp3.get(0).getId()) == (allData.get(i).getId())) {
                            allData.remove(i);
                        }
                    }
                }
                cleanBeen.clear();
            }
        });
        rl_chuanshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allData.size() > 0) {
                    isClick = true;
                    Toast.makeText(MusicActivity.this, "准备中", Toast.LENGTH_SHORT).show();
                    onSendBtn(allData);
                }

            }
        });
    }

    //遍历map集合，。取出其中的人名和id放入poeples中，shijiPoeples中存放被选中的人的个数,deletepoeples存放要删除的那一个数据
    private List<Mp3Info> mp3 = new ArrayList<>();
    private List<Mp3Info> deletmp3 = new ArrayList<>();
    private Map<Integer, ImageInfo> shijiimage;

    //打钩时的正常遍历添加
    private List<Mp3Info> addImage(Map<Integer, Mp3Info> beans) {
        mp3.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            Mp3Info val = (Mp3Info) entry.getValue();
            mp3.add(new Mp3Info(val.getId(), val.getUrl(), val.getArtist()));
        }
        return mp3;
    }

    //取消打钩的遍历，从集合中找到他并删除他个狗日的，妈卖批
    private List<Mp3Info> deleteImage(Map<Integer, Mp3Info> beans) {
        deletmp3.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            Mp3Info val = (Mp3Info) entry.getValue();
            deletmp3.add(new Mp3Info(val.getId(), val.getUrl(), val.getArtist()));
        }
        return deletmp3;
    }

    private ArrayList<VcontrolCmd.CustomPlay.PlayList> playLists = new ArrayList<>();

    //传音乐的方法
    private void onSendBtn(List<Mp3Info> data) {
        mData2 = allData;
//        showDilog("传输中...");
        ToosUtil.getInstance().addEventUmeng(MusicActivity.this, "event_file_send_music");
        ToosUtil.getInstance().addEventUmeng(MusicActivity.this, "event_file_send");
        for (int i = 0; i < data.size(); i++) {
            String id = GlobalConsts.AUDIO_PREFIX + data.get(i).getId();
            String title = data.get(i).getTitle();
            String sendCommand = "http://" + App.mPhoneIP + ":" + HttpServer.PORT + "/" + id + UdpManager.separator + title;
            //GMDeviceController.getInstance().SendJC(sendCommand);
            VcontrolCmd.CustomPlay.PlayList playList = new VcontrolCmd.CustomPlay.PlayList("." + getHouZhui(title), null, null, sendCommand, getFileName(title), null);
            playLists.add(playList);
            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                    "2", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(21, 0, null, playLists, 0), null, null, null)));
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
//        tishi.setVisibility(View.VISIBLE);
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
                    if (mData2.size() == position && isClick) {
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
                                "2", new VcontrolCmd.ControlCmd(30, 0, 0), 20000)));
                        MissDilog();
                        finish();
                        Toast.makeText(MusicActivity.this, "传输完成", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
