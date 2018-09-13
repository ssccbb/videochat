package com.weiliao.kinnek.model;

import com.weiliao.kinnek.model.basemodel.XBaseModel;

import java.util.List;

public class HotWordResults extends XBaseModel {

    public List<HotWordItem> list;

    public class HotWordItem extends XBaseModel {
        public int id;
        public String name;
    }
}
