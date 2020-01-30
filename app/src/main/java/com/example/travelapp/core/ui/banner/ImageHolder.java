package com.example.travelapp.core.ui.banner;

import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.travelapp.R;
import com.example.travelapp.core.app.Travel;

import androidx.appcompat.widget.AppCompatImageView;

public class ImageHolder extends Holder<String> {

    private AppCompatImageView mImageView = null;

    public ImageHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    @Override
    protected void initView(View itemView) {
        mImageView = itemView.findViewById(R.id.img_banner);
    }

    @Override
    public void updateUI(String data) {
        Glide.with(Travel.getApplicationContext())
                .load(data)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .centerCrop()
                .into(mImageView);
    }
}
