package com.example.travelapp.ec.main.personal.note.sceneryName;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.travelapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class SceneryNameAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public SceneryNameAdapter(@Nullable List data) {
        super(R.layout.item_scenery_name,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_scenery_name,item);
    }
}
