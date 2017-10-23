package com.xgimi.zhushou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.VideoInfo;
import com.xgimi.zhushou.netUtil.HttpServer;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToastUtil;
import com.xgimi.zhushou.util.XGIMILOG;


import java.util.ArrayList;

/**
 * Created by XGIMI on 2017/8/30.
 */

public class NewVideoPlayActivity extends Activity {

    //////////////// UI  ///////////////////////
    private VideoView mVideoView;

    private RelativeLayout mTitleRl;
    private FrameLayout mBackBtnFl;
    private TextView mVideoTitleTv;


    private LinearLayout mVideoControlLayout;
    private FrameLayout mPlayBtnFl;
    private ImageView mPlayIv;
    private FrameLayout mPlayNextBtnFl;
    private ImageView mPlayNextIv;
    private TextView mCurrentTimeTv;
    private TextView mTotleTimeTv;
    private SeekBar mSeekBar;
    private FrameLayout mPlayOnTvBtnFl;
    private ImageView mPlayOnTvIv;
    private ImageView mTipPlayingOnTvIv;
    private boolean mIsVideoReady;


    ////////////////////// data  ////////////////////////
    private int mPlayIndex;
    private VideoInfo mVideoInfo;

    private Context mContext;


    /////////////////  Flags  /////////////
    private boolean mIsAnimationRunnig;
    private boolean mIsPlayingOnTv;
    private boolean mIsActivityDestroyed = false;

    ///////////////////  Animation  /////////////////
    private TranslateAnimation mShowTitleAnimation;
    private TranslateAnimation mHideTitleAnimation;
    private TranslateAnimation mShowControlLayoutAnimation;
    private TranslateAnimation mHideControlLayoutAnimation;



    ////////////////////////  handler  ///////////////////////
    private Handler mHideControlLayout = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVideoControlLayout.setVisibility(View.GONE);
        }
    };
    private Handler mFreshSeekBarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mIsVideoReady && !mIsActivityDestroyed) {
                mCurrentTimeTv.setText(StringUtils.formatTime(mVideoView.getCurrentPosition()));
                mSeekBar.setProgress(mVideoView.getCurrentPosition());
                mFreshSeekBarHandler.sendEmptyMessageDelayed(0, 500);
            }
        }
    };

    /////////////////////   VideoLiateners   ///////////////////////
    private MediaPlayer.OnCompletionListener mOnComplateListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            XGIMILOG.E("");
            mIsVideoReady = false;
            playNextVideo();
        }
    };

    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            XGIMILOG.E("what = " + what + ", extra = " + extra);
            mIsVideoReady = false;
            return false;
        }
    };

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mVideoView.start();
            mIsVideoReady = true;
            mFreshSeekBarHandler.removeMessages(0);
            mFreshSeekBarHandler.sendEmptyMessage(0);
        }
    };

//    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
//        @Override
//        public void onSeekComplete(MediaPlayer mp) {
//            XGIMILOG.E("");
//            mFreshSeekBarHandler.removeMessages(0);
//            mFreshSeekBarHandler.sendEmptyMessage(0);
//        }
//    };

    ///////////////////////    Button Listeners    ////////////////////
    private View.OnClickListener mOnPlayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mIsPlayingOnTv) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    Glide.with(mContext).load(R.drawable.btn_video_play).into(mPlayIv);
                } else {
                    mVideoView.start();
                    Glide.with(mContext).load(R.drawable.btn_video_pause).into(mPlayIv);
                }
            }
        }
    };

    private View.OnClickListener mOnPlayNextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mIsPlayingOnTv) {
                playNextVideo();
            }

        }
    };


    private View.OnClickListener mOnPlayOnTvClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mIsPlayingOnTv) {
                playOnTv();
            } else {
                XGIMILOG.E("");
            }
        }
    };

    private View.OnTouchListener mOnVideoTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !mIsAnimationRunnig && !mIsPlayingOnTv) {
                XGIMILOG.E("");
                if (mVideoControlLayout.getVisibility() == View.VISIBLE) {
                    mVideoControlLayout.startAnimation(mHideControlLayoutAnimation);
                    mTitleRl.startAnimation(mHideTitleAnimation);
                } else {
                    mVideoControlLayout.startAnimation(mShowControlLayoutAnimation);
                    mTitleRl.startAnimation(mShowTitleAnimation);
                }
            }
            return false;
        }
    };

    private View.OnClickListener mOnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFreshSeekBarHandler.removeMessages(0);
            mVideoView.stopPlayback();
            finish();
        }
    };



    //////////////////// SeekBar Listener /////////////////
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            if (fromUser) {
//                XGIMILOG.E("" + progress);
//            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            XGIMILOG.E("");
            if (!mIsPlayingOnTv) {
                mFreshSeekBarHandler.removeMessages(0);
            }

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            XGIMILOG.E("");
            if (!mIsPlayingOnTv) {
                mVideoView.seekTo(seekBar.getProgress());
                mFreshSeekBarHandler.removeMessages(0);
                mFreshSeekBarHandler.sendEmptyMessage(0);
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_activity);
        initData();
        initAnimation();
        initView();
        initVideo();
    }

    private void initAnimation() {
        mShowTitleAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mHideTitleAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f);
        mShowControlLayoutAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        mHideControlLayoutAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);

        mShowTitleAnimation.setDuration(200);
        mHideTitleAnimation.setDuration(200);
        mShowControlLayoutAnimation.setDuration(200);
        mHideControlLayoutAnimation.setDuration(200);

        mShowTitleAnimation.setFillAfter(true);
        mHideTitleAnimation.setFillAfter(true);
        mShowControlLayoutAnimation.setFillAfter(true);
        mHideControlLayoutAnimation.setFillAfter(true);

        mShowTitleAnimation.setAnimationListener(new MyAnimationListener(1));
        mHideTitleAnimation.setAnimationListener(new MyAnimationListener(2));
        mShowControlLayoutAnimation.setAnimationListener(new MyAnimationListener(1));
        mHideControlLayoutAnimation.setAnimationListener(new MyAnimationListener(2));
    }

    private void initVideo() {
        mVideoView.setVideoPath(mVideoInfo.filePath);
        mVideoView.setOnCompletionListener(mOnComplateListener);
        mVideoView.setOnPreparedListener(mOnPreparedListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
        mVideoView.setOnTouchListener(mOnVideoTouchListener);
    }

    @Override
    protected void onDestroy() {
        mIsActivityDestroyed = true;
        mFreshSeekBarHandler.removeMessages(0);
        super.onDestroy();
    }

    private void initData() {
        mContext = this;
        Intent intent = getIntent();
        if (intent != null) {
            mPlayIndex = intent.getExtras().getInt("position");
        }
        if (SaveData.getInstance().myVideoInfo != null && SaveData.getInstance().myVideoInfo.get(mPlayIndex) != null) {
            mVideoInfo = SaveData.getInstance().myVideoInfo.get(mPlayIndex);
        }
    }

    private void initView() {
        mVideoView = (VideoView) findViewById(R.id.vv_video_play);

        mTitleRl = (RelativeLayout) findViewById(R.id.rl_title_video_play);
        mBackBtnFl = (FrameLayout) findViewById(R.id.fl_btn_back_video_play);
        mVideoTitleTv = (TextView) findViewById(R.id.tv_title_video_play);

        mVideoControlLayout = (LinearLayout) findViewById(R.id.ll_video_control_video_play);
        mPlayBtnFl = (FrameLayout) findViewById(R.id.fl_btn_play_video_play);
        mPlayIv = (ImageView) findViewById(R.id.iv_play_video_play);
        mPlayNextBtnFl = (FrameLayout) findViewById(R.id.fl_btn_next_video_play);
        mPlayNextIv = (ImageView) findViewById(R.id.iv_play_next_video_play);
        mCurrentTimeTv = (TextView) findViewById(R.id.tv_current_time_video_play);
        mTotleTimeTv = (TextView) findViewById(R.id.tv_total_time_video_play);
        mSeekBar = (SeekBar) findViewById(R.id.sb_video_play);
        mPlayOnTvBtnFl = (FrameLayout) findViewById(R.id.fl_btn_play_on_tv_video_play);
        mPlayOnTvIv = (ImageView) findViewById(R.id.iv_play_on_tv_video_play);
        mTipPlayingOnTvIv = (ImageView) findViewById(R.id.iv_playing_on_tv_video_play);

        mVideoTitleTv.setText(mVideoInfo.title);
        mTotleTimeTv.setText(StringUtils.formatTime(mVideoInfo.duration));
        mSeekBar.setMax((int) ((long) mVideoInfo.duration));
        mPlayBtnFl.setOnClickListener(mOnPlayClickListener);
        mPlayNextBtnFl.setOnClickListener(mOnPlayNextClickListener);
        mPlayOnTvBtnFl.setOnClickListener(mOnPlayOnTvClickListener);



        mBackBtnFl.setOnClickListener(mOnBackClickListener);
        mSeekBar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
    }

    private void playNextVideo() {
        if (mPlayIndex == SaveData.getInstance().myVideoInfo.size() - 1) {
            ToastUtil.getToast("已经是最后一集", mContext).show();
        } else {
            mPlayIndex++;
            Glide.with(mContext).load(R.drawable.btn_video_pause).into(mPlayIv);
            mVideoInfo = SaveData.getInstance().myVideoInfo.get(mPlayIndex);
            mVideoTitleTv.setText(mVideoInfo.title);
            mTotleTimeTv.setText(StringUtils.formatTime(mVideoInfo.duration));
            mCurrentTimeTv.setText("0:00:00");
            mSeekBar.setMax((int) ((long) mVideoInfo.duration));
            mVideoView.setVideoPath(mVideoInfo.filePath);
            mVideoView.start();
        }
    }

    public void playOnTv() {
        if (Constant.netStatus) {
            mIsPlayingOnTv = true;
            mSeekBar.setEnabled(false);
            mFreshSeekBarHandler.removeMessages(0);
            mVideoView.stopPlayback();
            mTipPlayingOnTvIv.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            String id = GlobalConsts.VIDEO_PREFIX + String.valueOf(mVideoInfo.id);
            String title = mVideoInfo.title;
            String sendCommand = "http://" + App.getContext().getPhoneIp() + ":" + HttpServer.PORT + "/" + id + "#" + title;
            ArrayList<VcontrolCmd.CustomPlay.PlayList> playLists = new ArrayList<>();
            playLists.add(new VcontrolCmd.CustomPlay.PlayList(null
                    , null, null, null, null, sendCommand, null));
//            SendCommondUtil.getIntentce().sendCommond(new Gson().toJson(new VcontrolCmd(30200, "2", null, null,
//                    new VcontrolCmd.CustomPlay(0, 0, null, playLists, 0),
//                    null, null, null)));
            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,"2", GMSdkCheck.appId,null,
                    new VcontrolCmd.CustomPlay(0,0,null,playLists,0),
                    null,null,null)));
        } else {
            ToastUtil.getToast("请先连接设备", mContext).show();
        }
    }

    private class MyAnimationListener implements Animation.AnimationListener {

        private int type;

        public MyAnimationListener(int type) {
            this.type = type;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            mIsAnimationRunnig = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mIsAnimationRunnig = false;
            if (type == 1) {
                mTitleRl.setVisibility(View.VISIBLE);
                mVideoControlLayout.setVisibility(View.VISIBLE);
            } else {
                mTitleRl.setVisibility(View.GONE);
                mVideoControlLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
