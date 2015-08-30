package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.dialog.ShareDialog;

public class ShareJourneyActivity extends Activity implements View.OnClickListener{
    private TextView tvTitle;
    private TextView tvOrderNum;
    private TextView tvCreateTime;
    private TextView tvBeginPlace;
    private TextView tvDate;
    private TextView tvMemberNum;
    private TextView tvStay;
    private TextView tvMeal;
    private TextView tvTransport;
    private TextView tvGuide;
    private TextView tvInsurance;
    private TextView tvPlayItemCount;
    private TextView tvPlayItemState;
    private TextView tvShare;
    private ListView lvItemList;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_journey);
        mContext = ShareJourneyActivity.this;
        initData();
        initView();
        initListener();
    }

    private void initData() {

    }

    private void initView() {
        tvTitle = (TextView)findViewById(R.id.share_journey_title);
        tvOrderNum = (TextView)findViewById(R.id.share_journey_order_num);
        tvCreateTime = (TextView)findViewById(R.id.share_journey_create_time);
        tvBeginPlace = (TextView)findViewById(R.id.share_journey_start_place);
        tvDate = (TextView)findViewById(R.id.share_journey_date);
        tvMemberNum = (TextView)findViewById(R.id.share_journey_member_num);
        tvStay = (TextView)findViewById(R.id.share_journey_stay);
        tvMeal = (TextView)findViewById(R.id.share_journey_meal);
        tvTransport = (TextView)findViewById(R.id.share_journey_transport);
        tvGuide = (TextView)findViewById(R.id.share_journey_guide);
        tvInsurance = (TextView)findViewById(R.id.share_journey_insurance);
        tvPlayItemCount = (TextView)findViewById(R.id.share_journey_play_item_count);
        tvPlayItemState = (TextView)findViewById(R.id.share_journey_play_item_state);
        tvShare = (TextView)findViewById(R.id.share_journey_share);
        lvItemList = (ListView)findViewById(R.id.share_journey_play_item_list);
    }

    private void initListener() {
        tvShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.share_journey_share:
                ShareDialog dialog = new ShareDialog(mContext);
                dialog.show();
                break;

            default:
                break;
        }
    }
}
