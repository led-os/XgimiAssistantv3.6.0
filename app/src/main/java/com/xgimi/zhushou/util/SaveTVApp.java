package com.xgimi.zhushou.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.device.callback.FeedBackAppGetInterface;
import com.xgimi.device.callback.GMDeviceMusicInfor;
import com.xgimi.device.device.GMDeviceConnector;
import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMAppController;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.bean.ApplyFeilei;
import com.xgimi.zhushou.bean.TvApp;
import com.xgimi.zhushou.bean.Vctrolbean;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 霍长江 on 2016/8/17.
 */
public class SaveTVApp implements GMDeviceMusicInfor, FeedBackAppGetInterface {

    private static final String TAG = "SaveTVApp";
    public static SaveTVApp instance;
    public ApplyFeilei feilei;
    private Subscription subscription;
    private Context mContext;

    private SaveTVApp(Context context) {
        this.mContext = context;
    }

    public static SaveTVApp getInstance(Context context) {
        if (instance == null) {
            instance = new SaveTVApp(context);

        }
        return instance;
    }

    private String mUrl = "http://apkhome.qiniudn.com/XGIMIAssistantFile/gimihelper_remote.apk";

    private String appurl = "http://%s:16740/data/data/com.xgimi.filefly/app_appDatas/list";
    public TvApp mTvApp;
    public boolean isWirelessScreen = false;

    public void getApp(String ip) {
        // TODO Auto-generated method stub
        Log.e("tvzhushou", "getApp来了" + ip);
        if (ip != null) {
            GMDeviceConnector.getInstance().setFeedBackInforLisener(SaveTVApp.this);
            XGIMILOG.E("开始获取设备端应用列表");
            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                    null, new VcontrolCmd.ControlCmd(7, 3, 0, null, null, null), null, null)));
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("tvzhushou", SaveData.getInstance().isNewTv + "");
            if (!SaveData.getInstance().isNewTv) {
                getApplyDetail();
                GMDeviceConnector.getInstance().setGmDeviceMusic(SaveTVApp.this);
            }
        }
    };

    public void getApp1(String ip) {
        // TODO Auto-generated method stub
        isInstall = false;
        if (ip != null) {
            XGIMILOG.E("10秒钟之后获取更新请求");
            mHandler.sendEmptyMessageDelayed(2000, 10000);

//            HttpRequest.getInstance(mContext).getTouYingApp(String.format(appurl, ip), new CommonCallBack<TvApp>() {
//                @Override
//                public void onSuccess(TvApp data) {
//                    // TODO Auto-generated method stub
//                    if(data!=null){
//                        for (int i = 0; i <data.appList.size() ; i++) {
//                            if("com.xgimi.vcontrol".equals(data.appList.get(i).packageName)){
//                                isInstall=true;
////                                GMAppController.getInstance().openApp("com.xgimi.vcontrol");
//                            }
//                        }
//                    }
//                    if(!isInstall){
//                        getApplyDetail();
//                    }
//                }
//                @Override
//                public void onStart() {
//                    // TODO Auto-generated method stub
//                }
//                @Override
//                public void onFailed(String reason) {
//                    // TODO Auto-generated method stub
//                }
//            });
        }

    }


    //获取应用详情页面
    public void getApplyDetail() {
        XGIMILOG.E("开始获取更新请求");

        subscription = Api.getMangoApi().getVcontrol()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    Observer<Vctrolbean> observer = new Observer<Vctrolbean>() {
        @Override
        public void onCompleted() {
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Vctrolbean channels) {
            XGIMILOG.E("获取更新请求 :-------------------------------: \n");
//            LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(channels));
            if (channels != null && channels.data != null) {
                GMAppController.getInstance().installFilFly(channels.data.download_url, channels.data.package_name);
            } else {
                GMAppController.getInstance().installFilFly(mUrl, "com.xgimi.vcontrol");
            }
        }
    };

    private boolean isInstall = false;


    String prefix = "http://image.xgimi.com/recommend/musicPic/";

    public String[] BangDanTubiao = {prefix + "singertop.png",
            prefix + "singettop2.png", prefix + "billboard.png",
            prefix + "hitochinese.png", prefix + "ktvhot.png", prefix + "singer200.png",
            prefix + "oumeijnqu.png", prefix + "huayujingqu.png", prefix + "yangmi.png",
            prefix + "qinggeduichang.png", prefix + "wangluogequ.png",
            prefix + "jindianlaoge.png", prefix + "wuqubang.png",
            prefix + "yaogunbang.png", prefix + "jueshibang.png",
            prefix + "mingyaobang.png", prefix + "chichabang.png",
            prefix + "biaosheng.png", prefix + "biaoshengomei.png"};

    @Override
    public void musicInfor(String s) {
        JSONObject josnJsonObject;
        try {
            josnJsonObject = new JSONObject(s);
            int action = josnJsonObject.getInt("action");

            if (action == 200) {
                Toast.makeText(mContext, "正在下载无屏助手TV版", Toast.LENGTH_SHORT).show();
            } else if (action == 201) {
//                Toast.makeText(mContext, "正在安装插件", Toast.LENGTH_SHORT).show();
            } else if (action == 202) {
                GMAppController.getInstance().openApp("com.xgimi.vcontrol");
                Toast.makeText(mContext, "正在打开无屏助手TV版", Toast.LENGTH_SHORT).show();
                SaveData.getInstance().isNewTv = true;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//		}
        }
    }

    /**
     * 获取返回的应用
     *https://api.xgimi.com/apis/rest/videoSourceService/getLive?u_=1&v_=1&t_=1&p_=2348
     * @param url
     */
    @Override
    public void getUrl(String url) {
        HttpRequest.getInstance(mContext).getTouYingApp(String.format(url, GMDeviceStorage.getInstance().getConnectedDevice().getIp()), new CommonCallBack<TvApp>() {
            @Override
            public void onSuccess(TvApp data) {
                // TODO Auto-generated method stub
                if (data != null) {
                    mTvApp = data;
                    for (int i = 0; i < mTvApp.appList.size(); i++) {
//                        XGIMILOG.D("packageName = " + mTvApp.appList.get(i).packageName);
                        if (mTvApp.appList.get(i).packageName.equals("com.xgimi.wirelessscreen")
                                || mTvApp.appList.get(i).packageName.equals("com.waxrain.airplaydmr")) {
                            isWirelessScreen = true;
                        }
                    }
                    ToosUtil.getInstance().mInstallPacageNames.clear();
                    for (int i = 0; i < data.appList.size(); i++) {
                        ToosUtil.getInstance().mInstallPacageNames.add(data.appList.get(i).packageName);
//                        XGIMILOG.D(data.appList.get(i).packageName);
                        if ("com.xgimi.vcontrol".equals(data.appList.get(i))) {

                        }
                    }
                }
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFailed(String reason) {
            }
        });
    }
}
