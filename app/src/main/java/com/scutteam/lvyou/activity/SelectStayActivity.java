package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.adapter.HotelAdapter;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.Hotel;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;

import java.util.ArrayList;

public class SelectStayActivity extends Activity implements XListView.IXListViewListener, View.OnClickListener {

    private ArrayList<Hotel> hotelList = new ArrayList<Hotel>();
    private HotelAdapter adapter;
    private XListView mListView;
    private Hotel[]hotelArray;
    private ImageView mIvBack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stay);
        
        initData();
        initView();
        initListener();
    }
    
    public void initData() {
        hotelList = (ArrayList<Hotel>) getIntent().getSerializableExtra("hotel");
        
    }
    
    public void initView() {
        mListView = (XListView) findViewById(R.id.listView);
        adapter = new HotelAdapter(SelectStayActivity.this,hotelList);
        mListView.setAdapter(adapter);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
    }
    
    public void initListener() {
        mListView.setXListViewListener(this);
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(false);
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            for(int i = 0 ; i < hotelList.size();i++) {
                if(hotelList.get(i).is_select == 1) {
                    intent.putExtra("select_hotel", hotelList.get(i).hotel_id);
                    setResult(Constants.RESULT_SELECT_STAY, intent);
                }
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intent = new Intent();
                for(int i = 0 ; i < hotelList.size();i++) {
                    if(hotelList.get(i).is_select == 1) {
                        intent.putExtra("select_hotel", hotelList.get(i).hotel_id);
                        setResult(Constants.RESULT_SELECT_STAY, intent);
                    }
                }
                finish();
                break;
        }
    }
}
