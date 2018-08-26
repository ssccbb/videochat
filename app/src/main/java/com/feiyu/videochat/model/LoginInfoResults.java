package com.feiyu.videochat.model;

import com.feiyu.videochat.model.basemodel.XBaseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginInfoResults extends XBaseModel {

    /**
     * uid : de7ee638
     * phone : 110
     * user_id : 131313
     */

    public String uid;
    public String phone;
    public String user_id;

    public LoginInfoResults(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            code = jsonObject.getString("state");
            message = jsonObject.getString("msg");
            if (!jsonObject.has("data")) return;
            JSONObject data = jsonObject.getJSONObject("data");
            uid = data.getString("uid");
            phone = data.getString("phone");
            user_id = data.getString("user_id");
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
