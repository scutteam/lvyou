package com.scutteam.lvyou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scutteam.lvyou.R;

/**
 * Created by admin on 15/8/3.
 */
public class RecommendViewSpotFragment extends Fragment {

    private View view;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.recomment_view_post_fragment,null);
        }
        
        return view;
    }
}
