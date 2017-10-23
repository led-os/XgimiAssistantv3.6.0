package com.xgimi.zhushou.bean;

import com.baidu.music.model.Artist;
import com.baidu.music.model.ArtistList;
import com.baidu.music.model.BaseObject;
import com.baidu.music.model.Playlist;
import com.baidu.music.model.PlaylistItems;
import com.baidu.music.model.TopList;
import com.baidu.music.model.TopLists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linzh on 2017/10/23.
 */

public class NewMusicHomeBean {

    public static final int ITEM_TYPE_SINGER = 1;
    public static final int ITEM_TYPE_TOP_LIST = 2;
    public static final int ITEM_TYPE_PLAY_LIST = 3;

    public static final int SUBJECT_COUNT = 9;
    public static final int ITEM_COUNT = 3;

    private List<BaseObject> mDataList;

    public void addTopListIcon() {

    }
    public void addData(BaseObject data) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        if (data instanceof ArtistList) {
            mDataList.add(new HeadBean(ITEM_TYPE_SINGER));

            ArtistList list = (ArtistList)data;
            for (int i = 0; i < ITEM_COUNT; i++) {
                SingerItemBean bean = new SingerItemBean();
                bean.getArtistList().add(list.getItems().get(i * 3 + 0));
                bean.getArtistList().add(list.getItems().get(i * 3 + 1));
                bean.getArtistList().add(list.getItems().get(i * 3 + 2));
                mDataList.add(bean);
            }

            mDataList.add(new FootBean(ITEM_TYPE_SINGER));
        } else if (data instanceof TopLists) {
            mDataList.add(new HeadBean(ITEM_TYPE_TOP_LIST));

            TopLists list = (TopLists)data;
            for (int i = 0; i < ITEM_COUNT; i++) {
                TopListItemBean bean = new TopListItemBean();
                bean.getTopListList().add(new NewMusicMyTopListBean(list.getItems().get(i * 3 + 0)));
                bean.getTopListList().add(new NewMusicMyTopListBean(list.getItems().get(i * 3 + 1)));
                bean.getTopListList().add(new NewMusicMyTopListBean(list.getItems().get(i * 3 + 2)));
                mDataList.add(bean);
            }

            mDataList.add(new FootBean(ITEM_TYPE_TOP_LIST));
        } else if (data instanceof Playlist) {
            mDataList.add(new HeadBean(ITEM_TYPE_PLAY_LIST));

            Playlist list = (Playlist)data;
            for (int i = 0; i < ITEM_COUNT; i++) {
                PlayListItemBean bean = new PlayListItemBean();
                bean.getPlaylistItemsList().add(list.getItems().get(i * 3 + 0));
                bean.getPlaylistItemsList().add(list.getItems().get(i * 3 + 1));
                bean.getPlaylistItemsList().add(list.getItems().get(i * 3 + 2));
                mDataList.add(bean);
            }

            mDataList.add(new FootBean(ITEM_TYPE_PLAY_LIST));
        }
    }

    public List<BaseObject> getDataList() {
        return mDataList;
    }

    public class HeadBean extends BaseObject {
        private int type;

        public HeadBean(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public class FootBean extends BaseObject {
        private int type;

        public FootBean(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public class SingerItemBean extends BaseObject {
        private List<Artist> artistList;

        public List<Artist> getArtistList() {
            if (artistList == null) {
                artistList = new ArrayList<>();
            }
            return artistList;
        }

        public void setArtistList(List<Artist> artistList) {
            this.artistList = artistList;
        }
    }

    public class TopListItemBean extends BaseObject {

        private List<NewMusicMyTopListBean> topListList;

        public List<NewMusicMyTopListBean> getTopListList() {
            if (topListList == null) {
                topListList = new ArrayList<>();
            }
            return topListList;
        }


    }

    public class PlayListItemBean extends BaseObject {

        private List<PlaylistItems> playlistItemsList;

        public List<PlaylistItems> getPlaylistItemsList() {
            if (playlistItemsList == null) {
                playlistItemsList = new ArrayList<>();
            }
            return playlistItemsList;
        }

        public void setPlaylistItemsList(List<PlaylistItems> playlistItemsList) {
            this.playlistItemsList = playlistItemsList;
        }
    }
}
