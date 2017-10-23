package com.xgimi.zhushou.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.xgimi.zhushou.bean.ApplyTitleDanLi;
import com.xgimi.zhushou.bean.MusicState;
import com.xgimi.zhushou.util.GlobalConsts;

import de.greenrobot.event.EventBus;

public class MusicManager {

	private Context mContext;
	public int listPoston = 0;
	public boolean isPlaying;
	public Intent intent_playingservice; // 本地音乐播放服务
	public boolean isRemotePlay = false;// 正在推送标识
	
	public  boolean isBoFangle;

	private MusicManager(Context cntext) {
		this.mContext = cntext;
		regirst();
	}

	public static MusicManager musci = null;
	private MusicStatusReceiver musicStatusReceiver;

	public static MusicManager getInstance(Context context) {
		if (musci == null) {
			musci = new MusicManager(context);
		}
		return musci;
	}

	public void startMusic(int postion) {
		playLocal(postion);
	}

	/**
	 * 本地音乐播放
	 */
	public void playLocal(int postion) {
		listPoston=postion;
		intent_playingservice = new Intent(mContext, PlayService.class);

		intent_playingservice.putExtra("listPosition", listPoston);

		intent_playingservice.putExtra("MSG", PlayService.PLAY_MSG);

		mContext.startService(intent_playingservice);
		isPlaying = true;
		isRemotePlay = false;
	}

	// 播放暂停
	public void onPlayBtnClick(boolean isPlay) {
		if(GlobalConsts.mp3Infos==null|| GlobalConsts.mp3Infos.size()<1){
			return;
		}
		if(!isPlaying&&isPlay&&!isBoFangle){
			startMusic(listPoston);
			return;
		}
		
		/*if (listPoston ==-1&&isPlay) {
			listPoston = 0;
			startMusic(listPoston);
			return;
		}*/
		Intent intent = new Intent(mContext, PlayService.class);
		intent.putExtra("listPosition", listPoston);
		if(listPoston+1==GlobalConsts.mp3Infos.size())
		intent.putExtra("last", true);
		if (isPlaying) {
			intent.putExtra("MSG", PlayService.PAUSE_MSG);
			isPlaying = false;
		} else {
			intent.putExtra("MSG", PlayService.CONTINUE_MSG);
			isPlaying = true;
		}
		mContext.startService(intent);
	}

	// 下一首
	public void onPlayNext() {
		if(GlobalConsts.mp3Infos==null||GlobalConsts.mp3Infos.size()<1){
			return;
		}
		listPoston++;
		if (listPoston == GlobalConsts.mp3Infos.size()) {
			listPoston = 0;
		}
		EventBus.getDefault().post(GlobalConsts.mp3Infos.get(listPoston));
		startMusic(listPoston);
	}
	public void delete(){
		if(ApplyTitleDanLi.getInstance().mp3s==null|| ApplyTitleDanLi.getInstance().mp3s.size()<1){
			return;
		}
		if(ApplyTitleDanLi.getInstance().mp3s.size()<=1){
			EventBus.getDefault().post(new MusicState(2));
//			onPlayBtnClick(true);
			ApplyTitleDanLi.getInstance().mp3s.remove(0);
			listPoston=0;
			return;
		}
		ApplyTitleDanLi.getInstance().mp3s.remove(listPoston);
		if(ApplyTitleDanLi.getInstance().mp3s.size()==0){
			return;
		}
		 listPoston=listPoston%ApplyTitleDanLi.getInstance().mp3s.size();
//		 EventBus.getDefault().post(ApplyTitleDanLi.getInstance().mp3s.get(listPoston));
//		 if(isPlaying)
//		 startMusic(listPoston);
		 
	}
	
	// 下一首
		public void Delete() {
			if (listPoston == GlobalConsts.mp3Infos.size()) {
				listPoston = 0;
			}
			EventBus.getDefault().post(GlobalConsts.mp3Infos.get(listPoston));
			startMusic(listPoston);
		}

	
	//注册广播
	
	public void regirst(){
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(PlayService.UPDATE_ACTION);
		musicStatusReceiver = new MusicStatusReceiver();
		mContext.registerReceiver(musicStatusReceiver, intentFilter);
	}
	//销毁广播
	public void unregisrst(){
		mContext.unregisterReceiver(musicStatusReceiver);
	}
	/**
	 * 自定义的BroadcastReceiver，负责监听从Service传回来的广播
	 * 
	 * 
	 */
	public class MusicStatusReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();

			if (action.equals(PlayService.UPDATE_ACTION)) {
				if (intent.getStringExtra("flag").equals("end")) {
					Log.i("info", "我来播放下 一去 了 ");
					onPlayNext();
				}
			}
		}
	}
}
