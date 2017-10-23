package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.xgimi.device.callback.GMDeviceMusicInfor;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.aes.MyAesToGsonFactory;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.AboutMovieAdapter;
import com.xgimi.zhushou.adapter.JiShuGridviewAdapter;
import com.xgimi.zhushou.bean.CancleMovieCollect;
import com.xgimi.zhushou.bean.FilmDetailBean;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.IqiyiBean;
import com.xgimi.zhushou.bean.IsMovieCollect;
import com.xgimi.zhushou.bean.MovieCollect;
import com.xgimi.zhushou.bean.PlayHostory;
import com.xgimi.zhushou.db.RecordDao;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.GaoSi;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.FilmScrollview;
import com.xgimi.zhushou.widget.FlowLayout;
import com.xgimi.zhushou.widget.MyGridView;
import com.xgimi.zhushou.widget.SourceDilog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FilmDetailActivity extends BaseActivity implements
        View.OnClickListener, GMDeviceMusicInfor, FilmScrollview.ScrollViewListener {


    private static final String TAG = "FilmDetailActivity";
    private Subscription subscription;
    IqiyiBean mIqiYi = new IqiyiBean();
    private String mId;
    @Bind(R.id.rl_gaosi)
    RelativeLayout mRlGaoSi;
    @Bind(R.id.iv_tupian)
    ImageView mImageView;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_leixing)
    TextView mTvLeiXing;
    @Bind(R.id.tv_shangyin)
    TextView mTvShangYin;
    @Bind(R.id.tv_area)
    TextView tvArea;
    @Bind(R.id.collect)
    ImageView collect;
    @Bind(R.id.share)
    ImageView share;
    @Bind(R.id.laiyuan)
    TextView mLaiyuan;
    @Bind(R.id.laiyuan1)
    TextView mLaiyuan1;
    @Bind(R.id.genxinjishu)
    TextView mUpadateJiShu;
    @Bind(R.id.viewpager)
    MyGridView mViewPager;
    @Bind(R.id.actor_information)
    FlowLayout mViewFlow;
    @Bind(R.id.tv_xiangqing)
    TextView mTvDetais;
    @Bind(R.id.xiangguan)
    MyGridView mAboutGridView;
    @Bind(R.id.rl_juji)
    RelativeLayout mRlJuJi;
    @Bind(R.id.ll_xiangqing)
    LinearLayout mLLDecritpt;
    @Bind(R.id.ll_actor)
    LinearLayout mLLActor;
    @Bind(R.id.lltouping)
    LinearLayout mLLTouPing;
    @Bind(R.id.touping)
    TextView tv_touPing;
    @Bind(R.id.touping1)
    TextView tv_touPing1;
    @Bind(R.id.ll_laiyuan)
    LinearLayout mLLLaiYuan;
    @Bind(R.id.ll_laiyuan1)
    LinearLayout mLLLaiYuan1;
    private ImageView daohang;
    public int user_id;
    public String video_id;
    private GimiUser gimuser;
    private boolean isLogo;
    private boolean isCollect;
    private ImageView back;
    private LinearLayout content;
    private View myprog;
    FilmDetailBean mDatas = new FilmDetailBean();
    private List<FilmDetailBean.DataBean.RecommendBean> home = new ArrayList<>();
    private JiShuGridviewAdapter mAdapter;
    private AboutMovieAdapter mAboutMovieAdapter;
    private View jiange;
    private UMSocialService mController;
    private Subscription subscription1;
    private Subscription subscription2;
    private Subscription subscription3;
    private Subscription subscription4;
    private String video_title, video_type, video_cover;
    private LinearLayout jieguo;
    private LinearLayout jianjie;
    private TextView zhuanji;
    private View net_connect;
    private App app;
    public String mSourceIntroduction = "1";
    private ImageView iv_laiyuan;
    //下载进度相关提示初始化
    private RelativeLayout tishi;
    private TextView downName;
    private TextView downProgress;
    private LinearLayout rl_xiazaizhong;
    private TextView zhengzaianzhuang;
    private FilmScrollview filmScrollview;
    private LinearLayout ll_imageView;
    private LinearLayout ll_touping_bottom;
    private RelativeLayout rl_fudong;
    private LinearLayout lll_fudong;
    private ImageView iv_laiyuan1;
    private FrameLayout mLoadFalseFl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_film_detail, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        initExras();
        initView();
        initLocal();
        initListeners();
    }

    private void initExras() {
        Intent intent = getIntent();
        if (intent != null) {
            mId = intent.getStringExtra("id");
            video_id = mId;
            app = (App) getApplicationContext();
            if (app.getLoginInfo() != null) {
                gimuser = app.getLoginInfo();
                isLogo = true;
                user_id = Integer.valueOf(gimuser.data.uid);
            }
        }
        XGIMILOG.D("mId = " + mId);
    }

    private void initUMeng() {
        mController = UMServiceFactory
                .getUMSocialService("http://api.t.xgimi.com/mobile/shareVideo3/"
                        + mId);
        // 设置分享内容
        mController.setShareContent("我正在使用极米无屏电视观看<<" + mDatas.data.title
                + ">>,大屏幕才过瘾，最大可达300寸，欢迎来我这里一起体验玩耍," + "http://t.cn/zW1FDVp");
        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(this, mDatas.data.image));

        // 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104491791",
                "DIeHXRrC1DCVn3ru");
        qqSsoHandler.addToSocialSDK();

        // 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
                "1104491791", "DIeHXRrC1DCVn3ru");
        qZoneSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        // 设置分享文字
        qqShareContent.setShareContent("我正在使用极米无屏电视观看<<" + mDatas.data.title
                + ">>,大屏幕才过瘾，最大可达300寸，欢迎来我这里一起体验玩耍,"
                + "http://api.t.xgimi.com/mobile/shareVideo3/" + mId);
        // 设置分享title
        qqShareContent.setTitle("极米无屏电视");
        // 设置分享图片
        qqShareContent.setShareImage(new UMImage(this, mDatas.data.image));
        // 设置点击分享内容的跳转链接
        qqShareContent.setTargetUrl("http://api.t.xgimi.com/mobile/shareVideo3/"
                + mId);
        mController.setShareMedia(qqShareContent);

        QZoneShareContent qz = new QZoneShareContent();
        qz.setShareContent("我正在使用极米无屏电视观看<<" + mDatas.data.title
                + ">>,大屏幕才过瘾，最大可达300寸，欢迎来我这里一起体验玩耍,");
        qz.setTargetUrl("http://api.t.xgimi.com/mobile/shareVideo3/" + mId);
        qz.setShareImage(new UMImage(this, mDatas.data.image));
        qz.setTitle("极米无屏电视");
        mController.setShareMedia(qz);

        // qz.setShareMedia(new UMImage(this, xiangqing.data.image));
        // 微信
        String appID = "wxdf24aa8a5e969fe2";
        String appSecret = "19a84011e151450ae8cd0668999730e0";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, appID, appSecret);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, appID, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        // 设置微信好友分享内容
        WeiXinShareContent weixinContent1 = new WeiXinShareContent();
        // 设置分享文字
        weixinContent1.setShareContent("我正在使用极米无屏电视观看<<" + mDatas.data.title
                + ">>,大屏幕才过瘾，最大可达300寸，欢迎来我这里一起体验玩耍,"
                + "http://api.t.xgimi.com/mobile/shareVideo3/" + mId);
        // 设置title
        weixinContent1.setTitle("我正在使用极米无屏电视观看<<" + mDatas.data.title
                + ">>,大屏幕才过瘾，最大可达300寸，欢迎来我这里一起体验玩耍,"
                + "http://api.t.xgimi.com/mobile/shareVideo3/" + mId);
        // 设置分享内容跳转URL
        weixinContent1.setTargetUrl("http://api.t.xgimi.com/mobile/shareVideo3/"
                + mId);
        // 设置分享图片
        weixinContent1.setShareImage(new UMImage(this, mDatas.data.image));
        mController.setShareMedia(weixinContent1);

        CircleShareContent weixinContent = new CircleShareContent();
        weixinContent.setShareContent("我正在使用极米无屏电视观看<<" + mDatas.data.title
                + ">>,大屏幕才过瘾，最大可达300寸，欢迎来我这里一起体验玩耍,"
                + "http://api.t.xgimi.com/mobile/shareVideo3/" + mId);
        weixinContent.setTitle("我正在使用极米无屏电视观看<<" + mDatas.data.title
                + ">>,大屏幕才过瘾，最大可达300寸，欢迎来我这里一起体验玩耍,"
                + "http://api.t.xgimi.com/mobile/shareVideo3/" + mId);
        weixinContent.setTargetUrl("http://api.xgimi.com/mobile/shareVideo3/"
                + mId);
        weixinContent.setShareImage(new UMImage(this, mDatas.data.image));
        // weixinContent.setShareMedia(new UMImage(this, xiangqing.data.image));
        mController.setShareMedia(weixinContent);
    }

    private void initView() {
        if (Constant.netStatus) {
            if (GMDeviceStorage.getInstance().getConnectedDevice() != null) {
                SaveTVApp.getInstance(this).getApp(GMDeviceStorage.getInstance().getConnectedDevice().getIp());
            }
        }
        mLoadFalseFl = (FrameLayout) findViewById(R.id.fl_load_false_film_detail);
        myprog = findViewById(R.id.myprog);
        filmScrollview = (FilmScrollview) findViewById(R.id.filmscrollview);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        daohang = (ImageView) findViewById(R.id.daohang);
        daohang.setOnClickListener(this);
        zhuanji = (TextView) findViewById(R.id.zhuanji);
        iv_laiyuan = (ImageView) findViewById(R.id.iv_laiyuan);
        iv_laiyuan1 = (ImageView) findViewById(R.id.iv_laiyuan1);

        jieguo = (LinearLayout) findViewById(R.id.ll_have_net);
        content = (LinearLayout) findViewById(R.id.content);
        content.setVisibility(View.GONE);
        myprog.setVisibility(View.VISIBLE);

        // //下载进度相关提示初始化
        tishi = (RelativeLayout) findViewById(R.id.tishi);
        tishi.setVisibility(View.GONE);
        downName = (TextView) findViewById(R.id.downName);
        downProgress = (TextView) findViewById(R.id.downProgress);
        rl_xiazaizhong = (LinearLayout) findViewById(R.id.rl_xiazaizhong);
        zhengzaianzhuang = (TextView) findViewById(R.id.zhengzaianzhuang);
        ll_imageView = (LinearLayout) findViewById(R.id.ll_imageview);

        dao = new RecordDao(this);
        tv_touPing.setOnClickListener(this);
        tv_touPing1.setOnClickListener(this);
//        mLLTouPing.setOnClickListener(this);
        EventBus.getDefault().register(this);
        collect.setOnClickListener(this);
        share.setOnClickListener(this);
        ll_touping_bottom = (LinearLayout) findViewById(R.id.touping_bottonm);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        date = formatter.format(curDate);
        mAdapter = new JiShuGridviewAdapter(this, mDatas, mIqiYi);
        mViewPager.setAdapter(mAdapter);
        jianjie = (LinearLayout) findViewById(R.id.ll_xiangqing);
        TextView detail = (TextView) findViewById(R.id.detail).findViewById(
                R.id.tv_tishi);
        detail.setText("简介");
        TextView actor = (TextView) findViewById(R.id.actor).findViewById(
                R.id.tv_tishi);
        actor.setText("演员");
        TextView about = (TextView) findViewById(R.id.about).findViewById(
                R.id.tv_tishi);
        about.setText("相关推荐");
        mAboutMovieAdapter = new AboutMovieAdapter(this, home);
        mAboutGridView.setAdapter(mAboutMovieAdapter);
        mLLLaiYuan.setOnClickListener(this);
        mLLLaiYuan1.setOnClickListener(this);
        mLLTouPing.setOnClickListener(this);
        jiange = findViewById(R.id.jiange1);
        //  controlTitle(findViewById(R.id.id_toolbar),true,true,false,false);
//        zhuanji.setText("影视");
        zhuanji.setVisibility(View.GONE);
        lll_fudong = (LinearLayout) findViewById(R.id.touping_bottonm1);
        mAboutGridView.setFocusable(false);
        mAboutGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FilmDetailBean.DataBean.RecommendBean item = mAboutMovieAdapter.getItem(position);
//                XGIMILOG.D("相关 : " + new Gson().toJson(item));
                String videoId = item.video_id;
                Intent intent = new Intent(FilmDetailActivity.this, FilmDetailActivity.class);
                intent.putExtra("id", videoId);
                SaveData.getInstance().mSouceInsight = "5";
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        net_connect = findViewById(R.id.netconnect);
        rl_fudong = (RelativeLayout) findViewById(R.id.rl_title);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpUrl.isNetworkConnected(FilmDetailActivity.this)) {
                    mRlGaoSi.setVisibility(View.VISIBLE);
                    net_connect.setVisibility(View.GONE);
                    myprog.setVisibility(View.VISIBLE);
                    initData();
                }
            }
        });
    }

    private boolean mIsInstall;
    private int mheight;
    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    int a;

    private void initListeners() {
        ViewTreeObserver vto1 = ll_touping_bottom.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl_fudong.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                mheight = a - ll_touping_bottom.getHeight();
            }
        });
        ViewTreeObserver vto = mRlGaoSi.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl_fudong.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                a = mRlGaoSi.getHeight();

                filmScrollview.setScrollViewListener(FilmDetailActivity.this);
            }
        });

    }


    public void onEventMainThread(FeedbackInfo info) {
        tishi.setVisibility(View.VISIBLE);
        zhengzaianzhuang.setVisibility(View.GONE);
        rl_xiazaizhong.setVisibility(View.VISIBLE);
        if (info != null && info.downloadInfo != null) {
            downProgress.setText(info.downloadInfo.progress + "%");
            downName.setText(info.downloadInfo.filename);
            if (info.downloadInfo.progress == 100 || info.downloadInfo.progress > 98) {
                rl_xiazaizhong.setVisibility(View.GONE);
                zhengzaianzhuang.setVisibility(View.VISIBLE);
            }
        }
        if (info.installInfo != null && info.installInfo.stat == 1) {
            if (mDatas != null && mDatas.data != null && mDatas.data.source != null && mDatas.data.source.size() > 0) {
                for (int i = 0; i < mDatas.data.source.size(); i++) {
                    if (info.installInfo.packagename.equals(mDatas.data.source.get(i).gm_intent.p)) {
                        mIsInstall = true;
                        Toast.makeText(FilmDetailActivity.this, mDatas.data.source.get(i).gm_intent.n + "安装完成", Toast.LENGTH_SHORT).show();
                        tishi.setVisibility(View.GONE);
//                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(null,"2",
//                                new VcontrolCmd.ControlCmd(7,3,0), 20000)));
                    }
                }
            }
        }
    }

    //读取缓存数据
    private void initLocal() {
        XGIMILOG.D("");
        String readHomeJson = app.readHomeJson(mId);
        if (!StringUtils.isEmpty(readHomeJson)) {
            XGIMILOG.D("readHomeJson = " + readHomeJson);
            FilmDetailBean data = new Gson().fromJson(readHomeJson, FilmDetailBean.class);
            XGIMILOG.D(" data = " + new Gson().toJson(data));
            if (data != null && data.data != null) {
                loadHome(data);
            }
        } else {
            if (!HttpUrl.isNetworkConnected(FilmDetailActivity.this)) {
                myprog.setVisibility(View.GONE);
                net_connect.setVisibility(View.VISIBLE);
                jieguo.setVisibility(View.GONE);
            }
        }
        String readHomeJson1 = app.readHomeJson(mId + "x");
        if (!StringUtils.isEmpty(readHomeJson)) {
            IsMovieCollect data = new Gson().fromJson(readHomeJson1, IsMovieCollect.class);
            loadHome1(data);
        } else {
            if (!HttpUrl.isNetworkConnected(FilmDetailActivity.this)) {
                myprog.setVisibility(View.GONE);
                net_connect.setVisibility(View.VISIBLE);
                collect.setVisibility(View.GONE);
            }
        }
        if (HttpUrl.isNetworkConnected(FilmDetailActivity.this)) {
            initData();
            getIsCollect(user_id, video_id);
        }
    }

    //是否收藏加载
    private void loadHome1(IsMovieCollect data) {
        if (data != null && data.data != null) {
            if (data.data.equals("had")) {
                isCollect = true;
                collect.setImageResource(R.drawable.shoucanggequ);
            } else {
                isCollect = false;
                collect.setImageResource(R.drawable.shoucang_radio);
            }
            collect.setVisibility(View.VISIBLE);
            net_connect.setVisibility(View.GONE);
            myprog.setVisibility(View.GONE);
        }
    }

    //加载数据
    private void loadHome(FilmDetailBean channels) {
        mTvName.setText(channels.data.title);
        mTvLeiXing.setText(channels.data.kind);
        tvArea.setText(channels.data.area);
        mDatas = channels;
        initUMeng();

        initHotCi(getActors(mDatas.data.actors));
        mAboutMovieAdapter.dataChange(channels.data.recommend);
        video_title = channels.data.title;
        video_type = channels.data.kind;
        video_cover = channels.data.image;
        if (channels.data.description == null) {
            mLLDecritpt.setVisibility(View.GONE);
        }

        for (int i = 0; i < channels.data.source.size(); i++) {
            String source = channels.data.source.get(i).from;
            if (source.equals("iqiyi")) {
                getIqiYi();
            }
        }

//                if(!channels.data.category.equals("电视剧")){
        //判断是否是有级数
        mRlJuJi.setVisibility(View.GONE);
        jiange.setVisibility(View.GONE);
//                }
        if (channels != null && channels.data != null && channels.data.actors == null || channels.data.actors.equals("")) {
            mLLActor.setVisibility(View.GONE);
        }
        mLaiyuan.setText(channels.data.source.size() + "来源");
        String a = channels.data.image;
        if (channels != null && channels.data != null && channels.data.image != null && channels.data.image.equals("")) {
//            mImageView.setImageResource(R.drawable.logo);
        } else {
            Glide.with(getApplicationContext()).load(channels.data.image).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    mImageView.setImageBitmap(resource);
                    Message message = handler.obtainMessage();
                    message.obj = resizeImage(resource, 100, 100);
                    message.what = 0;
                    handler.sendMessage(message);
                }
            });
        }
        if (channels.data.description != null) {
            mTvDetais.setText(channels.data.description);
        } else {
            mLLDecritpt.setVisibility(View.GONE);
        }
        content.setVisibility(View.VISIBLE);
        net_connect.setVisibility(View.GONE);
        myprog.setVisibility(View.GONE);
    }

    //获取影视详情
    Observer<FilmDetailBean> observer = new Observer<FilmDetailBean>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
            myprog.setVisibility(View.GONE);
            XGIMILOG.E("获取完成");
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            XGIMILOG.E("获取出错");
            XGIMILOG.E(e.getMessage());
            myprog.setVisibility(View.GONE);
            filmScrollview.setVisibility(View.GONE);
            mLoadFalseFl.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNext(FilmDetailBean channels) {
//            LogUtil.d("影视详情 : " + new Gson().toJson(channels));
            if (channels != null && channels.data != null) {
                app = (App) FilmDetailActivity.this.getApplicationContext();
                String savaHomeJson = new Gson().toJson(channels);
                app.savaHomeJson(mId, savaHomeJson);
                loadHome(channels);
                mTvName.setText(channels.data.title);
                mTvLeiXing.setText(channels.data.kind.equals("") ? "未知" : channels.data.kind);
                tvArea.setText(channels.data.area.equals("") ? "未知" : channels.data.area);
                mDatas = channels;
                initUMeng();
                mTvShangYin.setText(channels.data.year.equals("") ? "未知" : channels.data.year);
                zhuanji.setText(channels.data.title);
                initHotCi(getActors(mDatas.data.actors));
                mAboutMovieAdapter.dataChange(channels.data.recommend);
                video_title = channels.data.title;
                video_type = channels.data.kind;
                video_cover = channels.data.image;
                if (channels.data.source != null && channels.data.source.size() > 0) {
                    ImageLoaderUtils.display(FilmDetailActivity.this, iv_laiyuan, channels.data.source.get(0).df_icon);
                    ImageLoaderUtils.display(FilmDetailActivity.this, iv_laiyuan1, channels.data.source.get(0).df_icon);
                    if (channels.data.description == null) {
                        mLLDecritpt.setVisibility(View.GONE);
                    }

                    for (int i = 0; i < channels.data.source.size(); i++) {
                        String source = channels.data.source.get(i).from;
                        if (source.equals("iqiyi")) {
                            getIqiYi();
                        }
                    }

//                if(!channels.data.category.equals("电视剧")){
                    //判断是否是有级数
                    mRlJuJi.setVisibility(View.GONE);
                    jiange.setVisibility(View.GONE);
//                }
                    if (channels.data.actors == null || channels.data.actors.equals("")) {
                        mLLActor.setVisibility(View.GONE);
                    }
                    if (channels.data.source == null) {
                        mLaiyuan.setText("暂无来源");
                        mLaiyuan1.setText("暂无来源");
                    } else {
                        mLaiyuan.setText(channels.data.source.size() + "个来源");
                        mLaiyuan1.setText(channels.data.source.size() + "个来源");
                    }

                    Glide.with(getApplicationContext()).load(channels.data.image).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mImageView.setImageBitmap(resource);
                            Message message = handler.obtainMessage();
                            message.obj = resizeImage(resource, 100, 100);
                            message.what = 0;
                            handler.sendMessage(message);
                        }
                    });
                    if (channels.data.description != null) {
                        mTvDetais.setText(channels.data.description);
                    } else {
                        mLLDecritpt.setVisibility(View.GONE);
                    }
                }
                content.setVisibility(View.VISIBLE);
                jieguo.setVisibility(View.VISIBLE);
            }
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    mRlGaoSi.setBackgroundDrawable(new BitmapDrawable(GaoSi.doBlur(
                            bitmap, 50, false)));
                    break;
                case 1:
                    Intent intent = new Intent(FilmDetailActivity.this, AllNumberActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("number", data);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    FilmDetailActivity.this.overridePendingTransition(R.anim.activity_in, 0);
                    break;
            }
        }
    };

    private void initData() {
//        subscription = Api.getMangoApi().getMovieDetail(mId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
        try {
            XGIMILOG.E("开始获取影视详情");
            subscription = Api.getXgimiVideoApi(MyAesToGsonFactory.create(FilmDetailBean.class))
                    .getMovieDetail(Api.getEncodeParam(new String[]{"video_id"}, new String[]{mId}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }

    private boolean isInstall;

    @Override
    public void musicInfor(String s) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lltouping:

                break;
            case R.id.ll_laiyuan1:
            case R.id.ll_laiyuan:
                if (mDatas != null && mDatas.data != null && mDatas.data.source != null && mDatas.data.source.size() > 0) {

//                    SourceDilog mDilog = new SourceDilog(this, mDatas.data.source);

                    SourceDilog mDilog = new SourceDilog(this, mDatas, mDatas.data.source);
                    mDilog.setOnItemClickerListener(new SourceDilog.ItemClick() {
                        @Override
                        public void onItemClick() {
                            addSql(mId);
                            String savaHomeJson = new Gson().toJson(mDatas);
                            app.savaHomeJson(mId, savaHomeJson);
                        }
                    });
                    mDilog.show();
                }
                break;
            case R.id.collect:
                if (app.getLoginInfo() == null) {
                    SaveData.getInstance().toLogo(FilmDetailActivity.this);
                } else {
                    if (!isLogo) {
                        return;
                    }
                    if (!isCollect) {
//                        getFilmCollect(user_id, video_id, video_title, video_type, video_cover);
                        getFilmCollect(user_id, video_id);
                    } else {
                        getCancleCollect(user_id, video_id);
                    }
                }
                break;
            case R.id.touping1:
            case R.id.touping:
                if (mDatas != null) {
                    isInstall = false;
                    if (Constant.netStatus) {
                        if (ToosUtil.getInstance().isInstallTvControl(this)) {
                            ToosUtil.getInstance().addEventUmeng(FilmDetailActivity.this, "event_movie_play");
                            if (SaveData.getInstance().mSouceInsight.equals("1")) {
                                ToosUtil.getInstance().addEventUmeng(FilmDetailActivity.this, "machine_click_event");
                            }
                            if (mDatas.data != null && mDatas.data.source != null && mDatas.data.source.size() > 0) {
                                for (int i = 0; i < ToosUtil.getInstance().mInstallPacageNames.size(); i++) {
                                    XGIMILOG.D(ToosUtil.getInstance().mInstallPacageNames.get(i));
                                    if (mDatas.data.source.get(0).gm_intent.p.equals(ToosUtil.getInstance().mInstallPacageNames.get(i))) {
                                        isInstall = true;
                                        addSql(mId);
//                                    FilmDetailBean data = new Gson().fromJson(readHomeJson, FilmDetailBean.class);
                                        String savaHomeJson = new Gson().toJson(mDatas);
                                        app.savaHomeJson(mId, savaHomeJson);
                                        if (mDatas.data.source.get(0).gm_intent != null && mDatas.data.source.get(0).gm_intent.gm_is != null && mDatas.data.source.get(0).gm_intent.gm_is != null) {
                                            String type = mDatas.data.kind + "." + mDatas.data.category;
                                            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(FilmDetailActivity.this), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(type, mDatas.data.title, mDatas.data.id, 0,
                                                    SaveData.getInstance().mSoucePage, SaveData.getInstance().mSouceInsight, SaveData.getInstance().mSourceInsightLocation, mDatas.data.source.get(0).gm_intent.n,
                                                    mDatas.data.source.get(0).gm_intent.o,
                                                    mDatas.data.source.get(0).gm_intent.u,
//                                                    mDatas.data.source.get(0).gm_intent.p.equals("com.hunantv.license") ? "com.hunantv.market" : mDatas.data.source.get(0).gm_intent.p,
                                                    mDatas.data.source.get(0).gm_intent.p,
                                                    mDatas.data.source.get(0).gm_intent.gm_is.i,
                                                    mDatas.data.source.get(0).gm_intent.gm_is.m), null, null, null, null)));
                                        }
//                                    Toast.makeText(FilmDetailActivity.this, "正在无屏电视上打开" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                                        Intent inten = new Intent(FilmDetailActivity.this, RemountActivity.class);
                                        startActivity(inten);
                                    }
                                }
                                if (isInstall) {
                                    Toast.makeText(FilmDetailActivity.this, "正在无屏电视上打开" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                                } else {
                                    String type1 = mDatas.data.kind + "." + mDatas.data.category;
//                                Toast.makeText(FilmDetailActivity.this, "正在无屏电视上安装" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(
                                            type1, mDatas.data.title, mDatas.data.id, 0, SaveData.getInstance().mSoucePage, SaveData.getInstance().mSouceInsight, SaveData.getInstance().mSourceInsightLocation,
                                            mDatas.data.source.get(0).gm_intent.n,
                                            mDatas.data.source.get(0).gm_intent.o,
                                            mDatas.data.source.get(0).gm_intent.u,
                                            mDatas.data.source.get(0).gm_intent.p,
                                            mDatas.data.source.get(0).gm_intent.gm_is.i,
                                            mDatas.data.source.get(0).gm_intent.gm_is.m), null, null, null, null)));
                                    if (mIsInstall) {
                                        Toast.makeText(FilmDetailActivity.this, "正在无屏电视上打开" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                                        Intent inten = new Intent(FilmDetailActivity.this, RemountActivity.class);
                                        startActivity(inten);
                                    } else {
                                        Toast.makeText(FilmDetailActivity.this, "正在无屏电视上安装" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                                    }
//                                Intent inten = new Intent(FilmDetailActivity.this, RemountActivity.class);
//                                startActivity(inten);
                                }

//                            else{
//                                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(mDatas.data.source.get(0).gm_intent.n,
//                                        mDatas.data.source.get(0).gm_intent.o,
//                                        mDatas.data.source.get(0).gm_intent.u,
//                                        mDatas.data.source.get(0).gm_intent.p,
//                                        mDatas.data.source.get(0).gm_intent.gm_is.get(0).i,
//                                        mDatas.data.source.get(0).gm_intent.gm_is.get(0).m), null, null, null, null)));
//                            }
                            }
                        }

                    } else {
                        ToosUtil.getInstance().isConnectTv(this);
                    }
                }
                break;
            case R.id.share:
                if (mController != null) {
                    ToosUtil.getInstance().addEventUmeng(FilmDetailActivity.this, "event_movie_share");
                    mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
                            SHARE_MEDIA.DOUBAN);
                    mController.openShare(this, false);
                }
                break;
            case R.id.daohang:
                if (Constant.netStatus) {
                    Intent intent = new Intent(this, RemountActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, NewSearchDeviceActivity.class);
                    startActivity(intent);
                }
                break;


        }
    }

    private RecordDao dao;

    // 添加经数据库
    public void addSql(String id) {
        if (mDatas.data.image == null || mDatas.data == null) {
            return;
        }
        List<PlayHostory> playRecords = dao.ChaXunPlay();
        for (int i = 0; i < playRecords.size(); i++) {
            PlayHostory play = playRecords.get(i);
            if (play.id.equals(id)) {
                dao.deletPlayHostory(play);
            }
        }
        dao.addPlay(new PlayHostory(id, mDatas.data.image,
                mDatas.data.title, date, mDatas.data.category));

    }

    private String date;

    // 加载演员
    private void initHotCi(String[] list) {
        mViewFlow.removeAllViews();
        if (list == null || list.length == 0 || list[0].equals("")) {
            mViewFlow.setVisibility(View.GONE);
            return;
        }
        for (int i = 0; i < list.length; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            final TextView tv = new TextView(this);
            tv.setText(list[i]);
            tv.setBackgroundResource(R.drawable.hot_selector);
            tv.setSingleLine(true);
            // tv.setMaxWidth(width/2);
            layoutParams.leftMargin = 20;
            layoutParams.topMargin = 20;
            tv.setTextColor(Color.rgb(94, 94, 94));
            tv.setTextSize(16);
            tv.setLayoutParams(layoutParams);
            tv.setOnClickListener(new OnActorClickListener(list[i]));
            mViewFlow.addView(tv);
        }
    }

    public String[] getActors(String str) {
        XGIMILOG.E(str);
        String[] ary = null;
        if (str != null) {
            ary = str.split(",");//调用API方法按照逗号分隔字符串
        }
        return ary;
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);

        return resizedBitmap;
    }

    public FilmDetailBean.DataBean data;

    public void onEventMainThread(final FilmDetailBean.DataBean data) {
        this.data = data;
        handler.sendEmptyMessage(1);

    }

    public void onEventMainThread(FilmDetailBean.DataBean.SourceBean item) {

        if (!mDatas.data.category.equals("电视剧")) {
            mRlJuJi.setVisibility(View.GONE);
            jiange.setVisibility(View.GONE);
        } else {
            if (item.from.equals("iqiyi")) {
                mRlJuJi.setVisibility(View.VISIBLE);
                jiange.setVisibility(View.VISIBLE);
            } else {
                mRlJuJi.setVisibility(View.GONE);
                jiange.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //检查是否收藏影视
    private void getIsCollect(int user_id, String video_id) {
//        subscription4 = Api.getMangoApi().getcheckCollect(user_id, video_id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer4);
        XGIMILOG.E("获取收藏状态");
        try {
            subscription4 = Api.getXgimiVideoApi(MyAesToGsonFactory.create(IsMovieCollect.class))
                    .getcheckCollect(Api.getEncodeParam(new String[]{"user_id", "video_id"}, new String[]{"" + user_id, video_id}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Observer<IsMovieCollect> observer4 = new Observer<IsMovieCollect>() {
        @Override
        public void onCompleted() {
            unRegist(subscription4);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(IsMovieCollect isMovieCollect) {
            XGIMILOG.D("获取收藏状态成功 : " + isMovieCollect.toString());
            if (isMovieCollect != null && isMovieCollect.data != null) {
                app = (App) FilmDetailActivity.this.getApplicationContext();
                String IsMovieCollect = new Gson().toJson(isMovieCollect);
//                app.savaHomeJson(mId + "x", IsMovieCollect);
                loadHome1(isMovieCollect);
            }
        }
    };

    //添加收藏
    private void getFilmCollect(int user_id, String video_id) {
//        subscription2 = Api.getMangoApi().getMovieCollect(user_id, video_id, video_title, video_type, video_cover).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer2);
        try {
            subscription2 = Api.getXgimiVideoApi(MyAesToGsonFactory.create(MovieCollect.class))
                    .getMovieCollect(Api.getEncodeParam(new String[]{"user_id", "video_id"}, new String[]{user_id + "", video_id}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Observer<MovieCollect> observer2 = new Observer<MovieCollect>() {

        @Override
        public void onCompleted() {
            unRegist(subscription2);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(MovieCollect movieCollect) {
            ToosUtil.getInstance().addEventUmeng(FilmDetailActivity.this, "event_movie_collect");
            if (movieCollect != null && movieCollect.data != null) {
                isCollect = true;
                collect.setImageResource(R.drawable.shoucanggequ);
                Toast.makeText(FilmDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //取消收藏
    private void getCancleCollect(int user_id, String video_id) {
//        subscription3 = Api.getMangoApi().CancleMovieCollect(user_id, video_id).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer3);
        XGIMILOG.D("取消收藏");
        try {
            subscription3 = Api.getXgimiVideoApi(MyAesToGsonFactory.create(CancleMovieCollect.class))
                    .CancleMovieCollect(Api.getEncodeParam(new String[]{"user_id", "video_id"}, new String[]{user_id + "", video_id}))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    Observer<CancleMovieCollect> observer3=new Observer<CancleMovieCollect>() {
//        @Override
//        public void onCompleted() {
//            unRegist(subscription3);

    Observer<CancleMovieCollect> observer3 = new Observer<CancleMovieCollect>() {
        @Override
        public void onCompleted() {
            unRegist(subscription3);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(CancleMovieCollect cancleMovieCollect) {
            XGIMILOG.D("取消收藏 : " + new Gson().toJson(cancleMovieCollect));
            if (cancleMovieCollect != null && cancleMovieCollect.data != null) {
                isCollect = false;
                collect.setImageResource(R.drawable.shoucang_radio);
                Toast.makeText(FilmDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void getIqiYi() {
        subscription1 = Api.getMangoApi().getAllIqiyi("iqiyi", mDatas.data.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }

    Observer<IqiyiBean> observer1 = new Observer<IqiyiBean>() {
        @Override
        public void onCompleted() {
            unRegist(subscription1);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(IqiyiBean channels) {
            if (channels != null && channels.data != null) {
                mAdapter.dataChange(channels);
                Log.e("info", channels.data.size() + "---aiqiyi");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mId != null && app.getLoginInfo() != null) {
            gimuser = app.getLoginInfo();
            isLogo = true;
            user_id = Integer.valueOf(gimuser.data.uid);
            getIsCollect(user_id, video_id);
        }
        if (Constant.netStatus) {
            daohang.setImageResource(R.drawable.yaokongqi);
        } else {
            daohang.setImageResource(R.drawable.gimi_yaokong);

        }
    }

    @Override
    public void onScrollChanged(FilmScrollview scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {   //设置标题的背景颜色
            rl_fudong.setBackgroundColor(Color.argb((int) 0, 41, 41, 51));
            zhuanji.setVisibility(View.GONE);
            lll_fudong.setVisibility(View.GONE);

        } else if (y > 0 && y <= mheight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / mheight;
            float alpha = (255 * scale);
            zhuanji.setVisibility(View.GONE);
            lll_fudong.setVisibility(View.GONE);
            rl_fudong.setBackgroundColor(Color.argb((int) alpha, 41, 41, 51));
        } else {    //滑动到banner下面设置普通颜色
            rl_fudong.setBackgroundColor(Color.argb((int) 255, 41, 41, 51));
            zhuanji.setVisibility(View.VISIBLE);
            lll_fudong.setVisibility(View.VISIBLE);
        }
    }

    private class OnActorClickListener implements View.OnClickListener {

        private String name;

        public OnActorClickListener(String name) {
            this.name = name;
        }

        @Override
        public void onClick(View v) {
            if (name != null && !"".equals(name)) {
                XGIMILOG.E("开始搜索演员 : " + name);
                Intent intent = new Intent(FilmDetailActivity.this, SearchMovieActivity.class);
                intent.putExtra(SearchMovieActivity.SEARCH_EXTRA_KEY, name);
                FilmDetailActivity.this.startActivity(intent);
            }
        }
    }
}







