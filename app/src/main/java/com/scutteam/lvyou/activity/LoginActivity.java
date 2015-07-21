package com.scutteam.lvyou.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.util.DensityUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;

import java.util.Map;

public class LoginActivity extends Activity implements View.OnClickListener {

    private LinearLayout mLlMain;
    private TextView mTvLoginTitle;
    private RelativeLayout mRlLoginTop;
    private LinearLayout mLlOauth;
    private EditText mEtPhone;
    private EditText mEtPassword;
    private TextView tvLogin;
    private TextView tvForgetPassword;
    private TextView tvRegister;
    private LinearLayout mLlWeibo;
    private LinearLayout mLlWeiXin;
    private LinearLayout mLlQQ;
    private LinearLayout mLlLoginCenter;
    private int mEtPhoneFirstY; //一开始的y
    private int mEtPhoneSecondY; //消失了上半部分的y
    private boolean isEtPhoneFocusBefore = false;
    private boolean isEtPasswordFocusBefore = false;
    private boolean isFirstTime = true;
    private boolean isSecondTime = true;
    private UMSocialService mController = UMServiceFactory
            .getUMSocialService(Constants.DESCRIPTOR);
    private long mExitTime = 0L;
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUmeng();
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initUmeng() {
        addQZoneQQPlatform();
    }

    private void addQZoneQQPlatform() {
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(LoginActivity.this,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://www.umeng.com");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(LoginActivity.this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }
    
    public void initView() {
        mLlMain = (LinearLayout) findViewById(R.id.ll_main);
        mTvLoginTitle = (TextView) findViewById(R.id.tv_login_title);
        mRlLoginTop = (RelativeLayout) findViewById(R.id.rl_login_top);
        mLlOauth = (LinearLayout) findViewById(R.id.ll_oauth);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        mLlWeibo = (LinearLayout) findViewById(R.id.ll_weibo);
        mLlWeiXin = (LinearLayout) findViewById(R.id.ll_weixin);
        mLlQQ = (LinearLayout) findViewById(R.id.ll_qq);
        mLlLoginCenter = (LinearLayout) findViewById(R.id.ll_login_center);
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
        mLlMain.setOnClickListener(this);
        mEtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(isEtPasswordFocusBefore) {
                        
                    } else {
                        hideLoginTopAndOauth();
                    }
                    isEtPhoneFocusBefore = true;
                    isEtPasswordFocusBefore = false;
                } else {
                    
                }
            }
        });
        mEtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(isEtPhoneFocusBefore) {

                    } else {
                        hideLoginTopAndOauth();
                    }
                    isEtPasswordFocusBefore = true;
                    isEtPhoneFocusBefore = false;
                } else {
                    
                }
            }
        });
        tvLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
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

        if(isFirstTime) {
            initFirstY();
            isFirstTime = false;
        }
        
        mTvLoginTitle.setVisibility(View.VISIBLE);
        
        AlphaAnimation animation = new AlphaAnimation(1.0f,0.1f);
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());
        
        mRlLoginTop.setVisibility(View.GONE);
        mLlOauth.setVisibility(View.GONE);
        
        mRlLoginTop.startAnimation(animation);
        mLlOauth.startAnimation(animation);

        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,DensityUtil.px2dip(LoginActivity.this, mEtPhoneFirstY),48);
        translateAnimation.setDuration(300);
        translateAnimation.setInterpolator(new LinearInterpolator());
        mLlLoginCenter.startAnimation(translateAnimation);
    }

    public void showLoginTopAndOauth() {

        if(isSecondTime) {
            initSecondY();
            isSecondTime = false;
        }
        
        mTvLoginTitle.setVisibility(View.GONE);

        AlphaAnimation animation = new AlphaAnimation(0.1f,1.0f);
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());

        mRlLoginTop.setVisibility(View.VISIBLE);
        mLlOauth.setVisibility(View.VISIBLE);
        
        mRlLoginTop.startAnimation(animation);
        mLlOauth.startAnimation(animation);

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_main:
                if(mLlOauth.getVisibility() == View.GONE) {
                    isEtPhoneFocusBefore = false;
                    isEtPasswordFocusBefore = false;
                    showLoginTopAndOauth();
                    forceHideKeyboard(mEtPhone); //强制退出键盘
                }
                break;
            case R.id.tv_login:
                Toast.makeText(LoginActivity.this,"准备登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_forget_password:
                intent = new Intent();
                intent.setClass(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_out,R.anim.push_left_in);
                break;
            case R.id.tv_register:
                intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_out,R.anim.push_left_in);
                break;
            case R.id.ll_weibo:
                login(SHARE_MEDIA.SINA);
                break;
            case R.id.ll_weixin:
                Toast.makeText(LoginActivity.this,"准备微信登录",Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_qq:
                login(SHARE_MEDIA.QQ);
                break;
            default:
                break;
        }
    }

    /**
     * 授权。如果授权成功，则获取用户信息</br>
     */
    private void login(final SHARE_MEDIA platform) {
        mController.doOauthVerify(LoginActivity.this, platform, new SocializeListeners.UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA platform) {
                Toast.makeText(LoginActivity.this, "请稍候,正在连接...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                Toast.makeText(LoginActivity.this, "网络连接故障,请稍候再试...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Bundle value, SHARE_MEDIA platform) {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                String uid = value.getString("uid");
                if (!TextUtils.isEmpty(uid)) {
                    getUserInfo(platform);
                } else {
                    Toast.makeText(LoginActivity.this, "授权失败...", Toast.LENGTH_SHORT).show();
                }
                
                //接下来绑定账号
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,BindAccountActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }
        });
    }

    private void getUserInfo(SHARE_MEDIA platform) {
        mController.getPlatformInfo(LoginActivity.this, platform, new SocializeListeners.UMDataListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(int status, Map<String, Object> info) {
                //在这里获取用户的数据
                if (info != null) {
                    Toast.makeText(LoginActivity.this, info.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(System.currentTimeMillis() - mExitTime > 2000) {
                mExitTime = System.currentTimeMillis();
                Toast.makeText(LoginActivity.this,"两次点击退出",Toast.LENGTH_SHORT).show();
            } else {
                System.exit(0);
            }
        }
        return true;
    }
}
