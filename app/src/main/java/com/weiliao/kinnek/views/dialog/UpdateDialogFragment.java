package com.weiliao.kinnek.views.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weiliao.kinnek.App;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.model.CheckUpdateResult;
import com.weiliao.kinnek.model.LoginInfoResults;
import com.weiliao.kinnek.model.PhoneVertifyResult;
import com.weiliao.kinnek.net.StateCode;
import com.weiliao.kinnek.net.api.Api;
import com.weiliao.kinnek.net.httprequest.ApiCallback;
import com.weiliao.kinnek.net.httprequest.okhttp.JKOkHttpParamKey;
import com.weiliao.kinnek.net.httprequest.okhttp.OkHttpRequestUtils;
import com.weiliao.kinnek.utils.ScreenUtils;
import com.weiliao.kinnek.utils.SharedPreUtil;
import com.weiliao.kinnek.utils.StringUtils;
import com.weiliao.kinnek.utils.Utils;

public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG = UpdateDialogFragment.class.getSimpleName();
    private onClickListener onClickListener;
    private Context mContext;
    private CheckUpdateResult update;
    private TextView content,cancel,ok;

    public UpdateDialogFragment() {
        mContext = App.getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_update,container,false);
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
        this.getDialog().setCancelable(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(this.getArguments() == null){
            this.dismissAllowingStateLoss();
            return;
        }

        update = (CheckUpdateResult) this.getArguments().getSerializable(TAG);
        content = view.findViewById(R.id.content);
        cancel = view.findViewById(R.id.cancel);
        ok = view.findViewById(R.id.update);

        String desc = update.desc +"\n\n";
        desc = desc + "版本号："+update.version_outer+"\n";
        desc = desc + "更新说明：\n"+update.info;
        content.setText(desc);
        if (update.version.equals("0")){
            cancel.setVisibility(View.GONE);
        }
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (onClickListener == null) return;
        if (v == cancel){
            onClickListener.onCancelCallback();
        }
        if (v == ok){
            onClickListener.onConfirmCallback();
        }
    }

    public interface onClickListener {
        void onConfirmCallback();
        void onCancelCallback();
    }

    public void addOnClickListener(onClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
}
