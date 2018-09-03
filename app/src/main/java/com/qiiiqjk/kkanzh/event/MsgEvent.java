package com.qiiiqjk.kkanzh.event;

public class MsgEvent<T> {

    private T data;

    private String mMsg;
    private int type;
    private int requestCode;

    public MsgEvent(T data) {
        this.data = data;
    }

    public MsgEvent(int requestCode, int type, String msg) {
        this.type = type;
        this.mMsg = msg;
        this.requestCode = requestCode;
    }

    public String getMsg() {
        return mMsg;
    }

    public int getType() {
        return type;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public T getData() {
        return data;
    }

}