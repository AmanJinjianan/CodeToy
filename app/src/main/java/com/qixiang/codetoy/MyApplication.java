package com.qixiang.codetoy;

import android.app.Application;
import android.content.Context;

import com.qixiang.codetoy.Util.DeviceInfoUtils;


/**
 * 自定的Application
 *
 * @author zhangyazhou
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        DeviceInfoUtils.init(this);
    }

    public static Context getContext() {
        return context;
    }
}

