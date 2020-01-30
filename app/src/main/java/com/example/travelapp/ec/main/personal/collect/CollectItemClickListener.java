package com.example.travelapp.ec.main.personal.collect;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.ec.main.detial.SceneryDetialDelegate;
import com.example.travelapp.ec.main.personal.note.NoteItemClickListener;

public class CollectItemClickListener extends SimpleClickListener {
    private TravelDelegate delegate;

    public CollectItemClickListener(TravelDelegate delegate) {
        this.delegate = delegate;
    }

    public static SimpleClickListener create(TravelDelegate delegate){
        return new CollectItemClickListener(delegate);
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final MultipleItemBean bean = (MultipleItemBean) baseQuickAdapter.getData().get(position);
        final int goodsId = bean.getField(MultipleFields.ID);
        final SceneryDetialDelegate detialDelegate = SceneryDetialDelegate.create(goodsId);
        delegate.start(detialDelegate);
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
