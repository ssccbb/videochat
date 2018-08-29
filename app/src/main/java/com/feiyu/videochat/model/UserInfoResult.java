package com.feiyu.videochat.model;

import com.feiyu.videochat.model.basemodel.XBaseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoResult extends XBaseModel {


    /**
     * uid : 1
     * nickname : 草莓
     * user_id : 12313
     * city : 杭州
     * level : 1
     * diamond : 11313
     * vip : 0
     */

    public String uid;
    public String nickname;
    public String user_id;
    public String city;
    public String level;
    public String diamond;
    public String vip;

    public UserInfoResult(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            code = jsonObject.getString("state");
            message = jsonObject.getString("msg");
            if (!jsonObject.has("data")) return;
            JSONObject data = jsonObject.getJSONObject("data");
            uid = data.getString("uid");
            nickname = data.getString("nickname");
            user_id = data.getString("user_id");
            city = data.getString("city");
            level = data.getString("level");
            diamond = data.getString("diamond");
            vip = data.getString("vip");
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDiamond() {
        return diamond;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }
}
