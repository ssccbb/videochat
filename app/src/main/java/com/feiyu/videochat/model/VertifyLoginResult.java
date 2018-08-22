package com.feiyu.videochat.model;

import com.feiyu.videochat.net.httpresponse.BaseResponse;

import java.io.Serializable;

public class VertifyLoginResult extends BaseResponse implements Serializable {
    private static final long serialVersionUID = 4863726647304575308L;
    /**
     * state : 0
     * msg : 请求成功
     * data : {"uid":"de7ee638","phone":"110","user_id":"131313"}
     */

    public int state;
    public String msg;
    public DataBean data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : de7ee638
         * phone : 110
         * user_id : 131313
         */

        public String uid;
        public String phone;
        public String user_id;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
