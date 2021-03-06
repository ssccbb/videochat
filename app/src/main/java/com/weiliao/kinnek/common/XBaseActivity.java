package com.weiliao.kinnek.common;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jaeger.library.StatusBarUtil;
import com.umeng.analytics.MobclickAgent;

import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XActivity;

public abstract class XBaseActivity<P extends IPresent> extends XActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentDiff(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AppManager.getInstance().removeActivity(this);
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

    /*  只在基类统计子类重写失效  */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
//        AppManager.getInstance().addActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
