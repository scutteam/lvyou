package com.scutteam.lvyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.activity.ViewSpotDetailActivity;
import com.scutteam.lvyou.adapter.ViewSpotAdapter;
import com.scutteam.lvyou.constant.Constants;
import com.scutteam.lvyou.model.ViewSpot;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;

import java.util.ArrayList;

/**
 * Created by admin on 15/8/3.
 */
public class AllViewSpotFragment extends Fragment {
    
    private XListView listView;
    public ViewSpotAdapter adapter;
    private View view;
    public int selectNum;
    public int limitNum;
    public ArrayList<ViewSpot>viewSpotList = new ArrayList<ViewSpot>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.all_view_spot_fragment,null);

            if(getArguments() != null) {
                viewSpotList = (ArrayList<ViewSpot>) getArguments().getSerializable("viewSpot");
            }
            
            initView();
            initAdapter();
        }
        
        return view;
    }
    
    public void initView() {
        listView = (XListView) view.findViewById(R.id.listView);
        listView.setPullRefreshEnable(false);
        listView.setPullLoadEnable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewSpot viewSpot = viewSpotList.get(position - listView.getHeaderViewsCount());
                Intent intent = new Intent();
                intent.setClass(getActivity(), ViewSpotDetailActivity.class);
                intent.putExtra("view_spot_id",viewSpot.view_spot_id);
                intent.putExtra("selectNum",selectNum);
                intent.putExtra("limitNum",limitNum);
                startActivityForResult(intent, Constants.REQUEST_GET_VIEW_SPOT_DETAIL);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode == Constants.RESULT_GET_VIEW_SPOT_DETAIL) {
            long view_spot_id = data.getLongExtra("view_spot_id",0L);

            for(int i = 0 ; i < viewSpotList.size(); i++) {
                if(viewSpotList.get(i).view_spot_id == view_spot_id) {
                    adapter.listener.WhenViewSpotSelectIconClickSetFocus(viewSpotList.get(i));
                    break;
                } 
            }
        }
    }

    public void initAdapter() {
        listView.setAdapter(adapter);
    }
    
    
}
