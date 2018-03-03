package com.forher.forher;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by root on 11/1/16.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        sInstance=this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Quicksand-Regular.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    public static MyApplication getInstance()
    {

        return sInstance;
    }

    public static Context getAppContext()
    {
        return sInstance.getApplicationContext();
    }

}
