package com.weiliao.kinnek.model;

import com.weiliao.kinnek.model.basemodel.XBaseModel;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SpecialResults extends XBaseModel {

    @SerializedName("list")
    public List<SpecialItem> list;

    @Override
    public boolean isNull() {
        return list == null || list.isEmpty();
    }

    public static class SpecialItem {
        public int id;
        public String name;
    }
}
