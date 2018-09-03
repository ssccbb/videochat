package com.qiiiqjk.kkanzh.model;

import com.qiiiqjk.kkanzh.model.basemodel.XBaseModel;

public class UniformAlipayPayResult extends XBaseModel {

    /**
     * uid : c16907d6b51fc7aabaa2ef3a
     * price : 0.01
     * istype : 1
     * orderid : 5b7d2784b4889fd1ce6f213f3
     * orderuid : 5b7d2784b4889
     * goodsname : 购买钻石
     * key : 3488563a865057ad699e8f200ff5e004
     * notify_url : http://a.kinnekpro.com/pay/notify_url
     * return_url : http://a.kinnekpro.com/pay/notify_url
     */

    public String uid;
    public String price;
    public String istype;
    public String orderid;
    public String orderuid;
    public String goodsname;
    public String key;
    public String notify_url;
    public String return_url;

    public String str;

    @Override
    public String toString() {
        return str;
    }
}
