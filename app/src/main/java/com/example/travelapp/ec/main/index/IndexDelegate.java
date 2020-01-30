package com.example.travelapp.ec.main.index;

import android.os.Bundle;
import android.view.View;

import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.delegates.bottom.BottomItemDelegate;
import com.example.travelapp.core.ui.recycler.BaseDecoration;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.core.ui.refresh.RefreshHandler;
import com.example.travelapp.ec.main.EcBottomDelegate;
import com.example.travelapp.ec.main.index.search.SearchDelegate;
import com.example.travelapp.ec.main.personal.PersonalDelegate;
import com.joanzapata.iconify.widget.IconTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class IndexDelegate extends BottomItemDelegate {

    @BindView(R2.id.rv_index)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.srl_index)
    SwipeRefreshLayout mRefreshLayout = null;
    @BindView(R2.id.tb_index)
    Toolbar mToolbar = null;
    @BindView(R2.id.icon_index_scan)
    IconTextView mIconScan = null;

    @OnClick(R2.id.tv_search_view)
    void onClickSearch() {
        getParentDelegate().getSupportDelegate().start(new SearchDelegate());
    }

    @OnClick(R2.id.de_popup_userpanel)
    void onClickPersonal(){
        getParentDelegate().getSupportDelegate().start(new PersonalDelegate());
    }

    private RefreshHandler mRefreshHandler = null;
    private DataConverter mDataConverter = null;
    private MultipleRecyclerAdapter mAdapter = null;

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
    }

    private void initRecyclerView() {
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.addItemDecoration(
                BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_backgroud), 5));
        mRecyclerView.setLayoutManager(manager);
        final EcBottomDelegate ecBottomDelegate = getParentDelegate();
        mRecyclerView.addOnItemTouchListener(IndexItemClickListener.create(ecBottomDelegate));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        initRecyclerView();
        mRefreshHandler.firstPage("scenery/findSceneryByRand");
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        mRefreshHandler = new RefreshHandler(mRefreshLayout, mRecyclerView,this);
    }
}
