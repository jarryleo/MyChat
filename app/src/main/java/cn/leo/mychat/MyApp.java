package cn.leo.mychat;

import android.app.Application;

import cn.leo.mychat.core.Client;

/**
 * Created by Leo on 2017/9/14.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Client.init(this, "119.29.253.156", 25627); //初始化即时通讯服务
    }
}
