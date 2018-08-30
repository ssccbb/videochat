package com.weiliao.kinnek.model;

import com.weiliao.kinnek.model.basemodel.XBaseModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BannerResults extends XBaseModel {

    public List<Banner> revolve_list = new ArrayList<>();;

    public BannerResults(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            code = jsonObject.getString("state");
            message = jsonObject.getString("msg");
            if (!jsonObject.has("data")) return;
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray array = data.getJSONArray("revolve_list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Banner banner = new Banner(object);
                revolve_list.add(banner);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Banner> getRevolve_list() {
        return revolve_list;
    }

    public void setRevolve_list(List<Banner> revolve_list) {
        this.revolve_list = revolve_list;
    }

    public static class Banner {
        /**
         * cover_url : a.kinnekpro.com/Public/images/banner/7b9060a65804efac.jpg
         * type : 3
         * data : {"link_url":"https://www.baidu.com/"}
         * inner : 0
         */

        public String cover_url = null;
        public String type;
        public String inner;
        public String link_url;

        public Banner() {
        }

        public Banner(JSONObject jsonObject) {
            try {
                cover_url = jsonObject.getString("cover_url");
                type = jsonObject.getString("type");
                JSONObject data = jsonObject.getJSONObject("data");
                link_url = data.getString("link_url");
                inner = jsonObject.getString("inner");
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getInner() {
            return inner;
        }

        public void setInner(String inner) {
            this.inner = inner;
        }

        public String getLink_url() {
            return link_url;
        }

        public void setLink_url(String link_url) {
            this.link_url = link_url;
        }
    }
}
