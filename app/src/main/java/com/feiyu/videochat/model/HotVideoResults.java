package com.feiyu.videochat.model;

import com.feiyu.videochat.model.basemodel.XBaseModel;

import java.util.List;

public class HotVideoResults extends XBaseModel {
    public String name;
    public int audience;
    public int position;
    public String cover;
    public int status;

    public HotVideoResults(int position) {
        this.position = position;
    }
}
