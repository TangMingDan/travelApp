package com.example.travelapp.ec.main.comment;

import android.os.Bundle;
import android.view.View;

import com.example.travelapp.R;
import com.example.travelapp.core.delegates.TravelDelegate;

import androidx.annotation.NonNull;

public class CommentDelegate extends TravelDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_comment;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {

    }
}
