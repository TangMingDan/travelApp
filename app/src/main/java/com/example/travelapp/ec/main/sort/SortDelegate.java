package com.example.travelapp.ec.main.sort;

import android.os.Bundle;
import android.view.View;

import com.example.travelapp.R;
import com.example.travelapp.core.delegates.bottom.BottomItemDelegate;
import com.example.travelapp.ec.main.sort.content.ContentDelegate;
import com.example.travelapp.ec.main.sort.list.SortListDelegate;

import androidx.annotation.NonNull;

public class SortDelegate extends BottomItemDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_sort;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        super.onLazyInitView(savedInstanceState);
        final SortListDelegate listDelegate = new SortListDelegate();
        getSupportDelegate().loadRootFragment(R.id.vertical_list_container,listDelegate);
        getSupportDelegate().loadRootFragment(R.id.sort_content_container, ContentDelegate.newInstance(1));
    }
}
