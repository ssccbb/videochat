package com.feiyu.videochat.ui.activitys;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.HomePagerAdapter;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.views.TabIndicatorView;
import com.tencent.bugly.crashreport.CrashReport;

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
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.addOnPageChangeListener(this);
        mIndicator.addOnTabIndicatorSelectListener(this);

//        CrashReport.testJavaCrash();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_index;
    }

    @Override
    public Object newP() {
        return null;
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
}
