package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.dialog.DialogListener;
import com.scutteam.lvyou.dialog.SubmitJourneyPlanDialog;
import com.scutteam.lvyou.model.PlanDetail;
import com.scutteam.lvyou.model.ViewSpot;
import com.scutteam.lvyou.util.PayResult;
import com.scutteam.lvyou.util.SignUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MyJourneyActivity extends Activity {

    private Context mContext;

    private ImageView ivHeadImage;      //最上面的图片
    private TextView tvTitle;           //行程的标题
    private TextView tvOrderNum;        //订单号
    private TextView tvCreateTime;      //创建时间
    private TextView tvBeginPlace;      //出发地点
    private TextView tvDate;            //出发时间
    private TextView tvMemberNum;       //成员人数
    private TextView tvPlayItemCount;   //游玩项目数
    private TextView tvUnitPrice;       //人均单价
    private TextView tvState;           //订单状态
    private TextView tvStateHint;       //订单状态对应的提示
    private TextView tvStateText1;      //提交订单
    private TextView tvStateText2;      //制作行程
    private TextView tvStateText3;      //签约出发
    private TextView tvBottomBtn1;      //底部第一个按钮
    private TextView tvBottomBtn2;      //
    private TextView tvBottomBtn3;      //
    private LinearLayout llBottomBtns;  //底部按钮的父视图


    private final int STATE_7 = 0;    //底部显示删除订单(state = 0)
    private final int STATE_1 = 1;    //底部显示提交行程计划跟修改行程计划(state = 1)
    private final int STATE_2 = 2;    //底部显示什么都没有(state = 2)
    private final int STATE_3 = 3;    //底部显示查看行程计划(state = 3)
    private final int STATE_4 = 4;    //底部显示查看行程方案，查看合同(state = 4)
    private final int STATE_8 = 5;    //底部显示查看行程方案，支付订金(state = 5)
    private final int STATE_5 = 6;    //底部显示获取保险，行程方案(state = 6)
    private final int STATE_6 = 7;    //底部显示评价反馈，查看保险，行程方案(state = 7)
    private final int STATE_9 = 8;    //底部显示评价反馈，查看保险，行程方案(state = 8)

    private final int REQUEST_WATCH_PLAN = 1;    //跳转到查看行程方案网页
    private final int REQUEST_WATCH_PROTOCAL = 2;//跳转到查看合同页面

    /* 商户PID **/
    public static final String PARTNER = "2088611807463433";
    /* 商户收款账号 **/
    public static final String SELLER = "huangzibin172@qq.com";
    /* 商户私钥，pkcs8格式 **/
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKND9721eDw0kOp4" +
            "JT1OOo6HnV04UYEgqJQFLVnxmxOc8+YLDXWGlJtnSDP/iUqxFBqODz+SBW/dZ2Sw" +
            "hsSltmgq9CoKtD2nEjJXgnVpeJmA8kcZZVVY7DhfQoz+9Rph3tu8ggGegpz/eJ7Y" +
            "niDY6vbJ1+zH4izLIawtsfLvD4VzAgMBAAECgYEAi6V/Kg0mIpjzuxm5AI8BFxh4" +
            "SVv6mvBMQQ+MkvpNIqGFHKcng/bw9VuKLq/Lxs9f2rbX5BAKBnziCwXSwDDdqy4U" +
            "aie1VAeHE1cvjZoPOa+oYQwHLGTzCk1jd6Oa3fPs/ERmnXThAfO5XC6IODffEvj1" +
            "IgbW8r/3dfY0IAqs+AECQQDNtoVZIL2xF+rOBEHR6qHOnbQ00edOdQ0BScBi3zrz" +
            "ltVBaAlLXCShEQSQ60m0Pb8aQ9ho3wx8NC0M4A3IJW/zAkEAyy0XimBoMOTuEjF+" +
            "MFukVD5bfGCbnhsIJT5NXOe42v7rCW2gXo5hee/Xefyu4aNpxmmKEYRqeqZfY9oW" +
            "Qqh0gQJAexwxX66ld3d90T9g+LS3k0R0FWxNRnnsh4nQthssV508EQGnFk+VqELb" +
            "/bKiEFknphtWRyS9fxNmpp7sAIzPGwJAfWPAyXeiFbrTaor889861Xr+fw4oPDU2" +
            "m7WNjS69GPqGF3V+qZx5cAWjF/fn/f0aBIeR3Cm6hSM0b61iW5UXAQJBAKUNiVLk" +
            "ZDq97tlSNPaoT3Kqv1LSrpc37SqpFKb0aD1b+1rkIljFW0UTO2Qz3bYObCFW4PIV" +
            "tmycKTVGKe7pw+Y=";
    /* 支付宝公钥 **/
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;


    private final int color1 = R.color.image_color;
    private final int color2 = R.color.ldrawer_color;
    private final int color3 = R.color.my_journey_state_delete;
    private final int color4 = R.color.main_text_color;
    private final int color5 = R.color.secondary_text_color;

    private boolean isFirstIn = true;
    private long hotelId;
    private String planLogicId;
    private PlanDetail planDetail;
    private boolean needRefresh = false;
    private long destId = 0L;
    private long placeId = 0L;
    private String contactName;
    private String contactTel;
    private String startDate;
    private String endDate;
    private String key;   //商品唯一的订单号
    private List<ViewSpot> viewSpotList = new ArrayList<ViewSpot>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case GET_VIEW_SPOT_DATA_SUCCESS:
                    startModifyJourney();
                    break;
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                   // Log.i("liujie", resultInfo);

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(MyJourneyActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        needRefresh = true;
                        initData();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(MyJourneyActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(MyJourneyActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();
                            needRefresh = true;

                        }
                    }
                    break;


            }
        }
    };
    private static final int GET_VIEW_SPOT_DATA_SUCCESS = 888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_journey);
        mContext = MyJourneyActivity.this;
        Intent intent = getIntent();
        if (null != intent) {
            planLogicId = intent.getStringExtra("plan_logic_id");
            Log.i("logic plan id", planLogicId);
        }
        findView();
        initData();
    }

    private void initData() {
        if (null != planLogicId) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("sessionid", LvYouApplication.getSessionId());
            params.put("id", planLogicId);
            client.get(Constants.URL + "/user/trip.detail.json", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("liujie", response.toString());
                    if (0 == response.optInt("code")) {
                        planDetail = PlanDetail.fromJson(response.optJSONObject("data"));
                        destId = planDetail.destination_logic_id;
                        initView(planDetail);
                    } else {
                        Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i("liujie", responseString.toString());
                    Toast.makeText(mContext, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void findView() {
        ivHeadImage = (ImageView) findViewById(R.id.my_journey_image);
        tvTitle = (TextView) findViewById(R.id.my_journey_title);
        tvOrderNum = (TextView) findViewById(R.id.my_journey_order_num);
        tvCreateTime = (TextView) findViewById(R.id.my_journey_create_time);
        tvBeginPlace = (TextView) findViewById(R.id.my_journey_begin_place);
        tvDate = (TextView) findViewById(R.id.my_journey_date);
        tvMemberNum = (TextView) findViewById(R.id.my_journey_member_num);
        tvPlayItemCount = (TextView) findViewById(R.id.my_journey_play_item_count);
        tvUnitPrice = (TextView) findViewById(R.id.my_journey_price);
        tvState = (TextView) findViewById(R.id.my_journey_order_state);
        tvStateHint = (TextView) findViewById(R.id.my_journey_order_state_hint);
        tvBottomBtn1 = (TextView) findViewById(R.id.my_journey_bottom_btn_1);
        tvBottomBtn2 = (TextView) findViewById(R.id.my_journey_bottom_btn_2);
        tvBottomBtn3 = (TextView) findViewById(R.id.my_journey_bottom_btn_3);
        llBottomBtns = (LinearLayout) findViewById(R.id.my_journey_bottom);
        tvStateText1 = (TextView) findViewById(R.id.my_journey_state_text_1);
        tvStateText2 = (TextView) findViewById(R.id.my_journey_state_text_2);
        tvStateText3 = (TextView) findViewById(R.id.my_journey_state_text_3);
    }

    private void initView(PlanDetail plan) {
        if (isFirstIn) {
            TextView title = (TextView) findViewById(R.id.center_text);
            title.setText("我的行程:" + plan.title);
            findViewById(R.id.left_icon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    backToMyPlanFragment();
                }
            });
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(Constants.IMAGE_URL + plan.head_image, ivHeadImage);
            tvTitle.setText(plan.title);
            tvOrderNum.setText("订单号:" + plan.order_num);
            tvCreateTime.setText("创建于:" + plan.create_time);
            if (null == plan.area)
                tvBeginPlace.setText(plan.place);
            else
                tvBeginPlace.setText(plan.place + "(" + plan.area + ")");
            tvDate.setText(plan.start_date + "-" + plan.end_date);
            tvMemberNum.setText(plan.member_num + "人");
            tvPlayItemCount.setText(plan.play_item_num + "");
            tvUnitPrice.setText((int) plan.unit_price + "元/人");
            isFirstIn = false;
        }
        tvState.setText(plan.state_text);
        tvStateHint.setText(plan.status_tips);


        refreshBottomButton(plan.state);

    }

    private void backToMyPlanFragment() {
        Intent intent = new Intent(MyJourneyActivity.this, MainActivity.class);
        intent.putExtra("destination_fragment", MainActivity.FRAGMENT_MAIN);
        intent.putExtra("needRefresh", needRefresh);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToMyPlanFragment();
    }

    private void refreshBottomButton(int state) {
        llBottomBtns.setVisibility(View.GONE);
        tvBottomBtn1.setVisibility(View.GONE);
        tvBottomBtn2.setVisibility(View.GONE);
        tvBottomBtn3.setVisibility(View.GONE);
        switch (state) {
            case STATE_1:
                llBottomBtns.setVisibility(View.VISIBLE);
                tvBottomBtn1.setVisibility(View.VISIBLE);
                tvBottomBtn2.setVisibility(View.VISIBLE);
                tvBottomBtn1.setText("提交行程方案");
                tvBottomBtn1.setTextColor(getResources().getColor(color2));
                tvBottomBtn1.setBackgroundColor(getResources().getColor(color1));
                tvBottomBtn2.setText("修改行程方案");
                tvBottomBtn2.setTextColor(getResources().getColor(color1));
                tvBottomBtn2.setBackgroundColor(getResources().getColor(color2));
                tvStateText1.setTextColor(getResources().getColor(color1));
                tvStateText2.setTextColor(getResources().getColor(color5));
                tvStateText3.setTextColor(getResources().getColor(color5));
                tvBottomBtn1.setOnClickListener(submitJourneyPlanListener);
                tvBottomBtn2.setOnClickListener(modifyJourneyPlanListener);
//                tvBottomBtn2.setOnClickListener(paySubscriptionListener);
                break;

            case STATE_2:
                tvStateText1.setTextColor(getResources().getColor(color4));
                tvStateText2.setTextColor(getResources().getColor(color1));
                tvStateText3.setTextColor(getResources().getColor(color5));
                break;

            case STATE_3:
                llBottomBtns.setVisibility(View.VISIBLE);
                tvBottomBtn1.setVisibility(View.VISIBLE);
                tvBottomBtn1.setText("查看行程方案");
                tvBottomBtn1.setTextColor(getResources().getColor(color2));
                tvBottomBtn1.setBackgroundColor(getResources().getColor(color1));
                tvStateText1.setTextColor(getResources().getColor(color4));
                tvStateText2.setTextColor(getResources().getColor(color1));
                tvStateText3.setTextColor(getResources().getColor(color5));
                tvBottomBtn1.setOnClickListener(watchJourneyPlanListener);
                break;

            case STATE_4:
                llBottomBtns.setVisibility(View.VISIBLE);
                tvBottomBtn1.setVisibility(View.VISIBLE);
                tvBottomBtn2.setVisibility(View.VISIBLE);
                tvBottomBtn1.setText("查看行程方案");
                tvBottomBtn1.setTextColor(getResources().getColor(color2));
                tvBottomBtn1.setBackgroundColor(getResources().getColor(color1));
                tvBottomBtn2.setText("查看合同");
                tvBottomBtn2.setTextColor(getResources().getColor(color1));
                tvBottomBtn2.setBackgroundColor(getResources().getColor(color2));
                tvStateText1.setTextColor(getResources().getColor(color4));
                tvStateText2.setTextColor(getResources().getColor(color4));
                tvBottomBtn1.setOnClickListener(watchJourneyPlanListener);
                tvBottomBtn2.setOnClickListener(watchProtocalListener);
                break;

            case STATE_5:
                llBottomBtns.setVisibility(View.VISIBLE);
                tvBottomBtn1.setVisibility(View.VISIBLE);
                tvBottomBtn2.setVisibility(View.VISIBLE);
                tvBottomBtn1.setText("获取保险");
                tvBottomBtn1.setTextColor(getResources().getColor(color2));
                tvBottomBtn1.setBackgroundColor(getResources().getColor(color1));
                tvBottomBtn2.setText("行程方案");
                tvBottomBtn2.setTextColor(getResources().getColor(color1));
                tvBottomBtn2.setBackgroundColor(getResources().getColor(color2));
                tvStateText1.setTextColor(getResources().getColor(color4));
                tvStateText2.setTextColor(getResources().getColor(color4));
                tvStateText3.setTextColor(getResources().getColor(color1));
                tvBottomBtn1.setOnClickListener(getInsuranceListener);
                tvBottomBtn2.setOnClickListener(watchJourneyPlanListener);
                break;

            case STATE_6:
            case STATE_9:
                llBottomBtns.setVisibility(View.VISIBLE);
                tvBottomBtn1.setVisibility(View.VISIBLE);
                tvBottomBtn2.setVisibility(View.VISIBLE);
                tvBottomBtn3.setVisibility(View.VISIBLE);
                tvBottomBtn1.setText("评价反馈");
                tvBottomBtn1.setTextColor(getResources().getColor(color2));
                tvBottomBtn1.setBackgroundColor(getResources().getColor(color1));
                tvBottomBtn2.setText("查看保险");
                tvBottomBtn2.setTextColor(getResources().getColor(color1));
                tvBottomBtn2.setBackgroundColor(getResources().getColor(color2));
                tvBottomBtn3.setText("行程方案");
                tvBottomBtn3.setTextColor(getResources().getColor(color2));
                tvBottomBtn3.setBackgroundColor(getResources().getColor(color1));
                tvStateText1.setTextColor(getResources().getColor(color4));
                tvStateText2.setTextColor(getResources().getColor(color4));
                tvStateText3.setTextColor(getResources().getColor(color1));
                tvBottomBtn1.setOnClickListener(callbackListener);
                tvBottomBtn2.setOnClickListener(watchInsuranceListener);
                tvBottomBtn3.setOnClickListener(watchJourneyPlanListener);
                break;

            case STATE_8:
                llBottomBtns.setVisibility(View.VISIBLE);
                tvBottomBtn1.setVisibility(View.VISIBLE);
                tvBottomBtn2.setVisibility(View.VISIBLE);
                tvBottomBtn1.setText("行程方案");
                tvBottomBtn1.setTextColor(getResources().getColor(color2));
                tvBottomBtn1.setBackgroundColor(getResources().getColor(color1));
                tvBottomBtn2.setText("支付订金");
                tvBottomBtn2.setTextColor(getResources().getColor(color1));
                tvBottomBtn2.setBackgroundColor(getResources().getColor(color2));
                tvStateText1.setTextColor(getResources().getColor(color4));
                tvStateText2.setTextColor(getResources().getColor(color4));
                tvStateText3.setTextColor(getResources().getColor(color1));
                tvBottomBtn1.setOnClickListener(watchJourneyPlanListener);
                tvBottomBtn2.setOnClickListener(paySubscriptionListener);
                break;

            case STATE_7:
                llBottomBtns.setVisibility(View.VISIBLE);
                tvBottomBtn1.setVisibility(View.VISIBLE);
                tvBottomBtn1.setText("删除订单");
                tvBottomBtn1.setTextColor(getResources().getColor(color2));
                tvBottomBtn1.setBackgroundColor(getResources().getColor(color3));
                tvStateText1.setTextColor(getResources().getColor(color4));
                tvStateText2.setTextColor(getResources().getColor(color4));
                tvStateText3.setTextColor(getResources().getColor(color1));
                tvBottomBtn1.setOnClickListener(deleteJourneyPlanListener);
                break;

            default:
                break;
        }

    }

    private View.OnClickListener paySubscriptionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "正在打开支付功能...", Toast.LENGTH_SHORT).show();
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("sessionid", LvYouApplication.getSessionId());
            params.put("id", planLogicId);
            client.post(MyJourneyActivity.this, Constants.URL + "/user/trip.get_trade_no.do", params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {

                        int code = response.getInt("code");
                        if (code == 0) {
                            key = response.optString("data");
                            paySubscription();
                        }else {
                            Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Toast.makeText(mContext, "网络连接失败，请稍后重试", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    private View.OnClickListener submitJourneyPlanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            submitJourneyPlan();
        }
    };

    private View.OnClickListener modifyJourneyPlanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("id", planLogicId);
            client.get(MyJourneyActivity.this, Constants.URL + "user/trip.edit.json", params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {

                        int code = response.getInt("code");
                        if (code == 0) {
                            JSONObject dataObject = response.getJSONObject("data");
                            hotelId = dataObject.optLong("hotelId");
                            placeId = dataObject.optLong("placeId");
                            contactName = dataObject.optString("contactName");
                            contactTel = dataObject.optString("contactTel");
                            startDate = dataObject.optString("startDate");
                            endDate = dataObject.optString("endDate");
                            viewSpotList = ViewSpot.insertWithArray(dataObject.getJSONArray("viewspotList"));

                            handler.sendEmptyMessage(GET_VIEW_SPOT_DATA_SUCCESS);
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
    };

    public void startModifyJourney() {
        Intent intent = new Intent();
        intent.setClass(MyJourneyActivity.this, MakeJourneyActivity.class);
        intent.putExtra("come_from_my_journey",true);
        intent.putExtra("destination_id", destId);
        intent.putExtra("place", planDetail.place);
        intent.putExtra("peopleNum", planDetail.member_num);
        intent.putExtra("request_to_modify", true);
        intent.putExtra("startDate", startDate);
        intent.putExtra("endDate", endDate);
        intent.putExtra("hotel", planDetail.hotel_name);
        intent.putExtra("hotelId", hotelId);
        intent.putExtra("placeId", placeId);
        intent.putExtra("contactName", contactName);
        intent.putExtra("contactTel", contactTel);
        intent.putExtra("view_spot_list", (Serializable) viewSpotList);
        intent.putExtra("planId", planLogicId);
        startActivity(intent);
    }

    private View.OnClickListener watchJourneyPlanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            watchJourneyPlan();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_WATCH_PLAN:
                needRefresh = true;
                initData();
                break;

            case REQUEST_WATCH_PROTOCAL:
                break;

            default:
                break;
        }
    }

    private View.OnClickListener watchProtocalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            watchProtocal();
        }
    };

    private View.OnClickListener confirmProtocalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            confirmProtocal();
        }
    };


    private View.OnClickListener getInsuranceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getInsurance();
        }
    };

    private View.OnClickListener callbackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MyJourneyActivity.this, CallbackActivity.class);
            intent.putExtra("plan_logic_id", planLogicId);
            intent.putExtra("dest_id", planDetail.destination_logic_id + "");
            intent.putExtra("image_url", planDetail.head_image);
            intent.putExtra("title", planDetail.title);
            intent.putExtra("order_num", planDetail.order_num);
            intent.putExtra("create_time", planDetail.create_time);
            startActivity(intent);
        }
    };

    private View.OnClickListener watchInsuranceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getInsurance();
        }
    };

    private View.OnClickListener deleteJourneyPlanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            deleteJourneyPlan();
        }
    };

    private void deleteJourneyPlan() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("id", planLogicId);
        client.post(Constants.URL + "/user/trip.del.do", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("liujie", response.toString());
                if (0 == response.optInt("code")) {
                    Toast.makeText(mContext, "删除订单成功", Toast.LENGTH_SHORT).show();
                    needRefresh = true;
                    finish();
                } else {
                    Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("liujie", responseString.toString());
                Toast.makeText(mContext, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void paySubscription() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    finish();
                                }
                            }).show();
            return;
        }
        // 订单
        String orderInfo = getOrderInfo("行程订单 : " + planDetail.title, "愉快的旅行", (0.5 * planDetail.unit_price * planDetail.member_num) + "");
 //       String orderInfo = getOrderInfo("行程订单 : " + planDetail.title, "愉快的旅行", 0.01 + "");

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(MyJourneyActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + Constants.URL + "pay/app_notify.do"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }


    /**
     * 获取保险
     */
    private void getInsurance() {
        Intent intent = new Intent(MyJourneyActivity.this, GetInsuranceActivity.class);
        intent.putExtra("plan_logic_id", planLogicId);
        intent.putExtra("title", planDetail.title);
        intent.putExtra("order_num", planDetail.order_num);
        intent.putExtra("create_time", planDetail.create_time);
        startActivity(intent);
    }

    /**
     * 确认合同
     */
    private void confirmProtocal() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("id", planLogicId);
        client.post(Constants.URL + "/user/trip.confirm_contract.do", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("liujie", response.toString());
                if (0 == response.optInt("code")) {
                    Toast.makeText(mContext, "确认成功", Toast.LENGTH_SHORT).show();
                    needRefresh = true;
                    initData();
                } else {
                    Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("liujie", responseString.toString());
                Toast.makeText(mContext, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查看合同
     */
    private void watchProtocal() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("planId", planLogicId);
        client.post(Constants.URL + "/main/common.view_contract_url.json", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("liujie", response.toString());
                if (0 == response.optInt("code")) {
                    Intent intent = new Intent(MyJourneyActivity.this, WebViewActivity.class);
                    intent.putExtra("title", "查看合同");
                    intent.putExtra("back_text", "关闭网页");
                    intent.putExtra("url", response.optString("data"));
                    startActivityForResult(intent, REQUEST_WATCH_PLAN);
                } else {
                    Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("liujie", responseString.toString());
                Toast.makeText(mContext, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 查看行程方案
     */
    private void watchJourneyPlan() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("planId", planLogicId);
        client.post(Constants.URL + "/main/common.trip_plan_url.json", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("liujie", response.toString());
                if (0 == response.optInt("code")) {
                    Intent intent = new Intent(MyJourneyActivity.this, WebViewActivity.class);
                    intent.putExtra("title", "查看行程方案");
                    intent.putExtra("back_text", "关闭网页");
                    intent.putExtra("url", response.optString("data"));
                    startActivityForResult(intent, REQUEST_WATCH_PLAN);
                } else {
                    Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("liujie", responseString.toString());
                Toast.makeText(mContext, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 提交行程方案
     */
    private void submitJourneyPlan() {
        final SubmitJourneyPlanDialog dialog = new SubmitJourneyPlanDialog(mContext, planDetail.id + "", new DialogListener() {
            @Override
            public void refreshActivity(Object data) {
                String result = (String) data;
                if ("0".equals(result)) {
                    Toast.makeText(mContext, "提交行程方案成功", Toast.LENGTH_SHORT).show();
                    needRefresh = true;
                    initData();
                } else {
                    Toast.makeText(mContext, "提交失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}
