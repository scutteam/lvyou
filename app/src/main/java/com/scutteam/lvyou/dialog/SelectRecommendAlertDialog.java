package com.scutteam.lvyou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.scutteam.lvyou.R;

/**
 * Created by admin on 15/8/19.
 */
public class SelectRecommendAlertDialog extends Dialog {

    private Context mContext;
    public TextView mTvYes;
    public TextView mTvNo;
    
    public SelectRecommendAlertDialog(Context context) {
        super(context);
    }

    public SelectRecommendAlertDialog(Context context, int theme) {
        super(context, theme);

        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.select_recommend_alert_dialog);
        
        initView();
    }
    
    public void initView() {
        mTvYes = (TextView) findViewById(R.id.tv_yes);
        mTvNo = (TextView) findViewById(R.id.tv_no);
    }
}
