package com.weiliao.kinnek.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weiliao.kinnek.adapter.VipVideoAdapter;
import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.model.HotVideoResult;
import com.weiliao.kinnek.model.HotVideoResults;
import com.weiliao.kinnek.model.PhoneVertifyResult;
import com.weiliao.kinnek.model.UGCVideoResult;
import com.weiliao.kinnek.net.StateCode;
import com.weiliao.kinnek.net.api.Api;
import com.weiliao.kinnek.net.httprequest.ApiCallback;
import com.weiliao.kinnek.net.httprequest.okhttp.JKOkHttpParamKey;
import com.weiliao.kinnek.net.httprequest.okhttp.OkHttpRequestUtils;
import com.weiliao.kinnek.ui.activitys.VideoBrowseActivity;
import com.weiliao.kinnek.views.XReloadableRecyclerContentLayout;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.ui.activitys.VipVideoActivity;

import java.util.ArrayList;

import butterknife.BindView;
import cn.droidlover.xrecyclerview.XRecyclerView;
import cn.droidlover.xstatecontroller.XStateController;

public class VideoPagerFragment extends XBaseFragment implements XRecyclerView.OnRefreshAndLoadMoreListener,
        VipVideoAdapter.OnItemClickListener {
    public static final String TAG = VideoPagerFragment.class.getSimpleName();
    public static VideoPagerFragment instance;
    private VipVideoAdapter mVideoAdapter;
    private int next_page = 1;

    @BindView(R.id.text)
    TextView mHeadText;
    @BindView(R.id.list)
    XReloadableRecyclerContentLayout mList;

    public static VideoPagerFragment newInstance() {
        if (instance != null) {
            return instance;
        }
        return instance = new VideoPagerFragment();
    }

    public VideoPagerFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mHeadText.setText("视频专区");
        recyclerSet();
        postHotList(1);
    }

    private void recyclerSet() {
        if (mVideoAdapter != null) return;
        mList.showLoading();
        mVideoAdapter = new VipVideoAdapter(getActivity(), false, null);
        mVideoAdapter.addOnItemClickListener(this);
        mList.getRecyclerView().setAdapter(mVideoAdapter);
        mList.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        mList.getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mList.getRecyclerView().setOnRefreshAndLoadMoreListener(this);
        mList.getRecyclerView().useDefLoadMoreView();
        mList.getLoadingView().setVisibility(View.GONE);
        mList.setDisplayState(XStateController.STATE_CONTENT);
    }

    /**
     * post获取热门
     */
    private void postHotList(int page) {
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL + "/Video/video_list",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.SINGLE_PAGE, String.valueOf(page)),
                PhoneVertifyResult.class, getActivity(), new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        mList.refreshState(false);
                        Log.e(TAG, "onSuccess: " + (String) response);
                        HotVideoResults mHotData = new HotVideoResults((String) response);
                        if (!mHotData.code.equals(StateCode.STATE_0000)) {
                            Toast.makeText(getActivity(), mHotData.message, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onSuccess: code == " + mHotData.code + " & msg == " + mHotData.message);
                            mList.showError();
                            return;
                        }
                        if (mHotData.list.isEmpty()) {
                            Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
                            if (page == 1) {
                                mList.showEmpty();
                            }
                            mList.getRecyclerView().removeAllFootView();
                            return;
                        }
                        next_page = Integer.valueOf(mHotData.next_page);
                        mList.getRecyclerView().setPage(page,next_page);
                        mVideoAdapter.addData(mHotData.list, page == 1 ? true : false);
                    }

                    @Override
                    public void onError(String err_msg) {
                        mList.refreshState(false);
                        mList.showError();
                        Log.e(TAG, "onError: " + err_msg);
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
    public void onItemClick(View view, int type, HotVideoResult hotVideo) {
        //不可直接处理原数据 否则会导致返回树目不对
        ArrayList<HotVideoResult> videos = new ArrayList<HotVideoResult>();
        videos = (ArrayList<HotVideoResult>) ((ArrayList<HotVideoResult>) mVideoAdapter.getData()).clone();
        videos.remove(0);
        int position = videos.indexOf(hotVideo);
        //bean转换
        ArrayList<UGCVideoResult> datas = new ArrayList<>();
        for (HotVideoResult video : videos) {
            UGCVideoResult result = new UGCVideoResult(video);
            datas.add(result);
        }
        //跳转
        VideoBrowseActivity.open(getActivity(), position, datas.get(position), datas);
    }

    @Override
    public void onVipItemClick() {
        VipVideoActivity.open(getActivity());
    }

    @Override
    public void onRefresh() {
        postHotList(1);
    }

    @Override
    public void onLoadMore(int page) {
        postHotList(next_page);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    public Object newP() {
        return null;
    }
}
