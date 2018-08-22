package com.feiyu.videochat.ui.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.HomeHotBannerAdapter;
import com.feiyu.videochat.adapter.HomeHotVideoAdapter;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.model.HotVideoResults;
import com.feiyu.videochat.model.PhoneVertifyResult;
import com.feiyu.videochat.net.api.Api;
import com.feiyu.videochat.net.httprequest.ApiCallback;
import com.feiyu.videochat.net.httprequest.okhttp.CommonOkHttpCallBack;
import com.feiyu.videochat.net.httprequest.okhttp.JKOkHttpParamKey;
import com.feiyu.videochat.net.httprequest.okhttp.OkHttpRequestUtils;
import com.feiyu.videochat.ui.activitys.HostInfoActivity;
import com.feiyu.videochat.views.XReloadableRecyclerContentLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerView;

public class HotPagerFragment extends XBaseFragment implements XRecyclerView.OnRefreshAndLoadMoreListener,HomeHotVideoAdapter.OnItemClickListener{
    @BindView(R.id.list)
    XReloadableRecyclerContentLayout mList;

    private static final String TAG = HotPagerFragment.class.getSimpleName();
    public static HotPagerFragment instance;
    private HomeHotVideoAdapter mHotAdapter;

    public static HotPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new HotPagerFragment();
    }

    public HotPagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mList.showLoading();
        List<HotVideoResults> data = new ArrayList();
        for (int i = 0; i < 20; i++) {
            data.add(new HotVideoResults(i));
        }
        List bannerData = new ArrayList();
        bannerData.add(0);
        bannerData.add(1);
        bannerData.add(2);
        bannerData.add(3);
        HomeHotBannerAdapter adapter = new HomeHotBannerAdapter(App.getContext(),bannerData);
        mHotAdapter = new HomeHotVideoAdapter(getActivity(),null);
        mHotAdapter.setBannerAdapter(adapter);
        mList.getRecyclerView().setAdapter(mHotAdapter);
        mList.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        mList.getRecyclerView().setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? manager.getSpanCount() : 1;
            }
        });
        mHotAdapter.addData(data,true);
        mList.getRecyclerView().setOnRefreshAndLoadMoreListener(this);
        mList.getLoadingView().setVisibility(View.GONE);
        mHotAdapter.addOnItemClickListener(this);
    }

    /**
     * retrofit获取验证码（当前body未过加密封装，会报参数不足）
     * */
    private void retrofitVertifyCode() {
        Api api = new Api();
        api.getVerifyCode("18600574847", getActivity(), new ApiCallback<PhoneVertifyResult>() {
            @Override
            public void onSuccess(PhoneVertifyResult response) {
                Log.e(TAG, "onSuccess: "+response.dataInfo );
            }

            @Override
            public void onError(String err_msg) {
                Log.e(TAG, "onError: "+err_msg );
            }

            @Override
            public void onFailure() {
                Log.e(TAG, "onFailure !");
            }
        });
    }

    /**
     * get获取验证码
     * */
    private void getVertifyCode(){
        OkHttpRequestUtils.getInstance().requestByGet(Api.API_BASE_URL +"/user/send_verification_code",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.PHONE_VERTIFY_CODE, "18600574847"),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e(TAG, "onSuccess: "+(String) response );
                    }

                    @Override
                    public void onError(String err_msg) {
                        Log.e(TAG, "onError: "+err_msg );
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    /**
     * get获取banner
     * */
    private void getBanner(){
        OkHttpRequestUtils.getInstance().requestByGet(Api.API_BASE_URL +"/Index/start_advertisement", null,
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e(TAG, "onSuccess: "+(String) response );
                    }

                    @Override
                    public void onError(String err_msg) {
                        Log.e(TAG, "onError: "+err_msg );
                    }

                    @Override
                    public void onFailure() {
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position, HotVideoResults hotVideo) {
        startActivity(new Intent(getActivity(), HostInfoActivity.class));
    }

    @Override
    public void onRefresh() {
        mList.refreshState(false);
    }

    @Override
    public void onLoadMore(int page) {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_child_hot;
    }

    @Override
    public Object newP() {
        return null;
    }
}
