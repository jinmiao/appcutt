package com.appcutt.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.appcutt.demo.constants.Constants;
import com.appcutt.demo.libs.log.Logger;
import com.appcutt.demo.libs.log.LoggerFactory;

/**
 * 执行系统操作的工具集，与业务无关
 *
 */
public class SystemUtil {

    private static final Logger logger = LoggerFactory.getDefaultLogger();

    /**
     * 处理用户按两次BACK退出应用.
     */
    public static int BACK_COUNT = 0;

    private static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 在ui线程中执行
     *
     * @param runnable
     * @param delayMillis
     */
    public static void runInUIThread(Runnable runnable, long delayMillis) {
        handler.postDelayed(runnable, delayMillis);
    }

    /**
     * 在ui线程中执行
     *
     * @param runnable
     */
    public static void runInUIThread(Runnable runnable) {
        runInUIThread(runnable, 0);
    }

    /**
     * 获取本地广播管理器
     *
     * @param context
     * @return
     */
    public static LocalBroadcastManager getLocalBroadcastManager(Context context) {
        return LocalBroadcastManager.getInstance(context);
    }

    /**
     * 获取手机的IMEI信息
     * ˆ
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();

        return imei == null ? "" : imei;
    }

    /**
     * MAC地址
     *
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String result = info.getMacAddress();
        return result == null ? "" : result;
    }

    /**
     * getIMSI
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        return imsi == null ? "" : imsi;
    }

    /**
     * 服务版本号
     *
     * @param context
     * @return
     */
    public static final String getAPIV(Context context) {

        PackageManager manager = context.getPackageManager();
        try {
            ApplicationInfo appInfo = manager.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            String apiv = appInfo.metaData.getInt("KD_APIV") + "";
            if (!TextUtils.isEmpty(apiv)) {
                return apiv;
            }

            logger.e("APIV not set , please check");
        } catch (PackageManager.NameNotFoundException e) {
            logger.e("obtain app APIV error");
        }

        return "";
    }


    /**
     * 获取状态栏高度
     *
     * @param
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getStatusBarHeight(Context context) {
        Rect frame = new Rect();
        ((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;

    }

    /**
     * 获取屏幕的宽
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenWidth(Context context) {

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        int screenWdith = display.getWidth();

        // 根据屏幕方向获取手机屏幕的真实宽度
        int rotation = display.getRotation();
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            screenWdith = display.getWidth();
        } else {
            screenWdith = display.getHeight();
        }

        return screenWdith;
    }

    /**
     * getScreenHeight
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        int screenHeight = display.getHeight();

        // 根据屏幕方向获取手机屏幕的真实宽度
        int rotation = display.getRotation();
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            screenHeight = display.getHeight();
        } else {
            screenHeight = display.getWidth();
        }

        return screenHeight;
    }

    /**
     * 使用Toast.LENGTH_SHORT来展示toast
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 使用Toast.LENGTH_LONG来展示toast
     *
     * @param context
     * @param text
     */
    public static void showToastLong(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 复制内容到剪切板
     */
    public static void copyToClipboard(Context context, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content);
        Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show();
    }

    /**
     * 复制内容到剪切板
     */
    public static void copyToClipboard(Context context, String content, String toast) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content);
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 应用退出窗体
     *
     * @param context
     */
    public static void showExitDialog(Context context) {

        if (BACK_COUNT == 0) {

            Toast.makeText(context, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            BACK_COUNT = BACK_COUNT + 1;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    BACK_COUNT = 0;
                }
            }, 2000);

            return;
        }

        BACK_COUNT = 0;

        // 退出应用
        exitApp();
    }

    /**
     * 广播程序退出，用于一些窗体的结束，比如通过push启动的一些窗体
     */
    public static void exitApp() {

    }

    /**
     * 判断程序是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppOnForeground(Context context) {

        android.app.ActivityManager activityManager = (android.app.ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        android.content.ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
        if (cn.getPackageName().equals(context.getPackageName())) {
            return true;
        }

        return false;
    }

    /**
     * 应用重启
     * <p/>
     * 杀死应用的进程，使得设置的全局变量生效
     */
    public static void restartApplication(Context context) {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 防止View被点击两次
     */
    public static class CommonUtils {
        private static long lastClickTime;

        public static boolean isFastDoubleClick() {
            long time = System.currentTimeMillis();
            long timeD = time - lastClickTime;
            if (0 < timeD && timeD < 1000) {
                return true;
            }
            lastClickTime = time;
            return false;
        }
    }


}
