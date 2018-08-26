package com.feiyu.videochat.model;

import com.feiyu.videochat.model.basemodel.XBaseModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class IDResult extends XBaseModel {

    /**
     * cover_url : http://
     * link_url : http://
     */

    public String cover_url = "";
    public String link_url = "";

    public IDResult(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject data = jsonObject.getJSONObject("data");
            cover_url = data.getString("cover_url");
            link_url = data.getString("link_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
