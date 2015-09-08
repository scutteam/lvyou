package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
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
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.Comment;
import com.scutteam.lvyou.model.Destination;
import com.scutteam.lvyou.model.LvYouDest;
import com.scutteam.lvyou.util.DensityUtil;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DestinationDetailActivity extends Activity implements XListView.IXListViewListener, View.OnClickListener {

    private Long destination_id = 0L;
    private  String[] top_pic_list;
    private String top_pic;
    private String long_intro;
    private List<Comment> commentList = new ArrayList<Comment>();
    private List<Comment> newCommentList = new ArrayList<Comment>();
    private int current_page;
    private int total_page;
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
                    if (current_page == total_page) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }
                    mTvCommentCount.setText("用户评论（" + total_items + "）");
                    initAdapter();

                    break;
                case LOAD_MORE_COMMENT_SUCCESS:
                    if (current_page == total_page) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }
                    adapter.loadMoreWithCommentList(newCommentList);
                    listView.stopLoadMore();
                    break;
            }
        }
    };
    public View mHeadView;
    public Destination destination;
    public static final int LOAD_COMMENT_SUCCESS = 88888;
    public static final int LOAD_MORE_COMMENT_SUCCESS = 88889;
    public TextView mTvCurrentPage;
    public TextView mTvTotalPage;
    public ViewPager viewPager;
    public List<ImageView> imageViews = new ArrayList<ImageView>();

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
        adapter = new CommentAdapter(DestinationDetailActivity.this, commentList);

        listView.setAdapter(adapter);
    }

    public void initData() {
        destination_id = getIntent().getLongExtra("destination_id", 0L);
        top_pic = getIntent().getStringExtra("top_pic");
        long_intro = getIntent().getStringExtra("long_intro");
        
        top_pic_list = top_pic.split(";");

        destination = Destination.findDestinationById(destination_id);

        initCommentData();
    }

    public void initCommentData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("kw.destId", destination_id);
        client.get(DestinationDetailActivity.this, Constants.URL + "main/comment.dest_page_list.json", params, new JsonHttpResponseHandler() {
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

    public void initView() {
        listView = (XListView) findViewById(R.id.listView);
        listView.setPullRefreshEnable(false);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
    }


    public void initHeadView() {
        mHeadView = LayoutInflater.from(DestinationDetailActivity.this).inflate(R.layout.destination_detail_head_layout, null);
        mTvIntro = (TextView) mHeadView.findViewById(R.id.tv_intro);
        mTvDestinationName = (TextView) mHeadView.findViewById(R.id.tv_destination_name);
        mRBDestinationStar = (RatingBar) mHeadView.findViewById(R.id.rb_destination_star);
        mTvDestinationScore = (TextView) mHeadView.findViewById(R.id.tv_destination_score);
        mTvCurrentPage = (TextView) mHeadView.findViewById(R.id.tv_current_page);
        mTvTotalPage = (TextView) mHeadView.findViewById(R.id.tv_total_page);
        viewPager = (ViewPager) mHeadView.findViewById(R.id.viewPager);
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.width = DensityUtil.getScreenWidthPx(LvYouApplication.getInstance());
        params.height = params.width * Constants.Config.IMAGE_HEIGHT / Constants.Config.IMAGE_WIDTH;
        viewPager.setLayoutParams(params);
        mTvCommentCount = (TextView) mHeadView.findViewById(R.id.tv_comment_count);

        listView.addHeaderView(mHeadView);
        mTvIntro.setText(Html.fromHtml(long_intro));
        if (null != destination) {
            mTvTitle.setText(destination.title);
            mTvDestinationName.setText(destination.title);
            mRBDestinationStar.setRating(Float.parseFloat(destination.score.toString()));
            mTvDestinationScore.setText(destination.score.toString());
        }else{
            LvYouDest lvYouDest = LvYouDest.findDestById(destination_id);
            if(null != lvYouDest){
                mTvTitle.setText(lvYouDest.title);
                mTvDestinationName.setText(lvYouDest.title);
                mRBDestinationStar.setRating((float)lvYouDest.score);
                mTvDestinationScore.setText(lvYouDest.score + "");
            }
        }
        mTvTotalPage.setText(top_pic_list.length + "");
    }

    public void initListener() {
        mIvBack.setOnClickListener(this);
        listView.setXListViewListener(this);

        for (int i = 0; i < top_pic_list.length; i++) {
            ImageView imageView = new ImageView(DestinationDetailActivity.this);
            ImageLoader.getInstance().displayImage(Constants.IMAGE_URL + top_pic_list[i], imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setAdjustViewBounds(true);
            imageViews.add(imageView);
        }

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return top_pic_list.length;
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
                mTvCurrentPage.setText((i + 1) + "");
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
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("kw.destId", destination_id);
        params.put("pr.page", current_page);
        client.get(DestinationDetailActivity.this, Constants.URL + "main/comment.dest_page_list.json", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    newCommentList = Comment.insertWithArray(dataObject.getJSONArray("items"));
                    total_page = dataObject.getInt("totalPages");
                    current_page++;

                    handler.sendEmptyMessage(LOAD_MORE_COMMENT_SUCCESS);

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
