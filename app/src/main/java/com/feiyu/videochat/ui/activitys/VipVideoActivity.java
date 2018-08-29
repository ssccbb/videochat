package com.feiyu.videochat.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.VipVideoAdapter;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.model.HotVideoResult;
import com.feiyu.videochat.model.HotVideoResults;
import com.feiyu.videochat.model.PhoneVertifyResult;
import com.feiyu.videochat.model.UGCVideoResult;
import com.feiyu.videochat.net.StateCode;
import com.feiyu.videochat.net.api.Api;
import com.feiyu.videochat.net.httprequest.ApiCallback;
import com.feiyu.videochat.net.httprequest.okhttp.JKOkHttpParamKey;
import com.feiyu.videochat.net.httprequest.okhttp.OkHttpRequestUtils;
import com.feiyu.videochat.utils.SharedPreUtil;
import com.feiyu.videochat.views.XReloadableRecyclerContentLayout;
import com.feiyu.videochat.views.dialog.VCDialog;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerView;
import cn.droidlover.xstatecontroller.XStateController;

/**
 * 已开通VIP视频列表页面
 * */
public class VipVideoActivity extends XBaseActivity implements XRecyclerView.OnRefreshAndLoadMoreListener, View.OnClickListener,VipVideoAdapter.OnItemClickListener{
    private VipVideoAdapter mVideoAdapter;
    public static final String TAG = VipVideoActivity.class.getSimpleName();
    private int next_page = 1;
    private String uid;

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.tittle)
    TextView mTittle;
    @BindView(R.id.list)
    XReloadableRecyclerContentLayout mList;

    @Override
    public void initData(Bundle savedInstanceState) {
        mTittle.setText("VIP视频专区");
        mBack.setOnClickListener(this);
        recyclerSet();
        uid = SharedPreUtil.getLoginInfo().uid;
        postHotList(1, uid);
    }

    private void recyclerSet(){
        if (mVideoAdapter != null) return;
        mList.showLoading();
        mVideoAdapter = new VipVideoAdapter(this,true,null);
        mVideoAdapter.addOnItemClickListener(this);
        mList.getRecyclerView().setAdapter(mVideoAdapter);
        mList.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mList.getRecyclerView().setLayoutManager(new GridLayoutManager(this,2));
        mList.getRecyclerView().setOnRefreshAndLoadMoreListener(this);
        mList.getLoadingView().setVisibility(View.GONE);
        mList.setDisplayState(XStateController.STATE_CONTENT);
    }

    /**
     * post获取热门
     * */
    private void postHotList(int page,String uid){
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/Video/vip_video",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.VIP_VIDEO, String.valueOf(page),uid),
                PhoneVertifyResult.class, this, new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        mList.refreshState(false);
                        Log.e(TAG, "onSuccess: "+(String) response );
                        HotVideoResults mHotData = new HotVideoResults((String)response);
                        if (!mHotData.code.equals(StateCode.STATE_0000)){
                            //Toast.makeText(VipVideoActivity.this, mHotData.message, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onSuccess: code == "+mHotData.code +" & msg == "+mHotData.message);
                            mList.showError();
                            return;
                        }
                        if (mHotData.list.isEmpty()){
                            //Toast.makeText(VipVideoActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
                            if (page == 1){
                                mList.showEmpty();
                            }
                            return;
                        }
                        next_page = Integer.valueOf(mHotData.next_page);
                        mVideoAdapter.addData(mHotData.list,page == 1 ? true : false);
                    }

                    @Override
                    public void onError(String err_msg) {
                        mList.refreshState(false);
                        mList.showError();
                        Log.e(TAG, "onError: "+err_msg );
                    }

                    @Override
                    public void onFailure() {
                        mList.refreshState(false);
                        mList.showError();
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    @Override
    public void onRefresh() {
        postHotList(1,uid);
    }

    @Override
    public void onLoadMore(int page) {
        postHotList(next_page,uid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedPreUtil.isVip()){
            showOpenVipDialog();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip_video;
    }

    @Override
    public Object newP() {
        return null;
    }

    private void showOpenVipDialog(){
        VCDialog dialog = new VCDialog(VCDialog.Ddialog_Without_tittle_Block_Confirm,"","成为VIP即可无限观看\nVIP专区视频");
        dialog.addOnDialogActionListner(new VCDialog.onDialogActionListner() {
            @Override
            public void onCancel() {
                mBack.performClick();
                dialog.dismissAllowingStateLoss();
            }

            @Override
            public void onConfirm() {
                dialog.dismissAllowingStateLoss();
                VipActivity.open(VipVideoActivity.this);
            }
        });
        dialog.show(getSupportFragmentManager(),VCDialog.TAG);
        dialog.setCancelable(false);
    }

    public static void open(Context context){
        context.startActivity(new Intent(context,VipVideoActivity.class));
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            this.finish();
        }
    }

    @Override
    public void onItemClick(View view, int type, HotVideoResult hotVideo) {
        List<HotVideoResult> videos = mVideoAdapter.getData();
        int position = videos.indexOf(hotVideo);
        //bean转换
        ArrayList<UGCVideoResult> datas = new ArrayList<>();
        for (HotVideoResult video : videos) {
            UGCVideoResult result = new UGCVideoResult(video);
            datas.add(result);
        }
        //跳转
        VideoBrowseActivity.open(this,position,datas.get(position),datas);
    }

    @Override
    public void onVipItemClick() {//无用
    }
}
