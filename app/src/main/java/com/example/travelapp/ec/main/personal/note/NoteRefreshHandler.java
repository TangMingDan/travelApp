package com.example.travelapp.ec.main.personal.note;

import android.util.Log;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
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

public class NoteRefreshHandler implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final RecyclerView RECYCLERVIEW;
    private DataConverter mDataConverter = new NoteDataConverter();
    private TravelDelegate mDelegate;
    private NoteAdapter mAdapter = null;
    private String refreshUrl = null;
    private int param = 1;

    public NoteRefreshHandler(SwipeRefreshLayout refresh_layout,
                              RecyclerView recyclerview,
                              TravelDelegate delegate) {
        REFRESH_LAYOUT = refresh_layout;
        RECYCLERVIEW = recyclerview;
        REFRESH_LAYOUT.setOnRefreshListener(this);
        mDelegate = delegate;
    }

    private void refresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        submit(refreshUrl,param);

    }

    @Override
    public void onRefresh() {
        refresh();
    }

    public void submit(String url,int param) {
        refreshUrl = url;
        this.param = param;
        RestClient.builder()
                .loader(mDelegate.getContext())
                .url(url)
                .param("id", TravelPreference.getCustomAppProfile(UserInfoType.USER_ID.name()))
                .param("state",param)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.clearBeans();
                        //设置adapter
                        mAdapter = new NoteAdapter(mDataConverter.setJsonData(response).convert());
//                        mAdapter.setOnLoadMoreListener(NoteRefreshHandler.this, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        RECYCLERVIEW.addOnItemTouchListener(NoteItemClickListener.create(mDelegate,mAdapter));
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

}
