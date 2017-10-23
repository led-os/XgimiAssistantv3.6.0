package com.xgimi.zhushou.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.xgimi.zhushou.bean.Mp3Info;
import com.xgimi.zhushou.bean.MusicPostion;
import com.xgimi.zhushou.util.GlobalConsts;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class PlayService extends Service {

	public static final int PLAY_MSG = 1;
	public static final int PAUSE_MSG = 2;
	public static final int STOP_MSG = 3;
	public static final int CONTINUE_MSG = 4;
	public static final int PRIVIOUS_MSG = 5;
	public static final int NEXT_MSG = 6;
	public static final int PROGRESS_CHANGE = 7;
	public static final int PLAYING_MSG = 8;

	private MediaPlayer mediaPlayer;
	public static final String UPDATE_ACTION = "com.wwj.action.UPDATE_ACTION"; // 更新动作
	public static final String MUSIC_CURRENT = "com.wwj.action.MUSIC_CURRENT"; // 当前音乐改变动作
	public static final String MUSIC_DURATION = "com.wwj.action.MUSIC_DURATION"; // 音乐时长改变动作
	public static final String STOPSERVICEPLAY = "com.wwj.action.PLAYTYPE"; // 结束音乐服务.外部调用

	private int current = 0;
	private List<Mp3Info> mp3Infos;
	private boolean isPause;
	private String path;
	private int msg;
	private int currentTime;
	private int duration;
	Timer timer;
	private boolean isStart;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			if (msg.what == 1) {
				if (mediaPlayer != null) {
					currentTime = mediaPlayer.getCurrentPosition();
					// EventBus.getDefault().post(GlobalConsts.mp3Infos.get(current));
					EventBus.getDefault().post(new MusicPostion(currentTime));
					Intent intent = new Intent();
					intent.setAction(MUSIC_CURRENT);
					intent.putExtra("currentTime", currentTime);
					sendBroadcast(intent);
					handler.sendEmptyMessageDelayed(1, 1000);
				}
			}
		};
	};

	private TimerTask timertast;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initMediaPlayer();
	}

	private void initMediaPlayer() {
		if (mediaPlayer != null) {
			return;
		}
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				Log.i("indo", "bofangwancheng");
				Intent sendIntent = new Intent(UPDATE_ACTION);
				sendIntent.putExtra("flag", "end");
				sendBroadcast(sendIntent);
			}
		});

	}
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		if (GlobalConsts.mp3Infos == null || intent == null) {
			return;
		}
		current = intent.getIntExtra("listPosition", -1);
		if (current == -1) {
			return;
		}
		if(GlobalConsts.mp3Infos!=null&&GlobalConsts.mp3Infos.size()>0){
			boolean last=intent.getBooleanExtra("last", false);
			if(!last){
			mp3Infos = GlobalConsts.mp3Infos;
			path = mp3Infos.get(current).getUrl();
			EventBus.getDefault().post(GlobalConsts.mp3Infos.get(current));
			}
		}
		msg = intent.getIntExtra("MSG", 0);
		if (msg == PLAY_MSG) {
			play(0);
		} else if (msg == PAUSE_MSG) {
			pause();
		} else if (msg == STOP_MSG) {
			stop();
		} else if (msg == CONTINUE_MSG) {
			resume();
		} else if (msg == PROGRESS_CHANGE) {
			currentTime = intent.getIntExtra("progress", -1);
			play(currentTime);
		} else if (msg == PLAYING_MSG) {
			handler.sendEmptyMessage(1);
		}
		super.onStart(intent, startId);
	}

	private void play(int currentTime) {
		try {
			initMediaPlayer();
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer
					.setOnPreparedListener(new PreparedListener(currentTime));
			handler.sendEmptyMessage(1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			isPause = true;
		}
	}

	private void stop() {

		if (mediaPlayer != null) {
			handler.removeMessages(1);
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	/**
	 * 推送到服务端之后mediaPlayer为空，再次本地播放会进到这里来
	 */
	private void resume() {

		if (mediaPlayer == null) {
			play(0);
			return;
		}

		if (isPause) {
			mediaPlayer.start();
			isPause = false;
		}
	}

	private final class PreparedListener implements OnPreparedListener {
		private int currentTime;

		public PreparedListener(int currentTime) {
			this.currentTime = currentTime;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start();
			if (currentTime > 0) {
				mediaPlayer.seekTo(currentTime);
			}
			Intent intent = new Intent();
			intent.setAction(MUSIC_DURATION);
			duration = mediaPlayer.getDuration();
			isStart = false;
			intent.putExtra("duration", duration);
			// sendBroadcast(intent);
		}
	}

	// 获取它的位置
	private int getCurrentPosition() {
		int currentPosition = 0;
		if (mediaPlayer != null) {
			currentPosition = mediaPlayer.getCurrentPosition();
		}
		return currentPosition;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
	}
}
