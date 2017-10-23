package com.xgimi.zhushou.bean;

/**
 * Created by 霍长江 on 2016/8/30.
 */
public class CurrentPlay {

    public DataBean data;

    public int action;

    public static class DataBean {

        public CurrentPlayMusicInfoBean CurrentPlayMusicInfo;
        public CurrentPlayMusicInfoBean getCurrentPlayMusicInfo() {
            return CurrentPlayMusicInfo;
        }

        public void setCurrentPlayMusicInfo(CurrentPlayMusicInfoBean CurrentPlayMusicInfo) {
            this.CurrentPlayMusicInfo = CurrentPlayMusicInfo;
        }

        public static class CurrentPlayMusicInfoBean {
            public String id;
            public String singer;
            public String title;

         
        }
    }
}
