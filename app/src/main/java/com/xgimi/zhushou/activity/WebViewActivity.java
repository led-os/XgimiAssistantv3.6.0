package com.xgimi.zhushou.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;

/**
 * Created by Administrator on 2016/12/12.
 */
public class WebViewActivity extends BaseActivity{
    private ImageView back;
    private TextView title;
    private WebView webView;
    private String Url,mytitle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView() {
        if(getIntent()!=null){
            Url=getIntent().getStringExtra("url");
            mytitle=getIntent().getStringExtra("mytitle");
        }
        back= (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        back(back);
        title.setText(mytitle);
        webView= (WebView) findViewById(R.id.webview);
        final ProgressBar progessbar= (ProgressBar) findViewById(R.id.progessbar);
        webView.setVisibility(View.GONE);
        progessbar.setVisibility(View.VISIBLE);
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;

            }
        });
        webView.loadUrl(Url);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                //加载完成
                webView.setVisibility(View.VISIBLE);
                progessbar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //加载开始
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                //加载失败
            }

        });
    }
}
