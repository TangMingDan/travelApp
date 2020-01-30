package com.example.travelapp.core.ui.recycler;

import com.choices.divider.DividerItemDecoration;

import androidx.annotation.ColorInt;

public class BaseDecoration extends DividerItemDecoration {

    private BaseDecoration(@ColorInt int color, int size) {
        setDividerLookup(new DividerLookUpImpl(color,size));
    }

    public static BaseDecoration create(@ColorInt int color, int size){
        return new BaseDecoration(color,size);
    }
}
