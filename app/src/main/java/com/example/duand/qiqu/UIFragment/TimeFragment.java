package com.example.duand.qiqu.UIFragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.MyLocationStyle;
//import com.amap.api.track.AMapTrackClient;
//import com.amap.api.track.OnTrackLifecycleListener;
//import com.amap.api.track.TrackParam;
//import com.amap.api.track.query.model.QueryTrackRequest;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.model.MyLocationStyle;
import com.example.duand.qiqu.Constants;
import com.example.duand.qiqu.R;

public class TimeFragment extends Fragment {

//    MapView mMapView;
//    AMap aMap;
//    MyLocationStyle myLocationStyle;
//    public AMapLocationClient mLocationClient = null;  //声明AMapLocationClient类对象
//    public AMapLocationListener mLocationListener;  //声明定位回调监听器
//    public AMapLocationClientOption mLocationOption = null; //声明AMapLocationClientOption对象
//    Button button1,button2,button3,button4,button5; //开始定位起点
//    public static double latitud; //经度
//    public static double longitude; //纬度
//    static LatLng latLng;//通过小蓝点获取到的坐标
//
//    long serviceId=16164;
//    String terminalName="zy";
//    long terminalId=100281481;
//    long trackId=0;
//    AMapTrackClient aMapTrackClient;
//    OnTrackLifecycleListener onTrackLifecycleListener;
//    TrackParam trackParam=new TrackParam(serviceId,terminalId);
//
//    QueryTrackRequest queryTrackRequest; //查询终端轨迹点信息


        public static TimeFragment newInstance(String s) {
        TimeFragment timeFragment = new TimeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS, s);
        timeFragment.setArguments(bundle);
        return timeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.base_fragment, container, false);

        Bundle bundle = getArguments();
        String s = bundle.getString(Constants.ARGS);
        TextView textView = (TextView) view.findViewById(R.id.base_fragment_tv);
        textView.setText(s);

//        mMapView = (MapView)view.findViewById(R.id.map);
//        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
//        mMapView.onCreate(savedInstanceState);
//
//        initMap(); //初始化地图
//        initMyLocation(); // 初始化定位

        return view;
    }

//    @Override
//    public void onMapClick(LatLng latLng) {
//
//    }

//    /**
//     * 初始化地图
//     */
//    private void initMap() {
//        if(aMap==null) {
//            aMap = mMapView.getMap();
//        }
//        myLocationStyle = new MyLocationStyle();  //初始化定位蓝点样式类
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
//        myLocationStyle.interval(10000);//设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
////        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.bicycle));
//        aMap.setMyLocationStyle(myLocationStyle);  //设置定位蓝点的Style
//        aMap.setMyLocationEnabled(true);
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
//        // aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景地图
//
////        aMap.setOnMapClickListener(this);
////        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
//    }

//    /**
//     * 初始化定位到当前位置
//     */
//    private void initMyLocation() {
//        mLocationListener=new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//                aMapLocation.getLatitude();//获取纬度
//                aMapLocation.getLongitude();//获取经度
//                aMapLocation.getLocationType(); //获取定位来源
//                StringBuffer buffer = new StringBuffer();
//                buffer.append("纬度"+aMapLocation.getLatitude()+"经度"+aMapLocation.getLongitude()+"定位来源"+aMapLocation.getLocationType()
//                );
//                Toast.makeText(getActivity(),buffer.toString(),Toast.LENGTH_LONG).show();
////                latitud=aMapLocation.getLatitude();
////                longitude=aMapLocation.getLongitude();
//            }
//        };
//        //初始化定位
//        mLocationClient = new AMapLocationClient(getActivity());
//        //设置定位回调监听
//        mLocationClient.setLocationListener(mLocationListener);
//        //初始化AMapLocationClientOption对象
//        mLocationOption = new AMapLocationClientOption();
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); //设置定位模式为高精度模式
//        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
//        if(mLocationClient!=null){
//            mLocationClient.setLocationOption(mLocationOption);
//            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//            mLocationClient.stopLocation();
//            mLocationClient.startLocation();
//        }
//        mLocationClient.startLocation();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
//        mMapView.onDestroy();
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
//        mMapView.onResume();
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
//        mMapView.onPause();
//    }

}








