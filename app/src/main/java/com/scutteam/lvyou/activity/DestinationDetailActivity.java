package com.scutteam.lvyou.activity;

import android.app.Activity;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.adapter.CommentAdapter;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.Comment;
import com.scutteam.lvyou.model.Destination;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DestinationDetailActivity extends Activity implements XListView.IXListViewListener, View.OnClickListener {
    
    private Long destination_id = 0L;
    private ArrayList<String>viewSpotStringList = new ArrayList<String>();
    private String long_intro;
    private List<Comment>commentList = new ArrayList<Comment>();
    private int current_page;
    private int total_page;
    private XListView listView;
    private TextView mTvTitle;
    private TextView mTvIntro;
    private TextView mTvDestinationName;
    private RatingBar mRBDestinationStar;
    private TextView mTvDestinationScore;
    private ImageView mIvBack;
    private CommentAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case LOAD_COMMENT_SUCCESS:
//                    if(current_page == total_page) {
//                        listView.setPullLoadEnable(false);
//                    } else {
//                        listView.setPullLoadEnable(true);
//                    }
                    listView.setPullLoadEnable(true);
                    getFakeCommentData();
                    if(adapter == null) {
                        initAdapter();
                    } else {
                        adapter.loadMoreWithCommentList(commentList);
                    }
                    break;
            }
        }
    };
    public View mHeadView;
    public Destination destination;
    public static final int LOAD_COMMENT_SUCCESS = 88888;
    public TextView mTvCurrentPage;
    public TextView mTvTotalPage;
    public ViewPager viewPager;
    public List<ImageView>imageViews = new ArrayList<ImageView>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_detail);

        initData();
        initView();
        initHeadView();
        initListener();
    }
    
    public void initAdapter() {
        adapter = new CommentAdapter(DestinationDetailActivity.this,commentList);
        
        listView.setAdapter(adapter);
    }
   
    public void getFakeCommentData() {
        Comment comment = new Comment();
        comment.cust = "花开人醉";
        comment.face = Constants.IMAGE_URL + "default_face.jpg";
        comment.total_score = 4.5;
        comment.total_comment = "总体来说是挺不错的，我喜欢";
        comment.create_time = "1328007600000";

        Comment comment2 = new Comment();
        comment2.cust = "花开人醉2";
        comment2.face = Constants.IMAGE_URL + "default_face.jpg";
        comment2.total_score = 4.4;
        comment2.total_comment = "总体来说是挺不错的，我喜2";
        comment2.create_time = "132880600000";

        Comment comment3 = new Comment();
        comment3.cust = "花开人醉3";
        comment3.face = Constants.IMAGE_URL + "default_face.jpg";
        comment3.total_score = 4.5;
        comment3.total_comment = "总体来说是挺不错的，我喜3";
        comment3.create_time = "1329007600000";
        
        commentList.add(comment);
        commentList.add(comment2);
        commentList.add(comment3);
    }
    
    public void initData() {
        destination_id = getIntent().getLongExtra("destination_id",0L);
        viewSpotStringList = getIntent().getStringArrayListExtra("viewSpotList");
        long_intro = getIntent().getStringExtra("long_intro");

        destination = Destination.findDestinationById(destination_id);

        initCommentData();
    }
    
    public void initCommentData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("kw.destId",destination_id);
        client.get(DestinationDetailActivity.this, Constants.URL + "main/comment.dest_page_list.json",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    commentList = Comment.insertWithArray(dataObject.getJSONArray("items"));
                    current_page = dataObject.getInt("currentPage");
                    total_page = dataObject.getInt("totalPages");
                    
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
    }


    public void initHeadView() {
        mHeadView = LayoutInflater.from(DestinationDetailActivity.this).inflate(R.layout.destination_detail_head_layout,null);
        mTvIntro = (TextView) mHeadView.findViewById(R.id.tv_intro);
        mTvDestinationName = (TextView) mHeadView.findViewById(R.id.tv_destination_name);
        mRBDestinationStar = (RatingBar) mHeadView.findViewById(R.id.rb_destination_star);
        mTvDestinationScore = (TextView) mHeadView.findViewById(R.id.tv_destination_score);
        mTvCurrentPage = (TextView) mHeadView.findViewById(R.id.tv_current_page);
        mTvTotalPage = (TextView) mHeadView.findViewById(R.id.tv_total_page);
        viewPager = (ViewPager) mHeadView.findViewById(R.id.viewPager);
        
        listView.addHeaderView(mHeadView);

        mTvTitle.setText(destination.title);
        mTvDestinationName.setText(destination.title);
        mTvIntro.setText(long_intro);
        mRBDestinationStar.setRating(Float.parseFloat(destination.score.toString()));
        mTvDestinationScore.setText(destination.score.toString());
        mTvTotalPage.setText(viewSpotStringList.size()+"");

    }
    
    public void initListener() {
        mIvBack.setOnClickListener(this);
        listView.setXListViewListener(this);

        for(int i = 0 ; i < viewSpotStringList.size(); i++) {
            ImageView imageView = new ImageView(DestinationDetailActivity.this);
            ImageLoader.getInstance().displayImage(viewSpotStringList.get(i),imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViews.add(imageView);
        }
        
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return viewSpotStringList.size();
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

    @Override
    public void onRefresh() {
        
    }

    @Override
    public void onLoadMore() {
        loadMore();
    }
    
    public void loadMore() {
        handler.sendEmptyMessage(LOAD_COMMENT_SUCCESS);
        listView.stopLoadMore();
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
