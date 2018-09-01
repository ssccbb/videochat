package com.weiliao.kinnek.views.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.weiliao.kinnek.R;
import com.weiliao.kinnek.utils.ScreenUtils;
import com.weiliao.kinnek.utils.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by sung on 2017/12/15.
 * 共用dialog
 */

@SuppressLint("ValidFragment")
public class VCDialog extends android.support.v4.app.DialogFragment implements View.OnClickListener {
    public static final String TAG = VCDialog.class.getSimpleName();
    private onDialogActionListner onDialogActionListner;

    /**
     * type:
     * 1。有标题带内容单个确定
     * 2。无标题带内容单个确定
     * 3。有标题带内容确定取消
     * 4。无标题带内容确定取消
     * 5。有标题带内容方块形状的确定取消
     * 6。无标题带内容方块形状的确定取消
     */
    public static final int Ddialog_With_All_Single_Confirm = 0;
    public static final int Ddialog_Without_tittle_Single_Confirm = 1;
    public static final int Ddialog_With_All_Full_Confirm = 2;
    public static final int Ddialog_Without_tittle_Full_Confirm = 3;
    public static final int Ddialog_With_All_Block_Confirm = 4;
    public static final int Ddialog_Without_tittle_Block_Confirm = 5;

    private TextView mTittle;
    private TextView mContent;
    private View cancel;
    private View confirm;
    //展示类型
    private int type;

    //标题／内容
    private String tittle;
    private String content;

    @SuppressLint("ValidFragment")
    public VCDialog(int type, @Nullable String tittle, @Nullable String content) {
        this.type = type;
        if (tittle != null) this.tittle = tittle;
        if (content != null) this.content = content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resLayout = 0;
        switch (type) {
            case Ddialog_With_All_Full_Confirm:
            case Ddialog_Without_tittle_Full_Confirm:
                resLayout = R.layout.dialog_common_circle_confirm;
                break;
            case Ddialog_With_All_Single_Confirm:
            case Ddialog_Without_tittle_Single_Confirm:
            case Ddialog_With_All_Block_Confirm:
            case Ddialog_Without_tittle_Block_Confirm:
                resLayout = R.layout.dialog_common_block_confirm;
                break;
            default:
                resLayout = R.layout.dialog_common_circle_confirm;
                break;
        }
        return inflater.inflate(resLayout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.setCancelable(false);

        //两个xml内的id别改！！
        mTittle = view.findViewById(R.id.dialog_tittle);
        mContent = view.findViewById(R.id.dialog_content);
        cancel = view.findViewById(R.id.dialog_cancel);
        confirm = view.findViewById(R.id.dialog_confirm);

        if (!StringUtils.isEmpty(tittle)) mTittle.setText(tittle);
        if (!StringUtils.isEmpty(content)) mContent.setText(content);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
        switchLayoutView();
    }

    private void switchLayoutView() {
        switch (type) {
            case Ddialog_With_All_Single_Confirm://隐藏取消
                cancel.setVisibility(View.GONE);
                break;
            case Ddialog_Without_tittle_Single_Confirm://隐藏标题取消
                mTittle.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                break;
            case Ddialog_Without_tittle_Full_Confirm://隐藏标题
                mTittle.setVisibility(View.GONE);
                break;
            case Ddialog_Without_tittle_Block_Confirm://隐藏标题
                mTittle.setVisibility(View.GONE);
                break;
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
//        super.show(manager, tag);
//        mDismissed = false;
//        mShownByMe = true;
//        FragmentTransaction ft = manager.beginTransaction();
//        ft.add(this, tag);
//        // 这里吧原来的commit()方法换成了commitAllowingStateLoss()
//        ft.commitAllowingStateLoss();
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
        this.dismiss();
        if (onDialogActionListner == null) return;
        if (v == cancel) {
            onDialogActionListner.onCancel();
        }
        if (v == confirm) {
            onDialogActionListner.onConfirm();
        }
    }

    public void setCancelable(boolean cancelable){
        if (this.getDialog() == null) return;
        this.getDialog().setCancelable(cancelable);
    }

    public void addOnDialogActionListner(onDialogActionListner onDialogActionListner) {
        this.onDialogActionListner = onDialogActionListner;
    }

    public interface onDialogActionListner {
        void onCancel();

        void onConfirm();
    }
}
