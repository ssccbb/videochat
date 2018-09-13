package com.weiliao.kinnek.ui.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiliao.kinnek.App;
import com.weiliao.kinnek.common.XBaseActivity;
import com.weiliao.kinnek.model.PhoneVertifyResult;
import com.weiliao.kinnek.net.api.Api;
import com.weiliao.kinnek.net.httprequest.ApiCallback;
import com.weiliao.kinnek.net.httprequest.okhttp.OkHttpRequestUtils;
import com.weiliao.kinnek.utils.StringUtils;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.model.IDResult;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.imageloader.ILFactory;
import cn.droidlover.xdroidmvp.imageloader.ILoader;
import cn.droidlover.xdroidmvp.imageloader.LoadCallback;

public class SplashActivity extends XBaseActivity implements View.OnClickListener{
    public static final String TAG = SplashActivity.class.getSimpleName();

    @BindView(R.id.btn_next)
    TextView skip;
    @BindView(R.id.ids)
    ImageView img;
    @BindView(R.id.id_container)
    View idContainer;
    @BindView(R.id.splash)
    ImageView splash;

    private IDResult ids;
    private Handler uiHandler = new Handler();
    private Bitmap ad;

    /* 主页正常跳转 */private Runnable skipRunnable = new Runnable() {
        @Override
        public void run() {
            next();
        }
    };

    /* 广告页跳转 */private Runnable showAdsRunnable = new Runnable() {
        @Override
        public void run() {
            if (ad == null) {
                next();
            }else {
                showAd();
            }
        }
    };

    @Override
    public void initData(Bundle savedInstanceState) {
        skip.setOnClickListener(this);
        img.setOnClickListener(this);
        uiHandler.postDelayed(showAdsRunnable,2000);
        getIds();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == skip){
            uiHandler.removeCallbacksAndMessages(null);
            next();
        }
        if (v == img){
            uiHandler.removeCallbacksAndMessages(null);
            //Toast.makeText(this, "link to:"+ids.link_url, Toast.LENGTH_SHORT).show();
            if (ids == null || StringUtils.isEmpty(ids.link_url)) return;
            Intent intent = new Intent(context, WebBrowseActivity.class);
            intent.putExtra(WebBrowseActivity.TITTLE, this.getResources().getString(R.string.app_name));
            intent.putExtra(WebBrowseActivity.URL, ids.link_url);
            context.startActivityForResult(intent,0);
        }
    }

    /**
     * 获取id
     * */
    private void getIds(){
        OkHttpRequestUtils.getInstance().requestByGet(Api.API_BASE_URL +"/Index/start_advertisement",
                null,
                PhoneVertifyResult.class, this, new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        //Log.e(TAG, "onSuccess: "+(String) response );
                        ids = new IDResult((String) response);
                        ILFactory.getLoader().loadNet(App.getContext(), StringUtils.convertUrlStr(ids.cover_url),ILoader.Options.defaultOptions(),new LoadCallback(){

                            @Override
                            public void onLoadReady(Bitmap bitmap) {
                                ad = bitmap;
                                img.setImageBitmap(ad);
                            }
                        });
                    }

                    @Override
                    public void onError(String err_msg) {
                        Log.e(TAG, "onError: "+err_msg );
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    private void showAd(){
        idContainer.setVisibility(View.VISIBLE);
        splash.setVisibility(View.GONE);
        uiHandler.removeCallbacksAndMessages(null);
        uiHandler.postDelayed(skipRunnable,3000);
        new MyCountDownTimer(3000,1000).start();
    }

    private void next(){
        finish();
        IndexActivity.open(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ad != null){
            ad.recycle();
            ad = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (uiHandler != null){
            uiHandler.removeCallbacksAndMessages(null);
        }
        next();
    }

    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            skip.setText("跳过("+l/1000+"s)");
        }

        @Override
        public void onFinish() {
            skip.setText("跳过");
        }
    }
}
