package com.feiyu.videochat.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.VipVideoAdapter;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.model.HotHostResults;
import com.feiyu.videochat.model.HotVideoResult;
import com.feiyu.videochat.model.HotVideoResults;
import com.feiyu.videochat.model.PhoneVertifyResult;
import com.feiyu.videochat.model.UGCVideoResult;
import com.feiyu.videochat.net.StateCode;
import com.feiyu.videochat.net.api.Api;
import com.feiyu.videochat.net.httprequest.ApiCallback;
import com.feiyu.videochat.net.httprequest.okhttp.JKOkHttpParamKey;
import com.feiyu.videochat.net.httprequest.okhttp.OkHttpRequestUtils;
import com.feiyu.videochat.ui.activitys.VideoBrowseActivity;
import com.feiyu.videochat.ui.activitys.VipVideoActivity;
import com.feiyu.videochat.views.XReloadableRecyclerContentLayout;

import java.util.ArrayList;
import java.util.List;

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
                            return;
                        }
                        next_page = Integer.valueOf(mHotData.next_page);
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
