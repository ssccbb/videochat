package com.qiiiqjk.kkanzh.model;

import com.google.gson.annotations.SerializedName;
import com.qiiiqjk.kkanzh.model.basemodel.XBaseModel;

public class UniformWeixinPayResult extends XBaseModel {
    public String str;
    /**
     * prepayid : wx031723167141916e4eb802971281650842
     * partnerid : 1513603681
     * appid : wxf5a095d2bd305d89
     * package : Sign=WXPay
     * timestamp : 1535966596
     * noncestr : 2680361588
     * sign : 1B16469B402941676E9947A20ABEADE4
     */

    public String prepayid;
    public String partnerid;
    public String appid;
    @SerializedName("package")
    public String packageX;
    public String timestamp;
    public String noncestr;
    public String sign;

    @Override
    public String toString() {
        return str;
    }

}
