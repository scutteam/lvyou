package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.dialog.DialogListener;
import com.scutteam.lvyou.dialog.SelectDayDialog;
import com.scutteam.lvyou.model.Destination;


public class MakeJourneyActivity extends Activity implements View.OnClickListener {
    final int imageWidthRate = 4;                          //固定图片的宽高比例为4:3
    final int imageHeightRate = 3;
    private int memberNums = 6;                           //默认6人成团

    private ImageView destinationImage = null;            //目的地展示图片，最上方展示图片
    private TextView destinationName = null;              //目的地名称
    private RatingBar destinationStar = null;             //目的地获得星数对应的Bar
    private TextView destinationRatingNum = null;         //目的地获得星数对应的数值
    private TextView destionationDetail = null;           //目的地的详细描述
    private TextView selectBeginPlace = null;             //选择出发地点
    private TextView minusMemberNums = null;              //减少一个团员
    private TextView plusMemberNums = null;               //增加一个团员
    private TextView showMemberNums = null;               //显示团员数量
    private TextView beginDay = null;                     //出发日期
    private TextView returnDay = null;                    //返回日期

    private Long destination_id; //目的地的id
    private Destination destination; //所选目的地
    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_journey);
        mContext = MakeJourneyActivity.this;
        initData();
        initView();
        initListener();
    }

    private void initData() {
        destination_id = getIntent().getLongExtra("destination_id", 0L);
        destination = Destination.findDestinationById(destination_id);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        TextView title = (TextView) findViewById(R.id.center_text);
        title.setText("定制您的团队行程");

        destinationImage = (ImageView) findViewById(R.id.mj_image);
        ViewGroup.LayoutParams params = destinationImage.getLayoutParams();   //设置ImageView横高比例为4:3
        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = (params.width * imageHeightRate) / imageWidthRate;
        destinationImage.setLayoutParams(params);

        selectBeginPlace = (TextView) findViewById(R.id.mj_select_begin_place);

        showMemberNums = (TextView) findViewById(R.id.mj_member_num);
        minusMemberNums = (TextView) findViewById(R.id.mj_member_minus);
        plusMemberNums = (TextView) findViewById(R.id.mj_member_plus);

        beginDay = (TextView) findViewById(R.id.mj_begin_day);
        returnDay = (TextView) findViewById(R.id.mj_return_day);


    }

    private void initListener() {
        destinationImage.setOnClickListener(this);
        minusMemberNums.setOnClickListener(this);
        plusMemberNums.setOnClickListener(this);
        selectBeginPlace.setOnClickListener(this);

        //出发日期跟返程日期
        View.OnClickListener selectDayClickedListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("#########");
                SelectDayDialog dialog = new SelectDayDialog(mContext, new DialogListener() {
                    @Override
                    public void refreshActivity(String text) {
                    }
                });
                dialog.show();
            }
        };

        beginDay.setOnClickListener(selectDayClickedListener);
        returnDay.setOnClickListener(selectDayClickedListener);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.mj_image:
                intent = new Intent(MakeJourneyActivity.this, DestinationDetailActivity.class);
                startActivity(intent);
                break;

            case R.id.mj_select_begin_place:
                intent = new Intent(MakeJourneyActivity.this, SelectBeginPlaceActivity.class);
                startActivity(intent);
                break;

            case R.id.mj_member_plus:
                memberNums += 1;
                showMemberNums.setText(memberNums + "人成团");
                break;

            case R.id.mj_member_minus:
                memberNums -= 1;
                showMemberNums.setText(memberNums + "人成团");
                break;

            default:
                break;
        }
    }

}
