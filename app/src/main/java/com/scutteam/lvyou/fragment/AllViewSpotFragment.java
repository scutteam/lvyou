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
public class AllViewSpotFragment extends Fragment {
    
    private XListView listView;
    public ViewSpotAdapter adapter;
    private View view;
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
    }
    
    public void initAdapter() {
        listView.setAdapter(adapter);
    }
    
    
}
