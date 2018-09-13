package com.weiliao.kinnek.views.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weiliao.kinnek.ui.activitys.VipActivity;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.ui.activitys.SettingActivity;
import com.weiliao.kinnek.ui.fragments.setting.ChargeFragment;
import com.weiliao.kinnek.utils.ScreenUtils;
import com.weiliao.kinnek.utils.SharedPreUtil;

import java.lang.reflect.Field;

/**
 * 查看主播图片/视频余额不足弹窗
 * */
public class UnlockHostChargeDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener  {
    public static final String TAG = UnlockHostChargeDialog.class.getSimpleName();
    public static final String VALUE = "unlock_value";
    public static final int Charge_Pic_Type = 0;
    public static final int Charge_Video_Type = 1;

    private onDialogActionListner onDialogActionListner;
    private int type = 0;//0-照片 1-视频
    private int price = -1;

    private ImageView img;
    private TextView tittle;
    private TextView desc;
    private TextView goTo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_unlock_host_charge,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.setCancelable(true);
        if(this.getArguments() == null) return;
        type = this.getArguments().getInt(TAG);
        price = this.getArguments().getInt(VALUE);

        img = view.findViewById(R.id.img);
        tittle = view.findViewById(R.id.tittle);
        desc = view.findViewById(R.id.desc);
        goTo = view.findViewById(R.id.button);
        goTo.setOnClickListener(this);

        img.setImageResource(type == Charge_Pic_Type ? R.mipmap.ic_pic_dialog : R.mipmap.ic_video_dialog);
        goTo.setText(checkDiamond() ? (price+"钻石解锁") : "余额不足 立即充值");
        tittle.setText(type == Charge_Pic_Type ? "主播私密照片" : "主播私密视频");
        desc.setText(type == Charge_Pic_Type ? "主播说这些照片尺度大，要付费才能进行观看" : "主播说这段视频尺度大，要付费才能进行观看");
    }

    private boolean checkDiamond(){
        int diamond = -1;
        try {
            diamond = Integer.parseInt(SharedPreUtil.getLoginInfo().diamond);
        }catch (NumberFormatException e){
            Log.e(TAG, "checkDiamond: "+e.toString() );
        }

        if (diamond == -1 || price == -1){
            Toast.makeText(getActivity(), "获取解锁价格失败！请退出重试", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (diamond < price){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() == null) return;
        if (!getDialog().isShowing()) return;
        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = ScreenUtils.getScreenWidth(getContext()) / 5 * 4;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            Field dismissed = DialogFragment.class.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Field shown = DialogFragment.class.getDeclaredField("mShownByMe");
            shown.setAccessible(true);
            shown.set(this, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        if (onDialogActionListner == null) return;
        if (v == goTo){
            onDialogActionListner.onChargeClick();
        }
    }

    private void goToVip(){
        VipActivity.open(getActivity());
    }

    private void goToCharge(){
        SettingActivity.open(getActivity(), ChargeFragment.TAG);
    }

    public void addOnDialogActionListner(onDialogActionListner onDialogActionListner) {
        this.onDialogActionListner = onDialogActionListner;
    }

    public interface onDialogActionListner {
        void onChargeClick();
    }
}
