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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.CancleCollect;
import com.xgimi.zhushou.bean.Collect;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.IsCollect;
import com.xgimi.zhushou.bean.MVList;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.popupwindow.MVPopupWindow;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GaoSi;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/19.
 */
public class MVDetailActivity extends BaseActivity implements View.OnClickListener{
    private String title,artist,bitmap,mv_play_address;
    private TextView mv_name;
    private TextView singer;
    private ImageView back;
    private RelativeLayout gaosi;
    private ImageView tupian;
    private ImageView liebiao;
    private MVPopupWindow menuWindow;
    public int page=0;
    public int psize=10;
    private String area_id;
    public String keywords;
    private int type;
    private ImageView mcollect;
    private boolean isCollect;
    private Subscription subscription;
    private Subscription subscription1;
    private Subscription subscription2;
    private App app;
    public int user_id;
    private GimiUser gimuser;
    private boolean isLogo;
    private int collect_id;
    public int mv_id;
    private ImageView daohang;
    private Boolean isplay=true;
    private ImageView iv_play;
    private String ivplaytype;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mv_detail);
        initExtra();
        initView();
        initData();
    }
    List< VcontrolCmd.CustomPlay.PlayList> mPlyLists=new ArrayList<>();
    public void onEventMainThread(MVList mvList) {
        mv_name.setText(SaveData.getInstance().mv_title);
        singer.setText(SaveData.getInstance().mv_artist);
        bitmap=SaveData.getInstance().bitmap;
        initData();
        mv_id=Integer.valueOf(SaveData.getInstance().mvid);
        setisCollectMV(user_id,mv_id);
        mPlyLists.clear();
        for (int i = 0; i < SaveData.getInstance().mList.size(); i++) {
            VcontrolCmd.CustomPlay.PlayList mPlayList=new VcontrolCmd.CustomPlay.PlayList(null,null,null,SaveData.getInstance().mList.get(i).mv_title,null,SaveData.getInstance().mList.get(i).mv_play_address,null);
            mPlyLists.add(mPlayList);
        }
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,"2",
                GMSdkCheck.appId,null,
                new VcontrolCmd.CustomPlay(0,0,null,mPlyLists, SaveData.getInstance().position),
                null,null,null)));
    }




    public void onEventMainThread(FeedbackInfo.PlayInfo playInfo) {

      if(playInfo.resourcetype.equals("custom")){
            SaveData.getInstance().bofang_type="MV";
            mv_name.setText(playInfo.playingname);
          SaveData.getInstance().mv_title=playInfo.playingname;
          title=playInfo.playingname;
            SaveData.getInstance().nextTitle=playInfo.playingname;
            for (int i = 0; i < SaveData.getInstance().mList.size(); i++) {
                if(playInfo.playingname.equals(SaveData.getInstance().mList.get(i).mv_title))
                {
                    SaveData.getInstance().position=i;
                    SaveData.getInstance().bitmap=SaveData.getInstance().mList.get(i).mv_thumb;
                    SaveData.getInstance().mv_artist=SaveData.getInstance().mList.get(i).mv_artist;
                    mv_id=Integer.valueOf(SaveData.getInstance().mList.get(i).mv_id);
                    ImageLoaderUtils.display(MVDetailActivity.this,tupian, NetUtil.IMAGEURL+SaveData.getInstance().mList.get(i).mv_thumb);
                    singer.setText(SaveData.getInstance().mList.get(i).mv_artist);
                }
            }
          isPlay=true;
          playchange();
        }
    }

    private void initExtra() {
        XGIMILOG.D("收藏id = " + SaveData.getInstance().collect_id);
        if(getIntent()!=null&&getIntent().getStringExtra("title")!=null){
            ToosUtil.getInstance().addEventUmeng(MVDetailActivity.this,"event_music_mv_play");
            title=getIntent().getStringExtra("title");
            artist=getIntent().getStringExtra("artist");
            bitmap=getIntent().getStringExtra("bitmap");
            mv_play_address=getIntent().getStringExtra("mv_play_address");
            area_id=getIntent().getStringExtra("areaid");
            collect_id= Integer.valueOf(getIntent().getStringExtra("collect_id"));
            mv_id= Integer.valueOf(getIntent().getStringExtra("mv_id"));
            type = (int) getIntent().getSerializableExtra("type");
            SaveData.getInstance().MV_type=type;
            ivplaytype=getIntent().getStringExtra("ivplaytype");
        }else if(SaveData.getInstance().mList!=null){
                title = SaveData.getInstance().mList.get(SaveData.getInstance().position).mv_title;
                artist = SaveData.getInstance().mList.get(SaveData.getInstance().position).mv_artist;
                bitmap = SaveData.getInstance().bitmap;
                mv_play_address = SaveData.getInstance().mList.get(SaveData.getInstance().position).mv_play_address;
                collect_id = Integer.valueOf(SaveData.getInstance().mList.get(SaveData.getInstance().position).collect_id);
                mv_id = Integer.valueOf(SaveData.getInstance().mList.get(SaveData.getInstance().position).mv_id);
                type = SaveData.getInstance().MV_type;
        }else {
            title = SaveData.getInstance().mv_title;
            artist =SaveData.getInstance().mv_artist;
            bitmap = SaveData.getInstance().bitmap;
            mv_play_address = SaveData.getInstance().mv_play_address;
            collect_id = Integer.valueOf(SaveData.getInstance().collect_id);
            mv_id = Integer.valueOf(SaveData.getInstance().mvid);
            type = SaveData.getInstance().MV_type;
        }
            app = (App) getApplicationContext();
            if(app.getLoginInfo()!=null){
                gimuser = app.getLoginInfo();
                isLogo=true;
                user_id= Integer.valueOf(gimuser.data.uid);
            }
    }
    private void initView() {
        EventBus.getDefault().register(this);
        mv_name= (TextView) findViewById(R.id.title);
        singer= (TextView) findViewById(R.id.singer);
        mv_name.setText(title);
        singer.setText(artist);
        tupian= (ImageView) findViewById(R.id.iv_mv);
        back= (ImageView) findViewById(R.id.back);
        gaosi = (RelativeLayout) findViewById(R.id.rl_gaosi);
        liebiao= (ImageView) findViewById(R.id.liebiao);
        mcollect= (ImageView) findViewById(R.id.collect);
         iv_play=(ImageView) findViewById(R.id.play);
        ImageView iv_next=(ImageView) findViewById(R.id.next);
        ImageView iv_before=(ImageView) findViewById(R.id.last_one);

        iv_play.setOnClickListener(this);
        iv_before.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        mcollect.setOnClickListener(this);
        setisCollectMV(user_id,mv_id);
        SaveData.getInstance().bofang_type="MV";
        daohang= (ImageView) findViewById(R.id.daohang);
        daohang.setOnClickListener(this);
        isPlay=SaveData.getInstance().isPlay;
        playchange();
    }
    private void playchange(){
        if(ivplaytype!=null&&ivplaytype.equals("true")){
            if(isPlay){
                iv_play.setImageResource(R.drawable.qt_zanting);
            }else {
                iv_play.setImageResource(R.drawable.qt_play);
            }

        }else {
            if(isPlay){
                iv_play.setImageResource(R.drawable.qt_zanting);
            }else {
                iv_play.setImageResource(R.drawable.qt_play);
            }

        }
        SaveData.getInstance().isPlay=isPlay;
    }
    private void change(int collect_id){
        if(collect_id>0){
            isCollect=true;
            mcollect.setImageResource(R.drawable.shoucanggequ);
        }else if(collect_id==0){
            isCollect=false;
            mcollect.setImageResource(R.drawable.shoucang_radio);
        }
    }
    private void initData() {
        ImageLoaderUtils.display(MVDetailActivity.this,tupian, NetUtil.IMAGEURL+bitmap);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(getApplicationContext()).load(NetUtil.IMAGEURL+bitmap).asBitmap().into(new SimpleTarget<Bitmap>() {
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
                menuWindow = new MVPopupWindow(MVDetailActivity.this,type);
                menuWindow.showAtLocation(MVDetailActivity.this.findViewById(R.id.liebiao), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                if(SaveData.getInstance().mv_title!=null){
                    menuWindow.songname.setText(SaveData.getInstance().mv_title);
                }else {
                    menuWindow.songname.setText(title);
                }
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
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
    private void setCollectMV(int user_id,int mv_id,String mv_title,String mv_artist,String mv_thumb,String mv_play_address) {
        subscription= Api.getMangoApi().getCollectMV(user_id,mv_id,mv_title,mv_artist,mv_thumb,mv_play_address).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    //MV收藏
    Observer<Collect> observer=new Observer<Collect>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Collect collect) {

            if(collect!=null){
                if(collect.code==200){
                    if(Integer.valueOf(collect.data.status)==1){
                        isCollect=true;
                        collect_id=Integer.valueOf(collect.data.collect_id);
                        mcollect.setImageResource(R.drawable.shoucanggequ);
                        Toast.makeText(MVDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                    }
            }
        }
    };
    //取消
    public void setCancelMV(int collect_id,int user_id) {
        subscription1= Api.getMangoApi().getCancleMV(collect_id,user_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }
    Observer<CancleCollect> observer1=new Observer<CancleCollect>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(CancleCollect collect) {
            if(collect!=null){
                if(collect.code==200) {
                    if (Integer.parseInt(collect.data.status)== 1) {
                        isCollect=false;
                        mcollect.setImageResource(R.drawable.shoucang_radio);
                        Toast.makeText(MVDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };
    //是否收藏
    private void setisCollectMV(int user_id,int mv_id) {
        subscription2= Api.getMangoApi().getisCollectMV(user_id,mv_id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer2);
    }
    Observer<IsCollect> observer2=new Observer<IsCollect>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(IsCollect iscollect) {
            if(iscollect!=null){
                if(Integer.valueOf(iscollect.data.collect_id)>0){
                    collect_id=Integer.valueOf(iscollect.data.collect_id);
                    change(collect_id);
                }else {
                    collect_id=Integer.valueOf(iscollect.data.collect_id);
                    change(collect_id);
                }
            }
        }
    };
    private void unRegist(Subscription sub){
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }

    }
//    //为弹出窗口实现监听类
//    private View.OnClickListener itemsOnClick = new View.OnClickListener(){
//
//        public void onClick(View v) {
//            menuWindow.dismiss();
//            switch (v.getId()) {
//                case R.id.liebiao:
//                    break;
//                default:
//                    break;
//            }
//
//        }
//
//    };

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        finish();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unRegist(subscription);
        unRegist(subscription1);
        unRegist(subscription2);
    }

    private boolean isPlay=true;
    private boolean isMv;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.collect:
                if(app.getLoginInfo()==null){
                    SaveData.getInstance().toLogo(MVDetailActivity.this);
                }else {
                    if(!isLogo){
                        return;
                    }
                    if(!isCollect){
                        setCollectMV(user_id,mv_id,title,artist,bitmap,mv_play_address);
                    }else{
                        setCancelMV(collect_id,user_id);
                    }
                }
                break;
            case R.id.daohang:
                if (Constant.netStatus) {
                    Intent intent = new Intent(this, RemountActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(this, NewSearchDeviceActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.play:
                if(!isPlay){
                    isPlay=true;
                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000,"2", GMSdkCheck.appId,null,
                            null,new VcontrolCmd.ControlCmd(10,2,0,null,null,null),null,null)));
                }else{
                    isPlay=false;
                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000,"2", GMSdkCheck.appId,null,
                            null,new VcontrolCmd.ControlCmd(10,3,0,null,null,null),null,null)));
                }
                playchange();
                break;
            case R.id.last_one:
                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000,"2", GMSdkCheck.appId,null,
                        null,new VcontrolCmd.ControlCmd(10,5,5,null,null,null),null,null)));
                break;
            case R.id.next:
                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000,"2", GMSdkCheck.appId,null,
                        null,new VcontrolCmd.ControlCmd(10,4,4,null,null,null),null,null)));
                break;

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(app.getLoginInfo()!=null){
            gimuser = app.getLoginInfo();
            isLogo=true;
            user_id= Integer.valueOf(gimuser.data.uid);
            setisCollectMV(user_id,mv_id);
        }
        if(Constant.netStatus){
            daohang.setImageResource(R.drawable.yaokongqi);
        }else{
            daohang.setImageResource(R.drawable.gimi_yaokong);

        }
    }
}
