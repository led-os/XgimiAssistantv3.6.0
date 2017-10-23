package com.xgimi.zhushou.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.ObserverWebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */
public class WebHtmlIQiYiActivity extends BaseActivity {
    private String mUrl = "http://m.iqiyi.com/search.html?source=input&vfrm=2-3-0-1&key";
    private String myUrl;
    private ObserverWebView webView;
    private ImageView mIv;
    private TextView tv;
    List<VcontrolCmd.CustomPlay.PlayList> mPlyLists = new ArrayList<>();
    private ImageView yaokong;
    private SmartRefreshLayout mRefreshLayout;

    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
//            webView.destroy();
//            webView.reload();
            mIv.setVisibility(View.GONE);
            webView.reload();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iqiyi);
        initExras();
        initView();
    }

    private void initExras() {
        Intent intent = getIntent();
        if (intent != null) {
            String exrasUrl = intent.getStringExtra("url");
            mUrl = mUrl + "=" + exrasUrl;
        }
    }

    private void initView() {
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.srf_web_video_activity);
        mRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        webView = (ObserverWebView) findViewById(R.id.webView);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.getSettings().setJavaScriptEnabled(true);


        WebSettings webSettings = webView.getSettings();
        //开启java script的支持
        webSettings.setJavaScriptEnabled(true);

        // 启用localStorage 和 essionStorage
        webSettings.setDomStorageEnabled(true);

        // 开启应用程序缓存
        webSettings.setAppCacheEnabled(true);
        String appCacheDir = this.getApplicationContext()
                .getDir("cache", Context.MODE_PRIVATE).getPath();
        webSettings.setAppCachePath(appCacheDir);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 10);// 设置缓冲大小，我设的是10M
        webSettings.setAllowFileAccess(true);


        webView.setWebViewClient(new MyWebViewClient());
        mIv = (ImageView) findViewById(R.id.image_view);
        mIv.setVisibility(View.GONE);
        tv = (TextView) findViewById(R.id.tv_titile);
//        tv.setVisibility(View.GONE);
        webView.getSettings()
                .setUserAgentString(
                        "Mozilla/5.0 (iPhone; CPU iPhone OS 7_1 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D5145e Safari/9537.53");

        yaokong = (ImageView) findViewById(R.id.yaokong);
        if (Constant.netStatus) {
            yaokong.setImageResource(R.drawable.yaokongqi);
        } else {
            yaokong.setImageResource(R.drawable.gimi_yaokong_out);
        }
        yaokong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.netStatus) {
                    Intent intent = new Intent(WebHtmlIQiYiActivity.this, RemountActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(WebHtmlIQiYiActivity.this, NewSearchDeviceActivity.class);
                    startActivity(intent);
                }
            }
        });
        mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.netStatus) {
                    if (myUrl != null && !myUrl.equals("")) {
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                                DeviceUtils.getappVersion(WebHtmlIQiYiActivity.this), GMSdkCheck.appId, App.getContext().PACKAGENAME, "6463037342",
                                new VcontrolCmd.CustomPlay(0, mPlyLists, 0, 0, 0)
                        )));
                        Toast.makeText(WebHtmlIQiYiActivity.this, "正在无屏电视上打开", Toast.LENGTH_SHORT).show();

//                    GMMediaControl.getInstance().openVideo(url,1);
                    } else {
                        Toast.makeText(WebHtmlIQiYiActivity.this, "播放失败,请刷新", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ToosUtil.getInstance().isConnectTv(WebHtmlIQiYiActivity.this);
                }
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myUrl != null && !myUrl.equals("")) {

                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                            DeviceUtils.getappVersion(WebHtmlIQiYiActivity.this), GMSdkCheck.appId, App.getContext().PACKAGENAME, "2",
                            new VcontrolCmd.CustomPlay(0, mPlyLists, 0, 0, 0)
                    )));
                    Toast.makeText(WebHtmlIQiYiActivity.this, "正在无屏电视上打开", Toast.LENGTH_SHORT).show();

//                    GMMediaControl.getInstance().openVideo(url,1);
                } else {
                    Toast.makeText(WebHtmlIQiYiActivity.this, "播放失败,请刷新", Toast.LENGTH_SHORT).show();
                }
            }
        });
        webView.setOnScrollChangedCallback(new ObserverWebView.OnScrollChangedCallback() {
            @Override
            public void onScroll(int dx, int dy) {

            }
        });
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // TODO Auto-generated method stub
//                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                view.loadUrl(url);
//                return true;
//            }
//        });

//        webView.loadDataWithBaseURL("about:blank", mUrl, "text/html", "utf-8", null);
        webView.loadUrl(mUrl);
        ImageView back = (ImageView) findViewById(R.id.iv_user);
        ImageView fanui = (ImageView) findViewById(R.id.fanhui);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (webView.canGoBack()) {
                    webView.goBack();
                    mIv.setVisibility(View.GONE);
                } else {
                    finish();
                }
            }
        });
        fanui.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    private int mHeight;
    private int mWidth;
    private int mX;
    private int mY;

    final class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
                return true;
            }
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {

            }

            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            XGIMILOG.E("onPageStarted");
            showDilog("加载中..");
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {
            XGIMILOG.E("onPageFinished :" + url);
            MissDilog();
            mRefreshLayout.finishRefresh();

//            view.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");

//            view.loadUrl("javascript:window.local_obj.showSource((document.getElementsByTagName(\"video\")[0]).src);");


            view.loadUrl("javascript:var script = document.createElement('script');script.type = 'text/javascript';script.text = \"function pageY(elem) { return elem.offsetParent?(elem.offsetTop+pageY(elem.offsetParent)):elem.offsetTop;}\";document.getElementsByTagName('head')[0].appendChild(script);");
            view.loadUrl("javascript:var script = document.createElement('script');script.type = 'text/javascript';script.text = \"function pageX(elem) { return elem.offsetParent?(elem.offsetLeft+pageX(elem.offsetParent)):elem.offsetLeft;}\";document.getElementsByTagName('head')[0].appendChild(script);");
            view.loadUrl("javascript:window.local_obj.showX(pageX(document.getElementsByTagName(\"video\")[0]));");
            view.loadUrl("javascript:window.local_obj.showY(pageY(document.getElementsByTagName(\"video\")[0]));");
            view.loadUrl("javascript:window.local_obj.showWidth((document.getElementsByTagName(\"video\")[0]).offsetWidth);");
            view.loadUrl("javascript:window.local_obj.showHeight((document.getElementsByTagName(\"video\")[0]).offsetHeight);");

            view.loadUrl("javascript:window.local_obj.showSource((document.getElementsByTagName(\"video\")[0]).src);");

//            myUrl =url;

            super.onPageFinished(view, url);
        }
    }

    final class InJavaScriptLocalObj {

        @JavascriptInterface
        public void showSource(String html) {
            XGIMILOG.E(html);

//            Document document = Jsoup.parse(html);
//            Element element = document.getElementById("video");
//            XGIMILOG.E("" + (element == null));
//            if (element != null) {
//                XGIMILOG.E(element.attr("src"));
//            }
//            Elements elements = document.getAllElements();
//            for (int i = 0; i < elements.size(); i++) {
//                XGIMILOG.E(elements.get(i).id());
//            }

            MissDilog();
            if (html == null || "".equals(html)) {
                XGIMILOG.E("");
                Toast.makeText(WebHtmlIQiYiActivity.this, "抱歉，此视频暂不支持投屏播放", Toast.LENGTH_SHORT).show();
                myUrl = html;
            } else {
                XGIMILOG.E("");
                mHandler.sendEmptyMessage(0);
                mPlyLists.clear();
                myUrl = html;
                VcontrolCmd.CustomPlay.PlayList mPlayList = new VcontrolCmd.CustomPlay.PlayList("投屏", myUrl, "网络视频");
                mPlyLists.add(mPlayList);
                Log.e("info", html);
                webView.setWebViewClient(new MyWebViewClient());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setVisibility(View.VISIBLE);
                    }
                });

            }
        }

        @JavascriptInterface
        public void showX(String x) {
            XGIMILOG.E("x---" + x);
            mX = Integer.valueOf(x);
        }

        @JavascriptInterface
        public void showY(String y) {
            XGIMILOG.E("y---" + y);
            mY = Integer.valueOf(y);
        }

        @JavascriptInterface
        public void showWidth(String width) {
            XGIMILOG.E("width---" + width);
            mWidth = Integer.valueOf(width);
        }

        @JavascriptInterface
        public void showHeight(String height) {
            XGIMILOG.E("height---" + height);
            mHeight = Integer.valueOf(height);

        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            XGIMILOG.E("");

            mIv.setVisibility(View.VISIBLE);
            setLayout(mWidth, mHeight, mIv, mX, mY);
        }
    };

    public void setLayout(int width, int height, ImageView view, int x, int y) {
        AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) mIv.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = ToosUtil.dip2px(this, height);
        layoutParams.y = ToosUtil.dip2px(this, y);
        view.setLayoutParams(layoutParams);
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.onPause();
        XGIMILOG.E("");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        webView.onResume();
        XGIMILOG.E("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.netStatus) {
            yaokong.setImageResource(R.drawable.yaokongqi);
        } else {
            yaokong.setImageResource(R.drawable.gimi_yaokong_out);
        }
    }
}
