package com.weiliao.kinnek.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weiliao.kinnek.App;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.common.XBaseActivity;
import com.weiliao.kinnek.views.dialog.OrderPayDialog;

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
        Intent goTo = new Intent(context, VipActivity.class);
        context.startActivity(goTo);
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
        OrderPayDialog orderPayDialog = new OrderPayDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(OrderPayDialog.TAG, price);
        orderPayDialog.addOnChargeClickListener(new OrderPayDialog.onChargeClickListener() {
            @Override
            public void onChargeClick(int charge_value) {
                orderPayDialog.dismissAllowingStateLoss();
                Toast.makeText(VipActivity.this, ""+charge_value, Toast.LENGTH_SHORT).show();
            }
        });
        orderPayDialog.setArguments(bundle);
        orderPayDialog.show(this.getSupportFragmentManager(), OrderPayDialog.TAG);
    }
}
