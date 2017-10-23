package com.xgimi.zhushou.bean;

public class MusicInfor {

	public String action;
	public musicInfor data;
	public static class musicInfor{
		public currentInfor CurrentPlayMusicInfo;
	}
	public static class currentInfor{
		public String id;
		public String title;
		public String singer;
		public currentInfor(String id, String title, String singer) {
			this.id = id;
			this.title = title;
			this.singer = singer;
		}
		
	}
}
