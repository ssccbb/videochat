package com.feiyu.videochat.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.UiHandler;
import com.jaeger.library.StatusBarUtil;

import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XActivity;

public abstract class XBaseActivity<P extends IPresent> extends XActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.app_color));
//        SwipeBackHelper.onCreate(this);
//        SwipeBackHelper.getCurrentPage(this)
//                .setSwipeBackEnable(true)
//                .setSwipeSensitivity(0.5f)
//                .setSwipeRelateEnable(true)
//                .setSwipeRelateOffset(300);

        super.onCreate(savedInstanceState);
//        MobclickAgent.setDebugMode(true);
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SwipeBackHelper.onDestroy(this);
    }

    private P p;

    @Override
    protected P getP() {
        if (p == null) {
            p = (P) newP();
            if (p != null) {
                p.attachV(this);
            }
        }
        return p;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);//友盟统计
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);//友盟统计

    }

}
