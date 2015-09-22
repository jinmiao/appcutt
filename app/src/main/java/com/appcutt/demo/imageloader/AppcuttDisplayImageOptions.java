package com.appcutt.demo.imageloader;

import android.graphics.Bitmap;

import com.appcutt.demo.R;
import com.appcutt.demo.libs.imageloader.com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.appcutt.demo.libs.imageloader.com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 图片配置项
 */
public class AppcuttDisplayImageOptions {

    /**
     * 默认图片显示配置
     */
    public static final DisplayImageOptions DEFAULT_DISPLAY_IMAGE_OPTIONS = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ac_ic_loading).bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheOnDisk(true).build();

}
