package com.example.travelapp.ec.main.sort.content;

import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.example.travelapp.R;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.ec.main.detial.SceneryDetialDelegate;
import com.example.travelapp.ec.main.index.IndexItemClickListener;

import retrofit2.http.DELETE;

public class ContentItemClickListener extends SimpleClickListener {
    private final TravelDelegate DELEGATE;

    private ContentItemClickListener(TravelDelegate delegate) {
        this.DELEGATE = delegate;
    }

    public static SimpleClickListener create(TravelDelegate delegate){
        return new ContentItemClickListener(delegate);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final SectionBean bean = (SectionBean) baseQuickAdapter.getData().get(position);
        final int sceneryId = bean.getId();
        if(sceneryId != -1){
            final SceneryDetialDelegate delegate = SceneryDetialDelegate.create(sceneryId);
            DELEGATE.start(delegate);
        }
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
