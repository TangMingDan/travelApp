package com.example.travelapp.core.ui.refresh;

import android.util.Log;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.travelapp.core.app.Travel;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.IError;
import com.example.travelapp.core.net.callback.IFailure;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.ec.main.index.IndexDataConverter;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.http.DELETE;

public class RefreshHandler implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final RecyclerView RECYCLERVIEW;
    private DataConverter CONVERTER = new IndexDataConverter();
    private MultipleRecyclerAdapter mAdapter;
    private TravelDelegate DELEGATE;

    public RefreshHandler(SwipeRefreshLayout refresh_layout, RecyclerView recyclerView,TravelDelegate delegate) {
        this.RECYCLERVIEW = recyclerView;
        this.REFRESH_LAYOUT = refresh_layout;
        this.DELEGATE = delegate;
        this.REFRESH_LAYOUT.setOnRefreshListener(this);
    }

    private void refresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        firstPage("scenery/findSceneryByRand");
    }

    public void firstPage(String url) {
        RestClient.builder()
                .url(url)
                .loader(DELEGATE.getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        CONVERTER.clearBeans();
                        //设置adapter
                        mAdapter = MultipleRecyclerAdapter.create(CONVERTER.setJsonData(response));
                        mAdapter.setOnLoadMoreListener(RefreshHandler.this, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        if (REFRESH_LAYOUT.isRefreshing()) {
                            REFRESH_LAYOUT.setRefreshing(false);
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Log.d("index_delegate", "失败");
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Log.d("index_delegate", "错误");
                    }
                })
                .bulid()
                .post();
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoadMoreRequested() {
        paging("scenery/findSceneryByRand");
    }

    private void paging(String url) {
        RestClient.builder()
                .url(url)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter.addData(CONVERTER.setJsonData(response).convert());
                        mAdapter.loadMoreComplete();
                    }
                })
                .bulid()
                .get();

    }
}
