package com.weiliao.kinnek.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.adapter.HostVideoAdapter;
import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.model.AnchorInfoResult;
import com.weiliao.kinnek.model.HotVideoResult;
import com.weiliao.kinnek.model.PhoneVertifyResult;
import com.weiliao.kinnek.model.UGCVideoResult;
import com.weiliao.kinnek.net.api.Api;
import com.weiliao.kinnek.net.httprequest.ApiCallback;
import com.weiliao.kinnek.net.httprequest.okhttp.JKOkHttpParamKey;
import com.weiliao.kinnek.net.httprequest.okhttp.OkHttpRequestUtils;
import com.weiliao.kinnek.ui.activitys.SettingActivity;
import com.weiliao.kinnek.ui.activitys.VideoBrowseActivity;
import com.weiliao.kinnek.ui.activitys.VipActivity;
import com.weiliao.kinnek.ui.fragments.setting.ChargeFragment;
import com.weiliao.kinnek.utils.SharedPreUtil;
import com.weiliao.kinnek.views.dialog.UnlockHostChargeDialog;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

public class HostVideoFragment extends XBaseFragment implements View.OnClickListener,HostVideoAdapter.onHostVideoItemClickListener{
    public static final String TAG = HostVideoFragment.class.getSimpleName();
    public static HostVideoFragment instance;
    private AnchorInfoResult host = null;
    private HostVideoAdapter mVideoAdapter;
    private int pay_status = -1;

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.open_container)
    View mOpenVIP;
    @BindView(R.id.unlock)
    View mUnlock;
    @BindView(R.id.list)
    RecyclerView mList;

    public static HostVideoFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new HostVideoFragment();
    }

    public HostVideoFragment() {
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
            initVideoList();
        }
    }

    private void initVideoList() {
        mVideoAdapter = new HostVideoAdapter(host.video_list);
        mVideoAdapter.addOnHostVideoItemClickListener(this);
        LinearLayoutManager mannager = new LinearLayoutManager(getContext());
        mList.setLayoutManager(mannager);
        mList.setAdapter(mVideoAdapter);
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setHasFixedSize(true);

        if (pay_status == -1){
            postAnchorPayInfo(host.uid);
            return;
        }
        mVideoAdapter.setPayStatus(pay_status);
        switch2PayMode();
    }

    private void switch2PayMode(){
        if (pay_status != 1) return;
        mOpenVIP.setVisibility(View.GONE);
        mUnlock.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_host_video;
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
            UnlockHostChargeDialog dialog = new UnlockHostChargeDialog();
            Bundle bundle = new Bundle();
            bundle.putInt(UnlockHostChargeDialog.TAG,UnlockHostChargeDialog.Charge_Video_Type);
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
                            mVideoAdapter.setPayStatus(pay_status);
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
    public void onHostVideoItemClick(AnchorInfoResult.VideoListBean bean) {
        ArrayList<AnchorInfoResult.VideoListBean> videos = (ArrayList<AnchorInfoResult.VideoListBean>) mVideoAdapter.getData();
        int position = videos.indexOf(bean);
        //bean转换
        ArrayList<UGCVideoResult> datas = new ArrayList<>();
        for (AnchorInfoResult.VideoListBean video : videos) {
            UGCVideoResult result = new UGCVideoResult(video);
            datas.add(result);
        }
        //跳转
        VideoBrowseActivity.open(getActivity(), position, datas.get(position), datas);
    }
}
