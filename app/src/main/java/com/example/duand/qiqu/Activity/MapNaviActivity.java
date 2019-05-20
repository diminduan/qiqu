package com.example.duand.qiqu.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.example.duand.qiqu.R;
import com.example.duand.qiqu.UIFragment.TimeFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MapNaviActivity extends Activity implements AMapNaviViewListener,AMapNaviListener{

    private AMapNaviView mAMapNaviView;   //导航地图view
    private AMapNavi mAMapNavi;     //导航实例

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_navi);
        mAMapNaviView = (AMapNaviView)findViewById(R.id.navi_map);
        mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNaviView.onCreate(savedInstanceState);
        initNaviView();
        //获取AMapNavi实例
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        //添加监听回调，用于处理算路成功
        mAMapNavi.addAMapNaviListener(this);
    }

    private void initNaviView() {
        com.amap.api.navi.AMapNaviViewOptions options = mAMapNaviView.getViewOptions();

        mAMapNaviView.setViewOptions(options);
    }
    /**
     * calculateRideRoute
     * 参数1：起点坐标
     * 参数2：终点坐标
     */
    private void startNavi(){
        NaviLatLng endNaviLatLng=new NaviLatLng();
        NaviLatLng startNaviLatLng=new NaviLatLng();
        startNaviLatLng.setLatitude(TimeFragment.latitud);
        startNaviLatLng.setLongitude(TimeFragment.longitude);
        endNaviLatLng.setLongitude(TimeFragment.getLatLng().longitude);
        endNaviLatLng.setLatitude(TimeFragment.getLatLng().latitude);
        mAMapNavi.calculateRideRoute(startNaviLatLng,endNaviLatLng);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mAMapNaviView.onDestroy();
        mAMapNavi.stopGPS(); // 关闭GPS
        mAMapNavi.stopNavi();//关闭导航
        mAMapNavi.destroy();
        mAMapNaviView.onDestroy();

    }

    @Override
    public void onInitNaviFailure() {

    }

    /**
     初始化地图成功，开始计算路径
     */
    @Override
    public void onInitNaviSuccess() {

        startNavi();
    }

    /*
计算路径成功返回毁掉方法
 */
    @Override
    public void onCalculateRouteSuccess(com.amap.api.navi.model.AMapCalcRouteResult aMapCalcRouteResult){
        mAMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    /**
     * 实时回调位置变更信息，获取地理位置
     * @param aMapNaviLocation
     */
    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

        final Handler handler=new Handler(){
            /**
             * 监听位置
             * @param msg
             */
            @Override
            public void handleMessage(Message msg){
                if(msg.what==0x1233){
                    Toast.makeText(MapNaviActivity.this,aMapNaviLocation.getCoord().toString(),Toast.LENGTH_LONG).show();
                }
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x1233);
            }
        },0,1200);
    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {
        mAMapNavi.stopNavi();
        mAMapNavi.stopGPS();
        mAMapNavi.destroy();
        onDestroy();
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }
}
