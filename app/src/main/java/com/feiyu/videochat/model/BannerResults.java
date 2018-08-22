package com.feiyu.videochat.model;

import com.feiyu.videochat.net.httpresponse.BaseResponse;

import java.io.Serializable;

public class BannerResults extends BaseResponse implements Serializable {
    // 序列化UID 用于反序列化
    private static final long serialVersionUID = 4863726647304575308L;


    /**
     * state : 0
     * msg : 请求成功
     * data : {"cover_url":"http://","link_url":"http://"}
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
         * cover_url : http://
         * link_url : http://
         */

        public String cover_url;
        public String link_url;

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }
    }
}
