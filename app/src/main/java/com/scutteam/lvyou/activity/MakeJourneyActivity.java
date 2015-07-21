package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.scutteam.lvyou.R;


public class MakeJourneyActivity extends Activity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_journey);
        init();
    }

    /**
     * 初始化界面
     */
    private void init(){
        TextView title = (TextView)findViewById(R.id.center_text);
        title.setText("定制您的团队行程");

        destinationImage = (ImageView)findViewById(R.id.mj_image);
        ViewGroup.LayoutParams params = destinationImage.getLayoutParams();   //设置ImageView横高比例为4:3
        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = (params.width * imageHeightRate) / imageWidthRate;
        destinationImage.setLayoutParams(params);
        destinationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakeJourneyActivity.this, DestinationDetailActivity.class);
                startActivity(intent);
            }
        });

        selectBeginPlace = (TextView)findViewById(R.id.mj_select_begin_place);
        selectBeginPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakeJourneyActivity.this, SelectBeginPlaceActivity.class);
                startActivity(intent);
            }
        });

        minusMemberNums = (TextView)findViewById(R.id.mj_member_minus);
        plusMemberNums = (TextView)findViewById(R.id.mj_member_plus);
        showMemberNums = (TextView)findViewById(R.id.mj_member_num);
        minusMemberNums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberNums -= 1;
                showMemberNums.setText(memberNums + "人成团");
            }
        });
        plusMemberNums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberNums += 1;
                showMemberNums.setText(memberNums + "人成团");
            }
        });

    }

}
