package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.dialog.ShareDialog;

public class GetInsuranceActivity extends Activity implements View.OnClickListener{

    Context mContext;
    private RelativeLayout rlGetMyInsurance;             //获取我的保险
    private RelativeLayout rlGetPartnersInsurance;       //让团圆获取保险
    private TextView tvGetInsuranceHint;                 //获取保险下面的提示
    private TextView tvNums;                             //已经获取保险的人数
    private TextView tvMemberNone;                       //当还没人获取保险时显示的内容
    private ListView lvMembers;                          //已经获取保险的成员列表
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_insurance);
        mContext = GetInsuranceActivity.this;
        initView();
        initListener();
    }

    private void initView(){
        rlGetMyInsurance = (RelativeLayout)findViewById(R.id.get_insurance_mine);
        rlGetPartnersInsurance = (RelativeLayout)findViewById(R.id.get_insurance_partners);
        tvGetInsuranceHint = (TextView)findViewById(R.id.get_insurance_hint);
        tvNums = (TextView)findViewById(R.id.get_insurance_nums);
        lvMembers = (ListView)findViewById(R.id.get_insurance_menber);
        tvMemberNone = (TextView)findViewById(R.id.get_insurance_member_none);
    }

    private void initListener(){
        rlGetMyInsurance.setOnClickListener(this);
        rlGetPartnersInsurance.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.get_insurance_mine:
                break;

            case R.id.get_insurance_partners:
                ShareDialog shareDialog = new ShareDialog(mContext);
                shareDialog.show();
                break;

            default:
                break;
        }
    }
}
