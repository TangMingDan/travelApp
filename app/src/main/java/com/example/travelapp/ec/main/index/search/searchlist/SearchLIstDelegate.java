package com.example.travelapp.ec.main.index.search.searchlist;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.recycler.BaseDecoration;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.core.ui.refresh.RefreshHandler;
import com.example.travelapp.ec.main.EcBottomDelegate;
import com.example.travelapp.ec.main.detial.SceneryInfoDelegate;
import com.example.travelapp.ec.main.index.IndexItemClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class SearchLIstDelegate extends TravelDelegate
        implements SwipeRefreshLayout.OnRefreshListener, View.OnKeyListener {

    private static final String ARG_SCENERY_DATA = "ARG_SCENERY_DATA";
    private JSONObject mData = null;
    private DataConverter converter = new SearchListDataConverter();
    private MultipleRecyclerAdapter mAdapter;

    @BindView(R2.id.srl_search_list)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R2.id.rv_search_list)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.et_search_view)
    EditText editText = null;

    @OnClick(R2.id.tv_top_search)
    void onSearch() {
        if(editText.getText().toString() == null || editText.getText().toString().equals("")){
            Toast.makeText(getContext(),"输入内容不能为空",Toast.LENGTH_SHORT).show();
        }else {
            searchData();
        }
    }

    @OnClick(R2.id.icon_top_search_back)
    void onClickBack() {
        pop();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshLayout.setProgressViewOffset(true, 120, 300);
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.addItemDecoration(
                BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_backgroud), 5));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnItemTouchListener(IndexItemClickListener.create(this));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            final String sceneryData = args.getString(ARG_SCENERY_DATA);
            mData = JSON.parseObject(sceneryData);
        }
    }

    public static SearchLIstDelegate create(String sceneryData) {
        final Bundle args = new Bundle();
        args.putString(ARG_SCENERY_DATA, sceneryData);
        final SearchLIstDelegate delegate = new SearchLIstDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_search_list;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        initRecyclerView();
        initRefreshLayout();
        initData();
    }

    private void initData() {
        mAdapter = MultipleRecyclerAdapter.create(converter.setJsonData(mData.toString()));
        mRecyclerView.setAdapter(mAdapter);
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        searchData();
    }

    public void searchData() {
        RestClient.builder()
                .url("scenery/findScenery")
                .param("content", editText.getText().toString())
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        converter.clearBeans();
                        String status = JSON.parseObject(response).getString("status");
                        if (status.equals("failure")) {
                            Toast.makeText(getContext(), "没有查询到您想要的信息", Toast.LENGTH_SHORT).show();
                        } else if (status.equals("success")) {
                            //设置adapter
                            mAdapter = MultipleRecyclerAdapter.create(converter.setJsonData(response));
                            mRecyclerView.setAdapter(mAdapter);
                        }
                        if (mRefreshLayout.isRefreshing()) {
                            mRefreshLayout.setRefreshing(false);
                        }
                    }
                })
                .bulid()
                .get();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()){
            case R.id.et_search_view:
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                            || keyCode == KeyEvent.KEYCODE_ENTER) {

                        String str = editText.getText().toString();
                        if (TextUtils.isEmpty(str))
                            return false;
                        searchData();
                        return true;
                    }
                }
                break;
        }
        return false;
    }
}
