package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.scutteam.lvyou.R;

public class CheckPhoneNumberActivity extends Activity implements View.OnClickListener {

    private ImageButton mIbtnBack;
    private TextView mTvPhone;
    private EditText mEtName;
    private EditText mEtPassword;
    private TextView mTvRegister;
    private String phone;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_phone_number);
        
        initData();
        initView();
        initListener();
    }
    
    public void initData() {
        phone = getIntent().getStringExtra("phone");
    }
    
    public void initView() {
        mIbtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mTvRegister = (TextView) findViewById(R.id.tv_register);

        mTvPhone.setText(phone);
    }
    
    public void initListener() {
        mIbtnBack.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.tv_register:
                //最终注册
                break;
        }
    }
}
