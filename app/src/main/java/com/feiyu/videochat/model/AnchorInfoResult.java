package com.feiyu.videochat.model;

import com.feiyu.videochat.model.basemodel.XBaseModel;
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
            JSONArray pics = jsonObject.getJSONArray("pic_list");
            for (int i = 0; i < pics.length(); i++) {
                pic_list.add(pics.getString(i));
            }
            Gson gson = new Gson();
            JSONArray videos = jsonObject.getJSONArray("video_list");
            for (int i = 0; i < videos.length(); i++) {
                JSONObject jsonObject1 = videos.getJSONObject(i);
                VideoListBean video = gson.fromJson(jsonObject1.toString(), VideoListBean.class);
                video_list.add(video);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getVideo_5s() {
        return video_5s;
    }

    public void setVideo_5s(String video_5s) {
        this.video_5s = video_5s;
    }

    public String getAnchor_label() {
        return anchor_label;
    }

    public void setAnchor_label(String anchor_label) {
        this.anchor_label = anchor_label;
    }

    public String getLive_time() {
        return live_time;
    }

    public void setLive_time(String live_time) {
        this.live_time = live_time;
    }

    public String getAnswer_rate() {
        return answer_rate;
    }

    public void setAnswer_rate(String answer_rate) {
        this.answer_rate = answer_rate;
    }

    public String getAnswer_state() {
        return answer_state;
    }

    public void setAnswer_state(String answer_state) {
        this.answer_state = answer_state;
    }

    public String getPay_diamond() {
        return pay_diamond;
    }

    public void setPay_diamond(String pay_diamond) {
        this.pay_diamond = pay_diamond;
    }

    public String getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(String is_pay) {
        this.is_pay = is_pay;
    }

    public String getPic_count() {
        return pic_count;
    }

    public void setPic_count(String pic_count) {
        this.pic_count = pic_count;
    }

    public String getVideo_count() {
        return video_count;
    }

    public void setVideo_count(String video_count) {
        this.video_count = video_count;
    }

    public List<String> getPic_list() {
        return pic_list;
    }

    public void setPic_list(List<String> pic_list) {
        this.pic_list = pic_list;
    }

    public List<VideoListBean> getVideo_list() {
        return video_list;
    }

    public void setVideo_list(List<VideoListBean> video_list) {
        this.video_list = video_list;
    }

    public String getOnline_time() {
        return online_time;
    }

    public void setOnline_time(String online_time) {
        this.online_time = online_time;
    }

    public int getPay_status() {
        return pay_status;
    }

    public void setPay_status(int pay_status) {
        this.pay_status = pay_status;
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
        public String name;
        public String uid;
        public String avatar;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTotal_viewer() {
            return total_viewer;
        }

        public void setTotal_viewer(String total_viewer) {
            this.total_viewer = total_viewer;
        }

        public String getComment_num() {
            return comment_num;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
        }

        public String getPick_num() {
            return pick_num;
        }

        public void setPick_num(String pick_num) {
            this.pick_num = pick_num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
