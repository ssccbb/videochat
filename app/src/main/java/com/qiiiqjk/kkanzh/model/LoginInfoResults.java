package com.qiiiqjk.kkanzh.model;

import com.qiiiqjk.kkanzh.model.basemodel.XBaseModel;

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
    public String vip;
    public String diamond;

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
            vip = data.getString("vip");
            diamond = data.getString("diamond");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
