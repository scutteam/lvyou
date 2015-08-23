package com.scutteam.lvyou.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.adapter.MyPlanAdapter;
import com.scutteam.lvyou.application.LvYouApplication;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.Plan;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView.IXListViewListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by liujie on 15/7/22.
 */
public class MyPlanFragment extends Fragment implements IXListViewListener {

    private View view;
    private XListView mListView;
    private MyPlanAdapter adapter;
    private Context mContext;
    private ArrayList<Plan> plans;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_plan, null);
        mContext = LvYouApplication.getInstance();
        initView();
        return view;
    }

    private void initView() {
        mListView = (XListView) view.findViewById(R.id.my_plan_listView);
        mListView.setPullLoadEnable(false);
        getMyPlans();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    public void getMyPlans() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        client.post(Constants.URL + "/user/mobilelogin.info.do",params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i("liujie", response.toString());
                        if(response.optInt("code") == 0){
                            doGetMyPlans(response.optJSONObject("data").optLong("id"));
                        }else{
                            Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.i("liujie", errorResponse.toString());
                    }
                });
    }

    private void doGetMyPlans(long uid){
        Log.i("user id", uid + "");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("sessionid", LvYouApplication.getSessionId());
        params.put("kw.custId", uid);
        client.post(Constants.URL + "/user/trip.page_list.json",
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.i("liujie", response.toString());
                        if (0 == response.optInt("code")) {
                            showPlans(response.optJSONArray("data"));
                        } else {
                            Toast.makeText(mContext, response.optString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.i("liujie", responseString.toString());
                        Toast.makeText(mContext, "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showPlans(JSONArray jsonArray){
        plans = (ArrayList<Plan>)Plan.fromJson(jsonArray);
        adapter = new MyPlanAdapter(LvYouApplication.getInstance(), plans);
        mListView.setAdapter(adapter);
    }
}
