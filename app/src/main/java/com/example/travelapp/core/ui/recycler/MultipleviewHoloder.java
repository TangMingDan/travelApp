package com.example.travelapp.core.ui.recycler;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

public class MultipleviewHoloder extends BaseViewHolder {


    private MultipleviewHoloder(View view) {
        super(view);
    }
    public static MultipleviewHoloder create(View view){
        return new MultipleviewHoloder(view);
    }
}
