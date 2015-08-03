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
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.dialog.DialogListener;
import com.scutteam.lvyou.dialog.SelectDayDialog;
import com.scutteam.lvyou.model.Destination;
import com.scutteam.lvyou.model.Guide;
import com.scutteam.lvyou.model.Hotel;
import com.scutteam.lvyou.model.Insurance;
import com.scutteam.lvyou.model.Meal;
import com.scutteam.lvyou.model.ViewSpot;
import com.scutteam.lvyou.util.calendarlistview.library.SimpleMonthAdapter;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MakeJourneyActivity extends Activity implements View.OnClickListener {
    final int imageWidthRate = 4;                          //固定图片的宽高比例为4:3
    final int imageHeightRate = 3;
    private int memberNums = 6;                           //默认6人成团
    public static final int SBP_REQUEST_CODE = 999;       //onActivityResult中回调的code，对应选择出发地点（Select Begin Place）

    private ImageView destinationImage = null;            //目的地展示图片，最上方展示图片
    private TextView destinationName = null;              //目的地名称
    private RatingBar destinationStar = null;             //目的地获得星数对应的Bar
    private TextView destinationRatingNum = null;         //目的地获得星数对应的数值
    private TextView destinationDetail = null;            //目的地的详细描述
    private TextView selectBeginPlace = null;             //选择出发地点
    private TextView minusMemberNums = null;              //减少一个团员
    private TextView plusMemberNums = null;               //增加一个团员
    private TextView showMemberNums = null;               //显示团员数量
    private TextView tvBeginDay = null;                   //出发日期
    private TextView tvReturnDay = null;                  //返回日期
    private SimpleMonthAdapter.CalendarDay beginDay = null;
    private SimpleMonthAdapter.CalendarDay returnDay = null;

    private Long destination_id; //目的地的id
    private Destination destination; //所选目的地
    private Context mContext = null;
    private List<Hotel> hotelList = new ArrayList<Hotel>();
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
        destination = Destination.findDestinationById(destination_id);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", destination_id);
        client.get(MakeJourneyActivity.this, Constants.URL + "main/dest.detail.json", params, new JsonHttpResponseHandler() {
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

        tvBeginDay = (TextView) findViewById(R.id.mj_begin_day);
        tvReturnDay = (TextView) findViewById(R.id.mj_return_day);
    }
    
    public void refreshUi() {
        ImageLoader.getInstance().displayImage(destination.cover_pic,destinationImage);
        destinationName.setText(destination.title);
        destinationStar.setRating(Float.parseFloat(destination.score.toString()));
        destinationRatingNum.setText(destination.score.toString());
        destinationDetail.setText(destination.short_intro);

    }

    private void initListener() {
        minusMemberNums.setOnClickListener(this);
        plusMemberNums.setOnClickListener(this);
        selectBeginPlace.setOnClickListener(this);
        mLlTopLayout.setOnClickListener(this);

        //出发日期跟返程日期
        View.OnClickListener selectDayClickedListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDayDialog dialog = new SelectDayDialog(mContext, new DialogListener() {
                    @Override
                    public void refreshActivity(Object data) {
                        SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays
                                = (SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay>)data;
                        tvBeginDay.setText(selectedDays.getFirst().toString());
                        tvReturnDay.setText(selectedDays.getLast().toString());
                        beginDay = selectedDays.getFirst();
                        returnDay = selectedDays.getLast();
                        beginDay.month += 1;    //矫正，具体看源码，源码中月份从0开始
                        returnDay.month += 1;
                    }
                });
                dialog.show();
            }
        };

        tvBeginDay.setOnClickListener(selectDayClickedListener);
        tvReturnDay.setOnClickListener(selectDayClickedListener);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.mj_select_begin_place:
                intent = new Intent(MakeJourneyActivity.this, SelectBeginPlaceActivity.class);
                startActivityForResult(intent, MakeJourneyActivity.SBP_REQUEST_CODE);
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
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case MakeJourneyActivity.SBP_REQUEST_CODE:
                String bp = intent.getStringExtra("begin_place");
                if(null != bp){
                    selectBeginPlace.setText(bp);
                }
                break;
            default:
                break;
        }
    }

}
