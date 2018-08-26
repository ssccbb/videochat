package com.feiyu.videochat.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.adapter.HostResAdapter;
import com.feiyu.videochat.adapter.SimpleDividerDecoration;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.model.HotHostResults;
import com.feiyu.videochat.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HostInfoActivity extends XBaseActivity implements View.OnClickListener {
    public static final String TAG = HostInfoActivity.class.getSimpleName();
    private HotHostResults.HotHostResult mHost;

    @BindView(R.id.back)
    View mBack;
    @BindView(R.id.connect)
    View mConnect;
    @BindView(R.id.pic_list)
    RecyclerView mPicList;
    @BindView(R.id.video_list)
    RecyclerView mVideoList;
    @BindView(R.id.cover)
    ImageView mCover;
    @BindView(R.id.nickname)
    TextView mName;
    @BindView(R.id.status)
    ImageView mStatus;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.sex)
    ImageView mSex;
    @BindView(R.id.info)
    TextView mInfo;//eg:25 北京市
    @BindView(R.id.level)
    ImageView mLevel;
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

        Bundle bundle = this.getIntent().getExtras();
        mHost = (HotHostResults.HotHostResult) bundle.getSerializable(TAG);

        initInfo();
        initPicList();
        initVideoList();
    }

    private void initInfo() {
        if (mHost == null) return;
        Glide.with(App.getContext()).load(StringUtils.convertUrlStr(mHost.avatar))
                .crossFade().thumbnail(0.1f).centerCrop().into(mCover);
        mName.setText(StringUtils.isEmpty(mHost.nickname) ? "" : mHost.nickname);
        //mStatus.setImageResource(mHost.anchor_state);//1空闲 2在聊 3勿扰
        mPrice.setText(StringUtils.isEmpty(mHost.fee) ? "0" : mHost.fee);
        //mSex.setImageResource(mHost.sex.equals("0") ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);
        mInfo.setText(mHost.age + "\t" + mHost.city);
        //mLevel.setImageResource();
        mIDNum.setText("ID:" + mHost.user_id);
        //mFollow.setImageResource();
        //mOnlineTime.setText();
        mLineAgree.setText(mHost.answer_rate + "%");
        mLineTime.setText(mHost.live_time);
        //mSign.setText(mHost.);
    }

    private void initPicList() {
        List data = new ArrayList();
        for (int i = 0; i < 6; i++) {
            data.add(i);
        }
        HostResAdapter adapter = new HostResAdapter(data);
        LinearLayoutManager mannager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        mPicList.setLayoutManager(mannager);
        mPicList.setAdapter(adapter);
        mPicList.setItemAnimator(new DefaultItemAnimator());
        mVideoList.addItemDecoration(new SimpleDividerDecoration(this));
        mPicList.setHasFixedSize(true);
    }

    private void initVideoList() {
        List data = new ArrayList();
        for (int i = 0; i < 4; i++) {
            data.add(i);
        }
        HostResAdapter adapter = new HostResAdapter(data);
        LinearLayoutManager mannager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        mVideoList.setLayoutManager(mannager);
        mVideoList.setAdapter(adapter);
        mVideoList.setItemAnimator(new DefaultItemAnimator());
        mVideoList.addItemDecoration(new SimpleDividerDecoration(this));
        mVideoList.setHasFixedSize(true);
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
            ChatActivity.open(this);
        }
    }
}
