package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.scutteam.lvyou.R;

public class MyJourneyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_journey);
        initData();
        initView();
        initListener();
    }

    private void initData() {

    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.center_text);
        title.setText("我的行程:凤凰古城");
    }

    private void initListener() {
    }
}
