package com.xgimi.zhushou;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
//
//import com.baidu.music.login.LoginManager;
//import com.baidu.music.manager.ImageManager;
//import com.baidu.music.util.LogUtil;
//import com.baidu.utils.AdvertiseMentUtil;
import com.baidu.music.login.LoginManager;
import com.baidu.music.manager.ImageManager;
import com.baidu.music.model.ArtistList;
import com.baidu.music.model.Playlist;
import com.baidu.music.model.TopLists;
import com.baidu.music.util.LogUtil;
import com.baidu.utils.AdvertiseMentUtil;
import com.baidu.voicerecognition.android.VoiceRecognitionClient;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.scwang.smartrefresh.layout.SmartRefreshLayout;
//import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
//import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
//import com.scwang.smartrefresh.layout.api.RefreshFooter;
//import com.scwang.smartrefresh.layout.api.RefreshHeader;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
//import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
//import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.message.ALIAS_TYPE;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.xgimi.device.device.GMDevice;
import com.xgimi.device.device.GMDeviceConnector;
import com.xgimi.device.utils.FileUtils;
import com.xgimi.device.utils.StringUtils;
import com.xgimi.zhushou.bean.CkeckData;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.Heartbeat;
import com.xgimi.zhushou.bean.HotRadio;
import com.xgimi.zhushou.netUtil.HttpServer;
import com.xgimi.zhushou.util.ACache;
import com.xgimi.zhushou.util.FindResource;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.MediaUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.XGIMILOG;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by 霍长江 on 2016/8/5.
 */
public class App extends Application {


    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.main_blue_dark, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                layout.setPrimaryColorsId(android.R.color.white, R.color.main_blue_dark);
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    private static final String TAG = "App";
    private static App INSTANCE;
    public String PACKAGENAME = "com.xgimi.zhushou";
    public String YINYUETAI = "音悦台";
    public String BAIDUYINYUE = "百度音乐";
    public String QingTingFM = "蜻蜓FM";
    public String SOUSUO = "搜索";
    //过度页面
    public int advserMent = -1;
    public String url;
    public String title;

    public static String mPhoneIP; // 手机ip
    public String ConnectedIP = ""; // 设备ip
    public String connectionIp = "";// 连接ip
    public SharedPreferences sp;
    private ACache aCache;
    public static final String ExternalImageDir = "zhushou";

    public String avatarpath = "useravatar";
    public String music = "music";
    public String avatarpath_bk = "useravatar_bk";

    public static String CachePath = "image_loaders_local";
    // 保存各种头像和用户名实例
    private ImageView avatar_main = null;
    private TextView username_main = null;
    private ImageView avatar_setting = null;
    private TextView username_setting = null;
    public ImageView avatar_userinfo = null;
    public com.umeng.message.entity.UMessage UM;

    public static boolean mOpenDeviceByMagicPacket = false;
    public static boolean mIsPlayingMirrorOnDevice = false;

    private VoiceRecognitionClient mVoiceRecognitionClient;


    public String getPhoneIp() {
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //检查Wifi状态
        if (!wm.isWifiEnabled()) {
            wm.setWifiEnabled(true);
        }
        WifiInfo wi = wm.getConnectionInfo();
        //获取32位整型IP地址
        int ipAdd = wi.getIpAddress();
        //把整型地址转换成“*.*.*.*”地址
        String ip = intToIp(ipAdd);
        return ip;
    }

    public boolean isNoticeWhenCloseMirrorPlay() {
        String res = aCache.getAsString("notice_when_close_mirror");
        if (res == null || "".equals(res)) {
            return true;
        } else {
            return false;
        }
    }

    public void closeNoticeWhenCloseMirrorPlay() {
        aCache.put("notice_when_close_mirror", "close");
    }


    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public static App getContext() {
        return INSTANCE;
    }


    public void saveTopSinger(String singerCache) {
        aCache.put("top_singer", singerCache);
    }

    public ArtistList getTopSinger() {
        String cache = aCache.getAsString("top_singer");
        if (cache == null) {
            return null;
        }
        ArtistList artistList = null;
        try {
            artistList = new Gson().fromJson(cache, ArtistList.class);
        } catch (Exception e) {
            XGIMILOG.E(e.getMessage());
            e.printStackTrace();
        }
        return artistList;
    }

    public void saveTopList(String topList) {
        aCache.put("top_list", topList);
    }

    public TopLists getTopList() {
        String cache = aCache.getAsString("top_list");
        if (aCache == null) {
            return null;
        }
        TopLists topLists = null;
        try {
            topLists = new Gson().fromJson(cache, TopLists.class);
        } catch (Exception e) {
            XGIMILOG.E(e.getMessage());
            e.printStackTrace();
        }
        return topLists;
    }

    public void saveTopPlayList(String playList) {
        aCache.put("top_play_list", playList);
    }

    public Playlist getTopPlayList() {
        String cache = aCache.getAsString("top_play_list");
        if (cache == null) {
            return null;
        }
        Playlist playlist = null;
        try {
            playlist = new Gson().fromJson(cache, Playlist.class);
        } catch (Exception e) {
            XGIMILOG.E(e.getMessage());
            e.printStackTrace();
        }
        return playlist;
    }


    //版本名
    public String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }


    private static boolean isDebugVersion(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        if (!XGIMILOG.mHasInit) {
//            XGIMILOG.initTag("XGIMI_Assistant", isDebugVersion(this));
            XGIMILOG.initTag("XGIMI_Assistant");
        }

        MultiDex.install(this);
        sp = getSharedPreferences("Some_time", Context.MODE_PRIVATE);
        super.onCreate();
        INSTANCE = this;
//        receiveUMeng();
        if (aCache == null) {
            aCache = ACache.get(this, "zhushou" + getVersionCode(this));
        }
        initImageLoader();
        initBaiduVoice();
        try {
            new HttpServer(7766);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        GMDeviceConnector.getInstance().setGmdeiveceLost(
                Heartbeat.getInstance(this));
        sp = getSharedPreferences("Some_time", Context.MODE_PRIVATE);
        FileUtils.createDirectory(ExternalImageDir);

        avatarpath = getFilesDir().getAbsolutePath() + File.separator
                + avatarpath;
        avatarpath_bk = getFilesDir().getAbsolutePath() + File.separator
                + avatarpath_bk;

        music = getFilesDir().getAbsolutePath() + File.separator + music;

        ImageManager.init(this, ImageManager.POSTFIX_JPG,
                "/sdcard/baidu/music/", 1 * 1024 * 1024);
        /**
         * 设置debug模式；debug模式下会打印SDK内部Log，反之不打印；
         */
        LogUtil.setDebugMode(false);

        /**
         * 设置广告模式：true表示沙盒环境
         */
        AdvertiseMentUtil.setSandBoxMode(false);
        long validate = LoginManager.getInstance(getApplicationContext())
                .validate();
        initImageLoader();
        createSDCardDir();
        //友盟推送的
        final PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                XGIMILOG.E("注册友盟推送成功 : " + deviceToken);
//                mPushAgent.addAlias("123", "456", new UTrack.ICallBack() {
//
//                    @Override
//                    public void onMessage(boolean b, String s) {
//                        XGIMILOG.E("b = " + b + ", s = " + s);
//                    }
//                });
//                mDeviceToken=deviceToken;
            }

            @Override
            public void onFailure(String s, String s1) {
                XGIMILOG.E("注册友盟推送失败 : s = " + s + ", s1 = " + s1);
            }

        });

        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);

        /**
         * 自定义行为的回调处理
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                UM = msg;
                XGIMILOG.E("dealWithCustomAction");
                if (msg.custom != null) {
                    String json = new Gson().toJson(msg);
                    XGIMILOG.E(msg.custom);
                    XGIMILOG.E(json);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    private void initBaiduVoice() {
        if (mVoiceRecognitionClient == null) {
            mVoiceRecognitionClient = VoiceRecognitionClient.getInstance(this);
            mVoiceRecognitionClient.setTokenApis("Rv6V3k1PVtLugTsO18yu4d83",
                    "ESGl4ep5Pq6tOOW9AmZOG6544fLIKy5k");
        }
    }

    public VoiceRecognitionClient getBaiduVoiceClient() {
        return mVoiceRecognitionClient;
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

    private void initImageLoader() {
        // DisplayImageOptions options = new DisplayImageOptions.Builder()
        // .showImageOnLoading(R.drawable.icon_default)
        // .bitmapConfig(Config.RGB_565).cacheOnDisk(true)
        // .cacheInMemory(true).build();
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // .showImageOnLoading(R.drawable.icon_default)
                .showImageOnLoading(R.drawable.zhanweitu)
                .showImageForEmptyUri(R.drawable.zhanweitu).showImageOnFail(R.drawable.zhanweitu)
                // .showImageOnLoading(LayoutToDrawable());
//				.showImageOnLoading(new BitmapDrawable(convertViewToBitmap()))

                .bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
                // .cacheOnDisc(true)
                // .considerExifParams(true)
                .build();
        File dir = getDir(CachePath, 0).getAbsoluteFile();
        ImageLoaderConfiguration loaderConfiguration = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(options).threadPoolSize(3)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheExtraOptions(480, 800)
                .diskCache(new UnlimitedDiscCache(dir)).build();
        ImageLoader.getInstance().init(loaderConfiguration);
    }

    private String appurl = "http://%s:16740/data/data/com.xgimi.filefly/app_appDatas/list";

    /**
     * 添加到销毁队列
     * 用于文件传输那儿一次性返回两个界面s
     *
     * @param activity 要销毁的activity
     */
    Map<String, Activity> destoryMap = new HashMap<>();

    public void addDestoryActivity(Activity activity, String activityName) {
        destoryMap.put(activityName, activity);
    }

    /**
     * 销毁指定Activity
     */
    public void destoryActivity(String activityName) {
        Set<String> keySet = destoryMap.keySet();
        for (String key : keySet) {
            destoryMap.get(key).finish();
        }
    }

    // 缓存首页数据
    public void savaHomeJson(String key, String value) {
        aCache.put(key, value);
    }

    public String readHomeJson(String key) {
        String readJson = aCache.getAsString(key);
        return readJson;
    }

    /**
     * 设置重力感应开关状态
     *
     * @param i 0 关闭重力感应 1 灵敏度 低 2 灵敏度 中 3 灵敏度 高
     */
    public void setGravitySenseStatus(int i) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("GravitySenseState", i);
        editor.commit();
    }

    public int getGravitySenseStatus() {
        return sp.getInt("GravitySenseState", 3);
    }

    //
    // 保存案件整栋的状态
    // 存到配置清单里
    public boolean saveZhuangtai(boolean status) {
        return PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("anjian", status).commit();
    }

    public boolean readZhuangtai() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                "anjian", true);
    }

    // 保存小键盘的状态
    // 保存百度账号
    public boolean saveXiaoJianPan(boolean name) {
        return PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("xiaojianpan", name).commit();
    }

    // 阅读歌曲的状态
    public int readMusicStatus() {
        return PreferenceManager.getDefaultSharedPreferences(this).getInt(
                "musicstatus", 1);
    }

    // 保存歌曲的状态
    public boolean saveMusicStats(int status) {

        return PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putInt("musicstatus", status).commit();
    }


    public boolean readXiaoJianPan() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                "xiaojianpan", true);
    }

    //记住投屏时的密码
    public boolean saveAilplayPasswrid(String status) {

        return PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString("ailplaypassword", status).commit();
    }

    public String readAilplayPasswrid() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(
                "ailplaypassword", null);
    }


    // 退出销毁登陆信息
    public void clianLogoInfor() {
        aCache.clear();
    }

    /*
 * 获取登陆用户信息
 */
    public GimiUser getLoginInfo() {
        GimiUser user = new GimiUser();

        String uid = aCache.getAsString("user.data.uid");

        if (StringUtils.isEmpty(uid)) {
            user = null;
        } else {
            user.data.uid = uid;
            user.data.username = aCache.getAsString("user.data.username");
            user.data.email = aCache.getAsString("user.data.email");
            user.data.avatar = aCache.getAsString("user.data.avatar");
            user.data.token = aCache.getAsString("user.data.token");
            user.data.tel = aCache.getAsString("user.data.tel");
            user.data.picturelist = aCache.getAsString("user.data.picturelist");
        }
        return user;
    }

    /*
     * 将用户登录信息保存到本地
	 */
    public void saveLoginInfo(GimiUser user) {
        Log.d(TAG, "saveLoginInfo: user id  = " + user.data.uid);
        aCache.put("user.data.uid", user.data.uid);
        aCache.put("user.data.username", user.data.username);
        aCache.put("user.data.email", user.data.email);
        aCache.put("user.data.avatar", user.data.avatar);
        aCache.put("user.data.token", user.data.token);
        aCache.put("user.data.tel", user.data.tel);
        if (user.data.picturelist != null) {
            aCache.put("user.data.picturelist", user.data.picturelist);
        }
    }

    /**
     * 退出的时候需要更新头像
     */
    public void updateAvatar() {
        updateAvatar(false);
        updateAvatar(true);
    }

    /**
     * 用户头像和用户名信息更新，登陆之后。更换头像。注销的时候都需要调用
     * <p>
     * 方便控制头像和用户名的显示
     *
     * @param islogin 是否是登陆操作,区分修改头像和登陆之后
     */
    public void updateAvatar(boolean islogin) {

        GimiUser gimiUser = getLoginInfo();

        if (gimiUser == null) {
            if (new File(avatarpath).exists()) {
                new File(avatarpath).delete();
            }
            avatar_main.setImageResource(R.drawable.app_icon);
            username_main.setText(R.string.app_name);

            avatar_setting.setImageResource(R.drawable.app_icon);
            username_setting.setText(R.string.app_name);

        } else {
            Drawable drawable = Drawable.createFromPath(avatarpath);

            if (drawable == null) {
                drawable = getResources().getDrawable(R.drawable.icon_default);
            }

            if (avatar_main != null & avatar_setting != null) {
                avatar_main.setImageDrawable(drawable);
                avatar_setting.setImageDrawable(drawable);
            }

            if (avatar_userinfo != null) {
                avatar_userinfo.setImageDrawable(drawable);
            }

            if (islogin) {
                if (username_main != null && username_setting != null) {
                    username_main.setText(gimiUser.data.username);
                    username_setting.setText(gimiUser.data.username);
                }
            }
        }
    }

    /**
     * 查询到大头像地址之后保存下来
     *
     * @param avatarUrl 头像地址
     */
    public void setUserAvatarUrl(String avatarUrl) {
        aCache.put("user.data.avatar.big", avatarUrl);
    }

    /*
 * 保存用户密码，简单加密
 */
    public void SaveUserPwd(String pwd) {
        aCache.put("user.data.xgimippwwdd", pwd);
    }

    // 保存用手机音量键控制
    public boolean saveVoiceContrl(boolean status) {
        return PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("phonevoice", status).commit();
    }

    public boolean readVoiceContrl() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                "phonevoice", true);
    }
    //保存连接过的ip
    // 保存上次连接


    //保存用户保存的mac
    // 保存用手机音量键控制
    public boolean saveMachineMac(boolean status) {
        return PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("phonevoice", status).commit();
    }

    public boolean readMachineMac() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                "phonevoice", true);
    }

    public boolean saveDeviceIp(String ip) {
        XGIMILOG.D("保存本次连接的IP : " + ip);
        return PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString("deviceip", ip).commit();
    }

    public void saveConnectedDevice(GMDevice device) {
        XGIMILOG.D("保存本次连接的设备 : " + new Gson().toJson(device));
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString("connetcedDevice", new Gson().toJson(device)).apply();
    }

    public String readDeviceIp() {
        String lastTimeConnectIP = PreferenceManager.getDefaultSharedPreferences(this).getString("deviceip", null);
        XGIMILOG.D("读取上次连接的IP : " + lastTimeConnectIP);
        return lastTimeConnectIP;
    }

    public GMDevice readLastConnectedDevice() {
        String lastTimeConnectDevice = PreferenceManager.getDefaultSharedPreferences(this).getString("connetcedDevice", null);
        return new Gson().fromJson(lastTimeConnectDevice, GMDevice.class);
    }

    private String path;

    //在SD卡上创建一个文件夹
    public void createSDCardDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
            path = sdcardDir.getPath() + "/XGIMI";
            File path1 = new File(path);
            if (!path1.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
                path1.mkdirs();
                //setTitle("paht ok,path:"+path);
            }
        } else {
            //setTitle("false");
            return;

        }
    }

    public void saveRadio(HotRadio hotRadio) {
        aCache.put("hotRadioInfo", new Gson().toJson(hotRadio));
    }

    public HotRadio getRadioCache() {
        String res = aCache.getAsString("hotRadioInfo");
        if (res == null) {
            return null;
        } else {
            HotRadio radio = new Gson().fromJson(res, HotRadio.class);
            return radio;
        }
    }

    public void saveChannelData(String s) {
        aCache.put("chanel_data", s);
    }

    public CkeckData getChannelData() {
        String res = aCache.getAsString("chanel_data");
        if (res == null || res.length() == 0) {
            return null;
        } else {
            CkeckData channel = new Gson().fromJson(res, CkeckData.class);
            return channel;
        }
    }
}
