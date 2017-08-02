package com.xrouter;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by panda on 2017/8/1.
 */
public class MappingItemBean implements Parcelable {
    public String host;
    public String fragment;
    public String activity;
    public Bundle bundle;
    public int isLogin;

    protected MappingItemBean(Parcel in) {
        host = in.readString();
        fragment = in.readString();
        activity = in.readString();
        bundle = in.readBundle(getClass().getClassLoader());
        isLogin = in.readInt();
    }

    public static final Creator<MappingItemBean> CREATOR = new Creator<MappingItemBean>() {
        @Override
        public MappingItemBean createFromParcel(Parcel in) {
            return new MappingItemBean(in);
        }

        @Override
        public MappingItemBean[] newArray(int size) {
            return new MappingItemBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(host);
        dest.writeString(fragment);
        dest.writeString(activity);
        dest.writeBundle(bundle);
        dest.writeInt(isLogin);
    }
}
