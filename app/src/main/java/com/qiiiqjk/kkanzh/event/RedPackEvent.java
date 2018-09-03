package com.qiiiqjk.kkanzh.event;

public class RedPackEvent {

    private int data;

    private int type;
    private int requestCode;

    public RedPackEvent(int data) {
        this.data = data;
    }

    public RedPackEvent(int requestCode, int type, int data) {
        this.type = type;
        this.data = data;
        this.requestCode = requestCode;
    }

    public int getType() {
        return type;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public int getData() {
        return data;
    }

}