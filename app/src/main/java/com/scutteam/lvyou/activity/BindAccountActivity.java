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
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.util.ScreenManager;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import static com.scutteam.lvyou.R.id.et_password;

public class BindAccountActivity extends Activity implements View.OnClickListener {

    private EditText mEtPhone;
    private EditText mEtPassword;
    private EditText mEtCaptcha;
    private TextView mTvGetCaptcha;
    private TextView mTvBind;
    private ImageButton mIbtnBack;
    private int type;
    private String screen_name;
    private String profile_image_url;
    private String openId;
    private String sessionId;
    private String customerId;
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
                    } else {
                        mTvGetCaptcha.setText("已发送("+now_captcha_update_time+")");
                    }
                    break;
                case GET_CAPTCHA_SUCCESS:
                    now_captcha_update_time = DEFAULT_CAPTCHA_UPDATE_TIME;
                    startCaptchaTimer();
                    break;
                case BIND_ACCOUNT_SUCCESS:
                    Intent intent = new Intent();
                    intent.setClass(BindAccountActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                    break;
                case GET_SESSIONID_SUCCESS:
                    getUserInfo();
                    break;
                case GET_USER_INFO:
                    finalBindAccount();
                    break;
            }
        }
    };
    private static final int UPDATE_CAPTCHA_TEXT = 100;
    private static final int DEFAULT_CAPTCHA_UPDATE_TIME = 60;
    private static final int GET_CAPTCHA_SUCCESS = 61;
    private static final int BIND_ACCOUNT_SUCCESS = 62;
    private static final int GET_SESSIONID_SUCCESS = 63;
    private static final int GET_USER_INFO = 64;
    private int now_captcha_update_time = 0;
    private Timer timer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_account);

        ScreenManager.getScreenManager().addActivity(BindAccountActivity.this);
        initData();
        initView();
        initListener();
    }
    
    public void initData() {
        type = getIntent().getIntExtra("type",0);
        screen_name = getIntent().getStringExtra("screen_name");
        profile_image_url = getIntent().getStringExtra("profile_image_url");
        openId = getIntent().getStringExtra("openId");
    }

    @Override
    protected void onDestroy() {
        ScreenManager.getScreenManager().finishActivity(BindAccountActivity.this);
        
        super.onDestroy();
    }

    public void initView () {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPassword = (EditText) findViewById(et_password);
        mEtCaptcha = (EditText) findViewById(R.id.et_captcha);
        mTvGetCaptcha = (TextView) findViewById(R.id.tv_get_captcha);
        mTvBind = (TextView) findViewById(R.id.tv_bind);
        mIbtnBack = (ImageButton) findViewById(R.id.ibtn_back);
    }
    
    public void initListener() {
        mTvGetCaptcha.setOnClickListener(this);
        mTvBind.setOnClickListener(this);
        mIbtnBack.setOnClickListener(this);
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
    
    public void getCaptcha() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("mobile",mEtPhone.getText().toString());
        client.post(BindAccountActivity.this, Constants.URL + "user/personal.send_code.do", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        Toast.makeText(BindAccountActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BindAccountActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                    handler.sendEmptyMessage(GET_CAPTCHA_SUCCESS);

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
            case R.id.tv_get_captcha:
                //发送验证码
                //确定已经发送验证码了 之后 开启定时器
                if(TextUtils.isEmpty(mEtPhone.getText())) {
                    Toast.makeText(BindAccountActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(mEtPassword.getText())) {
                    Toast.makeText(BindAccountActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    getCaptcha();
                }
                break;
            case R.id.tv_bind:

                if(TextUtils.isEmpty(mEtCaptcha.getText())) {
                    Toast.makeText(BindAccountActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(mEtPhone.getText())) {
                    Toast.makeText(BindAccountActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(mEtPassword.getText())) {
                    Toast.makeText(BindAccountActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    bindAccount();
                }
                break;
            case R.id.ibtn_back:
                Toast.makeText(BindAccountActivity.this,"取消绑定",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.push_right_out,R.anim.push_left_in);
                break;
            default:
                break;
        }  
    }
    
    public void bindAccount() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("type",type);
        params.put("openId",openId);
        params.put("nickName",screen_name);
        params.put("faceUrl",profile_image_url);
        client.post(BindAccountActivity.this, Constants.URL + "user/mobilelogin.bind_login.do", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.e("zhaohong",response.toString());
                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        sessionId = response.getString("data");
                        LvYouApplication.setSessionId(sessionId);

                        handler.sendEmptyMessage(GET_SESSIONID_SUCCESS);
                    } else {
                        Toast.makeText(BindAccountActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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
    
    public void getUserInfo() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", sessionId);
        client.post(BindAccountActivity.this,Constants.URL + "user/mobilelogin.info.do",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    int code = response.getInt("code");
                    if(code == 0) {
                        customerId = dataObject.getString("id");
                        
                        handler.sendEmptyMessage(GET_USER_INFO);
                    } else {
                        Toast.makeText(BindAccountActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
    
    public void finalBindAccount() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid",sessionId);
        params.put("verifyCode",mEtCaptcha.getText().toString());
        params.put("customer.id",customerId);
        params.put("customer.phone", mEtPhone.getText().toString());
        params.put("customer.password", mEtPassword.getText().toString());
        client.post(BindAccountActivity.this, Constants.URL + "user/personal.bind.do", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        Toast.makeText(BindAccountActivity.this,"第三方账号绑定成功",Toast.LENGTH_SHORT).show();

                        handler.sendEmptyMessage(BIND_ACCOUNT_SUCCESS);
                    } else {
                        Toast.makeText(BindAccountActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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
