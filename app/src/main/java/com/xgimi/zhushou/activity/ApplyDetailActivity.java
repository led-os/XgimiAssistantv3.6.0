package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.AppCollect;
import com.xgimi.zhushou.bean.ApplyDetail;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.InstallNum;
import com.xgimi.zhushou.bean.IsAppCollect;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.ViewPagerDilog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApplyDetailActivity extends BaseActivity implements View.OnTouchListener,View.OnClickListener {
    private HorizontalScrollView shoot;
    private TextView tv_information;
    private TextView yaokongqi;
    private TextView size;
    private TextView time;
    private String id;
    private Button anzhuang;
    private App app;
    public int user_id;
    private GimiUser gimuser;
    private boolean isLogo;
    private boolean isCollect;
    private Subscription subscription;
    private Subscription subscription1;
    private Subscription subscription2;
    private Subscription subscription3;
    private Subscription subscription4;
    private View net_connect;
    private ScrollView lin_scro;
    private RelativeLayout button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_detail);
        initExras();
        initView();
        initData();
        initLocal();

    }
    //读取缓存数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson(id);
        if (!StringUtils.isEmpty(readHomeJson)) {
            //dialog.dismiss();
            ApplyDetail data = new Gson().fromJson(readHomeJson, ApplyDetail.class);
            loadHome(data);
        } else {
            if (!HttpUrl.isNetworkConnected(ApplyDetailActivity.this)) {
                MissDilog();
                net_connect.setVisibility(View.VISIBLE);
                lin_scro.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(ApplyDetailActivity.this)) {
            getApplyDetail(id);
            getIsCollect(user_id,id);
        }
    }
    //加载数据
    private void loadHome(ApplyDetail channels) {
        loadData(channels);
        VcontrolCmd.CustomPlay.PlayList playList=new VcontrolCmd.CustomPlay.PlayList(
                ".apk",channels.data.package_name,channels.data.icon,channels.data.download_url,channels.data.title,channels.data.version_code);
        mPlays.add(playList);
        lin_scro.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
        MissDilog();
    }

    private void initExras(){
        Intent itent=getIntent();
        id = itent.getStringExtra("id");
        app = (App) getApplicationContext();
        if(app.getLoginInfo()!=null){
            gimuser = app.getLoginInfo();
            isLogo=true;
            user_id= Integer.valueOf(gimuser.data.uid);
        }
    }
    private void initView(){
        EventBus.getDefault().register(this);
        showDilog("加载中...");
        shoucang = (ImageView) findViewById(R.id.shoucang);
        shoucang.setOnClickListener(this);
        shoot = (HorizontalScrollView) findViewById(R.id.horscrollview);
        tv_information = (TextView) findViewById(R.id.tv_informaton);
        yaokongqi = (TextView) findViewById(R.id.tv_is_zhichi);
        size = (TextView) findViewById(R.id.tv_size_is);
        time = (TextView) findViewById(R.id.tv_update_is);
        anzhuang = (Button) findViewById(R.id.anzhuang);
        anzhuang.setOnTouchListener(this);
        controlTitle(findViewById(R.id.id_toolbar),true,true,false,false);
        TextView jietu= (TextView) findViewById(R.id.jietu).findViewById(R.id.tv_tishi);
        jietu.setText("应用截图");
        TextView jiben= (TextView) findViewById(R.id.jianjie).findViewById(R.id.tv_tishi);
        jiben.setText("基本信息");
        lin_scro= (ScrollView) findViewById(R.id.scrollview);
        button= (RelativeLayout) findViewById(R.id.button);
        net_connect = findViewById(R.id.netconnect);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HttpUrl.isNetworkConnected(ApplyDetailActivity.this)){
                    lin_scro.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    net_connect.setVisibility(View.GONE);
                    getApplyDetail(id);
                    getIsCollect(user_id,id);
                }
            }
        });
    }

    public void onEventMainThread(FeedbackInfo info) {
//        if(info.downloadInfo!=null) {
//            if ( detail.data.title.equals(info.downloadInfo.filename)) {
//                Message message = mhanHandler.obtainMessage();
//                message.obj = info;
//                message.what = 0;
//                mhanHandler.sendMessage(message);
//            }
//        }
        if(info.downloadInfo!=null) {
            if (detail.data.title.equals(info.downloadInfo.filename)) {
                Message message = mhanHandler.obtainMessage();
                message.obj = info;
                message.what = 0;
                mhanHandler.sendMessage(message);
            }
        }
        if(info.installInfo!=null) {
            if (detail.data.package_name.equals(info.installInfo.packagename)) {
                Message message = mhanHandler.obtainMessage();
                message.obj = info;
                message.what = 0;
                mhanHandler.sendMessage(message);
            }
        }

    }

    Handler mhanHandler=new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if(anzhuang!=null) {
                FeedbackInfo info = (FeedbackInfo) msg.obj;
                if(info.downloadInfo != null &&info.downloadInfo.progress==-2){
                    anzhuang.setText("排队中");
                }else
                if (info.downloadInfo != null&& info.downloadInfo.progress >0 && info.downloadInfo.progress < 99) {
                        anzhuang.setText("正在下载" + info.downloadInfo.progress + "%");
                } else if (info.downloadInfo != null && info.downloadInfo.progress == 99) {
                    anzhuang.setText("正在安装");
                } else {
                    if (info.installInfo != null) {
                        if (info.installInfo.stat == 1) {
                            anzhuang.setText("打开");
                        }
                    }
                }
            }
        }
    };
    private void initData(){
        anzhuang.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(id!= null&&app.getLoginInfo()!=null){
            gimuser = app.getLoginInfo();
            isLogo=true;
            user_id= Integer.valueOf(gimuser.data.uid);
            getIsCollect(user_id,id);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void loadData(ApplyDetail data){
        if(data==null){
            return;
        }
        detail=data;
        StringBuffer sb=new StringBuffer();

        for (int i = 0; i < data.data.handler.size(); i++) {
            sb.append(data.data.handler.get(i)+"  ");
        }
        yaokongqi.setText(sb.toString());
        size.setText(data.data.file_size);
        tv.setText(data.data.title);
        time.setText(data.data.update_time);
        tv_information.setText("                   " +data.data.description);
        addMovieShoot(data);
        if(SaveTVApp.getInstance(this).mTvApp!=null&&SaveTVApp.getInstance(this).mTvApp.appList!=null&&SaveTVApp.getInstance(this).mTvApp.appList.size()>0){
            for (int i = 0; i < SaveTVApp.getInstance(this).mTvApp.appList.size(); i++) {
                String appitem = SaveTVApp.getInstance(this).mTvApp.appList.get(i).packageName;
                if(appitem.equals(data.data.package_name)){
                    anzhuang.setText("打开");
                }
            }
        }
    }
    //电影截图的horscroolview添加view
    private void addMovieShoot(final ApplyDetail data){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        shoot.removeAllViews();
        for (int i = 0; i < data.data.screenshots.size(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    (int)(ToosUtil.getInstance().getScreenWidth(this)/1.6),(int) ((ToosUtil.getInstance().getScreenHeight(this)/4.5)));
            ImageView iv = new ImageView(this);
            final int mpos=i;
            if(i!=0){
                layoutParams.leftMargin = 40;
            }
            layoutParams.bottomMargin=20;
            ImageLoader.getInstance().displayImage(data.data.screenshots.get(i), iv);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setLayoutParams(layoutParams);
            linearLayout.addView(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    ViewPagerDilog vp=new ViewPagerDilog(ApplyDetailActivity.this, (ArrayList<String>)data.data.screenshots,mpos);
                    vp.show();
//					Intent intent=new Intent(ApplyDetaiActivity.this,PhonoViewPagerActivity.class);
////					intent.putExtra(name, value)
//					intent.putStringArrayListExtra("phone", (ArrayList<String>)data.data.screenshots);
//					startActivity(intent);
                }
            });
        }
        shoot.addView(linearLayout);
    }

    private ApplyDetail detail=new ApplyDetail();
    private ImageView shoucang;
    //获取应用详情页面
    public void getApplyDetail(String id){
        XGIMILOG.E("获取应用详情 : " + id);
        subscription = Api.getMangoApi().getApplyDetais(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
    Observer<ApplyDetail> observer = new Observer<ApplyDetail>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
            MissDilog();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            MissDilog();
        }

        @Override
        public void onNext(ApplyDetail channels) {
            XGIMILOG.E("获取应用详情成功 : " + new Gson().toJson(channels));
            if (channels != null && channels.data != null) {
                app= (App) ApplyDetailActivity.this.getApplicationContext();
                String savaHomeJson=new Gson().toJson(channels);
                app.savaHomeJson(id,savaHomeJson);
                loadHome(channels);
            }
        }
    };
    List<VcontrolCmd.CustomPlay.PlayList> mPlays=new ArrayList<>();


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.anzhuang:
                if(Constant.netStatus){
                    if(ToosUtil.getInstance().isInstallTvControl(this) ) {

                        if (detail.data != null) {
                            if (anzhuang.getText().toString().trim().equals("打开")) {
//                            GMDeviceController.getInstance().senddOpen(detail.data.package_name);
                                //打开应用
                                ToosUtil.getInstance().addEventUmeng(ApplyDetailActivity.this, "event_app_open");
                                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                        null, new VcontrolCmd.ControlCmd(7, 1, 0, detail.data.package_name, null, null), null, null)));
                            } else if (anzhuang.getText().toString().trim().equals("远程安装")) {
                                ToosUtil.getInstance().addEventUmeng(ApplyDetailActivity.this, "event_app_install");
//                            GMDeviceController.getInstance().SendJC(sendJson(detail.data.download_url, detail.data.title+".apk",detail.data.package_name,detail.data.icon));
                                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                                        "2", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(3, 0, null, mPlays, 0), null, null, null)));
                                addAppPlays(id);
                            }
                        }
                    }
                }else{
                    ToosUtil.getInstance().isConnectTv(ApplyDetailActivity.this);
                }
                break;
            case R.id.shoucang:
                if(app.getLoginInfo()==null){
                    SaveData.getInstance().toLogo(ApplyDetailActivity.this);
                }else {
                    if(!isLogo){
                        return;
                    }
                    if(!isCollect){
                        getAppCollect(user_id,id);
                    }else{
                        getCancleAppCollect(user_id,id);
                    }
                }
                break;
            default:
                break;
        }
    }
    //发送json数据给投影一
    public String sendJson(String url,String title,String packageName,String iconUrl){
        JSONObject jsonObject=new JSONObject();
        JSONObject js2=new JSONObject();
        try {
            jsonObject.put("title",title);
            jsonObject.put("url", url);
            jsonObject.put("packageName", packageName);
            jsonObject.put("iconUrl", iconUrl);
            js2.put("data", jsonObject);
            js2.put("action", 1);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(js2.toString());
        return js2.toString();
    }
//@Override
//    public void musicInfor(String arg0) {
//        // TODO Auto-generated method stub
////		if(arg0.startsWith("APP")){
////			anzhuang.setText("打开");
////		}else{
//        JSONObject josnJsonObject;
//        try {
//            josnJsonObject = new JSONObject(arg0);
//            int action=josnJsonObject.getInt("action");
//            if(action==200){
//                anzhuang.setText("正在下载");
//            }else if(action==201){
//                anzhuang.setText("开始安装");
//                handler.sendEmptyMessageDelayed(1, 600);
//            }
//            else if(action==202){
//            }
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
////		}
//        }
//    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.anzhuang:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        anzhuang.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        anzhuang.setAlpha(1.0f);
                        break;
                }
                break;
        }
        return false;
    }
private void unRegist(Subscription sub){
        if (sub != null && !sub.isUnsubscribed()) {
        sub.unsubscribe();}
        }
    //检测是否收藏
    private void getIsCollect(int user_id,String app_id) {
        subscription1 = Api.getMangoApi().getAppcheckCollect(user_id,app_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }
    Observer<IsAppCollect> observer1 =new Observer<IsAppCollect>() {
        @Override
        public void onCompleted() {
            unRegist(subscription1);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(IsAppCollect isAppCollect) {
            if(isAppCollect!=null&&isAppCollect.data!=null){
               if(isAppCollect.data.equals("true")){
                  isCollect=true;
                  shoucang.setImageResource(R.drawable.shoucanggequ);
               }else {
                   isCollect=false;
                   shoucang.setImageResource(R.drawable.shoucang_radio);
               }
                lin_scro.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                net_connect.setVisibility(View.GONE);
                MissDilog();
            }
        }
    };
    //添加收藏
    private void getAppCollect(int user_id,String app_id) {
        subscription2 = Api.getMangoApi().getAppCollect(user_id,app_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);
    }
    Observer<AppCollect> observer2=new Observer<AppCollect>() {
        @Override
        public void onCompleted() {
            unRegist(subscription2);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(AppCollect appCollect) {
            if(appCollect!=null&&appCollect.data!=null){
                if(appCollect.code==200&&appCollect.data.equals("success")){
                    isCollect=true;
                    shoucang.setImageResource(R.drawable.shoucanggequ);
                    Toast.makeText(ApplyDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    //取消收藏
    private void getCancleAppCollect(int user_id,String app_id) {
        subscription3 = Api.getMangoApi().getCancleAppCollect(user_id,app_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer3);
    }
    Observer<AppCollect> observer3 =new Observer<AppCollect>() {
        @Override
        public void onCompleted() {
            unRegist(subscription3);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(AppCollect appCollect) {
            if(appCollect!=null&&appCollect.data!=null){
                if(appCollect.code==200&&appCollect.data.equals("success")){
                    isCollect=false;
                    shoucang.setImageResource(R.drawable.shoucang_radio);
                    Toast.makeText(ApplyDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    //添加app安装次数
    private void addAppPlays(String app_id) {
        subscription3 = Api.getMangoApi().addAppPlays(app_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer4);
    }
    Observer<InstallNum> observer4=new Observer<InstallNum>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(InstallNum installNum) {

        }
    };
//    @Override
//    public void receive(FeedbackInfo info) {
//        if (anzhuang != null) {
//            if (info.downloadInfo != null && info.downloadInfo.progress < 99) {
//                anzhuang.setText("正在下载" + info.downloadInfo.progress + "%");
//            } else if (info.downloadInfo != null && info.downloadInfo.progress == 99) {
//                anzhuang.setText("正在安装");
//            } else {
//                if (info.installInfo != null) {
//                    if (info.installInfo.stat == 1) {
//                        anzhuang.setText("打开");
//                    }
//                }
//            }
//        }
//    }
}
