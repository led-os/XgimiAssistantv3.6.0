package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xgimi.device.socket.UdpPostSender;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.MarqueeTextView;

public class XgimiCommunityActivity extends BaseActivity {

    private String url;
    private ProgressBar bar;
    private ImageView back;
    private String name;
    private WebView webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xgimi_community);
        initExras();
        initView();
        XGIMILOG.D("url = " + url);
        webview.loadUrl(url);
    }

    private void initExras() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        name = intent.getStringExtra("name");
    }

    private void initView() {

        ImageView iv_remount = (ImageView) findViewById(R.id.iv_remount);
        if (UdpPostSender.hasConnect) {
            iv_remount.setImageResource(R.drawable.yaokongqihui);
        } else {
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }

        // setYaokongBackground(iv_remount,this);
//        iv_remount.setVisibility(View.GONE);
        back = (ImageView) findViewById(R.id.iv_user);
        MarqueeTextView title = (MarqueeTextView) findViewById(R.id.tv_titile);
        bar = (ProgressBar) findViewById(R.id.myProgressBar);
        title.setText(name);
        webview = (WebView) findViewById(R.id.webView);
        bar.setMax(100);
        bar.setProgress(0);
        ImageView fanui = (ImageView) findViewById(R.id.fanhui);

        WebSettings s = webview.getSettings();
        s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);

        iv_remount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XgimiCommunityActivity.this, RemountActivity.class);
                XgimiCommunityActivity.this.startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (webview.canGoBack()) {
                    webview.goBack();
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

//
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub

                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return true;
                }
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {

                }
//                view.loadUrl(url);
                return true;
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    bar.setVisibility(View.GONE);
                } else {
                    if (bar.getVisibility() == View.GONE) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(progress);
                }
            }
        });

    }

//    public void getXgimi(){
//        subscription = Api.getMangoApi().getXgimi()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer1);
//    }
//    Observer<XgimiBean> observer1 = new Observer<XgimiBean>() {
//        @Override
//        public void onCompleted() {
//            unsubscribe(subscription);
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            e.printStackTrace();
//        }
//
//        @Override
//        public void onNext(XgimiBean channels) {
//            if (channels != null && channels.data != null) {
//        if(mId==1){
//            webview.loadUrl(channels.data.get(0).link_address);
//        }else if(mId==2){
//            webview.loadUrl(channels.data.get(1).link_address);
//
//        }else if(mId==3){
//            webview.loadUrl(channels.data.get(2).link_address);
//
//        }
//            }
//        }
//
//
//    };
}
