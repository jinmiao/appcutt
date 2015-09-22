package com.appcutt.demo.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.appcutt.demo.utils.AppUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareBarView extends LinearLayout {

    public ShareBarView(Context context) {
        super(context);
    }

    public ShareBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, com.appcutt.demo.R.layout.ui_share_bar, this);
        ButterKnife.bind(this);
    }


    @OnClick(com.appcutt.demo.R.id.btn_email)
    void onClickBtnEmail() {
    }

    @OnClick(com.appcutt.demo.R.id.btn_copy)
    void onClickBtnCopy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            AppUtils.showToast(com.appcutt.demo.R.string.copyed, getContext());
        } else {
            AppUtils.showToast(com.appcutt.demo.R.string.not_supported, getContext());
        }
    }

    @OnClick(com.appcutt.demo.R.id.btn_google_plus)
    void onClickBtnGooglePlus() {
        // TODO
        AppUtils.showToast(com.appcutt.demo.R.string.coming_soon, getContext());
    }

    @OnClick(com.appcutt.demo.R.id.btn_facebook)
    void onClickBtnFacebook() {
    }

    @OnClick(com.appcutt.demo.R.id.btn_twitter)
    void onClickTwitter() {
        // TODO
        AppUtils.showToast(com.appcutt.demo.R.string.coming_soon, getContext());
    }

}
