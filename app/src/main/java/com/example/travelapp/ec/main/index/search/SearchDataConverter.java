package com.example.travelapp.ec.main.index.search;

import com.alibaba.fastjson.JSONArray;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.core.utils.storage.TravelPreference;

import java.util.ArrayList;

public class SearchDataConverter extends DataConverter {

    public static final String TAG_SEARCH_HISTORY = "search_history";

    @Override
    public ArrayList<MultipleItemBean> convert() {
        final String jsonStr = TravelPreference.getCustomAppProfile(TAG_SEARCH_HISTORY);
        if(jsonStr != null){
            final JSONArray array = JSONArray.parseArray(jsonStr);
            if(array != null){
                final int size = array.size();
                for (int i = 0; i < size; i++) {
                    final String historyItemText = array.getString(i);
                    final MultipleItemBean entity=  MultipleItemBean.builder()
                            .setItemType(SearchItemType.ITEM_SEARCH)
                            .setField(MultipleFields.TEXT,historyItemText)
                            .build();
                    BEANS.add(entity);
                }
            }
        }
        return BEANS;
    }
}
