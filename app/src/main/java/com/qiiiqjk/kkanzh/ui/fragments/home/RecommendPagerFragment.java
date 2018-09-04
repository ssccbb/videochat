package com.qiiiqjk.kkanzh.ui.fragments.home;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qiiiqjk.kkanzh.adapter.HomeHotBannerAdapter;
import com.qiiiqjk.kkanzh.adapter.HomeHotHostAdapter;
import com.qiiiqjk.kkanzh.adapter.RecommendAdapter;
import com.qiiiqjk.kkanzh.common.XBaseFragment;
import com.qiiiqjk.kkanzh.model.BannerResults;
import com.qiiiqjk.kkanzh.model.HotHostResults;
import com.qiiiqjk.kkanzh.model.HotVideoResult;
import com.qiiiqjk.kkanzh.model.PhoneVertifyResult;
import com.qiiiqjk.kkanzh.net.StateCode;
import com.qiiiqjk.kkanzh.net.api.Api;
import com.qiiiqjk.kkanzh.net.httprequest.ApiCallback;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.JKOkHttpParamKey;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.OkHttpRequestUtils;
import com.qiiiqjk.kkanzh.ui.activitys.HostInfoActivity;
import com.qiiiqjk.kkanzh.ui.fragments.LoginDialogFragment;
import com.qiiiqjk.kkanzh.utils.SharedPreUtil;
import com.qiiiqjk.kkanzh.views.StateView;
import com.qiiiqjk.kkanzh.views.XReloadableRecyclerContentLayout;
import com.qiiiqjk.kkanzh.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerView;
import cn.droidlover.xstatecontroller.XStateController;

public class RecommendPagerFragment extends XBaseFragment implements XRecyclerView.OnRefreshAndLoadMoreListener,HomeHotHostAdapter.OnItemClickListener {
    public static final String TAG = RecommendPagerFragment.class.getSimpleName();
    public static RecommendPagerFragment instance;
    private HomeHotHostAdapter mHotAdapter;
    private int next_page = 0;

    @BindView(R.id.list)
    XReloadableRecyclerContentLayout mList;

    public static RecommendPagerFragment newInstance(){
        if (instance != null){
            return instance;
        }
        return instance = new RecommendPagerFragment();
    }

    public RecommendPagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mHotAdapter = new HomeHotHostAdapter(getActivity(),null);
        mList.getRecyclerView().setAdapter(mHotAdapter);
        mList.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        mList.getRecyclerView().setLayoutManager(manager);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //头部banner和底部loadmore占两格
                return (position == mHotAdapter.getItemCount() ) ? manager.getSpanCount() : 1;
            }
        });
        mHotAdapter.addData(null,true);
        mList.getRecyclerView().setOnRefreshAndLoadMoreListener(this);
        mList.getRecyclerView().useDefLoadMoreView();
        mHotAdapter.addOnItemClickListener(this);
        postHotList(1);
    }

    /**
     * post获取热门
     * */
    private void postHotList(int page){
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL +"/Page/hot_list",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.RECOMMEND_LIST, String.valueOf(page), "1"),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        mList.refreshState(false);
                        //Log.e(TAG, "onSuccess: "+(String) response );
                        HotHostResults mHotData = new HotHostResults((String)response);
                        if (!mHotData.code.equals(StateCode.STATE_0000)){
                            Toast.makeText(getActivity(), mHotData.message, Toast.LENGTH_SHORT).show();
                            mList.showError();
                            return;
                        }
                        if (mHotData.list.isEmpty()){
                            //Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                            if (page == 1){
                                mList.showEmpty();
                            }
                            mList.getRecyclerView().removeAllFootView();
                            return;
                        }
                        mList.showContent();
                        next_page = Integer.valueOf(mHotData.next_page);
                        mList.getRecyclerView().setPage(page,next_page);
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
        if (!SharedPreUtil.isLogin()){
            LoginDialogFragment login = new LoginDialogFragment();
            login.show(getActivity().getSupportFragmentManager(),LoginDialogFragment.TAG);
            return;
        }
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
        return R.layout.fragment_home_child_recommend;
    }

    @Override
    public Object newP() {
        return null;
    }
}
