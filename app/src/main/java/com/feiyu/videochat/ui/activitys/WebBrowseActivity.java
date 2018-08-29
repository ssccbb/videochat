package com.feiyu.videochat.ui.activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.utils.StringUtils;

import java.net.URI;

import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.OVER_SCROLL_NEVER;
import static android.view.View.VISIBLE;

public class WebBrowseActivity extends XBaseActivity implements View.OnClickListener {
    public static final String TAG = WebBrowseActivity.class.getSimpleName();
    public static final String URL = "web_url";
    public static final String TITTLE = "web_tittle";
    private String mUrl = "";
    private String mTittle = "";

    @BindView(R.id.tittle)
    TextView mHead;
    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.wv_browser)
    WebView mBrowser;
    @BindView(R.id.pb_loading)
    ProgressBar mProgress;

    @SuppressLint("JavascriptInterface")
    @Override
    public void initData(Bundle savedInstanceState) {
        mBack.setOnClickListener(this);
        if (this.getIntent() == null) {
            this.finish();
            return;
        }

        mUrl = this.getIntent().getStringExtra(URL);
        mTittle = this.getIntent().getStringExtra(TITTLE);
        mHead.setText(mTittle);
        mBrowser.getSettings().setDefaultTextEncodingName("UTF-8");
        mBrowser.getSettings().setLoadsImagesAutomatically(true);
        mBrowser.getSettings().setJavaScriptEnabled(true);
        mBrowser.getSettings().setDomStorageEnabled(true);
        mBrowser.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mBrowser.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mBrowser.addJavascriptInterface(this, "HkApp");
        mBrowser.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgress.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

        });
        mBrowser.setOverScrollMode(OVER_SCROLL_NEVER);
        mBrowser.setWebViewClient(new WebViewClient() {

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                LogUtils.e(TAG,url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mProgress.setVisibility(VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mProgress.setVisibility(GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                mProgress.setVisibility(GONE);
                Log.e(TAG, "errorCode: " + errorCode + "  description: " + description);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        if (!StringUtils.isEmpty(mUrl)) mBrowser.loadUrl(mUrl);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_browse;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context, String tittle, String url) {
        Intent intent = new Intent(context, WebBrowseActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(TITTLE, tittle);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mBrowser.canGoBack()) {
            mBrowser.goBack();
        }else {
            this.finish();
            setResult(0);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            this.finish();
            setResult(0);
        }
    }
}
