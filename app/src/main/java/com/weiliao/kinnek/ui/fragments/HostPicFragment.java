package com.weiliao.kinnek.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.model.AnchorInfoResult;
import com.weiliao.kinnek.model.PhoneVertifyResult;
import com.weiliao.kinnek.net.api.Api;
import com.weiliao.kinnek.net.httprequest.ApiCallback;
import com.weiliao.kinnek.net.httprequest.okhttp.JKOkHttpParamKey;
import com.weiliao.kinnek.net.httprequest.okhttp.OkHttpRequestUtils;
import com.weiliao.kinnek.ui.activitys.PicBrowseActivity;
import com.weiliao.kinnek.ui.activitys.SettingActivity;
import com.weiliao.kinnek.ui.activitys.VipActivity;
import com.weiliao.kinnek.ui.fragments.setting.ChargeFragment;
import com.weiliao.kinnek.utils.SharedPreUtil;
import com.weiliao.kinnek.views.dialog.UnlockHostChargeDialog;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.adapter.HostPicAdapter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

public class HostPicFragment extends XBaseFragment implements View.OnClickListener,HostPicAdapter.onHostPicItemClickListener{
    public static final String TAG = HostPicFragment.class.getSimpleName();
    public static HostPicFragment instance;
    private AnchorInfoResult host = null;
    private HostPicAdapter mPicAdapter;
    private int pay_status = -1;

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.open_container)
    View mOpenVIP;
    @BindView(R.id.unlock)
    View mUnlock;
    @BindView(R.id.list)
    RecyclerView mList;

    public static HostPicFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new HostPicFragment();
    }

    public HostPicFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mBack.setOnClickListener(this::onClick);
        mOpenVIP.setOnClickListener(this::onClick);
        mUnlock.setOnClickListener(this::onClick);
        if (getArguments() != null) {
            Gson gson = new Gson();
            host = gson.fromJson(getArguments().getString(TAG),AnchorInfoResult.class);
            pay_status = host.pay_status;
            initPicList();
        }
    }

    private void initPicList() {
        mPicAdapter = new HostPicAdapter(host.pic_list);
        mPicAdapter.addOnHostPicItemClickListener(this);
        GridLayoutManager mannager = new GridLayoutManager(getContext(),3);
        mList.setLayoutManager(mannager);
        mList.setAdapter(mPicAdapter);
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setHasFixedSize(true);

        if (pay_status == -1){
            postAnchorPayInfo(host.uid);
            return;
        }
        mPicAdapter.setPayStatus(pay_status);
        switch2PayMode();
    }

    private void switch2PayMode(){
        if (pay_status != 1) return;
        mOpenVIP.setVisibility(View.GONE);
        mUnlock.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_host_pics;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            getActivity().finish();
        }
        if (v == mOpenVIP){
            VipActivity.open(getActivity());
        }
        if (v == mUnlock){
            if (v == mUnlock){
                UnlockHostChargeDialog dialog = new UnlockHostChargeDialog();
                Bundle bundle = new Bundle();
                bundle.putInt(UnlockHostChargeDialog.TAG,UnlockHostChargeDialog.Charge_Pic_Type);
                dialog.setArguments(bundle);
                dialog.addOnDialogActionListner(new UnlockHostChargeDialog.onDialogActionListner() {
                    @Override
                    public void onChargeClick() {
                        SettingActivity.open(getActivity(), ChargeFragment.TAG);
                        dialog.dismissAllowingStateLoss();
                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(),UnlockHostChargeDialog.TAG);
            }
        }
    }

    /**
     * post获取用户查看主播权限
     */
    private void postAnchorPayInfo(String anchor_id) {
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL + "/Anchor/pay_info",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.ANCHOR_INFO, anchor_id, SharedPreUtil.getLoginInfo().uid),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            JSONObject jsonObject = new JSONObject((String) response);
                            String is_pay = jsonObject.getString("is_pay");
                            pay_status = Integer.parseInt(is_pay);
                            mPicAdapter.setPayStatus(pay_status);
                            switch2PayMode();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "onSuccess: " + (String) response);
                    }

                    @Override
                    public void onError(String err_msg) {
                        Log.e(TAG, "onError: " + err_msg);
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    @Override
    public void onHostPicItemClick(String url) {
        ArrayList<String> pics = (ArrayList<String>) mPicAdapter.getData();
        int position = pics.indexOf(url);
        PicBrowseActivity.open(getActivity(),position,pics);
    }
}
