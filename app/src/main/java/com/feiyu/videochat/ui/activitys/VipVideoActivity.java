package com.feiyu.videochat.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseActivity;

import butterknife.BindView;

public class VipVideoActivity extends XBaseActivity implements View.OnClickListener{
    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.tittle)
    TextView mTittle;

    @Override
    public void initData(Bundle savedInstanceState) {
        mTittle.setText("VIP视频专区");
        mBack.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip_video;
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
