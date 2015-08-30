package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.adapter.MemberAdapter;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.dialog.ShareDialog;
import com.scutteam.lvyou.model.Member;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetInsuranceActivity extends Activity implements View.OnClickListener{

    Context mContext;
    private RelativeLayout rlGetMyInsurance;             //获取我的保险
    private RelativeLayout rlGetPartnersInsurance;       //让团圆获取保险
    private TextView tvGetInsuranceHint;                 //获取保险下面的提示
    private TextView tvNums;                             //已经获取保险的人数
    private TextView tvMemberNone;                       //当还没人获取保险时显示的内容
    private XListView lvMembers;                          //已经获取保险的成员列表
    private String planLogicId;
    private String title;
    private String order_num;
    private String create_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_insurance);
        mContext = GetInsuranceActivity.this;
        initView();
        initData();
        initListener();
    }

    private void initData() {
        Intent intent = getIntent();
        if(null != intent){
            planLogicId = intent.getStringExtra("plan_logic_id");
            title = intent.getStringExtra("title");
            order_num = intent.getStringExtra("order_num");
            create_time = intent.getStringExtra("create_time");
        }
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("planId", planLogicId);
        client.post(Constants.URL + "/user/insurance.buy_list.json", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("liujie", response.toString());
                if (0 == response.optInt("code")) {
                    ArrayList<Member> members = Member.fromJson(response.optJSONArray("data"));
                    if(members.size() == 0){
                        tvMemberNone.setVisibility(View.VISIBLE);
                    }else {
                        tvMemberNone.setVisibility(View.GONE);
                        tvNums.setText("团队获取保险情况(" + members.size() + "/12)");
                        MemberAdapter adapter = new MemberAdapter(mContext, members);
                        lvMembers.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i("liujie", responseString.toString());
                Toast.makeText(mContext, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(){
        TextView title = (TextView) findViewById(R.id.center_text);
        title.setText("获取保险");
        rlGetMyInsurance = (RelativeLayout)findViewById(R.id.get_insurance_mine);
        rlGetPartnersInsurance = (RelativeLayout)findViewById(R.id.get_insurance_partners);
        tvGetInsuranceHint = (TextView)findViewById(R.id.get_insurance_hint);
        tvNums = (TextView)findViewById(R.id.get_insurance_nums);
        lvMembers = (XListView)findViewById(R.id.get_insurance_member);
        tvMemberNone = (TextView)findViewById(R.id.get_insurance_member_none);

        lvMembers.setPullLoadEnable(false);
        lvMembers.setPullRefreshEnable(false);
    }

    private void initListener(){
        findViewById(R.id.left_icon).setOnClickListener(this);
        rlGetMyInsurance.setOnClickListener(this);
        rlGetPartnersInsurance.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left_icon:
                finish();
                break;

            case R.id.get_insurance_mine:
                Intent intent = new Intent(mContext, GetMyInsuranceActivity.class);
                intent.putExtra("plan_logic_id", planLogicId);
                intent.putExtra("title", title);
                intent.putExtra("order_num", order_num);
                intent.putExtra("create_time", create_time);
                startActivityForResult(intent, 0);
                break;

            case R.id.get_insurance_partners:
                ShareDialog shareDialog = new ShareDialog(mContext);
                shareDialog.setPlanLogicId(planLogicId);
                shareDialog.show();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == GetMyInsuranceActivity.RESULT_GET){
            initData();
        }
    }
}
