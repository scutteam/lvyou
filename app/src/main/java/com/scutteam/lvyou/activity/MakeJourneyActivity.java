package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.activity.SimpleHUD.PairProgressHUD;
import com.scutteam.lvyou.adapter.ViewSpotShowAdapter;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.dialog.DialogListener;
import com.scutteam.lvyou.dialog.EditMemberNumDialog;
import com.scutteam.lvyou.dialog.SelectDayDialog;
import com.scutteam.lvyou.model.Guide;
import com.scutteam.lvyou.model.Hotel;
import com.scutteam.lvyou.model.Insurance;
import com.scutteam.lvyou.model.Meal;
import com.scutteam.lvyou.model.Recommendtrip;
import com.scutteam.lvyou.model.Vehicle;
import com.scutteam.lvyou.model.ViewSpot;
import com.scutteam.lvyou.util.calendarlistview.library.SimpleMonthAdapter;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MakeJourneyActivity extends Activity implements View.OnClickListener {

    private ImageView destinationImage = null;            //目的地展示图片，最上方展示图片
    private LinearLayout mLlTopLayout;
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
    private int minDay;                                   //最少游玩天数
    private int maxDay;                                   //最多游玩天数
    private RelativeLayout mj_stay_all;                   //住宿相关的视图
    private LinearLayout mj_stay_unchoosed;               //选择住宿的linearLayout
    private LinearLayout mj_stay_choosed;                 //选择住宿后显示linearlayout
    private RelativeLayout mj_stay;                       //住宿总的layout
    private TextView mj_stay_choosed_type;                //选择住宿的酒店类型
    private TextView mj_stay_choosed_price;               //选择住宿的酒店价格
    private ListView mj_plat_item_list;                   //选择游玩项目之后更新的游玩项目列表
    private ViewSpotShowAdapter showAdapter;
    private TextView mTvCalculate;
    private TextView mj_detail_food; //包餐
    private TextView mTvGuide;       //导游
    private TextView mTvInsurance;   //保险
    private TextView mj_detail_transport; //交通

    private SimpleMonthAdapter.CalendarDay beginDay = null;
    private SimpleMonthAdapter.CalendarDay returnDay = null;
    private Context mContext = null;
    private ArrayList<Hotel> hotelList = new ArrayList<Hotel>();
    private List<Insurance> insuranceList = new ArrayList<Insurance>();
    private List<Guide> guideList = new ArrayList<Guide>();
    private List<Vehicle> vehicleList = new ArrayList<Vehicle>();
    public ArrayList<ViewSpot> viewSpotList = new ArrayList<ViewSpot>();
    public ArrayList<ViewSpot> viewSpotSelectedList = new ArrayList<ViewSpot>();
    private List<Meal> mealList = new ArrayList<Meal>();
    private String address; //目的地地址
    private String short_intro;
    private String thumb_pic;
    private String top_pic;
    private String title;
    private String long_intro;
    private String local;
    private String label;
    private Long destination_id; //目的地的id
    private Double score;
    private int maxNum;
    private int minNum;
    private boolean is_hot;
    public int selectNum;

    private int memberNums = 6;                           //默认6人成团
    public static final int SBP_REQUEST_CODE = 999;       //onActivityResult中回调的code，对应选择出发地点（Select Begin Place）
    private static final int REFRESH_DATA_SUCCESS = 666666;
    private static final int CALCULATE_DATA_SUCCESS = 666667;
    private static final int LOAD_RECOMMEND_DATA_SUCCESS = 666668;
    private static final int CALCULATE_DATA_AND_READY_TO_SUBMIT = 666669;

    private LinearLayout mj_play_item;

    private TextView mj_price_per_person;
    private String startDate;
    private String endDate;
    private Hotel selectedHotel;
    private double average_price;
    private TextView mj_submit;
    public TextView mj_play_item_nums;
    private String begin_place;
    private long begin_place_id;
    private String contactName;
    private String contactTel;
    private String planId;
    private int limitNum;

    private int playDay;
    private boolean request_to_modify = false;

    public ArrayList<ViewSpot> recommendViewSpotList = new ArrayList<ViewSpot>();

//    private Boolean isChanged = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case REFRESH_DATA_SUCCESS:
                    if (!request_to_modify) {
                        refreshUi();
                    } else {
                        loadUi();
                    }
                    PairProgressHUD.dismiss();
                    break;
                case CALCULATE_DATA_SUCCESS:
                    mTvCalculate.setVisibility(View.GONE);
                    mj_price_per_person.setVisibility(View.VISIBLE);
                    mj_price_per_person.setText((int) average_price + "元");
                    break;
                case LOAD_RECOMMEND_DATA_SUCCESS:
                    refreshSelectRecommendUI();
                    break;
                case CALCULATE_DATA_AND_READY_TO_SUBMIT:
                    submitJourney();
                    break;
            }
        }
    };
    private String modifyPlace;
    private int modifyPeopleNum;
    private String modifyStartDate;
    private String modifyEndDate;
    private String modifyHotel;
    private long modifyHotelId;
    private long modifyPlaceId;
    private List<ViewSpot> modifyViewSpotList = new ArrayList<ViewSpot>();

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
        PairProgressHUD.showLoading(MakeJourneyActivity.this, "请稍候");

        request_to_modify = getIntent().getBooleanExtra("request_to_modify", false);
        modifyPlace = getIntent().getStringExtra("place");
        modifyPlaceId = getIntent().getLongExtra("placeId", 0L);
        modifyPeopleNum = getIntent().getIntExtra("peopleNum", minNum);
        modifyStartDate = getIntent().getStringExtra("startDate");
        modifyEndDate = getIntent().getStringExtra("endDate");
        modifyHotel = getIntent().getStringExtra("hotel");
        modifyHotelId = getIntent().getLongExtra("hotelId", 0L);
        contactName = getIntent().getStringExtra("contactName");
        contactTel = getIntent().getStringExtra("contactTel");
        planId = getIntent().getStringExtra("planId");
        modifyViewSpotList = (List<ViewSpot>) getIntent().getSerializableExtra("view_spot_list");

        destination_id = getIntent().getLongExtra("destination_id", 0L);

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
                    local = dataObject.optString("local");
                    long_intro = dataObject.optString("longIntro");
                    maxNum = dataObject.optInt("maxNum");
                    mealList = Meal.insertWithArray(dataObject.getJSONArray("mealList"));
                    minNum = dataObject.optInt("minNum");
                    minDay = dataObject.optInt("minDay");
                    maxDay = dataObject.optInt("maxDay");
                    score = dataObject.optDouble("score");
                    short_intro = dataObject.optString("shortIntro");
                    thumb_pic = dataObject.optString("thumbPic");
                    title = dataObject.optString("title");
                    top_pic = dataObject.optString("topPic");
                    limitNum = dataObject.optInt("limitNum");
                    vehicleList = Vehicle.insertWithArray(dataObject.getJSONArray("vehicleList"));
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

        selectBeginPlace = (TextView) findViewById(R.id.mj_select_begin_place);
        minusMemberNums = (TextView) findViewById(R.id.mj_member_minus);
        plusMemberNums = (TextView) findViewById(R.id.mj_member_plus);
        showMemberNums = (TextView) findViewById(R.id.mj_member_num);
        destinationName = (TextView) findViewById(R.id.mj_destination_name);
        destinationStar = (RatingBar) findViewById(R.id.mj_destination_star);
        destinationRatingNum = (TextView) findViewById(R.id.mj_destination_rate_num);
        destinationDetail = (TextView) findViewById(R.id.mj_destination_describe);
        mLlTopLayout = (LinearLayout) findViewById(R.id.ll_top_layout);
        mj_stay_all = (RelativeLayout) findViewById(R.id.mj_stay);
        mj_stay_unchoosed = (LinearLayout) findViewById(R.id.mj_stay_unchoosed);
        mj_stay_choosed = (LinearLayout) findViewById(R.id.mj_stay_choosed);
        mj_stay_choosed_type = (TextView) findViewById(R.id.mj_stay_choosed_type);
        mj_stay_choosed_price = (TextView) findViewById(R.id.mj_stay_choosed_price);
        tvBeginDay = (TextView) findViewById(R.id.mj_begin_day);
        tvReturnDay = (TextView) findViewById(R.id.mj_return_day);
        destinationImage = (ImageView) findViewById(R.id.mj_image);
        mj_stay = (RelativeLayout) findViewById(R.id.mj_stay);
        mj_play_item = (LinearLayout) findViewById(R.id.mj_play_item);
        mTvCalculate = (TextView) findViewById(R.id.tv_calculate);
        mj_detail_food = (TextView) findViewById(R.id.mj_detail_food);
        mTvGuide = (TextView) findViewById(R.id.tv_guide);
        mTvInsurance = (TextView) findViewById(R.id.tv_insurance);
        mj_price_per_person = (TextView) findViewById(R.id.mj_price_per_person);
        mj_submit = (TextView) findViewById(R.id.mj_submit);
        mj_detail_transport = (TextView) findViewById(R.id.mj_detail_transport);
        mj_play_item_nums = (TextView) findViewById(R.id.mj_play_item_nums);

        ViewGroup.LayoutParams params = destinationImage.getLayoutParams();   //设置ImageView横高比例为4:3
        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = (params.width * Constants.Config.IMAGE_HEIGHT) / Constants.Config.IMAGE_WIDTH;
        destinationImage.setLayoutParams(params);

        mj_plat_item_list = (ListView) findViewById(R.id.mj_plat_item_list);
    }

    public void loadUi() {
        String[] TopPicStringList = top_pic.split(";");
        if (TopPicStringList != null && TopPicStringList.length > 0) {
            ImageLoader.getInstance().displayImage(Constants.IMAGE_URL + TopPicStringList[0], destinationImage);
        }

        //出发地方
        begin_place = modifyPlace;
        begin_place_id = modifyPlaceId;
        if (null != begin_place) {
            selectBeginPlace.setText(begin_place);
        }
        //上面的ui
        destinationName.setText(title);
        destinationStar.setRating(Float.parseFloat(score.toString()));
        destinationRatingNum.setText(score.toString());
        destinationDetail.setText(short_intro);
        //人员
        memberNums = modifyPeopleNum;
        showMemberNums.setText(modifyPeopleNum + "人成团");
        //出发日期 返程日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String modifyStartDateString = sdf.format(new Date(Long.parseLong(modifyStartDate)));
        String modifyEndDateString = sdf.format(new Date(Long.parseLong(modifyEndDate)));
        String[] startDateArray = modifyStartDateString.split("-");
        tvBeginDay.setText(startDateArray[0] + "年" + getTrueMonthOrDay(startDateArray[1]) + "月" + getTrueMonthOrDay(startDateArray[2]) + "日");
        String[] endDateArray = modifyEndDateString.split("-");
        tvReturnDay.setText(endDateArray[0] + "年" + getTrueMonthOrDay(endDateArray[1]) + "月" + getTrueMonthOrDay(endDateArray[2]) + "日");
        startDate = modifyStartDateString;
        endDate = modifyEndDateString;

        //更新包餐(yes) 交通 导游(yes) 保险(yes)
        mj_detail_food.setText(mealList.get(0).intro);
        mTvGuide.setText(guideList.get(0).level_name);
        mTvInsurance.setText(insuranceList.get(0).insurance_type + "\n" + insuranceList.get(1).insurance_type);
        mj_detail_transport.setText(vehicleList.get(0).vehicle_name);

        //酒店
        for (int i = 0; i < hotelList.size(); i++) {
            hotelList.get(i).is_select = 0;
        }
        if (modifyHotelId != 0L) {
            for (int i = 0; i < hotelList.size(); i++) {
                if (hotelList.get(i).hotel_id == modifyHotelId) {
                    refreshHotelUI(hotelList.get(i));
                    break;
                }
            }
        } else {
            refreshHotelUI();//真正没走过这里
        }

        for (int i = 0; i < modifyViewSpotList.size(); i++) {
            for (int j = 0; j < viewSpotList.size(); j++) {
                long view_spot_id = viewSpotList.get(j).view_spot_id;
                if (modifyViewSpotList.get(i).view_spot_id == view_spot_id) {
                    viewSpotList.get(j).is_select = 1;
                    viewSpotSelectedList.add(viewSpotList.get(j));
                }
            }
        }

        refreshViewSpotUI();
    }

    public String getTrueMonthOrDay(String string) {
        return (Integer.parseInt(string) >= 10) ? string : string.substring(1, 2);
    }

    public void refreshUi() {
        String[] TopPicStringList = top_pic.split(";");
        if (TopPicStringList != null && TopPicStringList.length > 0) {
            ImageLoader.getInstance().displayImage(Constants.IMAGE_URL + TopPicStringList[0], destinationImage);
        }

        destinationName.setText(title);
        destinationStar.setRating(Float.parseFloat(score.toString()));
        destinationRatingNum.setText(score.toString());
        destinationDetail.setText(short_intro);
        showMemberNums.setText(minNum + "人成团");
        memberNums = minNum;

        if (minDay == 1 && maxDay == 1) {
            mj_stay_all.setVisibility(View.GONE);
        } else {
            mj_stay_all.setVisibility(View.VISIBLE);
        }

        //更新包餐(yes) 交通 导游(yes) 保险(yes)
        mj_detail_food.setText(mealList.get(0).intro);
        mTvGuide.setText(guideList.get(0).level_name);
        mTvInsurance.setText(insuranceList.get(0).insurance_type + "\n" + insuranceList.get(1).insurance_type);
        mj_detail_transport.setText(vehicleList.get(0).vehicle_name);
    }

    private void initListener() {
        findViewById(R.id.left_icon).setOnClickListener(this);
        minusMemberNums.setOnClickListener(this);
        plusMemberNums.setOnClickListener(this);
        selectBeginPlace.setOnClickListener(this);
        showMemberNums.setOnClickListener(this);
        mj_stay.setOnClickListener(this);
        mLlTopLayout.setOnClickListener(this);
        tvBeginDay.setOnClickListener(this);
        tvReturnDay.setOnClickListener(this);
        mj_play_item.setOnClickListener(this);
        mTvCalculate.setOnClickListener(this);
        mj_submit.setOnClickListener(this);
        destinationImage.setOnClickListener(this);
    }

    public void changeCalculateUI() {
        if (mTvCalculate.getVisibility() == View.GONE) {
            mTvCalculate.setVisibility(View.VISIBLE);
            mj_price_per_person.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.left_icon:
                backToMainActivity();
                break;
            case R.id.mj_select_begin_place:
                changeCalculateUI();
                intent = new Intent(MakeJourneyActivity.this, SelectBeginPlaceActivity.class);
                startActivityForResult(intent, MakeJourneyActivity.SBP_REQUEST_CODE);
                break;
            case R.id.mj_member_minus:
                changeCalculateUI();
                if (memberNums > minNum) {
                    memberNums -= 1;
                    showMemberNums.setText(memberNums + "人成团");
                } else {
                    Toast.makeText(mContext, "至少" + minNum + "人成团", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mj_member_plus:
                changeCalculateUI();
                if (memberNums < maxNum) {
                    memberNums += 1;
                    showMemberNums.setText(memberNums + "人成团");
                } else {
                    Toast.makeText(mContext, "至多" + maxNum + "人成团", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mj_member_num:
                EditMemberNumDialog editMemberNumDialog = new EditMemberNumDialog(mContext);
                editMemberNumDialog.setMaxNum(maxNum);
                editMemberNumDialog.setMinNum(minNum);
                editMemberNumDialog.setDialogListener(new DialogListener() {
                    @Override
                    public void refreshActivity(Object data) {
                        String memberNumString = (String) data;
                        memberNums = Integer.parseInt(memberNumString);
                        showMemberNums.setText(memberNumString + "人成团");

                        changeCalculateUI();
                    }
                });
                editMemberNumDialog.show();
                break;
            case R.id.ll_top_layout:
            case R.id.mj_image:
                intent = new Intent(MakeJourneyActivity.this, DestinationDetailActivity.class);
                ArrayList<String> viewSpotStringList = new ArrayList<String>();
                for (int i = 0; i < viewSpotList.size(); i++) {
                    viewSpotStringList.add(viewSpotList.get(i).cover_pic);
                }
                intent.putExtra("top_pic", top_pic);
                intent.putExtra("long_intro", long_intro);
                intent.putExtra("destination_id", destination_id);
                startActivity(intent);
                break;
            case R.id.mj_begin_day:
            case R.id.mj_return_day:
                changeCalculateUI();
                SelectDayDialog dialog = new SelectDayDialog(mContext, new DialogListener() {
                    @Override
                    public void refreshActivity(Object data) {
                        SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays
                                = (SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay>) data;
                        tvBeginDay.setText(selectedDays.getFirst().toString());
                        tvReturnDay.setText(selectedDays.getLast().toString());
                        beginDay = selectedDays.getFirst();
                        returnDay = selectedDays.getLast();
                        beginDay.month += 1;    //矫正，具体看源码，源码中月份从0开始
                        returnDay.month += 1;

                        startDate = getYYYYMMDDStringFrom(selectedDays, true);
                        endDate = getYYYYMMDDStringFrom(selectedDays, false);
                    }
                });
                dialog.setMaxDay(maxDay);
                dialog.setMinDay(minDay);
                dialog.setIsOneDay(minDay == 1 && maxDay == 1);
                dialog.show();
                break;
            case R.id.mj_stay:
                changeCalculateUI();
                intent = new Intent();
                intent.putExtra("hotel", (Serializable) hotelList);
                intent.setClass(MakeJourneyActivity.this, SelectStayActivity.class);
                startActivityForResult(intent, Constants.REQUEST_SELECT_STAY);
                break;
            case R.id.mj_play_item:
                mj_play_item_nums.setText("请选择游玩项目");
                changeCalculateUI();
                intent = new Intent();
                intent.putExtra("selectedViewSpot", (Serializable) viewSpotSelectedList);
                intent.putExtra("viewSpot", (Serializable) viewSpotList);
                intent.putExtra("id", destination_id);
                intent.putExtra("top_pic", top_pic);
                intent.putExtra("limitNum", limitNum);
                intent.putExtra("selectNum", selectNum);
                intent.setClass(MakeJourneyActivity.this, ViewSpotActivity.class);
                startActivityForResult(intent, Constants.REQUEST_SELECT_VIEW_SPOT);
                break;
            case R.id.tv_calculate:
                if (null == begin_place || "".equals(begin_place)) {
                    Toast.makeText(MakeJourneyActivity.this, "未选择出发地点", Toast.LENGTH_SHORT).show();
                } else if (startDate == null || endDate == null) {
                    Toast.makeText(MakeJourneyActivity.this, "未选择出发日期", Toast.LENGTH_SHORT).show();
                } else if (!(minDay == 1 && maxDay == 1) && selectedHotel == null) {
                    Toast.makeText(MakeJourneyActivity.this, "未选择住宿", Toast.LENGTH_SHORT).show();
                } else if (viewSpotSelectedList.size() == 0) {
                    Toast.makeText(MakeJourneyActivity.this, "未选择游玩项目", Toast.LENGTH_SHORT).show();
                } else {
                    calculatePrice();
                }
                break;
            case R.id.mj_submit:
                if (null == begin_place || "".equals(begin_place)) {
                    Toast.makeText(MakeJourneyActivity.this, "未选择出发地点", Toast.LENGTH_SHORT).show();
                } else if (startDate == null || endDate == null) {
                    Toast.makeText(MakeJourneyActivity.this, "未选择出发日期", Toast.LENGTH_SHORT).show();
                } else if (!(minDay == 1 && maxDay == 1) && selectedHotel == null) {
                    Toast.makeText(MakeJourneyActivity.this, "未选择住宿", Toast.LENGTH_SHORT).show();
                } else if (viewSpotSelectedList.size() == 0) {
                    Toast.makeText(MakeJourneyActivity.this, "未选择游玩项目", Toast.LENGTH_SHORT).show();
                } else {
                    if (LvYouApplication.getSessionId() != null) {
                        if (average_price > 0) {
                            submitJourney();
                        } else {
                            calculatePriceAndSubmitJourney();
                        }
                    } else {
                        Toast.makeText(MakeJourneyActivity.this, "未登录,正在跳转至登录界面", Toast.LENGTH_SHORT).show();

                        Intent intent1 = new Intent();
                        intent1.setClass(MakeJourneyActivity.this, LoginActivity.class);
                        intent1.putExtra("is_request_login", true);
                        startActivityForResult(intent1, Constants.REQUEST_LOGIN);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void submitJourney() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("plan.destId", destination_id);
        params.put("plan.placeId", begin_place_id);
        if (minDay == 1 && maxDay == 1) {
            params.put("plan.hotelId", 0);
        } else {
            params.put("plan.hotelId", selectedHotel.hotel_id);
        }
        params.put("plan.guideId", guideList.get(0).guide_id);
        params.put("plan.mealId", mealList.get(0).meal_id);
        params.put("plan.vehicleId", vehicleList.get(0).vehicle_id);
        params.put("plan.peopleNum", memberNums);
        params.put("plan.startDate", startDate);
        params.put("plan.endDate", endDate);
        params.put("plan.unitPrice", average_price);
        if (request_to_modify) {
            params.put("plan.id", planId);
        }
        String view_spot_string = "";
        for (int i = 0; i < viewSpotSelectedList.size(); i++) {
            if (i == viewSpotSelectedList.size() - 1) {
                view_spot_string += viewSpotSelectedList.get(i).view_spot_id;
            } else {
                view_spot_string += viewSpotSelectedList.get(i).view_spot_id + ",";
            }
        }
        params.put("plan.vsIds", view_spot_string);
        client.post(MakeJourneyActivity.this, Constants.URL + "user/trip.save_plan.do", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("submit journey", response.toString());

                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        Toast.makeText(MakeJourneyActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MakeJourneyActivity.this, MainActivity.class);
                        intent.putExtra("destination_fragment", MainActivity.FRAGMENT_PLAN);
                        LvYouApplication.setType(MainActivity.FRAGMENT_PLAN);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MakeJourneyActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
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

    public void calculatePrice() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("total.placeId", begin_place_id);
        params.put("total.destId", destination_id);
        if (minDay == 1 && maxDay == 1) {
            params.put("total.hotelId", 0);
        } else {
            params.put("total.hotelId", selectedHotel.hotel_id);
        }
        params.put("total.guideId", guideList.get(0).guide_id);
        params.put("total.mealId", mealList.get(0).meal_id);
        params.put("total.vehicleId", vehicleList.get(0).vehicle_id);
        params.put("total.peopleNum", memberNums);
        params.put("total.startDate", startDate);
        params.put("total.endDate", endDate);
        String view_spot_string = "";
        for (int i = 0; i < viewSpotSelectedList.size(); i++) {
            if (i == viewSpotSelectedList.size() - 1) {
                view_spot_string += viewSpotSelectedList.get(i).view_spot_id;
            } else {
                view_spot_string += viewSpotSelectedList.get(i).view_spot_id + ",";
            }
        }
        params.put("total.vsIds", view_spot_string);

        client.post(MakeJourneyActivity.this, Constants.URL + "main/dest.total_price.do", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        average_price = response.getDouble("data");

                        handler.sendEmptyMessage(CALCULATE_DATA_SUCCESS);
                    } else {
                        Toast.makeText(MakeJourneyActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
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

    public String getYYYYMMDDStringFrom(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays, boolean isStart) {
        String month = "";
        String day = "";
        if (isStart) {
            if (selectedDays.getFirst().month < 10) {
                month = "0" + selectedDays.getFirst().month;
            } else {
                month = selectedDays.getFirst().month + "";
            }
            if (selectedDays.getFirst().day < 10) {
                day = "0" + selectedDays.getFirst().day;
            } else {
                day = selectedDays.getFirst().day + "";
            }
            return selectedDays.getFirst().year + "-" + month + "-" + day;
        } else {
            if (selectedDays.getLast().month < 10) {
                month = "0" + selectedDays.getLast().month;
            } else {
                month = selectedDays.getLast().month + "";
            }
            if (selectedDays.getLast().day < 10) {
                day = "0" + selectedDays.getLast().day;
            } else {
                day = selectedDays.getLast().day + "";
            }
            return selectedDays.getLast().year + "-" + month + "-" + day;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case MakeJourneyActivity.SBP_REQUEST_CODE:
                if (null != data) {
                    begin_place = data.getStringExtra("begin_place");
                    begin_place_id = data.getLongExtra("begin_place_id", 0L);
                    if (null != begin_place) {
                        selectBeginPlace.setText(begin_place);
                    }
                }
                break;
            default:
                break;
        }

        switch (resultCode) {
            case Constants.RESULT_SELECT_STAY:
                Long id = data.getLongExtra("select_hotel", 0L);
                for (int i = 0; i < hotelList.size(); i++) {
                    hotelList.get(i).is_select = 0;
                }
                if (!id.equals(0L)) {
                    for (int i = 0; i < hotelList.size(); i++) {
                        if (hotelList.get(i).hotel_id.equals(id)) {
                            refreshHotelUI(hotelList.get(i));
                            break;
                        }
                    }
                } else {
                    refreshHotelUI();//真正没走过这里
                }
                break;
            case Constants.RESULT_SELECT_RECOMMEND_TRIP:
                Recommendtrip trip = (Recommendtrip) data.getSerializableExtra("trip");
                if (trip != null) {
                    recommendViewSpotList.clear();

                    playDay = trip.day_num;

                    getRecommendViewSpotData(trip.trip_id);
                }

                break;
            case Constants.RESULT_SELECT_VIEW_SPOT:
                viewSpotSelectedList = (ArrayList<ViewSpot>) data.getSerializableExtra("selected_view_spot_list");
                for (int i = 0; i < viewSpotList.size(); i++) {
                    viewSpotList.get(i).is_select = 0;
                }
                for (int i = 0; i < viewSpotSelectedList.size(); i++) {
                    for (int j = 0; j < viewSpotList.size(); j++) {
                        long view_spot_id = viewSpotList.get(j).view_spot_id;
                        if (viewSpotSelectedList.get(i).view_spot_id == view_spot_id) {
                            viewSpotList.get(j).is_select = 1;
                        }
                    }
                }
                selectNum = viewSpotSelectedList.size();
                refreshViewSpotUI();
                break;
            case Constants.RESULT_LOGIN:
                calculatePriceAndSubmitJourney();
                break;
        }
    }

    public void calculatePriceAndSubmitJourney() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("total.placeId", begin_place_id);
        params.put("total.destId", destination_id);
        if (minDay == 1 && maxDay == 1) {
            params.put("total.hotelId", 0);
        } else {
            params.put("total.hotelId", selectedHotel.hotel_id);
        }
        params.put("total.guideId", guideList.get(0).guide_id);
        params.put("total.mealId", mealList.get(0).meal_id);
        params.put("total.vehicleId", vehicleList.get(0).vehicle_id);
        params.put("total.peopleNum", memberNums);
        params.put("total.startDate", startDate);
        params.put("total.endDate", endDate);
        String view_spot_string = "";
        for (int i = 0; i < viewSpotSelectedList.size(); i++) {
            if (i == viewSpotSelectedList.size() - 1) {
                view_spot_string += viewSpotSelectedList.get(i).view_spot_id;
            } else {
                view_spot_string += viewSpotSelectedList.get(i).view_spot_id + ",";
            }
        }
        params.put("total.vsIds", view_spot_string);

        client.post(MakeJourneyActivity.this, Constants.URL + "main/dest.total_price.do", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        average_price = response.getDouble("data");

                        handler.sendEmptyMessage(CALCULATE_DATA_AND_READY_TO_SUBMIT);
                    } else {
                        Toast.makeText(MakeJourneyActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
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

    public void getRecommendViewSpotData(Long trip_id) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("kw.tripId", trip_id);
        client.get(MakeJourneyActivity.this, Constants.URL + "main/recommendtrip.detail.json", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONArray dataArray = response.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);

                            JSONArray viewSpotArray = dataObject.getJSONArray("viewSpotList");
                            for (int j = 0; j < viewSpotArray.length(); j++) {
                                ViewSpot viewSpot1 = ViewSpot.insertOrReplace(viewSpotArray.getJSONObject(j));

                                recommendViewSpotList.add(viewSpot1);
                            }
                        }

                        handler.sendEmptyMessage(LOAD_RECOMMEND_DATA_SUCCESS);

                    } else {
                        Toast.makeText(MakeJourneyActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
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

    public void refreshSelectRecommendUI() {

        String startDateString = tvBeginDay.getText().toString();
        if (startDateString.equals("出发日期")) {

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DATE);

            String monthString = "";
            String dayString = "";
            if (month < 10) {
                monthString = "0" + month;
            } else {
                monthString = "" + month;
            }
            if (day < 10) {
                dayString = "0" + day;
            } else {
                dayString = "" + day;
            }
            tvBeginDay.setText(year + "年" + month + "月" + day + "日");
            startDate = year + "-" + monthString + "-" + dayString;

            cal.add(Calendar.DAY_OF_YEAR, playDay - 1);
            int returnYear = cal.get(Calendar.YEAR);
            int returnMonth = cal.get(Calendar.MONTH) + 1;
            int returnDay = cal.get(Calendar.DATE);
            tvReturnDay.setText(returnYear + "年" + returnMonth + "月" + returnDay + "日");

            Date date = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            endDate = sdf.format(date);
        } else {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = sdf.parse(startDate);//初始日期
                c.setTime(date);
                c.add(Calendar.DAY_OF_YEAR, playDay - 1);

                int returnYear = c.get(Calendar.YEAR);
                int returnMonth = c.get(Calendar.MONTH) + 1;
                int returnDay = c.get(Calendar.DATE);

                String monthString = "";
                String dayString = "";
                if (returnMonth < 10) {
                    monthString = "0" + returnMonth;
                } else {
                    monthString = "" + returnMonth;
                }
                if (returnDay < 10) {
                    dayString = "0" + returnDay;
                } else {
                    dayString = "" + returnDay;
                }

                tvReturnDay.setText(returnYear + "年" + returnMonth + "月" + returnDay + "日");

                endDate = returnYear + "-" + monthString + "-" + dayString;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < viewSpotList.size(); i++) {
            viewSpotList.get(i).is_select = 0;
        }

        viewSpotSelectedList.clear();

        for (int i = 0; i < recommendViewSpotList.size(); i++) {
            long id = recommendViewSpotList.get(i).view_spot_id;

            for (int j = 0; j < viewSpotList.size(); j++) {
                if (viewSpotList.get(j).view_spot_id == id) {
                    viewSpotList.get(j).is_select = 1;
                    recommendViewSpotList.remove(i);
                    recommendViewSpotList.add(i, viewSpotList.get(j));
                    break;
                }
            }
        }

        viewSpotSelectedList.addAll(recommendViewSpotList);

        for (int i = 0; i < viewSpotSelectedList.size(); i++) {
            viewSpotSelectedList.get(i).is_select = 1;
        }

        refreshViewSpotUI();
    }

    public void refreshViewSpotUI() {
        if (showAdapter == null) {
            showAdapter = new ViewSpotShowAdapter(MakeJourneyActivity.this, viewSpotSelectedList);
            mj_plat_item_list.setAdapter(showAdapter);
        } else {
            showAdapter.reloadWithList(viewSpotSelectedList);
        }

        mj_play_item_nums.setText("已选择" + viewSpotSelectedList.size() + "个游玩项目");

        //动态设置listview的高度
        int totalHeight = 0;
        for (int i = 0; i < showAdapter.getCount(); i++) {
            View listItem = showAdapter.getView(i, null, mj_plat_item_list);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = mj_plat_item_list.getLayoutParams();
        params.height = totalHeight + (mj_plat_item_list.getDividerHeight() * (showAdapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        mj_plat_item_list.setLayoutParams(params);
    }

    private void refreshHotelUI() {
        selectedHotel = null;
        mj_stay_unchoosed.setVisibility(View.VISIBLE);
        mj_stay_choosed.setVisibility(View.GONE);
    }

    private void refreshHotelUI(Hotel hotel) {
        selectedHotel = hotel;
        hotel.is_select = 1;
        mj_stay_unchoosed.setVisibility(View.GONE);
        mj_stay_choosed.setVisibility(View.VISIBLE);

        mj_stay_choosed_price.setText(hotel.price + "元/间");
        mj_stay_choosed_type.setText(hotel.level_name);
    }
    
    private void backToMainActivity(){
        Intent fromIntent = getIntent();
        boolean ifFromMyJourneyActivity = fromIntent.getBooleanExtra("come_from_my_journey",false);
        if(!ifFromMyJourneyActivity) {
            Intent intent = new Intent();
            intent.setClass(MakeJourneyActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        backToMainActivity();
    }
}
