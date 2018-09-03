package com.qiiiqjk.kkanzh.model;

import android.util.Log;

import com.google.gson.Gson;
import com.qiiiqjk.kkanzh.model.basemodel.XBaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PayChargeItemResult extends XBaseModel {

    /**
     * data : [{"id":"1","price":"9800","diamond":"980"},{"id":"2","price":"9800","diamond":"980"},{"id":"3","price":"9800","diamond":"980"},{"id":"4","price":"9800","diamond":"980"}]
     * state : 0
     * msg : 请求成功
     */

    public List<PayChargeItem> data = new ArrayList<>();

    public PayChargeItemResult(String json) {
        try {
            JSONObject object = new JSONObject(json);
            code = object.getString("state");
            message = object.getString("msg");
            if (!object.has("data")) return;
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                Gson gson = new Gson();
                PayChargeItem payChargeItem = gson.fromJson(jsonObject.toString(), PayChargeItem.class);
                this.data.add(payChargeItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(PayChargeItemResult.class.getSimpleName(), "PayChargeItemResult: "+e.toString() );
        }
    }

    public static class PayChargeItem {
        /**
         * id : 1
         * price : 9800
         * diamond : 980
         */

        public String id;
        public String price;
        public String diamond;
    }
}
