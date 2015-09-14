package com.appcutt.demo.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.Palette.Builder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ColorPalleteView extends LinearLayout {

    private static final String HEX_PREFIX = "#";

    @Bind(com.appcutt.demo.R.id.row_vibrant)
    View rowVibrant;
    @Bind(com.appcutt.demo.R.id.row_muted)
    View rowMuted;
    @Bind(com.appcutt.demo.R.id.container_lignt_vibrant)
    View containerLightVibrant;
    @Bind(com.appcutt.demo.R.id.container_vibrant)
    View containerVibrant;
    @Bind(com.appcutt.demo.R.id.container_dark_vibrant)
    View containerDarkVibrant;
    @Bind(com.appcutt.demo.R.id.container_lignt_muted)
    View containerLightMuted;
    @Bind(com.appcutt.demo.R.id.container_muted)
    View containerMuted;
    @Bind(com.appcutt.demo.R.id.container_dark_muted)
    View containerDarkMuted;
    @Bind(com.appcutt.demo.R.id.txt_lignt_vibrant)
    TextView txtLightVibrant;
    @Bind(com.appcutt.demo.R.id.txt_vibrant)
    TextView txtVibrant;
    @Bind(com.appcutt.demo.R.id.txt_dark_vibrant)
    TextView txtDarkVibrant;
    @Bind(com.appcutt.demo.R.id.txt_lignt_muted)
    TextView txtLightMuted;
    @Bind(com.appcutt.demo.R.id.txt_muted)
    TextView txtMuted;
    @Bind(com.appcutt.demo.R.id.txt_dark_muted)
    TextView txtDarkMuted;

    public ColorPalleteView(Context context) {
        super(context);
    }

    public ColorPalleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, com.appcutt.demo.R.layout.ui_color_pallete, this);
        ButterKnife.bind(this);
    }

    public void initColors(Bitmap bitmap) {
        new Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                if (vibrantSwatch != null) {
                    containerVibrant.setBackgroundColor(vibrantSwatch.getRgb());
                    txtVibrant.setTextColor(vibrantSwatch.getTitleTextColor());
                    txtVibrant.setText(getHexString(vibrantSwatch.getRgb()));
                }

                Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
                if (lightVibrantSwatch != null) {
                    containerLightVibrant.setBackgroundColor(lightVibrantSwatch.getRgb());
                    txtLightVibrant.setTextColor(lightVibrantSwatch.getTitleTextColor());
                    txtLightVibrant.setText(getHexString(lightVibrantSwatch.getRgb()));
                }

                Palette.Swatch darkVibrantSwatch = palette.getDarkVibrantSwatch();
                if (darkVibrantSwatch != null) {
                    containerDarkVibrant.setBackgroundColor(darkVibrantSwatch.getRgb());
                    txtDarkVibrant.setTextColor(darkVibrantSwatch.getTitleTextColor());
                    txtDarkVibrant.setText(getHexString(darkVibrantSwatch.getRgb()));
                }

                if (vibrantSwatch == null && lightVibrantSwatch == null && darkVibrantSwatch == null) {
                    rowVibrant.setVisibility(View.GONE);
                }

                Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                if (mutedSwatch != null) {
                    containerMuted.setBackgroundColor(mutedSwatch.getRgb());
                    txtMuted.setTextColor(mutedSwatch.getTitleTextColor());
                    txtMuted.setText(getHexString(mutedSwatch.getRgb()));
                }

                Palette.Swatch lightMutedSwatch = palette.getLightMutedSwatch();
                if (lightMutedSwatch != null) {
                    containerLightMuted.setBackgroundColor(lightMutedSwatch.getRgb());
                    txtLightMuted.setTextColor(lightMutedSwatch.getTitleTextColor());
                    txtLightMuted.setText(getHexString(lightMutedSwatch.getRgb()));
                }

                Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                if (darkMutedSwatch != null) {
                    containerDarkMuted.setBackgroundColor(darkMutedSwatch.getRgb());
                    txtDarkMuted.setTextColor(darkMutedSwatch.getTitleTextColor());
                    txtDarkMuted.setText(getHexString(darkMutedSwatch.getRgb()));
                }

                if (mutedSwatch == null && lightMutedSwatch == null && darkMutedSwatch == null) {
                    rowMuted.setVisibility(View.GONE);
                }
            }
        });
    }

    private String getHexString(int rgb) {
        return HEX_PREFIX + Integer.toHexString(rgb);
    }

}
