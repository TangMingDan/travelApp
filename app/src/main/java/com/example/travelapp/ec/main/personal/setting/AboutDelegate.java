package com.example.travelapp.ec.main.personal.setting;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.ISuccess;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import butterknife.BindView;

public class AboutDelegate extends TravelDelegate {

    @BindView(R2.id.tv_info)
    AppCompatTextView mTextView = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_about;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        mTextView.setText("TravelAPP，version1.0,完成时间2020年1月，试运行");
        RestClient.builder()
                .url("/about.json")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                       final String info = JSON.parseObject(response).getString("data");
                       mTextView.setText(info);
                    }
                })
                .bulid()
                .get();
    }
}
