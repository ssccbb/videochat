package com.feiyu.videochat.model;

import com.feiyu.videochat.model.basemodel.XBaseModel;

import java.util.List;

public class HotWordResults extends XBaseModel {

    public List<HotWordItem> list;

    public class HotWordItem extends XBaseModel {
        public int id;
        public String name;
    }
}
