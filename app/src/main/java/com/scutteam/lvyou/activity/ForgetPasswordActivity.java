package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.Timer;
import java.util.TimerTask;


public class ForgetPasswordActivity extends Activity implements View.OnClickListener {

    private EditText mEtPhone;
    private EditText mEtCaptcha;
    private TextView mTvSubmit;
    private TextView mTvGetCaptcha;
    private ImageButton mIbtnBack;
    private Long user_id = 0L;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case UPDATE_CAPTCHA_TEXT:
                    now_captcha_update_time--;
                    if(now_captcha_update_time == 0) {
                        timer.cancel();
                        mTvGetCaptcha.setClickable(true);
                        mTvGetCaptcha.setText("获取验证码");
                        mTvGetCaptcha.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_another_two_button_background));
                    } else {
                        mTvGetCaptcha.setText("已发送("+now_captcha_update_time+")");
                    }
                    break;
                case CHECK_CAPTCHA_CORRECT:
                    //判断成功后 跳转到修改密码界面
                    Intent intent = new Intent();
                    intent.putExtra("user_id",user_id);
                    intent.setClass(ForgetPasswordActivity.this,ResetPasswordActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_out,R.anim.push_left_in);
                    break;
            }
        }
    };
    private static final int UPDATE_CAPTCHA_TEXT = 100;
    private static final int DEFAULT_CAPTCHA_UPDATE_TIME = 60;
    private static final int CHECK_CAPTCHA_CORRECT = 101;
    private int now_captcha_update_time = 0;
    private Timer timer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        ScreenManager.getScreenManager().addActivity(ForgetPasswordActivity.this);
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ScreenManager.getScreenManager().finishActivity(ForgetPasswordActivity.this);
    }

    public void initView() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtCaptcha = (EditText) findViewById(R.id.et_captcha);
        mTvSubmit = (TextView) findViewById(R.id.tv_submit);
        mTvGetCaptcha = (TextView) findViewById(R.id.tv_get_captcha);
        mIbtnBack = (ImageButton) findViewById(R.id.ibtn_back);
    }
    
    public void initListener() {
        mTvSubmit.setOnClickListener(this);
        mTvGetCaptcha.setOnClickListener(this);
        mIbtnBack.setOnClickListener(this);
    }
    
    public void checkCaptcha() {
        
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("phone",mEtPhone.getText().toString());
        params.put("verifyCode",mEtCaptcha.getText().toString());
        client.post(ForgetPasswordActivity.this,Constants.URL + "user/personal.verification_code.do",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    Log.e("response_captcha",response.toString());
                    int code = response.getInt("code");
                    if(code == 0) {
                         handler.sendEmptyMessage(CHECK_CAPTCHA_CORRECT);
                    } else {
                         Toast.makeText(ForgetPasswordActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                if(TextUtils.isEmpty(mEtCaptcha.getText())) {
                    Toast.makeText(ForgetPasswordActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(mEtPhone.getText())) {
                    Toast.makeText(ForgetPasswordActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    //提交
                    checkCaptcha();
                }
                break;
            case R.id.tv_get_captcha:
                if(TextUtils.isEmpty(mEtPhone.getText())) {
                    Toast.makeText(ForgetPasswordActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    getCaptcha();
                }
                break;
            case R.id.ibtn_back:
                finish();
                break;
            default:
                break;
        }
    }
    
    public void getCaptcha() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("mobile",mEtPhone.getText().toString());
        client.post(ForgetPasswordActivity.this, Constants.URL + "user/personal.send_code_check.do",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    Log.e("user_id",response.toString());
                    int code = response.getInt("code");
                    if(code == 0) {
                        user_id = response.getLong("data");
                        
                        Toast.makeText(ForgetPasswordActivity.this,"发送成功...",Toast.LENGTH_SHORT).show();
                        mTvGetCaptcha.setBackgroundDrawable(getResources().getDrawable(R.drawable.captcha_button_background_selected));
                        now_captcha_update_time = DEFAULT_CAPTCHA_UPDATE_TIME;
                        startCaptchaTimer();
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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

    public void startCaptchaTimer() {
        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = UPDATE_CAPTCHA_TEXT;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task,1000,1000);
        mTvGetCaptcha.setClickable(false);
    }
}
