package com.weiliao.kinnek.model;

import com.weiliao.kinnek.model.basemodel.XBaseModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HotVideoResults extends XBaseModel{

    /**
     * next_page : 2
     * list : [{"title":"有礼享.心五动1111111","cover_url":"/Public/images/video_cover/20180823/ee640bf092a36e15.png","video_url":"/Public/video/20180823/pre_5265d33c184af566aeb7ef8afd0b9b031534992570.mp4","total_viewer":"231111"},{"title":"MM","cover_url":"/Public/images/video_cover/574ee28b0c833832.jpg","video_url":"/Public/video/pre_b80ba73857eed2a36dc7640e2310055a1534850937.mp4","total_viewer":"231"},{"title":"231234214","cover_url":"/Public/images/video_cover/59027cc9b0204b26.jpg","video_url":"/Public/video/pre_b7f1f29db7c23648f2bb8d6a8ee0469b1534838705.mp4","total_viewer":"1231"}]
     */

    public String next_page;
    public List<HotVideoResult> list = new ArrayList<>();

    public HotVideoResults(String json) {
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
                HotVideoResult hotVideoResult = gson.fromJson(arr.getJSONObject(i).toString(), HotVideoResult.class);
                hotVideoResult.position = i;
                hotVideoResult.type = 1;
                list.add(hotVideoResult);
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

    public List<HotVideoResult> getList() {
        return list;
    }

    public void setList(List<HotVideoResult> list) {
        this.list = list;
    }
}
