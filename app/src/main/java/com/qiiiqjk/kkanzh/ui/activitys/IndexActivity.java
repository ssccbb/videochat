package com.qiiiqjk.kkanzh.ui.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qiiiqjk.kkanzh.App;
import com.qiiiqjk.kkanzh.BuildConfig;
import com.qiiiqjk.kkanzh.R;
import com.qiiiqjk.kkanzh.adapter.HomePagerAdapter;
import com.qiiiqjk.kkanzh.common.XBaseActivity;
import com.qiiiqjk.kkanzh.model.CheckUpdateResult;
import com.qiiiqjk.kkanzh.model.PhoneVertifyResult;
import com.qiiiqjk.kkanzh.net.api.Api;
import com.qiiiqjk.kkanzh.net.httprequest.ApiCallback;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.JKOkHttpParamKey;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.OkHttpRequestUtils;
import com.qiiiqjk.kkanzh.ui.fragments.LoginDialogFragment;
import com.qiiiqjk.kkanzh.utils.DownloadUtil;
import com.qiiiqjk.kkanzh.utils.SharedPreUtil;
import com.qiiiqjk.kkanzh.utils.StringUtils;
import com.qiiiqjk.kkanzh.utils.Utils;
import com.qiiiqjk.kkanzh.views.TabIndicatorView;
import com.qiiiqjk.kkanzh.views.dialog.UpdateDialogFragment;
import com.tencent.bugly.beta.Beta;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;

public class IndexActivity extends XBaseActivity implements TabIndicatorView.OnTabIndicatorSelectListener,ViewPager.OnPageChangeListener{
    @BindView(R.id.tab_indicator)
    TabIndicatorView mIndicator;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private HomePagerAdapter mHomeAdapter;
    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public void initData(Bundle savedInstanceState) {
        mHomeAdapter = new HomePagerAdapter(this, getSupportFragmentManager());
        mViewPager.setAdapter(mHomeAdapter);
        mViewPager.setOffscreenPageLimit(mHomeAdapter.getCount());
        mViewPager.addOnPageChangeListener(this);
        mIndicator.addOnTabIndicatorSelectListener(this);

        checkPermission();
        checkUpdate();
        Beta.checkUpgrade(false,false);
    }

    private void login(){
        if (SharedPreUtil.isLogin()){
            return;
        }
        LoginDialogFragment login = new LoginDialogFragment();
        login.show(getSupportFragmentManager(),LoginDialogFragment.TAG);
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int readCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int recordCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            int cameraCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            int writeCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            ArrayList<String> p = new ArrayList<>();
            if(recordCheck == PackageManager.PERMISSION_DENIED){
                p.add(Manifest.permission.RECORD_AUDIO);
            }
            if(writeCheck == PackageManager.PERMISSION_DENIED){
                p.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if(readCheck == PackageManager.PERMISSION_DENIED){
                p.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if(cameraCheck == PackageManager.PERMISSION_DENIED){
                p.add(Manifest.permission.CAMERA);
            }
            String[] permissions = new String[p.size()];
            for (int i = 0; i < p.size(); i++) {
                permissions[i] = p.get(i);
            }

            if (permissions.length != 0) {
                ActivityCompat.requestPermissions(this, permissions, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_index;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context){
        context.startActivity(new Intent(context,IndexActivity.class));
    }

    @Override
    public void onTabSelect(int position) {
        mViewPager.setCurrentItem(position, true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.select(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(App.getContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void checkUpdate(){
        if (!SharedPreUtil.isLogin() || Utils.isHideMode()) return;
        OkHttpRequestUtils.getInstance().requestByGet(Api.API_BASE_URL +"/config/check_update",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.CHECK_UPDATE,
                        SharedPreUtil.getLoginInfo().uid, "2", String.valueOf(BuildConfig.VERSION_CODE)),
                PhoneVertifyResult.class, this, new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e("update", "onSuccess: "+(String) response );
                        try {
                            JSONObject object = new JSONObject((String)response);
                            if(object.get("data") != null){
                                Gson gson = new Gson();
                                CheckUpdateResult update = gson.fromJson(
                                        object.getJSONObject("data").toString(), CheckUpdateResult.class);
                                if (StringUtils.isEmpty(update.link)) return;
                                UpdateDialogFragment dialog = new UpdateDialogFragment();
                                Bundle b = new Bundle();
                                b.putSerializable(UpdateDialogFragment.TAG,update);
                                dialog.setArguments(b);
                                dialog.addOnClickListener(new UpdateDialogFragment.onClickListener() {
                                    @Override
                                    public void onConfirmCallback() {
                                        dialog.dismissAllowingStateLoss();
                                        Toast.makeText(IndexActivity.this, "后台下载", Toast.LENGTH_SHORT).show();
                                        download(update.link);
                                    }

                                    @Override
                                    public void onCancelCallback() {
                                        dialog.dismissAllowingStateLoss();
                                    }
                                });
                                dialog.show(getSupportFragmentManager(),UpdateDialogFragment.TAG);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String err_msg) {
                        Log.e("update", "onError: "+err_msg );
                        Toast.makeText(IndexActivity.this, "安装包下载失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        Log.e("update", "onFailure !");
                        Toast.makeText(IndexActivity.this, "安装包下载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void download(String url){
        DownloadUtil.get().download(url, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File str) {
                Log.e("update", "onDownloadSuccess: "+str.getAbsolutePath() );
                Utils.installApk(IndexActivity.this,str);
            }

            @Override
            public void onDownloading(int progress) {
                Log.e("update", "onDownloading: "+progress );
            }

            @Override
            public void onDownloadFailed() {
                Log.e("update", "onDownloadFailed! ");
                Toast.makeText(IndexActivity.this, "安装包下载失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
