package com.weiliao.kinnek.net.httprequest.okhttp;

import android.util.Log;

import com.weiliao.kinnek.net.TripleDES;

import java.io.IOException;
import java.nio.charset.Charset;

import cn.droidlover.xdroidmvp.log.XLog;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @className: OkHttpInterceptor
 * @classDescription: Http拦截器
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class OkHttpInterceptor implements Interceptor {
    // 日志标识
    private final static String TAG = "OkHttpInterceptor";
    // utf8
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        // 获得Connection，内部有route、socket、handshake、protocol方法
        Connection connection = chain.connection();
        // 如果Connection为null，返回HTTP_1_1，否则返回connection.protocol()
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        // 比如: --> POST http://121.40.227.8:8088/api http/1.1
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        //Log.e(TAG, "#requestStartMessage=" + requestStartMessage);
        // 打印 Response
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            throw e;
        }
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        if (bodyEncoded(response.headers())) {
            //Log.e(TAG,"#bodyEncoded");
        } else {
            BufferedSource source = responseBody.source();
            // Buffer the entire body.
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            if (contentLength != 0) {
                // 获取Response的body的字符串 并打印
                //Log.e(TAG,"#body=" + buffer.readString(charset));

                //转码
                try {
                    MediaType mediaType = responseBody.contentType();
                    if (mediaType != null) {
                        if (isText(mediaType)) {
                            String resp = new String(chain.request().method().equals("POST")
                                    ? TripleDES.decryptMode(buffer.readString(charset)).getBytes() : responseBody.bytes(), "UTF-8");
                            responseBody = ResponseBody.create(mediaType, resp);
                            //Log.e(TAG, "#body= "+resp );
                            return response.newBuilder().body(responseBody).build();
                        } else {
                            XLog.d(TAG, "data : " + " maybe [file part] , too large too print , ignored!");
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "intercept: "+e.toString() );
                }
            }
        }
        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType == null) return false;

        return ("text".equals(mediaType.subtype())
                || "json".equals(mediaType.subtype())
                || "xml".equals(mediaType.subtype())
                || "html".equals(mediaType.subtype())
                || "webviewhtml".equals(mediaType.subtype())
                || "x-www-form-urlencoded".equals(mediaType.subtype()));
    }
}
