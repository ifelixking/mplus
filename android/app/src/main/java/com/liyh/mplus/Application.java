package com.liyh.mplus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONObject;

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