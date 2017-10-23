package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;

/**
 * Created by Administrator on 2016/9/12.
 */
public class CantConnectActivity extends BaseActivity{
    private ImageView back;
    private TextView title,qq,url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cant_connect);
        initView();
    }

    private void initView() {
        back= (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        title.setText("帮助");
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
        webview.loadDataWithBaseURL("about:blank", "http://assistantapp.xgimi.com/helpconnect.html", "text/html", "utf-8", null);

        webview.loadUrl("http://assistantapp.xgimi.com/helpconnect.html");

//        qq= (TextView) findViewById(R.id.qq);
//        url= (TextView) findViewById(R.id.url);
//        qq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                joinQQGroup("BzFURAiHAGYa3lTblj_GAt_JxTNbsJhL");
//            }
//        });
//        url.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                cmb.setText("http://apkhome.qiniudn.com/XGIMIAssistantFile/gimihelper_remote.apk");
//                cmb.getText();
//                Toast toast=Toast.makeText(CantConnectActivity.this,"复制链接成功",Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        });
    }
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
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
