package com.xgimi.zhushou.bean;

import com.baidu.music.model.Channel;
import com.baidu.music.model.Music;
import com.baidu.music.model.TopList;
import com.xgimi.device.callback.HeartBit;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.qttx.QingTingChannelInfo;

import java.util.ArrayList;
import java.util.List;

public class ApplyTitleDanLi {

    public static ApplyTitleDanLi instance;
    public ApplyFeilei feilei;
    public List<String> ztList = new ArrayList<String>();
    public List<Music> xgList = new ArrayList<Music>();
    public List<Channel> dtList = new ArrayList<Channel>();
    public List<TopList> bdList = new ArrayList<>();

    public List<QingTingChannelInfo.Data> qtDatas = new ArrayList<>();
    String prefix = "http://image.xgimi.com/recommend/musicPic/";
    public String QTurl = "http://7jptw2.com2.z0.glb.qiniucdn.com/music_proster/";

    public String[] zttubiao = {prefix + "qingge.png", prefix + "hongge.png",
            prefix + "jinbao.png", prefix + "tianlai.png", prefix + "zt_jdlaoge.png",
            prefix + "oumei.png", prefix + "wangluogequ.png", prefix + "mingge.png",
            prefix + "80hou.png", prefix + "erge.png", prefix + "shanggan.png",
            prefix + "anjing.png", prefix + "xiaoshuo.png", prefix + "tuokouxiu.png",
            prefix + "langman.png", prefix + "guangboju.png", prefix + "ettonggushi.png",
            prefix + "zongyiyule.png", prefix + "xiaohua.png", prefix + "xiangsheng.png",
            prefix + "zhiyu.png", prefix + "yingshi.png", prefix + "dj.png",
            prefix + "huaijiu.png", prefix + "jiqing.png", prefix + "gudianyinyue.png",
            prefix + "zhiyu.png", prefix + "haoshengyin.png", prefix + "guangchang.png",
            prefix + "chunyinyue.png", prefix + "duichang.png", prefix + "yueyu.png",
            prefix + "rege.png", prefix + "qingyinyue.png", prefix + "yingshi.png",
            prefix + "90hou.png", prefix + "liuxing.png", prefix + "rock.png",
            prefix + "beijingyinyue.png", prefix + "tianmi.png",
            prefix + "youxiyinyue.png", prefix + "dianshiju.png", prefix + "gudian.png",
            prefix + "gudian.png", prefix + "zgfeng.png", prefix + "xiangsheng.png",
            prefix + "70hou.png", prefix + "xinge.png", prefix + "yueyu.png",
            prefix + "qingrou.png", prefix + "kongling.png", prefix + "xiqu.png",
            prefix + "junlv.png", prefix + "renao.png", prefix + "taijioa.png",
            prefix + "woshigeshou.png", prefix + "saksi.png", prefix + "bangwan.png",
            prefix + "guoyu.png", prefix + "xiyue.png", prefix + "haoshengyin.png",
            prefix + "xiangsheng.png"};

    public String[] BangDanTubiao = {prefix + "singertop.png",
            prefix + "singettop2.png", prefix + "billboard.png",
            prefix + "hitochinese.png", prefix + "ktvhot.png", prefix + "singer200.png",
            prefix + "oumeijnqu.png", prefix + "huayujingqu.png", prefix + "yangmi.png",
            prefix + "qinggeduichang.png", prefix + "wangluogequ.png",
            prefix + "jindianlaoge.png", prefix + "wuqubang.png",
            prefix + "yaogunbang.png", prefix + "jueshibang.png",
            prefix + "mingyaobang.png", prefix + "chichabang.png",
            prefix + "biaosheng.png", prefix + "biaoshengomei.png"};
    public String[] QTUrlIndex = {QTurl + "touxiao.jpg", QTurl + "xiaoshuo.jpg", QTurl + "gaoxiao.jpg", QTurl + "tuokouxiu.jpg", QTurl + "jiaoyu.jpg", QTurl + "ertong.jpg", QTurl + "caijing.jpg", QTurl + "yule.jpg", QTurl + "guangboju.jpg", QTurl + "xiangsheng.jpg", QTurl + "keji.jpg", QTurl + "qiche.jpg", QTurl + "tiyu.jpg", QTurl + "jushi.jpg", QTurl + "lishi.jpg", QTurl + "gongkaike.jpg", QTurl + "wenhua.jpg", QTurl + "waiyu.jpg", QTurl + "nvxing.jpg", QTurl + "shishang.jpg", QTurl + "qinggan.jpg", QTurl + "jiankang.jpg", QTurl + "lvyou.jpg", QTurl + "pingshu.jpg", QTurl + "xiju.jpg", QTurl + "dianying.jpg", QTurl + "xiaoyuan.jpg", QTurl + "youxidongman.jpg", QTurl + "zimeiti.jpg", QTurl + "yinyue.jpg", QTurl + "pinpaidiantai.jpg", QTurl + "zhongguozhisheng.jpg"};

    private ApplyTitleDanLi() {
    }

    public String phoneNumber;

    public static ApplyTitleDanLi getInstance() {
        if (instance == null) {
            instance = new ApplyTitleDanLi();
        }
        return instance;
    }

    //	public List<GMDevice> gmDevies=new ArrayList<GMDevice>();
    //音乐的位置
    public int mpos = -1;

    public List<GuanGaoList.Infor> guangList = new ArrayList<GuanGaoList.Infor>();
    public List<Music> musicsList = new ArrayList<>();
    public List<Mp3Info> mp3s = new ArrayList<>();
    public int bendiPostion;
    public int musicPostion;
    public App app = new App();
    public String openId;
    public HeartBit heartbean;


    //保存播放的歌曲信息

//    public SaveSong saveSongs;
    //保存状态
    public int bofangzanting = 1;


    //保存连接的位置
    public int connectPostion = -1;

}
