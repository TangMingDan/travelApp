package com.example.travelapp.ec.main;

import com.example.travelapp.R;
import com.example.travelapp.core.delegates.bottom.BaseBottomDelegate;
import com.example.travelapp.core.delegates.bottom.BottomItemDelegate;
import com.example.travelapp.core.delegates.bottom.BottomTabBean;
import com.example.travelapp.core.delegates.bottom.ItemBuilder;
import com.example.travelapp.ec.main.hotspot.HotSpotDelegate;
import com.example.travelapp.ec.main.index.IndexDelegate;
import com.example.travelapp.ec.main.map.MapDelegate;
import com.example.travelapp.ec.main.sort.SortDelegate;

import java.util.LinkedHashMap;

public class EcBottomDelegate extends BaseBottomDelegate {
    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
        final LinkedHashMap<BottomTabBean,BottomItemDelegate> items = new LinkedHashMap<>();
        items.put(new BottomTabBean("{fa-home}","主页"),new IndexDelegate());
        items.put(new BottomTabBean("{fa-sort}","分类"),new SortDelegate());
        items.put(new BottomTabBean("{fa-user}","旅游热点"),new HotSpotDelegate());
        items.put(new BottomTabBean("{fa-shopping-cart}","地图"),new MapDelegate());
        return builder.addItems(items).build();
    }

    @Override
    public int setIndexDelegate() {
        return 0;
    }

    @Override
    public int setClickedColor() {
        return getResources().getColor(R.color.app_main);
    }
}
