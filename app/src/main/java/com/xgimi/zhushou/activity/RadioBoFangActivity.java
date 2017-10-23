package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.CurrentPlay;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.MusicInfor;
import com.xgimi.zhushou.bean.RadioShow;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.popupwindow.RadioPopupwindow;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GaoSi;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import de.greenrobot.event.EventBus;
import rx.Subscription;

/**
 * Created by Administrator on 2016/8/29.
 */
public class RadioBoFangActivity extends BaseActivity implements View.OnClickListener {

    private static final int PLAY_MODE_SHUNXU = 0;
    private static final int PLAY_MODE_SUIJI = 1;
    private static final int PLAY_MODE_DANQU = 2;

    public String title, radio_time, bitmap, mv_play_address;
    public TextView mv_name;
    public TextView time;
    private TextView zhuanji;
    private ImageView back;
    private RelativeLayout gaosi;
    private ImageView tupian;
    private ImageView liebiao;
    private RadioPopupwindow menuWindow;
    public int page = 0;
    public int psize = 10;
    private String area_id;
    private String fm_title;
    private String fm_author;
    public String keywords;
    private int id;
    private int index;
    private ImageView mcollect;
    private boolean isCollect;
    private Subscription subscription;
    private Subscription subscription1;
    private Subscription subscription2;
    private App app;
    public int user_id;
    private int type;
    private GimiUser gimuser;
    private boolean isLogo;
    private int collect_id;
    private String geName;
    public int mv_id;
    private ImageView daohang;
    private ImageView iv_play;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_bofang_detail);
        initExtra();
        initView();
        initData();
        initPlayMode();
        SaveData.getInstance().bofang_type = "Music";
//        initLocal();
    }

    private void initPlayMode() {
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                null, new VcontrolCmd.ControlCmd(11, 6, 0, null, null, null), null, null)));
    }

//    //获取缓存数据
//    private void initLocal() {
//        XGIMILOG.D("");
//        String readHomeJson = app.readHomeJson(id + "");
//        if (!StringUtils.isEmpty(readHomeJson)) {
//            //dialog.dismiss();
//            RadioInfo data = new Gson().fromJson(readHomeJson, RadioInfo.class);
//            SaveData.getInstance().bofang_type = "Radio";
//            SaveData.getInstance().mDetail = data;
//            XGIMILOG.D(new Gson().toJson(SaveData.getInstance().mDetail));
//        } else {
//        }
//    }

    public void onEventMainThread(RadioShow radioShow) {
        XGIMILOG.D(new Gson().toJson(radioShow));
        mv_name.setText(SaveData.getInstance().program_title);
        time.setText(SaveData.getInstance().program_play_time);
        SaveData.getInstance().fenlei = "1";
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                GMSdkCheck.appId, null,
                new VcontrolCmd.CustomPlay(1, -1, null, SaveData.getInstance().mPlayLists, SaveData.getInstance().position),
                null, null, null)));
    }

    public void onEventMainThread(MusicInfor musicInfor) {
        XGIMILOG.D(new Gson().toJson(musicInfor));
        mv_name.setText(SaveData.getInstance().music_title);
        time.setText(SaveData.getInstance().music_singer);
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                GMSdkCheck.appId, null,
                new VcontrolCmd.CustomPlay(1, 0, null, SaveData.getInstance().mPlayLists, SaveData.getInstance().position),
                null, null, null)));
        type = 2;
        index = SaveData.getInstance().position;
        if (SaveData.getInstance().mRadioShow != null) {
            SaveData.getInstance().mRadioShow.clear();
            SaveData.getInstance().mRadioShow = null;
        }
        SaveData.getInstance().fenlei = "2";
        SaveData.getInstance().index = index + "";
        SaveData.getInstance().geName = geName;
    }


    public void onEventMainThread(FeedbackInfo.PlayInfo playInfo) {
        XGIMILOG.D("type = " + type + ", " + new Gson().toJson(playInfo));
        if (playInfo.resourcetype.equals("随心听")) {
            SaveData.getInstance().bofang_type = "Music";
            Gson gson = new Gson();
            CurrentPlay currentPlay = gson.fromJson(playInfo.playingname, CurrentPlay.class);
            if (SaveData.getInstance().mPlayLists != null) {
                for (int i = 0; i < SaveData.getInstance().mPlayLists.size(); i++) {
                    if (currentPlay.data.CurrentPlayMusicInfo.id.equals(SaveData.getInstance().mPlayLists.get(i).id)) {
                        mv_name.setText(currentPlay.data.CurrentPlayMusicInfo.title);
                        time.setText(currentPlay.data.CurrentPlayMusicInfo.singer);
                        title = currentPlay.data.CurrentPlayMusicInfo.title;
                        SaveData.getInstance().position = i;
                    }
                }
            }
        } else if (playInfo.resourcetype.equals("custom")) {
            SaveData.getInstance().bofang_type = "MV";
        }
    }

    private void initExtra() {
        XGIMILOG.E("type = " + getIntent().getStringExtra("type"));
        //type = 1, 电台
        //type = 2, 音乐
        if (getIntent() != null && getIntent().getStringExtra("title") != null) {
            try {
                type = Integer.valueOf(getIntent().getStringExtra("type"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (type == 1) {
                title = getIntent().getStringExtra("title");
                radio_time = getIntent().getStringExtra("time");
                bitmap = SaveData.getInstance().mDetail.data.get(0).medium_thumb;
                try {
                    id = Integer.valueOf(getIntent().getStringExtra("id"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                XGIMILOG.D(title + ", " + radio_time + ", " + bitmap);

            } else if (type == 2) {
                try {
                    index = Integer.valueOf(getIntent().getStringExtra("index"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                bitmap = SaveTVApp.getInstance(this).BangDanTubiao[index];
                geName = getIntent().getStringExtra("geName");
                title = getIntent().getStringExtra("title");
                radio_time = getIntent().getStringExtra("singer");
                XGIMILOG.D(index + ", " + bitmap + "," +geName +  ", " + title + ", " + radio_time);
            }
        } else {

            try {
                type = Integer.valueOf(SaveData.getInstance().fenlei);
            } catch (Exception e) {
                e.printStackTrace();
            }
            XGIMILOG.D("type = " + type);
            if (type == 1) {
                title = SaveData.getInstance().mRadioShow.get(SaveData.getInstance().position).program_title;
                radio_time = SaveData.getInstance().mRadioShow.get(SaveData.getInstance().position).program_play_time;
                bitmap = SaveData.getInstance().mDetail.data.get(0).medium_thumb;
                try {
                    id = Integer.valueOf(SaveData.getInstance().mRadioShow.get(SaveData.getInstance().position).program_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (type == 2) {
                if (SaveData.getInstance().mPlayLists != null) {
                    XGIMILOG.D("");
                    try {
                        index = Integer.valueOf(SaveData.getInstance().index);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    bitmap = SaveTVApp.getInstance(this).BangDanTubiao[index];
                    bitmap = SaveData.getInstance().mPlayLists.get(SaveData.getInstance().position).iconurl;
                    geName = SaveData.getInstance().geName;
                    title = SaveData.getInstance().mPlayLists.get(SaveData.getInstance().position).title;
                    radio_time = SaveData.getInstance().mPlayLists.get(SaveData.getInstance().position).singer;
                } else {
                    XGIMILOG.D("");
                    title = SaveData.getInstance().currentPlay.data.CurrentPlayMusicInfo.title;
                    radio_time = SaveData.getInstance().currentPlay.data.CurrentPlayMusicInfo.singer;
                    bitmap = null;
                }
            }
            XGIMILOG.D("bitmap = " + bitmap + ", name = " + geName + ", title = " + title);
        }
        app = (App) getApplicationContext();
        if (app.getLoginInfo() != null) {
            gimuser = app.getLoginInfo();
            isLogo = true;
            try {
                user_id = Integer.valueOf(gimuser.data.uid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ImageView mPlayModeIv;
    private void initView() {
        EventBus.getDefault().register(this);
        zhuanji = (TextView) findViewById(R.id.zhuanji);
        if (type == 1) {
            //   fm_author=SaveData.getInstance().mDetail.data.get(0).fm_author;
            fm_title = SaveData.getInstance().mDetail.data.get(0).fm_title;
            zhuanji.setText(fm_title);
        } else if (type == 2) {
            zhuanji.setText(geName);
        }
        mv_name = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.singer);

        mv_name.setText(title);

        time.setText(radio_time);

        tupian = (ImageView) findViewById(R.id.iv_mv);
        back = (ImageView) findViewById(R.id.back);
        gaosi = (RelativeLayout) findViewById(R.id.rl_gaosi);
        liebiao = (ImageView) findViewById(R.id.liebiao);
        mcollect = (ImageView) findViewById(R.id.collect);
        iv_play = (ImageView) findViewById(R.id.play);
        ImageView iv_next = (ImageView) findViewById(R.id.next);
        ImageView iv_before = (ImageView) findViewById(R.id.last_one);
        mPlayModeIv = (ImageView) findViewById(R.id.suijis);

        iv_play.setOnClickListener(this);
        iv_before.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        mcollect.setOnClickListener(this);
        mPlayModeIv.setOnClickListener(this);
        daohang = (ImageView) findViewById(R.id.daohang);
        daohang.setOnClickListener(this);
        playchange();
        isPlay = SaveData.getInstance().isPlay;
        String type = getIntent().getStringExtra("type");
        if ("1".equals(type)) {//播放电台,取消播放模式
            mPlayModeIv.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
        } else if ("2".equals(type)) {
            mPlayModeIv.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        if (bitmap != null) {
            ImageLoaderUtils.display(RadioBoFangActivity.this, tupian, bitmap);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getApplicationContext()).load(bitmap).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            tupian.setImageBitmap(resource);
                            Message message = handler.obtainMessage();
                            message.obj = resizeImage(resource, 100, 100);
                            message.what = 0;
                            handler.sendMessage(message);
                        }
                    });
                }
            });
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        liebiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                if (SaveData.getInstance().mPlayLists != null) {
                    menuWindow = new RadioPopupwindow(RadioBoFangActivity.this, type, title);
                    menuWindow.showAtLocation(RadioBoFangActivity.this.findViewById(R.id.liebiao), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                    menuWindow.songname.setText(title);
                } else if (SaveData.getInstance().mRadioShow != null) {
                    menuWindow = new RadioPopupwindow(RadioBoFangActivity.this, type, title);
                    menuWindow.showAtLocation(RadioBoFangActivity.this.findViewById(R.id.liebiao), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                }
                if (type == 1 && SaveData.getInstance().program_title != null) {
                    menuWindow.songname.setText(SaveData.getInstance().program_title);
                } else if (type == 2 && SaveData.getInstance().music_title != null) {
                    menuWindow.songname.setText(SaveData.getInstance().music_title);
                }
//                else {
//                    if(menuWindow.songname!=null)
//
//                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    gaosi.setBackgroundDrawable(new BitmapDrawable(GaoSi.doBlur(
                            bitmap, 30, false)));
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
            }
        }
    };

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    private void playchange() {
        if (isPlay) {
            iv_play.setImageResource(R.drawable.qt_zanting);
        } else {
            iv_play.setImageResource(R.drawable.qt_play);
        }
        SaveData.getInstance().isPlay = isPlay;
    }

    private boolean isPlay = true;
    private int mCurrentPlayMode;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if (!isPlay) {
                    isPlay = true;
                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                            null, new VcontrolCmd.ControlCmd(11, 2, 0, null, null, null), null, null)));
                } else {
                    isPlay = false;
                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                            null, new VcontrolCmd.ControlCmd(11, 3, 0, null, null, null), null, null)));
                }
                playchange();
                break;
            case R.id.last_one:
                ToosUtil.getInstance().addEventUmeng(RadioBoFangActivity.this, "event_music_previous");
                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                        null, new VcontrolCmd.ControlCmd(11, 5, 0, null, null, null), null, null)));
                break;
            case R.id.next:
                ToosUtil.getInstance().addEventUmeng(RadioBoFangActivity.this, "event_music_next");
                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                        null, new VcontrolCmd.ControlCmd(11, 4, 0, null, null, null), null, null)));
                break;
            case R.id.suijis:
                ToosUtil.getInstance().addEventUmeng(RadioBoFangActivity.this, "event_music_single_play");
                mCurrentPlayMode++;
                if (mCurrentPlayMode == 4) {
                    mCurrentPlayMode = 0;
                }
                switch (mCurrentPlayMode) {
                    case PLAY_MODE_SHUNXU:
                        XGIMILOG.E("顺序播放");
                        mPlayModeIv.setImageResource(R.drawable.img_play_mode_xunhuan);
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                null, new VcontrolCmd.ControlCmd(11, 6, 0, null, null, null), null, null)));
                        break;
                    case PLAY_MODE_DANQU:
                        XGIMILOG.E("单曲播放");
                        mPlayModeIv.setImageResource(R.drawable.img_play_mode_danqu);
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                null, new VcontrolCmd.ControlCmd(11, 7, 0, null, null, null), null, null)));
                        break;
                    case PLAY_MODE_SUIJI:
                        XGIMILOG.E("随机播放");
                        mPlayModeIv.setImageResource(R.drawable.img_play_mode_suiji);
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                null, new VcontrolCmd.ControlCmd(11, 9, 0, null, null, null), null, null)));
                        break;
                }
                break;
            case R.id.daohang:
                if (Constant.netStatus) {
                    Intent intent = new Intent(this, RemountActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, NewSearchDeviceActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.netStatus) {
            daohang.setImageResource(R.drawable.yaokongqi);
        } else {
            daohang.setImageResource(R.drawable.gimi_yaokong);
        }
    }
}
