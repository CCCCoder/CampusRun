package com.n1njac.yiqipao.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by N1njaC on 2017/9/22.
 */

public class LocationBean implements Parcelable {

    private double latitude;
    private double longitude;
    private float bear;
    private float speed;
    private long time;

    protected LocationBean(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        bear = in.readFloat();
        speed = in.readFloat();
        time = in.readLong();
    }

    public static final Creator<LocationBean> CREATOR = new Creator<LocationBean>() {
        @Override
        public LocationBean createFromParcel(Parcel in) {
            return new LocationBean(in);
        }

        @Override
        public LocationBean[] newArray(int size) {
            return new LocationBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeFloat(bear);
        dest.writeFloat(speed);
        dest.writeLong(time);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getBear() {
        return bear;
    }

    public void setBear(float bear) {
        this.bear = bear;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
