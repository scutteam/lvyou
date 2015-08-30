package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.os.Bundle;
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

public class ModifyPasswordActivity extends Activity implements View.OnClickListener {
    
    private ImageView mIvBack;
    private EditText mEtOldPassword;
    private EditText mEtNewPassword1;
    private EditText mEtNewPassword2;
    private TextView mTvSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        
        initView();
        initListener();
    }
    
    public void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mEtOldPassword = (EditText) findViewById(R.id.et_old_password);
        mEtNewPassword1 = (EditText) findViewById(R.id.et_new_password);
        mEtNewPassword2 = (EditText) findViewById(R.id.et_new_password2);
        mTvSave = (TextView) findViewById(R.id.tv_save);
    }
    
    public void initListener() {
        mIvBack.setOnClickListener(this);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                if(TextUtils.isEmpty(mEtOldPassword.getText())) {
                    Toast.makeText(ModifyPasswordActivity.this,"旧密码不能为空",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(mEtNewPassword1.getText())) {
                    Toast.makeText(ModifyPasswordActivity.this,"新密码不能为空",Toast.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(mEtNewPassword2.getText())) {
                    Toast.makeText(ModifyPasswordActivity.this,"确定新密码不能为空",Toast.LENGTH_SHORT).show();
                } else if(!mEtNewPassword1.getText().toString().equals(mEtNewPassword2.getText().toString())) {
                    Toast.makeText(ModifyPasswordActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                } else {
                    startModifyPassword();
                }
                break;
            default:
                break;
        }
    }
    
    public void startModifyPassword() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();
        param.put("sessionid", LvYouApplication.getSessionId());
        param.put("customer.oldPassword",mEtOldPassword.getText().toString());
        param.put("customer.newPassword",mEtNewPassword1.getText().toString());
        client.post(ModifyPasswordActivity.this, Constants.URL + "user/personal.modify_pwd.do",param,new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    int code = response.getInt("code");
                    if(code == 0) {

                        Toast.makeText(ModifyPasswordActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ModifyPasswordActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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
