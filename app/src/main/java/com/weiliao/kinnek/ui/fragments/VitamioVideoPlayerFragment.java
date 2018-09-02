package com.weiliao.kinnek.ui.fragments;

import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.common.Constants;
import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.model.HotHostResults;
import com.weiliao.kinnek.model.UGCVideoResult;
import com.weiliao.kinnek.ui.activitys.HostInfoActivity;
import com.weiliao.kinnek.utils.StringUtils;
import com.weiliao.kinnek.views.CircleImageView;

import butterknife.BindView;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.CenterLayout;

public class VitamioVideoPlayerFragment extends XBaseFragment implements SurfaceHolder.Callback,
        MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, View.OnClickListener {
    public static final String TAG = VitamioVideoPlayerFragment.class.getSimpleName();
    private static VitamioVideoPlayerFragment instance;
    private UGCVideoResult video;
    private String path;
    private int mVideoWidth;
    private int mVideoHeight;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder holder;

    @BindView(R.id.iv_back)
    View mBack;
    @BindView(R.id.iv_more)
    View mMore;
    @BindView(R.id.cover)
    ImageView mCover;
    @BindView(R.id.surface)
    SurfaceView mPreview;
    @BindView(R.id.pb_loading)
    ProgressBar mLoading;
    @BindView(R.id.ci_avatar)
    CircleImageView mAvatar;
    @BindView(R.id.tv_like)
    TextView mLike;
    @BindView(R.id.tv_comment)
    TextView mComment;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.tittle)
    TextView mTittle;
    @BindView(R.id.pause)
    View mPause;
    @BindView(R.id.pause_icon)
    View mPauseIcon;

    private Handler mUiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lazyInit();
        }
    };

    public static VitamioVideoPlayerFragment newInstance(UGCVideoResult videoResult) {
        instance = new VitamioVideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, videoResult);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Vitamio.isInitialized(getActivity());
        mBack.setOnClickListener(this);
        mMore.setOnClickListener(this);
        mAvatar.setOnClickListener(this);
        mLike.setOnClickListener(this);
        mComment.setOnClickListener(this);
        mPause.setOnClickListener(this);
    }

    private void lazyInit(){
        video = getArguments().getParcelable(TAG);

        //ui
        Glide.with(getActivity()).load(StringUtils.convertUrlStr(video.cover_url)).thumbnail(0.1f).centerCrop().into(mCover);
        Glide.with(getActivity()).load(StringUtils.convertUrlStr(video.avatar)).thumbnail(0.1f).centerCrop().into(mAvatar);
        Log.e(TAG, "lazyInit ~ "+ video.toString());
        mLike.setText(video.pick_num);
        mComment.setText(video.comment_num);
        mName.setText(StringUtils.isEmpty(video.name) ? Constants.default_user_name : video.name);
        mTittle.setText(video.title);

        //mediaplayer
        path = StringUtils.convertUrlStr(video.video_url);
        holder = mPreview.getHolder();
        holder.addCallback(VitamioVideoPlayerFragment.this);
        holder.setFormat(PixelFormat.RGBA_8888);
        loadVideo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {//懒加载
            if (mMediaPlayer == null) {
                mUiHandler.sendEmptyMessageDelayed(0, 500);
                return;
            }
            if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
        } else {
            doCleanUp();
            releaseMediaPlayer();
        }
    }

    private void loadVideo() {
        if (StringUtils.isEmpty(path))
            //数据错误
            return;

        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
            //当前正在播放 防止 setUserVisibleHint 和 surfaceCreated 时两个播放冲突
            return;

        doCleanUp();
        releaseMediaPlayer();

        //数据加载中展示加载条
        if (mLoading != null &&
                (mLoading.getVisibility() == View.GONE)) {
            mLoading.setVisibility(View.VISIBLE);
        }
        //数据加载中展示封面
        if (mCover != null &&
                (mCover.getVisibility() == View.GONE)){
            mCover.setVisibility(View.VISIBLE);
        }

        try {
            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer(getContext());
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated called");
        loadVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e(TAG, "surfaceChanged called");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed called");
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.e(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
//        mVideoWidth = width;
//        mVideoHeight = height;
        changeVideoSize();
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown && getUserVisibleHint()) {
            startVideoPlayback();
        }
    }

    /**
      * 适配全屏
      * */
    public void changeVideoSize() {
        int videoWidth = mMediaPlayer.getVideoWidth();
        int videoHeight = mMediaPlayer.getVideoHeight();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int surfaceWidth = dm.widthPixels;
        int surfaceHeight = dm.heightPixels;
        float scaleVideo = (float) videoHeight / (float) videoWidth;
        float scaleSurface = (float) surfaceHeight / (float) surfaceWidth;

        if (scaleVideo > scaleSurface){
            float f = (float) videoWidth / (float) surfaceWidth;
            mVideoHeight = (int) (surfaceHeight / f);
            mVideoWidth = surfaceWidth;
        }
        if (scaleVideo <= scaleSurface) {
            mVideoWidth = (int) (surfaceHeight / scaleVideo);
            mVideoHeight = surfaceHeight;
        }
        mPreview.setLayoutParams(new CenterLayout.LayoutParams(mVideoWidth, mVideoHeight,0,0));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.e(TAG, "play source prepare done!");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown && getUserVisibleHint()) {
            startVideoPlayback();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e(TAG, "play complete!");
        if (!getUserVisibleHint()) return;
        mPauseIcon.setVisibility(View.VISIBLE);
        replay();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "play erro msg( "+what+","+extra+")" );
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (percent >= 100 && (mLoading.getVisibility() == View.VISIBLE)) {
            mLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_vitamio_test;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy ！" );
        releaseMediaPlayer();
        doCleanUp();
        if (mUiHandler != null) {
            mUiHandler.removeCallbacksAndMessages(null);
            mUiHandler = null;
        }
        if (mPreview != null){
            mPreview.setVisibility(View.GONE);
            mPreview = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBack){
            onDestroy();
            getActivity().finish();
        }
        if (v == mMore){

        }
        if (v == mAvatar){
            if (StringUtils.isEmpty(video.uid)) return;
            pause();
            HostInfoActivity.open(getActivity(),new HotHostResults.HotHostResult(video.uid));
        }
        if (v == mComment){
            Toast.makeText(getActivity(), "评论已暂时关闭", Toast.LENGTH_SHORT).show();
        }
        if (v == mLike){
            mLike.setSelected(!mLike.isSelected());
        }
        if (v == mPause){
            pause();
        }
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        Log.v(TAG, "start play!");
        //holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
        mLoading.setVisibility(View.GONE);
        mCover.setVisibility(View.GONE);
    }

    private void pause(){
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            mPauseIcon.setVisibility(View.VISIBLE);
        }else {
            mMediaPlayer.start();
            mPauseIcon.setVisibility(View.GONE);
        }
    }

    private void replay(){
        if (mMediaPlayer == null) return;
        if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
        mMediaPlayer.seekTo(0);
        mMediaPlayer.start();
        mPauseIcon.setVisibility(View.GONE);
    }
}
