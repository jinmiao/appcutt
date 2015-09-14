package com.appcutt.demo.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.appcutt.demo.R;
import com.appcutt.demo.models.pojo.Photo;
import com.appcutt.demo.utils.FabAnimationUtils;
import com.appcutt.demo.views.CustomKenBurnsView;
import com.appcutt.demo.views.PhotoInfoView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nineoldandroids.view.animation.AnimatorProxy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class PhotoDetailActivity extends AppCompatActivity {

    private static final String EXTRA_ORIENTATION = "extra_orientation";
    private static final String EXTRA_LEFT = "extra_left";
    private static final String EXTRA_TOP = "extra_top";
    private static final String EXTRA_WIDTH = "extra_width";
    private static final String EXTRA_HEIGHT = "extra_height";

    private static final long ANIMATION_DURATION = 250;
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();

    @Bind(R.id.app_bar)
    AppBarLayout appBarLayout;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.img_preview)
    CustomKenBurnsView imgPreview;
    @Bind(R.id.img_preview_cover)
    ImageView imgPreviewCover;
    @Bind(R.id.img_preview_dummy)
    ImageView imgPreviewDummy;
    @Bind(R.id.fab)
    FloatingActionButton fab;

//    @Bind(R.id.color_pallete)
//    ColorPalleteView colorPalleteView;
//
//    @Bind(R.id.photo_info_id)
//    PhotoInfoView photoInfoId;

    @Bind(R.id.photo_info_title)
    PhotoInfoView photoInfoTitle;



    private int leftDelta;
    private int topDelta;
    private float widthScale;
    private float heightScale;
    private int originalOrientation;

    public static void start(Activity activity, View transitionView, Photo photo) {
        Intent intent = new Intent(activity, PhotoDetailActivity.class);
        intent.putExtra(Photo.class.getSimpleName(), photo);

        int[] screenLocation = new int[2];
        transitionView.getLocationOnScreen(screenLocation);
        int orientation = activity.getResources().getConfiguration().orientation;
        intent.putExtra(EXTRA_ORIENTATION, orientation);
        intent.putExtra(EXTRA_LEFT, screenLocation[0]);
        intent.putExtra(EXTRA_TOP, screenLocation[1]);
        intent.putExtra(EXTRA_WIDTH, transitionView.getWidth());
        intent.putExtra(EXTRA_HEIGHT, transitionView.getHeight());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity);
            activity.startActivity(intent, options.toBundle());
        } else {
            activity.startActivity(intent);
        }
        activity.overridePendingTransition(0, R.anim.activity_fade_out);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        final Photo photo = (Photo) bundle.getSerializable(Photo.class.getSimpleName());

        initToolbar(photo);
        bindPhotoInfo(photo);

        Picasso.with(this)
                .load(photo.getImageUrl())
                .placeholder(R.color.theme50)
                .into(imgPreviewDummy);

        // For circular reveal animation
        Picasso.with(this)
                .load(photo.getImageUrl())
                .placeholder(R.color.theme50)
                .into(imgPreviewCover);

        initPallete(photo);

        if (savedInstanceState == null) {
            final int thumbnailTop = bundle.getInt(EXTRA_TOP);
            final int thumbnailLeft = bundle.getInt(EXTRA_LEFT);
            final int thumbnailWidth = bundle.getInt(EXTRA_WIDTH);
            final int thumbnailHeight = bundle.getInt(EXTRA_HEIGHT);
            originalOrientation = bundle.getInt(EXTRA_ORIENTATION);
            ViewTreeObserver observer = imgPreviewDummy.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    // TODO setVisibility(View.gone) does not work...
                    // fab.setVisibility(View.GONE);
                    FabAnimationUtils.scaleOut(fab, 50, null);

                    imgPreviewDummy.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] screenLocation = new int[2];
                    imgPreviewDummy.getLocationOnScreen(screenLocation);
                    leftDelta = thumbnailLeft - screenLocation[0];
                    topDelta = thumbnailTop - screenLocation[1];
                    widthScale = (float) thumbnailWidth / (imgPreviewDummy.getWidth());
                    heightScale = (float) thumbnailHeight / imgPreviewDummy.getHeight();

                    startEnterAnimation();

                    return true;
                }
            });
        }

        //onClickFab();

        initActivityTransitions();
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            transition.excludeTarget(R.id.img_preview_dummy, true);
            transition.setInterpolator(new LinearOutSlowInInterpolator());
            transition.setDuration(300);
            getWindow().setEnterTransition(transition);
        }
    }

    private void initPallete(Photo photo) {
        Picasso.with(this)
                .load(photo.getImageUrl())
                .placeholder(R.color.theme50)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imgPreview.setImageBitmap(bitmap);
//                        colorPalleteView.initColors(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        //
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        //
                    }
                });
    }

    private void bindPhotoInfo(Photo photo) {
//        photoInfoId.setTitleText(photo.getId());
        photoInfoTitle.setTitleText(photo.getTitle());

    }

    private void initToolbar(Photo photo) {
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(photo.getTitle());
    }

    public void startEnterAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            imgPreviewDummy.setPivotX(0);
            imgPreviewDummy.setPivotY(0);
            imgPreviewDummy.setScaleX(widthScale);
            imgPreviewDummy.setScaleY(heightScale);
            imgPreviewDummy.setTranslationX(leftDelta);
            imgPreviewDummy.setTranslationY(topDelta);
        } else {
            AnimatorProxy proxy = AnimatorProxy.wrap(imgPreviewDummy);
            proxy.setPivotX(0);
            proxy.setPivotY(0);
            proxy.setScaleX(widthScale);
            proxy.setScaleY(heightScale);
            proxy.setTranslationX(leftDelta);
            proxy.setTranslationY(topDelta);
        }

        ViewPropertyAnimator.animate(imgPreviewDummy)
                .setDuration(ANIMATION_DURATION)
                .scaleX(1).scaleY(1)
                .translationX(0).translationY(0)
                .setInterpolator(INTERPOLATOR)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // AppBarLayout is set invisible, because AppbarLayout has default background color.
                        appBarLayout.setVisibility(View.VISIBLE);
                        imgPreview.setVisibility(View.VISIBLE);
                        revealPreview();
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void revealPreview() {
        int cx = (imgPreview.getLeft() + imgPreview.getRight()) / 2;
        int cy = (imgPreview.getTop() + imgPreview.getBottom()) / 2;

        float finalRadius = (float) Math.hypot(cx, cy);

        SupportAnimator animator =
                ViewAnimationUtils.createCircularReveal(imgPreview, cx, cy, 0, finalRadius);
        animator.setInterpolator(INTERPOLATOR);
        animator.setDuration(300);
        animator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                imgPreviewDummy.setVisibility(View.GONE);
                imgPreviewCover.setVisibility(View.VISIBLE);
                imgPreview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd() {
                FabAnimationUtils.scaleIn(fab);
            }

            @Override
            public void onAnimationCancel() {
                //
            }

            @Override
            public void onAnimationRepeat() {
                //
            }
        });
        animator.start();
    }

    @OnClick(R.id.fab)
    void onClickFab() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = fab.getDrawable();
            if (drawable instanceof AnimatedVectorDrawable) {
                if (((AnimatedVectorDrawable) drawable).isRunning()) return;

                int drawableResId = fab.isSelected() ? R.drawable.ic_pause_to_play : R.drawable.ic_play_to_pause;
                fab.setImageResource(drawableResId);

                drawable = fab.getDrawable();
                ((AnimatedVectorDrawable) drawable).start();
            }
        }

        if (fab.isSelected()) {
            imgPreview.pause();
        } else {
            imgPreview.resume();
        }

        fab.setSelected(!fab.isSelected());
    }



}
