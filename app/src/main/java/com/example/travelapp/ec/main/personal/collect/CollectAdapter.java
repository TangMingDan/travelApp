package com.example.travelapp.ec.main.personal.collect;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.travelapp.R;
import com.example.travelapp.core.ui.recycler.ItemType;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.core.ui.recycler.MultipleviewHoloder;

import java.util.List;

import androidx.annotation.NonNull;

public class CollectAdapter extends BaseQuickAdapter<MultipleItemBean, BaseViewHolder> {
    protected CollectAdapter(List<MultipleItemBean> data) {
        super(R.layout.item_collect_layout,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultipleItemBean item) {
        helper.setText(R.id.tv_collect_scenery_name,item.getField(MultipleFields.TEXT));
        helper.setText(R.id.tv_collect_scenery_location,item.getField(MultipleFields.CONTENT));
        Glide.with(mContext)
                .load(item.getField(MultipleFields.IMAGE_URL) + "")
                .into((ImageView) helper.getView(R.id.img_collect_scenery_img));
    }

}
