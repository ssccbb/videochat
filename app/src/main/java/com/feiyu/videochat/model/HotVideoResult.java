package com.feiyu.videochat.model;

import com.feiyu.videochat.model.basemodel.XBaseModel;

public class HotVideoResult extends XBaseModel {

    /**
     * title : 有礼享.心五动1111111
     * cover_url : /Public/images/video_cover/20180823/ee640bf092a36e15.png
     * video_url : /Public/video/20180823/pre_5265d33c184af566aeb7ef8afd0b9b031534992570.mp4
     * total_viewer : 231111
     */

    public int type;//0-本地添加 1-网络
    public int position;
    public String title;
    public String cover_url;
    public String video_url;
    public String total_viewer;

    public HotVideoResult(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getTotal_viewer() {
        return total_viewer;
    }

    public void setTotal_viewer(String total_viewer) {
        this.total_viewer = total_viewer;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
