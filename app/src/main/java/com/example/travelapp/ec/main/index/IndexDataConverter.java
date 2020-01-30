package com.example.travelapp.ec.main.index;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.ItemType;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.core.ui.recycler.MultipleItemBeanBuilder;

import java.util.ArrayList;

public class IndexDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemBean> convert() {
        if(getJsonData() == null){
            return BEANS;
        }
        int type = 0;
        final JSONArray dataArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data  = dataArray.getJSONObject(i);
            final String text = data.getString("sceneryName");
            final int id = data.getInteger("sceneryId");
            final JSONArray imgs = data.getJSONArray("sceneryImgs");

            final MultipleItemBean bean = MultipleItemBean.builder()
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.TEXT,text)
                    .build();

            if(0 == i){
                final ArrayList<String> bannerImages = new ArrayList<>();
                int bannerSize = imgs.size();
                for (int j = 0; j < bannerSize; j++) {
                    JSONObject imgData = imgs.getJSONObject(j);
                    bannerImages.add(imgData.getString("imgUrl"));
                }
                type = ItemType.BANNER;
                bean.setField(MultipleFields.BANNERS,bannerImages)
                        .setField(MultipleFields.SPAN_SIZE,4)
                        .setField(MultipleFields.ITEM_TYPE,type);
            }else {
                String imgUrl = imgs.getJSONObject(0).getString("imgUrl");
                type = ItemType.TEXT_IMAGE;
                bean.setField(MultipleFields.IMAGE_URL,imgUrl)
                        .setField(MultipleFields.SPAN_SIZE,4)
                        .setField(MultipleFields.ITEM_TYPE,type);
            }

            BEANS.add(bean);
        }
        return BEANS;
    }
}
