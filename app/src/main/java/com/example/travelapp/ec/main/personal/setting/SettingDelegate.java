package com.example.travelapp.ec.main.personal.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.utils.callback.CallbackManager;
import com.example.travelapp.core.utils.callback.CallbackType;
import com.example.travelapp.ec.main.personal.list.ListAdapter;
import com.example.travelapp.ec.main.personal.list.ListBean;
import com.example.travelapp.ec.main.personal.list.ListItemType;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SettingDelegate extends TravelDelegate {

    @BindView(R2.id.rv_settings)
    RecyclerView mRecyclerView = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_settings;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        final ListBean sHelpNum = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setId(1)
                .setText("设置紧急联系人")
                .setValue("尚未设置")
                .build();
        final ListBean push = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_SWITCH)
                .setId(2)
                .setText("消息推送")
                .setCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            CallbackManager.getInstance().getCallback(CallbackType.TAG_OPEN_PUSH).executeCallback(null);
                            Toast.makeText(getContext(),"打开推送",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(),"关闭推送",Toast.LENGTH_SHORT).show();
                            CallbackManager.getInstance().getCallback(CallbackType.TAG_OPEN_PUSH).executeCallback(null);
                        }
                    }
                })
                .build();
        final ListBean about = new ListBean.Builder()
                .setItemType(ListItemType.ITEM_NORMAL)
                .setDelegate(new AboutDelegate())
                .setId(3)
                .setText("关于")
                .build();
        final List<ListBean> data = new ArrayList<>();
        data.add(sHelpNum);
        data.add(push);
        data.add(about);

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        final ListAdapter adapter = new ListAdapter(data);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new SettingClickListener(this));

    }
}
