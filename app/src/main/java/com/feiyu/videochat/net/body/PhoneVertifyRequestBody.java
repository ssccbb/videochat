package com.feiyu.videochat.net.body;

public class PhoneVertifyRequestBody extends BaseRequestBody{
    public String phone;

    public PhoneVertifyRequestBody(String phone) {
        super(0);
        this.phone = phone;
    }
}
