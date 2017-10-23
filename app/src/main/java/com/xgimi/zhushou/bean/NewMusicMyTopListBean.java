package com.xgimi.zhushou.bean;

import com.baidu.music.model.BaseObject;
import com.baidu.music.model.TopList;

/**
 * Created by linzh on 2017/10/23.
 */

public class NewMusicMyTopListBean extends BaseObject {
    private TopList topList;
    private String coverUrl;

    public NewMusicMyTopListBean(TopList topList) {
        this.topList = topList;
        String name = topList.mName;
        if ("新歌TOP100".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/singertop.png";
        } else if ("billboard".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/billboard.png";
        } else if ("KTV热歌榜".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/ktvhot.png";
        } else if ("欧美金曲榜".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/oumeijnqu.png";
        } else if ("华语金曲榜".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/huayujingqu.png";
        } else if ("影视歌曲榜".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/yangmi.png";
        } else if ("情歌对唱".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/qinggeduichang.png";
        } else if ("网络歌曲".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/wangluogequ.png";
        } else if ("经典老歌".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/jindianlaoge.png";
        } else if ("舞曲榜".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/wuqubang.png";
        } else if ("摇滚榜".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/yaogunbang.png";
        } else if ("爵士榜".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/jueshibang.png";
        } else if ("民谣榜".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/mingyaobang.png";
        } else if ("叱咤歌曲榜".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/chichabang.png";
        } else if ("飙升中文榜".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/biaosheng.png";
        } else if ("飙升欧美榜".equals(name)) {
            coverUrl = "http://image.xgimi.com/recommend/musicPic/biaoshengomei.png";
        }

    }

    public NewMusicMyTopListBean(TopList topList, String coverUrl) {
        this.topList = topList;
        this.coverUrl = coverUrl;
    }

    public TopList getTopList() {
        return topList;
    }

    public void setTopList(TopList topList) {
        this.topList = topList;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
