package com.example.duand.qiqu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duand.qiqu.JavaBean.Route;
import com.example.duand.qiqu.R;


import java.util.LinkedList;

public class RouteAdapter extends BaseAdapter {

    private LinkedList<Route> mData;
    private Context mcontext;
    private ImageView route_icon;
    private TextView route_name;
    private TextView route_detail;

    public RouteAdapter(LinkedList<Route> mData, Context context){
        this.mData = mData;
        this.mcontext = context;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mcontext).inflate(R.layout.route_list_item,parent,false);
        route_icon = (ImageView)convertView.findViewById(R.id.route_icon);
        route_name = (TextView)convertView.findViewById(R.id.route_name);
        route_detail = (TextView)convertView.findViewById(R.id.route_detail);
        route_icon.setImageResource(mData.get(position).getRouteIcon());
        route_name.setText(mData.get(position).getRouteName());
        route_detail.setText(mData.get(position).getRouteDetail());
        return convertView;
    }
}
