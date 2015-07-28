package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ModifyBindPhoneActivity extends Activity implements View.OnClickListener {

    private ImageView mIvBack;
    private TextView mTvPhone;
    private EditText mEtPhone;
    private EditText mEtCaptcha;
    private TextView mTvGetCaptcha;
    private TextView mTvSave;
    private Long data;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case Constants.MODIFY_BIND_PHONE_SUCCESS:
                    Toast.makeText(ModifyBindPhoneActivity.this,"修改绑定手机号码成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("phone",mEtPhone.getText().toString());
                    setResult(Constants.MODIFY_BIND_PHONE_SUCCESS,intent);
                    finish();
                    break;
                case UPDATE_CAPTCHA_TEXT:
                    now_captcha_update_time--;
                    if(now_captcha_update_time == 0) {
                        timer.cancel();
                        mTvGetCaptcha.setClickable(true);
                        mTvGetCaptcha.setText("获取验证码");
                        mTvGetCaptcha.setTextColor(getResources().getColor(R.color.image_color));
                        mTvGetCaptcha.setBackgroundDrawable(getResources().getDrawable(R.drawable.fragment_user_item_background));
                    } else {
                        mTvGetCaptcha.setText("重新获取("+now_captcha_update_time+")");
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private static final int UPDATE_CAPTCHA_TEXT = 4001;
    
    private static final int DEFAULT_CAPTCHA_UPDATE_TIME = 60;
    private int now_captcha_update_time = 0;
    private Timer timer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_bind_phone);
        
        initView();
        initListener();
    }
    
    public void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtCaptcha = (EditText) findViewById(R.id.et_captcha);
        mTvGetCaptcha = (TextView) findViewById(R.id.tv_get_captcha);
        mTvSave = (TextView) findViewById(R.id.tv_save);
        
        mTvPhone.setText(LvYouApplication.phone);
    }
    
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mTvGetCaptcha.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_get_captcha:
                if(TextUtils.isEmpty(mEtPhone.getText())) {
                    Toast.makeText(ModifyBindPhoneActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    getCaptcha();
                }
                break;
            case R.id.tv_save:
                if(TextUtils.isEmpty(mEtCaptcha.getText())) {
                    Toast.makeText(ModifyBindPhoneActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(mEtPhone.getText())) {
                    Toast.makeText(ModifyBindPhoneActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    modifyBindPhone();
                }
                break;
            default:
                break;
        }
    }
    
    public void getCaptcha() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("mobile", mEtPhone.getText().toString());
        client.post(ModifyBindPhoneActivity.this, Constants.URL + "user/personal.send_code.do",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        Toast.makeText(ModifyBindPhoneActivity.this,"验证码成功发送",Toast.LENGTH_SHORT).show();
                        data = response.getLong("data");
                        mTvGetCaptcha.setBackgroundDrawable(getResources().getDrawable(R.drawable.captcha_button_background_selected));
                        mTvGetCaptcha.setTextColor(getResources().getColor(R.color.white));
                        now_captcha_update_time = DEFAULT_CAPTCHA_UPDATE_TIME;
                        startCaptchaTimer();
                    } else {
                        Toast.makeText(ModifyBindPhoneActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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
    
    public void modifyBindPhone() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();
        param.put("sessionid",LvYouApplication.getSessionId());
        param.put("phone",LvYouApplication.phone);
        param.put("verifyCode",mEtCaptcha.getText().toString());
        param.put("newPhone",mEtPhone.getText().toString());
        client.post(ModifyBindPhoneActivity.this,Constants.URL + "user/personal.modify_phone.do",param,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        handler.sendEmptyMessage(Constants.MODIFY_BIND_PHONE_SUCCESS);
                    } else {
                        Toast.makeText(ModifyBindPhoneActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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
