package com.xgimi.zhushou.activity;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xgimi.zhushou.R;

import io.vov.vitamio.widget.VideoView;

public class VideoControlHolder {

	private Activity mContext;

	public VideoView videoView;

	public FrameLayout layout_send_tip;

	public RelativeLayout rootlayout;
	/*
	 * Control条
	 */
	public Button videonext;
	public Button videofront;
	public Button videoplay;
	public Button videovoice;
	public LinearLayout videosound;
	public RelativeLayout videohead;
	public RelativeLayout videocontrol;
	public RelativeLayout videohandler;
	public Button videosend;
	public Button videovoladd;
	public Button videovolsub;

	/*
	 * headtitle
	 */
	public Button videoback;
	public TextView videotitle;

	/*
	 * 播放进度
	 */
	public SeekBar videobar;
	public TextView videocurrent;
	public TextView videofinal;
	/*
	 * 亮度和声音大小显示
	 */
	public FrameLayout mVolumeBrightnessLayout;
	public ImageView mOperationBg, mOperationPercent;
	public ImageView voicevol;

	private TranslateAnimation show_head, hide_head;
	private TranslateAnimation show_bottom, hide_bottom;

	private OnClickListener listener;
	private OnLongClickListener longClickListener;

	public VideoControlHolder(Activity context) {
		mContext = context;
		findview();
	}

	public void findview() {

		show_head = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		hide_head = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -1.0f);
		show_bottom = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		hide_bottom = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 1.0f);
		show_head.setDuration(200);
		hide_head.setDuration(200);
		show_bottom.setDuration(200);
		hide_bottom.setDuration(200);

		rootlayout = (RelativeLayout) mContext.findViewById(R.id.videoplay_root);

		videoView = (VideoView) mContext.findViewById(R.id.vitamiovideoview);

		layout_send_tip = (FrameLayout) mContext.findViewById(R.id.videoview_sendtip);

		videonext = (Button) mContext.findViewById(R.id.next_video);
//		videofront = (Button) mContext.findViewById(R.id.previous_video);
		videoplay = (Button) mContext.findViewById(R.id.play_video);

		videovoice = (Button) mContext.findViewById(R.id.video_voice);
		videosound = (LinearLayout) mContext.findViewById(R.id.videoSound);
		videohead = (RelativeLayout) mContext.findViewById(R.id.videoHead);
		videocontrol = (RelativeLayout) mContext.findViewById(R.id.videoControl);
		videohandler = (RelativeLayout) mContext.findViewById(R.id.videoHandler);

		videosend = (Button) mContext.findViewById(R.id.play_send);
		videovoladd = (Button) mContext.findViewById(R.id.video_volume_add);
		videovolsub = (Button) mContext.findViewById(R.id.video_volume_decreast);

		videoback = (Button) mContext.findViewById(R.id.videoBack);

		videotitle = (TextView) mContext.findViewById(R.id.videoTitle);

		videobar = (SeekBar) mContext.findViewById(R.id.videoBar);
		videocurrent = (TextView) mContext.findViewById(R.id.current_video);
		videofinal = (TextView) mContext.findViewById(R.id.final_video);

		mVolumeBrightnessLayout = (FrameLayout) mContext.findViewById(R.id.operation_volume_brightness);
		mOperationBg = (ImageView) mContext.findViewById(R.id.operation_bg);
		mOperationPercent = (ImageView) mContext.findViewById(R.id.operation_percent);

	}

	public void registerListener() {
		if (this.listener != null) {
			videonext.setOnClickListener(listener);
//			videofront.setOnClickListener(listener);
			videoplay.setOnClickListener(listener);
			videovoice.setOnClickListener(listener);
			videosend.setOnClickListener(listener);
			videoback.setOnClickListener(listener);
			videovoladd.setOnClickListener(listener);
			videovolsub.setOnClickListener(listener);
			videovoladd.setOnLongClickListener(longClickListener);
			videovolsub.setOnLongClickListener(longClickListener);
		}
	}

	public void setClickListener(OnClickListener listener, OnLongClickListener longClickListener) {
		this.listener = listener;
		this.longClickListener = longClickListener;
		registerListener();
	}

	public void tipLayoutShow() {
		if (videohead.getVisibility() == View.VISIBLE) {
			videohandler.setVisibility(View.GONE);
			videohead.setVisibility(View.GONE);
			videohead.startAnimation(hide_head);
			videohandler.startAnimation(hide_bottom);
		} else {
			videohandler.setVisibility(View.VISIBLE);
			videohead.setVisibility(View.VISIBLE);
			videohead.startAnimation(show_head);
			videohandler.startAnimation(show_bottom);
		}
	}

}
