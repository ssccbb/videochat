package com.weiliao.kinnek.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.weiliao.kinnek.App;
import com.weiliao.kinnek.R;
import com.weiliao.kinnek.common.XBaseFragment;
import com.weiliao.kinnek.utils.Utils;
import com.weiliao.kinnek.views.subsamplingScaleImageView.ImageSource;
import com.weiliao.kinnek.views.subsamplingScaleImageView.ImageViewState;
import com.weiliao.kinnek.views.subsamplingScaleImageView.SubsamplingScaleImageView;

import java.io.File;

import butterknife.BindView;

/**
 * Created by sun on 04/01/2018.
 */

public class SubSampleImagesViewFragment extends XBaseFragment {

    public static final String URL = "url";
    public static final int WHAT_GET_FILE = 1;
    public static final int WHAT_GET_FILE_FAILED = 2;
    public static final String LOCAL_URL = "local_url";

    private String mUrl;
    private DataSource<Void> mDataSource;
    private boolean mLoadFailed;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_GET_FILE:
                    Bundle data = msg.getData();
                    String string = data.getString(LOCAL_URL);
                    if (mPBLoading != null) {
                        mPBLoading.setVisibility(View.GONE);
                    }
                    if (mSubImageView != null) {
                        mSubImageView.setImage(ImageSource.uri(Uri.parse(string)));
                    }
                    break;
                case WHAT_GET_FILE_FAILED:
                    if (mPBLoading != null) {
                        mPBLoading.setVisibility(View.GONE);
                    }

                    if (mSubImageView != null) {
                        mSubImageView.setImage(ImageSource.resource(R.mipmap.ic_loading_fail));
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @BindView(R.id.sub_sample_view)
    SubsamplingScaleImageView mSubImageView;
    @BindView(R.id.pb_loading)
    ProgressBar mPBLoading;

    public static SubSampleImagesViewFragment getInstance(String url) {
        SubSampleImagesViewFragment subSampleImagesViewFragment = new SubSampleImagesViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        subSampleImagesViewFragment.setArguments(bundle);
        return subSampleImagesViewFragment;
    }

    public SubSampleImagesViewFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_sub_sample_images_view_fragment;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUrl = arguments.getString(URL);
        }

        //mSubImageView.setMinScale(1.0f);
        //mSubImageView.setMaxScale(1.0f);
        mSubImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
        Glide.with(getActivity()).load(mUrl).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                if (mPBLoading != null) {
                    mPBLoading.setVisibility(View.GONE);
                }
                mSubImageView.setImage(ImageSource.uri(Uri.fromFile(resource)),
                        new ImageViewState(getInitImageScale(resource.getAbsolutePath()), new PointF(0, 0), 0));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //getImage();
    }

    private void getImage() {

        if (mPBLoading != null) {
            mPBLoading.setVisibility(View.VISIBLE);
        }

        if (mUrl.startsWith("content://")) {
            if (mPBLoading != null) {
                mPBLoading.setVisibility(View.GONE);
            }
            if (mSubImageView != null) {
                mSubImageView.setImage(ImageSource.uri(Uri.parse(mUrl)));
            }
            return;
        }

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(mUrl))
                .build();

        final ImagePipeline imagePipeline = Fresco.getImagePipeline();

        mDataSource = imagePipeline.prefetchToDiskCache(imageRequest, App.getContext(), Priority.HIGH);
        mDataSource.subscribe(new BaseDataSubscriber<Void>() {

            @Override
            protected void onNewResultImpl(DataSource<Void> dataSource) {
                CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                        .getEncodedCacheKey(ImageRequest.fromUri(Uri.parse(mUrl)), null);
                File cachedImageOnDisk = Utils.getCachedImageOnDisk(cacheKey);
                mLoadFailed = false;
                Message message = mHandler.obtainMessage();
                message.what = WHAT_GET_FILE;
                Bundle bundle = new Bundle();
                bundle.putString(LOCAL_URL, Uri.fromFile(cachedImageOnDisk).toString());
                message.setData(bundle);
                mHandler.sendMessage(message);
            }

            @Override
            public void onProgressUpdate(DataSource<Void> dataSource) {
                super.onProgressUpdate(dataSource);
            }

            @Override
            protected void onFailureImpl(DataSource<Void> dataSource) {
                Throwable failureCause = dataSource.getFailureCause();
                Log.e("TAG", "failureCause:" + failureCause.toString());
                mHandler.sendEmptyMessage(WHAT_GET_FILE_FAILED);
                mLoadFailed = true;
            }
        }, CallerThreadExecutor.getInstance());
    }

    /**
     * 计算出图片初次显示需要放大倍数
     * @param imagePath 图片的绝对路径
     */
    public float getInitImageScale(String imagePath){
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        // 拿到图片的宽和高
        int dw = bitmap.getWidth();
        int dh = bitmap.getHeight();
        float scale = 1.0f;
        //图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh <= height) {
            scale = width * 1.0f / dw;
        }
        //图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
        if (dw <= width && dh > height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
        if (dw < width && dh < height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh > height) {
            scale = width * 1.0f / dw;
        }
        return scale;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDataSource != null) {
            mDataSource.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
