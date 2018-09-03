package com.qiiiqjk.kkanzh.model;

import com.qiiiqjk.kkanzh.net.httpresponse.BaseResponse;

import java.io.Serializable;

public class PhoneVertifyResult extends BaseResponse implements Serializable{
    // 序列化UID 用于反序列化
    private static final long serialVersionUID = 4863726647304575308L;

    /**
     * state : 9001
     * msg : 请求失败
     */

    public String state;
    public String msg;

    public PhoneVertifyResult(String state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
