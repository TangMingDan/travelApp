package com.example.travelapp.ec.main.sort.content;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SectionDataConverter {

    public List<SectionBean> conervt(String json){
        final List<SectionBean> dataList = new ArrayList<>();
        final JSONArray dataArray = JSON.parseObject(json).getJSONArray("data");
        int size = dataArray.size();
        for (int i = 0; i < size; i++) {
            final JSONObject data = dataArray.getJSONObject(i);
            final int id = data.getInteger("sceneryId");
            final String title = data.getString("sceneryName");

            //添加title
            final SectionBean sectionBean = new SectionBean(true,title);
            sectionBean.setId(id);
            sectionBean.setIsMore(true);
            dataList.add(sectionBean);




            final JSONArray sceneryImgs = data.getJSONArray("sceneryImgs");
            //商品内容循环
            int scenerySize = sceneryImgs.size();
            for (int j = 0; j < scenerySize / 2; j++) {
                final JSONObject contentItem = sceneryImgs.getJSONObject(j);

                final int imgId = contentItem.getInteger("imgId");
                final String sceneryThumb = contentItem.getString("imgUrl");
                //获取内容
                final SectionContentItemBean itemBean = new SectionContentItemBean();
                itemBean.setImgUrl(sceneryThumb);
                itemBean.setImgId(imgId);
                //添加内容
                dataList.add(new SectionBean(itemBean));
            }
        }
        return dataList;
    }
}
