//package com.weiliao.kinnek.utils;
//
//import com.tencent.mm.opensdk.modelpay.PayReq;
//import com.weiliao.kinnek.model.UniformWeixinPayResult;
//
///**
// * 微信支付utils
// */
//public class WXPayUtils {
//
//
//    /**
//     * 微信支付调用
//     */
//    public static PayReq weChatPay(UniformWeixinPayResult wxpayBean) {
//        PayReq req = new PayReq();
//        req.appId = wxpayBean.appid;
//        req.partnerId = wxpayBean.partnerid;
//        req.prepayId = wxpayBean.prepayid;
//        req.packageValue = wxpayBean.packageX;
//        req.nonceStr = wxpayBean.noncestr;
//        req.timeStamp = wxpayBean.timestamp;
//        req.sign = wxpayBean.sign;
//
//        return req;
//    }
//
//
//}
