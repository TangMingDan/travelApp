package com.example.travelapp.ec.main.map;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.travelapp.core.app.Travel;

public class TravleMap {
    public LocationClient mLocationClient = null;
    public BaiduMap mBaiduMap = null;
    public boolean isFirstLocate = true;
    private MapView mMapView = null;


    public static class Holder{
        public static TravleMap MAP_INSTANCE = new TravleMap();
    }

    public static TravleMap getInstance(){
        return Holder.MAP_INSTANCE;
    }

    public TravleMap() {
        mLocationClient = new LocationClient(Travel.getApplicationContext());
        initLocation();
    }

    public void initClient(){
        if(mLocationClient == null){
            mLocationClient = new LocationClient(Travel.getApplicationContext());
            initLocation();
        }
    }

    public void initMap(MapView mapView){
        mMapView = mapView;
        mBaiduMap = mMapView.getMap();
        mLocationClient.registerLocationListener(new MyLocationListener());
        mLocationClient.start();
        mBaiduMap.setMyLocationEnabled(true); //将我显示在地图上
    }

    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder builder = new StringBuilder();
            builder.append("纬度").append(bdLocation.getLatitude()).append("\n");
            builder.append("经线").append(bdLocation.getLongitude()).append("\n");
            builder.append("定位方式");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                builder.append("GPS");
            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                builder.append("网络");
            }
            navigateTo(bdLocation);
        }
    }

    //设置地图随时间更新
    public void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll"); //将坐标转化为百度地图坐标
        option.setScanSpan(3000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    //移动到当前位置
    public void navigateTo(BDLocation bdLocation) {
        if(isFirstLocate){
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomBy(12.5f);
            mBaiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(bdLocation.getLatitude());
        locationBuilder.longitude(bdLocation.getLongitude() );
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }
}
