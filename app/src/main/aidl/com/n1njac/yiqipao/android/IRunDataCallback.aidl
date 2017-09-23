// IRunDataCallback.aidl
package com.n1njac.yiqipao.android;

import com.n1njac.yiqipao.android.bean.LocationBean;

interface IRunDataCallback {

    void onDistanceChange(int distance);
    void onSpeedChange(float speed);
    void onLocationChange(in List<LocationBean> locations);
    //平均配速
    void onAvSpeedChange(float avSpeed);

}
