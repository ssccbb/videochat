package com.qiiiqjk.kkanzh.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UGCVideoResult implements Parcelable{
    public String title;
    public String cover_url;
    public String video_url;
    public String total_viewer;
    public String comment_num;
    public String pick_num;
    public String name;
    public String anchor_name;
    public String uid;
    public String avatar;

    protected UGCVideoResult(Parcel in) {
        this.title = in.readString();
        this.cover_url = in.readString();
        this.video_url = in.readString();
        this.total_viewer = in.readString();
        this.comment_num = in.readString();
        this.pick_num = in.readString();
        this.name = in.readString();
        this.anchor_name = in.readString();
        this.uid = in.readString();
        this.avatar = in.readString();
    }

    public UGCVideoResult(HotVideoResult hotVideoResult) {
        this.title = hotVideoResult.title;
        this.cover_url = hotVideoResult.cover_url;
        this.video_url = hotVideoResult.video_url;
        this.total_viewer = hotVideoResult.total_viewer;
        this.comment_num = hotVideoResult.comment_num;
        this.pick_num = hotVideoResult.pick_num;
        this.name = hotVideoResult.name;
        this.anchor_name = hotVideoResult.anchor_name;
        this.uid = hotVideoResult.uid;
        this.avatar = hotVideoResult.uid;
    }

    public UGCVideoResult(AnchorInfoResult.VideoListBean hotVideoResult) {
        this.title = hotVideoResult.title;
        this.cover_url = hotVideoResult.cover_url;
        this.video_url = hotVideoResult.video_url;
        this.total_viewer = hotVideoResult.total_viewer;
        this.comment_num = hotVideoResult.comment_num;
        this.pick_num = hotVideoResult.pick_num;
        this.name = hotVideoResult.name;
        this.anchor_name = hotVideoResult.anchor_name;
        this.uid = hotVideoResult.uid;
        this.avatar = hotVideoResult.avatar;
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
        dest.writeString(this.comment_num);
        dest.writeString(this.pick_num);
        dest.writeString(this.name);
        dest.writeString(this.anchor_name);
        dest.writeString(this.uid);
        dest.writeString(this.avatar);
    }

    @Override
    public String toString() {
        return "ugc video param:\ntitle - "+title
                +"\nvideo_url - "+video_url
                +"\ncover_url - "+cover_url
                +"\ntotal_viewer - "+total_viewer
                +"\ncomment_num - "+comment_num
                +"\npick_num - "+pick_num
                +"\nname - "+name
                +"\nanchor_name - "+anchor_name
                +"\nuid - "+uid
                +"\navatar - "+avatar;
    }
}
