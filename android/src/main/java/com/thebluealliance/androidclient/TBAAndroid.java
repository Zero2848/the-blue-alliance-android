package com.thebluealliance.androidclient;

import android.app.Application;
import android.util.Log;

/**
 * File created by phil on 7/21/14.
 */
public class TBAAndroid extends Application {

    /* So we can disable some network requests in tests
     * They can still be tested separately
     */
    private boolean testMode;
    
    @Override
    public void onCreate() {
        super.onCreate();
        testMode = false;
        Log.i(Constants.LOG_TAG, "Welcome to The Blue Alliance for Android, v" + BuildConfig.VERSION_NAME);
    }
    
    public void setTestMode(boolean test){
        testMode = test;
    }
    
    public boolean getTestMode(){
        return testMode;
    }
}
