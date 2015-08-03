package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.dialog.DialogListener;
import com.scutteam.lvyou.dialog.SelectDayDialog;
import com.scutteam.lvyou.model.Guide;
import com.scutteam.lvyou.model.Hotel;
import com.scutteam.lvyou.model.Insurance;
import com.scutteam.lvyou.model.Meal;
import com.scutteam.lvyou.model.ViewSpot;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MakeJourneyActivity extends Activity implements View.OnClickListener {
    final int imageWidthRate = 4;                          //固定图片的宽高比例为4:3
    final int imageHeightRate = 3;
    private int memberNums = 6;                           //默认6人成团

    private ImageView destinationImage = null;            //目的地展示图片，最上方展示图片
    private TextView destinationName = null;              //目的地名称
    private RatingBar destinationStar = null;             //目的地获得星数对应的Bar
    private TextView destinationRatingNum = null;         //目的地获得星数对应的数值
    private TextView destinationDetail = null;           //目的地的详细描述
    private TextView selectBeginPlace = null;             //选择出发地点
    private TextView minusMemberNums = null;              //减少一个团员
    private TextView plusMemberNums = null;               //增加一个团员
    private TextView showMemberNums = null;               //显示团员数量
    private TextView beginDay = null;                     //出发日期
    private TextView returnDay = null;                    //返回日期
    private LinearLayout mj_stay_unchoosed;//选择住宿的linearLayout
    private LinearLayout mj_stay_choosed; //选择住宿后显示linearlayout
    private TextView mj_stay_choosed_type; //选择住宿的酒店类型
    private TextView mj_stay_choosed_price;//选择住宿的酒店价格

    private Long destination_id; //目的地的id
    private Context mContext = null;
    private ArrayList<Hotel> hotelList = new ArrayList<Hotel>();
    private List<Insurance>insuranceList = new ArrayList<Insurance>();
    private List<Guide>guideList = new ArrayList<Guide>();
    private List<ViewSpot>viewSpotList = new ArrayList<ViewSpot>();
    private List<Meal>mealList = new ArrayList<Meal>();
    private String address; //目的地地址
    private String short_intro;
    private String thumb_pic;
    private String top_pic;
    private String title;
    private Double score;   
    private int minNum;
    private String long_intro;
    private String local;
    private String label;
    private int limit_num;
    private boolean is_hot;
    private int maxNum;
    
    private RelativeLayout mj_stay;//住宿总的layout
     
    private LinearLayout mLlTopLayout;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case REFRESH_DATA_SUCCESS:
                    refreshUi();
                    break;
            }
        }
    };
    private static final int REFRESH_DATA_SUCCESS = 666666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_journey);
        mContext = MakeJourneyActivity.this;
        initData();
        initView();
        initListener();
    }

    private void initData() {
        destination_id = getIntent().getLongExtra("destination_id", 0L);
//        destination = Destination.findDestinationById(destination_id);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id",destination_id);
        client.get(MakeJourneyActivity.this, Constants.URL + "main/dest.detail.json",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    
                    address = dataObject.optString("address");
                    guideList = Guide.insertWithArray(dataObject.getJSONArray("guideList"));
                    hotelList = Hotel.insertWithArray(dataObject.getJSONArray("hotelList"));
                    insuranceList = Insurance.insertWithArray(dataObject.getJSONArray("insuranceList"));
                    is_hot = dataObject.optBoolean("isHot");
                    label = dataObject.optString("label");
                    limit_num = dataObject.optInt("limitNum");
                    local = dataObject.optString("local");
                    long_intro = dataObject.optString("longIntro");
                    maxNum = dataObject.optInt("maxNum");
                    mealList = Meal.insertWithArray(dataObject.getJSONArray("mealList"));
                    minNum = dataObject.optInt("minNum");
                    score = dataObject.optDouble("score");
                    short_intro = dataObject.optString("shortIntro");
                    thumb_pic = dataObject.optString("thumbPic");
                    title = dataObject.optString("title");
                    top_pic = dataObject.optString("topPic");
                    viewSpotList = ViewSpot.insertWithArray(dataObject.getJSONArray("viewSpotList"));
                    
                    handler.sendEmptyMessage(REFRESH_DATA_SUCCESS);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 初始化界面
     */
    private void initView() {
        TextView title = (TextView) findViewById(R.id.center_text);
        title.setText("定制您的团队行程");

        destinationImage = (ImageView) findViewById(R.id.mj_image);
        ViewGroup.LayoutParams params = destinationImage.getLayoutParams();   //设置ImageView横高比例为4:3
        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = (params.width * imageHeightRate) / imageWidthRate;
        mj_stay = (RelativeLayout) findViewById(R.id.mj_stay);
        destinationImage.setLayoutParams(params);
        
        selectBeginPlace = (TextView)findViewById(R.id.mj_select_begin_place);
        minusMemberNums = (TextView)findViewById(R.id.mj_member_minus);
        plusMemberNums = (TextView)findViewById(R.id.mj_member_plus);
        showMemberNums = (TextView)findViewById(R.id.mj_member_num);
        destinationName = (TextView) findViewById(R.id.mj_destination_name);
        destinationStar = (RatingBar) findViewById(R.id.mj_destination_star);
        destinationRatingNum = (TextView) findViewById(R.id.mj_destination_rate_num);
        destinationDetail = (TextView) findViewById(R.id.mj_destination_describe);
        mLlTopLayout = (LinearLayout) findViewById(R.id.ll_top_layout);
        mj_stay_unchoosed = (LinearLayout) findViewById(R.id.mj_stay_unchoosed);
        mj_stay_choosed = (LinearLayout) findViewById(R.id.mj_stay_choosed);
        
        mj_stay_choosed_type = (TextView) findViewById(R.id.mj_stay_choosed_type);
        mj_stay_choosed_price = (TextView) findViewById(R.id.mj_stay_choosed_price);
        
        beginDay = (TextView) findViewById(R.id.mj_begin_day);
        returnDay = (TextView) findViewById(R.id.mj_return_day);
    }
    
    public void refreshUi() {
        String [] TopPicStringList = top_pic.split(";");
        if(TopPicStringList != null && TopPicStringList.length > 0) {
            ImageLoader.getInstance().displayImage(Constants.IMAGE_URL + TopPicStringList[0],destinationImage);
        }

        destinationName.setText(title);
        destinationStar.setRating(Float.parseFloat(score.toString()));
        destinationRatingNum.setText(score.toString());
        destinationDetail.setText(short_intro);

    }

    private void initListener() {
        minusMemberNums.setOnClickListener(this);
        plusMemberNums.setOnClickListener(this);
        selectBeginPlace.setOnClickListener(this);
        mj_stay.setOnClickListener(this);

        //出发日期跟返程日期
        View.OnClickListener selectDayClickedListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("#########");
                SelectDayDialog dialog = new SelectDayDialog(mContext, new DialogListener() {
                    @Override
                    public void refreshActivity(String text) {
                    }
                });
                dialog.show();
            }
        };

        beginDay.setOnClickListener(selectDayClickedListener);
        returnDay.setOnClickListener(selectDayClickedListener);
        mLlTopLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.mj_select_begin_place:
                intent = new Intent(MakeJourneyActivity.this, SelectBeginPlaceActivity.class);
                startActivity(intent);
                break;
            case R.id.mj_member_minus:
                memberNums -= 1;
                showMemberNums.setText(memberNums + "人成团");
                break;
            case R.id.mj_member_plus:
                memberNums += 1;
                showMemberNums.setText(memberNums + "人成团");
                break;
            case R.id.ll_top_layout:
                intent = new Intent(MakeJourneyActivity.this, DestinationDetailActivity.class);
                ArrayList<String>viewSpotStringList = new ArrayList<String>();
                for(int i = 0 ; i < viewSpotList.size(); i++) {
                    viewSpotStringList.add(viewSpotList.get(i).cover_pic);
                }
                intent.putStringArrayListExtra("viewSpotList",viewSpotStringList);
                intent.putExtra("long_intro",long_intro);
                intent.putExtra("destination_id",destination_id);
                startActivity(intent);
                break;
            case R.id.mj_stay:
                intent = new Intent();
                intent.putExtra("hotel",(Serializable)hotelList);
                intent.setClass(MakeJourneyActivity.this,SelectStayActivity.class);
//                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.REQUEST_SELECT_STAY);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        switch (resultCode) {
            case Constants.RESULT_SELECT_STAY:
                Long id = data.getLongExtra("select_hotel",0L);
                for(int i = 0 ; i < hotelList.size(); i++) {
                    hotelList.get(i).is_select = 0;
                }
                if(!id.equals(0L)) {
                    for(int i = 0 ; i < hotelList.size(); i++) {
                        if(hotelList.get(i).hotel_id.equals(id)) {
                            refreshHotelUI(hotelList.get(i));
                            break;
                        }
                    }
                } else {
                    refreshHotelUI();//真正没走过这里
                }
                break;
        }
    }
    
    public void refreshHotelUI() {
        mj_stay_unchoosed.setVisibility(View.VISIBLE);
        mj_stay_choosed.setVisibility(View.GONE);
    }
    
    public void refreshHotelUI(Hotel hotel) {
        hotel.is_select = 1;
        mj_stay_unchoosed.setVisibility(View.GONE);
        mj_stay_choosed.setVisibility(View.VISIBLE);

        mj_stay_choosed_price.setText(hotel.price+"元/天");
        mj_stay_choosed_type.setText(hotel.level_name);
    }
}
