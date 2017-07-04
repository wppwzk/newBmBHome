package com.ybc.bmbhome.function;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;


/**
 * Created by ybc on 2016/7/14.
 * webview
 */
public class WebActivity extends Activity {
    private WebView webView;
    private ProgressBar pg1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        String a = intent.getStringExtra("url");
        webView = (WebView) findViewById(R.id.webview);
        pg1 = (ProgressBar) findViewById(R.id.progressBar1);
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
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {


                if (newProgress == 100) {
                    pg1.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    pg1.setProgress(newProgress);//设置进度值
                }

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
