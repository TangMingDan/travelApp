package com.example.travelapp.core.utils.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.travelapp.core.app.Travel;

public class DimenUtil {

    public static int getScreenWidth(){
        final Resources resources = Travel.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(){
        final Resources resources = Travel.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
