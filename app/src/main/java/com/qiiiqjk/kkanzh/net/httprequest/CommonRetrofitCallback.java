package com.qiiiqjk.kkanzh.net.httprequest;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.qiiiqjk.kkanzh.net.httpresponse.BaseResponse;
import com.qiiiqjk.kkanzh.common.AppManager;
import com.qiiiqjk.kkanzh.utils.StringUtils;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @className: CommonRetrofitCallback
 * @classDescription: 统一Api回调
 * @author: leibing
 * @createTime: 2017/4/16
 */
public abstract class CommonRetrofitCallback<T> implements Callback <T>{
    // 日志标识
    private final static String TAG = "CommonRetrofitCallback";
    // 页面弱引用为空
    private final static String ACTIVITY_WEAK_REF_IS_NULL = "activity weak ref is null";
    // 回调更新页面非当前页面
    private final static String UPDATE_UI_PAGE_IS_NOT_CURRENT_PAGE = "update ui page is not request current page";
    // 请求标识为空
    private final static String REQUESTID_IS_NULL = "requestid_is_null";
    // 添加json标签名称
    public final static String JK_JSON_NAME = "jk_json_name";
    // 请求标识--数据格式一({error_code：0，reason：成功，result：array})
    public static final String REQUEST_ID_ONE = "request_id_one";
    // 请求标识--数据格式二（无需解析数据，直接回调）
    public static final String REQUEST_ID_TWO = "request_id_two";
    // 请求标识--数据格式三（{"errorcode":"0","errorMsg":"成功","dataInfo"：obj}）
    public static final String REQUEST_ID_THREE = "request_id_three";
    // 数据解析标识--error_code
    public final static String DATA_ANALYSIS_IDENTIFY_ERROR_CODE = "error_code";
    // 数据解析标识--reason
    public final static String DATA_ANALYSIS_IDENTIFY_REASON = "reason";
    // 数据解析标识--result
    public final static String DATA_ANALYSIS_IDENTIFY_RESULT = "result";
    // 数据解析标识--errorcode
    public final static String DATA_ANALYSIS_IDENTIFY_ERRORCODE = "errorcode";
    // 数据解析标识--errorMsg
    public final static String DATA_ANALYSIS_IDENTIFY_ERRORMSG = "errormsg";
    // 数据解析标识--dataInfo
    public final static String DATA_ANALYSIS_IDENTIFY_INFO = "info";
    // 状态标识--0
    public final static String STATUS_IDENTIFY_ZORE = "0";
    // 状态标识--1
    public final static String STATUS_IDENTIFY_ONE = "1";
    // 回调
    private ApiCallback<T> mCallback;
    // 页面弱引用
    private WeakReference<Activity> activityWeakRef;
    // type cls
    public Class<T> typeCls;
    // ui thread handler
    public Handler mHandler;
    // 请求标识
    public String requestId;
    // 异常
    public final static String EXCEPTION = "exception";
    // 空数据
    public final static String NULL_DATA  = "nodata";

    /**
     * Constructor
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2017/4/11
     * @param mCallback 回调
     * @param activity 页面实例
     * @param typeCls 数据类型
     * @param requestId 请求标识
     * @return
     */
    public CommonRetrofitCallback(ApiCallback<T> mCallback, Activity activity,
                                  Class<T> typeCls, String requestId){
        this.mCallback = mCallback;
        activityWeakRef = new WeakReference<Activity>(activity);
        this.typeCls = typeCls;
        mHandler = new Handler(Looper.getMainLooper());
        this.requestId = requestId;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (activityWeakRef == null
                || activityWeakRef.get() == null) {
            Log.e(TAG, "#" + ACTIVITY_WEAK_REF_IS_NULL);
            return;
        }
        // 处理是否当前页，如果非当前页则无需回调更新UI
        if (!AppManager.getInstance().isCurrent(activityWeakRef.get())){
            Log.e(TAG, "#" + UPDATE_UI_PAGE_IS_NOT_CURRENT_PAGE);
            return;
        }
        // 若数据为空则回调返回
        if (response == null || response.body() == null) {
            mCallback.onError(NULL_DATA);
            Log.e(TAG, "#" + NULL_DATA);
            return;
        }
        String body = (String) response.body();
        // 无数据回调处理
        if (StringUtils.isEmpty(body)){
            mCallback.onError(NULL_DATA);
            Log.e(TAG, "#" + NULL_DATA);
            return;
        }
        if (StringUtils.isNotEmpty(requestId)){
            switch (requestId){
                case REQUEST_ID_ONE:
                    // 数据格式一处理
                    Log.e(TAG, "#requestId=" + REQUEST_ID_ONE);
                    requestIdOneDeal(body);
                    return;
                case REQUEST_ID_TWO:
                    // 数据格式二处理
                    Log.e(TAG, "#requestId=" + REQUEST_ID_TWO);
                    requestIdTwoDeal(body);
                    return;
                case REQUEST_ID_THREE:
                    // 数据格式三处理
                    Log.e(TAG, "#requestId=" + REQUEST_ID_THREE);
                    requestIdThreeDeal(body);
                    return;
                default:
                    break;
            }
            Log.e(TAG, "#are you compatible data ?");
            // 兼容数据
            compatibleData();
        }else {
            Log.e(TAG, "#" + REQUESTID_IS_NULL);
            // 数据格式二处理
            Log.e(TAG, "#requestId=" + REQUEST_ID_TWO);
            requestIdTwoDeal(body);
        }
    }

    // 兼容数据
    public abstract void compatibleData();

    /**
     * 数据格式三处理
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param body
     * @return
     */
    private void requestIdThreeDeal(String body) {
        try {
            JSONObject mJson = new JSONObject(body);
            // 响应码
            String errorCode = mJson.optString(DATA_ANALYSIS_IDENTIFY_ERRORCODE);
            // 消息
            String errormsg = mJson.optString(DATA_ANALYSIS_IDENTIFY_ERRORMSG);
            // 结果信息
            String info = mJson.optString(DATA_ANALYSIS_IDENTIFY_INFO);
            BaseResponse baseResponse;
            // 请求成功
            if (STATUS_IDENTIFY_ZORE.equals(errorCode)
                    && StringUtils.isNotEmpty(info)){
                // Gson解析数据，回调数据
                baseResponse = (BaseResponse) new Gson().fromJson(info, typeCls);
                baseResponse.setSuccess(true);
                baseResponse.setErrorMsg(errormsg);
                baseResponse.setDataInfo(info);
                successCallBack(baseResponse);
            }else {
                // 请求错误（回调错误信息）
                errorCallBack(info);
            }
        } catch (JSONException e) {
            // 异常
            errorCallBack(EXCEPTION);
            e.printStackTrace();
        }
    }

    /**
     * 数据格式二处理
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param body 数据字符串
     * @return
     */
    private void requestIdTwoDeal(String body) {
        if (StringUtils.isNotEmpty(body)){
            successCallBack(body);
        }else {
            errorCallBack(NULL_DATA);
        }
    }

    /**
     * 数据格式一处理
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param body 数据字符串
     * @return
     */
    private void requestIdOneDeal(String body) {
        try {
            JSONObject mJson = new JSONObject(body);
            // 响应码
            String errorCode = mJson.optString(DATA_ANALYSIS_IDENTIFY_ERROR_CODE);
            // 消息
            String reason = mJson.optString(DATA_ANALYSIS_IDENTIFY_REASON);
            // 结果信息
            String result = mJson.optString(DATA_ANALYSIS_IDENTIFY_RESULT);
            // 改造后json字符串
            String remakeStr = remakeJsonData(body);
            // 获取需解析数据
            JSONObject remakeJsonObject = new JSONObject(remakeStr);
            String data = remakeJsonObject.optString(JK_JSON_NAME);
            // 请求成功
            if (STATUS_IDENTIFY_ZORE.equals(errorCode)
                    && StringUtils.isNotEmpty(result)){
                // Gson解析数据，回调数据
                BaseResponse baseResponse;
                baseResponse = (BaseResponse) new Gson().fromJson(data, typeCls);
                baseResponse.setSuccess(true);
                baseResponse.setErrorMsg(reason);
                baseResponse.setDataInfo(data);
                successCallBack(baseResponse);
            }else {
                // 请求错误（回调错误信息）
                errorCallBack(reason);
            }
        } catch (JSONException e) {
            // 异常
            errorCallBack(EXCEPTION);
            e.printStackTrace();
        }
    }

    /**
     * 改造json数据，增加一层标签
     * @author leibing
     * @createTime 2017/4/11
     * @lastModify 2017/4/11
     * @param jsonStr 改造前json字符串
     * @return jsonObject.toString 改造后json字符串
     */
    public static String remakeJsonData(String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JK_JSON_NAME, new JSONObject(jsonStr));
        return jsonObject.toString();
    }

    /**
     * 成功回调
     * @author leibing
     * @createTime 2017/4/10
     * @lastModify 2017/4/10
     * @param object
     * @return
     */
    private void successCallBack(final Object object){
        if (mCallback != null && mHandler != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (object != null){
                        mCallback.onSuccess((T) object);
                    }else {
                        mCallback.onSuccess(null);
                    }
                }
            });
        }
    }

    /**
     * 请求错误回调
     * @author leibing
     * @createTime 2017/4/10
     * @lastModify 2017/4/10
     * @param msg 错误消息
     * @return
     */
    private void errorCallBack(final String msg){
        if (mCallback != null && mHandler != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError(msg);
                }
            });
        }
    }

    /**
     * 请求失败回调
     * @author leibing
     * @createTime 2017/4/10
     * @lastModify 2017/4/10
     * @param
     * @return
     */
    private void failCallBack(){
        if (mCallback != null && mHandler != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onFailure();
                }
            });
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (activityWeakRef == null
                || activityWeakRef.get() == null) {
            Log.e(TAG, "#onFailure#" + ACTIVITY_WEAK_REF_IS_NULL);
            return;
        }
        // 失败回调
        failCallBack();
    }
}
