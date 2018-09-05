package com.qiiiqjk.kkanzh.ui.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiiiqjk.kkanzh.App;
import com.qiiiqjk.kkanzh.common.Constants;
import com.qiiiqjk.kkanzh.common.XBaseActivity;
import com.qiiiqjk.kkanzh.model.AnchorInfoResult;
import com.qiiiqjk.kkanzh.model.PhoneVertifyResult;
import com.qiiiqjk.kkanzh.net.api.Api;
import com.qiiiqjk.kkanzh.net.httprequest.ApiCallback;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.JKOkHttpParamKey;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.OkHttpRequestUtils;
import com.qiiiqjk.kkanzh.ui.fragments.setting.ChargeFragment;
import com.qiiiqjk.kkanzh.utils.MusicPlayer;
import com.qiiiqjk.kkanzh.utils.SharedPreUtil;
import com.qiiiqjk.kkanzh.utils.StringUtils;
import com.qiiiqjk.kkanzh.utils.Utils;
import com.qiiiqjk.kkanzh.views.CircleImageView;
import com.qiiiqjk.kkanzh.views.dialog.VCDialog;
import com.qiiiqjk.kkanzh.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.CenterLayout;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class ChatActivity extends XBaseActivity implements View.OnClickListener {
    @BindView(R.id.cover)
    ImageView mCover;
    //
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.avatar)
    CircleImageView mAvatar;
    @BindView(R.id.stop)
    View mStop;
    //
    @BindView(R.id.name1)
    TextView mConnectedName;
    @BindView(R.id.connect_container)
    View mRequestView;
    @BindView(R.id.chat_container)
    View mChatView;
    @BindView(R.id.surface)
    SurfaceView mCameraView;
    @BindView(R.id.close)
    View mExit;
    //5s视频
    @BindView(R.id.sv_video)
    SurfaceView mVideoView;

    public static final String TAG = ChatActivity.class.getSimpleName();
    private Handler mPlayerHandler;
    private MusicPlayer mPlayer;
    private SurfaceHolder mCameraHolder;
    private Camera mCamera;
    private final int REQUEST_CAMERA = 101;
    private int frontCamera = 0;
    private int backCamera = 0;
    private int current_camera_type = frontCamera;
    private MediaPlayer mMediaPlayer;
    private AnchorInfoResult mHost;
    private int mVideoWidth;
    private int mVideoHeight;
    private boolean mIsConnected = false;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;

    @Override
    public void initData(Bundle savedInstanceState) {
        Vitamio.isInitialized(this);
        mStop.setOnClickListener(this);
        mExit.setOnClickListener(this);

        String host_json = this.getIntent().getStringExtra(TAG);
        if (!StringUtils.isEmpty(host_json)){
            Gson gson = new Gson();
            mHost = gson.fromJson(host_json,AnchorInfoResult.class);
        }
        if (mHost == null) return;

        Glide.with(App.getContext()).load(StringUtils.convertUrlStr(mHost.avatar))
                .bitmapTransform(new BlurTransformation(App.getContext(),
                        Constants.BLUR_RADIUS + 3,Constants.BLUR_SAMPLING))
                .crossFade()/*.thumbnail(0.1f)*/.into(mCover);
        Glide.with(App.getContext()).load(StringUtils.convertUrlStr(mHost.avatar))
                .crossFade()/*.thumbnail(0.1f)*/.into(mAvatar);
        name.setText(mHost.nickname);
        mConnectedName.setText(mHost.nickname);

        //Log.e(TAG, "initData: "+mHost.video_5s );
        mVideoView.getHolder().addCallback(video_callback);
        mVideoView.getHolder().setFormat(PixelFormat.RGBA_8888);

        mPlayerHandler = new Handler();
        mPlayer = new MusicPlayer(this);
        mCameraHolder = mCameraView.getHolder();
        mCameraHolder.addCallback(camera_callback);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            //Log.e(TAG, "checkPermission: " + permissionCheck);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                return;
            }
        }

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

    public static void open(Context context, AnchorInfoResult host){
        Gson gson = new Gson();
        String json = gson.toJson(host);
        Intent goTo = new Intent(context, ChatActivity.class);
        goTo.putExtra(ChatActivity.TAG,json);
        context.startActivity(goTo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.releaseAll();
            mPlayer = null;
        }
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
            mMediaPlayer.release();
            mMediaPlayer = null;
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
                    refuse(R.string.call_cancel);
                }
            }, 500);
        }
        if (v == mExit){
            mPlayer.playCancelSound();
            mPlayerHandler.removeCallbacksAndMessages(null);
            mPlayerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refuse(R.string.call_exit);
                }
            }, 500);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requestCode == REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Log.e(TAG, "onRequestPermissionsResult: " + grantResults.toString());
                autoResult();
            } else {
                Toast.makeText(this, "未获取到摄像头权限！", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }



    /******        连接过程         ******/

    // 模拟结果
    private void autoResult() {
        mPlayer.playBgSound(R.raw.call_connectting);
        mPlayerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPlayer.playCancelSound();
                //随机生成结果
                int result = (int) (Math.random() * 2);
                //5s看过
                if (mHost.is_watch.equals(Constants.HOST_IS_WATCH)) result = Constants.CONNECTTING_REFUSE;
                //Log.e(TAG, "run: result-"+result+"/watch-"+mHost.is_watch );
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



    /*****         聊天过程         ******/

    private void switch2Connect(){
        mIsConnected = true;
        mRequestView.setVisibility(View.GONE);
        mChatView.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.VISIBLE);
        mCover.setVisibility(View.GONE);
        if (mCamera != null){
            mCamera.startPreview();
        }
        play();
    }

    private SurfaceHolder.Callback camera_callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                int cameraCount = Camera.getNumberOfCameras();
                Camera.CameraInfo info = new Camera.CameraInfo();
                for(int cameraIndex = 0; cameraIndex<cameraCount; cameraIndex++){
                    Camera.getCameraInfo(cameraIndex, info);
                    if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                        frontCamera = cameraIndex;//前置
                    }else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                        backCamera = cameraIndex;//后置
                    }
                }
                current_camera_type = frontCamera;
                mCamera = Camera.open(current_camera_type);
                mCamera.setPreviewDisplay(mCameraHolder);
                mCamera.stopPreview();
            } catch (Exception e) {
                if (null != mCamera) {
                    mCamera.release();
                    mCamera = null;
                }
                e.printStackTrace();
                Log.e(TAG, "surfaceCreated: "+e.getMessage() );
                Toast.makeText(ChatActivity.this, "启动摄像头失败,请开启摄像头权限", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            initCamera();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (null != mCamera) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        }
    };

    private void initCamera(){
        Camera.Parameters parameters = mCamera.getParameters();//获取camera的parameter实例
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
        Camera.Size optionSize = getOptimalPreviewSize(
                sizeList, mCameraView.getWidth(), mCameraView.getHeight());//获取一个最为适配的屏幕尺寸
        parameters.setPreviewSize(optionSize.width, optionSize.height);//把只存设置给parameters
        mCamera.setParameters(parameters);//把parameters设置给camera上
        mCamera.startPreview();//开始预览
        mCamera.setDisplayOrientation(90);//将预览旋转90度
    }

    private void switchCamera(){
        try {
            current_camera_type = (current_camera_type == frontCamera ? backCamera : frontCamera);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = Camera.open(current_camera_type);
            mCamera.setPreviewDisplay(mCameraHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;
            }
            e.printStackTrace();
            Log.e(TAG, "switch camera failed: "+e.getMessage() );
            Toast.makeText(this, "启动摄像头失败,请开启摄像头权限", Toast.LENGTH_SHORT).show();
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**   5s视频播放  */

    private SurfaceHolder.Callback video_callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.e(TAG, "surfaceCreated call!" );
            loadVideo();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.e(TAG, "surfaceChanged call!" );
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.e(TAG, "surfaceDestroyed call!" );
        }
    };

    private void loadVideo() {
        if (StringUtils.isEmpty(mHost.video_5s))
            //数据错误
            return;

        //Log.e(TAG, "loadVideo: "+ mHost.video_5s);
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
            //当前正在播放 防止 setUserVisibleHint 和 surfaceCreated 时两个播放冲突
            return;

        doCleanUp();
        releaseMediaPlayer();

        //数据加载中展示封面
        if (mCover != null &&
                (mCover.getVisibility() == View.GONE)){
            mCover.setVisibility(View.VISIBLE);
        }

        try {
            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer(this);
            mMediaPlayer.setDataSource(StringUtils.convertUrlStr(mHost.video_5s));
            mMediaPlayer.setDisplay(mVideoView.getHolder());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {

                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    postHasWatch(SharedPreUtil.getLoginInfo().uid,mHost.uid);
                    mMediaPlayer.pause();
                    showChargeDialog();
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e(TAG, "play source prepare done!");
                    mIsVideoReadyToBePlayed = true;
                    //play();
                }
            });
            mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    Log.d(TAG, "onVideoSizeChanged called");
                    if (width == 0 || height == 0) {
                        Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
                        return;
                    }
                    mIsVideoSizeKnown = true;
                    changeVideoSize();
                    //play();
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "play erro msg( "+what+","+extra+")" );
                    if (mIsConnected) {
                        hostExit(R.string.call_busy);
                    }
                    return false;
                }
            });
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
    }

    /**
     * 适配全屏
     * */
    public void changeVideoSize() {
        int videoWidth = mMediaPlayer.getVideoWidth();
        int videoHeight = mMediaPlayer.getVideoHeight();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
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
        mVideoView.setLayoutParams(new CenterLayout.LayoutParams(mVideoWidth, mVideoHeight,0,0));
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

    private void play() {
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            Log.e(TAG, "start play!");
            mMediaPlayer.start();
            mCover.setVisibility(View.GONE);
        }
    }

    private void showChargeDialog(){
        Log.e(TAG, "showChargeDialog: ");
        if (Utils.isHideMode()){
            hostExit(R.string.call_stop);
            return;
        }
        if (!mIsVideoReadyToBePlayed) return;
        VCDialog dialog = new VCDialog(VCDialog.Ddialog_Without_tittle_Block_Confirm,"",
                getResources().getString(R.string.host_diamond_not_enough));
        dialog.addOnDialogActionListner(new VCDialog.onDialogActionListner() {
            @Override
            public void onCancel() {
                mExit.performClick();
                dialog.dismissAllowingStateLoss();
            }

            @Override
            public void onConfirm() {
                dialog.dismissAllowingStateLoss();
                SettingActivity.open(ChatActivity.this, ChargeFragment.TAG);
                ChatActivity.this.finish();
            }
        });
        dialog.show(getSupportFragmentManager(),VCDialog.TAG);
    }

    private void hostExit(int str){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }
        mPlayer.playCancelSound();
        mPlayerHandler.removeCallbacksAndMessages(null);
        mPlayerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refuse(str);
            }
        }, 500);
    }

    /**
     * post支付宝支付
     */
    private void postHasWatch(String uid, String anchor_uid) {
        OkHttpRequestUtils.getInstance().requestByPost(Api.API_BASE_URL + "/Anchor/watch_video",
                OkHttpRequestUtils.getInstance().JkRequestParameters(
                        JKOkHttpParamKey.ANCHOR_INFO, anchor_uid,uid ),
                PhoneVertifyResult.class, ChatActivity.this, new ApiCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        //Log.e(TAG, "onSuccess: " + (String) response);
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
    /**   5s视频播放完毕  */

}
