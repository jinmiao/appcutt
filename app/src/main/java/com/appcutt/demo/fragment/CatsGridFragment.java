package com.appcutt.demo.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.appcutt.demo.R;
import com.appcutt.demo.activity.ImagePreviewActivity;
import com.appcutt.demo.adapters.PhotosAdapter;
import com.appcutt.demo.listeners.OnLoadMoreScrollListener;
import com.appcutt.demo.models.pojo.Photo;
import com.appcutt.demo.views.ListLoadingView;
import com.appcutt.demo.activity.PhotoDetailActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import jp.co.recruit_mp.android.widget.HeaderFooterGridView;

public class CatsGridFragment extends Fragment {

    private static final String ARG_KEY_SORT = "arg_key_sort";

    @Bind(com.appcutt.demo.R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(com.appcutt.demo.R.id.grid_view)
    HeaderFooterGridView gridView;
    @Bind(com.appcutt.demo.R.id.loading)
    FrameLayout loading;

    private PhotosAdapter adapter;
    private ListLoadingView loadingFooter;
    private String sort;

    private boolean isLoading = false;

    public static CatsGridFragment newInstance(String sort) {
        CatsGridFragment fragment = new CatsGridFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_KEY_SORT, sort);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments.containsKey(ARG_KEY_SORT)) {
            sort = arguments.getString(ARG_KEY_SORT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cats_grid, container, false);
        ButterKnife.bind(this, view);

        initGridView();
        initSwipeRefresh();

        showList(1);
        Log.d("CatsGridFragment", "onCreateView");
        return view;
    }

    private void initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.theme500);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoading = false;

                showList(1);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        EventBus.getDefault().unregister(this);
    }

    private void initGridView() {
        adapter = new PhotosAdapter(getActivity(), new ArrayList<Photo>());
        loadingFooter = new ListLoadingView(getActivity());
        gridView.addFooterView(loadingFooter);
        gridView.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridView.setNestedScrollingEnabled(true);
        }
    }

    @OnItemClick(com.appcutt.demo.R.id.grid_view)
    void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Photo photo = (Photo) adapter.getItem(position);
//        PhotoDetailActivity.start(getActivity(), view, photo);

        /**
         * 图片浏览
         */
        Intent intent = new Intent();
        intent.setClass(getContext(), ImagePreviewActivity.class);
        intent.putStringArrayListExtra("imgs", adapter.getAllImages());
        intent.putExtra("index", position);

        if (android.os.Build.VERSION.SDK_INT > 15) {
            // Create a scale-up animation that originates at the button
            // being pressed.
            ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(
                    v, 0, 0, v.getWidth(), v.getHeight());
            // Request the activity be started, using the custom animation options.
            getActivity().startActivity(intent, opts.toBundle());
        } else {
            getActivity().startActivity(intent);
        }
    }

    private void showList(final int page) {
        if (isLoading) return;

        if (page > 1) loadingFooter.switchVisible(true);

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                try {
                    String[] img3 = getResources().getAssets().list(sort);
                    List<Photo> photos3 = new ArrayList<Photo>();
                    for(int i=0;i < img3.length; i++) {
                        String imgFile = img3[i];
                        String imgName = imgFile.substring(0,imgFile.indexOf("."));

                        Photo photo = new Photo();
                        photo.setId(imgName);
                        photo.setTitle(imgName);
//                photo.setImageUrl("file:///android_asset/" + sort + "/" + imgFile);

                        photo.setImageUrl("assets://" + sort + "/" + imgFile);

                        photos3.add(photo);

                        Log.d("showlist i:"+i, photo.getImageUrl());
                    }

                    if (swipeRefresh.isRefreshing()) {
                        swipeRefresh.setRefreshing(false);
                        adapter.clearData();
                    }

                    Log.d("showlist photos3 size:", photos3.size()+"");
                    Log.d("showlist adapter size:", adapter.getCount() + "");

                    adapter.clearData();
                    adapter.addData(photos3);

                    if (loading.getVisibility() == View.VISIBLE) {
                        initListViewScrollListener();
                        loading.setVisibility(View.GONE);
                    }

                    loadingFooter.switchVisible(false);

                    isLoading = true;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initListViewScrollListener() {
        AbsListView.OnScrollListener scrollListener = (AbsListView.OnScrollListener) getActivity();
        gridView.setOnScrollListener(new OnLoadMoreScrollListener(scrollListener) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                showList(page);
            }
        });
    }


}