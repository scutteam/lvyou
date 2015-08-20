package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.adapter.CommentAdapter;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.Comment;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewSpotDetailActivity extends Activity implements XListView.IXListViewListener, View.OnClickListener {

    private Long view_spot_id = 0L;
//    private ArrayList<String>viewSpotStringList = new ArrayList<String>();
    private String intro;
    private List<Comment>commentList = new ArrayList<Comment>();
//    private List<Comment>newCommentList = new ArrayList<Comment>();
    private int current_page;
    private int total_page;
    
    private String play;
    private String play_duration;
    private int price;
    private double score;
    private String title;
    private String [] viewSpotStringList;
    
    private int total_items;
    private XListView listView;
    private TextView mTvTitle;
    private TextView mTvIntro;
    private TextView mTvDestinationName;
    private RatingBar mRBDestinationStar;
    private TextView mTvDestinationScore;
    private TextView mTvCommentCount;
    private ImageView mIvBack;
    private CommentAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case LOAD_COMMENT_SUCCESS:
                    mTvCommentCount.setText("用户评论("+total_items+")");
                    initAdapter();

                    break;
//                case LOAD_MORE_COMMENT_SUCCESS:
//                    if(current_page == total_page) {
//                        listView.setPullLoadEnable(false);
//                    } else {
//                        listView.setPullLoadEnable(true);
//                    }
//                    adapter.loadMoreWithCommentList(newCommentList);
//                    listView.stopLoadMore();
//                    break;
                case LOAD_HEAD_DATA_SUCCESS:
                    setHeaderUI();
                    break;
            }
        }
    };
    public View mHeadView;
    public static final int LOAD_COMMENT_SUCCESS = 88888;
    public static final int LOAD_MORE_COMMENT_SUCCESS = 88889;
    public static final int LOAD_HEAD_DATA_SUCCESS = 88890;
    public TextView mTvCurrentPage;
    public TextView mTvTotalPage;
    public ViewPager viewPager;
    public List<ImageView>imageViews = new ArrayList<ImageView>();
    
    public TextView mTvAddViewSpot;
    public TextView mTvType;
    public TextView mTvPlayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_spot_detail);

        initData();
        initView();
        initHeadView();
        initListener();
    }

    public void initAdapter() {
        adapter = new CommentAdapter(ViewSpotDetailActivity.this,commentList);

        listView.setAdapter(adapter);
    }

    public void initData() {
        view_spot_id = getIntent().getLongExtra("view_spot_id",0L);
        
        initHeadData();
        initCommentData();
    }
    
    public void setHeaderUI() {
        mTvType.setText("景点类型:后台还没有数据");
        mTvPlayTime.setText("游玩时间:"+play_duration);

        mTvTitle.setText(title);
        mTvDestinationName.setText(title);
        mTvIntro.setText(intro);
        mRBDestinationStar.setRating((float)score);
        mTvDestinationScore.setText(score+"");
        mTvTotalPage.setText(viewSpotStringList.length+"");


        for(int i = 0 ; i < viewSpotStringList.length; i++) {
            ImageView imageView = new ImageView(ViewSpotDetailActivity.this);
            ImageLoader.getInstance().displayImage(Constants.IMAGE_URL + viewSpotStringList[i],imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(imageView);
        }

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewSpotStringList.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(imageViews.get(position));
                return imageViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(imageViews.get(position));
            }
        };


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                mTvCurrentPage.setText((i+1)+"");
                viewPager.setCurrentItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        viewPager.setAdapter(pagerAdapter);
    }
    
    public void initHeadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id",view_spot_id);
        client.get(ViewSpotDetailActivity.this,Constants.URL + "main/viewspot.detail.json",params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    int code = response.getInt("code");
                   JSONObject dataObject = response.getJSONObject("data");
                    if(code == 0) {
                        intro = dataObject.getString("intro");
                        play = dataObject.getString("play");
                        play_duration = dataObject.getString("playDuration");
                        price = dataObject.getInt("price");
                        score = dataObject.getDouble("score");
                        title = dataObject.getString("title");
                        viewSpotStringList = dataObject.getString("topPic").split(";");
                        
                        handler.sendEmptyMessage(LOAD_HEAD_DATA_SUCCESS);
                    } else {
                        Toast.makeText(ViewSpotDetailActivity.this,response.getString("msg"),Toast.LENGTH_SHORT).show();
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

    public void initCommentData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("kw.viewspotId",view_spot_id);
        client.get(ViewSpotDetailActivity.this, Constants.URL + "main/comment.vs_page_list.json",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    commentList = Comment.insertWithArray(dataObject.getJSONArray("items"));
                    current_page = dataObject.getInt("currentPage");
                    total_page = dataObject.getInt("totalPages");
                    total_items = dataObject.getInt("totalItems");
                    handler.sendEmptyMessage(LOAD_COMMENT_SUCCESS);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void initView() {
        listView = (XListView) findViewById(R.id.listView);
        listView.setPullRefreshEnable(false);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvAddViewSpot = (TextView) findViewById(R.id.tv_add_view_spot);
    }


    public void initHeadView() {
        mHeadView = LayoutInflater.from(ViewSpotDetailActivity.this).inflate(R.layout.view_spot_detail_head_layout,null);
        mTvIntro = (TextView) mHeadView.findViewById(R.id.tv_intro);
        mTvDestinationName = (TextView) mHeadView.findViewById(R.id.tv_destination_name);
        mRBDestinationStar = (RatingBar) mHeadView.findViewById(R.id.rb_destination_star);
        mTvDestinationScore = (TextView) mHeadView.findViewById(R.id.tv_destination_score);
        mTvCurrentPage = (TextView) mHeadView.findViewById(R.id.tv_current_page);
        mTvTotalPage = (TextView) mHeadView.findViewById(R.id.tv_total_page);
        viewPager = (ViewPager) mHeadView.findViewById(R.id.viewPager);
        mTvCommentCount = (TextView) mHeadView.findViewById(R.id.tv_comment_count);
        mTvType = (TextView) mHeadView.findViewById(R.id.tv_type);
        mTvPlayTime = (TextView) mHeadView.findViewById(R.id.tv_play_time);

        listView.addHeaderView(mHeadView);

        mTvCommentCount.setOnClickListener(this);
    }

    public void initListener() {
        mIvBack.setOnClickListener(this);
        listView.setXListViewListener(this);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
//        loadMore();
    }

//    public void loadMore() {
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
//        params.put("kw.destId",view_spot_id);
//        params.put("pr.page",current_page);
//        client.get(ViewSpotDetailActivity.this, Constants.URL + "main/comment.dest_page_list.json",params,new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//
//                try {
//                    JSONObject dataObject = response.getJSONObject("data");
//                    newCommentList = Comment.insertWithArray(dataObject.getJSONArray("items"));
//                    total_page = dataObject.getInt("totalPages");
//                    current_page ++;
//
//                    handler.sendEmptyMessage(LOAD_MORE_COMMENT_SUCCESS);
//
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_comment_count:
                Intent intent = new Intent();
                intent.putExtra("id",view_spot_id);
                intent.setClass(ViewSpotDetailActivity.this,CommentDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
