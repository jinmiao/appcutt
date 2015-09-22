package com.appcutt.demo.libs.log;

/**
 * Created by ouyangjinmiao on 5/6/15.
 */
public class LoggerFactory {

    private static Logger mLog;

    public static Logger getDefaultLogger () {

        if (mLog == null) {
            mLog = new Logger();
        }

        return mLog;
    }

    public static Logger getLogger (String str) {

        if (mLog == null) {
            mLog = new Logger();
        }

        return mLog;
    }


}
