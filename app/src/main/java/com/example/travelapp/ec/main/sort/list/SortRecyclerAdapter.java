package com.example.travelapp.ec.main.sort.list;

import android.graphics.Color;
import android.view.View;

import com.example.travelapp.R;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.ui.recycler.ItemType;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.core.ui.recycler.MultipleviewHoloder;
import com.example.travelapp.ec.main.sort.SortDelegate;
import com.example.travelapp.ec.main.sort.content.ContentDelegate;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

public class SortRecyclerAdapter extends MultipleRecyclerAdapter {

    private final SortDelegate DELEGATE;
    private int mPrePosition = 0;

    protected SortRecyclerAdapter(List<MultipleItemBean> data,SortDelegate delegate) {
        super(data);
        this.DELEGATE = delegate;
        addItemType(ItemType.VERTICAL_MENU_LIST, R.layout.item_vertical_menu_list);
    }

    @Override
    protected void convert(@NonNull MultipleviewHoloder helper, MultipleItemBean item) {
        super.convert(helper, item);
        switch (item.getItemType()){
            case ItemType.VERTICAL_MENU_LIST:
                final String text = item.getField(MultipleFields.TEXT);
                final boolean isClicked = item.getField(MultipleFields.TAG);
                final AppCompatTextView name = helper.getView(R.id.tv_vertical_item_name);
                final View line = helper.getView(R.id.view_line);
                final View itemView = helper.itemView;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int currentPosition = helper.getAdapterPosition();
                        if(mPrePosition != currentPosition){
                            //还原上一个
                            getData().get(mPrePosition).setField(MultipleFields.TAG,false);
                            notifyItemChanged(mPrePosition);

                            //更新选中的item
                            item.setField(MultipleFields.TAG,true);
                            notifyItemChanged(currentPosition);
                            mPrePosition = currentPosition;

                            final int countId = getData().get(currentPosition).getField(MultipleFields.ID);
                            showContent(countId);
                        }
                    }
                });
                if(!isClicked){
                    line.setVisibility(View.INVISIBLE);
                    name.setTextColor(ContextCompat.getColor(mContext,R.color.we_chat_black));
                    itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.item_background));
                }else {
                    line.setVisibility(View.VISIBLE);
                    name.setTextColor(ContextCompat.getColor(mContext,R.color.app_main));
                    line.setBackgroundColor(ContextCompat.getColor(mContext,R.color.app_main));
                    itemView.setBackgroundColor(Color.WHITE);
                }
                helper.setText(R.id.tv_vertical_item_name,text);
                break;
            default:
                break;
        }
    }
    private void showContent(int contentId){
        final ContentDelegate delegate = ContentDelegate.newInstance(contentId);
        switchContent(delegate);
    }
    private void switchContent(ContentDelegate delegate){
        final TravelDelegate contentDelegate = DELEGATE.findChildFragment(ContentDelegate.class);
        if(contentDelegate != null){
            contentDelegate.replaceFragment(delegate,false);
        }
    }
}
