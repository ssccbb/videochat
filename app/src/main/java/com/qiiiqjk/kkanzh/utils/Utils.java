package com.qiiiqjk.kkanzh.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.qiiiqjk.kkanzh.common.Constants;
import com.qiiiqjk.kkanzh.net.api.Api;
import com.qiiiqjk.kkanzh.R;

import org.json.JSONArray;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern
            .compile("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$");
    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset ASCII = Charset.forName("US-ASCII");

    public static int dip2px(Context context, float dipValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static String getMd5(String value) {
        final MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return value;
        }
        md5.update(Utils.toUtf8(value));
        final StringBuilder sb = new StringBuilder();
        for (byte b : md5.digest()) {
            if (b < 0) {
                b += 256;
            }
            if (b < 16) {
                b = 0;
            }
            sb.append(Integer.toHexString(b));
        }

        return sb.toString();
    }

    public static byte[] toUtf8(String s) {
        return encode(UTF_8, s);
    }

    private static byte[] encode(Charset charset, String s) {
        if (s == null) {
            return null;
        }
        final ByteBuffer buffer = charset.encode(CharBuffer.wrap(s));
        final byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return bytes;
    }

    public static long getLongValue(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("VC", Context.MODE_PRIVATE);
        long value = sp.getLong(key, 0);
        return value;
    }

    public static void putLongValue(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences("VC", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static String getSha1(String val) {
        byte[] data = new byte[0];
        String result = "";
        try {
            data = val.getBytes("utf-8");
            MessageDigest mDigest = MessageDigest.getInstance("sha1");
            byte[] digest = mDigest.digest(data);
            result = byteArrayToHexStr(digest);
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static String decode(Charset charset, byte[] b) {
        if (b == null) {
            return null;
        }
        final CharBuffer buffer = charset.decode(ByteBuffer.wrap(b));
        return new String(buffer.array(), 0, buffer.length());
    }

    public static String emptyConverter(String str) {
        return TextUtils.isEmpty(str) || str.equals("null") ? "" : str;
    }

    public static JSONArray emptyConverter(List<String> list) {
        JSONArray jsonArray = new JSONArray(list);
        return jsonArray;
    }

    /**
     * 是否是手机号码
     */
    public static boolean isMobile(String str) {
        if (!TextUtils.isEmpty(str)) {
            return PHONE_NUMBER_PATTERN.matcher(str).find();
        }
        return false;
    }

    /**
     * 是否是邮箱
     */
    public static boolean isEmail(String str) {
        if (!TextUtils.isEmpty(str)) {
            return EMAIL_PATTERN.matcher(str).find();
        }
        return false;
    }

    @NonNull
    public static String getToken(String method, String url, long l) {
        String replace = url.replace(Api.API_BASE_URL + "/", "");
        if (method.equals("GET")) {
            int i = replace.indexOf("?");
            if (i > 0) {
                replace = replace.substring(0, i);
            }
        }
        replace = replace.toLowerCase();

        String token = Utils.getSha1(Constants.API_KEY + replace + Long.toHexString(l));
        token = token.toLowerCase();
        return token;
    }

    /** file && cache */

    /**
     * 获取缓存大小
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清除缓存
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     *
     * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     *
     * @param context
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     *
     * @param context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * * 按名字清除本应用数据库 * *
     *
     * @param context
     * @param dbName
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     *
     * @param context
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }
    /**
     * * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * *
     *
     * @param filePath
     * */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * * 清除本应用所有的数据 * *
     *
     * @param context
     * @param filepath
     */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        if (filepath == null) {
            return;
        }
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 如果下面还有文件
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "MB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


    public static String getCacheSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }

    /**
     * 获取在线状态
     * 1空闲 2在聊 3勿扰
     * */
    public static int getHostStatus(int status){
        switch (status){
            case 1:
                return R.mipmap.ic_host_status_free;
            case 2:
                return R.mipmap.ic_host_status_chat;
            case 3:
                return R.mipmap.ic_host_status_busy;
        }
        return R.mipmap.ic_host_status_busy;
    }

    public static File getCachedImageOnDisk(CacheKey cacheKey) {
        File localFile = null;
        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageFileCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }

    public View inflateFlag(Context context, String flag){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_flag, null, false);
        TextView text = view.findViewById(R.id.text);
        text.setText(flag);
        text.setBackground(context.getResources().getDrawable(Constants.round_bg[Math.round(Constants.round_bg.length)]));
        return view;
    }

    /**
     * 给出url，获取视频的第一帧
     *
     * @param url
     * @return
     */
    public static Bitmap getVideoThumbnail(String url) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(url, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

//    /**
//     * 自动适配视频尺寸（留做备份）
//     * */
//    public void changeVideoSize() {
//        int videoWidth = mediaPlayer.getVideoWidth();
//        int videoHeight = mediaPlayer.getVideoHeight();
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int surfaceWidth = dm.widthPixels;
//        int surfaceHeight = dm.heightPixels;
//
//        //根据视频尺寸去计算->视频可以在sufaceView中放大的最大倍数。
//        float max;
//        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            //竖屏模式下按视频宽度计算放大倍数值
//            max = Math.max((float) videoWidth / (float) surfaceWidth, (float) videoHeight / (float) surfaceHeight);
//        } else {
//            //横屏模式下按视频高度计算放大倍数值
//            max = Math.max(((float) videoWidth / (float) surfaceHeight), (float) videoHeight / (float) surfaceWidth);
//        }
//
//        //视频宽高分别/最大倍数值 计算出放大后的视频尺寸
//        videoWidth = (int) Math.ceil((float) videoWidth / max);
//        videoHeight = (int) Math.ceil((float) videoHeight / max);
//
//        //无法直接设置视频尺寸，将计算出的视频尺寸设置到surfaceView 让视频自动填充。
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(videoWidth, videoHeight);
//        mVideoView.setLayoutParams(params);
//    }

    public static String formatTime(long time) {
        String hs, ms, ss, formatTime;
        long h, m, s;
        h = time / 3600;
        m = (time % 3600) / 60;
        s = (time % 3600) % 60;
        hs = "" + h;
        ms = "" + m;
        ss = "" + s;
        if (hs.equals("0")) {
            formatTime = ms + "分钟" + ss + "秒";
        } else {
            formatTime = hs + "小时" + ms + "分钟" + ss + "秒";
        }

        return formatTime;
    }

}
