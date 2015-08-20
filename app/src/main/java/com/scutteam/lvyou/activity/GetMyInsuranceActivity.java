package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.widget.CircleImageView;
import com.umeng.socialize.utils.Log;

public class GetMyInsuranceActivity extends Activity implements View.OnClickListener{

    private CircleImageView civAvatar;
    private TextView tvPlanName;
    private TextView tvInsuranceDetail;
    private TextView tvSubmit;
    private TextView tvDestinationName;
    private TextView tvPlanCreateTime;
    private TextView tvPlanId;
    private EditText etRealName;
    private EditText etCreditId;
    private EditText etPhone;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_my_insurance);
        mContext = GetMyInsuranceActivity.this;
        initData();
        initView();
        initListener();
    }

    private void initData() {
    }


    private void initView(){
        TextView title = (TextView) findViewById(R.id.center_text);
        title.setText("获取保险");
        civAvatar= (CircleImageView)findViewById(R.id.get_my_insurance_image);
        tvPlanName = (TextView)findViewById(R.id.get_my_insurance_plan_name);   //对应 哈波波的大团游
        tvDestinationName = (TextView)findViewById(R.id.get_my_insurance_dest_name);
        tvPlanCreateTime = (TextView)findViewById(R.id.get_my_insurance_create_time);
        tvPlanId = (TextView)findViewById(R.id.get_my_insurance_plan_id);
        tvInsuranceDetail = (TextView)findViewById(R.id.get_my_insurance_detail);
        tvSubmit = (TextView)findViewById(R.id.get_my_insurance_sumit);
        etRealName = (EditText)findViewById(R.id.get_my_insurance_user_name);
        etCreditId = (EditText)findViewById(R.id.get_my_insurance_user_id);
        etPhone = (EditText)findViewById(R.id.get_my_insurance_user_phone);
    }

    private void initListener() {
        findViewById(R.id.left_icon).setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    private void onSubmitClick(){
        Log.i("liujie", "click submit");
        String userName = etRealName.getText().toString();
        String userCreditId = etCreditId.getText().toString();
        String userPhone = etPhone.getText().toString();
        if(!"".equals(userName)){
            if(!"".equals(userCreditId)){
                if(!"".equals(userPhone)){
                    getMyInsurance();
                }else {
                    Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(mContext, "身份证号码不能为空", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMyInsurance(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left_icon:
                finish();
                break;

            case R.id.get_my_insurance_sumit:
                onSubmitClick();
                break;

            default:
                break;
        }
    }
}
