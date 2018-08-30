package com.weiliao.kinnek.model;

import com.weiliao.kinnek.model.basemodel.XBaseModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HotHostResults extends XBaseModel {

    /**
     * next_page : 2
     * list : [{"uid":"5796cdd0caf4a","nickname":"壹","age":"18","sex":"0","avatar":"/Public/images/avatar/d22789671c5326fe.jpg","user_id":"888888","city":"月球","fee":"30","video_5s":"a.kinnekpro.com/Public/video/pre_8ce241e1ed84937ee48322b170b9b18c1534322197.mp4","anchor_label":"可爱,装逼","live_time":"98416","answer_rate":"100","anchor_state":"1"},{"uid":"5b78579ecfcd5","nickname":"②","age":"22","sex":"0","avatar":"/Public/images/avatar/892df4cce525f792.jpg","user_id":"883591","city":"江苏南京","fee":"20","video_5s":"a.kinnekpro.com/Public/video/pre_9f6992966d4c363ea0162a056cb45fe51534848724.mp4","anchor_label":"萌妹子,御姐","live_time":"2312","answer_rate":"98","anchor_state":"1"},{"uid":"5b797be080cb0","nickname":"仨","age":"22","sex":"0","avatar":"/Public/images/avatar/5065c4be434f61f8.jpg","user_id":"172119","city":"北京","fee":"2","video_5s":"a.kinnekpro.com/Public/video/pre_764a9f2462bf088af07b6ae6c107e62c1534612987.mp4","anchor_label":"御姐,萝莉","live_time":"236","answer_rate":"98","anchor_state":"1"},{"uid":"5b7bf050288d7","nickname":"三石","age":"12","sex":"0","avatar":"/Public/images/avatar/ed1290884e14b40c.png","user_id":"269139","city":"北京市","fee":"20","video_5s":"a.kinnekpro.com/Public/video/pre_71ddb91e8fa0541e426a54e538075a5a1534849331.mp4","anchor_label":"少妇","live_time":"1313","answer_rate":"100","anchor_state":"1"},{"uid":"5b7bf474d5717","nickname":"大橘子","age":"23","sex":"0","avatar":"/Public/images/avatar/1e3a180aaefb9741.jpg","user_id":"986282","city":"北京","fee":"23","video_5s":"a.kinnekpro.com/Public/video/pre_c37f9e1283cbd4a6edfd778fc8b1c6521534850242.mp4","anchor_label":"御姐,御姐,御姐","live_time":"2321","answer_rate":"98","anchor_state":"1"}]
     */

    public String next_page;
    public List<HotHostResult> list = new ArrayList<>();;

    public HotHostResults(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            code = jsonObject.getString("state");
            message = jsonObject.getString("msg");
            if (!jsonObject.has("data")) return;
            JSONObject data = jsonObject.getJSONObject("data");
            next_page = data.getString("next_page");
            JSONArray arr = data.getJSONArray("list");
            Gson gson = new Gson();
            for (int i = 0; i < arr.length(); i++) {
                HotHostResult hotHostResult = gson.fromJson(arr.getJSONObject(i).toString(), HotHostResult.class);
                hotHostResult.position = i;
                list.add(hotHostResult);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getNext_page() {
        return next_page;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public List<HotHostResult> getList() {
        return list;
    }

    public void setList(List<HotHostResult> list) {
        this.list = list;
    }

    public static class HotHostResult extends XBaseModel {
        /**
         * uid : 5796cdd0caf4a
         * nickname : 壹
         * age : 18
         * sex : 0
         * avatar : /Public/images/avatar/d22789671c5326fe.jpg
         * user_id : 888888
         * city : 月球
         * fee : 30
         * video_5s : a.kinnekpro.com/Public/video/pre_8ce241e1ed84937ee48322b170b9b18c1534322197.mp4
         * anchor_label : 可爱,装逼
         * live_time : 98416
         * answer_rate : 100
         * anchor_state : 1
         */

        public int position;
        public String uid;
        public String nickname;
        public String age;
        public String sex;
        public String avatar;
        public String user_id;
        public String city;
        public String fee;
        public String video_5s;
        public String anchor_label;
        public String live_time;
        public String answer_rate;
        public String anchor_state;//1空闲 2在聊 3勿扰

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

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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

        public String getAnchor_state() {
            return anchor_state;
        }

        public void setAnchor_state(String anchor_state) {
            this.anchor_state = anchor_state;
        }
    }
}
