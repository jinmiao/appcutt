package com.appcutt.demo.utils;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class ShareUtils {

    public static final String REPOGITORY_URL = "https://github.com/konifar/material-cat";

    public static void showShareDialog(@NonNull Activity activity) {
        if (!ShareDialog.canShow(ShareLinkContent.class)) return;

        ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
        builder = builder.setContentUrl(Uri.parse(REPOGITORY_URL))
                .setContentTitle(activity.getString(com.appcutt.demo.R.string.app_name));
        ShareDialog.show(activity, builder.build());
    }

}
