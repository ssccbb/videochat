package com.feiyu.videochat.net.body;

import java.io.Serializable;

public class BaseRequestBody implements Serializable {
    public int page;

    public BaseRequestBody(int page) {
        this.page = page;
    }

    public static class Builder {
        public int page;

        public Builder(int page) {
            this.page = page;
        }

        public Builder setPage(int page) {
            this.page = page;
            return this;
        }

        public BaseRequestBody builder() {
            return new BaseRequestBody(page);
        }
    }
}
