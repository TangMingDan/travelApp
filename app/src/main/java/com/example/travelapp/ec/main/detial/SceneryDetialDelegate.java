package com.example.travelapp.ec.main.detial;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.app.UserInfoType;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.net.RestClient;
import com.example.travelapp.core.net.callback.IError;
import com.example.travelapp.core.net.callback.IFailure;
import com.example.travelapp.core.net.callback.ISuccess;
import com.example.travelapp.core.ui.banner.BannerCreator;
import com.example.travelapp.core.ui.recycler.BaseDecoration;
import com.example.travelapp.core.ui.recycler.ItemType;
import com.example.travelapp.core.ui.recycler.MultipleFields;
import com.example.travelapp.core.ui.recycler.MultipleItemBean;
import com.example.travelapp.core.ui.recycler.MultipleRecyclerAdapter;
import com.example.travelapp.core.utils.storage.TravelPreference;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class SceneryDetialDelegate extends TravelDelegate implements AppBarLayout.OnOffsetChangedListener {
    @BindView(R2.id.scenery_detail_toolbar)
    Toolbar mToolbar = null;


    @BindView(R2.id.detail_banner)
    ConvenientBanner<String> mBanner = null;
    @BindView(R2.id.collapsing_toolbar_detail)
    CollapsingToolbarLayout mCollapsingToolbarLayout = null;
    @BindView(R2.id.app_bar_detail)
    AppBarLayout mAppBar = null;
    @BindView(R2.id.rl_goods)
    RecyclerView mRecyclerView = null;
    private List<MultipleItemBean> itemBeans = new ArrayList<>();
    MultipleRecyclerAdapter adapter;


    @BindView(R2.id.rl_add_shop_cart)
    RelativeLayout mRlAddShopCart = null;
    @BindView(R2.id.icon_shop_cart)
    IconTextView mIconShopCart = null;

    @OnClick(R2.id.rl_add_shop_cart)
    void addCollect() {
        RestClient.builder()
                .url("collcet/save")
                .loader(getContext())
                .param("userId", TravelPreference.getCustomAppProfile(UserInfoType.USER_ID.name()))
                .param("sceneryId",mSceneryId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String reason = object.getString("reason");
                        if(reason.equals("删除成功")){
                            mIconShopCart.setTextColor(Color.GRAY);
                        }else if(reason.equals("保存成功")){
                            mIconShopCart.setTextColor(Color.RED);
                            Toast.makeText(getContext(),"保存成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(getContext(),"错误",Toast.LENGTH_SHORT).show();
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(),"失败",Toast.LENGTH_SHORT).show();
                    }
                })
                .bulid()
                .get();
    }

    @OnClick(R2.id.rl_add_remark)
    void onAddRemark() {
        TravelDelegate delegate = RemarkDelegate.create(mSceneryId);
        start(delegate);
    }


    private static final String ARG_SCENERY_ID = "ARG_SCENERY_ID";
    private int mSceneryId = -1;

    public static SceneryDetialDelegate create(@NonNull int goodsId) {
        final Bundle args = new Bundle();
        args.putInt(ARG_SCENERY_ID, goodsId);
        final SceneryDetialDelegate delegate = new SceneryDetialDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mSceneryId = args.getInt(ARG_SCENERY_ID);
            Toast.makeText(getContext(), mSceneryId + "", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_scenery_detail;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
        mCollapsingToolbarLayout.setContentScrimColor(Color.WHITE);
        mAppBar.addOnOffsetChangedListener(this);
        initData();

    }

    private void initData() {
        RestClient.builder()
                .url("scenery/findSceneryById")
                .param("sceneryId", mSceneryId)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject data =
                                JSON.parseObject(response).getJSONObject("data");
                        initBanner(data);
                        initSceneryInfo(data);
                        initRecycler(data);
                    }
                })
                .bulid()
                .post();

    }

    private void initRecycler(JSONObject data) {
        JSONArray goodsData = data.getJSONArray("goodsList");
        int size = goodsData.size();
        for (int i = 0; i < size; i++) {
            JSONObject goods = goodsData.getJSONObject(i);
            JSONObject goodImgData = goods.getJSONObject("goodsImg");
            String imgUrl = goodImgData.getString("imgUrl");
            String goodsName = goods.getString("goodsName");
            MultipleItemBean bean = MultipleItemBean.builder()
                    .setField(MultipleFields.IMAGE_URL,imgUrl)
                    .setField(MultipleFields.SPAN_SIZE,4)
                    .setField(MultipleFields.ITEM_TYPE,ItemType.TEXT_IMAGE)
                    .setField(MultipleFields.TEXT,goodsName)
                    .build();
            itemBeans.add(bean);
        }
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.addItemDecoration(
                BaseDecoration.create(ContextCompat.getColor(getContext(), R.color.app_backgroud), 5));
        mRecyclerView.setLayoutManager(manager);
        adapter = MultipleRecyclerAdapter.create(itemBeans);
        mRecyclerView.setAdapter(adapter);
    }

    private void initSceneryInfo(JSONObject data) {
        final String sceneryData = data.toJSONString();
        getSupportDelegate().loadRootFragment(R.id.frame_scenery_info, SceneryInfoDelegate.create(sceneryData));
    }

    private void initBanner(JSONObject data) {
        final JSONArray array = data.getJSONArray("sceneryImgs");
        final List<String> images = new ArrayList<>();
        final int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject imgData = array.getJSONObject(i);
            images.add(imgData.getString("imgUrl"));
        }
        BannerCreator.setDefault(mBanner, (ArrayList<String>) images, null);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

    }
}
