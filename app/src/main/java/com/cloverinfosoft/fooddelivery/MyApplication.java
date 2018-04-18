package com.cloverinfosoft.fooddelivery;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.cloverinfosoft.fooddelivery.dashboard.DashboardActivity;
import com.cloverinfosoft.fooddelivery.initials.LoginActivity;
import com.cloverinfosoft.fooddelivery.utils.PreferencesManager;

/**
 * Created by admin on 4/12/2018.
 */

public class MyApplication extends Application {

    private PreferencesManager preferencesManager;

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        preferencesManager=PreferencesManager.getInstance(this);
        preferencesManager.setdeviceIP(ip);


        // Required initialization logic here!



    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
