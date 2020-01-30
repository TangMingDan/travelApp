package com.example.travelapp.ec.main.personal.collect;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.ec.main.personal.note.NoteRefreshHandler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class CollectDelegate extends TravelDelegate {

    @BindView(R2.id.rv_collect)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.srl_collect)
    SwipeRefreshLayout mRefreshLayout = null;

    private CollectRefreshHandler mHandler = null;
    private String mUrl = "scenery/findCollectedScenery";

    @Override
    public Object setLayout() {
        return R.layout.delegate_collect;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        initRecyclerView();
        initRefreshLayout();
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
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnItemTouchListener(CollectItemClickListener.create(this));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        mHandler = new CollectRefreshHandler(mRefreshLayout,mRecyclerView,this);
        mHandler.submit(mUrl);
    }
}
