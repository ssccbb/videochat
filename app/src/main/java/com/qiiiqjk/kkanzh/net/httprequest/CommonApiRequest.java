package com.qiiiqjk.kkanzh.net.httprequest;

import com.qiiiqjk.kkanzh.App;
import com.qiiiqjk.kkanzh.common.Constants;
import com.qiiiqjk.kkanzh.net.httprequest.okhttp.OkHttpInterceptor;
import com.qiiiqjk.kkanzh.utils.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * @className:CommonApiRequest
 * @classDescription: retrofit api request
 * @author: leibing
 * @createTime: 2017/4/16
 */
public class CommonApiRequest {
    // sington
    private static CommonApiRequest instance;
    // Retrofit object
    private Retrofit retrofit;

    /**
     * Constructor
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param
     * @return
     */
    private CommonApiRequest(){
    }

    /**
     * sington
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param
     * @return
     */
    public static CommonApiRequest getInstance(){
        if (instance == null){
            instance = new CommonApiRequest();
        }
        return instance;
    }

    /**
     * create api instance
     * @author leibing
     * @createTime 2016/08/30
     * @lastModify 2016/08/30
     * @param service api class
     * @return
     */
    public <T> T create(Class<T> service, String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //服务器token鉴权添加
        Interceptor[] interceptors = configInterceptor();
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }
        //response截断器
        builder.addInterceptor(new OkHttpInterceptor());
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(CommonApiConvertFactory.create())
                .client(client)
                .build();
        return retrofit.create(service);
    }

    private Interceptor[] configInterceptor(){
        Interceptor[] ins = new Interceptor[1];
        Interceptor in = new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                //头部截断器
                long l = System.currentTimeMillis() / 1000;
                l = 5 * 60 + l + Utils.getLongValue(App.getContext(), Constants.EXTIME);
                String token = Utils.getToken(chain.request().method(),
                        chain.request().url().url().toString(),l);
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Extime",Long.toString(l))
                        .addHeader("apiVersion","1")
                        .addHeader("Apitoken",token)
                        .build();
                return chain.proceed(request);
            }

        };
        ins[0] = in;
        return ins;
    }
}
