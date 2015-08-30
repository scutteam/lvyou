package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.adapter.MemberAdapter;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.Member;
import com.scutteam.lvyou.widget.CircleImageView;
import com.umeng.socialize.utils.Log;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetMyInsuranceActivity extends Activity implements View.OnClickListener{

    private CircleImageView civAvatar;
    private TextView tvUserName;
    private TextView tvInsuranceDetail;
    private TextView tvSubmit;
    private TextView tvDestinationName;
    private TextView tvPlanCreateTime;
    private TextView tvPlanId;
    private EditText etSchool;
    private EditText etClass;
    private EditText etRealName;
    private EditText etCreditId;
    private EditText etPhone;

    private Context mContext;
    private ImageLoader imageLoader;

    private String planLogicId;
    private String title;
    private String orderNum;
    private String createTime;

    public static final int RESULT_GET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_my_insurance);
        mContext = GetMyInsuranceActivity.this;
        imageLoader = ImageLoader.getInstance();
        initData();
        initView();
        initListener();
    }

    private void initData() {
        Intent intent = getIntent();
        if(null != intent){
            planLogicId = intent.getStringExtra("plan_logic_id");
            title = intent.getStringExtra("title");
            orderNum = intent.getStringExtra("order_num");
            createTime = intent.getStringExtra("create_time");
        }
    }


    private void initView(){
        ((TextView)findViewById(R.id.center_text)).setText("获取保险");
        civAvatar= (CircleImageView)findViewById(R.id.get_my_insurance_image);
        tvUserName = (TextView)findViewById(R.id.get_my_insurance_plan_name);   //对应 哈波波的大团游
        tvDestinationName = (TextView)findViewById(R.id.get_my_insurance_dest_name);
        tvPlanCreateTime = (TextView)findViewById(R.id.get_my_insurance_create_time);
        tvPlanId = (TextView)findViewById(R.id.get_my_insurance_plan_id);
        tvInsuranceDetail = (TextView)findViewById(R.id.get_my_insurance_detail);
        tvSubmit = (TextView)findViewById(R.id.get_my_insurance_submit);
        etSchool = (EditText)findViewById(R.id.get_my_insurance_school);
        etClass = (EditText)findViewById(R.id.get_my_insurance_class);
        etRealName = (EditText)findViewById(R.id.get_my_insurance_user_name);
        etCreditId = (EditText)findViewById(R.id.get_my_insurance_user_id);
        etPhone = (EditText)findViewById(R.id.get_my_insurance_user_phone);

        tvDestinationName.setText(title);
        tvPlanCreateTime.setText("创建时间: " + createTime);
        tvPlanId.setText("订单号: " + orderNum);
        imageLoader.displayImage(LvYouApplication.getImageProfileUrl(), civAvatar);
        tvUserName.setText(LvYouApplication.getScreenName());
    }

    private void initListener() {
        findViewById(R.id.left_icon).setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    private void onSubmitClick(){
        Log.i("liujie", "click submit");
        String school = etSchool.getText().toString();
        String clvss = etClass.getText().toString();
        String userName = etRealName.getText().toString();
        String userCreditId = etCreditId.getText().toString();
        String userPhone = etPhone.getText().toString();
        if(!"".equals(school)) {
            if(!"".equals(clvss)) {
                if (!"".equals(userName)) {
                    if (!"".equals(userCreditId)) {
                        if (!"".equals(userPhone)) {
                            getMyInsurance(school, clvss, userName, userCreditId, userPhone);
                        } else {
                            Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "身份证号码不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(mContext, "班级不能为空", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(mContext, "学校不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMyInsurance(String school, String clvss, String name, String id, String phone){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("bi.planId", planLogicId);
        params.put("bi.school", school);
        params.put("bi.grade", clvss);
        params.put("bi.name", name);
        params.put("bi.idCard", id);
        params.put("bi.mobile", phone);
        client.post(Constants.URL + "/user/insurance.submit_buy.do", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                android.util.Log.i("liujie", response.toString());
                if (0 == response.optInt("code")) {
                    Toast.makeText(mContext, "获取保险成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_GET);
                    finish();
                } else {
                    Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                android.util.Log.i("liujie", responseString.toString());
                Toast.makeText(mContext, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left_icon:
                finish();
                break;

            case R.id.get_my_insurance_submit:
                onSubmitClick();
                break;

            default:
                break;
        }
    }
}
