package com.qiiiqjk.kkanzh.views.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.EditText;

import com.qiiiqjk.kkanzh.utils.ScreenUtils;
import com.qiiiqjk.kkanzh.utils.StringUtils;
import com.qiiiqjk.kkanzh.R;

import java.lang.reflect.Field;

/**
 * 查看主播图片/视频余额不足弹窗
 * */
@SuppressLint("ValidFragment")
public class UserEditDialog extends DialogFragment implements View.OnClickListener  {
    public static final String TAG = UserEditDialog.class.getSimpleName();
    private onDismissListener onDismissListener;
    private EditText content;

    private String desc;
    private int inputType;

    @SuppressLint("ValidFragment")
    public UserEditDialog(String desc, int inputType) {
        this.desc = desc;
        this.inputType = inputType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_user_edit,container,false);
        content = view.findViewById(R.id.content);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.setCancelable(true);
        content.setText(desc);
        content.setInputType(inputType);
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
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener == null){
            return;
        }
        String content = this.content.getText().toString().trim();
        onDismissListener.onDialogDismiss(StringUtils.isEmpty(content) ? "" : content);
    }

    @Override
    public void onClick(View v) {
    }

    public interface onDismissListener{
        void onDialogDismiss(String content);
    }

    public void addOnDismissListener(onDismissListener onDismissListener){
        this.onDismissListener = onDismissListener;
    }
}
