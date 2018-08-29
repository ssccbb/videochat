package com.feiyu.videochat.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.HostResAdapter;
import com.feiyu.videochat.adapter.SimpleDividerDecoration;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.model.AnchorInfoResult;
import com.feiyu.videochat.model.HotHostResults;
import com.feiyu.videochat.model.PhoneVertifyResult;
import com.feiyu.videochat.model.UserInfoResult;
import com.feiyu.videochat.net.StateCode;
import com.feiyu.videochat.net.api.Api;
import com.feiyu.videochat.net.httprequest.ApiCallback;
import com.feiyu.videochat.net.httprequest.okhttp.JKOkHttpParamKey;
import com.feiyu.videochat.net.httprequest.okhttp.OkHttpRequestUtils;
import com.feiyu.videochat.ui.fragments.HostPicFragment;
import com.feiyu.videochat.ui.fragments.HostVideoFragment;
import com.feiyu.videochat.utils.SharedPreUtil;
import com.feiyu.videochat.utils.StringUtils;
import com.feiyu.videochat.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HostInfoActivity extends XBaseActivity implements View.OnClickListener {
    public static final String TAG = HostInfoActivity.class.getSimpleName();
    private HotHostResults.HotHostResult mHost;
    private AnchorInfoResult mUserInfo;
    private HostResAdapter mPicAdapter;
    private HostResAdapter mVideoAdapter;
    private int pay_status = -1;

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.connect)
    View mConnect;
    @BindView(R.id.pic_list)
    RecyclerView mPicList;
    @BindView(R.id.video_list)
    RecyclerView mVideoList;
    @BindView(R.id.item_pic)
    View mFullPics;
    @BindView(R.id.item_video)
    View mFullVideos;
    @BindView(R.id.cover)
    ImageView mCover;
    @BindView(R.id.nickname)
    TextView mName;
    @BindView(R.id.status)
    ImageView mStatus;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.info)
    TextView mInfo;//eg:25 北京市
    @BindView(R.id.num)
    TextView mIDNum;
    @BindView(R.id.follow)
    ImageView mFollow;
    @BindView(R.id.online_time)
    TextView mOnlineTime;
    @BindView(R.id.line_agree)
    TextView mLineAgree;
    @BindView(R.id.line_time)
    TextView mLineTime;
    @BindView(R.id.sign)
    TextView mSign;


    @Override
    public void initData(Bundle savedInstanceState) {
        mBack.setOnClickListener(this);
        mConnect.setOnClickListener(this);
        mFullPics.setOnClickListener(this);
        mFullVideos.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();
        mHost = (HotHostResults.HotHostResult) bundle.getSerializable(TAG);

        initPicList();
        initVideoList();
        postAnchorInfo(mHost.uid);
        postAnchorPayInfo(mHost.uid);

    }

    private void initPicList() {
        mPicAdapter = new HostResAdapter(null);
        LinearLayoutManager mannager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        mPicList.setLayoutManager(mannager);
        mPicList.setAdapter(mPicAdapter);
        mPicList.setItemAnimator(new DefaultItemAnimator());
        mVideoList.addItemDecoration(new SimpleDividerDecoration(this));
        mPicList.setHasFixedSize(true);
    }

    private void initVideoList() {
        mVideoAdapter = new HostResAdapter(null);
        LinearLayoutManager mannager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        mVideoList.setLayoutManager(mannager);
        mVideoList.setAdapter(mVideoAdapter);
        mVideoList.setItemAnimator(new DefaultItemAnimator());
        mVideoList.addItemDecoration(new SimpleDividerDecoration(this));
        mVideoList.setHasFixedSize(true);
    }

    /**
     * post获取用户
     */
    private void postAnchorInfo(String uid) {
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL + "/Anchor/anchor_info",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.ANCHOR_INFO, uid, SharedPreUtil.getLoginInfo().uid),
                PhoneVertifyResult.class, HostInfoActivity.this, new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.e(TAG, "onSuccess: " + (String) response);
                        mUserInfo = new AnchorInfoResult((String) response);
                        if (!mUserInfo.code.equals(StateCode.STATE_0000)) {
                            Toast.makeText(HostInfoActivity.this, mUserInfo.message, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        initInfo();
                    }

                    @Override
                    public void onError(String err_msg) {
                        Toast.makeText(HostInfoActivity.this, "用户拉取失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: " + err_msg);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(HostInfoActivity.this, "用户拉取失败！", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure !");
                    }
                });
    }

    private void initInfo() {
        if (mUserInfo == null) return;

        Glide.with(App.getContext()).load(StringUtils.convertUrlStr(mUserInfo.avatar))
                .crossFade()/*.thumbnail(0.1f)*/.centerCrop().into(mCover);
        mName.setText(StringUtils.isEmpty(mUserInfo.nickname) ? "" : mUserInfo.nickname);
        mStatus.setImageResource(Utils.getHostStatus(Integer.parseInt(mUserInfo.answer_state)));//1空闲 2在聊 3勿扰
        mPrice.setText(StringUtils.isEmpty(mUserInfo.fee) ? "0" : mUserInfo.fee);
        mInfo.setText( mUserInfo.city);
        mIDNum.setText("微聊ID:" + mUserInfo.user_id);
        mFollow.setSelected(false);
        mFollow.setOnClickListener(this::onClick);
        mOnlineTime.setText(mUserInfo.online_time);
        mLineAgree.setText(mUserInfo.answer_rate + "%");
        mLineTime.setText(mUserInfo.live_time);
        mSign.setText(mUserInfo.signature);

        List<AnchorInfoResult.VideoListBean> pics = new ArrayList<>();
        for (int i = 0; i < mUserInfo.pic_list.size(); i++) {
            if (i >= 5) continue;
            AnchorInfoResult.VideoListBean pic = new AnchorInfoResult.VideoListBean();
            pic.cover_url = mUserInfo.pic_list.get(i);
            pics.add(pic);
        }
        mPicAdapter.addData(pics, true);
        if (pics.isEmpty()) mFullPics.setVisibility(View.GONE);

        List<AnchorInfoResult.VideoListBean> videos = new ArrayList<>();
        for (int i = 0; i < mUserInfo.video_list.size(); i++) {
            if (i >= 5) continue;
            videos.add(mUserInfo.video_list.get(i));
        }
        mVideoAdapter.addData(videos, true);
        if (videos.isEmpty()) mFullVideos.setVisibility(View.GONE);
    }

    /**
     * post获取用户查看主播权限
     */
    private void postAnchorPayInfo(String anchor_id) {
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL + "/Anchor/pay_info",
                OkHttpRequestUtils.getInstance().JkRequestParameters(JKOkHttpParamKey.ANCHOR_INFO, anchor_id, SharedPreUtil.getLoginInfo().uid),
                PhoneVertifyResult.class, HostInfoActivity.this, new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        try {
                            JSONObject jsonObject = new JSONObject((String) response);
                            String is_pay = jsonObject.getString("is_pay");
                            pay_status = Integer.parseInt(is_pay);
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
    public int getLayoutId() {
        return R.layout.activity_host_info;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static void open(Context context, HotHostResults.HotHostResult host) {
        Intent goTo = new Intent(context, HostInfoActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(HostInfoActivity.TAG, host);
        goTo.putExtras(b);
        context.startActivity(goTo);
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            this.finish();
        }
        if (v == mConnect) {
            ChatActivity.open(this, mUserInfo);
        }
        if (v == mFollow) {
            mFollow.setSelected(!mFollow.isSelected());
        }
        if (v == mFullPics) {
            if(mUserInfo!=null){
                mUserInfo.setPay_status(pay_status);
            }
            HostResActivity.open(HostInfoActivity.this, HostPicFragment.TAG, mUserInfo);
        }
        if (v == mFullVideos) {
            if(mUserInfo!=null){
                mUserInfo.setPay_status(pay_status);
            }
            HostResActivity.open(HostInfoActivity.this, HostVideoFragment.TAG, mUserInfo);
        }
    }
}
