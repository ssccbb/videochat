package com.qiiiqjk.kkanzh.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qiiiqjk.kkanzh.common.XBaseFragment;
import com.qiiiqjk.kkanzh.model.AnchorInfoResult;
import com.qiiiqjk.kkanzh.model.LoginInfoResults;
import com.qiiiqjk.kkanzh.model.PhoneVertifyResult;
import com.qiiiqjk.kkanzh.net.StateCode;
import com.qiiiqjk.kkanzh.net.api.Api;
import com.qiiiqjk.kkanzh.net.httprequest.ApiCallback;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.JKOkHttpParamKey;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.OkHttpRequestUtils;
import com.qiiiqjk.kkanzh.ui.activitys.PicBrowseActivity;
import com.qiiiqjk.kkanzh.ui.activitys.SettingActivity;
import com.qiiiqjk.kkanzh.ui.activitys.VipActivity;
import com.qiiiqjk.kkanzh.ui.fragments.setting.ChargeFragment;
import com.qiiiqjk.kkanzh.utils.SharedPreUtil;
import com.qiiiqjk.kkanzh.views.dialog.UnlockHostChargeDialog;
import com.qiiiqjk.kkanzh.R;
import com.qiiiqjk.kkanzh.adapter.HostPicAdapter;
import com.google.gson.Gson;
import com.qiiiqjk.kkanzh.views.dialog.VCDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

public class HostPicFragment extends XBaseFragment implements View.OnClickListener,HostPicAdapter.onHostPicItemClickListener{
    public static final String TAG = HostPicFragment.class.getSimpleName();
    public static HostPicFragment instance;
    private AnchorInfoResult host = null;
    private HostPicAdapter mPicAdapter;
    private int pay_status = -1;//权限状态
    private int pay_price = -1;//解锁金额

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.open_container)
    View mOpenVIP;
    @BindView(R.id.unlock)
    TextView mUnlock;
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
            try {
                pay_price = Integer.parseInt(host.pay_diamond);
                mUnlock.setText("解锁全部/"+pay_price+"钻石");
            }catch (NumberFormatException e){
                Log.e(TAG, "checkDiamond: "+e.toString() );
            }
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

        postAnchorPayInfo(host.uid);
        mPicAdapter.setPayStatus(pay_status);
        switch2PayMode();
    }

    private void switch2PayMode(){
        if (pay_status == 1) {
            mOpenVIP.setVisibility(View.GONE);
            mUnlock.setVisibility(View.GONE);
        }else {
            mUnlock.setVisibility(View.VISIBLE);
        }
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
            showUnlockDialog();
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
                        }catch (NumberFormatException e){
                            Log.e(TAG, "onSuccess: "+e.toString() );
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

    /**
     * post解锁查看主播权限
     */
    private void postUnlock() {
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL + "/Anchor/pay_anchor",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.ANCHOR_INFO, host.uid, SharedPreUtil.getLoginInfo().uid),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            JSONObject jsonObject = new JSONObject((String) response);
                            String state = jsonObject.getString("state");
                            if (state.equals(StateCode.STATE_0000)){
                                pay_status = 1;
                                switch2PayMode();

                                //扣费
                                try {
                                    LoginInfoResults saved = SharedPreUtil.getLoginInfo();
                                    int saved_diamond = Integer.parseInt(saved.diamond);
                                    int left_diamond = saved_diamond - pay_price;
                                    if (left_diamond < 0) left_diamond = 0;
                                    saved.diamond = String.valueOf(left_diamond);
                                    SharedPreUtil.updateLoginInfo(saved);
                                }catch (NumberFormatException e){
                                    Log.e(TAG, "服务器解锁成功！ 本地扣费失败！\n"+e.toString() );
                                }

                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "onSuccess: " + (String) response);
                        Toast.makeText(getContext(), "支付失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String err_msg) {
                        Toast.makeText(getContext(), "支付失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: " + err_msg);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(), "支付失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    @Override
    public void onHostPicItemClick(String url) {
        if (pay_status != 1){
            showUnlockDialog();
            return;
        }
        ArrayList<String> pics = (ArrayList<String>) mPicAdapter.getData();
        int position = pics.indexOf(url);
        PicBrowseActivity.open(getActivity(),position,pics);
    }

    private void checkUnLockDiamond(){
        if (pay_price == -1) {
            Toast.makeText(getActivity(), "获取解锁价格失败！请退出重试", Toast.LENGTH_SHORT).show();
            return;
        }
        int diamond = 0;
        try {
            diamond = Integer.parseInt(SharedPreUtil.getLoginInfo().diamond);
        }catch (NumberFormatException e){
            Log.e(TAG, "checkDiamond: "+e.toString() );
        }
        if (diamond >= pay_price){
            postUnlock();
        }else {
            SettingActivity.open(getActivity(), ChargeFragment.TAG);
        }
    }

    private void showUnlockDialog(){
        UnlockHostChargeDialog dialog = new UnlockHostChargeDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(UnlockHostChargeDialog.TAG,UnlockHostChargeDialog.Charge_Video_Type);
        bundle.putInt(UnlockHostChargeDialog.VALUE,pay_price);
        dialog.setArguments(bundle);
        dialog.addOnDialogActionListner(new UnlockHostChargeDialog.onDialogActionListner() {
            @Override
            public void onChargeClick() {
                dialog.dismissAllowingStateLoss();
                checkUnLockDiamond();
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(),UnlockHostChargeDialog.TAG);
    }
}
