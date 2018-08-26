package com.feiyu.videochat.ui.fragments.home;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.HomeHotBannerAdapter;
import com.feiyu.videochat.adapter.HomeHotHostAdapter;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.model.HotHostResults;
import com.feiyu.videochat.model.PhoneVertifyResult;
import com.feiyu.videochat.net.StateCode;
import com.feiyu.videochat.net.api.Api;
import com.feiyu.videochat.net.httprequest.ApiCallback;
import com.feiyu.videochat.net.httprequest.okhttp.JKOkHttpParamKey;
import com.feiyu.videochat.net.httprequest.okhttp.OkHttpRequestUtils;
import com.feiyu.videochat.ui.activitys.HostInfoActivity;
import com.feiyu.videochat.views.XReloadableRecyclerContentLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerView;

public class HotPagerFragment extends XBaseFragment implements XRecyclerView.OnRefreshAndLoadMoreListener,HomeHotHostAdapter.OnItemClickListener{
    @BindView(R.id.list)
    XReloadableRecyclerContentLayout mList;

    private static final String TAG = HotPagerFragment.class.getSimpleName();
    public static HotPagerFragment instance;
    private HomeHotHostAdapter mHotAdapter;
    private int next_page = 0;

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
        List bannerData = new ArrayList();
        bannerData.add(0);
        bannerData.add(1);
        bannerData.add(2);
        bannerData.add(3);
        HomeHotBannerAdapter adapter = new HomeHotBannerAdapter(App.getContext(),bannerData);
        mHotAdapter = new HomeHotHostAdapter(getActivity(),null);
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
        mHotAdapter.addData(null,true);
        mList.getRecyclerView().setOnRefreshAndLoadMoreListener(this);
        mList.getLoadingView().setVisibility(View.GONE);
        mHotAdapter.addOnItemClickListener(this);

        postHotList(1);
    }

    /**
     * post获取热门
     * */
    private void postHotList(int page){
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/Page/hot_list",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.SINGLE_PAGE, String.valueOf(page)),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        mList.refreshState(false);
                        Log.e(TAG, "onSuccess: "+(String) response );
                        HotHostResults mHotData = new HotHostResults((String)response);
                        if (!mHotData.code.equals(StateCode.STATE_0000)){
                            Toast.makeText(getActivity(), mHotData.message, Toast.LENGTH_SHORT).show();
                            mList.showError();
                            return;
                        }
                        if (mHotData.list.isEmpty()){
                            Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                            if (page == 1){
                                mList.showEmpty();
                            }
                            return;
                        }
                        next_page = Integer.valueOf(mHotData.next_page);
                        mHotAdapter.addData(mHotData.list,page == 1 ? true : false);
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
    public void onItemClick(View view, int position, HotHostResults.HotHostResult hotHostResult) {
        HostInfoActivity.open(getActivity(),hotHostResult);
    }

    @Override
    public void onRefresh() {
        postHotList(1);
    }

    @Override
    public void onLoadMore(int page) {
        Log.e(TAG, "onLoadMore: "+page );
        postHotList(next_page);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_child_hot;
    }

    @Override
    public Object newP() {
        return null;
    }


    // TODO: 2018/8/24  for test
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
}
