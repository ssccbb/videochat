package com.feiyu.videochat.ui.activitys;

import android.os.Bundle;
import android.view.View;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseActivity;

import butterknife.BindView;

public class SearchActivity extends XBaseActivity implements View.OnClickListener{
    @BindView(R.id.back)
    View mBack;

    @Override
    public void initData(Bundle savedInstanceState) {
        mBack.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            this.finish();
        }
    }
}
