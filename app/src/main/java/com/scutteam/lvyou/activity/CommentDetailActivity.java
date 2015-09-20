package com.scutteam.lvyou.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.adapter.CommentAdapter;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.Comment;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentDetailActivity extends Activity implements XListView.IXListViewListener {

    private XListView listView;
    private CommentAdapter adapter;
    private long view_spot_id;
    private int current_page;
    private boolean lastPage;
    private ImageView mIvBack;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case LOAD_COMMENT_SUCCESS:
                    if(lastPage) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }
                    if(adapter == null) {
                        initAdapter();     
                    } else {
                        adapter.reloadWithCommentList(commentList);
                        listView.stopRefresh();
                    }
                   
                    break;
                case LOAD_MORE_COMMENT_SUCCESS:
                    if(lastPage) {
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
    private List<Comment> commentList = new ArrayList<Comment>();
    private List<Comment> newCommentList = new ArrayList<Comment>();
    public static final int LOAD_COMMENT_SUCCESS = 88888;
    public static final int LOAD_MORE_COMMENT_SUCCESS = 88889;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        
        initData();
        initView();
        initCommentData();
        initListener();
    }
    
    public void initAdapter() {
        adapter = new CommentAdapter(CommentDetailActivity.this,commentList);

        listView.setAdapter(adapter);
    }
    
    public void initData() {
        view_spot_id = getIntent().getLongExtra("id",0L);
    }

    public void initListener() {
        listView.setXListViewListener(this);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    public void loadMore() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("kw.destId",view_spot_id);
        params.put("pr.page",current_page + 1);
        client.get(CommentDetailActivity.this, Constants.URL + "main/comment.dest_page_list.json",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    newCommentList = Comment.insertWithArray(dataObject.getJSONArray("items"));
                    lastPage = dataObject.getBoolean("lastPage");
                    current_page = dataObject.getInt("currentPage");

                    handler.sendEmptyMessage(LOAD_MORE_COMMENT_SUCCESS);

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

    public void initCommentData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("kw.viewspotId",view_spot_id);
        client.get(CommentDetailActivity.this, Constants.URL + "main/comment.vs_page_list.json",params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONObject dataObject = response.getJSONObject("data");
                    commentList = Comment.insertWithArray(dataObject.getJSONArray("items"));
                    current_page = dataObject.getInt("currentPage");
                    lastPage = dataObject.getBoolean("lastPage");
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
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        listView = (XListView) findViewById(R.id.listView);
        listView.setPullRefreshEnable(true);
    }

    @Override
    public void onRefresh() {
        initCommentData();
    }

    @Override
    public void onLoadMore() {
        loadMore();
    }
}
