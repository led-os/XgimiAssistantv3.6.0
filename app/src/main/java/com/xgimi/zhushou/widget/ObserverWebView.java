package com.xgimi.zhushou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by 霍长江 on 2016/9/5.
 */
public class ObserverWebView extends WebView{


    private OnScrollChangedCallback mOnScrollChangedCallback;

    public ObserverWebView(final Context context) {
        super(context);
    }

    public ObserverWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public ObserverWebView(final Context context, final AttributeSet attrs,
                             final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl,
                                   final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(
             OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    public  interface OnScrollChangedCallback {
        public void onScroll(int dx, int dy);
}
}
