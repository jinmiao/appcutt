package com.appcutt.demo.libs.log;

import android.util.Log;

/**
 * Created by ouyangjinmiao on 5/6/15.
 */
public class Logger {

    private String TAG;

    public void d(String s) {
        Log.d(TAG, s);
    }


    public void e(String s) {
        Log.e(TAG, s);
    }
}
