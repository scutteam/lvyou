package com.scutteam.lvyou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scutteam.lvyou.R;

public class AboutAppFragment extends Fragment {
    private View view;
    private static AboutAppFragment aboutAppFragment;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_app,null);
        return view;
    }
    
    public static AboutAppFragment getInstance() {
        if(aboutAppFragment == null) {
            aboutAppFragment = new AboutAppFragment();
        }
        return aboutAppFragment;
    } 
    
}
