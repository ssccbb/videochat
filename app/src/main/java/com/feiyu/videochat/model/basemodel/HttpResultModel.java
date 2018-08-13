package com.feiyu.videochat.model.basemodel;

import com.feiyu.videochat.net.StateCode;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.net.IModel;

/**
 * Created by zr on 2017-10-13.
 */
public class HttpResultModel<T> extends XBaseModel {
    public T data;

    @Override
    public boolean isNull() {
        if (isSucceful())
            return false;
        if (data instanceof IModel)
            return ((IModel) data).isNull();
        return Kits.Empty.check(data);
    }

    @Override
    public boolean isAuthError() {
        return code.equals(StateCode.STATE_0006);//登录错误（验证错误）
    }

    @Override
    public boolean isSucceful() {
        return super.isSucceful();
    }

    @Override
    public boolean isBizError() {
        return super.isBizError();//业务错误
    }

    @Override
    public String getResponseMsg() {
        return StateCode.getMessage(code);
    }
}
