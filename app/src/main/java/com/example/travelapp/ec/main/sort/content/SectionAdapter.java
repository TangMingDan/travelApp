package com.example.travelapp.ec.main.sort.content;

import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.travelapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import static com.example.travelapp.R2.id.iv;

public class SectionAdapter extends BaseSectionQuickAdapter<SectionBean, BaseViewHolder> {

    public SectionAdapter(int layoutResId, int sectionHeadResId, List<SectionBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, SectionBean item) {
        helper.setText(R.id.header,item.header);
        helper.setVisible(R.id.more,item.isMore());
//        helper.getView(R.id.more).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext,"more",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SectionBean item) {
        //item.t返回SectionBean
        final String thumb = item.t.getImgUrl();
//        final SectionContentItemBean bean = item.t;

        final AppCompatImageView goodImageView = helper.getView(R.id.iv);
        Glide.with(mContext)
                .load(thumb)
                .into(goodImageView);
    }
}
