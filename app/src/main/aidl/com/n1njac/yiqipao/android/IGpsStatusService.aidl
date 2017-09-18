// IGpsStatusService.aidl
package com.n1njac.yiqipao.android;

// Declare any non-default types here with import statements
import com.n1njac.yiqipao.android.IGpsStatusCallback;

interface IGpsStatusService {

    void registerCallback(IGpsStatusCallback callback);
    void unRegisterCallback(IGpsStatusCallback callback);

}
