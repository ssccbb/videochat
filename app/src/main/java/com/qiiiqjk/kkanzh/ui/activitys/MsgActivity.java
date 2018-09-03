package com.qiiiqjk.kkanzh.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qiiiqjk.kkanzh.common.XBaseActivity;
import com.qiiiqjk.kkanzh.R;

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

    public static void open(Context context, String tittle){
        Intent intent = new Intent(context, MsgActivity.class);
        intent.putExtra(TAG,tittle);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            this.finish();
        }
    }
}
