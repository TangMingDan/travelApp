package com.example.travelapp.core.ui.recycler;

import java.util.ArrayList;

public abstract class DataConverter {
    protected final ArrayList<MultipleItemBean> BEANS = new ArrayList<>();
    private String mJsonData = null;

    public abstract ArrayList<MultipleItemBean> convert();

    public DataConverter setJsonData(String json) {
        this.mJsonData = json;
        return this;
    }

    public String getJsonData() {
//        if (mJsonData == null || mJsonData.isEmpty()) {
//            throw new NullPointerException("DATA IS NULL");
//        }
        return mJsonData;
    }

    public void  clearBeans(){
        if(!BEANS.isEmpty()){
            BEANS.clear();
        }
    }
}
