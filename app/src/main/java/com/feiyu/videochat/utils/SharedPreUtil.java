package com.feiyu.videochat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.feiyu.videochat.common.Constants;
import com.feiyu.videochat.model.LoginInfoResults;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SharedPreUtil {
    public static final String TAG = SharedPreUtil.class.getSimpleName();
    private static SharedPreferences sp;

    private static final String SharedPreference_SessionId = "sessionId";

    public static void init(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * session id
     */
    public static String getSessionId() {
        return sp.getString(SharedPreference_SessionId, "");
    }

    public static void saveSessionId(String sissonid) {
        sp.edit().putString(SharedPreference_SessionId, sissonid).apply();
    }

    public static void clearSessionId() {
        sp.edit().remove(SharedPreference_SessionId).apply();
    }

    /*****************************          public method start                 ******************************/

    private static <T> T getObject(String key, Class<T> c) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String encryptedJson = sp.getString(key, null);
        if (TextUtils.isEmpty(encryptedJson)) {
            return null;
        }
        try {
//            String decryptedJson = des.decrypt(encryptedJson);
            return new Gson().fromJson(encryptedJson, c);
        } catch (Throwable t) {
            return null;
        }
    }

    public static void saveObject(String key, Object object) {
        if (null == object) return;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oosw = new ObjectOutputStream(baos);
            oosw.writeObject(object);
//            String objstr = DESUtil.encrypt(baos.toString()/*new String(baos.toByteArray(), "utf-8")*/, DESUtil.DEFAULT_KEY);
            String objstr = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            sp.edit().putString(key, objstr).commit();
            baos.close();
            oosw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T getObject(String key) {
        T t = null;
        String objstr = sp.getString(key, "");
        if (TextUtils.isEmpty(objstr)) {
            return t;
        }
        try {
//            byte[] bytes = DESUtil.decrypt(objstr, DESUtil.DEFAULT_KEY).getBytes();
            byte[] bytes = Base64.decode(objstr, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            t = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void saveLoginInfo(LoginInfoResults loginInfoResults){
        SharedPreferences.Editor editor = sp.edit();
        if (isLogin()){
            editor.remove(Constants.SP_LOGIN_INFO);
        }
        Gson gson = new Gson();
        String json = gson.toJson(loginInfoResults);
        Log.e(Constants.SP_LOGIN_INFO, "saveLoginInfo: "+json );
        editor.putString(Constants.SP_LOGIN_INFO,json).commit();
    }
    
    public static LoginInfoResults getLoginInfo(){
        String json = sp.getString(Constants.SP_LOGIN_INFO, "");
        if (StringUtils.isEmpty(json)){
            return null;
        }
        Gson gson = new Gson();
        LoginInfoResults user = gson.fromJson(json,LoginInfoResults.class);
        return user;
    }

    public static void clearLoginInfo(){
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Constants.SP_LOGIN_INFO);
        editor.commit();
    }

    public static boolean isLogin(){
        return sp.contains(Constants.SP_LOGIN_INFO);
    }

    public static boolean isVip(){
        LoginInfoResults loginInfo = getLoginInfo();
        int vip = 0;
        try {
            vip = Integer.parseInt(loginInfo.vip);
        }catch (NumberFormatException e){
            Log.e(SharedPreUtil.TAG, "isVip: "+e.toString() );
        }
        return !(vip == Constants.NORMAL_USER_TYPE);
    }

    /*****************************          public method end                 ******************************/
}
