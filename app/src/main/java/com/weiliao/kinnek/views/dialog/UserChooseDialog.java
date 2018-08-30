package com.weiliao.kinnek.views.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.weiliao.kinnek.R;
import com.weiliao.kinnek.utils.ScreenUtils;
import com.weiliao.kinnek.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 查看主播图片/视频余额不足弹窗
 * */
@SuppressLint("ValidFragment")
public class UserChooseDialog extends DialogFragment implements View.OnClickListener  {
    public static final String TAG = UserChooseDialog.class.getSimpleName();
    private onDismissListener onDismissListener;
    private ArrayList<String> data = new ArrayList<>();
    private RecyclerView content;
    private String result = "";

    @SuppressLint("ValidFragment")
    public UserChooseDialog(ArrayList<String> data) {
        if (data != null){
            this.data.addAll(data);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_user_choose,container,false);
        content = view.findViewById(R.id.content);
        content.setLayoutManager(new LinearLayoutManager(getContext()));
        content.setItemAnimator(new DefaultItemAnimator());
        content.setAdapter(new ChooseAdapter());
        content.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.setCancelable(true);
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
        onDismissListener.onDialogDismiss(StringUtils.isEmpty(result) ? "" : result);
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

    class ChooseAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_choose_item, parent, false);
            return new ChooseHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ChooseHolder chooseHolder = (ChooseHolder) holder;
            chooseHolder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ChooseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public TextView text;

            public ChooseHolder(View itemView) {
                super(itemView);
                text = (TextView) itemView;
            }

            void onBind(int position){
                text.setText(data.get(position));
                text.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (v == text && onDismissListener != null){
                    result = text.getText().toString();
                    onDismissListener.onDialogDismiss(result);
                    dismissAllowingStateLoss();
                }
            }
        }
    }
}
