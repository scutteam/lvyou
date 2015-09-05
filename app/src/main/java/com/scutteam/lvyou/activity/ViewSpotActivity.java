package com.scutteam.lvyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.adapter.ViewSpotAdapter;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.fragment.AllViewSpotFragment;
import com.scutteam.lvyou.fragment.RecommendViewSpotFragment;
import com.scutteam.lvyou.fragment.SelectViewSpotFragment;
import com.scutteam.lvyou.interfaces.ViewSpotListener;
import com.scutteam.lvyou.model.ViewSpot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewSpotActivity extends FragmentActivity implements View.OnClickListener, ViewSpotListener{

    private ImageView mIvBack;
    private ViewPager mViewPager;
    private AllViewSpotFragment allViewSpotFragment;
    private SelectViewSpotFragment selectViewSpotFragment;
    private RecommendViewSpotFragment recommendViewSpotFragment;
    private ArrayList<ViewSpot> viewSpotList= new ArrayList<ViewSpot>();
    private ArrayList<ViewSpot> selectedViewSpotList= new ArrayList<ViewSpot>();
    private FragmentPagerAdapter adapter;
    String[] title = { "默认", "已选择", "推荐计划" };
    private TextView mTvDefault;
    private TextView mTvSelected;
    private TextView mTvRecommend;
    private ImageView mIvDefault;
    private ImageView mIvSelected;
    private ImageView mIvRecommend;
    private Long destination_id;
    
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_spot);
        
        initData();
        initView();
        initFragment();
        initAdapter();
        initListener();
    }
    
    public void initData() {
        selectedViewSpotList = (ArrayList<ViewSpot>) getIntent().getSerializableExtra("selectedViewSpot");
        viewSpotList = (ArrayList<ViewSpot>) getIntent().getSerializableExtra("viewSpot");
        destination_id = getIntent().getLongExtra("id",0L);
    }
    
    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvDefault = (TextView) findViewById(R.id.tv_default);
        mTvSelected = (TextView) findViewById(R.id.tv_selected);
        mTvRecommend = (TextView) findViewById(R.id.tv_recommend);
        mIvDefault = (ImageView) findViewById(R.id.iv_default);
        mIvSelected = (ImageView) findViewById(R.id.iv_selected);
        mIvRecommend = (ImageView) findViewById(R.id.iv_recommend);
    }
    
    public void initFragment() {
        allViewSpotFragment = new AllViewSpotFragment();
        allViewSpotFragment.viewSpotList = viewSpotList;
        allViewSpotFragment.adapter = new ViewSpotAdapter(ViewSpotActivity.this,viewSpotList);
        selectViewSpotFragment = new SelectViewSpotFragment();
        selectViewSpotFragment.selectedViewSpotList = selectedViewSpotList;
        selectViewSpotFragment.adapter = new ViewSpotAdapter(ViewSpotActivity.this,selectedViewSpotList);
        recommendViewSpotFragment = new RecommendViewSpotFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id",destination_id);
        recommendViewSpotFragment.setArguments(bundle);


        fragmentList.add(allViewSpotFragment);
        fragmentList.add(selectViewSpotFragment);
        fragmentList.add(recommendViewSpotFragment);
    }
    
    public void initAdapter() {
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
            
            
        };
        
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
                
                setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    
    public void setSelect(int position) {
        mTvDefault.setTextColor(getResources().getColor(R.color.main_text_color));
        mTvSelected.setTextColor(getResources().getColor(R.color.main_text_color));
        mTvRecommend.setTextColor(getResources().getColor(R.color.main_text_color));
        mIvDefault.setVisibility(View.INVISIBLE);
        mIvSelected.setVisibility(View.INVISIBLE);
        mIvRecommend.setVisibility(View.INVISIBLE);
        
        switch (position) {
            case 0:
                mTvDefault.setTextColor(getResources().getColor(R.color.image_color));
                mIvDefault.setVisibility(View.VISIBLE);
                break;
            case 1:
                mTvSelected.setTextColor(getResources().getColor(R.color.image_color));
                mIvSelected.setVisibility(View.VISIBLE);
                break;
            case 2:
                mTvRecommend.setTextColor(getResources().getColor(R.color.image_color));
                mIvRecommend.setVisibility(View.VISIBLE);
                break;
        }
    }
    
    public void initListener() {
        mIvBack.setOnClickListener(this);
        
        allViewSpotFragment.adapter.setListener(this);
        selectViewSpotFragment.adapter.setListener(this);

        mTvDefault.setOnClickListener(this);
        mTvSelected.setOnClickListener(this);
        mTvRecommend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                Intent intent =new Intent();
                intent.putExtra("selected_view_spot_list",(Serializable)selectedViewSpotList);
                setResult(Constants.RESULT_SELECT_VIEW_SPOT, intent);
                finish();
                break;
            case R.id.tv_default:
                mViewPager.setCurrentItem(0);
                setSelect(0);
                break;
            case R.id.tv_selected:
                mViewPager.setCurrentItem(1);
                setSelect(1);
                break;
            case R.id.tv_recommend:
                mViewPager.setCurrentItem(2);
                setSelect(2);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent();
        intent.putExtra("selected_view_spot_list",(Serializable)selectedViewSpotList);
        setResult(Constants.RESULT_SELECT_VIEW_SPOT, intent);
        finish();
    }

    @Override
    public void WhenViewSpotSelectIconClick(ViewSpot viewSpot) {
        if(viewSpot.is_select == 0) {
            viewSpot.is_select = 1;
            selectedViewSpotList.add(viewSpot);

            for(int i = 0; i < allViewSpotFragment.viewSpotList.size(); i++) {
                long id = allViewSpotFragment.viewSpotList.get(i).view_spot_id;
                if(id == viewSpot.view_spot_id) {
                    allViewSpotFragment.viewSpotList.get(i).is_select = 1;
                    break;
                }
            }
            
        } else {
            viewSpot.is_select = 0;
            for(int i = 0 ; i < selectedViewSpotList.size(); i++) {
                long id = selectedViewSpotList.get(i).view_spot_id;
                if(id == viewSpot.view_spot_id) {
                    selectedViewSpotList.remove(i);
                    break;
                }
            }
            for(int i = 0; i < allViewSpotFragment.viewSpotList.size(); i++) {
                long id = allViewSpotFragment.viewSpotList.get(i).view_spot_id;
                if(id == viewSpot.view_spot_id) {
                    allViewSpotFragment.viewSpotList.get(i).is_select = 0;
                    break;
                }
            }
        }
        selectViewSpotFragment.selectedViewSpotList = selectedViewSpotList;
        selectViewSpotFragment.adapter.notifyDataSetChanged();
        allViewSpotFragment.adapter.notifyDataSetChanged();
    }

    @Override
    public void WhenViewSpotSelectIconClickSetFocus(ViewSpot viewSpot) {
        if(selectedViewSpotList.contains(viewSpot)) {
            
        } else {
            viewSpot.is_select = 1;
            selectedViewSpotList.add(viewSpot);

            selectViewSpotFragment.selectedViewSpotList = selectedViewSpotList;
            selectViewSpotFragment.adapter.notifyDataSetChanged();
            allViewSpotFragment.adapter.notifyDataSetChanged();
        }
    }
}
