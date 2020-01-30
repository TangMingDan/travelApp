package com.example.travelapp.ec.launcher;

import android.util.Log;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.example.travelapp.R;

import androidx.appcompat.widget.AppCompatImageView;

public class LauncherHolder extends Holder<Integer> {

    private AppCompatImageView mImageView;

    public LauncherHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }


    @Override
    protected void initView(View itemView) {
        mImageView = itemView.findViewById(R.id.iv_guide_page);
    }

    @Override
    public void updateUI(Integer data) {
        if(mImageView != null) {
            mImageView.setBackgroundResource(data);
        }else {
            Log.d("123456","失败");
        }
    }
}
