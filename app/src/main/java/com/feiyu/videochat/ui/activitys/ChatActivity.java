package com.feiyu.videochat.ui.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.feiyu.videochat.R;
import com.feiyu.videochat.common.Constants;
import com.feiyu.videochat.common.XBaseActivity;
import com.feiyu.videochat.utils.MusicPlayer;
import com.feiyu.videochat.views.CircleImageView;
import com.zhouwei.blurlibrary.EasyBlur;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

public class ChatActivity extends XBaseActivity implements View.OnClickListener,SurfaceHolder.Callback {
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
    @BindView(R.id.surface)
    SurfaceView mCameraView;
    @BindView(R.id.close)
    View mExit;

    public static final String TAG = ChatActivity.class.getSimpleName();
    private Bitmap mCoverBitmap;
    private Handler mPlayerHandler;
    private MusicPlayer mPlayer;
    private SurfaceHolder mCameraHolder;
    private Camera mCamera;
    private final int REQUEST_CAMERA = 101;
    private int frontCamera = 0;
    private int backCamera = 0;
    private int current_camera_type = frontCamera;

    @Override
    public void initData(Bundle savedInstanceState) {
        BitmapDrawable d = (BitmapDrawable) getResources().getDrawable(R.drawable.timg);
        mCoverBitmap = EasyBlur.with(ChatActivity.this)
                .bitmap(d.getBitmap()) //要模糊的图片
                .radius(10)//模糊半径
                .scale(15)//指定模糊前缩小的倍数
                //.policy(EasyBlur.BlurPolicy.FAST_BLUR)//使用fastBlur
                .blur();
        mCover.setImageBitmap(mCoverBitmap);
        mAvatar.setImageResource(R.drawable.timg1);
        mPlayerHandler = new Handler();
        mPlayer = new MusicPlayer(this);
        mStop.setOnClickListener(this);
        mExit.setOnClickListener(this);
        mCameraHolder = mCameraView.getHolder();
        mCameraHolder.addCallback(this);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCoverBitmap.recycle();
        if (mPlayer != null) {
            mPlayer.releaseAll();
            mPlayer = null;
        }
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
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
                    refuse(R.string.call_stop);
                }
            }, 500);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requestCode == REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "onRequestPermissionsResult: " + grantResults.toString());
                autoResult();
            } else {
                Toast.makeText(this, "未获取到摄像头权限！", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }



    /******        连接过程         ******/

    // 模拟结果（无vip）
    private void autoResult() {
        mPlayer.playBgSound(R.raw.call_connectting);
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



    /*****         聊天过程         ******/

    private void switch2Connect(){
        mRequestView.setVisibility(View.GONE);
        mChatView.setVisibility(View.VISIBLE);
        if (mCamera != null){
            mCamera.startPreview();
        }
    }

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
            Toast.makeText(this, "启动摄像头失败,请开启摄像头权限", Toast.LENGTH_SHORT).show();
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
}
