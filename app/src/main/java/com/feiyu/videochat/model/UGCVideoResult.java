package com.feiyu.videochat.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UGCVideoResult implements Parcelable{
    public int position;

    protected UGCVideoResult(Parcel in) {
    }

    public UGCVideoResult(int position) {
        this.position = position;
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
    }
}
