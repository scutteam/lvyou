package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.dialog.DialogListener;
import com.scutteam.lvyou.dialog.SubmitJourneyPlanDialog;
import com.scutteam.lvyou.model.Plan;
import com.scutteam.lvyou.model.PlanDetail;

import org.apache.http.Header;
import org.json.JSONObject;

public class MyJourneyActivity extends Activity{

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


    private final int STATE_1 = 1;    //底部显示提交行程计划跟修改行程计划(state = 1)
    private final int STATE_2 = 2;    //底部显示什么都没有(state = 2)
    private final int STATE_3 = 3;    //底部显示查看行程计划(state = 3)
    private final int STATE_4 = 4;    //底部显示查看行程方案，查看合同，确认合同(state = 4)
    private final int STATE_5 = 5;    //底部显示获取保险，支付订金，行程方案(state = 5)
    private final int STATE_6 = 6;    //底部显示评价反馈，查看保险，行程方案(state = 7)
    private final int STATE_7 = 7;    //底部显示删除订单(state = 0)


    private final int color1 = R.color.image_color;
    private final int color2 = R.color.ldrawer_color;
    private final int color3 = R.color.my_journey_state_delete;
    private final int color4 = R.color.main_text_color;
    private final int color5 = R.color.secondary_text_color;

    private boolean isFirstIn = true;
    private String planLogicId;
    private PlanDetail planDetail;
    private boolean needRefresh = false;

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
    private void findView(){
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
        tvStateText1 = (TextView)findViewById(R.id.my_journey_state_text_1);
        tvStateText2 = (TextView)findViewById(R.id.my_journey_state_text_2);
        tvStateText3 = (TextView)findViewById(R.id.my_journey_state_text_3);
    }

    private void initView(PlanDetail plan) {
        if(isFirstIn) {
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
            tvUnitPrice.setText((int) plan.unit_price + "/人");
            isFirstIn = false;
        }
        tvState.setText(plan.state_text);
        tvStateHint.setText(plan.status_tips);

        if(plan.state == 0) {
            refreshBottomButton(STATE_7);
        }else if(plan.state == 7){
            refreshBottomButton(STATE_6);
        }else{
            refreshBottomButton(plan.state);
        }

    }

    private void backToMyPlanFragment(){
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
                tvBottomBtn2.setOnClickListener(modifyJorneyPlanListener);
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
                tvBottomBtn1.setOnClickListener(wathchJorneyPlanListener);
                break;

            case STATE_4:
                llBottomBtns.setVisibility(View.VISIBLE);
                tvBottomBtn1.setVisibility(View.VISIBLE);
                tvBottomBtn2.setVisibility(View.VISIBLE);
                tvBottomBtn3.setVisibility(View.VISIBLE);
                tvBottomBtn1.setText("查看行程方案");
                tvBottomBtn1.setTextColor(getResources().getColor(color2));
                tvBottomBtn1.setBackgroundColor(getResources().getColor(color1));
                tvBottomBtn2.setText("查看合同");
                tvBottomBtn2.setTextColor(getResources().getColor(color1));
                tvBottomBtn2.setBackgroundColor(getResources().getColor(color2));
                tvBottomBtn3.setText("确认合同");
                tvBottomBtn3.setTextColor(getResources().getColor(color2));
                tvBottomBtn3.setBackgroundColor(getResources().getColor(color1));
                tvStateText1.setTextColor(getResources().getColor(color4));
                tvStateText2.setTextColor(getResources().getColor(color4));
                tvStateText3.setTextColor(getResources().getColor(color1));
                tvBottomBtn1.setOnClickListener(wathchJorneyPlanListener);
                tvBottomBtn2.setOnClickListener(watchProtocalListener);
                tvBottomBtn3.setOnClickListener(confirmProtocalListener);

                break;

            case STATE_5:
                llBottomBtns.setVisibility(View.VISIBLE);
                tvBottomBtn1.setVisibility(View.VISIBLE);
                tvBottomBtn2.setVisibility(View.VISIBLE);
                tvBottomBtn3.setVisibility(View.VISIBLE);
                tvBottomBtn1.setText("获取保险");
                tvBottomBtn1.setTextColor(getResources().getColor(color2));
                tvBottomBtn1.setBackgroundColor(getResources().getColor(color1));
                tvBottomBtn2.setText("支付订金");
                tvBottomBtn2.setTextColor(getResources().getColor(color1));
                tvBottomBtn2.setBackgroundColor(getResources().getColor(color2));
                tvBottomBtn3.setText("行程方案");
                tvBottomBtn3.setTextColor(getResources().getColor(color2));
                tvBottomBtn3.setBackgroundColor(getResources().getColor(color1));
                tvStateText1.setTextColor(getResources().getColor(color4));
                tvStateText2.setTextColor(getResources().getColor(color4));
                tvStateText3.setTextColor(getResources().getColor(color1));
                tvBottomBtn1.setOnClickListener(getInsuranceListener);
                tvBottomBtn2.setOnClickListener(payListener);
                tvBottomBtn3.setOnClickListener(wathchJorneyPlanListener);
                break;

            case STATE_6:
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
                tvBottomBtn3.setOnClickListener(wathchJorneyPlanListener);
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

    private View.OnClickListener submitJourneyPlanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            submitJourneyPlan();
        }
    };

    private View.OnClickListener modifyJorneyPlanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener wathchJorneyPlanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener watchProtocalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener confirmProtocalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };


    private View.OnClickListener getInsuranceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener payListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener callbackListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener watchInsuranceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener deleteJourneyPlanListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private void submitJourneyPlan(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("id", planLogicId);
        client.post(Constants.URL + "/user/trip.submit_plan.do", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("liujie", response.toString());
                if (0 == response.optInt("code")) {
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
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.show();
                        }
                    });
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
