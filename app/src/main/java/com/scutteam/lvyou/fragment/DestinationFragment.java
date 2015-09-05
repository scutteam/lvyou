package com.scutteam.lvyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scutteam.lvyou.R;
import com.scutteam.lvyou.activity.MakeJourneyActivity;
import com.scutteam.lvyou.activity.SimpleHUD.PairProgressHUD;
import com.scutteam.lvyou.adapter.DestinationItemAdapter;
import com.scutteam.lvyou.adapter.SelectDestinationTypeAdapter;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.Destination;
import com.scutteam.lvyou.model.LvYouTheme;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 15/7/22.
 */
public class DestinationFragment extends Fragment implements XListView.IXListViewListener {

    private View view;
    public List<LvYouTheme> themeList = new ArrayList<LvYouTheme>();
    public List<Destination> destinationList = new ArrayList<Destination>();//全部Destination
    public List<Destination> newDestinationList = new ArrayList<Destination>();//加载更多的Destination
    public static DestinationFragment instance;
    private XListView mListView;
    private DestinationItemAdapter adapter;
    private View mHeadView;
    private RelativeLayout mRlSelectLayout;
    private ListView mSelectListView;
    private TextView mTvSelect;
    private ImageView mIvArrow;
    private View barrerView;
    private SelectDestinationTypeAdapter selectAdapter;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            switch (msg.what) {
                case LOAD_THEME_DATA_SUCCESS:
                    if(selectAdapter == null) {
                        initHeadView();
                        initListViewData(selected_theme_id);    
                    } else {
                        for(int i = 0 ; i < themeList.size();i++) {
                            if(themeList.get(i).theme_id.equals(selected_theme_id)) {
                                themeList.get(i).is_select = true;
                            }
                        }
                        selectAdapter.notifyDataSetChanged();
                        initListViewData(selected_theme_id);
                    }
                    break;
                case LOAD_LISTVIEW_DATA_SUCCESS:
                    if(adapter == null) {
                        initAdapter();
                    } else {
                        adapter.refreshWithDestinationList(destinationList);
                    }
                    mListView.stopRefresh();
                    PairProgressHUD.dismiss();
                    break;
                case LOAD_MORE_LISTVIEW_DATA_SUCCESS:
                    destinationList.addAll(newDestinationList);
                    adapter.loadMoreWithDestinationList(newDestinationList);
                    newDestinationList.clear();
                    mListView.stopLoadMore();
                    break;
            }
        }
    };
    private int current_page = 1;
    private int total_page = 1;
    
    public static final int LOAD_THEME_DATA_SUCCESS = 300000;
    public static final int LOAD_LISTVIEW_DATA_SUCCESS = 300001;
    public static final int LOAD_MORE_LISTVIEW_DATA_SUCCESS = 300002;
    public Long selected_theme_id = 0L;
    
    public static DestinationFragment getInstance() {
        if(instance == null) {
            instance = new DestinationFragment();
        }
        return instance;
    }
    
    public void setSelectTitle(String title) {
        mTvSelect.setText(title);
    }

    public void refreshSelectedListView(LvYouTheme theme) {
        for(int i = 0;  i < themeList.size() ; i++) {
            themeList.get(i).is_select = false;
            if(themeList.get(i).theme_id.equals(theme.theme_id)) {
                themeList.get(i).is_select = true;
            } 
        }
        selectAdapter.notifyDataSetChanged();
    }
    
    public void refreshListView(LvYouTheme theme) {
        initListViewData(theme.theme_id);
    }

    public void initHeadView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_destination_head_view,null);
        mRlSelectLayout = (RelativeLayout) mHeadView.findViewById(R.id.rl_select_layout);
        mTvSelect = (TextView) mHeadView.findViewById(R.id.tv_select);
        mIvArrow = (ImageView) mHeadView.findViewById(R.id.iv_arrow);

        if(selected_theme_id != 0L) {
            for(int i = 0 ; i < themeList.size(); i++) {
                if(themeList.get(i).theme_id.equals(selected_theme_id)) {
                    themeList.get(i).is_select = true;
                    mTvSelect.setText(themeList.get(i).title);
                }
            }
        }
        selectAdapter = new SelectDestinationTypeAdapter(themeList,getActivity());
        mSelectListView.setAdapter(selectAdapter);

        mListView.addHeaderView(mHeadView);

        mRlSelectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mSelectListView.getVisibility() == View.GONE) {
                mSelectListView.setVisibility(View.VISIBLE);
                barrerView.setVisibility(View.VISIBLE);
                mIvArrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_up));
            } else {
                mSelectListView.setVisibility(View.GONE);
                barrerView.setVisibility(View.GONE);
                mIvArrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_down));
            }
            }
        });

        barrerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectListView.setVisibility(View.GONE);
                barrerView.setVisibility(View.GONE);
                mIvArrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_down));
            }
        });

        mSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for(int i = 0 ; i < themeList.size(); i++) {
                if(i!=position) {
                    LvYouTheme theme = themeList.get(i);
                    theme.is_select = false;
                }
            }
            LvYouTheme theme = themeList.get(position);
            theme.is_select = true;


            mSelectListView.setVisibility(View.GONE);
            barrerView.setVisibility(View.GONE);
            mTvSelect.setText(theme.title);
            mIvArrow.setImageDrawable(getResources().getDrawable(R.mipmap.arrow_down));

            selectAdapter.notifyDataSetChanged();

            selected_theme_id = theme.theme_id;

            initListViewData(selected_theme_id);
            }
        });
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_destination,null);
            if(getArguments() != null) {
                selected_theme_id = getArguments().getLong("selected_theme_id");
            }

            initThemeData();
            initView();
            initListener();
        }
        return view;
    }
    
    public void initAdapter() {
        adapter = new DestinationItemAdapter(getActivity(),destinationList);
        mListView.setAdapter(adapter);
    }
    
    public void initThemeData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getActivity(), Constants.URL + "main/theme.list.json",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("liujie", response.toString());
                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        JSONArray dataArray = response.getJSONArray("data");
                        themeList = LvYouTheme.insertWithArray(dataArray);
                        
                        if(themeList!=null && themeList.size() > 0) {
                            handler.sendEmptyMessage(LOAD_THEME_DATA_SUCCESS);
                        }
                    } else {
                        Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_SHORT).show();
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
    
    public void initListViewData(Long id) {
        PairProgressHUD.showLoading(getActivity(),"请稍候");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if(id != 0L) {
            params.put("kw.themeId",id);
        }
        client.get(getActivity(),Constants.URL + "main/dest.page_list.json",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("liujie", response.toString());
                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        JSONObject dataObject = response.getJSONObject("data");
                        JSONArray itemArray = dataObject.getJSONArray("items");
                        destinationList = Destination.insertWithArray(itemArray);

                        current_page = dataObject.getInt("currentPage");
                        total_page = dataObject.getInt("totalPages");
                        
                        if(current_page == total_page) {
                            mListView.setPullLoadEnable(false);
                        } else {
                            mListView.setPullLoadEnable(true);
                        }
                        
                        if(destinationList != null && destinationList.size() > 0) {
                            //修改ui
                            handler.sendEmptyMessage(LOAD_LISTVIEW_DATA_SUCCESS);
                        }
                        
                    } else {
                        Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
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
        mSelectListView = (ListView) view.findViewById(R.id.selectlistView);
        mListView = (XListView) view.findViewById(R.id.listView);
        barrerView = view.findViewById(R.id.view);
    }
    
    public void initListener() {
        mListView.setXListViewListener(this);
        
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int new_position = position - 1 - 1;
                if(new_position >= 0) {
                    Destination destination = destinationList.get(new_position);
                    
                    Intent intent = new Intent();
                    intent.putExtra("destination_id",destination.destination_id);
                    intent.setClass(getActivity(), MakeJourneyActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoadMore() {
        loadMore(selected_theme_id);
    }
    
    public void refresh() {
        initThemeData();
    }
    
    public void loadMore(Long theme_id) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        if(theme_id != 0L) {
            params.put("kw.themId",theme_id);
        }
        params.put("pr.page",current_page + 1);
        client.get(getActivity(),Constants.URL + "main/dest.page_list.json",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    int code = response.getInt("code");
                    if(code == 0) {
                        JSONObject dataObject = response.getJSONObject("data");
                        JSONArray itemArray = dataObject.getJSONArray("items");
                        newDestinationList = Destination.insertWithArray(itemArray);

                        current_page = dataObject.getInt("currentPage");
                        total_page = dataObject.getInt("totalPages");

                        if(current_page == total_page) {
                            mListView.setPullLoadEnable(false);
                        } else {
                            mListView.setPullLoadEnable(true);
                        }

                        if(newDestinationList != null && newDestinationList.size() > 0) {
                            //修改ui
                            handler.sendEmptyMessage(LOAD_MORE_LISTVIEW_DATA_SUCCESS);
                        }

                    } else {
                        Toast.makeText(getActivity(),response.getString("msg"),Toast.LENGTH_SHORT).show();
                    }
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
}
