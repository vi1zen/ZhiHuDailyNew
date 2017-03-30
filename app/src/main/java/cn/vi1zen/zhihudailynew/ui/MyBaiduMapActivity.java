package cn.vi1zen.zhihudailynew.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import cn.vi1zen.zhihudailynew.R;
import cn.vi1zen.zhihudailynew.tool.MyOrientationListener;

/**
 * Created by Destiny on 2017/3/28.
 */

public class MyBaiduMapActivity extends Activity implements View.OnClickListener{
    private MapView mMapView;
    private ImageButton mLocationButton;
    private BaiduMap mBaiduMap;
    //定位相关
    private LocationClient mLocationClient;
    private MyLocationistener mLocationListener;
    private boolean isFirstIn = true;
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());//在SDK各功能组件使用之前都需要调用,因此我们建议该方法放在Application的初始化方法中
        setContentView(R.layout.baidu_map);
        initView();
        initLocation();
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationistener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        myOrientationListener = new MyOrientationListener(this);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChange(float x) {
                mCurrentX = x;
            }
        });
    }

    private void initView() {
        mLocationButton = (ImageButton) findViewById(R.id.location_button);
        mLocationButton.setOnClickListener(this);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.location_button:
                isFirstIn = true;
                break;
        }
    }

    private class MyLocationistener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .direction(mCurrentX)//方向
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            //设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.mipmap.arrow);
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);
            if(isFirstIn){
                LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirstIn = false;
                Toast.makeText(MyBaiduMapActivity.this,bdLocation.getAddrStr(),Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        if(!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        //开始方向传感器
        myOrientationListener.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        if(mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        //停止方向传感器
        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
