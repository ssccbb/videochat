package com.weiliao.kinnek.utils;

import android.os.Environment;
import android.util.Log;

import com.weiliao.kinnek.common.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lhc on 2017/6/21.
 * 1.应该先判断下载的文件是否存在
 * 2.监听的接口应该抛到主线程中。
 *
 * 1.怎么判断SD卡是否存在，是否已经满了？ Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
 * 2.怎么创建SD卡二级目录？              new File(Environment.getExternalStorageDirectory(), saveDir)
 * 3.怎么得到文件名？                    url.substring(url.lastIndexOf("/") + 1);
 * 4.怎么根据文件名创建File
 * 5.怎么输出到文件
 */
public class DownloadUtil {
    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param url 下载连接
     * @param listener 下载监听
     */
    public void download(final String url, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("update", "onFailure: "+e.toString() );
                // 下载失败
                listener.onDownloadFailed();
            }
            @Override
            public void onResponse(Call call, Response response){
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    // 储存下载文件的目录
                    String savePath = isExistDir(Constants.default_download_apk_dir);
                    Log.e("update", "onResponse: "+savePath );
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
                    Log.e("update", "onResponse:IOException- "+e.toString() );
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        Log.e("update", "onResponse:IOException- "+e.toString() );
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        Log.e("update", "onResponse:IOException- "+e.toString() );
                    }
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
            if (!downloadFile.mkdirs()) {
                downloadFile.createNewFile();
            }
            String savePath = downloadFile.getAbsolutePath();
            Log.e("savePath",savePath);
            return savePath;
        }
        return null;
    }

    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(File str);

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}
