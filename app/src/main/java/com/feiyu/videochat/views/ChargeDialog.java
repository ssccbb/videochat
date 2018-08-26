package com.feiyu.videochat.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.feiyu.videochat.R;
import com.feiyu.videochat.utils.StringUtils;

/**
 * Created by sun on 21/12/2017.
 */

public class ChargeDialog extends DialogFragment implements View.OnClickListener{
    public static final String TAG = ChargeDialog.class.getSimpleName();
    private onChargeClickListener onChargeClickListener;
    private View mCancel,mAlipay;
    private TextView mPrice;
    private int charge_value = 0;


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout(dm.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Theme_FullScreenDialog);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View inflate = layoutInflater.inflate(R.layout.view_charge_dialog, null);
        dialog.setContentView(inflate);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        //window.setWindowAnimations(R.style.BottomDialogAnimation);

        mCancel = inflate.findViewById(R.id.cancel);
        mAlipay = inflate.findViewById(R.id.alipay);
        mPrice = inflate.findViewById(R.id.price);

        mCancel.setOnClickListener(this);
        mAlipay.setOnClickListener(this);

        charge_value = this.getArguments().getInt(TAG,0);
        mPrice.setText(charge_value+".00");
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }

    public void addOnChargeClickListener(onChargeClickListener onChargeClickListener){
        this.onChargeClickListener = onChargeClickListener;
    }

    @Override
    public void onClick(View v) {
        if (v == mCancel){
            dismissAllowingStateLoss();
        }
        if (v == mAlipay && onChargeClickListener != null){
            onChargeClickListener.onChargeClick(charge_value);
        }
    }

    public interface onChargeClickListener{
        void onChargeClick(int charge_value);
    }
}
