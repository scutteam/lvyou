package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.util.ScreenManager;

import org.apache.http.Header;
import org.json.JSONObject;

public class CheckPhoneNumberActivity extends Activity implements View.OnClickListener {

    private ImageButton mIbtnBack;
    private TextView mTvPhone;
    private EditText mEtName;
    private EditText mEtPassword;
    private TextView mTvRegister;
    private String phone;
    private String captcha;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case FINALLY_REGISTER_SUCCESS:
                    Intent intent = new Intent();
                    intent.putExtra("is_back",true);
                    intent.setClass(CheckPhoneNumberActivity.this,LoginActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
    private static final int FINALLY_REGISTER_SUCCESS = 50000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone_number);

        ScreenManager.getScreenManager().addActivity(CheckPhoneNumberActivity.this);
        initData();
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {
        ScreenManager.getScreenManager().finishActivity(CheckPhoneNumberActivity.this);
        
        super.onDestroy();
    }

    public void initData() {
        phone = getIntent().getStringExtra("phone");
        captcha = getIntent().getStringExtra("captcha");
    }
    
    public void initView() {
        mIbtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mTvRegister = (TextView) findViewById(R.id.tv_register);

        mTvPhone.setText(phone);
    }
    
    public void initListener() {
        mIbtnBack.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.tv_register:
                //最终注册
                finally_register();
                break;
        }
    }
    
    public void finally_register() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("verifyCode",captcha);
        params.put("customer.nickName",mEtName.getText().toString());
        params.put("customer.phone",phone);
        params.put("customer.password",mEtPassword.getText().toString());
        client.post(CheckPhoneNumberActivity.this, Constants.URL + "user/personal.register.do",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        Toast.makeText(CheckPhoneNumberActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessage(FINALLY_REGISTER_SUCCESS);
                    } else {
                        Toast.makeText(CheckPhoneNumberActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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
}
