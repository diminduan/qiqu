package com.example.duand.qiqu.UIFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.duand.qiqu.Activity.LoginActivity;
import com.example.duand.qiqu.Activity.NewRouteActivity;
import com.example.duand.qiqu.Activity.ShowRouteActivity;
import com.example.duand.qiqu.Adapter.RouteAdapter;
import com.example.duand.qiqu.Constants;
import com.example.duand.qiqu.JavaBean.Route;
import com.example.duand.qiqu.R;
import com.example.duand.qiqu.Utils.GetHttpConnection;
import com.example.duand.qiqu.Utils.ListViewForScrollView;
import com.zaaach.citypicker.CityPickerActivity;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class RouteFragment extends Fragment implements View.OnClickListener{
    private SearchView route_search;
//    private List<Route> mData;
//    private List<Route> sData;
    private RouteAdapter routeAdapter;
    private SliderLayout mslider;
    private PagerIndicator indicator;
    private TextView location;
    private static final int REQUEST_CODE_PICK_CITY = 1;
    private ImageView add_route;
    private NiceSpinner style_spinner;
    private List<String> spinnerData;
    private TextView text_search;
    private int user_id;
    private ListViewForScrollView route_list;
    private SwipeRefreshLayout route_refresh;
    private Handler handler;
    private List<Route> mData;
    private String city;
    private String style_name;
    int pageNumber = 3;

//    public static RouteFragment newInstance(String s) {
//        RouteFragment routeFragment = new RouteFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.ARGS, s);
//        routeFragment.setArguments(bundle);
//        return routeFragment;
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        final View view = inflater.inflate(R.layout.route_fragment,container,false);

        route_search = (SearchView)view.findViewById(R.id.route_search);
        route_list = (ListViewForScrollView)view.findViewById(R.id.route_list);
        mslider = (SliderLayout)view.findViewById(R.id.route_slider);
        indicator = (PagerIndicator)view.findViewById(R.id.route_indicator);
        location = (TextView) view.findViewById(R.id.city);
        add_route = (ImageView)view.findViewById(R.id.add_route);
        style_spinner = (NiceSpinner)view.findViewById(R.id.style_spinner);
        route_refresh = (SwipeRefreshLayout)view.findViewById(R.id.route_refresh);

        location.setOnClickListener(this);
        add_route.setOnClickListener(this);



        //获取user_id
        Bundle bundle = this.getArguments();
        user_id = bundle.getInt("user_id");
        Log.e("check", "user_id:  "+user_id );

        ChangeSearchView();   //修改route_search的样式 && 设置spinner的样式
        initRoute();     //展示ListView


        return view;
    }

    /**
     * 控件监听事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.city:
                SelectCity();
                break;
            case R.id.add_route:
                AddRoute();
                break;
        }
    }
    /**
     * 修改route_search的样式 && 设置spinner的样式 &&定义位置信息
     * **/
    private void ChangeSearchView() {

        if (route_search == null){
            return ;
        }else{
            int text_id = route_search.getContext().getResources().getIdentifier(
                    "android:id/search_src_text",null,null);
            text_search = (TextView)route_search.findViewById(text_id); //获取到搜索框字体
            text_search.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);   //设置字体大小为16sp
            text_search.setTextColor(getResources().getColor(R.color.search_txt_color));   //设置字体颜色
            text_search.setHintTextColor(getResources().getColor(R.color.search_txt_hint_color));  //设置提示文字颜色
            //去除searchView的下划线
            int plate_id = route_search.getContext().getResources().getIdentifier(
                    "android:id/search_plate",null,null);    //获取到搜索框下划线
            View plate_search = (View)route_search.findViewById(plate_id);
            plate_search.setBackgroundColor(Color.TRANSPARENT);
        }

        spinnerData = new LinkedList<>(Arrays.asList("全部","市区","公路","乡村","山路","休闲"));
        style_spinner.attachDataSource(spinnerData);
        style_spinner.setBackgroundResource(R.drawable.spinner_border);
        style_spinner.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        style_spinner.setSelectedIndex(0);

        style_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                style_name = style_spinner.getSelectedItem().toString();
                Toast.makeText(getActivity(),style_name,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.e("check", "style_name="+style_name );

        //设置默认位置信息
        if (city != null){
            location.setText(Html.fromHtml("<u>"+ city +"</u>"));
        }else {
            location.setText(Html.fromHtml("<u>"+ "定位" +"</u>"));
        }
    }

    /**
     * 添加路线
     */
    private void AddRoute() {

        Intent intent = new Intent(getActivity(), NewRouteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("user_id",user_id);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    /**
     * 选择城市
     */
    private void SelectCity() {
        startActivityForResult(new Intent(getActivity(), CityPickerActivity.class),REQUEST_CODE_PICK_CITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);

                location.setText(Html.fromHtml("<u>"+ city +"</u>"));
            }
        }
    }

    /**
     *    下拉刷新路线信息
     */
    private void RefreshRoute() {



        route_refresh.setColorSchemeResources(R.color.start,R.color.middle,R.color.end);

        route_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Log.e(TAG, "check:"+pageNumber+" "+style_name+" "+city);

                if (style_name == null || city == null || style_name.equals("全部")){
                    startRefresh(pageNumber++,style_name,city);

                }else {

                    startRefresh(1,style_name,city);
                }


            }

            private void startRefresh(final int pageNumber, final String type, final String city) {

                new Handler().postDelayed(new Runnable() {

                  private String url;
                @Override
                  public void run() {
                      if (type == null || city == null || type.equals("全部")){
                          url = Constants.newUrl + "getRouteInfoBypage?" + "pageNumber=" + pageNumber ;

                          getData(mData,R.mipmap.route_1,false,false);
                      }else {
                             url = Constants.newUrl + "getRouteInfoByType?" +
                                  "pageNumber=" + pageNumber + "&type="+type + "&city="+city ;

                             getData(mData,R.mipmap.route_1,false,true);
                            }

                             Log.e("check", "Url="+ url);

                             new GetHttpConnection(url,handler).start();
                             route_refresh.setRefreshing(false);

                       }
                },2000);

            }

        });

    }

//    private void startRefresh(final int pageNumber, final String type, final String city) {
//        new Handler().postDelayed(new Runnable() {
//            private String url;
//            @Override
//            public void run() {
//                if (type !=null && city != null){
//                    url = Constants.newUrl + "getRouteInfoByType?" +
//                            "pageNumber=" + pageNumber + "&type="+type + "&city="+city ;
//                }else {
//                    url = Constants.newUrl + "getRouteInfoBypage?" + "pageNumber=" + pageNumber ;
//                }
//
//                Log.e("check", "Url="+ url);
//                getData(mData,R.mipmap.route_1,false);
//
//                new GetHttpConnection(url,handler).start();
//                route_refresh.setRefreshing(false);
//
//
//            }
//        },2000);
//    }

    /**
     * 显示ListView(加载10条数据)
     */
    private void initRoute() {

        mData = new LinkedList<Route>();
        for (int page = 1; page<3; page++){
            String route_url = Constants.newUrl + "getRouteInfoBypage?" + "pageNumber=" + page;
            Log.e("check", "Url="+route_url );

//            handler = new Handler(){
//                public void handleMessage(Message msg) {
//                    if (msg.what == 1){
//                        String response = msg.obj.toString();
//                        Log.e(TAG, "route_response "+response );
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
////                            Log.e("check", "init_route_array: "+jsonArray );
//                            for (int i = 0; i< jsonArray.length(); i++){
//                                JSONObject result = jsonArray.getJSONObject(i);
//                                String title = result.getString("boutiqueRouteTitle");
//                                String desc = result.getString("boutiqueRouteDescription");
//                                int route_id = result.getInt("boutiqueRouteId");
//                                String city = result.getString("boutiqueRouteCity");
//                                String type = result.getString("boutiqueRouteType");
//                                double distance = result.getDouble("boutiqueRouteDistance");
//
//                                Log.e("json", "title=" + title + " || desc=" + desc );
//                                mData.add(new Route(R.mipmap.test_1, title,desc,route_id,city,type,distance));
//                                routeAdapter.notifyDataSetChanged();
//
//                            }
//                            Log.e(TAG, "route_mData "+mData );
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            };
//            getData(mData,R.mipmap.test_1);
            getData(mData,R.mipmap.test_1,false,false);
            new GetHttpConnection(route_url,handler).start();
        }

        Log.e("check", "init_mData: "+ mData);
        routeAdapter = new RouteAdapter((LinkedList<Route>) mData, getActivity());

        route_list.setAdapter(routeAdapter);

        initSlider();   //获取轮播图数据

        RefreshRoute();  //刷新路线数据
    }

    private void parseJson() {

        route_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                        Log.e(TAG, "onItemClick: "+ );

//                                        Intent intent = new Intent(getActivity(), ShowRouteActivity.class);
//
//                                        startActivity(intent);
            }
        });

    }

    /**
     *    获取轮播图数据
     */
    private void initSlider() {

        List<Route> sData = new LinkedList<Route>();

        String Url = Constants.newUrl + "getTopFive";
        Log.e("check", "Url="+Url );

        getData(sData,R.mipmap.route_6,true,false);  //获取网络数据

        new GetHttpConnection(Url,handler).start();

        Log.e("check", "init_sData: "+ sData );

    }

    /**
     *   获取网络数据
     * @param list  数组
     * @param image 暂时的图片
     * @param flag 是否放入轮播图
     */
    private void getData(final List<Route> list, final int image, final boolean flag, final boolean position) {

        handler = new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    String response = msg.obj.toString();
                    Log.e(TAG, "response: "+response );
                    if (response.equals("[]")){
                        Toast.makeText(getActivity(),"未加载到更多数据", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i< jsonArray.length(); i++){
                            JSONObject result = jsonArray.getJSONObject(i);
                            String title = result.getString("boutiqueRouteTitle");
                            String desc = result.getString("boutiqueRouteDescription");
                            int route_id = result.getInt("boutiqueRouteId");
                            String city = result.getString("boutiqueRouteCity");
                            String type = result.getString("boutiqueRouteType");
                            double distance = result.getDouble("boutiqueRouteDistance");

//                            Log.e("json", "title=" + title + " || desc=" + desc );
                            if (position == true){
                                list.add(0,new Route(image, title,desc,route_id,city,type,distance));  //从首部插入数据
                            }else {
                                list.add(new Route(image, title,desc,route_id,city,type,distance));  //从尾部加载数据
                            }
                            routeAdapter.notifyDataSetChanged();
                        }
                        Log.e(TAG, "list: "+list );
                        if (flag == true){
                            showSlider(list);  //展示轮播图
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(),"未查询到信息", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

    }

    /**
     *  展示轮播图
     */
    private void showSlider(List<Route> list) {
        if ( list != null){
            for (Route route: list){
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(route.getRouteIcon())  //图片
                        .description(route.getRouteName())    //描述
                        .setScaleType(BaseSliderView.ScaleType.Fit)   //图片缩放类型
                        .setOnSliderClickListener(clickListener);   //图片点击事件
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle().putString("extra",route.getRouteName());  //传入参数
                mslider.addSlider(textSliderView);   //添加滑动页面
            }
        }

        mslider.setPresetTransformer(SliderLayout.Transformer.Accordion);      //设置过渡效果
        mslider.setCustomIndicator(indicator);    //设置自定义指示器
        mslider.setCustomAnimation(new DescriptionAnimation());     //设置动画
        mslider.setDuration(3500);    //设置持续时间
        mslider.addOnPageChangeListener(changeListener);   //图片改变事件
    }

    /**
     *   轮播图页面点击监听
     */
    private BaseSliderView.OnSliderClickListener clickListener = new BaseSliderView.OnSliderClickListener() {
        @Override
        public void onSliderClick(BaseSliderView slider) {
            Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",
                    Toast.LENGTH_SHORT).show();
        }
    };

    /**
     *    轮播图页面改变监听
     */

    private ViewPagerEx.OnPageChangeListener changeListener = new ViewPagerEx.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onStop(){
        mslider.stopAutoCycle();
        super.onStop();
        pageNumber = 3;
        route_refresh.setRefreshing(false);
        route_refresh.destroyDrawingCache();
        route_refresh.clearAnimation();
    }

//    @Override
//    public void onResume(){
//        super.onResume();
//    }

}
