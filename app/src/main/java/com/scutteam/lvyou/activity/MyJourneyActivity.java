package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.model.Plan;

public class MyJourneyActivity extends Activity {

    private TextView tvTitle;
    private TextView tvOrderNum;
    private TextView tvCreateTime;
    private TextView tvBeginPlace;
    private TextView tvDate;
    private TextView tvMemberNum;
    private TextView tvPlayItemCount;
    private TextView tvUnitPrice;
    private String planLogicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_journey);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            planLogicId = intent.getStringExtra("plan_logic_id");
            Log.i("logic plan id", planLogicId);
        }
        if (null != planLogicId) {
            /* 发请求 */

        }
    }

    private void initView() {
 /*
 TextView title = (TextView) findViewById(R.id.center_text);
 title.setText("我的行程:" + plan.title);

 tvTitle = (TextView) findViewById(R.id.my_journey_title);
 tvOrderNum = (TextView) findViewById(R.id.my_journey_order_num);
 tvCreateTime = (TextView) findViewById(R.id.my_journey_create_time);
 tvBeginPlace = (TextView) findViewById(R.id.my_journey_begin_place);
 tvDate = (TextView) findViewById(R.id.my_journey_date);
 tvMemberNum = (TextView) findViewById(R.id.my_journey_member_num);
 tvPlayItemCount = (TextView) findViewById(R.id.my_journey_play_item_count);
 tvUnitPrice = (TextView) findViewById(R.id.my_journey_price);

 tvTitle.setText(plan.title);
 tvOrderNum.setText("订单号:" + plan.order_num);
 tvCreateTime.setText("创建于:" + plan.create_time);
 if (null == plan.area)
 tvBeginPlace.setText(plan.place);
 else
 tvBeginPlace.setText(plan.place + "(" + plan.area + ")");
 tvDate.setText(plan.start_date + "-" + plan.end_date);
 tvPlayItemCount.setText("暂时还没获取。。。。。。");
 tvUnitPrice.setText((int)plan.unit_price + "/人");
 */
    }
    

    private void initListener() {
    }
}
