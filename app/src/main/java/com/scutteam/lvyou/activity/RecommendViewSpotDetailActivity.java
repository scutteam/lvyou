package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.adapter.RecommendViewSpotDetailAdapter;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.dialog.SelectRecommendAlertDialog;
import com.scutteam.lvyou.model.Recommendtrip;
import com.scutteam.lvyou.model.ViewSpot;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecommendViewSpotDetailActivity extends Activity implements View.OnClickListener {

    private Recommendtrip recommendtrip;
    private TextView tv_title;
    private ImageView iv_back;
    private ListView listView;
    private List<ViewSpot> viewSpotList = new ArrayList<ViewSpot>();
    private RecommendViewSpotDetailAdapter adapter;
    private View mHeadView;
    private String top_pic;
    
    private TextView tv_dest;
    private TextView tv_select;
    private TextView tv_dest_title;
    private TextView tv_play_num;
    private ImageView iv_background;
    private SelectRecommendAlertDialog dialog;
    
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case LOAD_DATA_SUCCESS:
                    initAdapter();
                    break;
            }
        }
    };
    private static final int LOAD_DATA_SUCCESS = 19999;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_view_spot_detail);
        
        initData();
        initView();
        initHeadView();
        loadData();
        initListener();
    }
    
    public void initData() {
        top_pic = getIntent().getStringExtra("top_pic");
        recommendtrip = (Recommendtrip) getIntent().getSerializableExtra("RecommendTrip");
    }
    
    public void initView() {
        listView = (ListView) findViewById(R.id.listView);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        tv_title.setText(recommendtrip.trip_title);
    }
    
    public void initHeadView() {
        mHeadView = LayoutInflater.from(RecommendViewSpotDetailActivity.this).inflate(R.layout.activity_recommend_view_spot_detail_head,null);
        tv_dest = (TextView) mHeadView.findViewById(R.id.tv_dest);
        tv_dest_title = (TextView) mHeadView.findViewById(R.id.tv_dest_title);
        tv_play_num = (TextView) mHeadView.findViewById(R.id.tv_play_num);
        iv_background = (ImageView) mHeadView.findViewById(R.id.iv_background);
        tv_select = (TextView) mHeadView.findViewById(R.id.tv_select);

        ImageLoader.getInstance().displayImage(Constants.IMAGE_URL + top_pic.split(";")[0],iv_background);
        tv_dest.setText(recommendtrip.dest);
        tv_dest_title.setText(recommendtrip.trip_title);
        tv_play_num.setText(recommendtrip.play_num + "个游玩项目");
        
        listView.addHeaderView(mHeadView);

        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new SelectRecommendAlertDialog(RecommendViewSpotDetailActivity.this,R.style.TransparentDialog);
                dialog.show();
                dialog.mTvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("trip",(Serializable)recommendtrip);
                        setResult(Constants.RESULT_GET_RECOMMENT_DETAIL,intent);
                        finish();
                    }
                });
                dialog.mTvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
    
    public void initAdapter() {
        adapter = new RecommendViewSpotDetailAdapter(viewSpotList,RecommendViewSpotDetailActivity.this);
        listView.setAdapter(adapter);
    }
    
    public void initListener() {
        iv_back.setOnClickListener(this);
    }
    
    public void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("kw.tripId",recommendtrip.trip_id);
        client.get(RecommendViewSpotDetailActivity.this, Constants.URL + "main/recommendtrip.detail.json",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        JSONArray dataArray = response.getJSONArray("data");
                        
                        for(int i = 0 ; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);

                            ViewSpot viewSpot = new ViewSpot();
                            viewSpot.title = dataObject.getString("showDay");
                            viewSpotList.add(viewSpot);
                            
                            JSONArray viewSpotArray = dataObject.getJSONArray("viewSpotList");
                            for(int j = 0 ; j < viewSpotArray.length() ;j++) {
                                ViewSpot viewSpot1 = ViewSpot.insertOrReplace(viewSpotArray.getJSONObject(j));

                                viewSpotList.add(viewSpot1);
                            }
                        }
                        
                        handler.sendEmptyMessage(LOAD_DATA_SUCCESS);
                        
                    } else {
                        Toast.makeText(RecommendViewSpotDetailActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
