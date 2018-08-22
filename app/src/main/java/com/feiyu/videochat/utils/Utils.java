package com.feiyu.videochat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.feiyu.videochat.App;
import com.feiyu.videochat.common.Constants;
import com.feiyu.videochat.net.api.Api;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
}
