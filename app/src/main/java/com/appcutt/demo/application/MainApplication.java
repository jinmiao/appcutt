package com.appcutt.demo.application;

import android.app.Application;
import android.content.Context;

import com.appcutt.demo.imageloader.ImageLoaderUtil;

public class MainApplication extends Application {


    public static MainApplication get(Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化图片缓存
        ImageLoaderUtil.initImageLoader(this);
    }

}
