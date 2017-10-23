package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ImageInfo;
import com.xgimi.zhushou.util.XGIMILOG;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by XGIMI on 2017/7/17.
 */

public class WallpaperActivity extends BaseActivity {

    public static final int GRIDE_PHOTO_WALL_CODE = 2;
    private VcontrolCmd.PhotoWall mPhotoWallBean;
    private int mCurrentTemplate;

    /**
     * 模板1
     */
    @Bind(R.id.fl_t1)
    FrameLayout mTemplate1Fl;

    @Bind(R.id.fl_t1_p1)
    FrameLayout mT1FlFl;

    @Bind(R.id.iv_photo_t1_p1)
    ImageView mT1P1Iv;

    @Bind(R.id.fl_t1_p2)
    FrameLayout mT1F2Fl;

    @Bind(R.id.iv_photo_t1_p2)
    ImageView mT1P2Iv;

//    @Bind(R.id.fl_t1_p3)
//    FrameLayout mT1F3Fl;

    @Bind(R.id.iv_photo_t1_p3)
    ImageView mT1P3Iv;

    @Bind(R.id.fl_t1_p4)
    FrameLayout mT1F4Fl;

    @Bind(R.id.iv_photo_t1_p4)
    ImageView mT1P4Iv;

    @Bind(R.id.fl_t1_p5)
    FrameLayout mT1F5Fl;

    @Bind(R.id.iv_photo_t1_p5)
    ImageView mT1P5Iv;

    @Bind(R.id.fl_t1_p6)
    FrameLayout mT1F6Fl;

    @Bind(R.id.iv_photo_t1_p6)
    ImageView mT1P6Iv;

    @Bind(R.id.fl_t1_p7)
    FrameLayout mT1F7Fl;

    @Bind(R.id.iv_photo_t1_p7)
    ImageView mT1P7Iv;

    @Bind(R.id.fl_t1_p8)
    FrameLayout mT1F8Fl;
    @Bind(R.id.iv_photo_t1_p8)
    ImageView mT1P8Iv;

    /**
     * 模板2
     */


    @Bind(R.id.fl_t2)
    FrameLayout mTemplate2Fl;

    @Bind(R.id.fl_t2_p1)
    FrameLayout mT2F1Fl;

    @Bind(R.id.iv_photo_t2_p1)
    ImageView mT2P1Iv;

    @Bind(R.id.fl_t2_p2)
    FrameLayout mT2F2Fl;

    @Bind(R.id.iv_photo_t2_p2)
    ImageView mT2P2Iv;

    @Bind(R.id.iv_t2_right_up)
    ImageView mT2RightUpIv;

    @Bind(R.id.fl_t2_p4)
    FrameLayout mT2F4Fl;

    @Bind(R.id.iv_photo_t2_p4)
    ImageView mT2P4Iv;

    @Bind(R.id.fl_t2_p5)
    FrameLayout mT2F5Fl;

    @Bind(R.id.iv_photo_t2_p5)
    ImageView mT2P5Iv;

    @Bind(R.id.fl_t2_p6)
    FrameLayout mT2F6Fl;

    @Bind(R.id.iv_photo_t2_p6)
    ImageView mT2P6Iv;


    /**
     * 模板3
     */
    @Bind(R.id.fl_t3)
    FrameLayout mTemplate3;

    @Bind(R.id.iv_t3_wall_photo)
    ImageView mT3Img;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        ButterKnife.bind(this);
        XGIMILOG.D("启动高清壁纸ACTIVITY");
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        initView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        XGIMILOG.D("onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    private void initView() {
        mT1FlFl.setOnClickListener(new OnChooseImgClick(1));
        mT1F2Fl.setOnClickListener(new OnChooseImgClick(2));
//        mT1P3Iv.setOnClickListener(new OnChooseImgClick(3));
        mT1F4Fl.setOnClickListener(new OnChooseImgClick(3));
        mT1F5Fl.setOnClickListener(new OnChooseImgClick(4));
        mT1F6Fl.setOnClickListener(new OnChooseImgClick(5));
        mT1F7Fl.setOnClickListener(new OnChooseImgClick(6));
        mT1F8Fl.setOnClickListener(new OnChooseImgClick(7));

        mT2F1Fl.setOnClickListener(new OnChooseImgClick(9));
        mT2F2Fl.setOnClickListener(new OnChooseImgClick(10));
        mT2F4Fl.setOnClickListener(new OnChooseImgClick(11));
        mT2F5Fl.setOnClickListener(new OnChooseImgClick(12));
        mT2F6Fl.setOnClickListener(new OnChooseImgClick(13));

        mTemplate3.setOnClickListener(new OnChooseImgClick(14));
    }

    @OnClick(R.id.btn_change_template_photo_wall)
    public void changeTemplate(View view) {
        if (mCurrentTemplate == 0) {
            mTemplate3.setVisibility(View.GONE);
            mTemplate1Fl.setVisibility(View.GONE);
            mTemplate2Fl.setVisibility(View.VISIBLE);
            mCurrentTemplate = 1;
        } else if (mCurrentTemplate == 1) {
            mTemplate3.setVisibility(View.VISIBLE);
            mTemplate1Fl.setVisibility(View.GONE);
            mTemplate2Fl.setVisibility(View.GONE);
            mCurrentTemplate = 2;
        } else {
            mTemplate3.setVisibility(View.GONE);
            mTemplate1Fl.setVisibility(View.VISIBLE);
            mTemplate2Fl.setVisibility(View.GONE);
            mCurrentTemplate = 0;
        }
    }


    class OnChooseImgClick implements View.OnClickListener {

        int clickPostion;

        public OnChooseImgClick(int clickPostion) {
            this.clickPostion = clickPostion;
        }

        @Override
        public void onClick(View v) {
            XGIMILOG.D(clickPostion+"");
            Intent intent = new Intent(WallpaperActivity.this, ImageGridViewActivityForPhotoWall.class);
            Bundle b = new Bundle();
            b.putInt("position", -1);
            intent.putExtras(b);
            startActivityForResult(intent, clickPostion);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        XGIMILOG.D("" + resultCode + ", " + requestCode);
        if (data != null) {
            String imgUrl = data.getStringExtra("imgHttpUrl");
            XGIMILOG.D(imgUrl);
            switch (requestCode) {
                case 1:
                    Glide.with(this).load(imgUrl).into(mT1P1Iv);
                    break;
                case 2:
                    Glide.with(this).load(imgUrl).into(mT1P2Iv);
                    break;
//                case 3:
//                    Glide.with(this).load(imgUrl).into(mT1P3Iv);
//                    break;
                case 3:
                    Glide.with(this).load(imgUrl).into(mT1P4Iv);
                    break;
                case 4:
                    Glide.with(this).load(imgUrl).into(mT1P5Iv);
                    break;
                case 5:
                    Glide.with(this).load(imgUrl).into(mT1P6Iv);
                    break;
                case 6:
                    Glide.with(this).load(imgUrl).into(mT1P7Iv);
                    break;
                case 7:
                    Glide.with(this).load(imgUrl).into(mT1P8Iv);
                    break;


                case 9:
                    Glide.with(this).load(imgUrl).into(mT2P1Iv);
                    break;
                case 10:
                    Glide.with(this).load(imgUrl).into(mT2P2Iv);
                    break;
                case 11:
                    Glide.with(this).load(imgUrl).into(mT2P4Iv);
                    break;
                case 12:
                    Glide.with(this).load(imgUrl).into(mT2P5Iv);
                    break;
                case 13:
                    Glide.with(this).load(imgUrl).into(mT2P6Iv);
                    break;

                case 14:
                    Glide.with(this).load(imgUrl).into(mT3Img);
                    break;
            }
            if (mPhotoWallBean == null) {
                mPhotoWallBean = new VcontrolCmd.PhotoWall();
            }
            if (mPhotoWallBean.imgInfo == null) {
                mPhotoWallBean.imgInfo = new ArrayList<>();
            }
//            mPhotoWallBean.imgInfo.add( new VcontrolCmd.PhotoWall.ImgInfoBean(requestCode, imgUrl));
            addImgInfo(new VcontrolCmd.PhotoWall.ImgInfoBean(requestCode, imgUrl));
        }
    }

    private void addImgInfo(VcontrolCmd.PhotoWall.ImgInfoBean info) {
        int containPostion = -1;
        for (int i = 0; i <  mPhotoWallBean.imgInfo.size(); i++) {
            if (mPhotoWallBean.imgInfo.get(i).getImgPosition() == info.getImgPosition()) {
                containPostion = i;
                break;
            }
        }
        if (containPostion > -1) {
            mPhotoWallBean.imgInfo.remove(containPostion);
            mPhotoWallBean.imgInfo.add(containPostion, info);
        } else {
            mPhotoWallBean.imgInfo.add(info);
        }

    }

    @OnClick(R.id.btn_push_template_photo_wall)
    public void push(View view) {
        if (mPhotoWallBean == null) {
            return;
        }
        mPhotoWallBean.template = mCurrentTemplate;
        int positionTotal = -1;
        if (mCurrentTemplate == 0) {
            positionTotal = 28;
            for (int i = 0; i < mPhotoWallBean.imgInfo.size(); i++) {
                int p = mPhotoWallBean.imgInfo.get(i).getImgPosition();
                if (p < 8) {
                    positionTotal -= p;
                }
            }

        } else if (mCurrentTemplate == 1) {
            positionTotal = 55;
            for (int i = 0; i < mPhotoWallBean.imgInfo.size(); i++) {
                int p = mPhotoWallBean.imgInfo.get(i).getImgPosition();
                if (p > 8 && p < 14) {
                    positionTotal -= p;
                }
            }
        } else {
            positionTotal = 14;
            for (int i = 0; i < mPhotoWallBean.imgInfo.size(); i++) {
                int p = mPhotoWallBean.imgInfo.get(i).getImgPosition();
                if (p == 14) {
                    positionTotal -= p;
                }
            }
        }
        if (positionTotal != 0) {
            Toast.makeText(this, "请先填满所有相框", Toast.LENGTH_SHORT).show();
            return;
        }
        XGIMILOG.D(new Gson().toJson(new VcontrolCmd(30400, mPhotoWallBean)));
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30400, mPhotoWallBean)));
//        mPhotoWallBean = new VcontrolCmd.PhotoWall();
    }

}
