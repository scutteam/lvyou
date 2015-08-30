package com.scutteam.lvyou.activity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.util.SpUtils;
import android.os.Handler;

public class SplashActivity extends Activity {
    private ImageView iv;
    private boolean isFirstLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        iv = (ImageView)findViewById(R.id.splash_iv);
        YoYo.with(Techniques.Pulse).duration(10000).playOn(iv);
        SpUtils sp = new SpUtils(this, Constants.Sp.PRE_NAME);
        isFirstLaunch = sp.getValue(R.string.sp_first_launch, true);
        startActivity(800);
    }

    /**
     * 延迟启动 Activity
     *
     * @param postTime       延迟的时间(ms)
     */
    public void startActivity(int postTime) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstLaunch) {
                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }  else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, postTime);
    }



}
