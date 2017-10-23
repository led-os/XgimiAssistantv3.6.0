package com.xgimi.zhushou;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.music.SDKEngine;
import com.baidu.music.SDKInterface;
import com.baidu.music.interfaces.OAuthInterface;
import com.baidu.music.manager.OAuthManager;
import com.baidu.music.onlinedata.PlayinglistManager;
import com.google.gson.Gson;
import com.waxrain.droidsender.SenderService;
import com.waxrain.droidsender.delegate.Global;


//import com.xgimi.device.callback.GMDeviceBrowserListener;
//import com.xgimi.device.callback.GMDeviceConnectorListener;
//import com.xgimi.device.device.FeedbackInfo;
//import com.xgimi.device.device.GMDevice;
//import com.xgimi.device.device.GMDeviceBrowser;
//import com.xgimi.device.device.GMDeviceConnector;
//import com.xgimi.device.device.GMDeviceStorage;


//import com.xgimi.device.utils.NsdUtil;


import com.xgimi.gmsdk.connect.GMDeviceProxyFactory;
import com.xgimi.gmsdk.connect.IGMDeviceProxy;


import com.xgimi.zhushou.activity.ApplyDetailActivity;
import com.xgimi.zhushou.activity.FilmDetailActivity;
import com.xgimi.zhushou.activity.MusicSearchActivity;
import com.xgimi.zhushou.activity.MyHuodongActivity;
import com.xgimi.zhushou.activity.MyXitongActivity;
import com.xgimi.zhushou.activity.MyYinshiActivity;
import com.xgimi.zhushou.activity.NewSearchDeviceActivity;
import com.xgimi.zhushou.activity.RadioBoFangActivity;
import com.xgimi.zhushou.activity.RemountActivity;
import com.xgimi.zhushou.activity.SearchApplyActivity;
//import com.xgimi.zhushou.activity.SearchDeviceActivity;
import com.xgimi.zhushou.activity.SearchLiveActivity;
import com.xgimi.zhushou.activity.SearchMovieActivity;
import com.xgimi.zhushou.activity.WebHtmlActivity;
import com.xgimi.zhushou.activity.XgimiCommunityActivity;
import com.xgimi.zhushou.bean.PushBean;
import com.xgimi.zhushou.bean.getAppTab;
import com.xgimi.zhushou.fragment.ApplyFragment;
import com.xgimi.zhushou.fragment.FilmFragment;
import com.xgimi.zhushou.fragment.FindFragment;
import com.xgimi.zhushou.fragment.MusicFragment;
import com.xgimi.zhushou.fragment.NewMusicFragment;
import com.xgimi.zhushou.fragment.livefragment.LiveAllChannelFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.popupwindow.TabPopupWindow;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.FindResource;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.MediaUtil;
import com.xgimi.zhushou.util.NetAbout;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.UpdateManager;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.QuitMirrorPlayComfirDialog;
import com.xgimi.zhushou.widget.WifiAilplay;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener, SDKInterface, SenderService.passwordLisener {

    private static final String TAG = "MainActivity";

    @Bind(R.id.ll_film)
    LinearLayout mLFilm;
    @Bind(R.id.ll_live)
    LinearLayout mLLive;
    @Bind(R.id.ll_music)
    LinearLayout mLMusic;
    @Bind(R.id.ll_apply)
    LinearLayout mLApply;
    @Bind(R.id.ll_person)
    LinearLayout mLPerson;
    @Bind(R.id.iv_remount)
    ImageView iv_remount;
    @Bind(R.id.id_toolbar)
    Toolbar mToorBar;
    @Bind(R.id.iv_play)
    ImageView iv_play;

    @Bind(R.id.iv_film)
    ImageView iv_film;
    @Bind(R.id.iv_live)
    ImageView iv_live;

    @Bind(R.id.iv_music)
    ImageView iv_music;
    @Bind(R.id.iv_apply)
    ImageView iv_apply;
    @Bind(R.id.iv_person)
    ImageView iv_person;

    @Bind(R.id.tv_film)
    TextView tv_film;

    @Bind(R.id.tv_live)
    TextView tv_live;

    @Bind(R.id.tv_music)
    TextView tv_music;
    @Bind(R.id.tv_apply)
    TextView tv_apply;
    @Bind(R.id.tv_person)
    TextView tv_person;
    public Fragment last_fragment;
    private View view;
    public int seach;
    private EditText edit;
    private SDKEngine engine;
    @Bind(R.id.layout)
    public FrameLayout mFrameLayout;
    private Subscription subscription;
    List<ImageView> ivs = new ArrayList<>();
    List<TextView> tvs = new ArrayList<>();
    private final static String CLIENT_ID = "DbR0Tkzklg21A2ez8gqng736";
    // "-------------请输入申请的APP Key--------------";
    private final static String CLIENT_SECRET = "Fd4Qd8ulFMLCkSfXSbGWdkZXY1xMqgIB";
    // "---------------请输入申请的Secret Key-----------------";
    private final static String SCOPE = "music_media_basic,music_search_basic";
    private OAuthManager manager;
    private TabPopupWindow menuWindow;
    private View pop_one, pop_two, pop_three, pop_four, pop_five;
    private TextView tv1, tv2, tv3, tv4, tv5;
    private getAppTab tabData;
    private int pxwidth;
    private int width;
    public Subscription subscription1;

    public int[] mBackgrounds = new int[]{
            R.drawable.iv_film, R.drawable.iv_live, R.drawable.iv_music, R.drawable.iv_apply, R.drawable.iv_find
    };
    private LinearLayout relativeLayout1;
    private LinearLayout bottom;

    private static Context _context = null;
    private boolean activityRunning = true;
    private ImageButton playButton = null;
    private ImageButton stopButton = null;

    private static final String STATE_MEDIAPROJECTION_RESULT_CODE = "result_mediaprojection_code";
    private static final String STATE_MEDIAPROJECTION_RESULT_DATA = "result_mediaprojection_data";
    public static final int REQUEST_MEDIA_PROJECTION = 1;
    private static int mMediaProjectionResultCode = 0;
    private static Intent mMediaProjectionResultData = null;
    public static int mImageWidth = 0;
    public static int mImageHeight = 0;
    public static int mScreenWidth = 0;
    public static int mScreenHeight = 0;
    public static int mDensityDPI = 0;
    public static Handler mainHandler = null;
    public static int pendingRequest = 0;
    public static Display _display = null;
    private static int mImageMaxBuffer = 2; // 1/2
    public static byte[] mImageBuffer = null;
    public static byte[] mImagePadding = null;
    private static boolean mImageAllocation = true;
    private static boolean mImageIgnorePadding = true;
    public static Object mImageReader = null;
    public static Object mMediaProjection = null; // MediaProjection
    public static Object mVirtualDisplay = null; // VirtualDisplay
    public static Object mMediaProjectionManager = null; // MediaProjectionManager
    private static boolean mMediaProjectionStopping = false;
    private QuitMirrorPlayComfirDialog mQuitMirrorDialog;

    Bundle msavedInstanceState;


    private IGMDeviceProxy mDeviceProxy;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XGIMILOG.E("MainActivity....................");
        App.mPhoneIP = App.getContext().getPhoneIp();
        XGIMILOG.D("phoneIP = " + App.mPhoneIP);
        view = View.inflate(this, R.layout.activity_main, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        GMDeviceProxyFactory.createDeviceProxy().initAuthentication("", "");

        msavedInstanceState = savedInstanceState;
        EventBus.getDefault().register(this);
        startSearchDevice();
        addFragement(FilmFragment.getInstance());
        addFragement(LiveAllChannelFragment.getInstance());
        addFragement(NewMusicFragment.getInstance());
        addFragement(ApplyFragment.getInstance());
        addFragement(FindFragment.getInstance());
        initData();
        initBaiDu();
        checkData();
        if (Integer.valueOf(Build.VERSION.SDK) >= 21) {
            initImage();
        }
//        checkPhoneControl();
        // WiFi下检测版本更新
        Log.e("info", ToosUtil.getInstance().getScreenWidth(this) + "--" + ToosUtil.getInstance().px2dip(this, ToosUtil.getInstance().getScreenWidth(this)));
        if (NetAbout.getNetworkType(MainActivity.this) == NetAbout.NETTYPE_WIFI) {
            UpdateManager.getUpdateManager().checkAppUpdate(MainActivity.this);
        }
        initExras();
        intentUrlActivity();
        relativeLayout1 = (LinearLayout) findViewById(R.id.rl_tv_pormt);
        bottom = (LinearLayout) findViewById(R.id.botton_table);
        mHandler.sendEmptyMessageDelayed(10000, 2000);
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewSearchDeviceActivity.class);
                startActivity(intent);
            }
        });
//


        XGIMILOG.E("查找本地文件...........");
        findFile();
        findVideo();
        findImage();
        //开线程找音乐
        findMusic();

    }


    private void getHeight(final RelativeLayout rl) {
        ViewTreeObserver vto = rl.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mHandler.sendEmptyMessage(10000);
            }
        });
    }


    //

    /**
     * 消除fragment的重叠
     */
//    public void onSaveInstanceState(Bundle outState) {
//        // TODO Auto-generated method stub
//        //super.onSaveInstanceState(outState);   //将这一行注释掉，阻止activity保存fragment的状态
//    }
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        //    super.onRestoreInstanceState(savedInstanceState);
//    }
//    public void onEventMainThread(FeedbackInfo info) {
//        if (info.installInfo != null && info.installInfo.stat == 1) {
//            if (info.installInfo.packagename.equals("com.elinkway.tvlive2")) {
//                Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();
//                SaveData.getInstance().mIsInstall = true;
//            }
//            SaveTVApp.getInstance(this).getApp(GMDeviceStorage.getInstance().getConnectedDevice().getIp());
//        }
//    }

    public void onEventMainThread(String info) {
        if (info.equals("tv")) {
            mLLive.performClick();
        } else if (info.equals("connectsucess")) {
            relativeLayout1.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDeviceProxy.isConnectedToDevice()) {
            iv_remount.setImageResource(R.drawable.yaokongqihui);
        } else {
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }
//        if (SenderService.serviceExiting == true) { // Pending exiting
//            finish();
//            return;
//        }
    }

    /**
     * 根据连接状态在首页显示未连接提示
     */
    private void initConnectLayout() {
//        XGIMILOG.E("" + (relativeLayout1 == null));
        if (Constant.netStatus) {
            iv_remount.setImageResource(R.drawable.yaokongqihui);
            if (relativeLayout1 != null) {
                relativeLayout1.setVisibility(View.GONE);
            }
        } else {
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
            if (relativeLayout1 != null) {
                relativeLayout1.setVisibility(View.VISIBLE);
            }
        }
    }

    // 初始化音乐需要的东西
    private void initBaiDu() {
        engine = SDKEngine.getInstance();
        engine.init(getApplicationContext(), CLIENT_ID, CLIENT_SECRET, SCOPE,
                this);
        manager = OAuthManager.getInstance(getApplicationContext());
        /**
         * 鉴权
         */
        if (manager.validate() < 5 * 24 * 60 * 60) {
            manager.authorize(new OAuthInterface.onAuthorizeFinishListener() {
                @Override
                public void onAuthorizeFinish(int status) {
                }
            });
        }
        PlayinglistManager.getInstance(getApplicationContext()).initPlayer(
                getApplicationContext());
    }

    private String one_id, two_id, three_id, four_id, five_id;
    private SharedPreferences sp;
    private SharedPreferences.Editor one_edit, two_edit, three_edit, four_edit, five_edit;

    private void initData() {
        //实例化SharedPreferences对象
        sp = this.getSharedPreferences("data", MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        one_edit = sp.edit();
        two_edit = sp.edit();
        three_edit = sp.edit();
        four_edit = sp.edit();
        five_edit = sp.edit();
        one_id = sp.getString("one_id", "one");
        two_id = sp.getString("two_id", "two");
        three_id = sp.getString("three_id", "three");
        four_id = sp.getString("four_id", "four");
        five_id = sp.getString("five_id", "five");

        ivs.add(iv_film);
        ivs.add(iv_live);
        ivs.add(iv_music);
        ivs.add(iv_apply);
        ivs.add(iv_person);
        tvs.add(tv_film);
        tvs.add(tv_live);
        tvs.add(tv_music);
        tvs.add(tv_apply);
        tvs.add(tv_person);
        edit = (EditText) findViewById(R.id.search);
        mLApply.setOnClickListener(this);
        mLLive.setOnClickListener(this);
        mLFilm.setOnClickListener(this);
        mLMusic.setOnClickListener(this);
        mLPerson.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        mLFilm.performClick();
        iv_remount.setOnClickListener(this);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActi(mIndexs);
            }
        });
        pop_one = findViewById(R.id.pop_one);
        pop_two = findViewById(R.id.pop_two);
        pop_three = findViewById(R.id.pop_three);
        pop_four = findViewById(R.id.pop_four);
        pop_five = findViewById(R.id.pop_five);
        tv1 = (TextView) findViewById(R.id.pop_one).findViewById(R.id.tab_pop_text);
        tv2 = (TextView) findViewById(R.id.pop_two).findViewById(R.id.tab_pop_text);
        tv3 = (TextView) findViewById(R.id.pop_three).findViewById(R.id.tab_pop_text);
        tv4 = (TextView) findViewById(R.id.pop_four).findViewById(R.id.tab_pop_text);
        tv5 = (TextView) findViewById(R.id.pop_five).findViewById(R.id.tab_pop_text);
        relativeLayout = (RelativeLayout) findViewById(R.id.mylinear);
        SenderService.setLisener(this);
    }

    private void startSearchDevice()  {
        XGIMILOG.D("startSearchDevice");
        if (mDeviceProxy == null) {
            mDeviceProxy = GMDeviceProxyFactory.createDeviceProxy();
        }
    }

    private int mIndexs;

    private void startActi(int a) {
        if (a == 0) {
            Intent intent = new Intent(MainActivity.this, SearchMovieActivity.class);
            startActivity(intent);
        } else if (a == 1) {
            Intent intent = new Intent(MainActivity.this, SearchLiveActivity.class);
            startActivity(intent);
        } else if (a == 2) {
            ToosUtil.getInstance().addEventUmeng(MainActivity.this, "event_music_search");
            Intent intent = new Intent(MainActivity.this, MusicSearchActivity.class);
            startActivity(intent);
        } else if (a == 3) {
            Intent intent = new Intent(MainActivity.this, SearchApplyActivity.class);
            startActivity(intent);
        } else if (a == 4) {

        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10000:
                    transYup(relativeLayout1, bottom.getHeight());
                    break;
                case EXIT:
                    isExit = false;
                    break;
                case 1000:
                    //实例化SelectPicPopupWindow
//                    if(num>1){
//                        menuWindow = new TabPopupWindow(MainActivity.this,"right");
//                        menuWindow.showAtLocation(iv_person, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
//                    }else {
//                        menuWindow = new TabPopupWindow(MainActivity.this,"left");
//                        menuWindow.showAtLocation(iv_person, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
//                    }
                    break;
                default:
                    break;
            }
        }
    };
    public static final int EXIT = 1005;
    public boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                exit();
            } else {
                super.onBackPressed();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private QuitMirrorPlayComfirDialog.QuitDialogListener mOnQuitDialogEnterClickListener = new QuitMirrorPlayComfirDialog.QuitDialogListener() {
        @Override
        public void onEnterClick() {
            App.getContext().closeNoticeWhenCloseMirrorPlay();
//            finish();
        }
    };
    public void exit() {
        if (!App.mIsPlayingMirrorOnDevice) {
            if (!isExit) {
                isExit = true;
                Toast.makeText(MainActivity.this, "再按一次退出无屏助手", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessageDelayed(EXIT, 2000);
            } else {
                finish();
            }
        } else {
            if (!App.getContext().isNoticeWhenCloseMirrorPlay()) {
                if (!isExit) {
                    isExit = true;
                    Toast.makeText(MainActivity.this, "再按一次退出无屏助手", Toast.LENGTH_SHORT).show();
                    mHandler.sendEmptyMessageDelayed(EXIT, 2000);
                } else {
                    finish();
                }
            } else {
                if (mQuitMirrorDialog != null) {
                    XGIMILOG.E("");
                    mQuitMirrorDialog.cancle();
                    finish();
                } else {
                    XGIMILOG.E("");
                    mQuitMirrorDialog = new QuitMirrorPlayComfirDialog(this, mOnQuitDialogEnterClickListener);
                    mQuitMirrorDialog.show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_film:
                initConnectLayout();
                MissDilog();
                edit.setText("搜索影视");
                iv_play.setVisibility(View.GONE);
                mIndexs = 0;
                mToorBar.setVisibility(View.VISIBLE);
                showFragmentWithoutBackStackAndAnim(FilmFragment.getInstance(), last_fragment, "film");

                last_fragment = FilmFragment.getInstance();

                seach = 1;
                ivs.get(0).setBackgroundResource(R.drawable.iv_film_selector);
                changeColor(0);
                if (num != -1) {
                    if (tabData != null && tabData.data.size() > 0 && tabData.data.get(num).position.equals("video")) {
                        one_edit.putString("one_id", tabData.data.get(num).red_point.id);
                        //提交当前数据
                        one_edit.commit();
//                        pop_one.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.ll_live:
                initConnectLayout();
                MissDilog();
                ToosUtil.getInstance().addEventUmeng(MainActivity.this, "event_discovery_search");
                edit.setText("搜索发现");
                iv_play.setVisibility(View.GONE);
                mToorBar.setVisibility(View.VISIBLE);
                mIndexs = 1;
//                showFragmentWithoutBackStackAndAnim(LiveFragment.getInstance(), last_fragment, "live");
//                last_fragment = LiveFragment.getInstance();
                showFragmentWithoutBackStackAndAnim(LiveAllChannelFragment.getInstance(), last_fragment, "live");
                last_fragment = LiveAllChannelFragment.getInstance();

                seach = 2;
                ivs.get(1).setBackgroundResource(R.drawable.iv_live_selctor);
                changeColor(1);
                //用putString的方法保存数据
                if (num != -1) {
                    if (tabData != null && tabData.data.size() > 0 && tabData.data.get(num).position.equals("discover")) {
                        four_edit.putString("four_id", tabData.data.get(num).red_point.id);
                        //提交当前数据
                        four_edit.commit();
//                        pop_four.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.ll_music:
                initConnectLayout();
                MissDilog();
                edit.setText("搜索音乐");
                iv_play.setVisibility(View.VISIBLE);
                mIndexs = 2;
                mToorBar.setVisibility(View.VISIBLE);
//                showFragmentWithoutBackStackAndAnim(MusicFragment.getInstance(), last_fragment, "musci");
//                last_fragment = MusicFragment.getInstance();

                showFragmentWithoutBackStackAndAnim(NewMusicFragment.getInstance(), last_fragment, "musci");
                last_fragment = NewMusicFragment.getInstance();
                seach = 3;
                ivs.get(2).setBackgroundResource(R.drawable.iv_music_selector);
                changeColor(2);
                //用putString的方法保存数据
                if (num != -1) {
                    if (tabData != null && tabData.data.size() > 0 && tabData.data.get(num).position.equals("music")) {
                        two_edit.putString("two_id", tabData.data.get(num).red_point.id);
                        //提交当前数据
                        two_edit.commit();
//                        pop_two.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.ll_apply:
                initConnectLayout();
                MissDilog();
                iv_play.setVisibility(View.GONE);
                edit.setText("搜索应用");
                mIndexs = 3;
                mToorBar.setVisibility(View.VISIBLE);
                showFragmentWithoutBackStackAndAnim(ApplyFragment.getInstance(), last_fragment, "apply");
                last_fragment = ApplyFragment.getInstance();
                ivs.get(3).setBackgroundResource(R.drawable.iv_apply_selector);
                changeColor(3);
                if (num != -1) {
                    if (tabData != null && tabData.data.size() > 0 && tabData.data.get(num).position.equals("app")) {
                        three_edit.putString("three_id", tabData.data.get(num).red_point.id);
                        //提交当前数据
                        three_edit.commit();
//                        pop_three.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.ll_person:
                MissDilog();
                relativeLayout1.setVisibility(View.GONE);
                iv_play.setVisibility(View.GONE);
                mIndexs = 4;
                mToorBar.setVisibility(View.GONE);
                showFragmentWithoutBackStackAndAnim(FindFragment.getInstance(), last_fragment, "find");

                last_fragment = FindFragment.getInstance();
                ivs.get(4).setBackgroundResource(R.drawable.iv_find_selector);
                changeColor(4);
                //用putString的方法保存数据
                if (num != -1) {
                    if (tabData != null && tabData.data.size() > 0 && tabData.data.get(num).position.equals("user")) {
                        five_edit.putString("five_id", tabData.data.get(num).red_point.id);
//                        提交当前数据
                        five_edit.commit();
//                        pop_five.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.iv_remount:
                if (mDeviceProxy.isConnectedToDevice()) {
                    Intent intent = new Intent(this, RemountActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, NewSearchDeviceActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_play:
                XGIMILOG.D("bofang_type = " + new Gson().toJson(SaveData.getInstance().bofang_type));
                if (SaveData.getInstance().bofang_type == null) {
                    Toast.makeText(MainActivity.this, "您暂无播放记录", Toast.LENGTH_SHORT).show();
                }
                else if (SaveData.getInstance().bofang_type.equals("MV")) {
//                    Intent intent1 = new Intent(this, MVDetailActivity.class);
//                    intent1.putExtra("ivplaytype", "true");
//                    startActivity(intent1);
                }
                else if (SaveData.getInstance().bofang_type.equals("Radio")) {
                    if ( SaveData.getInstance().mDetail == null || SaveData.getInstance().mDetail.data == null ||  SaveData.getInstance().mDetail.data.size() == 0) {
                        Toast.makeText(MainActivity.this, "您暂无播放记录", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent1 = new Intent(this, RadioBoFangActivity.class);
                        startActivity(intent1);
                    }
                } else if (SaveData.getInstance().bofang_type.equals("Music")) {
                    Intent intent1 = new Intent(this, RadioBoFangActivity.class);
                    startActivity(intent1);
                }
                break;
        }
    }


    private void changeColor(int postion) {
        for (int i = 0; i < tvs.size(); i++) {
            if (postion == i) {
                tvs.get(i).setTextColor(getResources().getColor(R.color.color_bule));
            } else {
                tvs.get(i).setTextColor(getResources().getColor(R.color.text_color_bule));
                ivs.get(i).setBackgroundResource(mBackgrounds[i]);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (SenderService.upnp != null) {
            SenderService.upnp.Stop();
        }
        stop_ScreenCapture();
        teardown_MediaProjection();
        stopService(new Intent(this, SenderService.class));
        activityRunning = false;
        SenderService.serviceExiting = true;
        super.onDestroy();
//        finish();
        System.exit(0);
        EventBus.getDefault().unregister(this);
    }

    private void intentUrlActivity() {
        Intent intent;
        if (App.getContext().advserMent == 1) {
            intent = new Intent(this, XgimiCommunityActivity.class);
            intent.putExtra("url", App.getContext().url);
            intent.putExtra("name", App.getContext().title);
            startActivity(intent);
        } else if (App.getContext().advserMent == 2) {
            intent = new Intent(this, WebHtmlActivity.class);
            intent.putExtra("url", App.getContext().url);
            startActivity(intent);
        } else if (App.getContext().advserMent == 3) {
            intent = new Intent(this, FilmDetailActivity.class);
            intent.putExtra("id", App.getContext().url);
            startActivity(intent);
        } else if (App.getContext().advserMent == 4) {
            intent = new Intent(this, ApplyDetailActivity.class);
            intent.putExtra("id", App.getContext().url);
            startActivity(intent);
        } else if (App.getContext().advserMent == 5) {
        }
    }

    @Override
    public void onOrdinaryInvalid() {
    }

    @Override
    public void onAccountTokenInvalid() {
    }

    private int num = -1;

//    @Override
//    public void deviceConnected(final GMDevice gm) {
//        Constant.netStatus = true;
//        XGIMILOG.E("连接成功");
//        if (mDeviceSearchUtil != null) {
//            mDeviceSearchUtil.stopSearchThread();
//        }
//        SaveTVApp.getInstance(MainActivity.this).getApp(gm.getIp());
//        SaveTVApp.getInstance(MainActivity.this).getApp1(gm.getIp());
//        App.mPhoneIP = App.getContext().getPhoneIp();
//        GMDeviceStorage.getInstance().gmdevice = gm;
//        EventBus.getDefault().post(GMDeviceStorage.getInstance().getConnectedDevice());
//        EventBus.getDefault().post("connectsucess");
//        App.getContext().saveConnectedDevice(gm);
//        XGIMILOG.E("成功连接到 : " + new Gson().toJson(gm));
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                iv_remount.setImageResource(R.drawable.yaokongqihui);
//            }
//        });
//    }

//    @Override
//    public void deviceNotConnected(GMDevice gm, int code) {
//
//    }

//    @Override
//    public int getVersion() {
//        return 2;
//    }

//    //检测是否是打开手机同屏
//    private void checkPhoneControl() {
//        subscription1 = Api.getMangoApi().getPhoneControl().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer1);
//    }

//    Observer<PhoneControl> observer1 = new Observer<PhoneControl>() {
//        @Override
//        public void onCompleted() {
//            if (subscription1 != null && !subscription1.isUnsubscribed()) {
//                subscription1.unsubscribe();
//            }
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            //Log.e("info",e.getMessage());
//        }
//
//        @Override
//        public void onNext(PhoneControl data) {
//            if (data != null && data.data != null && data.data.size() > 0) {
//                if (data.data.get(0).status != null && data.data.get(0).status.equals("1")) {
//                    SaveData.getInstance().isSupport = true;
//                } else {
//                    SaveData.getInstance().isSupport = false;
//                }
//            }
//        }
//    };

    //检测是否有弹窗
    private void checkData() {
        subscription = Api.getMangoApi().getAppTab().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<getAppTab> observer = new Observer<getAppTab>() {
        @Override
        public void onCompleted() {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(getAppTab getAppTab) {
            if (getAppTab.data.size() > 0) {
                tabData = getAppTab;
                if (getAppTab.data.size() == 5) {
                    for (int y = 0; y < getAppTab.data.size(); y++) {
                        if (getAppTab.data.get(y).position.equals("video")) {
                            tvs.get(0).setText(getAppTab.data.get(y).text);
                        }
                        if (getAppTab.data.get(y).position.equals("music")) {
                            tvs.get(2).setText(getAppTab.data.get(y).text);
                        }
                        if (getAppTab.data.get(y).position.equals("app")) {
                            tvs.get(3).setText(getAppTab.data.get(y).text);
                        }
                        if (getAppTab.data.get(y).position.equals("discover")) {
                            tvs.get(1).setText(getAppTab.data.get(y).text);
                        }
                        if (getAppTab.data.get(y).position.equals("user")) {
                            tvs.get(4).setText(getAppTab.data.get(y).text);
                        }
                    }
                }
                for (int i = 0; i < getAppTab.data.size(); i++) {
                    if (getAppTab.data.get(i).red_point.show.equals("1")) {
                        num = i;
                        break;
                    }
                }
                if (num != -1) {
                    if (!getAppTab.data.get(num).red_point.id.equals(one_id) && !getAppTab.data.get(num).red_point.id.equals(two_id) && !getAppTab.data.get(num).red_point.id.equals(three_id) && !getAppTab.data.get(num).red_point.id.equals(four_id) && !getAppTab.data.get(num).red_point.id.equals(five_id)) {
                        if (getAppTab.data.get(num).position.equals("video")) {
                            addView(1, getAppTab.data.get(num).title);
                        } else if (getAppTab.data.get(num).position.equals("music")) {
                            addView(2, getAppTab.data.get(num).title);
                        } else if (getAppTab.data.get(num).position.equals("app")) {
                            addView(3, getAppTab.data.get(num).title);
                        } else if (getAppTab.data.get(num).position.equals("discover")) {
                            addView(4, getAppTab.data.get(num).title);
                        } else if (getAppTab.data.get(num).position.equals("user")) {
                            addView(5, getAppTab.data.get(num).title);
                        }
                    }
                }
            }
        }
    };


    private int tvid;
    private RelativeLayout relativeLayout;

    //动态添加弹窗
    private void addView(int number, String data) {
        relativeLayout.setVisibility(View.VISIBLE);
        height = ToosUtil.getInstance().getScreenHeight(this);
        pxwidth = ToosUtil.getInstance().getScreenWidth(this);
        width = ToosUtil.getInstance().px2dip(MainActivity.this, pxwidth);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //  relativeLayout = new RelativeLayout(this);
        final TextView tv = new TextView(this);
        tv.setText(data);
        tv.setId(number);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setPadding(15, 15, 15, 15);
        if (number == 1 || number == 2) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.leftMargin = ToosUtil.getInstance().dip2px(MainActivity.this, width / 10 + (number - 1) * (width / 5));
            tv.setBackgroundResource(R.drawable.tabpop_left);
            tv.setLayoutParams(layoutParams);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.rightMargin = ToosUtil.getInstance().dip2px(MainActivity.this, width / 10 + (5 - number) * (width / 5));
            tv.setBackgroundResource(R.drawable.tabpop);
            tv.setLayoutParams(layoutParams);
        }
        relativeLayout.addView(tv);
    }
//    private void removeView(int id) {//动态删除组件（按钮）
//        //获取linearlayout子view的个数
////        int count = linearLayout.getChildCount();
//        //研究整个LAYOUT布局，第0位的是含add和remove两个button的layout
//        //第count-1个是那个文字被置中的textview
//        //因此，在remove的时候，只能操作的是0<location<count-1这个范围的
//        //在执行每次remove时，我们从count-2的位置即textview上面的那个控件开始删除~
////        if (count - 2 > 0) {
//            //count-2>0用来判断当前linearlayout子view数多于2个，即还有我们点add增加的button
//        relativeLayout.removeViewAt(id);
//        }

    //推送消息点击后的跳转
    private void initExras() {
        if (App.getContext().UM != null) {
            if (App.getContext().UM.extra != null) {
                Map<String, String> extra = App.getContext().UM.extra;

                Set<Map.Entry<String, String>> set = extra.entrySet();
                Iterator<Map.Entry<String, String>> iterator2 = set.iterator();
                while (iterator2.hasNext()) {
                    Map.Entry<String, String> entry = iterator2.next();
                    if (entry.getKey().equals("m")) {
                        Gson gson = new Gson();
                        PushBean push = gson.fromJson(entry.getValue(), PushBean.class);
                        if (push.pt == 1) {
                            Intent intnIntent = new Intent(this, MyYinshiActivity.class);
//                            intnIntent.putExtra("id", push.i);
//							intnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intnIntent);
                        } else if (push.pt == 2) {
                            Intent intnIntent = new Intent(this, MyHuodongActivity.class);
//                            intnIntent.putExtra("id", push.i);
//							intnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intnIntent);
                        } else if (push.pt == 3) {
                            Intent intnIntent = new Intent(this, MyXitongActivity.class);
//                            intnIntent.putExtra("id", push.i);
//							intnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intnIntent);
                        }
                    }
                }
            }
        }
    }

    //平移动画
    public void transYup(LinearLayout rl, int height) {
        float curTranslationY = rl.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(rl, "translationY", curTranslationY, -height);
        animator.setDuration(1000);
        animator.start();
    }

    public void transYdown(RelativeLayout rl) {
        float curTranslationY = rl.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(rl, "translationY", -curTranslationY, curTranslationY);
        animator.setDuration(2000);
        animator.start();
    }

    public static int[] getRealScreenSize(Context ctx) {
        Display _display = ((Activity) ctx).getWindowManager().getDefaultDisplay();
        DisplayMetrics _metrics = new DisplayMetrics();
        _display.getMetrics(_metrics); // Include NAVIGATION BAR
        if (Build.VERSION.SDK_INT >= 17) {
            _display.getRealMetrics(_metrics);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                Method method = _display.getClass().getMethod("getRealMetrics");
                method.invoke(_display, _metrics);
            } catch (Exception ex) {
            } catch (Throwable t) {
            }
        }
        int[] _ssize = new int[3];
        _ssize[0] = _metrics.widthPixels;
        _ssize[1] = _metrics.heightPixels;
        _ssize[2] = _metrics.densityDpi;
        return _ssize;
    }

    private void initImage() {
        if (SenderService.serviceExiting == true) { // Pending exiting
            finish();
            return;
        }
        _context = this;
        DisplayMetrics dm = new DisplayMetrics();
        if (_display == null)
            _display = getWindowManager().getDefaultDisplay();
        int[] ssize = getRealScreenSize(this);
        mScreenWidth = ssize[0];
        mScreenHeight = ssize[1];
        mDensityDPI = ssize[2];
        int rotation = _display.getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            int tmp = mScreenWidth;
            mScreenWidth = mScreenHeight;
            mScreenHeight = tmp;
        }

        mainHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (activityRunning == false) {
                    super.handleMessage(msg);
                    return; // Avoid of pendding UI request
                }
                switch (msg.what) {
                    case 1:
                        start_ScreenCapture(msg.arg1, msg.arg2);
                        pendingRequest--;
                        break;
                    case 2:
                        stop_ScreenCapture();
                        break;
                    default:
                        break;
                }
            }
        };

        if (Build.VERSION.SDK_INT >= 21) {
            try {
                mMediaProjectionManager = (Object) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                if (msavedInstanceState != null) {
                    mMediaProjectionResultCode = msavedInstanceState.getInt(STATE_MEDIAPROJECTION_RESULT_CODE);
                    mMediaProjectionResultData = msavedInstanceState.getParcelable(STATE_MEDIAPROJECTION_RESULT_DATA);
                }
            } catch (Exception ex) {
            } catch (Throwable th) {
            }
        }

        Intent mIntent = new Intent();
        mIntent.setAction("com.waxrain.droidsender.SenderService");
        mIntent.setPackage(_context.getPackageName());
        _context.startService(mIntent);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                if (mMediaProjectionResultData != null) {
                    outState.putInt(STATE_MEDIAPROJECTION_RESULT_CODE, mMediaProjectionResultCode);
                    outState.putParcelable(STATE_MEDIAPROJECTION_RESULT_DATA, mMediaProjectionResultData);
                }
            } catch (Exception ex) {
            } catch (Throwable th) {
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (Build.VERSION.SDK_INT >= 21) {
                try {
                    if (resultCode != Activity.RESULT_OK) {
                        Log.i(Global.LOGTAG, "MP cancelled");
                        return;
                    }
                    Log.i(Global.LOGTAG, "MP confirmed");
                    mMediaProjectionResultCode = resultCode;
                    mMediaProjectionResultData = data;
                    setup_MediaProjection();
                    setup_VirtualDisplay();
                } catch (Exception ex) {
                } catch (Throwable th) {
                }
            }
        }
    }

    public static int start_ScreenCapture(int width, int height) {
        XGIMILOG.E("");
        if (Build.VERSION.SDK_INT >= 21) {
            int retVal = 0;
            if (width == mImageWidth && height == mImageHeight)
                return retVal;
            stop_ScreenCapture(); // For screen rotation
            mImageWidth = width;
            mImageHeight = height;
            try {
                if (mMediaProjection != null) {
                    setup_VirtualDisplay();
                } else if (mMediaProjectionResultCode != 0 && mMediaProjectionResultData != null) {
                    setup_MediaProjection();
                    setup_VirtualDisplay();
                } else {
                    Log.i(Global.LOGTAG, "MP Request for " + mImageWidth + "x" + mImageHeight + "...");
                    // This initiates a prompt dialog for the user to confirm screen projection
                    ((Activity) _context).startActivityForResult(
                            ((MediaProjectionManager) mMediaProjectionManager).createScreenCaptureIntent(),
                            REQUEST_MEDIA_PROJECTION);
                }
            } catch (Exception ex) {
                retVal = -1;
            } catch (Throwable th) {
                retVal = -1;
            }
            return retVal;
        }
        return -2;
    }

    public static int stop_ScreenCapture() {
        XGIMILOG.E("");
        if (Build.VERSION.SDK_INT >= 21) {
            int retVal = 0;
            mMediaProjectionStopping = true;
            SenderService.ukm(1);
            mImageBuffer = null;
            mImagePadding = null;
            try {
                if (mVirtualDisplay != null) {
                    XGIMILOG.E("关闭镜像1....");
                    ((VirtualDisplay) mVirtualDisplay).release();
                    Log.i(Global.LOGTAG, "VD release...");
                }
                if (mImageReader != null) {
                    XGIMILOG.E("关闭镜像2....");
                    ((ImageReader) mImageReader).close();
                    App.mIsPlayingMirrorOnDevice = false;
                }
            } catch (Exception ex) {
                retVal = -1;
            } catch (Throwable th) {
                retVal = -1;
            }
            mVirtualDisplay = null;
            mImageReader = null;
            mImageWidth = 0;
            mImageHeight = 0;
            return retVal;
        }
        return -2;
    }

    public static int setup_MediaProjection() {
        if (Build.VERSION.SDK_INT >= 21) {
            int retVal = 0;
            try {
                mMediaProjection = (Object) ((MediaProjectionManager) mMediaProjectionManager).
                        getMediaProjection(mMediaProjectionResultCode, mMediaProjectionResultData);
            } catch (Exception ex) {
                retVal = -1;
            } catch (Throwable th) {
                retVal = -1;
            }
            return retVal;
        }
        return -2;
    }


    public void findImage() {
        XGIMILOG.E("开始查找图片");
        new FindResource(this).startFindImages();
    }

    public void findFile() {
        XGIMILOG.E("开始查找文件");
        new FindResource(this).startFindFiles();
    }

    public void findVideo() {
        XGIMILOG.E("开始查找视频");
        new FindResource(this).startFindVideos();
    }

    public void findMusic() {
        XGIMILOG.E("开始查找音乐");
        new Thread() {
            public void run() {
                GlobalConsts.mp3Infos = MediaUtil
                        .getMp3Infos(getApplicationContext());
                SaveData.getInstance().formerData = MediaUtil
                        .getMp3Infos(getApplicationContext());
            }
        }.start();
    }

    private static void setup_ImageCallback() {
        if (Build.VERSION.SDK_INT >= 21 && mImageReader != null) {
            ((ImageReader) mImageReader).setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    if (mMediaProjectionStopping == true) // Avoid entering dead lock
                        return;
                    Image image = null;
                    try {
                        boolean surfaceChanged = false;
                        if (mImageReader != null) {
                            //long _startTs = System.currentTimeMillis();
                            image = ((ImageReader) mImageReader).acquireLatestImage();
                            Image.Plane[] planes = image.getPlanes();
                            int scr_pixelBytes = planes[0].getPixelStride();
                            int scr_linesize = planes[0].getRowStride();
                            final ByteBuffer buffer = planes[0].getBuffer();
                            if (buffer != null) {
                                int buffer_size = buffer.capacity();
                                if (buffer_size > 0) {
                                    if (mImageIgnorePadding == false) {
                                        if (mImageWidth != scr_linesize / scr_pixelBytes) {
                                            mImageWidth = scr_linesize / scr_pixelBytes;
                                            surfaceChanged = true;
                                        }
                                        if (mImageHeight != buffer_size / scr_linesize) {
                                            mImageHeight = buffer_size / scr_linesize;
                                            surfaceChanged = true;
                                        }
                                        if (surfaceChanged == true) {
                                            mImageReader = (Object) ImageReader.newInstance(mImageWidth, mImageHeight, PixelFormat.RGBA_8888, mImageMaxBuffer);
                                            ((VirtualDisplay) mVirtualDisplay).setSurface(((ImageReader) mImageReader).getSurface());
                                            setup_ImageCallback();
                                            return;
                                        }
                                    }
                                    SenderService.lkm(1);
                                    if (mImageAllocation == false) {
                                        mImageBuffer = buffer.array();
                                    } else {
                                        if (mImageIgnorePadding == false) {
                                            if (mImageBuffer == null || mImageBuffer.length != buffer_size)
                                                mImageBuffer = new byte[buffer_size];
                                            buffer.get(mImageBuffer, 0, buffer_size);
                                        } else {
                                            int dst_offset = 0;
                                            int dst_linesize = mImageWidth * 4;
                                            int linediff = scr_linesize - dst_linesize;
                                            int y = 0;
                                            if (mImageBuffer == null)
                                                mImageBuffer = new byte[dst_linesize * mImageHeight];
                                            if (linediff == 0) {
                                                buffer.get(mImageBuffer, 0, buffer_size);
                                            } else if (linediff > 0) {
                                                if (mImagePadding == null || mImagePadding.length != linediff)
                                                    mImagePadding = new byte[linediff];
                                                for (y = 0; y < mImageHeight; y++) {
                                                    buffer.get(mImageBuffer, dst_offset, dst_linesize);
                                                    buffer.get(mImagePadding, 0, linediff); // Skip padding size
                                                    dst_offset += dst_linesize;
                                                }
                                            } else if (linediff < 0) { // WILL THIS HAPPEN?
                                                for (y = 0; y < mImageHeight; y++) {
                                                    buffer.get(mImageBuffer, dst_offset, scr_linesize);
                                                    dst_offset += dst_linesize;
                                                }
                                            }
                                        }
                                    }
                                    SenderService.ukm(1);
                                    //Log.i(Global.LOGTAG,"onImageAvailable "+mImageWidth+"x"+mImageHeight+" in "+(System.currentTimeMillis()-_startTs)+"ms");
                                }
                            }
                        }
                    } catch (Exception ex) { // Maybe stop_ScreenCapture() called in onPause()
                    } catch (Throwable th) { // Maybe stop_ScreenCapture() called in onPause()
                    } finally {
                        if (image != null)
                            image.close();
                    }
                }
            }, null);
        }
    }

    public static int setup_VirtualDisplay() {
        if (Build.VERSION.SDK_INT >= 21) {
            int retVal = 0;
            mImageMaxBuffer = 2;
            if (mImageAllocation == false)
                mImageMaxBuffer = 2;
            try {
                mImageReader = (Object) ImageReader.newInstance(mImageWidth, mImageHeight, PixelFormat.RGBA_8888, mImageMaxBuffer);
                if (mImageReader == null)
                    return -1;
                mVirtualDisplay = (Object) ((MediaProjection) mMediaProjection).createVirtualDisplay("ScreenCapture",
                        mImageWidth, mImageHeight, mDensityDPI,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        ((ImageReader) mImageReader).getSurface(), new VirtualDisplay.Callback() {
                            @Override
                            public void onResumed() {
                                super.onResumed();
                            }

                            @Override
                            public void onPaused() {
                                super.onPaused();
                            }
                        }, null);
                mMediaProjectionStopping = false;
                SenderService.ukm(1);
                mImageBuffer = null;
                setup_ImageCallback();
                Log.i(Global.LOGTAG, "VD Changed to " + mImageWidth + "x" + mImageHeight + "...");
            } catch (Exception ex) {
                retVal = -1;
            } catch (Throwable th) {
                retVal = -1;
            }
            return retVal;
        }
        return -2;
    }

    public static int readDone_ScreenCapture() {
        if (Build.VERSION.SDK_INT >= 21) {
            SenderService.ukm(1);
            return 0;
        }
        return -1;
    }

    public static byte[] read_ScreenCapture() {
        if (Build.VERSION.SDK_INT >= 21) {
            int timeout = 5000;
            while (mImageBuffer == null && timeout > 0) {
                Global.do_sleep(2);
                timeout -= 2;
            }
            if (timeout > 0) {
                SenderService.lkm(1);
                return mImageBuffer;
            }
        }
        return null;
    }

    public static int get_ScreenCaptureWidth() {
        int timeout = 5000;
        while (mImageBuffer == null && timeout > 0) {
            Global.do_sleep(10);
            timeout -= 10;
        }
        return mImageWidth;
    }

    public static int get_ScreenCaptureHeight() {
        int timeout = 5000;
        while (mImageBuffer == null && timeout > 0) {
            Global.do_sleep(10);
            timeout -= 10;
        }
        return mImageHeight;
    }

    public static int teardown_MediaProjection() {
        if (Build.VERSION.SDK_INT >= 21) {
            int retVal = 0;
            try {
                if (mMediaProjection != null)
                    ((MediaProjection) mMediaProjection).stop();
            } catch (Exception ex) {
                retVal = -1;
            } catch (Throwable th) {
                retVal = -1;
            }
            mMediaProjection = null;
            return retVal;
        }
        return -2;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    /**
     * 用户更改了设备端无线同屏的密码
     * 弹出对话框等待用户输入密码
     * @param path
     */
    @Override
    public void path(int path) {
        if (path == 0) {
            WifiAilplay dilog = new WifiAilplay(this, "");
            dilog.show();
        }
    }

    public void showLiveFragment() {
        mLLive.setVisibility(View.VISIBLE);
    }

    public void hideLiveFragment() {
        mLLive.setVisibility(View.GONE);
        removeFragment(LiveAllChannelFragment.getInstance());
    }

//    @Override
//    public void GMDevice(GMDevice gmdevices) {
//        addDeviceToFoundDeviceList(gmdevices);
//        Log.d(TAG, "Found GMDevice: " + gmdevices.toString() + "\nFoundDeviceListSize = " + mFoundDeviceList.size());
//        mSearchStopHandler.removeMessages(0);
//        mSearchStopHandler.sendEmptyMessageDelayed(0, 1000);
//    }
}

