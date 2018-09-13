package com.weiliao.kinnek.net.httpresponse;

import java.io.Serializable;

/**
 * @className: BaseResponse
 * @classDescription: 网络请求返回对象公共抽象类
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class BaseResponse implements Serializable {
    // 序列化UID 用于反序列化
    private static final long serialVersionUID = 234513596098152098L;
    // 是否成功
    private boolean isSuccess;
    // 数据
    public String dataInfo;
    // 错误消息
    public String errorMsg;

    /**------------------------------------get and set---------------------------------------------
     -----------------------------------------------------------------------------------*/

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getDataInfo() {
        return dataInfo;
    }

    public void setDataInfo(String dataInfo) {
        this.dataInfo = dataInfo;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
