package com.weiliao.kinnek.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.weiliao.kinnek.App;
import com.weiliao.kinnek.adapter.HomeChildPagerAdapter;
import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.ui.activitys.SearchActivity;
import com.weiliao.kinnek.R;

import butterknife.BindView;

public class HomePagerFragment extends XBaseFragment implements ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener,View.OnClickListener{
    @BindView(R.id.view_pager_1)
    ViewPager mPager;
    @BindView(R.id.tab_layout)
    TabLayout mTab;
    @BindView(R.id.search)
    View mSearch;

    public static HomePagerFragment instance;
    private HomeChildPagerAdapter mHomeAdapter;

    public static HomePagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new HomePagerFragment();
    }

    public HomePagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (mHomeAdapter != null) return;
        mHomeAdapter = new HomeChildPagerAdapter(App.getContext(), getChildFragmentManager());
        mPager.setOffscreenPageLimit(mHomeAdapter.getCount());
        mPager.setAdapter(mHomeAdapter);
        mPager.addOnPageChangeListener(this);
        mPager.setCurrentItem(1);

        mTab.setupWithViewPager(mPager);
        mTab.removeAllTabs();
        mTab.addTab(mTab.newTab().setText("关注"));
        mTab.addTab(mTab.newTab().setText("热门"));
        mTab.addTab(mTab.newTab().setText("推荐"));
        mTab.setSelectedTabIndicatorHeight(0);
        mTab.setOnTabSelectedListener(this);
        mTab.getTabAt(1).select();

        mSearch.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == mSearch){
            SearchActivity.open(getActivity());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        if (mPager != null) mPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
