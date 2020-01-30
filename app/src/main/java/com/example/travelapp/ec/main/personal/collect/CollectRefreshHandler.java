package com.example.travelapp.ec.main.personal.collect;

import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.travelapp.core.app.Travel;
import com.example.travelapp.core.app.UserInfoType;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.core.utils.storage.TravelPreference;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CollectRefreshHandler implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final RecyclerView RECYCLERVIEW;
    private DataConverter mDataConverter = new CollectDataConverter();
    private TravelDelegate mDelegate;
    private BaseQuickAdapter mAdapter = null;
    private String refreshUrl = null;

    public CollectRefreshHandler(SwipeRefreshLayout refresh_layout,
                                 RecyclerView recyclerview,
                                 TravelDelegate delegate) {
        REFRESH_LAYOUT = refresh_layout;
        RECYCLERVIEW = recyclerview;
        REFRESH_LAYOUT.setOnRefreshListener(this);
        mDelegate = delegate;
    }

    private void refresh() {
        submit(refreshUrl);
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    public void submit(String url) {
        refreshUrl = url;
        RestClient.builder()
                .loader(mDelegate.getContext())
                .url(url)
                .param("id", TravelPreference.getCustomAppProfile(UserInfoType.USER_ID.name()))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.clearBeans();
                        //设置adapter
                        mAdapter = new CollectAdapter(mDataConverter.setJsonData(response).convert());
//                        mAdapter.setOnLoadMoreListener(CollectRefreshHandler.this, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        if (REFRESH_LAYOUT.isRefreshing()) {
                            REFRESH_LAYOUT.setRefreshing(false);
                        }
                    }
                })
                .bulid()
                .get();
    }

    @Override
    public void onLoadMoreRequested() {
//        paging("index_2_data.json");
    }

    private void paging(String url) {
        Travel.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RestClient.builder()
                        .url(url)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                mAdapter.addData(mDataConverter.setJsonData(response).convert());
                                mAdapter.loadMoreComplete();
                            }
                        })
                        .bulid()
                        .get();
            }
        }, 1000);
    }
}
