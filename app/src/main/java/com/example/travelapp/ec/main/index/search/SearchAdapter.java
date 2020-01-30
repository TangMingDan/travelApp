package com.example.travelapp.ec.main.index.search;

import com.example.travelapp.R;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.core.ui.recycler.MultipleviewHoloder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

public class SearchAdapter extends MultipleRecyclerAdapter {
    protected SearchAdapter(List<MultipleItemBean> data) {
        super(data);
        addItemType(SearchItemType.ITEM_SEARCH,R.layout.item_search);
    }

    @Override
    protected void convert(@NonNull MultipleviewHoloder helper, MultipleItemBean item) {
        super.convert(helper, item);
        switch (item.getItemType()){
            case SearchItemType.ITEM_SEARCH:
                final AppCompatTextView tvSearchItem = helper.getView(R.id.tv_search_item);
                final String history = item.getField(MultipleFields.TEXT);
                tvSearchItem.setText(history);
                break;
            default:
                break;
        }
    }
}
