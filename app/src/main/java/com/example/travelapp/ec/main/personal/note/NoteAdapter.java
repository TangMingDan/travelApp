package com.example.travelapp.ec.main.personal.note;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.travelapp.R;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.core.ui.recycler.MultipleviewHoloder;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.List;

import androidx.annotation.NonNull;

public class NoteAdapter extends BaseQuickAdapter<MultipleItemBean,BaseViewHolder> {


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public NoteAdapter(List<MultipleItemBean> data) {
        super(R.layout.item_note,data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultipleItemBean item) {
        switch (item.getItemType()) {
            case 1:
                IconTextView iconTextView = helper.getView(R.id.icon_note_state);
                iconTextView.setText("{icon-write}");
                iconTextView.setTextColor(Color.RED);
                helper.setText(R.id.tv_note_title, item.getField(MultipleFields.TITLE));
                helper.setText(R.id.tv_note_scenery, item.getField(MultipleFields.TEXT));
                break;
            case 2:
                IconTextView iconText = helper.getView(R.id.icon_note_state);
                iconText.setText("{icon-tick}");
                iconText.setTextColor(Color.YELLOW);
                helper.setText(R.id.tv_note_title, item.getField(MultipleFields.TITLE));
                helper.setText(R.id.tv_note_scenery, item.getField(MultipleFields.TEXT));
                break;
            default:
                break;
        }
    }

}
