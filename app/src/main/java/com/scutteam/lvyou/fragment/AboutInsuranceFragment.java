package com.scutteam.lvyou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scutteam.lvyou.R;

/**
 * Created by admin on 15/7/25.
 */
public class AboutInsuranceFragment extends Fragment {
    private View view;
    private static AboutInsuranceFragment aboutInsuranceFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_insurance,null);
        return view;
    }

    public static AboutInsuranceFragment getInstance() {
        if(aboutInsuranceFragment == null) {
            aboutInsuranceFragment = new AboutInsuranceFragment();
        }
        return aboutInsuranceFragment;
    }
}
