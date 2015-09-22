package com.appcutt.demo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.appcutt.demo.R;
import com.appcutt.demo.imageloader.AppcuttDisplayImageOptions;
import com.appcutt.demo.libs.imageloader.com.nostra13.universalimageloader.core.ImageLoader;
import com.appcutt.demo.models.pojo.Photo;
import com.appcutt.demo.utils.SystemUtil;
import com.appcutt.demo.views.AspectRatioImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片适配器
 * <p/>
 * Created by ouyangjinmiao on 15-09-22.
 */
public class PhotosAdapter extends BaseAdapter {

    private List<Photo> mData;

    private LayoutInflater mInflater;

    private final Context mContext;

    /**
     * 一行有几列
     */
    private final int ROWS = 3;

    public PhotosAdapter(Context context, List<Photo> data) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
    }

    public void addData(List<Photo> data) {

        if (data != null && data.size() > 0) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        if (mData == null || i >= mData.size()) {
            return null;
        }
        return mData != null ? mData.get(i) : null;
    }

    public ArrayList<String> getAllImages() {
        ArrayList<String> imgs = new ArrayList<String>();

        for(Photo p : mData) {
            if (p != null) {
                imgs.add(p.getImageUrl());
            }
        }

        return imgs;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public List<Photo> getData() {
        return mData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        final ViewHolder viewHolder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item_photo, null);

            viewHolder = new ViewHolder();

            viewHolder.rootView = convertView.findViewById(R.id.ripple);
            viewHolder.imageView = (AspectRatioImageView) convertView.findViewById(R.id.img_preview);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 显示数据
        final Photo data = mData.get(position);

        Log.d("photos adapter:", data.getImageUrl()+", pos:"+position);
        ImageLoader.getInstance().displayImage(data.getImageUrl(), viewHolder.imageView, AppcuttDisplayImageOptions.DEFAULT_DISPLAY_IMAGE_OPTIONS);

        /**
         * 设置分类菜单的高
         */
        int width = SystemUtil.getScreenWidth(mContext) / ROWS;

        /** 图片的高宽 */
        RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) viewHolder.imageView.getLayoutParams();
        para.width = width;
        para.height = para.width ;
        viewHolder.imageView.setLayoutParams(para);

        /**  item的高宽 */
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, width);
        convertView.setLayoutParams(lp);


        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtil.showToast(mContext, "click:"+data.getTitle());
            }
        });

        return convertView;
    }


    class ViewHolder {

        View rootView;

        AspectRatioImageView imageView;

    }
}
