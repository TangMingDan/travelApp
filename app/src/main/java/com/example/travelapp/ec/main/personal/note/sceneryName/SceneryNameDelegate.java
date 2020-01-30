package com.example.travelapp.ec.main.personal.note.sceneryName;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.core.utils.callback.CallbackManager;
import com.example.travelapp.core.utils.callback.CallbackType;
import com.example.travelapp.core.utils.callback.IGlobalCallback;
import com.example.travelapp.ec.main.sort.SortDelegate;
import com.example.travelapp.ec.main.sort.list.SortListDataConverter;
import com.example.travelapp.ec.main.sort.list.SortRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class SceneryNameDelegate extends TravelDelegate {

    @BindView(R2.id.rv_scenery)
    RecyclerView mRecyclerview = null;

    private int level = 1;
    private List<String> dataList = new ArrayList<>();
    private List<Integer> tagIds = new ArrayList<>();
    private SceneryNameAdapter adapter = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_scenery_name;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        adapter = new SceneryNameAdapter(dataList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerview.setLayoutManager(manager);
        mRecyclerview.setAdapter(adapter);
        mRecyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(level == 2){
                    initSceneryName(tagIds.get(position));
                }else if(level == 1){
                    String data = (String) adapter.getData().get(position);
                    IGlobalCallback callback = CallbackManager.getInstance()
                            .getCallback(CallbackType.WRITE_SCENERY);
                    if(callback != null){
                        callback.executeCallback(tagIds.get(position) + "/"+data);
                        pop();
                    }
                }
            }
        });
    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        RestClient.builder()
                .url("tag/alltags")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONArray sceneryArray = JSON.parseObject(response).getJSONArray("data");
                        int size = sceneryArray.size();
                        for (int i = 0; i < size; i++) {
                            JSONObject sceneryObject = sceneryArray.getJSONObject(i);
                            dataList.add(sceneryObject.getString("tagName"));
                            tagIds.add(sceneryObject.getInteger("tagId"));
                        }
                        adapter.setNewData(dataList);
                        adapter.notifyDataSetChanged();
                        level += 1;
                    }
                })
                .bulid()
                .get();
    }
    public void initSceneryName(int tagId){

        RestClient.builder()
                .url("scenery/findSceneryByTag")
                .param("tagId",tagId)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONArray sceneryArray = JSON.parseObject(response).getJSONArray("data");
                        dataList.clear();
                        tagIds.clear();
                        int size = sceneryArray.size();
                        for (int i = 0; i < size; i++) {
                            JSONObject sceneryObject = sceneryArray.getJSONObject(i);
                            dataList.add(sceneryObject.getString("sceneryName"));
                            tagIds.add(sceneryObject.getInteger("sceneryId"));
                        }
                        adapter.setNewData(dataList);
                        adapter.notifyDataSetChanged();
                        level -= 1;
                    }
                })
                .bulid()
                .post();
    }
}
