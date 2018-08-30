package com.weiliao.kinnek.model;

import com.weiliao.kinnek.model.basemodel.XBaseModel;

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
    public String signature;
    public String age;
    public String sex;
    public String emotion;
    public String vip_ttl;

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
            signature = data.getString("signature");
            age = data.getString("age");
            sex = data.getString("sex");
            emotion = data.getString("emotion");
            vip_ttl = data.getString("vip_ttl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
