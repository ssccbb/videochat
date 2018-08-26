package com.feiyu.videochat.ui.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.views.ChargeDialog;

import butterknife.BindView;

/**
 * VIP开通页面
 * */
public class VipActivity extends XBaseActivity implements View.OnClickListener{
    public static final String TAG = VipActivity.class.getSimpleName();

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.tittle)
    TextView mTittle;
    @BindView(R.id.open_vip)
    View open;

    @Override
    public void initData(Bundle savedInstanceState) {
        mTittle.setText("VIP中心");
        mBack.setOnClickListener(this);
        open.setOnClickListener(this);
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
        if (v == open){
            showChargeDialog(58);
        }
    }

    private void showChargeDialog(int price){
        ChargeDialog chargeDialog = new ChargeDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(ChargeDialog.TAG, price);
        chargeDialog.addOnChargeClickListener(new ChargeDialog.onChargeClickListener() {
            @Override
            public void onChargeClick(int charge_value) {
                chargeDialog.dismissAllowingStateLoss();
                Toast.makeText(VipActivity.this, ""+charge_value, Toast.LENGTH_SHORT).show();
            }
        });
        chargeDialog.setArguments(bundle);
        chargeDialog.show(this.getSupportFragmentManager(), ChargeDialog.TAG);
    }
}
