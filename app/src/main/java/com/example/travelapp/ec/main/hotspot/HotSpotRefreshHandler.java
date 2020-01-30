package com.example.travelapp.ec.main.hotspot;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.ec.main.personal.collect.CollectAdapter;
import com.example.travelapp.ec.main.personal.collect.CollectRefreshHandler;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HotSpotRefreshHandler implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final RecyclerView RECYCLERVIEW;
    private DataConverter mDataConverter = new HotSpotDataConverter();
    private TravelDelegate mDelegate;
    private MultipleRecyclerAdapter mAdapter = null;
    private String refreshUrl = null;
    private int pageSize = 5;

    public HotSpotRefreshHandler(SwipeRefreshLayout refresh_layout, RecyclerView recyclerview,
                                 TravelDelegate delegate) {
        REFRESH_LAYOUT = refresh_layout;
        RECYCLERVIEW = recyclerview;
        REFRESH_LAYOUT.setOnRefreshListener(this);
        mDelegate = delegate;
    }


    @Override
    public void onRefresh() {
        loadHotSpot(refreshUrl);
    }

    public void loadHotSpot(String url){
        RestClient.builder()
                .url("remark/findRemarkByRand")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.clearBeans();
                        //设置adapter
                        mAdapter = HotSpotAdapter.create(mDataConverter.setJsonData(response).convert(),mDelegate.getContext());
                        mAdapter.setOnLoadMoreListener(HotSpotRefreshHandler.this, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        if (REFRESH_LAYOUT.isRefreshing()) {
                            REFRESH_LAYOUT.setRefreshing(false);
                        }
                    }
                })
                .loader(mDelegate.getContext())
                .bulid()
                .post();
    }

    @Override
    public void onLoadMoreRequested() {
        if(mAdapter.getData().size() < pageSize){
            mAdapter.loadMoreEnd(true);
        }else {
            RestClient.builder()
                    .url("remark/findRemarkByRand")
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
    }
}
