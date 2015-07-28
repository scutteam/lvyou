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

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.util.ScreenManager;

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
            }
        }
    };
    private static final int UPDATE_CAPTCHA_TEXT = 100;
    private static final int DEFAULT_CAPTCHA_UPDATE_TIME = 60;
    private int now_captcha_update_time = 0;
    private Timer timer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_account);

        ScreenManager.getScreenManager().addActivity(BindAccountActivity.this);
        initView();
        initListener();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_captcha:
                //发送验证码
                //确定已经发送验证码了 之后 开启定时器
                Toast.makeText(BindAccountActivity.this,"发送成功...",Toast.LENGTH_SHORT).show();
                now_captcha_update_time = DEFAULT_CAPTCHA_UPDATE_TIME;
                startCaptchaTimer();
                break;
            case R.id.tv_bind:

                if(TextUtils.isEmpty(mEtCaptcha.getText())) {
                    Toast.makeText(BindAccountActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(mEtPhone.getText())) {
                    Toast.makeText(BindAccountActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(mEtPassword.getText())) {
                    Toast.makeText(BindAccountActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    //完成绑定
                    Intent intent = new Intent();
                    intent.setClass(BindAccountActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);

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
}
