package com.xgimi.zhushou.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.zhushou.activity.LogoActivity;
import com.xgimi.zhushou.bean.CurrentPlay;
import com.xgimi.zhushou.bean.Individuality;
import com.xgimi.zhushou.bean.MVList;
import com.xgimi.zhushou.bean.MVTypes;
import com.xgimi.zhushou.bean.Mp3Info;
import com.xgimi.zhushou.bean.RadioInfo;
import com.xgimi.zhushou.bean.RadioShow;
import com.xgimi.zhushou.bean.SearchMovieResult;
import com.xgimi.zhushou.bean.VideoInfo;

import java.util.List;

/**
 * Created by 霍长江 on 2016/6/21.
 */
public class SaveData {
    public static SaveData instance;
    //保存引入源
    public String mSouceInsight = "2";
    //保存引入源的页面
    public String mSoucePage = "1";
    //保存引入源位置
    public String mSourceInsightLocation = "1";

    //保存手机同屏的状态
    public boolean isSupport = false;


    private SaveData() {
    }

    public static SaveData getInstance() {
        if (instance == null) {
            instance = new SaveData();
        }
        return instance;
    }

    //保存是哪个路径的跳转
    public int path = 0;

    /**
     * 手机资源那儿的保存的视频顺序
     */
    public List<VideoInfo> myVideoInfo;
    /**
     * 保存个性化推荐视频
     */
    public Individuality saveIndivi;
    /**
     * 保存mac地址
     */
    /**
     * 保存返回的机器型号
     */
    public String device;
    /**
     * 保存电台统计那儿的分类和电台名字和id
     */
    public String dianTaiClass;
    public String fm_title;
    public int category_id;
    /**
     * 传输文件点的那个
     */
    public String deviceMac;
    public int fileTransferPos;
    /**
     * 保存传输的文件
     */
    public List<FeedbackInfo.fileLists> transferFile;
    /**
     * 保存音乐搜索是电台还是mv跳过去的
     */
    public String searchMusic = "mv";
    /**
     * 检测是否是新版的连接
     */
    public boolean isNewTv = false;
    /**
     * 检测是否在播放状态
     */
    public boolean isPlay;
    /**
     * 检测频道是否关闭的状态
     */
    public int gameLive = 1;
    public int channel = 1;
    public int video = 1;
    public int mv = 1;
    /**
     * 音乐详情的相关信息
     */
    public String fenlei;
    public String index;
    public String geName;
    /**
     * 播放界面的跳转判断
     */
    public String bofang_type;
    public int MV_type;
    public int position;
    /**
     * 保存获取的MV类型
     */
    public MVTypes mTypes;
    /**
     * 保存电台详情信息
     */
    public String radio_title;
    public String cover_path;
    public RadioInfo mDetail;
    public RadioInfo mDetail1;
    /**
     * 保存获取到的MV列表信息
     */
    public List<MVList.data> mList;
    public String mv_title;
    public String mv_artist;
    public String bitmap;
    public String mvid;
    public String mv_play_address;
    //保存点击下一曲MV时弹框中的标题
    public String nextTitle;
    /**
     * 保存搜索出来的MV信息
     */
//    public List<SearchMV.data> mVSearch;
    /**
     * 保存获取推荐的MV列表信息
     */
    public List<MVList.data> tuijianList;
    public String collect_id;
    public String mv_id;
    /**
     * 保存百度音乐的信息
     */
    public List<VcontrolCmd.CustomPlay.PlayList> mPlayLists;
    public String music_title;
    public String music_singer;
    /**
     * 保存本地音乐
     */
    public List<Mp3Info> mp3Infos;
    /**
     * 保存当前播放的信息
     */
    public CurrentPlay currentPlay;
    /**
     * 保存电台节目列表信息
     */
    public List<RadioShow.data> mRadioShow;
    public String program_title;
    public String program_play_time;
    /**
     * 保存
     */
    /**
     * 判断是否登陆并跳转到登陆界面
     */
    public void toLogo(final Activity context) {
        SaveData.getInstance().startLogo(context);
//        SignOutDilog singDilog = new SignOutDilog(context, "您还未登陆，是否现在登陆",
//                null);
//        singDilog.show();
//        singDilog.setOnLisener(new SignOutDilog.onListern() {
//            @Override
//            public void send() {
//                // TODO Auto-generated method stub
//
//            }
//        });
    }

    /**
     * 跳转到登陆界面
     */
    public void startLogo(Context context) {
        Intent intent = new Intent(context, LogoActivity.class);
        intent.putExtra("laiyuan", "qita");
        context.startActivity(intent);
    }

    /**
     * 保存上一次的播放的来源
     */
    public String mPlaypackName;

    /**
     * 保存状态
     */
    public boolean mIsInstall;
    /**
     * 保存播放历史那儿的ID
     */
    public String hositoryId;
    /**
     * 保存影视搜索的相关数据
     */
    public List<SearchMovieResult.DataBean> searchmovie;
    /**
     * 保存影视搜索的title的position
     */
    public int dy;
    public int dsj;
    public int zy;
    public int dm;
    public int jlp;
    /**
     * 保存影视搜索的ID
     */
    public String movieSearchId;
    /**
     * 保存音乐的默认顺序
     */
    public List<Mp3Info> formerData;
}
