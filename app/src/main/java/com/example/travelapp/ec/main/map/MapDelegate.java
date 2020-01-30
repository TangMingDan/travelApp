package com.example.travelapp.ec.main.map;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.travelapp.R;
import com.example.travelapp.R2;
import com.example.travelapp.core.app.Travel;
import com.example.travelapp.core.delegates.TravelDelegate;
import com.example.travelapp.core.delegates.bottom.BottomItemDelegate;
import com.example.travelapp.core.utils.callback.CallbackManager;
import com.example.travelapp.core.utils.callback.CallbackType;
import com.example.travelapp.core.utils.callback.IGlobalCallback;
import com.example.travelapp.ec.main.personal.help.HelpHandler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class MapDelegate extends BottomItemDelegate {

    @BindView(R2.id.map_mapView)
    MapView mMapView = null;  //地图视图

    @OnClick(R2.id.ll_help)
    void onClickShowDialog(){
        HelpHandler.getInstance().showDialog(this);
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_map;
    }

    @Override
    public void onBindView(@NonNull Bundle savedInstanceState, View rootView) {
//        initTravelMapWithCheck(mMapView);
        initCallback();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initTravelMapWithCheck(mMapView);
    }

    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (TravleMap.getInstance().mLocationClient != null) {
            TravleMap.getInstance().mLocationClient.stop();
        }
        if (TravleMap.getInstance().mBaiduMap != null) {
            TravleMap.getInstance().mBaiduMap.setMyLocationEnabled(false);
        }
    }
    public void initCallback(){
        CallbackManager.getInstance().addCallback(CallbackType.SEND_SOS_MESSAGE, new IGlobalCallback() {
            @Override
            public void executeCallback(@Nullable Object args) {
                sendSosMessage(args);
            }
        });
    }
}
