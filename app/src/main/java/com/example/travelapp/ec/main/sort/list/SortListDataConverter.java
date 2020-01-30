package com.example.travelapp.ec.main.sort.list;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.ItemType;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;

import java.util.ArrayList;

public class SortListDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemBean> convert() {
        final ArrayList<MultipleItemBean> dataList = new ArrayList<>();
        final JSONArray dataArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = dataArray.getJSONObject(i);
            final int id = data.getInteger("tagId");
            final String name = data.getString("tagName");
            final MultipleItemBean entity = MultipleItemBean.builder()
                    .setField(MultipleFields.ITEM_TYPE, ItemType.VERTICAL_MENU_LIST)
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.TEXT,name)
                    .setField(MultipleFields.TAG,false)
                    .build();
            dataList.add(entity);
            //设置第一个被选中
        }
        dataList.get(0).setField(MultipleFields.TAG,true);
        return dataList;
    }
}
