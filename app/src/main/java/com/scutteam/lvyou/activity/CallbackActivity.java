package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;

import org.apache.http.Header;
import org.json.JSONObject;

public class CallbackActivity extends Activity {
    private ImageView ivHeadImage;      //最上面的图片
    private TextView tvTitle;           //行程的标题
    private TextView tvOrderNum;        //订单号
    private TextView tvCreateTime;      //创建时间
    private TextView tvSubmit;          //提交
    private EditText etContent;         //评价的文字
    private RatingBar rbGuide;          //导游评价
    private RatingBar rbMeal;           //包餐服务
    private RatingBar rbStay;           //酒店住宿
    private RatingBar rbAgency;         //旅行社服务
    private RatingBar rbAll;            //旅程整体评价

    private String planLogicId;
    private String destinationLogicId;
    private String imageUrl;
    private String title;
    private String orderNum;
    private String createTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        initData();
        findView();
        initListener();
    }

    private void initData() {
        Intent intent = getIntent();
        if (null != intent) {
            planLogicId = intent.getStringExtra("plan_logic_id");
            destinationLogicId = intent.getStringExtra("dest_id");
            imageUrl = Constants.IMAGE_URL + intent.getStringExtra("image_url");
            title = intent.getStringExtra("title");
            orderNum = intent.getStringExtra("order_num");
            createTime = intent.getStringExtra("create_time");
        }
    }

    private void findView() {

        ivHeadImage = (ImageView) findViewById(R.id.callback_image);
        tvTitle = (TextView) findViewById(R.id.callback_title);
        tvOrderNum = (TextView) findViewById(R.id.callback_order_num);
        tvCreateTime = (TextView) findViewById(R.id.callback_create_time);
        tvSubmit = (TextView) findViewById(R.id.callback_submit);
        etContent = (EditText) findViewById(R.id.callback_content);
        rbGuide = (RatingBar) findViewById(R.id.callback_guide_star);
        rbMeal = (RatingBar) findViewById(R.id.callback_meal_star);
        rbStay = (RatingBar) findViewById(R.id.callback_stay_star);
        rbAgency = (RatingBar) findViewById(R.id.callback_agency_star);
        rbAll = (RatingBar) findViewById(R.id.callback_all_star);

        ((TextView) findViewById(R.id.center_text)).setText("反馈评价");
        tvTitle.setText(title);
        tvCreateTime.setText(createTime);
        tvOrderNum.setText(orderNum);
        ImageLoader.getInstance().displayImage(imageUrl, ivHeadImage);
    }

    private void initListener() {
        findViewById(R.id.left_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSubmit();
            }
        });
    }

    private void doSubmit() {
        String content = etContent.getText().toString();
        if(!"".equals(content)){
            Log.i("liujie", planLogicId + "");
            Log.i("liujie", destinationLogicId + "");
            Log.i("liujie", (long)rbAll.getNumStars() + "");
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("sessionid", LvYouApplication.getSessionId());
            params.put("comment.planId", planLogicId);
            params.put("comment.destId", destinationLogicId);
            params.put("comment.totalScore", (long)rbAll.getNumStars());
            params.put("comment.totalComment", content);
            params.put("comment.hotelScore", (long)rbStay.getNumStars());
            params.put("comment.guideScore", (long)rbGuide.getNumStars());
            params.put("comment.mealScore", (long)rbMeal.getNumStars());
            params.put("comment.serviceScore", (long)rbAgency.getNumStars());
            client.post(Constants.URL + "/main/comment.submit_comment.do", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    android.util.Log.i("liujie", response.toString());
                    if (0 == response.optInt("code")) {
                        Toast.makeText(LvYouApplication.getInstance(), "提交成功,感谢您的反馈", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(LvYouApplication.getInstance(), response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    android.util.Log.i("liujie", responseString.toString());
                    Toast.makeText(LvYouApplication.getInstance(), "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(LvYouApplication.getInstance(), "请输入反馈内容", Toast.LENGTH_SHORT).show();
        }
    }
}
