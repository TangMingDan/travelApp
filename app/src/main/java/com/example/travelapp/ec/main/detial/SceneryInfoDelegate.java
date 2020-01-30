package com.example.travelapp.ec.main.detial;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.delegates.TravelDelegate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;

public class SceneryInfoDelegate extends TravelDelegate {

    @BindView(R2.id.tv_scenery_info_title)
    AppCompatTextView mSceneryInfoTitle = null;
    @BindView(R2.id.tv_scenery_info_desc)
    AppCompatTextView mSceneryInfoDesc = null;
    @BindView(R2.id.tv_scenery_info_price)
    AppCompatTextView mSceneryInfoPrice = null;
    @BindView(R2.id.tv_scenery_info_location)
    AppCompatTextView mSceneryLocation = null;

    private static final String ARG_SCENERY_DATA = "ARG_SCENERY_DATA";
    private JSONObject mData = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        final String sceneryData = args.getString(ARG_SCENERY_DATA);
        mData = JSON.parseObject(sceneryData);
    }

    public static SceneryInfoDelegate create(String sceneryData){
        final Bundle args = new Bundle();
        args.putString(ARG_SCENERY_DATA,sceneryData);
        final SceneryInfoDelegate delegate = new SceneryInfoDelegate();
        delegate.setArguments(args);
        return delegate;
    }
    @Override
    public Object setLayout() {
        return R.layout.delegate_scenery_info;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        final String name = mData.getString("sceneryName");
        final String desc = mData.getString("sceneryDescription");
        final double price = mData.getDouble("sceneryPrice");
        final String location = mData.getString("sceneryLocation");
        mSceneryInfoTitle.setText(name);
        mSceneryInfoDesc.setText(desc);
        mSceneryInfoPrice.setText(String.valueOf(price));
        mSceneryLocation.setText(location);
    }
}
