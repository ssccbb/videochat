package com.weiliao.kinnek.ui.fragments.setting;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.model.LoginInfoResults;
import com.weiliao.kinnek.model.PhoneVertifyResult;
import com.weiliao.kinnek.model.UserInfoResult;
import com.weiliao.kinnek.net.StateCode;
import com.weiliao.kinnek.net.api.Api;
import com.weiliao.kinnek.net.httprequest.ApiCallback;
import com.weiliao.kinnek.net.httprequest.okhttp.JKOkHttpParamKey;
import com.weiliao.kinnek.net.httprequest.okhttp.OkHttpRequestUtils;
import com.weiliao.kinnek.utils.SharedPreUtil;
import com.weiliao.kinnek.utils.StringUtils;
import com.weiliao.kinnek.views.CircleImageView;
import com.weiliao.kinnek.views.dialog.UserChooseDialog;
import com.weiliao.kinnek.views.dialog.UserEditDialog;
import com.weiliao.kinnek.views.dialog.VCDialog;
import com.weiliao.kinnek.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

public class UserInfoEditFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = UserInfoEditFragment.class.getSimpleName();
    public static UserInfoEditFragment instance;
    private LoginInfoResults mLoginUser;
    private UserInfoResult user;
    private boolean is_edit = false;

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.tittle)
    TextView mTIttle;
    @BindView(R.id.right_text_tool)
    View mTools;
    @BindView(R.id.pb_loading)
    ProgressBar loading;

    @BindView(R.id.item_avatar)
    View mItemAvatar;
    @BindView(R.id.item_id)
    View mItemId;
    @BindView(R.id.item_nickname)
    View mItemNickname;
    @BindView(R.id.item_city)
    View mItemCity;
    @BindView(R.id.item_age)
    View mItemAge;
    @BindView(R.id.item_sex)
    View mItemSex;
    @BindView(R.id.item_motion)
    View mItemMotion;
    @BindView(R.id.item_sign)
    View mItemSign;

    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.id)
    TextView mId;
    @BindView(R.id.nickname)
    TextView mNcikname;
    @BindView(R.id.city)
    TextView mCity;
    @BindView(R.id.age)
    TextView mAge;
    @BindView(R.id.sex)
    TextView mSex;
    @BindView(R.id.motion)
    TextView mMotion;
    @BindView(R.id.desc)
    TextView mDesc;

    public static UserInfoEditFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new UserInfoEditFragment();
    }

    public UserInfoEditFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mTIttle.setText("资料编辑");
        mTools.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(this::onClick);
        mTools.setOnClickListener(this::onClick);
        mItemAvatar.setOnClickListener(this::onClick);
        mItemId.setOnClickListener(this::onClick);
        mItemNickname.setOnClickListener(this::onClick);
        mItemCity.setOnClickListener(this::onClick);
        mItemAge.setOnClickListener(this::onClick);
        mItemSex.setOnClickListener(this::onClick);
        mItemMotion.setOnClickListener(this::onClick);
        mItemSign.setOnClickListener(this::onClick);

        mLoginUser = SharedPreUtil.getLoginInfo();
        if (mLoginUser == null) return;
        mId.setText(mLoginUser.user_id);
        postUserInfo(mLoginUser.uid);
    }

    /**
     * post获取用户
     * */
    private void postUserInfo(String uid){
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/user/get_info",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.GET_USER_INFO, uid),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e(TAG, "onSuccess: "+(String) response );
                        user = new UserInfoResult((String) response);
                        if (!user.code.equals(StateCode.STATE_0000)){
                            Toast.makeText(getActivity(), user.message, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        bindUiData();
                    }

                    @Override
                    public void onError(String err_msg) {
                        Toast.makeText(getActivity(), "用户拉取失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: "+err_msg );
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getActivity(), "用户拉取失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    /**
     * post获取用户
     * */
    private void postUpdateUserInfo(String uid,String nickname,String city,String age,String sex,String signature,String emotion){
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/user/update_info",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.UPDATE_USER_INFO, uid,nickname,city,age,sex,signature,emotion),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        loading.setVisibility(View.GONE);
                        Log.e(TAG, "onSuccess: "+(String) response );
                        try {
                            JSONObject json = new JSONObject((String)response);
                            String state = json.getString("state");
                            if (state.equals(StateCode.STATE_0000)){
                                Toast.makeText(getActivity(), "提交成功！", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }else {
                                String msg = json.getString("msg");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String err_msg) {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "提交失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: "+err_msg );
                    }

                    @Override
                    public void onFailure() {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "提交失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    private void bindUiData(){
        mId.setText(user.user_id);
        mNcikname.setText(user.nickname);
        mCity.setText(user.city);
        mAge.setText(user.age);
        String sex = "未知";
        if (user.sex.equals("1")){
            sex = "男";
        }
        if (user.sex.equals("2")){
            sex = "女";
        }
        mSex.setText(sex);
        String motion = "保密";
        if (user.emotion.equals("0")){
            motion = "保密";
        }
        if (user.emotion.equals("1")){
            motion = "单身";
        }
        if (user.emotion.equals("2")){
            motion = "恋爱中";
        }
        if (user.emotion.equals("3")){
            motion = "已婚";
        }
        mMotion.setText(motion);
        mDesc.setText(user.signature);
    }

    private void showEditDialog(TextView desc){
        is_edit = true;
        int type = ((desc == mAge) ? InputType.TYPE_CLASS_NUMBER : InputType.TYPE_CLASS_TEXT);
        UserEditDialog dialog = new UserEditDialog(desc.getText().toString().trim(),type);
        dialog.addOnDismissListener(new UserEditDialog.onDismissListener() {
            @Override
            public void onDialogDismiss(String content) {
                if (!StringUtils.isEmpty(content)) {
                    desc.setText(content);
                }
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), UserEditDialog.TAG);
    }

    private void showChooseDialog(TextView desc, ArrayList<String> data){
        is_edit = true;
        UserChooseDialog dialog = new UserChooseDialog(data);
        dialog.addOnDismissListener(new UserChooseDialog.onDismissListener() {
            @Override
            public void onDialogDismiss(String content) {
                if (!StringUtils.isEmpty(content)) {
                    desc.setText(content);
                }
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), UserChooseDialog.TAG);
    }

    private void showExitDialog(){
        VCDialog dialog = new VCDialog(VCDialog.Ddialog_Without_tittle_Block_Confirm,"","编辑内容未保存，退出将不保存\n确定要退出吗？");
        dialog.addOnDialogActionListner(new VCDialog.onDialogActionListner() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm() {
                getActivity().finish();
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(),VCDialog.TAG);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_info_edit;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            if (is_edit){
                showExitDialog();
                return;
            }
            getActivity().finish();
        }
        if (v == mTools){
            loading.setVisibility(View.VISIBLE);
            String nickname = mNcikname.getText().toString().trim();
            String city = mCity.getText().toString().trim();
            String age = mAge.getText().toString().trim();
            String sex = mSex.getText().toString().trim();
            if (sex.equals("男")){
                sex = "1";
            }
            if (sex.equals("女")){
                sex = "2";
            }
            if (sex.equals("未知")){
                sex = "0";
            }
            String signature = mDesc.getText().toString().trim();
            String emotion = mMotion.getText().toString().trim();
            if (emotion.equals("单身")){
                emotion = "1";
            }
            if (emotion.equals("已婚")){
                emotion = "3";
            }
            if (emotion.equals("保密")){
                emotion = "0";
            }
            if (emotion.equals("恋爱中")){
                emotion = "2";
            }
            postUpdateUserInfo(user.uid,nickname,city,age,sex,signature,emotion);
        }
        if (v == mItemAvatar){

        }
        if (v == mItemNickname){
            showEditDialog(mNcikname);
        }
        if (v == mItemCity){
            showEditDialog(mCity);
        }
        if (v == mItemAge){
            showEditDialog(mAge);
        }
        if (v == mItemSex){
            ArrayList<String> sex = new ArrayList<>();
            sex.add("男");
            sex.add("女");
            sex.add("未知");
            showChooseDialog(mSex,sex);
        }
        if (v == mItemMotion){
            ArrayList<String> motion = new ArrayList<>();
            motion.add("单身");
            motion.add("恋爱中");
            motion.add("已婚");
            motion.add("未知");
            showChooseDialog(mMotion,motion);
        }
        if (v == mItemSign){
            showEditDialog(mDesc);
        }
    }
}
