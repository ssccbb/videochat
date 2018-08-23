package com.feiyu.videochat.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ProgressBar;
import com.feiyu.videochat.App;
import com.feiyu.videochat.R;
import com.feiyu.videochat.common.XBaseFragment;
import com.feiyu.videochat.model.UGCVideoResult;
import butterknife.BindView;
import android.view.SurfaceHolder.Callback;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.Toast;

/**
 * 上下切换是的播放fragment
 * */
public class VideoBrowsePlayerFragment extends XBaseFragment implements View.OnClickListener{
    public static final String TAG = VideoBrowsePlayerFragment.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private int currentPosition = 0;
    private UGCVideoResult mVideoResult;

    @BindView(R.id.sv_video)
    SurfaceView mVideoView;
    @BindView(R.id.iv_start)
    View mStart;
    @BindView(R.id.pause)
    View mPause;
    @BindView(R.id.pb_progressbar)
    ProgressBar mProgressBar;

    public VideoBrowsePlayerFragment() {
    }

    public static VideoBrowsePlayerFragment newInstance(UGCVideoResult videoResult){
        VideoBrowsePlayerFragment fragment = new VideoBrowsePlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG,videoResult);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVideoResult = getArguments().getParcelable(TAG);

        mVideoView.getHolder().addCallback(callback);
        mVideoView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mVideoView.getHolder().setKeepScreenOn(true);
        mPause.setOnClickListener(this);
        mStart.setOnClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser || mVideoResult == null) {
            stop();
            return;
        }

        if (mediaPlayer == null){
            play(0);
        }else {
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    private Callback callback = new Callback() {
        // SurfaceHolder被修改的时候回调
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.e(TAG+mVideoResult.position, "SurfaceHolder 被销毁");
            // 销毁SurfaceHolder的时候记录当前的播放位置并停止播放
//            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                currentPosition = mediaPlayer.getCurrentPosition();
//                mediaPlayer.stop();
//            }
            stop();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.e(TAG+mVideoResult.position, "SurfaceHolder 被创建");
            if (currentPosition == 0){
                play(0);
                return;
            }
            if (currentPosition > 0) {
                // 创建SurfaceHolder的时候，如果存在上次播放的位置，则按照上次播放位置进行播放
                play(currentPosition);
                currentPosition = 0;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.e(TAG+mVideoResult.position, "SurfaceHolder 大小被改变");
        }

    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_video_browse_player;
    }

    @Override
    public Object newP() {
        return null;
    }

    /*
    * 停止播放
    */
    protected void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
    }

    /**
    * 开始播放
    *
    * @param msec 播放初始位置
    */
    protected void play(final int msec) {
        if (!getUserVisibleHint()) return;
        // 获取视频文件地址
        Uri uri = Uri.parse("android.resource://com.feiyu.videochat/"+R.raw.test);
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            mediaPlayer.setDataSource(App.getContext(),uri);
            Log.e(TAG+mVideoResult.position, "开始装载");
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 设置显示视频的SurfaceHolder
                    mediaPlayer.setDisplay(mVideoView.getHolder());
                    Log.e(TAG+mVideoResult.position, "装载完成");
                    mediaPlayer.start();
                    // 按照初始位置播放
                    mediaPlayer.seekTo(msec);
                    /*// 设置进度条的最大进度为视频流的最大播放时长
                    seekBar.setMax(mediaPlayer.getDuration());
                    // 开始线程，更新进度条的刻度
                    new Thread() {

                        @Override
                        public void run() {
                            try {
                                isPlaying = true;
                                while (isPlaying) {
                                    int current = mediaPlayer.getCurrentPosition();
                                    //seekBar.setProgress(current);

                                    sleep(500);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();*/
                    isPlaying = true;
                    mProgressBar.setVisibility(View.GONE);
                }
            });
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 在播放完毕被回调
                    mProgressBar.setVisibility(View.GONE);
                }
            });

            mediaPlayer.setOnErrorListener(new OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // 发生错误重新播放
//                    play(0);
                    stop();
                    isPlaying = false;
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG+mVideoResult.position, "play error: "+e.toString() );
        }

    }

    /**
    * 重新开始播放
    */
    protected void replay() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
            Toast.makeText(getActivity(), "重新播放", Toast.LENGTH_SHORT).show();
            //btn_pause.setText("暂停");
            return;
        }
        isPlaying = false;
        play(0);
    }

    /**
    * 暂停或继续
    */
    protected void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Toast.makeText(getActivity(), "暂停播放", Toast.LENGTH_SHORT).show();
            isPlaying = false;
        }else if (mediaPlayer != null && !mediaPlayer.isPlaying()){
            //            btn_pause.setText("暂停");
            mediaPlayer.start();
            Toast.makeText(getActivity(), "继续播放", Toast.LENGTH_SHORT).show();
            isPlaying = true;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mPause){
            pause();
        }
        if (v == mStart){
        }
    }
}
