package com.feiyu.videochat.ui.activitys;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.Constants;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.utils.MusicPlayer;
import com.feiyu.videochat.views.CircleImageView;
import com.zhouwei.blurlibrary.EasyBlur;

import butterknife.BindView;

public class ChatActivity extends XBaseActivity implements View.OnClickListener {
    @BindView(R.id.cover)
    ImageView mCover;
    //
    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.stop)
    View mStop;
    //
    @BindView(R.id.connect_container)
    View mRequestView;
    @BindView(R.id.chat_container)
    View mChatView;

    public static final String TAG = ChatActivity.class.getSimpleName();
    private Bitmap mCoverBitmap;
    private Handler mPlayerHandler;
    private MusicPlayer mPlayer;

    @Override
    public void initData(Bundle savedInstanceState) {
        mPlayerHandler = new Handler();
        mPlayer = new MusicPlayer(this);
        BitmapDrawable d = (BitmapDrawable) getResources().getDrawable(R.drawable.timg);
        mCoverBitmap = EasyBlur.with(ChatActivity.this)
                .bitmap(d.getBitmap()) //要模糊的图片
                .radius(10)//模糊半径
                .scale(15)//指定模糊前缩小的倍数
                //.policy(EasyBlur.BlurPolicy.FAST_BLUR)//使用fastBlur
                .blur();
        mCover.setImageBitmap(mCoverBitmap);
        mPlayer.playConnectSound();
        mAvatar.setImageResource(R.drawable.timg1);
        mStop.setOnClickListener(this);
        autoResult();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCoverBitmap.recycle();
        if (mPlayer != null) {
            mPlayer.releaseAll();
            mPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mStop) {
            mPlayer.playCancelSound();
            mPlayerHandler.removeCallbacksAndMessages(null);
            mPlayerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refuse(R.string.call_stop);
                }
            }, 500);
        }
    }

    /******        连接过程         ******/
    // 模拟结果（无vip）
    private void autoResult() {
        // TODO: 2018/8/18 加个vip判断
        mPlayerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlayer.playCancelSound();
                //随机生成结果
                int result = (int) (Math.random() * 2);
                Log.e(TAG, "run: "+result );
                if (result == Constants.CONNECTTING_ACCEPT) {
                    accept();
                } else if (result == Constants.CONNECTTING_REFUSE) {
                    refuse(R.string.call_busy);
                }
            }
        }, (long) (Math.random() * 4 * 1000 + 4000));
    }

    private void refuse(int msg){
        Toast.makeText(ChatActivity.this,
                getResources().getString(msg), Toast.LENGTH_SHORT).show();
        ChatActivity.this.finish();
    }

    private void accept(){
        Toast.makeText(ChatActivity.this,
                getResources().getString(R.string.call_connect), Toast.LENGTH_SHORT).show();
        switch2Connect();
    }

    /*****         通过过程         ******/
    private void switch2Connect(){
        mRequestView.setVisibility(View.GONE);
        mChatView.setVisibility(View.VISIBLE);
    }
}
