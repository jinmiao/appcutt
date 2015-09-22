package com.appcutt.demo.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.appcutt.demo.R;
import com.appcutt.demo.imageloader.AppcuttDisplayImageOptions;
import com.appcutt.demo.libs.imageloader.com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.appcutt.demo.libs.imageloader.com.nostra13.universalimageloader.core.ImageLoader;
import com.appcutt.demo.libs.imageloader.com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.appcutt.demo.views.previewimage.ExtendedViewPager;
import com.appcutt.demo.views.previewimage.TouchImageView;

import java.util.ArrayList;

/**
 * 图片浏览界面
 * <p/>
 * Created by ouyangjinmiao on 8/1/15.
 */
public class ImagePreviewActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private Context mContext;

    private TextView mTitle;

    private View mDel;

    private ExtendedViewPager mGallery;

    private ImageAdapter mImageAdapter;

    private ArrayList<String> mImageList;

    private int mCurrentIndex;

    private View mTopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_image_preview_activity);

        mContext = this;

        mGallery = (ExtendedViewPager) findViewById(R.id.gallery);

        mTopView = (View) findViewById(R.id.top_view);


        mImageList = getIntent().getStringArrayListExtra("imgs");
        if (mImageList == null) {
            mImageList = new ArrayList<String>();
        }

        Log.d("image","mImageList size:" + mImageList.size());

        int count = mImageList.size();
        int index = getIntent().getIntExtra("index", 0);
        if (index >= count) {
            index = count - 1;
        }
        if (index < 0) {
            index = 0;
        }

        mCurrentIndex = index;

        mImageAdapter = new ImageAdapter(this, mImageList);
        mGallery.setAdapter(mImageAdapter);
        mGallery.setCurrentItem(mCurrentIndex);
        mGallery.setOnPageChangeListener(this);

        mTitle = (TextView) findViewById(R.id.title);

        hideStatusBar();

        setCustomerTitle((mCurrentIndex + 1) + "/" + count);
    }

    private void setCustomerTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    public void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    public void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        mCurrentIndex = i;

        setCustomerTitle((mCurrentIndex + 1) + "/" + mImageList.size());
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        mCurrentIndex = i;
    }

    private class ImageAdapter extends PagerAdapter {

        private SparseArray<View> views = new SparseArray<View>();

        private Context mContext;

        private LayoutInflater mLayoutInflater;

        private ArrayList<String> mData = new ArrayList<String>();

        public ImageAdapter(Context context, ArrayList<String> data) {
            mLayoutInflater = LayoutInflater.from(context);
            mData = (data == null) ? new ArrayList<String>() : data;
            mContext = context;
        }

        public void addData(ArrayList<String> data) {
            if (mData != null) {
                mData.clear();

                mData.addAll(data);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {

            if (mData == null) {
                return 0;
            }

            return mData.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View convertView = mLayoutInflater.inflate(R.layout.ac_image_preview_item, null);

            final ViewHolder holder = new ViewHolder();

            holder.pic = (TouchImageView) convertView.findViewById(R.id.pic);

            final String path = mData.get(position);

            try {
                if (!TextUtils.isEmpty(path)) {

                    Log.d("instantiateItem", "preview adapter pictureInfo:" + path);

                    if (path.startsWith("http")) {
                        // 加载网络图片
                        ImageLoader.getInstance().displayImage(path, holder.pic,
                                AppcuttDisplayImageOptions.DEFAULT_DISPLAY_IMAGE_OPTIONS, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                        holder.pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    }
                                });

                    } else {
                        // 加载本地图片
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .cloneFrom(AppcuttDisplayImageOptions.DEFAULT_DISPLAY_IMAGE_OPTIONS)
                                .cacheOnDisk(false)
                                .build();

                        ImageLoader.getInstance().displayImage(path, holder.pic, options, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                holder.pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                        });
                    }
                }

                ((ViewPager) container).addView(convertView);

                views.put(position, convertView);

                holder.pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object o) {
            View view = (View) o;
            ((ViewPager) container).removeView(view);
            views.remove(position);
            view = null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        class ViewHolder {

            TouchImageView pic;

        }
    }
}
