package com.feiyu.videochat.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseActivity;

import butterknife.BindView;

public class HostInfoActivity extends XBaseActivity implements View.OnClickListener{
    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.connect)
    View mConnect;

    @Override
    public void initData(Bundle savedInstanceState) {
        mBack.setOnClickListener(this);
        mConnect.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_host_info;
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
        if (v == mConnect){
            startActivity(new Intent(this,ChatActivity.class));
        }
    }
}
