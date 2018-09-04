package com.qiiiqjk.kkanzh.model;

import com.qiiiqjk.kkanzh.model.basemodel.XBaseModel;

public class HotVideoResult extends XBaseModel {

    /**
     * title : 有礼享.心五动1111111
     * cover_url : /Public/images/video_cover/20180823/ee640bf092a36e15.png
     * video_url : /Public/video/20180823/pre_5265d33c184af566aeb7ef8afd0b9b031534992570.mp4
     * total_viewer : 231111
     */

    /**
     * 需要注意⚠️ 如果此处字段更改 请同步更改UGCVideoResult
     * */

    public int type;//0-本地添加 1-网络
    public int position;
    public String title;
    public String cover_url;
    public String video_url;
    public String total_viewer;
    public String comment_num;
    public String pick_num;
    public String name;
    public String anchor_name;
    public String uid;
    public String avatar;

    public HotVideoResult(int type) {
        this.type = type;
    }
}
