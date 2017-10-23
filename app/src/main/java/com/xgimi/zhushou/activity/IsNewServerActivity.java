package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.SaveTVApp;

public class IsNewServerActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_new_server);
        initView();
    }
    private void initView(){
        controlTitle(findViewById(R.id.id_toolbar),true,true,false,false);
        tv.setText("帮助");
        WebView webview= (WebView) findViewById(R.id.webView);
        WebSettings s = webview.getSettings();
        s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);

        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new MyWebViewClient());
        webview.loadDataWithBaseURL("about:blank", "http://assistantapp.xgimi.com/helpinstall.html", "text/html", "utf-8", null);

        webview.loadUrl("http://assistantapp.xgimi.com/helpinstall.html");
        SaveTVApp.getInstance(this).getApp1(GMDeviceStorage.getInstance().getConnectedDevice().getIp());
    }
    final class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
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
    }
}
