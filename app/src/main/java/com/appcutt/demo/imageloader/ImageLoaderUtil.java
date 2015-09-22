package com.appcutt.demo.imageloader;

import android.content.Context;

import com.appcutt.demo.libs.imageloader.com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.appcutt.demo.libs.imageloader.com.nostra13.universalimageloader.core.ImageLoader;
import com.appcutt.demo.libs.imageloader.com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.appcutt.demo.libs.imageloader.com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by ouyangjinmiao on 4/6/15.
 */
public class ImageLoaderUtil {

    private static ImageLoader mImageLoader;

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public static ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = ImageLoader.getInstance();
        }
        return mImageLoader;
    }


}
