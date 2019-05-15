package com.example.duand.qiqu.UIFragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.duand.qiqu.Adapter.MatchAdapter;
import com.example.duand.qiqu.Constants;
import com.example.duand.qiqu.JavaBean.Match;
import com.example.duand.qiqu.R;
import com.example.duand.qiqu.Utils.ListViewForScrollView;

import java.util.LinkedList;
import java.util.List;

public class MatchFragment extends Fragment{
    private ImageView menu;
    private SearchView match_search;
    private ImageView match_add;
    private SliderLayout match_slider;
    private PagerIndicator match_indicator;
    private TextView text_search;
    private List<Match> mData;
    private ListViewForScrollView match_list;

    private int user_id;

//    public static MatchFragment newInstance(String s){
//        MatchFragment matchFragment = new MatchFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.ARGS,s);
//        matchFragment.setArguments(bundle);
//        return matchFragment;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.match_fragment,container,false);
        menu = (ImageView)view.findViewById(R.id.menu_iv);
        match_search = (SearchView)view.findViewById(R.id.match_search);
        match_add = (ImageView)view.findViewById(R.id.match_add);
        match_slider = (SliderLayout)view.findViewById(R.id.match_slider);
        match_indicator = (PagerIndicator)view.findViewById(R.id.match_indicator);
        match_list = (ListViewForScrollView)view.findViewById(R.id.match_list);


        ChangeSearchView();
        showMatch();

        return view;
    }

    //修改搜索框样式
    private void ChangeSearchView() {
        if (match_search == null){
            return ;
        }else{
            int text_id = match_search.getContext().getResources().getIdentifier(
                    "android:id/search_src_text",null,null);
            //获取到搜索框字体
            text_search = (TextView)match_search.findViewById(text_id);
            text_search.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);   //设置字体大小为16sp
            text_search.setTextColor(getResources().getColor(R.color.search_txt_color));   //设置字体颜色
            text_search.setHintTextColor(getResources().getColor(R.color.search_txt_hint_color));  //设置提示文字颜色
            //去除searchView的下划线
            int plate_id = match_search.getContext().getResources().getIdentifier(
                    "android:id/search_plate",null,null);    //获取到搜索框下划线
            View plate_search = (View)match_search.findViewById(plate_id);
            plate_search.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void showMatch() {
        mData = new LinkedList<Match>();
        mData.add(new Match(R.mipmap.match_1,
                "环瑶湖骑行赛","时间：2018.10.1","地点：江西省南昌市"));
        mData.add(new Match(R.mipmap.match_2,
                "寻源赣拉练赛","时间：2018.11.1","地点：江西省南昌市"));
        mData.add(new Match(R.mipmap.match_3,
                "南昌大学生联合赛","时间：2018.12.1","地点：江西省南昌市"));
        mData.add(new Match(R.mipmap.match_4,
                "国际公路赛","时间：2019.4.25","地点：江苏省杭州市"));

        MatchAdapter matchAdapter = new MatchAdapter((LinkedList<Match>) mData,getActivity());
        match_list.setAdapter(matchAdapter);

        if (mData != null){
            for (Match match: mData){
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(match.getMatchIcon())  //图片
                        .description(match.getMatchName())    //描述
                        .setScaleType(BaseSliderView.ScaleType.Fit)   //图片缩放类型
                        .setOnSliderClickListener(clickListener);   //图片点击事件
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra",match.getMatchName());  //传入参数
                match_slider.addSlider(textSliderView);   //添加滑动页面

            }
        }
        match_slider.setPresetTransformer(SliderLayout.Transformer.Accordion);      //设置过渡效果
        match_slider.setCustomIndicator(match_indicator);    //设置自定义指示器
        match_slider.setCustomAnimation(new DescriptionAnimation());     //设置动画
        match_slider.setDuration(2500);    //设置持续时间
        match_slider.addOnPageChangeListener(changeListener);   //图片改变事件

    }
    //页面点击监听
    private BaseSliderView.OnSliderClickListener clickListener = new BaseSliderView.OnSliderClickListener() {
        @Override
        public void onSliderClick(BaseSliderView slider) {
            Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",
                    Toast.LENGTH_SHORT).show();
        }
    };

    //页面改变监听
    private ViewPagerEx.OnPageChangeListener changeListener = new ViewPagerEx.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d("qiqu", "Match Changed: " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onStop(){
        match_slider.stopAutoCycle();
        super.onStop();
    }

}
