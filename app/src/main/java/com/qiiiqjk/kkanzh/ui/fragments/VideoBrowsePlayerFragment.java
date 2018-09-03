//package com.qiiiqjk.kkanzh.ui.fragments;
//
//import android.graphics.PixelFormat;
//import android.graphics.SurfaceTexture;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.TextureView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import com.bumptech.glide.Glide;
//import com.qiiiqjk.kkanzh.App;
//import com.qiiiqjk.kkanzh.R;
//import com.qiiiqjk.kkanzh.common.XBaseFragment;
//import com.qiiiqjk.kkanzh.model.UGCVideoResult;
//import com.qiiiqjk.kkanzh.utils.StringUtils;
//import butterknife.BindView;
//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.Vitamio;
//
//import android.widget.TextView;
//import android.widget.Toast;
//
///**
// * 上下切换是的播放fragment
// */
//public class VideoBrowsePlayerFragment extends XBaseFragment implements View.OnClickListener,
//        MediaPlayer.OnPreparedListener,MediaPlayer.OnBufferingUpdateListener,
//        MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener,
//        MediaPlayer.OnVideoSizeChangedListener, TextureView.SurfaceTextureListener {
//    public static final String TAG = VideoBrowsePlayerFragment.class.getSimpleName();
//    private MediaPlayer mediaPlayer;
//    private UGCVideoResult mVideoResult;
//    private int error_reload_time = 0;//错误重载次数
//    private Surface mSurface;
//
//    @BindView(R.id.pause)
//    View mPause;
//    @BindView(R.id.pb_progressbar)
//    ProgressBar mProgressBar;
//    @BindView(R.id.cover)
//    ImageView mCover;
//    @BindView(R.id.sv_video)
//    TextureView mTextureView;
//    @BindView(R.id.name)
//    TextView mUserName;
//    @BindView(R.id.tittle)
//    TextView mUserTittle;
//    @BindView(R.id.tv_like)
//    TextView mUserLike;
//    @BindView(R.id.tv_comment)
//    TextView mUserComment;
//    @BindView(R.id.tv_share)
//    TextView mUserShare;
//    @BindView(R.id.iv_follow)
//    ImageView mUserFollow;
//    @BindView(R.id.ci_avatar)
//    ImageView mUserAvatar;
//
//    public VideoBrowsePlayerFragment() {
//    }
//
//    public static VideoBrowsePlayerFragment newInstance(UGCVideoResult videoResult) {
//        VideoBrowsePlayerFragment fragment = new VideoBrowsePlayerFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(TAG, videoResult);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    public void initData(Bundle savedInstanceState) {
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        Vitamio.isInitialized(App.getContext());
//        super.onActivityCreated(savedInstanceState);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mVideoResult = getArguments().getParcelable(TAG);
//        mPause.setOnClickListener(this);
//        mTextureView.setSurfaceTextureListener(this);
//        mUserName.setText(mVideoResult.name);
//        mUserTittle.setText(mVideoResult.title);
//        mUserLike.setText(mVideoResult.total_viewer);
//        mUserComment.setText(mVideoResult.comment_num);
//        Glide.with(App.getContext()).load(StringUtils.convertUrlStr(mVideoResult.avatar))
//                .crossFade().centerCrop().thumbnail(0.1f).into(mUserAvatar);
////        Glide.with(App.getContext()).load(mVideoResult.cover_url)
////                .crossFade().centerCrop().thumbnail(0.1f).into(mCover);
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (mediaPlayer == null) {
//            return;
//        }
//
//        if (isVisibleToUser){
//            play(0);
//        }else {
//            if (mediaPlayer != null){
//                mediaPlayer.pause();
//            }
//        }
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.fragment_video_browse_player;
//    }
//
//    @Override
//    public Object newP() {
//        return null;
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == mPause) {
//            pause();
//        }
//    }
//
//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//        Log.e(TAG, "SurfaceHolder 被创建");
//        loadVideo();
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//        Log.e(TAG, "SurfaceHolder 大小被改变");
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//        Log.e(TAG, "SurfaceHolder 被销毁");
//        if (mediaPlayer != null){
//            mediaPlayer.pause();
//        }
//        return false;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//        Log.e(TAG, "SurfaceHolder onSurfaceTextureUpdated");
//    }
//
//    /***********            加载             **********/
//
//    /**
//     * 初始化播放数据
//     * */
//    private void loadVideo(){
//        if (mediaPlayer != null || mVideoResult == null)
//            return;
//        String mUrlWithCache = /*"cache:/sdcard/download.mp4:"+*/StringUtils.convertUrlStr(mVideoResult.video_url);
//        Log.e(TAG, "play - " + mUrlWithCache);
//        Uri uri = Uri.parse(mUrlWithCache);
//        try {
//            mediaPlayer = new MediaPlayer(getContext(),true);
//            if (mSurface == null){
//                mSurface = new Surface(mTextureView.getSurfaceTexture());
//            }
//            mediaPlayer.setSurface(mSurface);
//            mediaPlayer.setDataSource(App.getContext(), uri);
//            Log.e(TAG, "开始装载");
//            mediaPlayer.prepareAsync();
//            mediaPlayer.setOnVideoSizeChangedListener(this);
//            mediaPlayer.setOnBufferingUpdateListener(this);
//            mediaPlayer.setOnCompletionListener(this);
//            mediaPlayer.setOnPreparedListener(this);
//            mediaPlayer.setOnErrorListener(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//            if (error_reload_time <= 3){
//                error_reload_time++;
//                stop();//释放
//                loadVideo();
//            }else {
//                Toast.makeText(getContext(), "当前视频加载错误！请尝试其他视频", Toast.LENGTH_SHORT).show();
//            }
//            Log.e(TAG, "load error: " + e.toString());
//        }
//    }
//
//    @Override
//    public void onPrepared(MediaPlayer mp) {
//        try {
//            Log.e(TAG, "装载完成");
//            mProgressBar.setVisibility(View.GONE);
//            if (getUserVisibleHint()){
//                play(0);
//            }
//        } catch (IllegalArgumentException e) {
//            //提前结束了activity，surface被提前释放异常
//            Log.e(TAG, "onPrepared error: " + e.toString());
//        }
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        // 在播放完毕被回调
//        mProgressBar.setVisibility(View.GONE);
//        replay();
//    }
//
//    @Override
//    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//
//    }
//
//    @Override
//    public boolean onError(MediaPlayer mp, int what, int extra) {
//        //Log.e(TAG, "Error Play (" + what + "," + extra + ")");
//        // 发生错误重新播放
//        //play(0);
//        //Toast.makeText(getActivity(), "播放失败", Toast.LENGTH_SHORT).show();
//        return false;
//    }
//
//    @Override
//    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//        changeVideoSize();
//        mTextureView.setVisibility(View.VISIBLE);
//    }
//
//    /**
//     * 适配全屏
//     * */
//    public void changeVideoSize() {
//        int videoWidth = mediaPlayer.getVideoWidth();
//        int videoHeight = mediaPlayer.getVideoHeight();
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int surfaceWidth = dm.widthPixels;
//        int surfaceHeight = dm.heightPixels;
//        float scaleVideo = (float) videoHeight / (float) videoWidth;
//        float scaleSurface = (float) surfaceHeight / (float) surfaceWidth;
//
//        if (scaleVideo > scaleSurface){
//            float f = (float) videoWidth / (float) surfaceWidth;
//            videoHeight = (int) (surfaceHeight / f);
//            videoWidth = surfaceWidth;
//        }
//        if (scaleVideo <= scaleSurface) {
//            videoWidth = (int) (surfaceHeight / scaleVideo);
//            videoHeight = surfaceHeight;
//        }
//        mTextureView.setLayoutParams(new LinearLayout.LayoutParams(videoWidth, videoHeight));
//    }
//
//
//
//    /***********            控制             **********/
//
//
//    /**
//     * 开始播放
//     *
//     * @param msec 播放初始位置
//     */
//    protected void play(final int msec) {
//        mediaPlayer.start();
//        mediaPlayer.seekTo(msec);
//    }
//
//    /**
//     * 重新开始播放
//     */
//    protected void replay() {
//        mProgressBar.setVisibility(View.VISIBLE);
//        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            play(0);
//            Toast.makeText(getActivity(), "重新播放", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 暂停或继续
//     */
//    protected void pause() {
//        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//            Toast.makeText(getActivity(), "暂停播放", Toast.LENGTH_SHORT).show();
//        } else if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
//            mediaPlayer.start();
//            Toast.makeText(getActivity(), "继续播放", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 停止播放
//     */
//    protected void stop() {
//        if (mediaPlayer != null) {
//            if (mediaPlayer.isPlaying()) {
//                mediaPlayer.stop();
//            }
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }
//}
