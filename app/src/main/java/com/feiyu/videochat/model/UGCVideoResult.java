package com.feiyu.videochat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UGCVideoResult implements Parcelable{
    public String title;
    public String cover_url;
    public String video_url;
    public String total_viewer;

    protected UGCVideoResult(Parcel in) {
        this.title = in.readString();
        this.cover_url = in.readString();
        this.video_url = in.readString();
        this.total_viewer = in.readString();
    }

    public UGCVideoResult(HotVideoResult hotVideoResult) {
        this.title = hotVideoResult.title;
        this.cover_url = hotVideoResult.cover_url;
        this.video_url = hotVideoResult.video_url;
        this.total_viewer = hotVideoResult.total_viewer;
    }

    public static final Creator<UGCVideoResult> CREATOR = new Creator<UGCVideoResult>() {
        @Override
        public UGCVideoResult createFromParcel(Parcel in) {
            return new UGCVideoResult(in);
        }

        @Override
        public UGCVideoResult[] newArray(int size) {
            return new UGCVideoResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.cover_url);
        dest.writeString(this.video_url);
        dest.writeString(this.total_viewer);
    }
}
