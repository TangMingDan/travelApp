package com.example.travelapp.ec.main.hotspot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.ItemType;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;

import java.util.ArrayList;
import java.util.List;

public class HotSpotDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemBean> convert() {
        if(getJsonData() == null){
            return BEANS;
        }
        final JSONArray dataArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = dataArray.getJSONObject(i);

            final int id = data.getInteger("remarkId");
            final String title = data.getString("remarkTitle");
            final String content = data.getString("remarkContent");
            final String sceneryName = data.getString("sceneryName");
            final int sceneryId = data.getInteger("sceneryId");
            final String time = data.getString("remarkTime");


            JSONObject userData = data.getJSONObject("user");
            final String headImage = userData.getString("avatar");
            final String name = userData.getString("userName");


            final int spanSize = 4;
            int type = ItemType.SHARE_PART;

            final ArrayList<String> images = new ArrayList<>();
            int imageNum = 0;
            JSONArray jsonImages = data.getJSONArray("remarkImgList");
            if(!jsonImages.isEmpty()){
                imageNum = jsonImages.size();
                for (int j = 0; j < imageNum; j++) {
                    JSONObject imgData = jsonImages.getJSONObject(j);

                    final String image = imgData.getString("imgUrl");
                    images.add(image);
                }
            }
            final MultipleItemBean bean = MultipleItemBean.builder()
                    .setField(MultipleFields.ITEM_TYPE,type)
                    .setField(MultipleFields.IMAGE_URL,headImage)
                    .setField(MultipleFields.SPAN_SIZE,spanSize)
                    .setField(MultipleFields.ID,id)
                    .setField(MultipleFields.NAME,name)
                    .setField(MultipleFields.TITLE,title)
                    .setField(MultipleFields.CONTENT,content)
                    .setField(MultipleFields.TEXT,sceneryName)
                    .setField(MultipleFields.OTHER_ID,sceneryId)
                    .setField(MultipleFields.NUM,imageNum)
                    .setField(MultipleFields.TIME,time)
                    .setField(MultipleFields.BANNERS,images)
                    .build();
            BEANS.add(bean);
        }
        return BEANS;
    }
}
