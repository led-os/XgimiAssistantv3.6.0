package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.ObserverWebView;

import java.util.ArrayList;
import java.util.List;

public class WebHtmlActivity extends BaseActivity {

    private String mUrl;
    private String url;

    private ObserverWebView webView;
    private TextView tv;
    List<VcontrolCmd.CustomPlay.PlayList> mPlyLists = new ArrayList<>();
    private ImageView mIv;
    private ImageView yaokong;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        XGIMILOG.E("");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_html);
        initExras();
        webView = (ObserverWebView) findViewById(R.id.webView);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        mIv = (ImageView) findViewById(R.id.image_view);
        mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.netStatus) {
                    if (url != null && !url.equals("")) {

                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                                GMSdkCheck.appId, null,
                                new VcontrolCmd.CustomPlay(0, 0, null, mPlyLists, 0),
                                null, null, null)));
                        Toast.makeText(WebHtmlActivity.this, "正在无屏电视上打开", Toast.LENGTH_SHORT).show();

//                    GMMediaControl.getInstance().openVideo(url,1);
                    } else {
                        Toast.makeText(WebHtmlActivity.this, "播放失败,请刷新", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ToosUtil.getInstance().isConnectTv(WebHtmlActivity.this);
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
        mIv.setVisibility(View.GONE);
        webView.loadDataWithBaseURL("about:blank", mUrl, "text/html", "utf-8", null);
        webView.loadUrl(mUrl);
        ImageView back = (ImageView) findViewById(R.id.iv_user);

        ImageView fanui = (ImageView) findViewById(R.id.fanhui);
        tv = (TextView) findViewById(R.id.tv_titile);
        tv.setVisibility(View.GONE);
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
                    Intent intent = new Intent(WebHtmlActivity.this, RemountActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(WebHtmlActivity.this, NewSearchDeviceActivity.class);
                    startActivity(intent);
                }
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url != null && !url.equals("")) {

                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                            GMSdkCheck.appId, null,
                            new VcontrolCmd.CustomPlay(0, 0, null, mPlyLists, 0),
                            null, null, null)));
                    Toast.makeText(WebHtmlActivity.this, "正在无屏电视上打开", Toast.LENGTH_SHORT).show();

//                    GMMediaControl.getInstance().openVideo(url,1);
                } else {
                    Toast.makeText(WebHtmlActivity.this, "播放失败,请刷新", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
//				if(webview.canGoBack()){
//					webview.goBack();
//				}
            }
        });

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

    public void setLayout(int width, int height, ImageView view, int x, int y) {
        AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) mIv.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = ToosUtil.dip2px(this, height);
        layoutParams.y = ToosUtil.dip2px(this, y);
//        layoutParams.topMargin=y;
        view.setLayoutParams(layoutParams);
    }

    private void initExras() {
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
    }

    private int mHeight;
    private int mWidth;
    private int mX;
    private int mY;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mIv.setVisibility(View.VISIBLE);
            setLayout(mWidth, mHeight, mIv, mX, mY);
        }
    };

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
            XGIMILOG.E("");
            super.onPageStarted(view, url, favicon);
        }

        public void onPageFinished(WebView view, String url) {

            view.loadUrl("javascript:var script = document.createElement('script');script.type = 'text/javascript';script.text = \"function pageY(elem) { return elem.offsetParent?(elem.offsetTop+pageY(elem.offsetParent)):elem.offsetTop;}\";document.getElementsByTagName('head')[0].appendChild(script);");
            view.loadUrl("javascript:var script = document.createElement('script');script.type = 'text/javascript';script.text = \"function pageX(elem) { return elem.offsetParent?(elem.offsetLeft+pageX(elem.offsetParent)):elem.offsetLeft;}\";document.getElementsByTagName('head')[0].appendChild(script);");
            view.loadUrl("javascript:window.local_obj.showX(pageX(document.getElementsByTagName(\"video\")[0]));");
            view.loadUrl("javascript:window.local_obj.showY(pageY(document.getElementsByTagName(\"video\")[0]));");
            view.loadUrl("javascript:window.local_obj.showWidth((document.getElementsByTagName(\"video\")[0]).offsetWidth);");
            view.loadUrl("javascript:window.local_obj.showHeight((document.getElementsByTagName(\"video\")[0]).offsetHeight);");

            view.loadUrl("javascript:window.local_obj.showSource((document.getElementsByTagName(\"video\")[0]).src);");

            super.onPageFinished(view, url);
        }
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            XGIMILOG.E("html = " + html);
            if (html == null || html.equals("")) {
                Toast.makeText(WebHtmlActivity.this, "数据获取失败,请刷新", Toast.LENGTH_SHORT).show();
                url = html;
            } else {
                XGIMILOG.E("");
                mHandler.sendEmptyMessage(0);
                mPlyLists.clear();
                url = html;
                VcontrolCmd.CustomPlay.PlayList mPlayList = new VcontrolCmd.CustomPlay.PlayList(null, null, null, "", null, url, null);
                mPlyLists.add(mPlayList);
                Log.e("info", html);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setVisibility(View.VISIBLE);
                        webView.setWebViewClient(new MyWebViewClient());
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
}
