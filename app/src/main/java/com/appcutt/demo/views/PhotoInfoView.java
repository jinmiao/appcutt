package com.appcutt.demo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoInfoView extends FrameLayout {

    @Bind(com.appcutt.demo.R.id.img_icon)
    ImageView imgIcon;
    @Bind(com.appcutt.demo.R.id.txt_title)
    TextView txtTitle;
    @Bind(com.appcutt.demo.R.id.txt_sub)
    TextView txtSub;

    public PhotoInfoView(Context context) {
        super(context);
    }

    public PhotoInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, com.appcutt.demo.R.layout.ui_photo_info, this);
        ButterKnife.bind(this);

        TypedArray a = context.obtainStyledAttributes(attrs, com.appcutt.demo.R.styleable.PhotoInfoView);
        try {
            Drawable iconSrc = a.getDrawable(com.appcutt.demo.R.styleable.PhotoInfoView_iconSrc);
            String titleText = a.getString(com.appcutt.demo.R.styleable.PhotoInfoView_titleText);
            String subText = a.getString(com.appcutt.demo.R.styleable.PhotoInfoView_subText);

            bindData(iconSrc, titleText, subText);
        } finally {
            a.recycle();
        }
    }

    private void bindData(Drawable iconSrc, String titleText, String subText) {
        if (iconSrc != null) imgIcon.setImageDrawable(iconSrc);
        if (titleText != null) txtTitle.setText(titleText);
        if (subText != null) txtSub.setText(subText);
    }

    public void setTitleText(String text) {
        txtTitle.setText(text);
    }

}
