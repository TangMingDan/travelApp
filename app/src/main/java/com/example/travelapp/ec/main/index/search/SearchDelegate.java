package com.example.travelapp.ec.main.index.search;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;

import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.core.utils.storage.TravelPreference;
import com.example.travelapp.ec.main.index.search.searchlist.SearchLIstDelegate;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class SearchDelegate extends TravelDelegate {

    @BindView(R2.id.rv_search)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.et_search_view)
    AppCompatEditText mSearchEdit = null;

    @OnClick(R2.id.tv_top_search)
    void onClickSearch(){
        final String searchItemText = mSearchEdit.getText().toString();
        search(searchItemText);
    }

    @OnClick(R2.id.icon_top_search_back)
    void onClickBack(){
        getSupportDelegate().pop();
    }


    private void saveItem(String item){
        if(!StringUtils.isEmpty(item) && !StringUtils.isSpace(item)){
            List<String> history;
            final String historyStr = TravelPreference.getCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY);
            if(StringUtils.isEmpty(historyStr)){
                history = new ArrayList<>();
            }else {
                history = JSON.parseObject(historyStr,ArrayList.class);
            }
            history.add(item);
            final String json = JSON.toJSONString(history);
            TravelPreference.addCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY,json);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_search;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final List<MultipleItemBean> data = new SearchDataConverter().convert();
        final SearchAdapter adapter = new SearchAdapter(data);
        mRecyclerView.setAdapter(adapter);
        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            @Override
            public Divider getVerticalDivider(int position) {
                return null;
            }

            @Override
            public Divider getHorizontalDivider(int position) {
                return new Divider.Builder()
                        .size(2)
                        .margin(20,20)
                        .color(Color.GRAY)
                        .build();
            }
        });
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItemBean bean = (MultipleItemBean) adapter.getData().get(position);
                String data = bean.getField(MultipleFields.TEXT);
                search(data);
            }
        });
    }

    public void search(String content){
        RestClient.builder()
                .url("scenery/findScenery")
                .param("content",content)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        saveItem(content);
                        mSearchEdit.setText("");
                        TravelDelegate delegate = SearchLIstDelegate.create(response);
                        startWithPop(delegate);
                    }
                })
                .loader(getContext())
                .bulid()
                .get();
    }
}
