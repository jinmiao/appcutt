package com.konifar.materialcat.views.fragments;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.konifar.materialcat.PhotoDetailActivity;
import com.konifar.materialcat.R;
import com.konifar.materialcat.models.pojo.Photo;
import com.konifar.materialcat.views.ListLoadingView;
import com.konifar.materialcat.views.adapters.PhotosArrayAdapter;
import com.konifar.materialcat.views.listeners.OnLoadMoreScrollListener;

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

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.grid_view)
    HeaderFooterGridView gridView;
    @Bind(R.id.loading)
    FrameLayout loading;

    private PhotosArrayAdapter adapter;
    private ListLoadingView loadingFooter;
    private String sort;

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

        return view;
    }

    private void initSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.theme500);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showList(1);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //EventBus.getDefault().unregister(this);
    }

    private void initGridView() {
        adapter = new PhotosArrayAdapter(getActivity());
        loadingFooter = new ListLoadingView(getActivity());
        gridView.addFooterView(loadingFooter);
        gridView.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gridView.setNestedScrollingEnabled(true);
        }
    }

    @OnItemClick(R.id.grid_view)
    void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Photo photo = adapter.getItem(position);
        PhotoDetailActivity.start(getActivity(), view, photo);
    }

    private void showList(final int page) {
        if (page > 1) loadingFooter.switchVisible(true);

        try {
            String[] img3 = getResources().getAssets().list(sort);
            List<Photo> photos3 = new ArrayList<Photo>();
            for(int i=0;i< img3.length; i++) {
                String imgFile = img3[i];
                String imgName = imgFile.substring(0,imgFile.indexOf("."));
                Photo photo = new Photo();
                photo.setId(imgName);
                photo.setTitle(imgName);
                photo.setImageUrl("file:///android_asset/" + sort + "/" + imgFile);
                photos3.add(photo);
            }

            if (swipeRefresh.isRefreshing()) {
                swipeRefresh.setRefreshing(false);
                adapter.clear();
            }

            adapter.addAll(photos3);

            if (loading.getVisibility() == View.VISIBLE) {
                initListViewScrollListener();
                loading.setVisibility(View.GONE);
            }

            loadingFooter.switchVisible(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
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