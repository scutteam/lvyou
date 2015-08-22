package com.scutteam.lvyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.activity.RecommendViewSpotDetailActivity;
import com.scutteam.lvyou.adapter.RecommendViewSpotAdapter;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.dialog.SelectRecommendAlertDialog;
import com.scutteam.lvyou.interfaces.RecommendViewSpotItemListener;
import com.scutteam.lvyou.model.Recommendtrip;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/8/3.
 */
public class RecommendViewSpotFragment extends Fragment implements RecommendViewSpotItemListener {

    private View view;
    private XListView mListView;
    private Long destination_id;
    public RecommendViewSpotAdapter adapter;
    public SelectRecommendAlertDialog dialog;
    private List<Recommendtrip> recommendtripList = new ArrayList<Recommendtrip>();
    private static final int LOAD_RECOMMEND_DATA_SUCCESS = 888888;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case LOAD_RECOMMEND_DATA_SUCCESS:
                    initAdapter();
                    mListView.setAdapter(adapter);
                    break;
            }
        }
    };
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.recomment_view_post_fragment,null);

            destination_id = getArguments().getLong("id",0L);
            initData();
            initView();
            initListener();
        }
        return view;
    }
    
    public void initAdapter() {
        adapter = new RecommendViewSpotAdapter(getActivity(),recommendtripList);
        adapter.setListener(this);
    }
    
    public void initData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("kw.destId",destination_id);
        client.get(getActivity(), Constants.URL + "main/recommendtrip.list.json",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    recommendtripList = Recommendtrip.insertWithArray(dataArray);
                    
                    mHandler.sendEmptyMessage(LOAD_RECOMMEND_DATA_SUCCESS);
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
    
    public void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Recommendtrip recommendtrip = recommendtripList.get(position - mListView.getHeaderViewsCount());
                Intent intent = new Intent();
                intent.putExtra("RecommendTrip",(Serializable)recommendtrip);
                intent.setClass(getActivity(), RecommendViewSpotDetailActivity.class);
                startActivity(intent);
            }
        });
    }
    
    public void initView() {
        mListView = (XListView) view.findViewById(R.id.listView);
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(false);
    }

    @Override
    public void recommendViewSpotItemClick(final Recommendtrip trip) {
        dialog = new SelectRecommendAlertDialog(getActivity(),R.style.TransparentDialog);
        dialog.show();
        dialog.mTvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("trip",(Serializable)trip);
                getActivity().setResult(Constants.RESULT_SELECT_RECOMMEND_TRIP,intent);

                getActivity().finish();
            }
        });
        dialog.mTvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
