package com.example.travelapp.ec.main.comment;

import android.os.Bundle;
import android.view.View;

import com.example.travelapp.R;
import com.example.travelapp.core.delegates.TravelDelegate;

import androidx.annotation.NonNull;

public class ShareCommentDetialDelegate extends TravelDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_share_comment_detial;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {

    }
}
