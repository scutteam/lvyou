package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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


public class ResetPasswordActivity extends Activity implements View.OnClickListener {

    private EditText mEtPassword;
    private EditText mEtPassword2;
    private ImageButton mIbtnBack;
    private TextView mTvSubmit;
    private Long user_id;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case RESET_PASSWORD_SUCCESS:
                    Intent intent = new Intent();
                    intent.putExtra("is_back",true);
                    intent.setClass(ResetPasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
    private static final int RESET_PASSWORD_SUCCESS = 40000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ScreenManager.getScreenManager().addActivity(ResetPasswordActivity.this);
        initData();
        initView();
        initListener();
    }
    
    public void initData() {
        user_id = getIntent().getLongExtra("user_id",0L);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ScreenManager.getScreenManager().finishActivity(ResetPasswordActivity.this);
    }

    public void initView() {
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtPassword2 = (EditText) findViewById(R.id.et_password2);
        mIbtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mTvSubmit = (TextView) findViewById(R.id.tv_submit);
    }
    
    public void initListener() {
        mIbtnBack.setOnClickListener(this);
        mTvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.tv_submit:
                if(TextUtils.isEmpty(mEtPassword.getText()) ||TextUtils.isEmpty(mEtPassword2.getText())) {
                    Toast.makeText(ResetPasswordActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                } else if(!mEtPassword.getText().toString().equals(mEtPassword2.getText().toString())) {
                    Toast.makeText(ResetPasswordActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    changePassword();
                }
                break;
            default:
                break;
        }
    }
    
    public void changePassword() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("customer.id",user_id);
        params.put("customer.newPassword",mEtPassword.getText().toString());
        client.post(ResetPasswordActivity.this, Constants.URL + "user/personal.reset_pwd.do",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int code = response.getInt("code");
                    
                    if(code == 0) {
                        Toast.makeText(ResetPasswordActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                        handler.sendEmptyMessage(RESET_PASSWORD_SUCCESS);
                    } else {
                        Toast.makeText(ResetPasswordActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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
