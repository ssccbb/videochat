package com.feiyu.videochat.ui.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseActivity;

import butterknife.BindView;

public class MsgActivity extends XBaseActivity implements View.OnClickListener{
    public static final String TAG = MsgActivity.class.getSimpleName();

    @BindView(R.id.tittle)
    TextView mHead;
    @BindView(R.id.back)
    View mBack;

    @Override
    public void initData(Bundle savedInstanceState) {
        String from = this.getIntent().getStringExtra(TAG);
        mHead.setText(from == null ? "消息" : from);
        mBack.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_msg;
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
