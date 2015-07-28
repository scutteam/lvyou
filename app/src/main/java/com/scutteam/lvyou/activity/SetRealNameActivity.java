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


public class SetRealNameActivity extends Activity implements View.OnClickListener {

    private ImageView mIvBack;
    private EditText mEtRealName;
    private TextView mTvSave;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case Constants.MODIFY_REAL_NAME_SUCCESS:
                    Toast.makeText(SetRealNameActivity.this,"设置真实姓名成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("realName",mEtRealName.getText().toString());
                    setResult(Constants.MODIFY_REAL_NAME_SUCCESS,intent);
                    finish();
                    break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_real_name);
        
        initView();
        initListener();
    }
    
    public void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mEtRealName = (EditText) findViewById(R.id.et_real_name);
        mTvSave = (TextView) findViewById(R.id.tv_save);

        if(LvYouApplication.real_name != null && LvYouApplication.real_name.length() > 0 && !LvYouApplication.real_name.equals("null")) {
            mEtRealName.setHint(LvYouApplication.real_name);
        }

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
                if(TextUtils.isEmpty(mEtRealName.getText())) {
                    Toast.makeText(SetRealNameActivity.this, "真实名字不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    SetRealName();
                }
                break;
            default:
                break;
        }        
    }
    
    public void SetRealName() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();
        param.put("sessionid", LvYouApplication.getSessionId());
        param.put("realName",mEtRealName.getText().toString());
        client.post(SetRealNameActivity.this, Constants.URL + "user/personal.modify_realname.do",param,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
               try {
                   int code = response.getInt("code");
                   if(code == 0) {
                       handler.sendEmptyMessage(Constants.MODIFY_REAL_NAME_SUCCESS);
                   } else {
                       Toast.makeText(SetRealNameActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
                   }
                           
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
}
