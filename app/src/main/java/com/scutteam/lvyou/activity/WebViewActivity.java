package com.scutteam.lvyou.activity;

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
import android.widget.TextView;

import com.scutteam.lvyou.R;

public class WebViewActivity extends Activity {
    private WebView wvContent;
    private TextView tvBackIcon;
    private TextView tvBackText;
    private TextView tvTitle;
    private ProgressBar progressBar;

    private String titleText;
    private String backText;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initData();
        findView();
        initView();
        initListener();
        showWebView();
    }

    private void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            titleText = intent.getStringExtra("title");
            backText = intent.getStringExtra("back_text");
            url = intent.getStringExtra("url");
        }
    }

    private void findView() {
        tvBackIcon = (TextView) findViewById(R.id.web_view_left_icon);
        tvBackText = (TextView) findViewById(R.id.web_view_left_text);
        tvTitle = (TextView) findViewById(R.id.web_view_title_text);
        wvContent = (WebView) findViewById(R.id.web_view_content);
        progressBar = (ProgressBar) findViewById(R.id.web_view_progressbar);
    }

    private void initView() {
        if (null != backText) {
            tvBackText.setText(backText);
        }
        if (null != titleText) {
            tvTitle.setText(titleText);
        }
    }

    private void initListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        };
        tvBackIcon.setOnClickListener(listener);
        tvBackText.setOnClickListener(listener);
        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);   //在当前的webview中跳转到新的url

                return true;
            }
        });
    }

    private void showWebView() {
        if (null != url) {
            //启用支持javascript
            WebSettings settings = wvContent.getSettings();
            settings.setJavaScriptEnabled(true);
            wvContent.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        // 网页加载完成
                        progressBar.setVisibility(View.GONE);
                    } else {
                        // 加载中
                    }

                }
            });
            wvContent.loadUrl(url);
        }
    }

    /**
     * 设置webView上面显示的标题
     *
     * @param titleText
     */
    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    /**
     * 设置要显示网页的url
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wvContent.canGoBack()) {
                wvContent.goBack();//返回上一页面
                return true;
            }else {
                onBackClick();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onBackClick(){
        Intent mIntent = new Intent();
        // 设置结果，并进行传送
        this.setResult(0, mIntent);
        finish();
    }
}
