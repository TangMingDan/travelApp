package com.example.travelapp.ec.main.personal.note;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.travelapp.core.ui.recycler.DataConverter;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;

import java.util.ArrayList;
import java.util.List;

public class NoteDataConverter extends DataConverter {
    @Override
    public ArrayList<MultipleItemBean> convert() {
        final JSONArray dataArray = JSON.parseObject(getJsonData()).getJSONArray("data");
        final int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            JSONObject data = dataArray.getJSONObject(i);
            int noteId = data.getInteger("noteId");
            String noteTitle = data.getString("noteTitle");
            String noteContent = data.getString("noteContent");
            String noteTime = data.getString("noteTime");
            int noteSceneryId = data.getInteger("sceneryId");
            int state = data.getInteger("state");
            String sceneryName = data.getString("sceneryName");
            MultipleItemBean bean = MultipleItemBean.builder()
                    .setField(MultipleFields.ID,noteId)
                    .setField(MultipleFields.TITLE,noteTitle)
                    .setField(MultipleFields.NUM,noteSceneryId)
                    .setField(MultipleFields.CONTENT,noteContent)
                    .setField(MultipleFields.TIME,noteTime)
                    .setField(MultipleFields.TEXT,sceneryName)
                    .setField(MultipleFields.ITEM_TYPE,state)
                    .build();
            BEANS.add(bean);
        }
        return BEANS;
    }
}
