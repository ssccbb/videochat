package com.feiyu.videochat.ui.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseActivity;

import butterknife.BindView;

public class VipActivity extends XBaseActivity implements View.OnClickListener{
    public static final String TAG = VipActivity.class.getSimpleName();

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.tittle)
    TextView mTittle;

    @Override
    public void initData(Bundle savedInstanceState) {
        mTittle.setText("VIP中心");
        mBack.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context){
        App.getContext().startActivity(new Intent(context,VipActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            this.finish();
        }
    }
}
