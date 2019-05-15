package com.example.duand.qiqu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duand.qiqu.JavaBean.Match;
import com.example.duand.qiqu.R;

import java.util.LinkedList;

public class MatchAdapter extends BaseAdapter {
    private LinkedList<Match> mData;
    private Context mcontext;
    private ImageView match_icon;
    private TextView match_name;
    private TextView match_time;
    private TextView match_address;

    public MatchAdapter(LinkedList<Match> Data, Context context){
        this.mData = Data;
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
        convertView = LayoutInflater.from(mcontext).inflate(R.layout.match_list_item,parent,false);
        match_icon = (ImageView)convertView.findViewById(R.id.match_icon);
        match_name = (TextView)convertView.findViewById(R.id.match_name);
        match_time = (TextView)convertView.findViewById(R.id.match_time);
        match_address = (TextView)convertView.findViewById(R.id.match_address);
        match_icon.setImageResource(mData.get(position).getMatchIcon());
        match_name.setText(mData.get(position).getMatchName());
        match_time.setText(mData.get(position).getMatchTime());
        match_address.setText(mData.get(position).getMatchAddress());
        return convertView;
    }
}
