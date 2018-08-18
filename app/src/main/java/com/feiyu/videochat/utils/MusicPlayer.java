package com.feiyu.videochat.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import com.feiyu.videochat.R;

public class MusicPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private Context context;
    private static MusicPlayer instance;
    private MediaPlayer bgPlayer;  //播放背景音乐的播放器
    private SoundPool actionMusicPlayer;  //播放音效的播放器
    private int source_connect,source_cancel;  //各种音效的source

    public MusicPlayer(Context context) {  //初始化
        this.context = context;
        this.actionMusicPlayer = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);  //这里指定声音池的最大音频流数目为10，声音品质为5大家可以自己测试感受下效果
        this.source_connect = actionMusicPlayer.load(context, R.raw.call_connectting, 0);
        this.source_cancel = actionMusicPlayer.load(context, R.raw.call_cancel, 0);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {  //音频文件播放完成时自动调用
        stopBgSound();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {  //音频文件播放发生错误时自动调用
        return false;
    }

    public void playBgSound(int paramInt) {  //播放背景音乐，paramInt为音频文件ID
        stopBgSound();
        try {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, paramInt);
            bgPlayer = mediaPlayer;
            bgPlayer.setOnCompletionListener(this);
            bgPlayer.setLooping(true);  //设置是否循环，一般背景音乐都设为true
            bgPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public MediaPlayer getBackGroundPlayer() {
        return this.bgPlayer;
    }

    public void stopBgSound() {  //停止背景音乐的播放
        if(bgPlayer == null)
            return;
        if(bgPlayer.isPlaying())
            bgPlayer.stop();
        bgPlayer.release();
        bgPlayer = null;
    }

    private void playSound(int source) {  //如果系统设置中开启了音效，则播放相关的音频文件
        actionMusicPlayer.autoPause();
        actionMusicPlayer.play(source, 1, 1, 0, 0, 1);
    }

    public void playConnectSound(){
        actionMusicPlayer.autoPause();
        actionMusicPlayer.play(this.source_connect,1,1,0,-1,1);
    }

    public void playCancelSound(){
        playSound(this.source_cancel);
    }

    public void releaseAll(){
        stopBgSound();
        if (actionMusicPlayer == null) return;
        actionMusicPlayer.autoPause();
        actionMusicPlayer.release();
        actionMusicPlayer = null;
    }
}