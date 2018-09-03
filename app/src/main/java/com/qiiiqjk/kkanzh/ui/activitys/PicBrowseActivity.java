package com.qiiiqjk.kkanzh.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.qiiiqjk.kkanzh.adapter.PicBrowseAdapter;
import com.qiiiqjk.kkanzh.R;
import com.qiiiqjk.kkanzh.common.XBaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

public class PicBrowseActivity extends XBaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    public static final String TAG = PicBrowseActivity.class.getSimpleName();
    private static final String CURRENT_PAGE = "page_data";
    private static final String PICS = "pics_data";
    private int current_page = 0;
    private ArrayList<String> pics;

    @BindView(R.id.view_pager)
    ViewPager mPager;
    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.page)
    TextView mPageIndicator;

    @Override
    public void initData(Bundle savedInstanceState) {
        mBack.setOnClickListener(this);
        mPager.setOnClickListener(this);
        if (this.getIntent() == null) return;
        current_page = this.getIntent().getIntExtra(CURRENT_PAGE, 0);
        pics = this.getIntent().getStringArrayListExtra(PICS);

        if (pics.isEmpty()) return;
        mPager.setAdapter(new PicBrowseAdapter(this.getSupportFragmentManager(), pics));
        mPager.setCurrentItem(current_page);
        mPager.addOnPageChangeListener(this);
        mPageIndicator.setText(buildIndicator());
    }

    private String buildIndicator() {
        return (current_page + 1) + "/" + pics.size();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pic_browse;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context, int currentPage, ArrayList<String> pics) {
        Intent goTo = new Intent(context, PicBrowseActivity.class);
        goTo.putExtra(CURRENT_PAGE, currentPage);
        goTo.putStringArrayListExtra(PICS, pics);
        context.startActivity(goTo);
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            this.finish();
        }
        if (v == mPager) {
            this.finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        current_page = position;
        mPageIndicator.setText(buildIndicator());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
