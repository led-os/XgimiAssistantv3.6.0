package com.xgimi.zhushou.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.xgimi.zhushou.activity.IsNewServerActivity;
import com.xgimi.zhushou.activity.NewSearchDeviceActivity;
import com.xgimi.zhushou.bean.Individuality;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.netUtil.HttpRequest;
import com.xgimi.zhushou.widget.SignOutDilog;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by 霍长江 on 2016/8/11.
 */
public class ToosUtil {
    public static ToosUtil instance;
    public Subscription subscription;

    public static ToosUtil getInstance() {
        if (instance == null) {
            instance = new ToosUtil();

        }
        return instance;
    }

    private ToosUtil() {
        mPackageNames.add("net.myvst.v2");
        mPackageNames.add("com.youku.tv.ykew");
        mPackageNames.add("com.togic.livevideo");
        mPackageNames.add("com.starcor.mango");
        mPackageNames.add("com.sohuott.tv.vod");
        mPackageNames.add("com.moretv.android");
        mPackageNames.add("com.molitv.android");
        mPackageNames.add("com.ktcp.video");
        mPackageNames.add("com.elinkway.tvlive2");
        mPackageNames.add("cn.cibntv.ott");
        mPackageNames.add("cn.beevideo");
    }

    /**
     * 获取屏幕的宽和搞
     */
    public int getScreenWidth(Activity mcontext) {
        WindowManager wm;
        wm = mcontext.getWindowManager();
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕的宽和搞
     */
    public int getScreenHeight(Activity mcontext) {
        WindowManager wm;
        wm = mcontext.getWindowManager();
        return wm.getDefaultDisplay().getHeight();
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //判断是否是连接了设备
    //是否现在连接无屏电视
    public void isConnectTv(final Activity context) {
        SignOutDilog singDilog = new SignOutDilog(context, "是否现在连接无屏电视", null);
        singDilog.show();
        singDilog.setOnLisener(new SignOutDilog.onListern() {

            @Override
            public void send() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, NewSearchDeviceActivity.class);
                context.startActivity(intent);
            }
        });

    }

    public void isNeedHelp(final Activity context) {
        if (!SaveData.getInstance().isNewTv) {
            SignOutDilog singDilog = new SignOutDilog(context, "该功能需要安装无屏助手TV,去安装?", null);
            singDilog.show();
            singDilog.setOnLisener(new SignOutDilog.onListern() {

                @Override
                public void send() {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(context, IsNewServerActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }


    //统计u盟的自定义事件
    public void addEventUmeng(Context context, String event) {
        MobclickAgent.onEvent(context, event);
    }

    //投影上需要安装的apk的包名
    public List<String> mPackageNames = new ArrayList<>();
    //保存投影上已经安装的
    public List<String> mInstallPacageNames = new ArrayList<>();
    private boolean isInstallTv = false;

    public boolean isInstallTvControl(Activity context) {

        if (!SaveData.getInstance().isNewTv) {
            isNeedHelp(context);
        }
        return SaveData.getInstance().isNewTv;
    }

    //获取个性推荐
    public void getIndividualityVideo1(final CommonCallBack<List<Individuality.DataBean.DataBean1>> callback, Context context, String mac, int page, int pagesize) {
        HttpRequest.getInstance(context).getIndividuality(mac, page, pagesize, new CommonCallBack<Individuality>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(Individuality indivi) {
                if (indivi != null && indivi.data != null && indivi.data.data != null && indivi.data.data.size() > 0) {
                    callback.onSuccess(indivi.data.data);
                    SaveData.getInstance().saveIndivi = indivi;
                }
            }

            @Override
            public void onFailed(String reason) {
                callback.onFailed("服务器异常");
            }
        });
    }
    //用这个网络框架会有问题，但我也不晓得什么问题，所有改用HTTP
//    public void getIndividualityVideo(String mac,int page,int pagesize) {
//        subscription = Api.getMangoApi().getIndividuality(mac,page,pagesize)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//    }
//    Observer<Individuality> observer=new Observer<Individuality>() {
//        @Override
//        public void onCompleted() {
//            unRegist(subscription);
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            e.printStackTrace();
//        }
//
//        @Override
//        public void onNext(Individuality indivi) {
//            if(indivi!=null&&indivi.data!=null&&indivi.data.data!=null&&indivi.data.data.size()>0){
//                SaveData.getInstance().saveIndivi=indivi;
//                EventBus.getDefault().post(new GMDevice());
//            }
//        }
//    };
//    private void unRegist(Subscription sub) {
//        if (sub != null && !sub.isUnsubscribed()) {
//            sub.unsubscribe();
//        }
//    }
}
