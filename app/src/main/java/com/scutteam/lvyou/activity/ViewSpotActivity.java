package com.scutteam.lvyou.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.adapter.ViewSpotAdapter;
import com.scutteam.lvyou.fragment.AllViewSpotFragment;
import com.scutteam.lvyou.fragment.RecommendViewSpotFragment;
import com.scutteam.lvyou.fragment.SelectViewSpotFragment;
import com.scutteam.lvyou.interfaces.ViewSpotListener;
import com.scutteam.lvyou.model.ViewSpot;
import com.scutteam.lvyou.widget.com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class ViewSpotActivity extends FragmentActivity implements View.OnClickListener, ViewSpotListener {

    private ImageView mIvBack;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private AllViewSpotFragment allViewSpotFragment;
    private SelectViewSpotFragment selectViewSpotFragment;
    private RecommendViewSpotFragment recommendViewSpotFragment;
    private ArrayList<ViewSpot> viewSpotList= new ArrayList<ViewSpot>();
    private ArrayList<ViewSpot> selectedViewSpotList= new ArrayList<ViewSpot>();
    private FragmentPagerAdapter adapter;
    String[] title = { "默认", "已选择", "推荐计划" };
    
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
        viewSpotList = (ArrayList<ViewSpot>) getIntent().getSerializableExtra("viewSpot");
    }
    
    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_sliding_tab_strip);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
    }
    
    public void initFragment() {
        allViewSpotFragment = new AllViewSpotFragment();
        allViewSpotFragment.viewSpotList = viewSpotList;
        allViewSpotFragment.adapter = new ViewSpotAdapter(ViewSpotActivity.this,viewSpotList);
        selectViewSpotFragment = new SelectViewSpotFragment();
        selectViewSpotFragment.selectedViewSpotList = selectedViewSpotList;
        selectViewSpotFragment.adapter = new ViewSpotAdapter(ViewSpotActivity.this,selectedViewSpotList);
        recommendViewSpotFragment = new RecommendViewSpotFragment();

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
        mPagerSlidingTabStrip.setTextSize(30);
        mPagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.image_color));
        mPagerSlidingTabStrip.setUnderlineColorResource(R.color.default_bg);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    
    public void initListener() {
        mIvBack.setOnClickListener(this);
        
        allViewSpotFragment.adapter.setListener(this);
        selectViewSpotFragment.adapter.setListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                
                break;
        }
    }

    @Override
    public void WhenViewSpotSelectIconClick(ViewSpot viewSpot) {
        if(viewSpot.is_select == 0) {
            viewSpot.is_select = 1;
            selectedViewSpotList.add(viewSpot);
        } else {
            viewSpot.is_select = 0;
            selectedViewSpotList.remove(viewSpot);
        }
        selectViewSpotFragment.selectedViewSpotList = selectedViewSpotList;
        selectViewSpotFragment.adapter.notifyDataSetChanged();
        allViewSpotFragment.adapter.notifyDataSetChanged();
    }
}
