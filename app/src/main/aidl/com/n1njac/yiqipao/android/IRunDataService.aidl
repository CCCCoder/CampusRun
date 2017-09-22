// IRunDataService.aidl
package com.n1njac.yiqipao.android;

// Declare any non-default types here with import statements

import com.n1njac.yiqipao.android.IRunDataCallback;

interface IRunDataService {


    void registerRunDataCallback(IRunDataCallback callback);
    void unRegisterRunDataCallback(IRunDataCallback callback);

    void continueRun();
    void pauseRun();
    void stopRun();
    void startRun();

}
