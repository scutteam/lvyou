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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
<<<<<<< HEAD
import com.scutteam.lvyou.constant.Constants;
=======
import com.scutteam.lvyou.dialog.DialogListener;
import com.scutteam.lvyou.dialog.SelectDayDialog;
>>>>>>> db2419f8da6b8cc33725e8e35ae6b066edeaab8e
import com.scutteam.lvyou.model.Destination;

import org.apache.http.Header;
import org.json.JSONObject;

<<<<<<< HEAD

=======
>>>>>>> db2419f8da6b8cc33725e8e35ae6b066edeaab8e
public class MakeJourneyActivity extends Activity implements View.OnClickListener {
    final int imageWidthRate = 4;                          //固定图片的宽高比例为4:3
    final int imageHeightRate = 3;
    private int memberNums = 6;                           //默认6人成团

    private ImageView destinationImage = null;            //目的地展示图片，最上方展示图片
    private TextView destinationName = null;              //目的地名称
    private RatingBar destinationStar = null;             //目的地获得星数对应的Bar
    private TextView destinationRatingNum = null;         //目的地获得星数对应的数值
    private TextView destinationDetail = null;           //目的地的详细描述
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
<<<<<<< HEAD
        refreshUi();
=======
>>>>>>> db2419f8da6b8cc33725e8e35ae6b066edeaab8e
    }

    private void initData() {
        destination_id = getIntent().getLongExtra("destination_id", 0L);
        destination = Destination.findDestinationById(destination_id);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id",destination_id);
        client.get(MakeJourneyActivity.this, Constants.URL + "main/dest.detail.json",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    /**
     * 初始化界面
     */
<<<<<<< HEAD
    private void initView(){
        TextView title = (TextView)findViewById(R.id.center_text);
        title.setText("定制您的团队行程");
        
        destinationImage = (ImageView)findViewById(R.id.mj_image);
=======
    private void initView() {
        TextView title = (TextView) findViewById(R.id.center_text);
        title.setText("定制您的团队行程");

        destinationImage = (ImageView) findViewById(R.id.mj_image);
>>>>>>> db2419f8da6b8cc33725e8e35ae6b066edeaab8e
        ViewGroup.LayoutParams params = destinationImage.getLayoutParams();   //设置ImageView横高比例为4:3
        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = (params.width * imageHeightRate) / imageWidthRate;
        destinationImage.setLayoutParams(params);
<<<<<<< HEAD
        
        selectBeginPlace = (TextView)findViewById(R.id.mj_select_begin_place);
        minusMemberNums = (TextView)findViewById(R.id.mj_member_minus);
        plusMemberNums = (TextView)findViewById(R.id.mj_member_plus);
        showMemberNums = (TextView)findViewById(R.id.mj_member_num);
        destinationName = (TextView) findViewById(R.id.mj_destination_name);
        destinationStar = (RatingBar) findViewById(R.id.mj_destination_star);
        destinationRatingNum = (TextView) findViewById(R.id.mj_destination_rate_num);
        destinationDetail = (TextView) findViewById(R.id.mj_destination_describe);
    }
    
    public void initListener() {
        destinationImage.setOnClickListener(this);
        selectBeginPlace.setOnClickListener(this);
        minusMemberNums.setOnClickListener(this);
        plusMemberNums.setOnClickListener(this);
    }
    
    public void refreshUi() {
        ImageLoader.getInstance().displayImage(destination.cover_pic,destinationImage);
        destinationName.setText(destination.title);
        destinationStar.setRating(Float.parseFloat(destination.score.toString()));
        destinationRatingNum.setText(destination.score.toString());
        destinationDetail.setText(destination.short_intro);
=======

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
>>>>>>> db2419f8da6b8cc33725e8e35ae6b066edeaab8e
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.mj_image:
                intent = new Intent(MakeJourneyActivity.this, DestinationDetailActivity.class);
<<<<<<< HEAD
                intent.putExtra("destination_id",destination_id);
                startActivity(intent);
                break;
=======
                startActivity(intent);
                break;

>>>>>>> db2419f8da6b8cc33725e8e35ae6b066edeaab8e
            case R.id.mj_select_begin_place:
                intent = new Intent(MakeJourneyActivity.this, SelectBeginPlaceActivity.class);
                startActivity(intent);
                break;
<<<<<<< HEAD
            case R.id.mj_member_minus:
                memberNums -= 1;
                showMemberNums.setText(memberNums + "人成团");
                break;
=======

>>>>>>> db2419f8da6b8cc33725e8e35ae6b066edeaab8e
            case R.id.mj_member_plus:
                memberNums += 1;
                showMemberNums.setText(memberNums + "人成团");
                break;
<<<<<<< HEAD
            
=======

            case R.id.mj_member_minus:
                memberNums -= 1;
                showMemberNums.setText(memberNums + "人成团");
                break;

            default:
                break;
>>>>>>> db2419f8da6b8cc33725e8e35ae6b066edeaab8e
        }
    }
}
