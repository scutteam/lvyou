package com.scutteam.lvyou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scutteam.lvyou.R;
import com.scutteam.lvyou.adapter.ViewSpotAdapter;
import com.scutteam.lvyou.model.ViewSpot;
import com.scutteam.lvyou.widget.me.maxwin.view.XListView;

import java.util.ArrayList;

/**
 * Created by admin on 15/8/3.
 */
public class SelectViewSpotFragment extends Fragment{

    private View view;
    private XListView listView;
    public ViewSpotAdapter adapter;
    public ArrayList<ViewSpot> selectedViewSpotList = new ArrayList<ViewSpot>();
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.select_view_spot_fragment,null);

            initView();
            initAdapter();
        }
        
        return view;
    }
    
    public void initView() {
        listView = (XListView) view.findViewById(R.id.listView);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
    }
    
    public void initAdapter() {
        listView.setAdapter(adapter);
    }
}
