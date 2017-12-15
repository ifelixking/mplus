package com.hugoo.mplus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by liyh on 2017/11/10.
 */

public class Application extends android.app.Application {
    @SuppressLint("StaticFieldLeak")
    private static Context m_context;
    private static String m_deviceID;
    private static Application m_instance;

    @Override
    public void onCreate() {
        super.onCreate();
        m_instance = this;
        m_context = getApplicationContext();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static Application getInstance(){
        return  m_instance;
    }

    public static Context getContext() {
        return m_context;
    }

    public static String getDeviceID() {
        return m_deviceID;
    }

    public static void initDeviceID(String deviceID) {
        if (deviceID == null && BuildConfig.DEBUG) {
            deviceID = "nintendo";
        }
        Log.i("liyh -  deviceID", deviceID);
        m_deviceID = deviceID;
    }
}