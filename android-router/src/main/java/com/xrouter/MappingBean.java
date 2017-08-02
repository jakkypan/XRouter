package com.xrouter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by panda on 2017/8/1.
 */
public class MappingBean implements Parcelable {
    public int version;
    public String schema;
    public String loginActivity;
    public MappingItemBean[] mappings;

    protected MappingBean(Parcel in) {
        version = in.readInt();
        schema = in.readString();
        loginActivity = in.readString();
        mappings = in.createTypedArray(MappingItemBean.CREATOR);
    }

    public static final Creator<MappingBean> CREATOR = new Creator<MappingBean>() {
        @Override
        public MappingBean createFromParcel(Parcel in) {
            return new MappingBean(in);
        }

        @Override
        public MappingBean[] newArray(int size) {
            return new MappingBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(version);
        dest.writeString(schema);
        dest.writeString(loginActivity);
        dest.writeTypedArray(mappings, flags);
    }
}
