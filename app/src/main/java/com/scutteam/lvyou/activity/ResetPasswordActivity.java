package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.util.ScreenManager;


public class ResetPasswordActivity extends Activity implements View.OnClickListener {

    private EditText mEtPassword;
    private EditText mEtPassword2;
    private ImageButton mIbtnBack;
    private TextView mTvSubmit;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ScreenManager.getScreenManager().addActivity(ResetPasswordActivity.this);
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ScreenManager.getScreenManager().finishActivity(ResetPasswordActivity.this);
    }

    public void initView() {
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mEtPassword2 = (EditText) findViewById(R.id.et_password2);
        mIbtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        mTvSubmit = (TextView) findViewById(R.id.tv_submit);
    }
    
    public void initListener() {
        mIbtnBack.setOnClickListener(this);
        mTvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_back:
                finish();
                break;
            case R.id.tv_submit:
                if(TextUtils.isEmpty(mEtPassword.getText()) ||TextUtils.isEmpty(mEtPassword2.getText())) {
                    Toast.makeText(ResetPasswordActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                } else if(!mEtPassword.getText().toString().equals(mEtPassword2.getText().toString())) {
                    Toast.makeText(ResetPasswordActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                   //提交
                    Toast.makeText(ResetPasswordActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                    ScreenManager.getScreenManager().finishAllActivity();
                }
                break;
            default:
                break;
        }
    }
}
