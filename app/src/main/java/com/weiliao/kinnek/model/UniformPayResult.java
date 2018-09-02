package com.weiliao.kinnek.model;

import android.util.Log;

import com.google.gson.Gson;
import com.weiliao.kinnek.model.basemodel.XBaseModel;
import com.weiliao.kinnek.net.StateCode;

import org.json.JSONException;
import org.json.JSONObject;

public class UniformPayResult extends XBaseModel {

    /**
     * data : {"data":{"wx":[],"alipay":{"uid":"c16907d6b51fc7aabaa2ef3a","price":"0.01","istype":"1","orderid":"5b7d2784b4889fd1ce6f213f3","orderuid":"5b7d2784b4889","goodsname":"购买钻石","key":"3488563a865057ad699e8f200ff5e004","notify_url":"http://a.kinnekpro.com/pay/notify_url","return_url":"http://a.kinnekpro.com/pay/notify_url"}}}
     * state : 0
     * msg : 请求成功
     */

    public UniformAlipayPayResult alipay;
    public UniformWeixinPayResult wx;

    public UniformPayResult(String json) {
        try {
            JSONObject object = new JSONObject(json);
            code = object.getString("state");
            message = object.getString("msg");
            if (code.equals(StateCode.STATE_0000)){
                JSONObject data1 = object.getJSONObject("data");
                JSONObject data = data1.getJSONObject("data");
                JSONObject alipayJson = data.getJSONObject("alipay");
                //JSONObject wxJson = data.getJSONObject("wx");
                Gson gson = new Gson();
                alipay = gson.fromJson(alipayJson.toString(),UniformAlipayPayResult.class);
                //wx = gson.fromJson(wxJson.toString(),UniformWeixinPayResult.class);
                alipay.str = alipayJson.toString();
                //wx.str = wxJson.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("UniformPayResult", "UniformPayResult: "+e.toString() );
        }
    }
}
