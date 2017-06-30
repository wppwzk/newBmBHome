package com.ybc.bmbhome.function;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;


/**
 * Created by ybc on 2016/7/14.
 * webview
 */
public class WebActivity extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        String a = intent.getStringExtra("url");
        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(a);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });

        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.web_blue);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
    }

    /**
     * 设置webview点击返回时如果网页可以后退，不关闭webview而后退网页
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
