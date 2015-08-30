package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.util.SpUtils;
import com.scutteam.lvyou.widget.ScrollableViewPager;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity implements  View.OnClickListener{

    private ScrollableViewPager viewPager;
    private IntroPagerAdapter adapter;
    private ImageButton commitBtn;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = WelcomeActivity.this;

        initData();
        initView();
        initListener();
    }

    private void initData() {
    }

    private void initView() {
        viewPager = (ScrollableViewPager) findViewById(R.id.welcome_viewpager);
        commitBtn = (ImageButton)findViewById(R.id.welcome_ok);

        /** 展示页所用图片的 ID 们 */
        List<Integer> imageResourceIds = new ArrayList<>();

        imageResourceIds.add(R.drawable.welcome_page_1);
        imageResourceIds.add(R.drawable.welcome_page_2);
        imageResourceIds.add(R.drawable.welcome_page_3);

        adapter = new IntroPagerAdapter(imageResourceIds);

        viewPager.setAdapter(adapter);
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /** 标记跳转按钮展现状态 */
            boolean isShowUp = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == adapter.getFinalPosition()) {
                    commitBtn.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.FadeInUp).duration(500).playOn(commitBtn);
                    isShowUp = true;
                } else if (isShowUp) {
                    commitBtn.setVisibility(View.INVISIBLE);
                    YoYo.with(Techniques.FadeOutDown).duration(500).playOn(commitBtn);
                    isShowUp = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        commitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.welcome_ok:
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                SpUtils sp = new SpUtils(this, Constants.Sp.PRE_NAME);
                sp.setValue(R.string.sp_first_launch, false);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }


    private class IntroPagerAdapter extends PagerAdapter {

    List<Integer> imageResourceIds;

    public IntroPagerAdapter(List<Integer> imageResourceIds) {
        this.imageResourceIds = imageResourceIds;
    }

    @Override
    public int getCount() {
        return imageResourceIds.size();
    }

    public int getFinalPosition() {
        return getCount() - 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView = new ImageView(container.getContext());

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        imageView.setImageResource(imageResourceIds.get(position));

        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

}
