package com.scutteam.lvyou.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.fragment.AboutAppFragment;
import com.scutteam.lvyou.fragment.AboutInsuranceFragment;
import com.scutteam.lvyou.fragment.DestinationFragment;
import com.scutteam.lvyou.fragment.MainFragment;
import com.scutteam.lvyou.fragment.MyPlanFragment;
import com.scutteam.lvyou.fragment.UserFragment;
import com.scutteam.lvyou.interfaces.ThemeListener;
import com.scutteam.lvyou.model.LvYouTheme;
import com.scutteam.lvyou.util.ScreenManager;
import com.scutteam.lvyou.widget.CircleImageView;

import org.apache.http.Header;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements View.OnClickListener,ThemeListener{

    private RelativeLayout mRlAvatarLayout;
    private CircleImageView mCivAvatar;
    private TextView mTvName;
    private TextView mTvMain;
    private TextView mTvDestination;
    private TextView mTvMyPlan;
    private TextView mTvInsurance;
    private TextView mTvAbout;
    private TextView mTvLogout;
    private TextView mTvTitle;

    private DrawerLayout mDrawerLayout;

    private ImageView mIvDrawer;

    private long mExitTime = 0L;
    private String screen_name;
    private String profile_image_url;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private MainFragment mainFragment;
    private DestinationFragment destinationFragment;
    private AboutAppFragment aboutAppFragment;
    private AboutInsuranceFragment aboutInsuranceFragment;

    public static final int FRAGMENT_MAIN = 1001;
    public static final int FRAGMENT_DESTINATION = 1002;
    public static final int FRAGMENT_PLAN = 1003;
    public static final int FRAGMENT_USER = 1004;
    public static final int FRAGMENT_ABOUT_APP = 1005;
    public static final int FRAGMENT_ABOUT_INSURANCE = 1006;
    public static final int LOGOUT_SUCCESS = 1005;

    public Drawable mainDrawable;
    public Drawable destinationDrawable;
    public Drawable planDrawable;
    public Drawable userDrawable;
    private ImageView mIvArrowRight;

    private UserFragment userFragment;
    private LvYouTheme selectedTheme;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGOUT_SUCCESS:
                    SharedPreferences sharedPreferences = getSharedPreferences("lvyou",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("phone","");
                    editor.putString("password","");
                    editor.commit();

                    LvYouApplication.setSessionId(null);
                    LvYouApplication.clearAllInfo();

                    mTvLogout.setVisibility(View.GONE);
                    mCivAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.default_icon_new));
                    mTvName.setText("点击登录");
                    mIvArrowRight.setVisibility(View.GONE);

                    switchFragment(FRAGMENT_MAIN);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScreenManager.getScreenManager().addActivity(MainActivity.this);
        initData();
        initMainFragment();
        initView();
        initListener();
        if(screen_name!=null && screen_name.length()>0) {
            initLeftDrawer();
            mIvArrowRight.setVisibility(View.VISIBLE);
        } else {
            mIvArrowRight.setVisibility(View.GONE);
        }

//        Intent intent = getIntent();
//        if(null != intent) {
//            int destinationFragment = intent.getIntExtra("destination_fragment", FRAGMENT_MAIN);
//            if(null != mDrawerLayout){
//                if(destinationFragment == FRAGMENT_PLAN){
//                    mDrawerLayout.closeDrawer(Gravity.LEFT);
//                    String sessionid = LvYouApplication.getSessionId();
//                    if(sessionid!=null && sessionid.length() > 0) {
//                        setTitle("我的行程");
//                        switchFragment(FRAGMENT_PLAN);
//                    } else {
//                        intent = new Intent();
//                        intent.setClass(MainActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
//                    }
//                }
//            }
//        }
        int destionationFragment = getIntent().getIntExtra("destination_fragment",FRAGMENT_MAIN);
        if(destionationFragment == FRAGMENT_PLAN) {
            setTitle("我的行程");
            switchFragment(FRAGMENT_PLAN);
        }

    }

    @Override
    protected void onDestroy() {
        Log.i("liujie", "onDestroy");
        ScreenManager.getScreenManager().finishActivity(MainActivity.this);

        super.onDestroy();
    }

    public void initData() {
        screen_name = LvYouApplication.getScreenName();
        profile_image_url = LvYouApplication.getImageProfileUrl();

        SharedPreferences sharedPreferences = getSharedPreferences("lvyou", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone","");
        String password = sharedPreferences.getString("password","");
        Log.e("haha","phone = "+phone+" password="+password);

        if(phone.length() > 0 && password.length() > 0) {
            startLogin(phone,password);
        }
    }

    public void startLogin(String phone,String password) {
        AsyncHttpClient client = new AsyncHttpClient();
//        client.addHeader("user-agent", "Android");
//        httpRequest.header("user-agent", "Android");
        RequestParams params = new RequestParams();
        params.put("phone",phone);
        params.put("pwd",password);
        client.post(MainActivity.this,Constants.URL+"user/mobilelogin.login.do",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int code = response.getInt("code");
                    String session_data = response.getString("data");

                    if(code == 0) {
                        Toast.makeText(MainActivity.this,"登录成功,正在获取用户信息",Toast.LENGTH_SHORT).show();
                        LvYouApplication.setSessionId(session_data);
                        getLoginInfo();
                    } else {
                        Toast.makeText(MainActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(MainActivity.this, "登录失败，请重试" ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getLoginInfo() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid",LvYouApplication.getSessionId());
        client.post(MainActivity.this,Constants.URL + "user/mobilelogin.info.do",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    LvYouApplication.setScreenName(dataObject.getString("nickName"));
                    LvYouApplication.setUserId(dataObject.getString("id"));
                    if(!dataObject.getString("faceIcon").equals("null")) {
                        LvYouApplication.setImageProfileUrl(Constants.IMAGE_URL + dataObject.getString("faceIcon"));
                    } else {
                        LvYouApplication.setImageProfileUrl(null);
                    }
                    screen_name = LvYouApplication.getScreenName();
                    profile_image_url = LvYouApplication.getImageProfileUrl();
                    initLeftDrawer();

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

    public void initMainFragment() {
        mainFragment = new MainFragment();
        mainFragment.setListener(this);
        destinationFragment = new DestinationFragment();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content,mainFragment);
        transaction.commit();
    }

    public void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvArrowRight = (ImageView) findViewById(R.id.iv_arrow_right);
        mRlAvatarLayout = (RelativeLayout) findViewById(R.id.rl_avatar_layout);
        mCivAvatar = (CircleImageView) findViewById(R.id.civ_avatar);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvMain = (TextView) findViewById(R.id.tv_main);
        mTvDestination = (TextView) findViewById(R.id.tv_destination);
        mTvMyPlan = (TextView) findViewById(R.id.tv_my_plan);
//        mTvUser = (TextView) findViewById(R.id.tv_user);
        mTvInsurance = (TextView) findViewById(R.id.tv_insurance);
        mTvAbout = (TextView) findViewById(R.id.tv_about);
        mTvLogout = (TextView) findViewById(R.id.tv_logout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mIvDrawer = (ImageView) findViewById(R.id.iv_drawer);
    }

    public void initListener() {

        mTvMain.setOnClickListener(this);
        mTvDestination.setOnClickListener(this);
        mTvMyPlan.setOnClickListener(this);
        mRlAvatarLayout.setOnClickListener(this);
//        mTvUser.setOnClickListener(this);

        mTvInsurance.setOnClickListener(this);
        mTvAbout.setOnClickListener(this);

        mTvLogout.setOnClickListener(this);

        mIvDrawer.setOnClickListener(this);
    }

    public void initLeftDrawer() {
        mTvName.setText(screen_name);
        if(profile_image_url!=null && profile_image_url.length() > 0 && !profile_image_url.equals("null")) {
            ImageLoader.getInstance().displayImage(profile_image_url,mCivAvatar);
        } else {
            ImageLoader.getInstance().displayImage(Constants.URL + "image/default_face.jpg", mCivAvatar);
        }
        mTvLogout.setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void switchFragment(int state) {
        switch (state) {
            case FRAGMENT_MAIN:
                transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
                transaction.replace(R.id.content,mainFragment);
                transaction.commit();

                changeLeftDrawerItem(state);

                break;
            case FRAGMENT_DESTINATION:
                if(selectedTheme != null) {
//                    if(destinationFragment.themeList.size() > 0) {
//                        destinationFragment = new DestinationFragment();
//                        Bundle data = new Bundle();
//                        data.putLong("selected_theme_id", selectedTheme.theme_id);
//                        destinationFragment.setArguments(data);
//                    } else {
                        destinationFragment = new DestinationFragment();
                        Bundle data = new Bundle();
                        data.putLong("selected_theme_id", selectedTheme.theme_id);
                        destinationFragment.setArguments(data);
//                    }
                } else {
                    destinationFragment = new DestinationFragment();
                }
                transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
                transaction.replace(R.id.content,destinationFragment);
                transaction.commit();

                changeLeftDrawerItem(state);
                break;
            case FRAGMENT_PLAN:
                //先判断用户是否登录
                MyPlanFragment myPlanFragment = new MyPlanFragment();
                transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
                transaction.replace(R.id.content,myPlanFragment);
                transaction.commit();

                changeLeftDrawerItem(state);
                break;
            case FRAGMENT_USER:
                //先判断用户是否登录
                userFragment = new UserFragment();
                transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.push_left_in,R.anim.push_right_out);
                transaction.replace(R.id.content,userFragment);
                transaction.commit();

                changeLeftDrawerItem(state);
                break;
            case FRAGMENT_ABOUT_APP:
                aboutAppFragment = AboutAppFragment.getInstance();
                transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
                transaction.replace(R.id.content,aboutAppFragment);
                transaction.commit();

                changeLeftDrawerItem(state);
                break;
            case FRAGMENT_ABOUT_INSURANCE:
                aboutInsuranceFragment = AboutInsuranceFragment.getInstance();
                transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
                transaction.replace(R.id.content,aboutInsuranceFragment);
                transaction.commit();

                changeLeftDrawerItem(state);
                break;
            default:
                break;
        }
    }

    public void changeLeftDrawerItem(int state) {
        switch (state) {
            case FRAGMENT_MAIN:
                mainDrawable = getResources().getDrawable(R.mipmap.star);
                mainDrawable.setBounds(0, 0, mainDrawable.getMinimumWidth(), mainDrawable.getMinimumHeight());
                mTvMain.setCompoundDrawables(mainDrawable, null, null, null); //设置左图标
                mTvMain.setTextColor(getResources().getColor(R.color.image_color));

                destinationDrawable = getResources().getDrawable(R.mipmap.destination_unselected);
                destinationDrawable.setBounds(0, 0, destinationDrawable.getMinimumWidth(), destinationDrawable.getMinimumHeight());
                mTvDestination.setCompoundDrawables(destinationDrawable, null, null, null); //设置左图标
                mTvDestination.setTextColor(getResources().getColor(R.color.main_text_color));

                planDrawable = getResources().getDrawable(R.mipmap.package_unselected);
                planDrawable.setBounds(0, 0, planDrawable.getMinimumWidth(), planDrawable.getMinimumHeight());
                mTvMyPlan.setCompoundDrawables(planDrawable, null, null, null); //设置左图标
                mTvMyPlan.setTextColor(getResources().getColor(R.color.main_text_color));

                userDrawable = getResources().getDrawable(R.mipmap.user_unlogined);
                userDrawable.setBounds(0, 0, userDrawable.getMinimumWidth(), userDrawable.getMinimumHeight());
//                mTvUser.setCompoundDrawables(userDrawable, null, null, null); //设置左图标
//                mTvUser.setTextColor(getResources().getColor(R.color.main_text_color));
                break;
            case FRAGMENT_DESTINATION:
                mainDrawable = getResources().getDrawable(R.mipmap.star_unselected);
                mainDrawable.setBounds(0, 0, mainDrawable.getMinimumWidth(), mainDrawable.getMinimumHeight());
                mTvMain.setCompoundDrawables(mainDrawable, null, null, null); //设置左图标
                mTvMain.setTextColor(getResources().getColor(R.color.main_text_color));

                destinationDrawable = getResources().getDrawable(R.mipmap.destination_selected);
                destinationDrawable.setBounds(0, 0, destinationDrawable.getMinimumWidth(), destinationDrawable.getMinimumHeight());
                mTvDestination.setCompoundDrawables(destinationDrawable, null, null, null); //设置左图标
                mTvDestination.setTextColor(getResources().getColor(R.color.image_color));

                planDrawable = getResources().getDrawable(R.mipmap.package_unselected);
                planDrawable.setBounds(0, 0, planDrawable.getMinimumWidth(), planDrawable.getMinimumHeight());
                mTvMyPlan.setCompoundDrawables(planDrawable, null, null, null); //设置左图标
                mTvMyPlan.setTextColor(getResources().getColor(R.color.main_text_color));

                userDrawable = getResources().getDrawable(R.mipmap.user_unlogined);
                userDrawable.setBounds(0, 0, userDrawable.getMinimumWidth(), userDrawable.getMinimumHeight());
//                mTvUser.setCompoundDrawables(userDrawable, null, null, null); //设置左图标
//                mTvUser.setTextColor(getResources().getColor(R.color.main_text_color));
                break;
            case FRAGMENT_PLAN:
                mainDrawable = getResources().getDrawable(R.mipmap.star_unselected);
                mainDrawable.setBounds(0, 0, mainDrawable.getMinimumWidth(), mainDrawable.getMinimumHeight());
                mTvMain.setCompoundDrawables(mainDrawable, null, null, null); //设置左图标
                mTvMain.setTextColor(getResources().getColor(R.color.main_text_color));

                destinationDrawable = getResources().getDrawable(R.mipmap.destination_unselected);
                destinationDrawable.setBounds(0, 0, destinationDrawable.getMinimumWidth(), destinationDrawable.getMinimumHeight());
                mTvDestination.setCompoundDrawables(destinationDrawable, null, null, null); //设置左图标
                mTvDestination.setTextColor(getResources().getColor(R.color.main_text_color));

                planDrawable = getResources().getDrawable(R.mipmap.package_selected);
                planDrawable.setBounds(0, 0, planDrawable.getMinimumWidth(), planDrawable.getMinimumHeight());
                mTvMyPlan.setCompoundDrawables(planDrawable, null, null, null); //设置左图标
                mTvMyPlan.setTextColor(getResources().getColor(R.color.image_color));

                userDrawable = getResources().getDrawable(R.mipmap.user_unlogined);
                userDrawable.setBounds(0, 0, userDrawable.getMinimumWidth(), userDrawable.getMinimumHeight());
//                mTvUser.setCompoundDrawables(userDrawable, null, null, null); //设置左图标
//                mTvUser.setTextColor(getResources().getColor(R.color.main_text_color));
                break;
            case FRAGMENT_USER:
                mainDrawable = getResources().getDrawable(R.mipmap.star_unselected);
                mainDrawable.setBounds(0, 0, mainDrawable.getMinimumWidth(), mainDrawable.getMinimumHeight());
                mTvMain.setCompoundDrawables(mainDrawable, null, null, null); //设置左图标
                mTvMain.setTextColor(getResources().getColor(R.color.main_text_color));

                destinationDrawable = getResources().getDrawable(R.mipmap.destination_unselected);
                destinationDrawable.setBounds(0, 0, destinationDrawable.getMinimumWidth(), destinationDrawable.getMinimumHeight());
                mTvDestination.setCompoundDrawables(destinationDrawable, null, null, null); //设置左图标
                mTvDestination.setTextColor(getResources().getColor(R.color.main_text_color));

                planDrawable = getResources().getDrawable(R.mipmap.package_unselected);
                planDrawable.setBounds(0, 0, planDrawable.getMinimumWidth(), planDrawable.getMinimumHeight());
                mTvMyPlan.setCompoundDrawables(planDrawable, null, null, null); //设置左图标
                mTvMyPlan.setTextColor(getResources().getColor(R.color.main_text_color));

                userDrawable = getResources().getDrawable(R.mipmap.user);
                userDrawable.setBounds(0, 0, userDrawable.getMinimumWidth(), userDrawable.getMinimumHeight());
//                mTvUser.setCompoundDrawables(userDrawable, null, null, null); //设置左图标
//                mTvUser.setTextColor(getResources().getColor(R.color.image_color));
                break;
            case FRAGMENT_ABOUT_APP:
            case FRAGMENT_ABOUT_INSURANCE:
                mainDrawable = getResources().getDrawable(R.mipmap.star_unselected);
                mainDrawable.setBounds(0, 0, mainDrawable.getMinimumWidth(), mainDrawable.getMinimumHeight());
                mTvMain.setCompoundDrawables(mainDrawable, null, null, null); //设置左图标
                mTvMain.setTextColor(getResources().getColor(R.color.main_text_color));

                destinationDrawable = getResources().getDrawable(R.mipmap.destination_unselected);
                destinationDrawable.setBounds(0, 0, destinationDrawable.getMinimumWidth(), destinationDrawable.getMinimumHeight());
                mTvDestination.setCompoundDrawables(destinationDrawable, null, null, null); //设置左图标
                mTvDestination.setTextColor(getResources().getColor(R.color.main_text_color));

                planDrawable = getResources().getDrawable(R.mipmap.package_unselected);
                planDrawable.setBounds(0, 0, planDrawable.getMinimumWidth(), planDrawable.getMinimumHeight());
                mTvMyPlan.setCompoundDrawables(planDrawable, null, null, null); //设置左图标
                mTvMyPlan.setTextColor(getResources().getColor(R.color.main_text_color));

                userDrawable = getResources().getDrawable(R.mipmap.user_unlogined);
                userDrawable.setBounds(0, 0, userDrawable.getMinimumWidth(), userDrawable.getMinimumHeight());
//                mTvUser.setCompoundDrawables(userDrawable, null, null, null); //设置左图标
//                mTvUser.setTextColor(getResources().getColor(R.color.main_text_color));
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        String sessionid = null;
        switch (v.getId()) {
            case R.id.tv_main:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                setTitle("首页");
                switchFragment(FRAGMENT_MAIN);
                break;
            case R.id.tv_destination:
                selectedTheme = null;
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                setTitle("目的地");
                switchFragment(FRAGMENT_DESTINATION);
                break;
            case R.id.tv_my_plan:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                sessionid = LvYouApplication.getSessionId();
                if(sessionid!=null && sessionid.length() > 0) {
                    setTitle("我的行程");
                    switchFragment(FRAGMENT_PLAN);
                } else {
//                    Toast.makeText(MainActivity.this,"现在前往登录界面",Toast.LENGTH_SHORT).show();
                    intent = new Intent();
                    intent.setClass(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                }

                break;
//            case R.id.tv_user:
//                mDrawerLayout.closeDrawer(Gravity.LEFT);
//                sessionid = LvYouApplication.getSessionId();
//                if(sessionid!=null && sessionid.length() > 0) {
//                    setTitle("个人资料");
//                    switchFragment(FRAGMENT_USER);
//                } else {
//                    Toast.makeText(MainActivity.this,"现在前往登录界面",Toast.LENGTH_SHORT).show();
//
//                    intent = new Intent();
//                    intent.setClass(MainActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
//                }
//
//                break;
            case R.id.rl_avatar_layout:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                sessionid = LvYouApplication.getSessionId();
                if(sessionid!=null && sessionid.length() > 0) {
                    setTitle("个人资料");
                    switchFragment(FRAGMENT_USER);
                } else {
                    intent = new Intent();
                    intent.setClass(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_right_out);
                }
                break;
            case R.id.tv_insurance:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                setTitle("关于旅游保险");
                switchFragment(FRAGMENT_ABOUT_INSURANCE);
                break;
            case R.id.tv_about:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                setTitle("关于简游熊");
                switchFragment(FRAGMENT_ABOUT_APP);
                break;
            case R.id.tv_logout:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                logOut();
                break;
            case R.id.iv_drawer:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            default:
                break;
        }
    }

    public void logOut() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid",LvYouApplication.getSessionId());
        client.post(MainActivity.this,Constants.URL + "user/mobilelogin.logout.do",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Toast.makeText(MainActivity.this,"注销成功",Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessage(LOGOUT_SUCCESS);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(fragmentManager.getFragments().get(0) instanceof  MainFragment) {
                setTitle("首页");
                if(System.currentTimeMillis() - mExitTime > 2000) {
                    mExitTime = System.currentTimeMillis();
                    Toast.makeText(MainActivity.this, "再点一次退出", Toast.LENGTH_SHORT).show();
                } else {
                    ScreenManager.getScreenManager().AppExit(MainActivity.this);
                }
            } else {
                setTitle("首页");
                switchFragment(FRAGMENT_MAIN);
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Constants.MODIFY_NAME_SUCCESS) {
            LvYouApplication.setScreenName(data.getStringExtra("nickName"));

            userFragment.refreshUI();
        } else if (resultCode == Constants.MODIFY_REAL_NAME_SUCCESS) {
            LvYouApplication.setReal_name(data.getStringExtra("realName"));

            userFragment.refreshUI();
        } else if (resultCode == Constants.MODIFY_BIND_PHONE_SUCCESS) {
            LvYouApplication.setPhone(data.getStringExtra("phone"));

            userFragment.refreshUI();
        }
    }

    @Override
    public void onThemeItenClick(LvYouTheme theme) {
        selectedTheme = theme;
        setTitle("目的地");
        switchFragment(FRAGMENT_DESTINATION);
    }

    @Override
    public void onBackPressed() {
        Log.i("liujie", "here");
        super.onBackPressed();
    }
}
