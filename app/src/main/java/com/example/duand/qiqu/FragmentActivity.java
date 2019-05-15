package com.example.duand.qiqu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.duand.qiqu.UIFragment.MatchFragment;
import com.example.duand.qiqu.UIFragment.MineFragment;
import com.example.duand.qiqu.UIFragment.RouteFragment;
import com.example.duand.qiqu.UIFragment.SocietyFragment;
import com.example.duand.qiqu.UIFragment.TimeFragment;


public class FragmentActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private BottomNavigationBar bottomNavigationBar;

    private RouteFragment routeFragment;
    private MatchFragment matchFragment;
    private TimeFragment timeFragment;
    private SocietyFragment societyFragment;
    private MineFragment mineFragment;
    private int user_id;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Bundle bundle = this.getIntent().getExtras();
        user_id = bundle.getInt("user_id");
        Log.e("check", "getResult: "+ user_id);
        initFragment();
    }

    private void initFragment() {
        bottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);   //设置固定模式
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);   //设置静态样式

        bottomNavigationBar.setInActiveColor("#c5c4c4");   //设置未选中图标颜色
        bottomNavigationBar.setActiveColor("#ce4141"); //选中颜色
        bottomNavigationBar                       //添加导航按钮
                .addItem(new BottomNavigationItem(R.drawable.route,"路线"))
                .addItem(new BottomNavigationItem(R.drawable.match,"赛事"))
                .addItem(new BottomNavigationItem(R.drawable.time,"时间"))
                .addItem(new BottomNavigationItem(R.drawable.society,"骑圈"))
                .addItem(new BottomNavigationItem(R.drawable.mine,"我的"))
                .setFirstSelectedPosition(0)
                .initialise();
        
        bottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
    }

    private void setDefaultFragment() {               //设置默认fragment页面
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        routeFragment = new RouteFragment();
        SendUserId(routeFragment);
        transaction.replace(R.id.frame_lay, routeFragment);
        transaction.commit();
    }

    private void SendUserId(Fragment fragment) {
        Bundle bundle1 = new Bundle();
        bundle1.putInt("user_id",user_id);
        fragment.setArguments(bundle1);
    }

    @Override
    public void onTabSelected(int position) {
        Log.d("FragmentActivity", "onTabSelected() called with: " + "position = [" + position + "]");

        FragmentManager fm = this.getSupportFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();           //事务开启
        switch (position){
            case 0:
                if(routeFragment == null){
                    routeFragment = new RouteFragment();
                    SendUserId(routeFragment);
                }
                transaction.replace(R.id.frame_lay,routeFragment);
                break;
            case 1:
                if(matchFragment == null){
                    matchFragment = new MatchFragment();
                }
                transaction.replace(R.id.frame_lay,matchFragment);
                break;
            case 2:
                if(timeFragment == null){
                    timeFragment = TimeFragment.newInstance("时间");
                }
                transaction.replace(R.id.frame_lay,timeFragment);
                break;
            case 3:
                if(societyFragment == null){
                    societyFragment = new SocietyFragment();
                    SendUserId(societyFragment);
                }
                transaction.replace(R.id.frame_lay,societyFragment);
                break;
            case 4:
                if(mineFragment == null){
                    mineFragment = new MineFragment();
                    SendUserId(mineFragment);
                }
                transaction.replace(R.id.frame_lay,mineFragment);
                break;
            default:
                if (routeFragment == null){
                    routeFragment = new RouteFragment();
                }
                transaction.replace(R.id.frame_lay,routeFragment);
                break;
        }
        transaction.commit();        //事务提交

    }


    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
