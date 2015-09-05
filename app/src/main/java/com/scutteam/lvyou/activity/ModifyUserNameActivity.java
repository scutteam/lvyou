package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class ModifyUserNameActivity extends Activity implements View.OnClickListener {

    private String name;
    private ImageView mIvBack;
    private TextView mTvName;
    private EditText mEtName;
    private TextView mTvSave;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case Constants.MODIFY_NAME_SUCCESS:
                    Toast.makeText(ModifyUserNameActivity.this,"用户名修改成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("nickName",mEtName.getText().toString());
                    setResult(Constants.MODIFY_NAME_SUCCESS,intent);
                    finish();
                    break;
            }
        }
    };
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_name);

        initView();
        initListenr();
    }
    
    public void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvName = (TextView) findViewById(R.id.tv_name);
        mEtName = (EditText) findViewById(R.id.et_name);
        mTvSave = (TextView) findViewById(R.id.tv_save);

        mTvName.setText(LvYouApplication.screen_name);
    }
    
    public void initListenr() {
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
                if(TextUtils.isEmpty(mEtName.getText())) {
                    Toast.makeText(ModifyUserNameActivity.this,"新的用户名不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    //修改用户名
                    modifyUserName();
                }
                break;
            default:
                break;
        }
    }
    
    public void modifyUserName() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("nickName",mEtName.getText().toString());
        client.post(ModifyUserNameActivity.this, Constants.URL + "user/personal.modify_nickname.do",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    int code = response.getInt("code");   
                    if(code == 0) {
                        handler.sendEmptyMessage(Constants.MODIFY_NAME_SUCCESS);
                    } else {
                        Toast.makeText(ModifyUserNameActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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
