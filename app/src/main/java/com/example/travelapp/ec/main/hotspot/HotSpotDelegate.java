package com.example.travelapp.ec.main.hotspot;

import android.os.Bundle;
import android.view.View;

import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.delegates.bottom.BottomItemDelegate;
import com.example.travelapp.ec.main.personal.collect.CollectItemClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class HotSpotDelegate extends BottomItemDelegate {

    @BindView(R2.id.srl_hot_spot)
    SwipeRefreshLayout mRefreshLayout = null;
    @BindView(R2.id.rv_hot_spot)
    RecyclerView mRecyclerView = null;

    private HotSpotRefreshHandler mHandler = null;
    private String mUrl = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_hot_spot;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        mHandler = new HotSpotRefreshHandler(mRefreshLayout,mRecyclerView,this);
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
    }

    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnItemTouchListener(CollectItemClickListener.create(getParentDelegate()));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView();
        initRefreshLayout();
        mHandler.loadHotSpot(mUrl);
    }

}
