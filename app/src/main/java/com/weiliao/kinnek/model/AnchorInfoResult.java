package com.weiliao.kinnek.model;

import android.util.Log;

import com.weiliao.kinnek.model.basemodel.XBaseModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnchorInfoResult extends XBaseModel {

    /**
     * uid : 5b78579ecfcd5
     * nickname : ②
     * age : 22
     * sex : 0
     * city : 江苏南京
     * avatar : a.kinnekpro.com/Public/images/avatar/20180824/5e56c3ace9f34fe0.jpg
     * user_id : 883591
     * signature : 萌妹子，御姐萌妹子，御姐萌妹子，御姐
     * fee : 20
     * video_5s : a.kinnekpro.com/Public/video/pre_9f6992966d4c363ea0162a056cb45fe51534848724.mp4
     * anchor_label : 萌妹子,御姐
     * live_time : 2312
     * answer_rate : 98
     * answer_state : 1
     * pay_diamond : 123
     * is_pay : 0
     * pic_count : 10
     * video_count : 10
     * pic_list : ["a.kinnekpro.com/Public/images/anchor_pic/20180823/9560509840bfd032.png","a.kinnekpro.com/Public/images/anchor_pic/20180823/9560509840bfd032.png"]
     * video_list : [{"cover_url":"a.kinnekpro.com/Public/images/video_cover/20180824/7ecbc8d381118796.jpg","video_url":"a.kinnekpro.com/Public/video/20180823/pre_5265d33c184af566aeb7ef8afd0b9b031534992570.mp4"}]
     */

    public String uid;
    public String nickname;
    public String age;
    public String sex;
    public String city;
    public String avatar;
    public String user_id;
    public String signature;
    public String fee;
    public String video_5s;
    public String anchor_label;
    public String live_time;
    public String online_time;
    public String answer_rate;
    public String answer_state;
    public String pay_diamond;
    public String is_pay;
    public String pic_count;
    public String video_count;
    public List<String> pic_list = new ArrayList<>();
    public List<VideoListBean> video_list = new ArrayList<>();
    public String is_watch;//1观看0没看
    public String diamond;
    public int pay_status = -1;//识别当前登录用户是否买断主播（需要自己调接口设置）

    public AnchorInfoResult(String json) {
        try {
            JSONObject object = new JSONObject(json);
            code = object.getString("state");
            message = object.getString("msg");
            if (!object.has("data")) return;
            JSONObject jsonObject = object.getJSONObject("data");
            uid = jsonObject.getString("uid");
            nickname = jsonObject.getString("nickname");
            age = jsonObject.getString("age");
            sex = jsonObject.getString("sex");
            city = jsonObject.getString("city");
            avatar = jsonObject.getString("avatar");
            user_id = jsonObject.getString("user_id");
            signature = jsonObject.getString("signature");
            fee = jsonObject.getString("fee");
            video_5s = jsonObject.getString("video_5s");
            anchor_label = jsonObject.getString("anchor_label");
            live_time = jsonObject.getString("live_time");
            online_time = jsonObject.getString("online_time");
            answer_rate = jsonObject.getString("answer_rate");
            answer_state = jsonObject.getString("anchor_state");
            pay_diamond = jsonObject.getString("pay_diamond");
            is_pay = jsonObject.getString("is_pay");
            pic_count = jsonObject.getString("pic_count");
            video_count = jsonObject.getString("video_count");
            is_watch = jsonObject.getString("is_watch");
            diamond = jsonObject.getString("diamond");
            Gson gson = new Gson();
            JSONArray videos = jsonObject.getJSONArray("video_list");
            for (int i = 0; i < videos.length(); i++) {
                JSONObject jsonObject1 = videos.getJSONObject(i);
                VideoListBean video = gson.fromJson(jsonObject1.toString(), VideoListBean.class);
                video_list.add(video);
            }
            JSONArray pics = jsonObject.getJSONArray("pic_list");
            for (int i = 0; i < pics.length(); i++) {
                pic_list.add(pics.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(AnchorInfoResult.class.getSimpleName(), "AnchorInfoResult: "+e.toString() );
        }
    }

    public static class VideoListBean {
        /**
         * cover_url : a.kinnekpro.com/Public/images/video_cover/20180824/7ecbc8d381118796.jpg
         * video_url : a.kinnekpro.com/Public/video/20180823/pre_5265d33c184af566aeb7ef8afd0b9b031534992570.mp4
         * title : "看球吗？少年"
         * total_viewer : 2134
         */

        public String cover_url;
        public String video_url;
        public String title;
        public String total_viewer;
        public String comment_num;
        public String pick_num;
        public String anchor_name;
        public String name;
        public String uid;
        public String avatar;
        public String comment;
    }
}
