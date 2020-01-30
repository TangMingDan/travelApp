package com.example.travelapp.ec.main.personal.collect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.ItemType;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;

import java.util.ArrayList;

public class CollectDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemBean> convert() {
        JSONArray data = JSON.parseObject(getJsonData()).getJSONArray("data");
        if (data != null) {
            int size = data.size();
            for (int i = 0; i < size; i++) {
                JSONObject dataObject = data.getJSONObject(i);
                String sceneryName = dataObject.getString("sceneryName");
                String sceneryLocation = dataObject.getString("sceneryLocation");
                JSONArray imgJson = dataObject.getJSONArray("sceneryImgs");
                String imgUrl = imgJson.getJSONObject(1).getString("imgUrl");
                int sceneryId = dataObject.getInteger("sceneryId");

                MultipleItemBean bean = MultipleItemBean.builder()
                        .setField(MultipleFields.TEXT,sceneryName)
                        .setField(MultipleFields.CONTENT,sceneryLocation)
                        .setField(MultipleFields.IMAGE_URL,imgUrl)
                        .setField(MultipleFields.ID, sceneryId)
                        .setField(MultipleFields.ITEM_TYPE,ItemType.COLLECT_PART)
                        .setField(MultipleFields.SPAN_SIZE,4)
                        .build();
                BEANS.add(bean);
            }

        }
        return BEANS;
    }
}
