package com.xgimi.zhushou.netUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xgimi.zhushou.util.DeviceUtils;

public class HttpUrl {

	// 测试环境:
	public static final String zhengshi = "http://api.t.xgimi.com/";
	// 正式环境zhengshi
	public static final String ceshi = "http://api.xgimi.com/";
	// 图片url
	public static final String imageUrl = "http://image.t.xgimi.com";
	public static final String ImageUrl = "http://image.xgimi.com";
	
	

	// 找回密码邮箱

	public static final String passwordyouxiang = "http://api.xgimi.com//apis/rest/UserOtherService/forgetPass/json";
	public static final String filmtuijian = ceshi+"apis/rest/VideoService/getRecommendsForHelper&u_=1&v_=1&t_=1&p_=2348";
	// 获取影视推荐
	// public static final String
	// fimtuijian="http://api.xgimi.com/apis/rest/VideoService/getRecommends&u_=1&v_=1&t_=1&p_=2348";
	// 获取专题推荐
	public static final String zuantituijian = ceshi
			+ "apis/rest/VideoService/getSubjects?t_=1&u_=1&v_=1&p_=2348";
	// 获取专题下面的列表
	public static final String zuantiitem = ceshi
			+ "apis/rest/VideoService/getVideosBySubject?t_=1&u_=1&v_=1&p_=2348";
	// 获取影视的种类
	public static final String movieZhonglei = ceshi
			+ "apis/rest/VideoService/getKindsToHelper?t_=1&u_=1&v_=1&p_=2348";
	// 获取影视类别
	public static final String movieleiBie = ceshi
			+ "apis/rest/VideoService/getCategorysToHelper?t_=1&u_=1&v_=1&p_=2348";
	// 获取电影年份
	public static final String movieNianfen = ceshi
			+ "apis/rest/VideoService/getYears?t_=1&u_=1&v_=1&p_=2348";
	// 获取电影产地
	public static final String movieArea = ceshi
			+ "apis/rest/VideoService/getAreas?t_=1&u_=1&v_=1&p_=2348";
	// 获取影视热刺
	public static final String movieHorCi = ceshi
			+ "apis/rest/VideoService/getHots?t_=1&u_=1&v_=1&p_=2348";
	// 获取首页的电影电视剧
	public static final String movieHome = ceshi
			+ "apis/rest/VideoService/getIndexList?t_=1&u_=1&v_=1&p_=2348";
	// 获取更多的电影
	public static final String moreMovie = ceshi
			+ "apis/rest/VideoService/getVideosToHelper?t_=1&u_=1&v_=1&p_=2348";
	// 获取影视种类
	public static final String yingshiZhonglei = ceshi
			+ "apis/rest/VideoService/getKindsToHelper?t_=1&u_=1&v_=1&p_=2348";
	// 获取影视详情
	public static final String yingshixiangqing = ceshi
			+ "apis/rest/VideoService/getVideoDetail?t_=1&u_=1&v_=1&p_=2348";
	// 关键子查询
	public static final String guanjianzi = ceshi
			+ "apis/rest/VideoService/searchVideo?t_=1&u_=1&v_=1&p_=2348";
	// /应用先关的接口

	// 广告位
	public static final String applyguanggao = ceshi
			+ "apis/rest/VideoappService/getAppAd?t_=1&u_=1&v_=1&p_=2348";
	// 应用推荐接口
	public static final String applyYouXiis = ceshi
			+ "apis/rest/VideoappService/getRecommendsToHelper?t_=1&u_=1&v_=1&p_=2348";
	// 获取应用分类
	public static final String applyFeiLei = ceshi
			+ "apis/rest/VideoappService/getKinds?t_=1&u_=1&v_=1&p_=2348";
	// 获取所有的分类
	public static final String applyallFielei = ceshi
			+ "apis/rest/VideoappService/getKinds?t_=1&u_=1&v_=1&p_=2348";
	// 搜索游戏
	public static final String applySearch = ceshi
			+ "apis/rest/VideoappService/getAppsToHelper?t_=1&u_=1&v_=1&p_=2348";
	// 获取应用详情
	public static final String applyDetail = ceshi
			+ "apis/rest/VideoappService/getAppDetail?t_=1&u_=1&v_=1&p_=2348";
	// 关键字搜多
	public static final String applyGuanjianzi = ceshi
			+ "apis/rest/VideoappService/searchAppName?t_=1&u_=1&v_=1&p_=2348";
	// 获取热门应用
	public static final String applyHot = ceshi
			+ "apis/rest/VideoappService/getAppHots?t_=1&u_=1&v_=1&p_=2348";
	// 用户收藏
	public static final String userShouChang = ceshi
			+ "apis/rest/VideoService/collectVideo?t_=1&u_=1&v_=1&p_=2348";
	// 获取用户的收藏
	public static final String getUserSHouCang = ceshi
			+ "apis/rest/VideoService/getCollectVideo?t_=1&u_=1&v_=1&p_=2348";

	// 取消收藏
	public static final String quxiaoShouCang = ceshi
			+ "apis/rest/VideoService/cancelCollect?t_=1&u_=1&v_=1&p_=2348";
	// 检查用户是否已经收藏
	public static final String isCollection = ceshi
			+ "apis/rest/VideoService/isCollect?t_=1&u_=1&v_=1&p_=2348";
	// 应用收藏
	public static final String applyCollection = ceshi
			+ "apis/rest/VideoappService/collectApp?t_=1&u_=1&v_=1&p_=2348";
	// 应用取消
	public static final String applyCancelCollection = ceshi
			+ "apis/rest/VideoappService/cancelCollect?t_=1&u_=1&v_=1&p_=2348";
	// 获取应用收藏
	public static final String getAppltCollection = ceshi
			+ "apis/rest/VideoappService/getCollectApp?t_=1&u_=1&v_=1&p_=2348";
	// 获取用户是否应经收藏该应用
	public static final String isCollectionApply = ceshi
			+ "apis/rest/VideoappService/isCollect?t_=1&u_=1&v_=1&p_=2348";
	// 登陆拼接
	// 获取音乐广告接口
	public static final String musicGuangGao = ceshi
			+ "apis/rest/MusicService/getMusicAd?t_=1&u_=1&v_=1&p_=2348";
	// 获取广告列表
	public static final String musicList = ceshi
			+ "apis/rest/MusicService/getMusicList?t_=1&u_=1&v_=1&p_=2348";
	// 获取
	public static final String tuisongMusic = ceshi
			+ "apis/rest/MusicService/sendMusic?t_=1&u_=1&v_=1&p_=2348";

	// 手机号获取验证码
	public static final String user_get_yanzhengma = ceshi
			+ "apis/rest/UsersService/getRegValidate&u_=1&v_=1&t_=1&p_=2348";

	// 注册接口
	public static final String user_zhuce = ceshi
			+ "apis/rest/UsersService/new_reg&u_=1&v_=1&t_=1&p_=2348";
	// 登陆接口
	public static final String user_denglu = ceshi
			+ "apis/rest/UsersService/new_login&u_=1&v_=1&t_=1&p_=2348";
	// 退出接口
	public static final String user_tuichu = ceshi
			+ "apis/rest/UsersService/logout&u_=1&v_=1&t_=1&p_=2348";
	// 检查token
	public static final String user_jiancha = ceshi
			+ "apis/rest/UsersService/checkToken&u_=1&v_=1&t_=1&p_=2348";
	// 更改密码
	public static final String user_findpassword = ceshi
			+ "apis/rest/UsersService/updatePassword&u_=1&v_=1&t_=1&p_=2348";
	// 更改用户名接口
	public static final String user_name_genggai = ceshi
			+ "apis/rest/UsersService/updateUsername&u_=1&v_=1&t_=1&p_=2348";
	// 找回密码时获取的验证码
	public static final String user_findPassword = ceshi
			+ "apis/rest/UsersService/getFindPwdValidate&u_=1&v_=1&t_=1&p_=2348";
	// 找回密码接口
	public static final String user_password = ceshi
			+ "apis/rest/UsersService/findPassword&u_=1&v_=1&t_=1&p_=2348";
	// 用户信息
	public static final String user_information = ceshi
			+ "apis/rest/UsersService/setUserinfo&u_=1&v_=1&t_=1&p_=2348";
	
	//头像地址
	public static final String user_touxiang=ceshi+"apis/rest/UsersService/updateAvatar&u_=1&v_=1&t_=1&p_=2348";
	
	
	//第三方账号登陆
	public static final String thridLogo=ceshi+"apis/rest/UsersService/thirdLogin&u_=1&v_=1&t_=1&p_=2348";
	//第三方账号注册
	public static final String sanfang=ceshi+"apis/rest/UsersService/thirdRegister&u_=1&v_=1&t_=1&p_=2348";
	//第三方账号绑定
	public static final String thridBangDing=ceshi+"apis/rest/UsersService/thirdBound&u_=1&v_=1&t_=1&p_=2348";
	
	
	//第三方账号获取验证码
	public static final String thridYanZhengMa=ceshi+"apis/rest/UsersService/thirdRegisterMessage?u_=1&v_=1&t_=1&p_=2348";


	//影视个性推荐
	public static final String Individuality=ceshi+"apis/rest/VideoSourceService/getIndividuality?u_=1&v_=1&t_=1&p_=2348";
	/**
	 * 服务器那边的地址拼接,主要是时间段之类的说明
	 * 
	 * @param context
	 * @param rawUrl
	 * @return
	 */

	public final static String USER_LOGIN_POST = ceshi
			+ "/apis/rest/UsersService/login/josn";

	public final static String XIUGAITOUXIANG = ceshi
			+ "/apis/rest/UserOtherService/ModifyAvatarOne";
	public final static String CHECKUSERNAME = ceshi
			+ "/apis/rest/UserOtherService/CheckUsername/json";
	public final static String CHECKEMAIL = ceshi
			+ "/apis/rest/UserOtherService/CheckEmail/json";
	public final static String ZHUCE = ceshi
			+ "/apis/rest/UsersService/reg/json";

	public static String getPostAddr(Context context, String rawUrl) {
		StringBuilder b = new StringBuilder();
		long t_time = System.currentTimeMillis() ;
		long p_time = (t_time % 10000) * 3 + 2345;
		b.append("t_=" + t_time); // 请求发起时的时间戳，服务器端会对时间戳进行校验，时间戳与真实时间相差较大时将返回错误信息。
		b.append("&"); // 和时间相关的数值，计算方式为 ( t_ % 10000 ) * 3 + 2345
		b.append("p_=" + p_time);
		b.append("&"); // 手机IMEI识别码
		b.append("i_=" + "" + DeviceUtils.getIMEI(context));
		b.append("&"); // ：设备类型，请填写 ios，安卓请填写android.
		b.append("d_=" + "android");
		b.append("&"); // 设备版本号
		b.append("dv_=" + "" + DeviceUtils.getandroidVersion());
		b.append("&"); // // app版本号，五位整型，如 30202，则表示 版本v3.2.2
		b.append("v_=" + DeviceUtils.getappVersion(context));
		return rawUrl + "?" + b.toString();
	}
	
	
	public static String getTXPostAddr(String url,String pt,String chid,String qv,String pr,String vn,String vn_build ){
		StringBuilder b=new StringBuilder();
		
		b.append("PT="+pt);
		b.append("&CHID="+chid);
		b.append("&QV="+qv);
		b.append("&PR="+pr);
		b.append("&VN="+vn);
		b.append("&VN_BUILD="+vn_build);
		return url+"?"+b.toString();
		
	}

	public static String failedReason(int statusCode) {
		String failedReson = "登陆失败";
		switch (statusCode) {
		case 408:
			failedReson = "time out";
			break;
		case 404:
			failedReson = "not found";
			break;
		}
		return failedReson;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}


	 //腾讯相关的url
	 public static String txTest="tv.aiseet.atianqi.com";
	 public static final String channelList="http://"+txTest+"/i-tvbin/qtv_video/channel_list/get_channel_list?format=json";
	 public static final String OperationalContent="http://"+txTest+"/i-tvbin/qtv_video/special_channel/get_special_channel?channel_selector=";
	 public static final String Channelfilter="http://"+txTest+"/i-tvbin/qtv_video/channel_filter/get_filter?channel_selector=";
	 public static final String VideoList="http://"+txTest+"/i-tvbin/qtv_video/video_list/get_video_list?site=";
	 
	 
	 //获取视频列表接口
	 public static String GetVideosList="http://"+txTest+"/i-tvbin/qtv_video/video_list/get_video_list?site=";
	 //获取专辑详情接口
	 public static String GetAlbumDetails="http://"+txTest+"/i-tvbin/qtv_video/cover_details/get_cover_basic?cid=";
	//获取专辑剧集信息接口
	 public static String GetAlbumEpisodeInfo="http://"+txTest+"/i-tvbin/qtv_video/cover_details/get_cover_videos?cid=";
	//获取专题详情接口
	 public static String GetSpecialDetails="http://"+txTest+"/i-tvbin/qtv_video/topic_detail/qtv_get_topic_detail?tid=";
	//获取专辑推荐接口
	 public static String GetAlbumRecommended="http://"+txTest+"/i-tvbin/qtv_video/recommend/get_recommend_j?tv_cgi_ver=";
	 //获取栏目往期接口
	 public static String GetPastColumns="http://"+txTest+"/i-tvbin/qtv_video/column_info/get_column_info?column_id=";
	 //人名搜索接口
	 public static final String NamesSearch="http://"+txTest+"/i-tvbin/qtv_video/search/get_search_person?key=";
	//视频搜索接口
	public static final String VideoSearch="http://"+txTest+"/i-tvbin/qtv_video/search/get_search_video?page_size=";
	//搜索排行榜接口
	public static final String SearchRanking="http://"+txTest+"/i-tvbin/qtv_video/search/get_search_rank?req_num=";
	 //获得名人详情
	 public static final String CelebrityDetails="http://"+txTest+"/i-tvbin/qtv_video/famous_detail/get_famous_detail?name=";
	 //获得专题列表
	 public static final String GetZhuanTiXiangQing="http://"+txTest+"/i-tvbin/qtv_video/topic_detail/qtv_get_topic_detail?tid=";
}
