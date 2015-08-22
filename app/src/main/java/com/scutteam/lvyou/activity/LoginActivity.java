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
import android.view.KeyEvent;
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
    private boolean isQQ = false;
    private boolean isWeibo = false;
    private boolean isWeixin = false;
    private String screen_name;
    private String profile_image_url;
    private ImageButton mIbtnBack;
    
    private Boolean is_back; //重置密码之后会回来这里 这个时候 不给返回
    private Boolean is_request_login = false;
    
    public static final int LOGIN_SUCCESS = 10000;
    public static final int LOGIN_FAIL = 10001;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    if(!is_request_login) {
                        //准备登录
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);    
                    } else {
                        setResult(Constants.RESULT_LOGIN);
                        finish();
                    }

                    break;
                case LOGIN_FAIL:
                    break;
                default:
                    break;
            }
        }
    };
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ScreenManager.getScreenManager().addActivity(LoginActivity.this);

        initData();
        initUmeng();
        initView();
        initListener();
    }
    
    public void initData() {
        is_back = getIntent().getBooleanExtra("is_back",false);
        is_request_login = getIntent().getBooleanExtra("is_request_login",false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ScreenManager.getScreenManager().finishActivity(LoginActivity.this);
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
        mIbtnBack = (ImageButton) findViewById(R.id.ibtn_back);
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
                    if (isEtPasswordFocusBefore) {

                    } else {
                        hideLoginTopAndOauth();
                    }
                    isEtPhoneFocusBefore = true;
                    isEtPasswordFocusBefore = false;
                } else {

                }
            }
        });
        mIbtnBack.setOnClickListener(this);
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
    
    public void getLoginInfo() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid",LvYouApplication.getSessionId());
        client.post(LoginActivity.this,Constants.URL + "user/mobilelogin.info.do",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Toast.makeText(LoginActivity.this,"获取用户信息成功",Toast.LENGTH_SHORT).show();
                    JSONObject dataObject = response.getJSONObject("data");
                    LvYouApplication.setScreenName(dataObject.getString("nickName"));
                    if(!dataObject.getString("faceIcon").equals("null")) {
                        LvYouApplication.setImageProfileUrl(Constants.IMAGE_URL + dataObject.getString("faceIcon"));
                    } else {
                        LvYouApplication.setImageProfileUrl(null);
                    }

                    Message message = Message.obtain();
                    message.what = LOGIN_SUCCESS;
                    handler.sendMessage(message);
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
    
    public void startLogin() {
        String phone = mEtPhone.getText().toString();
        String password = mEtPassword.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();
//        client.addHeader("user-agent", "Android");
//        httpRequest.header("user-agent", "Android");
        RequestParams params = new RequestParams();
        params.put("phone",phone);
        params.put("pwd",password);
        client.post(LoginActivity.this,Constants.URL+"user/mobilelogin.login.do",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    Log.e("response",response.toString());
                    int code = response.getInt("code");
                    String session_data = response.getString("data");
                    
                    if(code == 0) {
                        Toast.makeText(LoginActivity.this,"登录成功,正在获取用户信息",Toast.LENGTH_SHORT).show();
                        LvYouApplication.setSessionId(session_data);
                        getLoginInfo();
                    } else if(code == 1) {
                        Toast.makeText(LoginActivity.this,"手机号码不存在",Toast.LENGTH_SHORT).show();
                        mEtPhone.setText("");
                    } else if(code == 2) {
                        Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                        mEtPassword.setText("");
                    } else if(code == 9) {
                        Toast.makeText(LoginActivity.this,"发现异常,请稍后重试",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Log.e("response_fail",responseString);
            }
        });
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
            case R.id.ibtn_back:
                if(!is_back) {
                    finish();
                }
                break;
            case R.id.tv_login:
                if(TextUtils.isEmpty(mEtPhone.getText())) {
                    Toast.makeText(LoginActivity.this,"手机号码不能为空",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(mEtPassword.getText())) {
                    Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    startLogin();
                }
                break;
            case R.id.tv_forget_password:
                intent = new Intent();
                intent.setClass(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                break;
            case R.id.tv_register:
                intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
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
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
            }
        });
    }

    private void getUserInfo(final SHARE_MEDIA platform) {
        mController.getPlatformInfo(LoginActivity.this, platform, new SocializeListeners.UMDataListener() {

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
                    intent.setClass(LoginActivity.this,BindAccountActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(!is_back) {
            return super.onKeyDown(keyCode, event);
        } else {
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                return true;//不让返回
            } else {
                return super.onKeyDown(keyCode, event);
            }   
        }
    }
}
