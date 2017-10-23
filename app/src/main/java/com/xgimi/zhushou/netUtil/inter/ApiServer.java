package com.xgimi.zhushou.netUtil.inter;

import com.xgimi.zhushou.bean.AdvertisementBean;
import com.xgimi.zhushou.bean.AllChannel;
import com.xgimi.zhushou.bean.AllMovie;
import com.xgimi.zhushou.bean.AllMovieArea;
import com.xgimi.zhushou.bean.AllMovieCategory;
import com.xgimi.zhushou.bean.AllMovieYear;
import com.xgimi.zhushou.bean.AllRadioClass;
import com.xgimi.zhushou.bean.AppCollect;
import com.xgimi.zhushou.bean.AppCollectBeen;
import com.xgimi.zhushou.bean.ApplyDetail;
import com.xgimi.zhushou.bean.ApplyFeilei;
import com.xgimi.zhushou.bean.ApplyHomeBean;
import com.xgimi.zhushou.bean.ApplySearc;
import com.xgimi.zhushou.bean.CancleCollect;
import com.xgimi.zhushou.bean.CancleMovieCollect;
import com.xgimi.zhushou.bean.CkeckData;
import com.xgimi.zhushou.bean.ClassToRadio;
import com.xgimi.zhushou.bean.Collect;
import com.xgimi.zhushou.bean.CollectNum;
import com.xgimi.zhushou.bean.FMCancleCollect;
import com.xgimi.zhushou.bean.FMCollect;
import com.xgimi.zhushou.bean.FMCollectList;
import com.xgimi.zhushou.bean.FilmDetailBean;
import com.xgimi.zhushou.bean.HotRadio;
import com.xgimi.zhushou.bean.Individuality;
import com.xgimi.zhushou.bean.InstallNum;
import com.xgimi.zhushou.bean.IqiyiBean;
import com.xgimi.zhushou.bean.IsAppCollect;
import com.xgimi.zhushou.bean.IsCollect;
import com.xgimi.zhushou.bean.IsMovieCollect;
import com.xgimi.zhushou.bean.MVCollectList;
import com.xgimi.zhushou.bean.MVList;
import com.xgimi.zhushou.bean.MVTypes;
import com.xgimi.zhushou.bean.MovieByCategory;
import com.xgimi.zhushou.bean.MovieCollect;
import com.xgimi.zhushou.bean.MovieCollectBeen;
import com.xgimi.zhushou.bean.MovieLeiBie;
import com.xgimi.zhushou.bean.MovieRecommend;
import com.xgimi.zhushou.bean.MyYinShiBeen;
import com.xgimi.zhushou.bean.NewMvList;
import com.xgimi.zhushou.bean.PhoneControl;
import com.xgimi.zhushou.bean.RadioInfo;
import com.xgimi.zhushou.bean.RadioShow;
import com.xgimi.zhushou.bean.SearchBean;
import com.xgimi.zhushou.bean.SearchLiveTv;
import com.xgimi.zhushou.bean.SearchMovieResult;
import com.xgimi.zhushou.bean.SearchRadio;
import com.xgimi.zhushou.bean.UrlBean;
import com.xgimi.zhushou.bean.Vctrolbean;
import com.xgimi.zhushou.bean.XgimiBean;
import com.xgimi.zhushou.bean.ZhuanTiBean;
import com.xgimi.zhushou.bean.getAppTab;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 霍长江 on 2016/8/5.
 */
public interface ApiServer {

    /**
     * music相关链接
     *
     * @return
     */
    //获取个性化统计的影视推荐getIndividuality
    @GET("/rest/VideoSourceService/getIndividuality?u_=1&v_=1&t_=1&p_=2348")
    Observable<Individuality> getIndividuality(@Query("mac") String mac, @Query("page") int page, @Query("pageSize") int pageSize);

    //获取新的MV详情页的数据
    @GET("rest/MvService/getCateDataLists?t_=1&u_=1&v_=1&p_=2348&t=1")
    Observable<NewMvList> getMV();

    //获取MV搜索
    @GET("rest//MvService/mvLists?u_=1&v_=1&t_=1&p_=2348&psize=10")
    Observable<MVList> getSearchMV(@Query("keywords") String keywords, @Query("page") int page);

    //获取所有MV筛选
    @GET("rest/MvService/mvLists?u_=1&v_=1&t_=1&p_=2348")
    Observable<MVList> getMVList(@Query("page") int page, @Query("psize") int psize, @Query("area_type") String area_type, @Query("user_id") int user_id, @Query("order") String order);

    //获取热门电台
    @GET("rest/FmService/fmRecommend?u_=1&v_=1&t_=1&p_=2348&psize=18")
    Observable<HotRadio> getHotRadio(@Query("page") int page);

    //获取所有电台分类
    @GET("rest/FmService/categoryLists?u_=1&v_=1&t_=1&p_=2348")
    Observable<AllRadioClass> getAllClass();

    //获取分类下的电台
    @GET("rest/FmService/cetegoryFmLists?u_=1&v_=1&t_=1&p_=2348&psize=30")
    Observable<ClassToRadio> getRadio(@Query("category_id") int category_id, @Query("page") int page);

    //获取电台搜索
    @GET("rest/FmService/fmSearch?u_=1&v_=1&t_=1&p_=2348&psize=12")
    Observable<SearchRadio> getSearchRadio(@Query("keywords") String keywords, @Query("page") int page);

    //获取电台的信息
    @GET("rest/FmService/fmInfo?u_=1&v_=1&t_=1&p_=2348")
    Observable<RadioInfo> getRadioInfo(@Query("fm_id") int fm_id, @Query("user_id") int user_id);

    //获取电台信息下的节目信息
    @GET("rest/FmService/fmDetail?u_=1&v_=1&t_=1&p_=2348&page=1&psize=30")
    Observable<RadioShow> getRadioShow(@Query("fm_id") int fm_id);

    //添加MV收藏
    @GET("rest/MvService/mvCollect&u_=1&v_=1&t_=1&p_=2348")
    Observable<Collect> getCollectMV(@Query("user_id") int user_id, @Query("mv_id") int mv_id, @Query("mv_title") String mv_title, @Query("mv_artist") String mv_artist, @Query("mv_thumb") String mv_thumb, @Query("mv_play_address") String mv_play_address);

    //取消收藏MV接口
    @GET("rest/MvService/removeCollect&u_=1&v_=1&t_=1&p_=2348")
    Observable<CancleCollect> getCancleMV(@Query("collect_id") int collect_id, @Query("user_id") int user_id);

    //添加MV收藏
    @GET("rest/MvService/checkCollect?u_=1&v_=1&t_=1&p_=2348")
    Observable<IsCollect> getisCollectMV(@Query("user_id") int user_id, @Query("mv_id") int mv_id);


//    //获取收藏数量信息
//    @GET("rest/UcCollectService/collction?u_=1&v_=1&t_=1&p_=2348")
//    Observable<CollectNum> getCollectNum(@Query("user_id") int user_id);

    //获取收藏数量信息 NEW
    @GET("uc_collection_count")
    Observable<CollectNum> getCollectNum(@Query("param") String param);

    //获取MV收藏的信息
    @GET("rest/FmService/collectLists?u_=1&v_=1&t_=1&p_=2348")
    Observable<MVCollectList> getMVCollectList(@Query("page") int page, @Query("psize") int psize, @Query("user_id") int user_id, @Query("type") int type);

    //添加电台收藏rest/FmService/removeCollect?u_=1&v_=1&t_=1&p_=2348
    @GET("rest/FmService/fmCollect?u_=1&v_=1&t_=1&p_=2348")
    Observable<FMCollect> getFMCollect(@Query("fm_id") int fm_id, @Query("user_id") int user_id, @Query("fm_title") String fm_title, @Query("fm_cover") String fm_cover, @Query("fm_author") String fm_author);

    //取消收藏FM接口
    @GET("rest/MvService/removeCollect&u_=1&v_=1&t_=1&p_=2348")
    Observable<FMCancleCollect> getCancleFM(@Query("collect_id") int collect_id, @Query("user_id") int user_id);

    //获取MV收藏的信息
    @GET("rest/FmService/collectLists?u_=1&v_=1&t_=1&p_=2348")
    Observable<FMCollectList> getFMCollectList(@Query("page") int page, @Query("psize") int psize, @Query("user_id") int user_id, @Query("type") int type);


//    //检查是否收藏影视
//    @GET("rest/VideoSourceService/checkCollect?u_=1&v_=1&t_=1&p_=2348")
//    Observable<IsMovieCollect> getcheckCollect(@Query("user_id") int user_id, @Query("video_id") int video_id );

    //检查是否收藏影视 NEW
    @GET("video_collection_check")
    Observable<IsMovieCollect> getcheckCollect(@Query("param") String param);

//    //添加影视收藏
//    @GET("rest/VideoSourceService/videoCollect?u_=1&v_=1&t_=1&p_=2348")
//    Observable<MovieCollect> getMovieCollect(@Query("user_id") int user_id, @Query("video_id") int video_id, @Query("video_title") String video_title, @Query("video_type") String video_type, @Query("video_cover") String video_cover );

    //video_collection_add
    //添加影视收藏 NEW
    @FormUrlEncoded
    @POST("video_collection_add")
    Observable<MovieCollect> getMovieCollect(@Field("param") String param);


//    //取消收藏
//    @GET("rest/VideoSourceService/removeCollect?u_=1&v_=1&t_=1&p_=2348")
//    Observable<CancleMovieCollect> CancleMovieCollect(@Query("user_id") int user_id, @Query("video_id") int video_id );


    //取消收藏 NEW
    @FormUrlEncoded
    @POST("video_collection_remove")
    Observable<CancleMovieCollect> CancleMovieCollect(@Field("param") String param);


//    //获取收藏影视列表
//    @GET("rest/VideoSourceService/collectLists?u_=1&v_=1&t_=1&p_=2348")
//    Observable<MovieCollectBeen> getMovieCollectList(@Query("page") int page , @Query("psize") int psize, @Query("user_id") int user_id );

    //获取收藏影视列表 NEW
    @GET("video_collection_list")
    Observable<MovieCollectBeen> getMovieCollectList(@Query("param") String param);

    //检查APP是否收藏
    @GET("rest/VideoappService/isCollect?u_=1&v_=1&t_=1&p_=2348")
    Observable<IsAppCollect> getAppcheckCollect(@Query("user_id") int user_id, @Query("app_id") String app_id);

    //收藏APP
    @GET("rest/VideoappService/collectApp?u_=1&v_=1&t_=1&p_=2348")
    Observable<AppCollect> getAppCollect(@Query("user_id") int user_id, @Query("app_id") String app_id);

    //取消收藏APP
    @GET("rest/VideoappService/cancelCollect?u_=1&v_=1&t_=1&p_=2348")
    Observable<AppCollect> getCancleAppCollect(@Query("user_id") int user_id, @Query("app_id") String app_id);

    //获取应用收藏列表
    @GET("rest/VideoappService/getCollectApp?u_=1&v_=1&t_=1&p_=2348")
    Observable<AppCollectBeen> getAppCollectList(@Query("user_id") int user_id, @Query("page") int page, @Query("page_size") int page_size, @Query("is_game") int is_game);


    //获取直播内容
    @GET("rest/VideoSourceService/getLive&u_=1&v_=1&t_=1&p_=2348")
    Observable<AllChannel> getLives();

    //获取所有的电影的地区
    @GET("rest/VideoSourceService/getVideoArea&u_=1&v_=1&t_=1&p_=2348")
    Observable<AllMovieArea> getAllArea();

    //获取所有的电影的种类
    @GET("rest/VideoSourceService/getVideoCategory?u_=1&v_=1&t_=1&p_=2348")
    Observable<AllMovieCategory> getAllMovieCategory();

//    //获取所有的电影分类
//    @GET("rest/VideoSourceService/getVideoKindByCategory?u_=1&v_=1&t_=1&p_=2348")
//    Observable<MovieByCategory> getMovieByCategory(@Query("id") String id);

    // 影视筛选条件 NEW
    @GET("video_screen_condition")
    Observable<MovieByCategory> getMovieByCategory(@Query("param") String param);

    //获取所有电影的年份
    @GET("rest/VideoSourceService/getVideoYear?id=4&id=?u_=1&v_=1&t_=1&p_=2348")
    Observable<AllMovieYear> getAllMovieYear();


//    //获取所有的电影
//    @GET("rest/VideoSourceService/getVideos?u_=1&v_=1&t_=1&p_=2348")
//    Observable<AllMovie> getAllMovie(@Query("page") int page, @Query("page_size") int pagesize, @Query("category") String category, @Query("kind") String kind, @Query("area") String area, @Query("year") String year, @Query("key") String key);
//

    //推荐列表以及筛选列表 NEW
//    @FormUrlEncoded
    @GET("video_screen_list")
    Observable<AllMovie> getAllMovie(@Query("param") String param);


    // 检测是否频道关闭
    @GET("rest/VideoSourceService/getCanPlayList?u_=1&v_=1&t_=1&p_=2348")
    Observable<CkeckData> checkData();

    //获取应用安装人数加1/
    @GET("rest/VideoappService/addAppPlays?u_=1&v_=1&t_=1&p_=2348")
    Observable<InstallNum> addAppPlays(@Query("id") String id);

    //获取影视推送
    @GET("rest/AssistanttipsService/getSubTipsList?u_=1&v_=1&t_=1&p_=2348")
    Observable<MyYinShiBeen> getSubTipsList(@Query("t") String t, @Query("device_token") String device_token, @Query("page_size") String page_size, @Query("page") String page);


    // MV地区类型
    @GET("rest/MvService/areaLists?&u_=1&v_=1&t_=1&p_=2348")
    Observable<MVTypes> getMVTypes();

    // MV推荐数据
    @GET("rest//RecommendMvService/mvLists?u_=1&v_=1&t_=1&p_=2348&psize=100")
    Observable<MVList> getMVTuijian();

    //搜索直播电视台
    @GET("rest/VideoSourceService/searchLive&u_=1&v_=1&t_=1&p_=2348")
    Observable<SearchLiveTv> getAllSearchTv(@Query("key") String key, @Query("page") int page, @Query("paget_size") int pagetsize);


    //    //搜索影视改版
//    @GET("rest/VideoSourceService/searchAllVideos&t_=1&p_=2348")
//    Observable<SearchMovieResult> getSearchMovie(@Query("key") String key);

    //搜索影视 NEW
    @GET("video_search_list")
    Observable<SearchMovieResult> getSearchMovie(@Query("param") String param);


//  //  影视推荐(精选)
//    @GET("rest/VideoSourceService/getVideoIndex?u_=1&v_=1&t_=1&p_=2348")
//    Observable<MovieRecommend> getMovieRecmmend(@Query("mac") String mac);

    //影视推荐(精选) new
    @GET("video_subject_list")
    Observable<MovieRecommend> getMovieRecmmendTest(@Query("param") String param);

//    //获取电影详情
//    @GET("rest/VideoSourceService/getVideoDetails&u_=1&v_=1&t_=1&p_=2348")
//    Observable<FilmDetailBean> getMovieDetail(@Query("id") String id);

    //获取电影详情 NEW
    @GET("video_detail")
    Observable<FilmDetailBean> getMovieDetail(@Query("param") String param);

    //获取应用推荐
    @GET("rest/VideoappService/getAppIndex?u_=1&v_=1&t_=1&p_=2348")
    Observable<ApplyHomeBean> getApplyRecomend();

    //获取应用详情
    @GET("rest/VideoappService/getAppDetail?t_=1&u_=1&v_=1&p_=2348")
    Observable<ApplyDetail> getApplyDetais(@Query("id") String id);

    //获取应用
    @GET("rest/VideoappService/getAppsToHelper?t_=1&u_=1&v_=1&p_=2348")
    Observable<ApplySearc> getApply(@Query("is_game") String isGame, @Query("kind") String kind, @Query("key") String key, @Query("page_size") int pagesize, @Query("page") int page);

    //获取应用的分类
    @GET("rest/VideoappService/getKinds?t_=1&u_=1&v_=1&p_=2348")
    Observable<ApplyFeilei> getAppClass();

    //获取应用的热词
    @GET("rest/VideoappService/getAppHots?t_=1&u_=1&v_=1&p_=2348")
    Observable<MovieLeiBie> getAppHot();

    //获取应用收索的关键字
    @GET("rest/VideoappService/searchAppName?t_=1&u_=1&v_=1&p_=2348")
    Observable<SearchBean> getApplyGuanJianJi(@Query("key") String key);

//    //获取影视专题列表
//    @GET("rest/VideoSourceService/getSubjectDetails?id=1&u_=1&v_=1&t_=1&p_=2348")
//    Observable<ZhuanTiBean> getFilmZhuanti(@Query("id") String id);

    //获取影视专题列表 NEW
    @GET("video_subject_content")
    Observable<ZhuanTiBean> getFilmZhuanti(@Query("param") String param);


    //获取服务类型
    @GET("rest/UcCategoryService/index?u_=1&v_=1&t_=1&p_=2348")
    Observable<XgimiBean> getXgimi();

    //获取发现
    @GET("rest/VideoSourceService/liveIndex?u_=1&v_=1&t_=1&p_=2348")
    Observable<UrlBean> getFindLive();

    //获取爱奇艺的级数
    @GET("rest/VideoSourceService/getVideoEpisodes&u_=1&v_=1&t_=1&p_=2348")
    Observable<IqiyiBean> getAllIqiyi(@Query("source") String source, @Query("id") String id);

    //获取新版vcontrol下载地址
    @GET("rest/VideoappService/updateappByPackagename?package_name=com.xgimi.vcontrol&gimi_device=all&u_=1&v_=1&t_=1&p_=2348")
    Observable<Vctrolbean> getVcontrol();

//    //获取影视搜索联想关键字的接口
//    @GET("rest/VideoSourceService/matchingName&u_=1&v_=1&t_=1&p_=2348")
//    Observable<SearchBean> getFilmKeyWord(@Query("key") String key, @Query("num") int num, @Query("type") String type);


    //影视搜索关键字
    @GET("video_search_keyword?")
    Observable<SearchBean> getFilmKeyWord(@Query("param") String param);


    //获取广告位的图片
    @GET("rest/Advertisement/getAdvertisement&u_=1&v_=1&t_=1&p_=2348")
    Observable<AdvertisementBean> getAdvertisement();

    //底部导航栏弹框apis/rest/ApptabService/getAppTab&t_=1&p_=2348
    @GET("rest/ApptabService/getAppTab&t_=1&p_=2348")
    Observable<getAppTab> getAppTab();

    //控制手机同屏显示与否
    @GET("rest/PhoneshowService/getShow?t_=1&u_=1&v_=1&p_=2348")
    Observable<PhoneControl> getPhoneControl();
}
