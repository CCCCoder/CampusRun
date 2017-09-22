package com.n1njac.yiqipao.android.utils;

import com.amap.api.maps.model.LatLng;
import com.n1njac.yiqipao.android.bean.LocationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by N1njaC on 2017/9/22.
 */

public class ParseUtil {


    public static List<LocationBean> parseLatLng2Bean(List<LatLng> linePoints) {

        List<LocationBean> list = new ArrayList<>();
        for (LatLng latLng : linePoints) {
            LocationBean location = new LocationBean();
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            list.add(location);
        }
        return list;
    }

    public static List<LatLng> parseBean2LatLng(List<LocationBean> locations) {

        List<LatLng> latLngList = new ArrayList<>();

        for (LocationBean location : locations) {

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            latLngList.add(latLng);
        }

        return latLngList;
    }

}
