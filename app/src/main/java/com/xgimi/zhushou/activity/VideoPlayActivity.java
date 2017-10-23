package com.xgimi.zhushou.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.xgimi.device.device.GMDeviceController;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.socket.UdpManager;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.netUtil.HttpServer;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.vov.vitamio.Vitamio;

public class VideoPlayActivity extends Activity implements OnClickListener, OnSeekBarChangeListener, io.vov.vitamio.MediaPlayer.OnCompletionListener, io.vov.vitamio.MediaPlayer.OnPreparedListener,
		OnLongClickListener {

	private static final int SHOW_PROGRESS = 2;

	private App app;
	// video message
	private Map<String, Object> currVideo = new HashMap<String, Object>();
	private String playPath;
	private String path;
	private String videoName;
	private long duration;

	private long Currentpos = 0;

	private int position; // 视频的列表位置

	private VideoControlHolder controlHolder; // 控件

	private VideoReceiver videoReceiver; // 接收服务端消息之后的广播


	// 播放状态
	private boolean isRemoteMode = false;
	private boolean isRemotePlaying = false;
	private boolean isHangup = false;

	private Animation showVoicePanelAnimation;
	private Animation hiddenVoicePanelAnimation;

	private GestureDetector mGestureDetector;
	private AudioManager mAudioManager;

	/** 最大声音 */
	private int mMaxVolume;
	/** 当前声音 */
	private int mVolume = -1;
	/** 当前亮度 */
	private float mBrightness = -1f;

	private boolean isControlState = false; // 服务端调节音量显示标识

	public static final String VIDEOACTION = "com.xgimi.broadcast.video";

	// 用于控制屏幕的亮度
	private PowerManager.WakeLock wakelock;

	// 标记长按
	private boolean volLongpress = false;
	private String long_command; // 长按的命令拷贝
	private static final int VOLUMEHANDLER = 199; // 长按消息
	private ArrayList<VcontrolCmd.CustomPlay.PlayList> playLists;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		app = (App) getApplicationContext();
		Vitamio.isInitialized(this);

		setContentView(R.layout.local_videoplayer);

		position = getIntent().getExtras().getInt("position");

		controlHolder = new VideoControlHolder(this);
		mGestureDetector = new GestureDetector(this, new MyGestureListener());

		controlHolder.videobar.setMax(1000);

		controlHolder.setClickListener(this, this);

		controlHolder.videoView.setOnCompletionListener(this);
		controlHolder.videoView.setOnPreparedListener(this);
		controlHolder.videobar.setOnSeekBarChangeListener(this);

		// 开始播放
		localORremote();

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		showVoicePanelAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
		hiddenVoicePanelAnimation = AnimationUtils.loadAnimation(this, R.anim.push_up_out);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, this.getClass().getCanonicalName());
		//影藏导航栏，但会出问题
//		View decorView = getWindow().getDecorView();
//// Hide both the navigation bar and the status bar.
//// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
//// a general rule, you should design your app to hide the status bar whenever you
//// hide the navigation bar.
//		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//				| View.SYSTEM_UI_FLAG_FULLSCREEN;
//		decorView.setSystemUiVisibility(uiOptions);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (!isRemoteMode) {
			PauseToPlay();
		}
		isHangup = false;

	}

	@Override
	protected void onResume() {
		super.onResume();

		registerReceiver();
		wakelock.acquire();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(videoReceiver);
		if (!isRemoteMode) {
			stopPlay();
		}
		wakelock.release();
		isHangup = true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GMDeviceController.getInstance().SendJC("MEDIACONTROL:" + GlobalConsts.MEDIA_RENDER_CTL_MSG_STOP);

	}

	/**
	 * 通过position获取视频信息来播放
	 */
	private void playVideo() {

		Currentpos = 0;

		controlHolder.videoView.setVideoPath(playPath);

		if (!controlHolder.videoView.isPlaying()) {
			controlHolder.videoplay.setBackgroundResource(R.drawable.btn_video_play);
		}

		mHandler.removeMessages(SHOW_PROGRESS);
		mHandler.sendEmptyMessage(SHOW_PROGRESS);

	}

	/**
	 * 下一首
	 */
	private void nextVideo() {
		if (position == SaveData.getInstance().myVideoInfo.size() - 1) {
			Toast.makeText(VideoPlayActivity.this, "当前已为最后一部", Toast.LENGTH_SHORT).show();
			return;
		} else {
			position++;
		}

		localORremote();

	}

	/**
	 * 上一首
	 */
	private void beforeVideo() {
		if (position == 0) {
			Toast.makeText(VideoPlayActivity.this, "当前已为第一部", Toast.LENGTH_SHORT).show();
			return;
		} else {
			position--;
		}

		localORremote();
	}

	private void localORremote() {
		if(SaveData.getInstance().myVideoInfo!=null){
			playPath = SaveData.getInstance().myVideoInfo.get(position).filePath;
			videoName = SaveData.getInstance().myVideoInfo.get(position).title;
			duration = SaveData.getInstance().myVideoInfo.get(position).duration;
		}

		controlHolder.videofinal.setText(StringUtils.formatTime(duration));
		controlHolder.videotitle.setText(videoName);
		controlHolder.videobar.setProgress(0);
		controlHolder.videocurrent.setText("00:00:00");

		if (isRemoteMode) {
			onSendBtn();
		} else {
			playVideo();
		}
	}

	/**
	 * 停止播放
	 */
	private void stopPlay() {

		if (controlHolder.videoView == null) {
			return;
		}

		mHandler.removeMessages(SHOW_PROGRESS);
		if (controlHolder.videoView.isPlaying()) {
			controlHolder.videoView.stopPlayback();
		}
	}

	/**
	 * 暂停播放
	 */
	private void PausePlay() {

		mHandler.removeMessages(SHOW_PROGRESS);

		if (controlHolder.videoView.isPlaying()) {
			controlHolder.videoView.pause();
		}
	}

	/**
	 * 暂停之后回复播放，解决后台无法恢复的问题(应该是调用不对，有时间再看)
	 */
	private void PauseToPlay() {

		if (controlHolder == null) {
			return;
		}

		if (!controlHolder.videoView.isPlaying()) {

			mHandler.removeMessages(SHOW_PROGRESS);
			mHandler.sendEmptyMessage(SHOW_PROGRESS);

			controlHolder.videoView.start();
			controlHolder.videoView.seekTo(Currentpos);

		}
	}

	/*
	 * 注册接收服务端的广播
	 */
	private void registerReceiver() {
		// 创建广播接收器
		videoReceiver = new VideoReceiver();
		// 创建IntentFilter
		IntentFilter filter = new IntentFilter();
		// 指定BroadcastReceiver监听的Action
		filter.addAction(VIDEOACTION);
		// 注册BroadcastReceiver

		registerReceiver(videoReceiver, filter);

	}

	/**
	 * 处理进度条和音量加减的长按
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case SHOW_PROGRESS:

				if (isRemoteMode) {
					return;
				}

				setProgress();

				msg = obtainMessage(SHOW_PROGRESS);

				sendMessageDelayed(msg, 1000 - (Currentpos % 1000));

				break;

			case VOLUMEHANDLER:
				Send(long_command);
				if (volLongpress) {
					msg = obtainMessage(VOLUMEHANDLER);
					sendMessageDelayed(msg, 100);
				}
				break;
			}
		}
	};

	/**
	 * 设置进度条
	 */
	private void setProgress() {
		if (duration == 0) {
			return;
		}
		Currentpos = controlHolder.videoView.getCurrentPosition();

		long pos = 1000L * Currentpos / duration;

		controlHolder.videobar.setProgress((int) pos);
		controlHolder.videocurrent.setText(StringUtils.formatTime(Currentpos));

	}

	/**
	 * 给服务器发送视频进度(第一版无效)
	 * 
	 */
	public void audioTrackChange(long seektime) {

		String sendCommand = "MEDIASEEK:" + seektime;
		GMDeviceController.getInstance().SendJC(sendCommand);

	}

	// 远程播放
	public void onSendBtn() {

//		String id = GlobalConsts.VIDEO_PREFIX + GlobalConsts.videoList.get(position).get("id");
		String id = GlobalConsts.VIDEO_PREFIX +String.valueOf(SaveData.getInstance().myVideoInfo.get(position).id);
//		String title = (String) GlobalConsts.videoList.get(position).get("title");
		String title =SaveData.getInstance().myVideoInfo.get(position).title;
//		controlHolder.videovoice.setVisibility(View.VISIBLE);

		isRemoteMode = true;

		stopPlay();

		controlHolder.layout_send_tip.setVisibility(View.VISIBLE);

//		controlHolder.videosend.setBackgroundResource(R.drawable.btn_video_send_ing);

		String sendCommand = "http://" + App.mPhoneIP + ":" + HttpServer.PORT + "/" + id + UdpManager.separator + title;
		playLists = new ArrayList<>();
		playLists.add(new VcontrolCmd.CustomPlay.PlayList(null
				,null,null,null,null,sendCommand,null));
//		GMDeviceController.getInstance().SendJC(sendCommand);
		VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,"2", GMSdkCheck.appId,null,
				new VcontrolCmd.CustomPlay(0,0,null,playLists,0),
		null,null,null)));

		controlHolder.rootlayout.removeView(controlHolder.videoView);
	}

	/**
	 * 发送命令给服务端,音量加减
	 * 
	 * @param
	 */
	private void Send(final String command) {
		if (!StringUtils.isEmpty(app.ConnectedIP)) {
			UdpManager.getInstance().sendCCommand(command);
		}
	}


	/**
	 * 广播接收，用于接收服务端发送过来的信息
	 * 
	 *
	 */
	public class VideoReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			// 判断是否为远程播放
			if (!isRemoteMode) {
				return;
			}

			String action = intent.getAction();

			if (action.equals(VIDEOACTION)) {

				String message = intent.getStringExtra("event");

				if (message.contains("pos")) {
					if (duration == 0) {
						return;
					}
					isRemotePlaying = true;

					String[] substr = message.split("video:");

					long RemoteCurrentmill = StringUtils.formatTime_turn(substr[1]);
					controlHolder.videocurrent.setText(substr[1]);

					long pos = 1000L * RemoteCurrentmill / duration;

					controlHolder.videobar.setProgress((int) pos);

				} else if (message.contains("state")) {

					String substr[] = message.split(":");

					if (substr[2].equals("STOPPED")) {

						// 服务端发送结束命令
						stopPlay();

						finish();

					} else if (substr[2].equals("PLAYING")) {

						isRemotePlaying = true;

					} else if (substr[2].equals("PAUSED_PLAYBACK")) {

						// 服务端发送暂停命令

						isRemotePlaying = false;
					}
				}
			}
		}

	}

	@Override
	public void onClick(View v) {

		volLongpress = false;

		switch (v.getId()) {
		case R.id.next_video:
			nextVideo();
			break;
//		case R.id.previous_video:
//			beforeVideo();
//			break;
		case R.id.play_video:

			if (isRemoteMode) {
				if (isRemotePlaying) {
					controlHolder.videoplay.setBackgroundResource(R.drawable.btn_video_pause);
					GMDeviceController.getInstance().SendJC("MEDIACONTROL:" + GlobalConsts.MEDIA_RENDER_CTL_MSG_PAUSE);
				} else {
					controlHolder.videoplay.setBackgroundResource(R.drawable.btn_video_play);
					GMDeviceController.getInstance().SendJC("MEDIACONTROL:" + GlobalConsts.MEDIA_RENDER_CTL_MSG_PLAY);
				}
			} else {
				if (controlHolder.videoView.isPlaying()) {
					controlHolder.videoView.pause();
					controlHolder.videoplay.setBackgroundResource(R.drawable.btn_video_pause);
				} else {
					controlHolder.videoView.start();
					controlHolder.videoplay.setBackgroundResource(R.drawable.btn_video_play);
				}
			}

			break;
		case R.id.play_send:

			if (isRemoteMode) {
				isRemoteMode = false;
				UdpManager.getInstance().sendJCommand("MEDIACONTROL:" + GlobalConsts.MEDIA_RENDER_CTL_MSG_STOP);
				finish();
			} else {
				if (Constant.netStatus) {
					onSendBtn();
				} else {
					Toast.makeText(VideoPlayActivity.this, "设备未连接", Toast.LENGTH_SHORT).show();
				}
			}

			break;
		case R.id.videoBack:

			finish();
			break;
		case R.id.video_voice:

			if (isRemoteMode) {
				voicePanelAnimation();
			}
			break;

		case R.id.video_volume_add:
			Send(Constant.COM_IncreaseVolume);

			break;
		case R.id.video_volume_decreast:
			Send(Constant.COM_DecreaseVolume);
			break;

		}
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.video_volume_add:
			long_command = Constant.COM_IncreaseVolume;
			break;
		case R.id.video_volume_decreast:
			long_command = Constant.COM_DecreaseVolume;
			break;
		}
		volLongpress = true;
		mHandler.sendEmptyMessage(VOLUMEHANDLER);
		return false;
	}

	@Override
	public void onCompletion(io.vov.vitamio.MediaPlayer mp) {
		nextVideo(); // 播放完成自动播放下一个
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

		long newposition = (duration * progress) / 1000;

		String time = StringUtils.formatTime(newposition);

		if (isRemoteMode) {
			if (fromUser) {

				audioTrackChange(newposition);
			}
		} else {
			if (fromUser) {

				controlHolder.videoView.pause();
				controlHolder.videoView.seekTo(newposition);
			}
		}

		controlHolder.videocurrent.setText(time);

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

		if (!controlHolder.videoView.isPlaying()) {
			controlHolder.videoplay.setBackgroundResource(R.drawable.btn_video_play);
		}

		controlHolder.videoView.start();

	}

	/*
	 * 
	 * Licensed under the Apache License, Version 2.0 (the "License"); you may
	 * not use this file except in compliance with the License. You may obtain a
	 * copy of the License at
	 * 
	 * http://www.apache.org/licenses/LICENSE-2.0
	 * 
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
	 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
	 * License for the specific language governing permissions and limitations
	 * under the License.
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mGestureDetector.onTouchEvent(event))
			return true;

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_UP:
			endGesture();
			break;
		}
		return super.onTouchEvent(event);
	}

	// ** 手势结束 *//*
	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;

		// 隐藏
		mDismissHandler.removeMessages(0);
		mDismissHandler.sendEmptyMessageDelayed(0, 500);
	}

	// ** 定时隐藏 *//*
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			controlHolder.mVolumeBrightnessLayout.setVisibility(View.GONE);
		}
	};

	/**
	 * 自定义手势，主要是进度亮度和音量的调节
	 * 
	 * 
	 */
	private class MyGestureListener extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			controlHolder.tipLayoutShow();

			if (isControlState) {

				isControlState = false;

				if (controlHolder.videosound.getVisibility() == View.VISIBLE) {
					controlHolder.videosound.startAnimation(hiddenVoicePanelAnimation);
					controlHolder.videosound.setVisibility(View.GONE);
				}
			} else {

				isControlState = true;

			}

			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return true;
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

			// 远程播放的时候不允许调节音量和亮度
			if (isRemoteMode) {
				return true;
			}

			float mOldX = e1.getX(), mOldY = e1.getY();

			int y = (int) e2.getRawY();

			Display disp = getWindowManager().getDefaultDisplay();

			int windowWidth = disp.getWidth();
			int windowHeight = disp.getHeight();

			if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
				onVolumeSlide((mOldY - y) / windowHeight);
			else if (mOldX < windowWidth / 5.0)// 左边滑动
				onBrightnessSlide((mOldY - y) / windowHeight);

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	/**
	 * 滑动改变音量或者亮度时隐藏和现实(包含动画)
	 */
	public void voicePanelAnimation() {
		if (controlHolder.videosound.getVisibility() == View.GONE) {
			controlHolder.videosound.startAnimation(showVoicePanelAnimation);
			controlHolder.videosound.setVisibility(View.VISIBLE);
		} else {
			controlHolder.videosound.startAnimation(hiddenVoicePanelAnimation);
			controlHolder.videosound.setVisibility(View.GONE);
		}
	}

	/**
	 * 滑动改变声音大小
	 * 
	 * @param percent
	 */
	private void onVolumeSlide(float percent) {

		if (mVolume == -1) {

			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

			if (mVolume < 0)
				mVolume = 0;

			// 显示
			controlHolder.mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			controlHolder.mVolumeBrightnessLayout.setVisibility(View.VISIBLE);

		}

		int index = (int) (percent * mMaxVolume) + mVolume;

		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		// 变更进度条
		ViewGroup.LayoutParams lp = controlHolder.mOperationPercent.getLayoutParams();

		lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;

		controlHolder.mOperationPercent.setLayoutParams(lp);
	}

	/**
	 * 滑动改变亮度
	 * 
	 * @param percent
	 */
	private void onBrightnessSlide(float percent) {

		if (mBrightness < 0) {

			mBrightness = getWindow().getAttributes().screenBrightness;

			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;

			// 显示
			controlHolder.mOperationBg.setImageResource(R.drawable.video_brightness_bg);
			controlHolder.mVolumeBrightnessLayout.setVisibility(View.VISIBLE);

		}

		WindowManager.LayoutParams lpa = getWindow().getAttributes();

		lpa.screenBrightness = mBrightness + percent;

		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.01f)
			lpa.screenBrightness = 0.01f;

		getWindow().setAttributes(lpa);
		ViewGroup.LayoutParams lp = controlHolder.mOperationPercent.getLayoutParams();

		lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);

		controlHolder.mOperationPercent.setLayoutParams(lp);
	}

	@Override
	public void onPrepared(io.vov.vitamio.MediaPlayer mp) {

		if (isHangup) {
			stopPlay();
		}

	}

}
