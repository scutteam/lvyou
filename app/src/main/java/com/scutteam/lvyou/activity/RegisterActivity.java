package com.scutteam.lvyou.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.util.DensityUtil;
import com.scutteam.lvyou.util.ScreenManager;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private UMSocialService mController = UMServiceFactory
            .getUMSocialService(Constants.DESCRIPTOR);
    private ImageButton mIbtnBack;
    private EditText mEtPhone;
    private EditText mEtCaptcha;
    private TextView mTvGetCaptcha;
    private TextView mTvRegister;
    private LinearLayout mLlWeibo;
    private LinearLayout mLlWeiXin;
    private TextView mTvRegisterTitle;
    private LinearLayout mLlQQ;
    private LinearLayout mLlMain;
    private TextView mTvLogin;
    private RelativeLayout mRlRegisterTop;
    private LinearLayout mLlOauth;
    private TextView mTvNotice;
    private LinearLayout mLlRegisterCenter;
    private int mEtPhoneFirstY; //一开始的y
    private int mEtPhoneSecondY; //消失了上半部分的y
    private boolean isEtPhoneFocusBefore = false;
    private boolean isEtCaptchaFocusBefore = false;
    private boolean isFirstTime = true;
    private boolean isSecondTime = true;private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = null;
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
                case CHECK_CAPTCHA_CORRECT:
                    //准备注册
                    intent = new Intent();
                    intent.putExtra("phone",mEtPhone.getText().toString());
                    intent.putExtra("captcha",mEtCaptcha.getText().toString());
                    intent.setClass(RegisterActivity.this,CheckPhoneNumberActivity.class);
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
    private String screen_name;
    private String profile_image_url;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ScreenManager.getScreenManager().addActivity(RegisterActivity.this);
        initUmeng();
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {

        ScreenManager.getScreenManager().finishActivity(RegisterActivity.this);
        super.onDestroy();
    }

    public void initUmeng() {
        addQZoneQQPlatform();
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

    private void addQZoneQQPlatform() {
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(RegisterActivity.this,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://www.umeng.com");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(RegisterActivity.this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }
    
    public void initView() {
        mTvLogin = (TextView) findViewById(R.id.tv_login);
        mIbtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtCaptcha = (EditText) findViewById(R.id.et_captcha);
        mTvGetCaptcha = (TextView) findViewById(R.id.tv_get_captcha);
        mTvRegister = (TextView) findViewById(R.id.tv_register);
        mLlWeibo = (LinearLayout) findViewById(R.id.ll_weibo);
        mLlWeiXin = (LinearLayout) findViewById(R.id.ll_weixin);
        mLlQQ = (LinearLayout) findViewById(R.id.ll_qq);
        mLlMain = (LinearLayout) findViewById(R.id.ll_main);
        mRlRegisterTop = (RelativeLayout) findViewById(R.id.rl_register_top);
        mLlOauth = (LinearLayout) findViewById(R.id.ll_oauth);
        mTvRegisterTitle = (TextView) findViewById(R.id.tv_register_title);
        mLlRegisterCenter = (LinearLayout) findViewById(R.id.ll_register_center);
        mTvNotice = (TextView) findViewById(R.id.tv_notice);
    }


    public void initSecondY() {
        int[] location = new int[2];
        mEtPhone.getLocationOnScreen(location);
        mEtPhoneSecondY = location[1]; //获得的是px
    }

    public void initFirstY() {
        int[] location = new int[2];
        mEtPhone.getLocationOnScreen(location);
        mEtPhoneFirstY = location[1]; //获得的是px
    }

    public void initListener() {
        mEtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (isEtCaptchaFocusBefore) {

                    } else {
                        hideLoginTopAndOauth();
                    }
                    isEtPhoneFocusBefore = true;
                    isEtCaptchaFocusBefore = false;
                } else {

                }
            }
        });
        mEtCaptcha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(isEtPhoneFocusBefore) {

                    } else {
                        hideLoginTopAndOauth();
                    }
                    isEtCaptchaFocusBefore = true;
                    isEtPhoneFocusBefore = false;
                } else {

                }
            }
        });
        mLlMain.setOnClickListener(this);
        mTvGetCaptcha.setOnClickListener(this);
        mIbtnBack.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        mLlWeibo.setOnClickListener(this);
        mLlWeiXin.setOnClickListener(this);
        mLlQQ.setOnClickListener(this);
    }

    public void forceHideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) LvYouApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void hideLoginTopAndOauth() {

        mTvNotice.setVisibility(View.VISIBLE);
        mTvLogin.setVisibility(View.GONE);

        if(isFirstTime) {
            initFirstY();
            isFirstTime = false;
        }

        mTvRegisterTitle.setVisibility(View.VISIBLE);

        AlphaAnimation animation = new AlphaAnimation(1.0f,0.1f);
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());

        mRlRegisterTop.setVisibility(View.GONE);
        mLlOauth.setVisibility(View.GONE);

        mRlRegisterTop.startAnimation(animation);
        mLlOauth.startAnimation(animation);

        TranslateAnimation translateAnimation = new TranslateAnimation(0,0, DensityUtil.px2dip(RegisterActivity.this, mEtPhoneFirstY),48);
        translateAnimation.setDuration(300);
        translateAnimation.setInterpolator(new LinearInterpolator());
        mLlRegisterCenter.startAnimation(translateAnimation);
    }

    public void showLoginTopAndOauth() {

        mTvNotice.setVisibility(View.GONE);
        mTvLogin.setVisibility(View.VISIBLE);

        if(isSecondTime) {
            initSecondY();
            isSecondTime = false;
        }

        mTvRegisterTitle.setVisibility(View.GONE);

        AlphaAnimation animation = new AlphaAnimation(0.1f,1.0f);
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());

        mRlRegisterTop.setVisibility(View.VISIBLE);
        mLlOauth.setVisibility(View.VISIBLE);

        mRlRegisterTop.startAnimation(animation);
        mLlOauth.startAnimation(animation);

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_main:
                if(mLlOauth.getVisibility() == View.GONE) {
                    isEtPhoneFocusBefore = false;
                    isEtCaptchaFocusBefore = false;
                    showLoginTopAndOauth();
                    forceHideKeyboard(mEtPhone); //强制退出键盘
                }
                break;
            case R.id.tv_login:
                finish();
                break;
            case R.id.tv_register:
               if(TextUtils.isEmpty(mEtPhone.getText())) {
                   Toast.makeText(RegisterActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
               } else if (TextUtils.isEmpty(mEtCaptcha.getText())) {
                   Toast.makeText(RegisterActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
               } else {
                   check_captcha();
               }
                break;
            case R.id.ll_weibo:
                login(SHARE_MEDIA.SINA);
                break;
            case R.id.ll_weixin:
                Toast.makeText(RegisterActivity.this,"准备微信登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_qq:
                login(SHARE_MEDIA.QQ);
                break;
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.tv_get_captcha:
                if(TextUtils.isEmpty(mEtPhone.getText())) {
                    Toast.makeText(RegisterActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    getCaptcha();
                }
                break;
            default:
                break;
        }
    }
    
    public void check_captcha() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("phone",mEtPhone.getText().toString());
        params.put("verifyCode",mEtCaptcha.getText().toString());
        client.post(RegisterActivity.this,Constants.URL + "user/personal.verification_code.do",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    Log.e("response_captcha", response.toString());
                    int code = response.getInt("code");
                    if(code == 0) {
                        handler.sendEmptyMessage(CHECK_CAPTCHA_CORRECT);
                    } else {
                        Toast.makeText(RegisterActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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
    
    public void getCaptcha() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("mobile",mEtPhone.getText().toString());
        client.post(RegisterActivity.this, Constants.URL + "user/personal.send_code.do",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        Toast.makeText(RegisterActivity.this,"发送成功...",Toast.LENGTH_SHORT).show();
                        now_captcha_update_time = DEFAULT_CAPTCHA_UPDATE_TIME;
                        startCaptchaTimer();
                    } else {
                        Toast.makeText(RegisterActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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

    /**
     * 授权。如果授权成功，则获取用户信息</br>
     */
    private void login(final SHARE_MEDIA platform) {
        mController.doOauthVerify(RegisterActivity.this, platform, new SocializeListeners.UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA platform) {
                Toast.makeText(RegisterActivity.this, "请稍候,正在连接...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                Toast.makeText(RegisterActivity.this, "网络连接故障,请稍候再试...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {

                Toast.makeText(RegisterActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                String uid = value.getString("uid");
                if (!TextUtils.isEmpty(uid)) {
                    getUserInfo(platform);
                } else {
                    Toast.makeText(RegisterActivity.this, "授权失败...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }
        });
    }

    private void getUserInfo(final SHARE_MEDIA platform) {
        mController.getPlatformInfo(RegisterActivity.this, platform, new SocializeListeners.UMDataListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
                //在这里获取用户的数据
                if (info != null) {
                    if(platform == SHARE_MEDIA.SINA) {
                        screen_name = String.valueOf(info.get("screen_name"));
                        profile_image_url = String.valueOf(info.get("profile_image_url"));
                    } else if(platform == SHARE_MEDIA.QQ) {
                        screen_name = String.valueOf(info.get("screen_name"));
                        profile_image_url = String.valueOf(info.get("profile_image_url"));
                    } else if(platform == SHARE_MEDIA.WEIXIN) {

                    }

                    LvYouApplication.setImageProfileUrl(profile_image_url);
                    LvYouApplication.setScreenName(screen_name);

                    //接下来绑定账号
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this,BindAccountActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                }
            }
        });
    }
}
