package com.xgimi.zhushou.fragment.musicfragment;

import android.content.Context;

import com.baidu.music.onlinedata.ArtistManager;
import com.baidu.music.onlinedata.FreshMusicManager;
import com.baidu.music.onlinedata.FreshMusicManager.FreshMusicListener;
import com.baidu.music.onlinedata.OnlineManagerEngine;
import com.baidu.music.onlinedata.PlayinglistManager.OnLoadMusicListListener;
import com.baidu.music.onlinedata.PlayinglistManager.OnPlayListListener;
import com.baidu.music.onlinedata.PlaylistManager;
import com.baidu.music.onlinedata.RadioManager;
import com.baidu.music.onlinedata.RadioManager.RadioListener;
import com.baidu.music.onlinedata.TagManager;
import com.baidu.music.onlinedata.TagManager.TagListener;
import com.baidu.music.onlinedata.TopListManager;
import com.baidu.music.onlinedata.TopListManager.TopListListener;

public class BaiDuMusicList {

	public static BaiDuMusicList instance;
	private BaiDuMusicList(){
		
	}
	
	public static BaiDuMusicList getInstance(){
		if(instance==null){
			instance=new BaiDuMusicList();
		}
		return instance;
	}
	//得到榜单列表
	public void getBangDanList(Context context, TopListListener callback){
		TopListManager manager = OnlineManagerEngine.getInstance(context)
				.getTopListManager(context);
		manager.getTopListAsync(context, callback);
	}
	
	//得到专题列表
	public void getZhuanTiList(Context context, TagListener callback){
		TagManager manager = OnlineManagerEngine.getInstance(context)
				.getTagManager(context);
		manager.getTagListAsync(context, callback);
	}
	//获取最新音乐
	public void getNewGeList(Context context,int limit,FreshMusicListener callback){
		FreshMusicManager manager = OnlineManagerEngine.getInstance(context)
				.getFreshMusicManager(context);
//		manager.getFreshMusicListAsync(context,limit ,callback);
//		manager.getFreshMusicListAsync(limit);
	}
	//得到电台的列表
	public void getDianTaiList(Context context,RadioListener callback){
		RadioManager manager = OnlineManagerEngine.getInstance(context)
				.getRadioManager(context);
		manager.getRadioListAsync(callback);
	}
	
	//得到榜单中音乐的列表
	public void getBangDanDetail(Context context, OnLoadMusicListListener callback, OnPlayListListener callback1, String mType){
		TopListManager mMamager = OnlineManagerEngine.getInstance(context)
	                .getTopListManager(context);
		  mMamager.setOnLoadMusicListListener(callback);
		  mMamager.setPlayListListener(callback1);
		  mMamager.loadTopList(mType);
	}
	
	//得到专题中的音乐列表
	public void getZhuanTiDetail(Context context, OnLoadMusicListListener callback, String mType){
		  TagManager mMamager = OnlineManagerEngine.getInstance(context)
                .getTagManager(context);
		  mMamager.setOnLoadMusicListListener(callback);
		  mMamager.loadTag(mType);
	}

	public void getHotSingerList(Context context, ArtistManager.ArtistListener listener) {
		ArtistManager manager = OnlineManagerEngine.getInstance(context).getArtistManager(context);
		manager.getHotArtistListAsync(context, 1, 9, listener);
	}

    public void getHotPlayList(Context context, PlaylistManager.PlayListInterface.onGetPlayListListener listListener) {
        PlaylistManager manager = OnlineManagerEngine.getInstance(context).getPlayListManager(context);
        manager.getPlayList(context, 1, 9, "", listListener);
    }
}
